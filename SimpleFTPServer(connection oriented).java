import java.io.*;
import java.net.*;

public class SimpleFTPServer {
    private static final int SERVER_PORT = 12345;
    private static final int PACKET_SIZE = 100;

  
    public static void connectionOrientedServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connection...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());


                processConnectionOrientedRequest(clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processConnectionOrientedRequest(Socket clientSocket) throws IOException {
        try (InputStream in = clientSocket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            byte[] buffer = new byte[PACKET_SIZE];
            int bytesRead;


            while ((bytesRead = in.read(buffer)) != -1) {
                System.out.write(buffer, 0, bytesRead);
            }


            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("File received successfully.");
            System.out.println("File received successfully.");
        }
    }

    public static void main(String[] args) {

       connectionOrientedServer();
        
    }
}

