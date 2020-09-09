package quiztastic.entries;

import quiztastic.entries.RunTUI;
import quiztastic.ui.Protocol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {

        int port = 2222;
        ServerSocket serverSocket = new ServerSocket(port);

        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getPort() + " connected.");
            Thread thread = new Thread(new Protocol(new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream(), true)));
            thread.start();
        }
    }
}
