import java.io.*;
import java.net.*;
import java.util.concurrent.*;

class MultiClientChatServer {
    // Concurrent hashmap to store PrintWriter objects of all connected clients
    private static ConcurrentHashMap<Socket, PrintWriter> clientMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int portNumber = 12345;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Create a new thread to handle the client
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle each client
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Add the PrintWriter to the clientMap
                clientMap.put(clientSocket, out);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from " + clientSocket + ": " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Remove the client from the clientMap and close resources
                clientMap.remove(clientSocket);
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Broadcast message to all clients
        private void broadcast(String message) {
            for (PrintWriter writer : clientMap.values()) {
                writer.println(message);
            }
        }
    }
}
