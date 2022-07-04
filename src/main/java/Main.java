import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 20000;

    public static void main(String[] args) {
        Runnable serverTask = () -> {
            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                 Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                out.println("New connection accepted");
                String name = in.readLine();
                out.println(String.format("Hi %s, your port is %d", name, clientSocket.getPort()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        var clientTask = new Runnable() {
            @Override
            public void run() {
                try(Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))  {
                    out.println("client");
                    String resp = in.readLine();
                    System.out.println(resp);
                    resp = in.readLine();
                    System.out.println(resp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
        Thread clientThread = new Thread(clientTask);
        clientThread.start();
    }
}