import java.io.*;
import java.net.*;

public class SimpleFTPServer {
    private static final int SERVER_PORT = 12345;
    private static final int PACKET_SIZE = 100;

    

    public static void connectionlessServer() {
        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connectionless packets...");

            while (true) {
                byte[] buffer = new byte[PACKET_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);

                // Process client request
                processConnectionlessRequest(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processConnectionlessRequest(DatagramPacket packet) {
        String data = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Received: " + data);

    }

    public static void main(String[] args) {
        connectionlessServer();
    }
}
