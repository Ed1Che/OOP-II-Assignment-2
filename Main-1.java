import java.io.*;
import java.net.*;

class UDPChatApp {
    public static void main(String[] args) {
        // Server
        try {
            DatagramSocket serverSocket = new DatagramSocket(8000);
            System.out.println("Server listening on port 8000");

            byte[] receiveBuffer = new byte[1000];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from " + receivePacket.getAddress().getHostAddress() + ": " + message);
                String reply = readLine("Enter your reply: ");
                byte[] sendBuffer = reply.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Client
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");

            while (true) {
                String message = readLine("Enter your message: ");
                byte[] sendBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 8000);
                clientSocket.send(sendPacket);

                byte[] receiveBuffer = new byte[1000];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket);
                String reply = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + reply);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read input from console
    private static String readLine(String prompt) throws IOException {
        System.out.print(prompt);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}
