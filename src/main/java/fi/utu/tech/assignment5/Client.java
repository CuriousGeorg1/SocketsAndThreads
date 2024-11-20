package fi.utu.tech.assignment5;

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

        String light = "LIGHT";

        //Create messages
        String[] messages = {"ON", "OFF", "QUERY"};
        String[] lampIds = {"1","2","3","4","5"};

        try (Socket s = new Socket("127.0.0.1", 1234)){
            System.out.println("Client" + clientId + " connected");

            /*
            For-loop -> tarkoitus lähettää useampi viesti, ei toimi. Kuitenkin
            useampi client voi lähettää viestejä onnistuneesti
             */
            for (int i = 0; i < 1; i++) {
                // Random message and lamp
                int messageIndex = (int) (Math.random() * messages.length);
                int lampIndex = (int) (Math.random() * lampIds.length);
                System.out.println("Sending message: " + messages[messageIndex] + " to lamp " + lampIds[lampIndex]);
                try(OutputStream os = s.getOutputStream(); InputStream is = s.getInputStream();) {
                    //Sending message
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    if (messages[messageIndex].equals("QUERY")) {
                        out.println(light + ";" + messages[messageIndex]);

                        String response = in.readLine();
                        System.out.println(response);
                    } else {
                        out.println(light + ";" + messages[messageIndex] + ";" + lampIds[lampIndex]);
                        String response = in.readLine();
                        System.out.println(response);
                    }
            }


            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



}


