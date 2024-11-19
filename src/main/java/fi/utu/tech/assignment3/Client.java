package fi.utu.tech.assignment3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client {

    public static void main(String[] args) {

        int clientCount = 5;
        for (int i = 0; i < clientCount; i++){
            int clientId = i;
            // Create clientCount amount of new client threads
            new Thread(() -> runClient(clientId)).start();
        }


    }

    private static void runClient(int clientId) {

        // Major Tom trying to send messages for Ground Control
        String[] messages = {"I'm stepping through the door", "And I'm floating in a most peculiar way",
                "And the stars look very different today", "Though I'm past one hundred thousand miles", "I'm feeling very still"};

        int random = (int) (Math.random() * messages.length);
        String message = messages[random];
        try (Socket s = new Socket("127.0.0.1", 1234)){
            System.out.println("Connected");

            try(OutputStream os = s.getOutputStream()) {
                //Sending message
                os.write((clientId + ": " + message + "\n").getBytes());
                os.flush();
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
