package fi.utu.tech.assignment1;

import fi.utu.tech.assignment3.ClientHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            while (serverRun) {
                try {
                    // Accept new clients
                    Socket commSocket = serverSocket.accept();
                    threadPool.execute(new CommHandler(commSocket, serverSocket));
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
            threadPool.shutdown();

            //Ohjelma päättyy
            System.out.println("Palvelin kiinni");
        }
    }

    /**
     * Metodi palvelimen main-metodin while-loopin lopettamiseksi
     * @param serverSocket palvelin joka suljetaan
     */
    public static void stopServer(ServerSocket serverSocket) {
        serverRun = false;
        try {
            // Close the ServerSocket to wake up the accept() call
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }
}

class CommHandler implements Runnable {
    private final Socket socket;
    private final ServerSocket serverSocket; // Reference to ServerSocket

    public CommHandler(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        System.out.println("Päästiin run metodiin");


        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
        System.out.println("Client disconnected");
        Server.stopServer(serverSocket); // Shut down the server after handling one client
        }


}

