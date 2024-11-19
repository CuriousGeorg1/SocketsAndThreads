package fi.utu.tech.assignment2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        // Define message
        String message = "And the stars look very different today";

        try (Socket s = new Socket("127.0.0.1", 1234)){
            System.out.println("Connected");

            try(OutputStream os = s.getOutputStream()) {
                //Sending message
                os.write((message + "\n").getBytes());
                os.flush();
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
