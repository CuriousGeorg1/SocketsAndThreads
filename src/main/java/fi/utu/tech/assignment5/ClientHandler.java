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

        //Read Clients message
        try (InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream()) {

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);


            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(Thread.currentThread().getName() + " Message received: " + message);
                if (response(message.trim(), out)) {break;}
            }

        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally { // Closing socket
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
        System.out.println("Client" + Thread.currentThread().getName() + " disconnected");


    }

    /**
     * Method to handle checkin Clients messages, crucial for automation
     * @param message , message the client has sent
     * @param out, Print writer
     * @return false if message doesn't quit program, true if quit
     */
    private boolean response(String message, PrintWriter out) {
        if ("Hello".equals(message)) {
            out.println("Ack");
            System.out.println(Thread.currentThread().getName() + " lähetettiin Ack");
            return false;
        } else if ("quit".equals(message)) {
            System.out.println(Thread.currentThread().getName() + " Vastaanotettiin quit");
            return true;
        } else {
            System.out.println("Dormamu I've come to bargain");
            out.println("Unknown message");
            return false;
        }
    }

}
