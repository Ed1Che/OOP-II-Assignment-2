import java.io.*;
import java.net.*;

public class SimpleFTP {
    private static final int SERVER_PORT = 12345;
    private static final int PACKET_SIZE = 100;
    private static final int TIMEOUT = 5000; // 5 seconds

    
    // Connectionless FTP client
    public static void connectionlessClient(String filename, InetAddress serverAddress) {
        try (DatagramSocket socket = new DatagramSocket()) {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                byte[] data = line.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, SERVER_PORT);
                socket.send(packet);
            }

            System.out.println("File transfer completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage:
        InetAddress serverAddress = InetAddress.getLoopbackAddress();
        String filename = "file.txt";
        
        connectionlessClient(filename, serverAddress);
    }
}
