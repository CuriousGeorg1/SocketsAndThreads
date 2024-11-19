package fi.utu.tech.assignment3;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static volatile boolean serverRun = true;
    public static void main(String[] args) {
        // TODO: Kopioi edellisen tehtäväsi vastaus tähän pohjalle
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
                    threadPool.execute(new ClientHandler(commSocket, serverSocket));
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



}
