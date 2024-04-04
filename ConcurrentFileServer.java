import java.io.*;
import java.net.*;

public class ConcurrentFileServer {
    private static final int SERVER_PORT = 55555;
    private static final int MAX_TRANSFER_SIZE = 1000;
    private static final int SLEEP_TIME = 200; // milliseconds

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Spawn a new thread for each client request
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the filename sent by the client
                String filename = reader.readLine();
                System.out.println("Client requested file: " + filename);

                // Open the requested file for reading
                try (FileInputStream fileInputStream = new FileInputStream(filename)) {
                    byte[] buffer = new byte[MAX_TRANSFER_SIZE];
                    int bytesRead;

                    // Transfer file contents to the client
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                        clientSocket.getOutputStream().flush();

                        // Sleep for 200 milliseconds
                        Thread.sleep(SLEEP_TIME);
                    }

                    System.out.println("File transfer completed.");
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + filename);
                    writer.println("File not found");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
