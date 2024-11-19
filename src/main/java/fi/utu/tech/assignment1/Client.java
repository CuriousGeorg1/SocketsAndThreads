package fi.utu.tech.assignment1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        // TODO: Asiakasohjelma
        try (Socket s = new Socket("127.0.0.1", 1234)){
            System.out.println("Connected");
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
