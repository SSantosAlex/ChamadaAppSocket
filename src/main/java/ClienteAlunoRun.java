import Util.ClientSocket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteAlunoRun {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private final Scanner scanner;
    private ClientSocket clientSocket;

    public ClienteAlunoRun(){
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ServerRun.PORT));
        System.out.println("Cliente conectado ao servidor em: " + SERVER_ADDRESS + ":" + ServerRun.PORT);
        optionListener();
    }

     private void optionListener() throws IOException {

        String clienteTipo;
        String matricula;
        String turmaID;
        boolean sair = false;
        String option;

        try{
            clienteTipo = "2";
            clientSocket.sendOption(clienteTipo);
            do {
                System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                option = scanner.nextLine();
                clientSocket.sendOption(option);
                switch (option) {

                    //Opção: Aluno
                    case "1":
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        matricula = scanner.nextLine();
                        clientSocket.sendOption(matricula);

                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        turmaID = scanner.nextLine();
                        clientSocket.sendOption(turmaID);
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        break;
                    //Opção voltar ao menu anterior
                    case "2":
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        option = "sair";
                        break;

                    default:
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        break;
                }
            }while (!option.equalsIgnoreCase("sair"));
        } finally {
            clientSocket.close();
        }
    }

    public static void main(String[] args) {
        try{
            ClienteAlunoRun client = new ClienteAlunoRun();
            client.start();
        } catch (IOException ex){
            System.out.println("Erro ao iniciar cliente: " + ex.getMessage());
        }
        System.out.println("Cliente finalizado.");
    }
}
