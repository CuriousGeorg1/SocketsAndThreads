package fi.utu.tech.assignment2;

import fi.utu.tech.assignment3.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static volatile boolean serverRun = true;

    public static void main(String[] args) {
        // Thread pool to handle client connections
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Kuunnellaan 1234");

            //Palvelimen käynnistys
            while (serverRun) {
                try {
                    // Accept new clients
                    Socket commSocket = serverSocket.accept();
                    // Start new Thread when connection made(currently one connection as it closes the server
                    threadPool.execute(new ClientHand(commSocket, serverSocket));
                } catch (IOException e) {
                    if (!serverRun) {
                        // Vältetään virhe palvelimen sulkeutuessa
                        break;
                    }
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {

            //Ohjelma päättyy
            threadPool.shutdown();
            System.out.println("Palvelin kiinni");
        }
    }

    /**
     * Method for shutting the server down, currently called from ClientHand after every
     * Thread --> only one client handled
     * @param serverSocket
     */
    public static void stopServer(ServerSocket serverSocket) {
        serverRun = false;
        try {
            // Close the ServerSocket to activate accept() call
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }
}

class ClientHand implements Runnable {
    private final Socket socket;
    private final ServerSocket serverSocket;

    public ClientHand(Socket socket, ServerSocket serverSocket) {
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
            System.out.println("Ground Control received: " + receivedMessage);
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
        Server.stopServer(serverSocket);

    }
}