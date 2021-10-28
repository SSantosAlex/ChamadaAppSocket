import Model.ChamadaModel;
import Service.ChamadaService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerRun {
    public static final int PORT = 4000;
    List<ChamadaModel> chamadaLista = new ArrayList<>();

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta: " + PORT);
        ChamadaService.clientConnectionListener(serverSocket, chamadaLista);
    }

    public static void main(String[] args) {
        try{
            ServerRun server = new ServerRun();
            server.start();
        } catch (IOException ex){
            System.out.println("Erro ao iniciar servidor: " + ex.getMessage());
        }
        System.out.println("Servidor finalizado.");
    }
}
