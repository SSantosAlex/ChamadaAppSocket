package Service;

import Model.ChamadaModel;
import Util.ClientSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChamadaService {
    final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ChamadaService() {
    }

    //Escuta de novas conexões (estratégia de múltiplas threads)
    public static void clientConnectionListener(ServerSocket serverSocket, List<ChamadaModel> chamadaLista) throws IOException {
        while (true) {
            ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
            new Thread(() -> ChamadaService.clientOptionListener(clientSocket, chamadaLista)).start();
            System.out.print("Conexão estabelecida\n");
        }
    }

    //Método para abrir chamada em uma turma
    private static void attendanceListOpener(ClientSocket clientSocket, List<ChamadaModel> chamadaLista){
        clientSocket.sendOption("Digite o ID da Turma: ");

        List<String> matriculas = new ArrayList<>();
        ChamadaModel chamada = new ChamadaModel();
        chamada.setInicio(LocalDateTime.now());
        chamada.setTurmaID(clientSocket.getOption());
        chamada.setMatriculas(matriculas);

        //Trata lista de chamadas
        if (chamadaLista.size() <= 0) {
            chamadaLista.add(chamada);
            clientSocket.sendOption("Chamada aberta com sucesso em: " + chamada.getInicio().format(formatter) + " Turma: " + chamada.getTurmaID());
        }else if(chamadaLista.stream().noneMatch(c -> c.getTurmaID().equals(chamada.getTurmaID()))) {
            chamadaLista.add(chamada);
            clientSocket.sendOption("Chamada aberta com sucesso em: " + chamada.getInicio().format(formatter) + " Turma: " + chamada.getTurmaID());
        }else {
            clientSocket.sendOption("Erro: Chamada já aberta para esta turma");
        }
    }

    private static void attendanceListCloser(ClientSocket clientSocket, List<ChamadaModel> chamadaLista){
        clientSocket.sendOption("Digite o ID da turma a ser fechada: ");
        String turmaId = clientSocket.getOption();

        //Validar chamadas abertas
        if (chamadaLista.size() <= 0) {
            clientSocket.sendOption("Erro: Não existem chamadas abertas no sistema.");
        }else if(chamadaLista.stream().noneMatch(c -> c.getTurmaID().equals(turmaId))){
            clientSocket.sendOption("Erro: Turma inexistente");
        }else {
            for(ChamadaModel fecharChamada: chamadaLista){
                if(fecharChamada.getTurmaID().equals(turmaId)){
                    fecharChamada.setFinal(LocalDateTime.now());
                    clientSocket.sendOption("Chamada fechada com sucesso em: " + fecharChamada.getFinal().format(formatter)
                            + " Turma: " + fecharChamada.getTurmaID()
                            + " Lista de Presença: " + fecharChamada.getMatriculas());
                    chamadaLista.remove(fecharChamada);
                    break;
                }
            }
        }
    }

    //Escuta e tratamento de requisições de clientes
    public static void clientOptionListener(ClientSocket clientSocket, List<ChamadaModel> chamadaLista){
        boolean checkSair = false;
        boolean sair = false;


        try {
            String clienteTipo = clientSocket.getOption();
            switch (clienteTipo) {
                //Opção: Professor
                case "1":
                    do {
                        clientSocket.sendOption("Digite a opção desejada: AbrirChamada(1) || FecharChamada(2) || Sair(3) //Apenas números ");
                        String chamadaOpcoes = clientSocket.getOption();
                        System.out.println("Cliente: " + clientSocket.getRemoteSocketAddress() + " Opção escolhida: " + chamadaOpcoes);

                        switch (chamadaOpcoes) {

                            //Opção: Abrir chamada
                            case "1":
                                attendanceListOpener(clientSocket, chamadaLista);
                                break;

                            //Opção: Fechar chamada
                            case "2":
                                attendanceListCloser(clientSocket, chamadaLista);
                                break;

                            //Opção: Sair
                            case "3":
                                clientSocket.sendOption("Saindo...");
                                sair = true;
                                break;

                            default:
                                clientSocket.sendOption("Erro: Opção inválida");
                                break;
                        }
                    } while (!sair);
                    break;

                //Opção: Aluno
                case "2":
                    do {
                        clientSocket.sendOption("Digite a opção desejada: RegistrarPresença(1) || Sair(2) //Apenas números ");
                        String chamadaOpcoes = clientSocket.getOption();
                        System.out.println("Cliente: " + clientSocket.getRemoteSocketAddress() + " Opção escolhida: " + chamadaOpcoes);

                        switch (chamadaOpcoes) {

                            //Opção: Registrar Presença
                            case "1":
                                //Solicitação de matrícula
                                String data = LocalDateTime.now().format(formatter);
                                clientSocket.sendOption("Digite sua matrícula: ");
                                String matricula = clientSocket.getOption();
                                System.out.println("Opção recebida do cliente: " + clientSocket.getRemoteSocketAddress() + " " + matricula);

                                //Solicitação de Turma
                                clientSocket.sendOption("Digite ID da Turma: ");
                                String turmaID = clientSocket.getOption();
                                System.out.println("Opção recebida do cliente: " + clientSocket.getRemoteSocketAddress() + " " + turmaID);

                                //Trata turma e chamadas existentes
                                if (chamadaLista.stream().noneMatch(c -> c.getTurmaID().equals(turmaID))) {
                                    clientSocket.sendOption("Data: " + data + " Turma: 0");
                                } else {
                                    for (ChamadaModel item : chamadaLista) {
                                        if (item.getTurmaID().equals(turmaID)) {
                                            if (item.getMatriculas().stream().anyMatch(s -> s.equals(matricula))) {
                                                clientSocket.sendOption("Erro: Está matrícula já respondeu a chamada para a Turma: " + turmaID);
                                            } else {
                                                item.getMatriculas().add(matricula);
                                                clientSocket.sendOption("Data: " + data
                                                        + " Turma: " + item.getTurmaID()
                                                        + " Presença registrada com sucesso.");
                                            }
                                        }
                                    }
                                }
                                break;
                            //Opção: Sair
                            case "2":
                                clientSocket.sendOption("Saindo...");
                                sair = true;
                                break;

                            default:
                                clientSocket.sendOption("Erro: Opção inválida");
                                break;
                        }
                    }while (!sair);
                    break;

                default:
                    clientSocket.sendOption("Erro: Opção inválida");
                    break;
            }
        }finally {
            clientSocket.close();
        }
    }
}
