package fi.utu.tech.assignment5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;


    public ClientHandler(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Päästiin run metodiin");

        // Read and process messages from the client
        try (InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream()) {

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(Thread.currentThread().getName() + " Message received: " + message);
                if (response(message.trim(), out)) {
                    break;  // If response() returns true, break the loop and disconnect
                }
            }

        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally {
            try {
                socket.close();  // Close the socket when done
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
        System.out.println("Client " + Thread.currentThread().getName() + " disconnected");



    }

    /**
     * Method to handle checkin Clients messages, crucial for automation
     * @param message , message the client has sent
     * @param out, Print writer
     * @return false if message doesn't quit program, true if quit
     */
    private boolean response(String message, PrintWriter out) {
        //System.out.println("response kutsuttu");

        String[] receivedMessage = message.split(";");

        // Make sure logic is correct for received message
        if (receivedMessage.length != 3 && receivedMessage.length != 2) {
            out.println("Invalid message format");
            return false;
        }

        // Sending response
        switch (receivedMessage[1]) {
            case "QUERY":
                out.println("Kyselykomento vastaanotettu");
                return false;
            case "ON":
                out.println("Kytketään lamppu " + receivedMessage[2] + " päälle");
                return false;
            case "OFF":
                out.println("Kytketään lamppu " + receivedMessage[2] + " pois");
                return false;
            default:
                out.println("Tuntematon komento");
                return false;
        }
    }

}
