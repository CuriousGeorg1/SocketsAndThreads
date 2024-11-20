package fi.utu.tech.assignment5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Server {

    public static volatile boolean serverRun = true;

    public static void main(String[] args) {
        // Thread pool to handle client connections
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Kuunnellaan 1234");
            serverSocket.setSoTimeout(60000); // 10 seconds timeout
            while (serverRun) {
                try {
                    // Accept new clients
                    Socket commSocket = serverSocket.accept();

                    // Start a new thread to handle the client
                    threadPool.execute(new ClientHandler(commSocket, serverSocket));

                } catch (IOException e) {
                    System.err.println("Error in connection: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            threadPool.shutdown();
            System.out.println("Palvelin kiinni");
        }
    }

    public static void stopServer(ServerSocket serverSocket, ExecutorService threadPool) {
        serverRun = false;
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();  // Close the ServerSocket
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        threadPool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            threadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
    
}
