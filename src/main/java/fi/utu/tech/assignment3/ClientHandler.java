package fi.utu.tech.assignment3;

import fi.utu.tech.assignment2.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread {

    // TODO: Toteuta asiakaspalvelija tänne
    private final Socket socket;
    private final ServerSocket serverSocket;

    public ClientHandler(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        System.out.println("Päästiin run metodiin");

        //Read Clients message
        try (InputStream is = socket.getInputStream()) {
            byte[] received = is.readAllBytes();
            String receivedMessage = new String(received, "UTF-8");
            System.out.print("Ground Control received: " + receivedMessage);
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally { // Closing socket
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
        System.out.println("Client disconnected");

        // Shutting down server
        //Server.stopServer(serverSocket);

    }

    
}
