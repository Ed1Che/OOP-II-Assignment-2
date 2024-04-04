import java.io.*;
import java.net.*;

public class CalculatorServer {
    private static final int PORT = 11111;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for client connection...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client request in a separate thread
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        public Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read input from client
                String input = reader.readLine();
                String[] parts = input.split(" ");

                if (parts.length != 3) {
                    writer.println("Invalid input format");
                    return;
                }

                // Parse operands and operator
                int operand1 = Integer.parseInt(parts[0]);
                int operand2 = Integer.parseInt(parts[1]);
                char operator = parts[2].charAt(0);

                // Perform arithmetic operation
                int result;
                switch (operator) {
                    case '+':
                        result = operand1 + operand2;
                        break;
                    case '-':
                        result = operand1 - operand2;
                        break;
                    case '*':
                        result = operand1 * operand2;
                        break;
                    case '/':
                        if (operand2 != 0)
                            result = operand1 / operand2;
                        else {
                            writer.println("Division by zero");
                            return;
                        }
                        break;
                    case '%':
                        if (operand2 != 0)
                            result = operand1 % operand2;
                        else {
                            writer.println("Modulo by zero");
                            return;
                        }
                        break;
                    default:
                        writer.println("Invalid operator");
                        return;
                }

                // Send result to client
                writer.println(result);

                System.out.println("Result sent to client: " + result);
            } catch (IOException e) {
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
