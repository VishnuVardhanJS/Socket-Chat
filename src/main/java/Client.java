import java.util.*;
import java.net.*;
import java.io.*;

public class Client {

    static void getMessage(Socket client) throws IOException {
        DataInputStream output = new DataInputStream(client.getInputStream());

        String data = "";

        while (true) {
            data = output.readUTF();
            System.out.println(data);
        }
    }

    static void sendMessage(Socket client) throws IOException {
        Scanner input = new Scanner(System.in);
        DataOutputStream output = new DataOutputStream(client.getOutputStream());

        String data = "";

        while (!data.equals("end")) {
            data = input.nextLine();
            output.writeUTF(data);
        }

        input.close();
        output.close();
    }

    public static void main(String[] args) throws IOException {
        try (Socket client = new Socket("127.0.0.1", 5000);) {

            System.out.println("Connected to : " + client.getInetAddress());

            new Thread() {
                public void run() {
                    try {
                        getMessage(client);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();


            try {
                sendMessage(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
