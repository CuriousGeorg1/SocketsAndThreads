package fi.utu.tech.assignment4;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {

        // Creating 7 Clients
        int clientCount = 7;
        for (int i = 0; i < clientCount; i++){
            int clientId = i;
            // Create clientCount amount of new client threads
            new Thread(() -> runClient(clientId)).start();
        }
    }

    private static void runClient(int clientId) {

        //Create messages
        String firstMessage = "Hello";
        String secondMessage = "quit";

        try (Socket s = new Socket("127.0.0.1", 1234)){
            System.out.println("Client" + clientId + " connected");

            try(OutputStream os = s.getOutputStream(); InputStream is = s.getInputStream()) {
                //Sending message
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);
                out.println(firstMessage);
                System.out.println("Client " + clientId + " sent message: Hello");


                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String response = in.readLine();

                if (response.equals("Ack")) {
                    System.out.println("Varmistus saatu! " + clientId);
                    out.println(secondMessage);
                    System.out.println("Client " + clientId + " sent message: quit");
                }
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


}

