import Util.ClientSocket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteProfessorRun {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private final Scanner scanner;
    private ClientSocket clientSocket;

    public ClienteProfessorRun(){ scanner = new Scanner(System.in);}

    public void start() throws IOException {
        clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ServerRun.PORT));
        System.out.println("Cliente conectado ao servidor em: " + SERVER_ADDRESS + ":" + ServerRun.PORT);
        optionListener();
    }

    private void optionListener() throws IOException {
        String option;
        try{
            String clienteTipo = "1";
            clientSocket.sendOption(clienteTipo);
            do {
                System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                option = scanner.nextLine();
                clientSocket.sendOption(option);
                switch (option) {

                    //Opção AbrirChamada
                    case "1":
                    //Opção FecharChamada
                    case "2":
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        String turmaID = scanner.nextLine();
                        clientSocket.sendOption(turmaID);
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        break;
                    //Opção voltar ao menu anterior
                    case "3":
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        option = "sair";
                        break;
                    default:
                        System.out.println("Mensagem do Servidor: " + clientSocket.getOption());
                        break;
                }
            } while (!option.equalsIgnoreCase("sair"));
        } finally {
            clientSocket.close();
        }
    }

    public static void main(String[] args) {
        try{
            ClienteProfessorRun client = new ClienteProfessorRun();
            client.start();
        } catch (IOException ex){
            System.out.println("Erro ao iniciar cliente: " + ex.getMessage());
        }
        System.out.println("Cliente finalizado.");
    }
}
