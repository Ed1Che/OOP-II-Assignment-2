import java.io.*;
import java.net.*;

public class CalculatorClient {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 11111;

    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("Connected to server.");

            // Input from user
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter first number: ");
            int num1 = Integer.parseInt(userInput.readLine());
            System.out.print("Enter second number: ");
            int num2 = Integer.parseInt(userInput.readLine());
            System.out.print("Enter operation (+, -, *, /, %): ");
            char operator = userInput.readLine().charAt(0);

            // Send input to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(num1 + " " + num2 + " " + operator);

            // Receive result from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = in.readLine();
            System.out.println("Result: " + result);

            // Close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
