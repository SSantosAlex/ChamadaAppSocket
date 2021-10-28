package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Cliente " + socket.getRemoteSocketAddress() + " conectou com sucesso.");
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }

    public void close(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch(IOException ex) {
            System.out.println("Erro ao fechar socket: " + ex.getMessage());
        }
    }

    public String getOption(){
        try {
            return in.readLine();
        } catch (IOException ex){
            return null;
        }
    }

    public boolean sendOption(String option){
        out.println(option);
        return !out.checkError();
    }
}
