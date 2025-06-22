import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    static void publishMessage(String data, Socket client, ArrayList<Socket> userList) throws IOException {
        for (Socket socket : userList) {
            if (socket != client) {
                DataOutputStream output = new DataOutputStream((socket.getOutputStream()));

                output.writeUTF(data);
            }
        }
    }

    static void addUser(Socket client, ArrayList<Socket> userList) throws IOException {
        DataInputStream input = new DataInputStream(client.getInputStream());


        String data = "";

        while (true) {
            data = input.readUTF();
            System.out.println(data);
            publishMessage(data, client, userList);

            if (data.equals("end")) {
                userList.remove(client);
                client.close();
                break;
            }

        }
    }

    public static void main(String[] args) throws IOException {

        try (ServerSocket sock = new ServerSocket(5000)) {
            int userCount = 0;
            ArrayList<Socket> userList = new ArrayList<>();
            while (true) {
                Socket clientSocket = sock.accept();
                System.out.println("New User " + userCount + " : " + clientSocket.getLocalAddress() + "Connected!");
                userList.add(clientSocket);

                int finalUserCount = userCount;
                new Thread() {
                    public void run() {
                        Socket client = clientSocket;
                        int curUser = finalUserCount;
                        try {
                            addUser(client, userList);
                        } catch (SocketException e) {
                            System.out.println("Connection " + curUser + " Closed");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
                userCount += 1;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

