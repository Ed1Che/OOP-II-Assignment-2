import java.io.*;
import java.net.*;

public class SimpleFTP {
    private static final int SERVER_PORT = 12345;
    private static final int PACKET_SIZE = 100;
    private static final int TIMEOUT = 5000; // 5 seconds


    public static void connectionOrientedClient(String filename, InetAddress serverAddress) {
        try (Socket socket = new Socket(serverAddress, SERVER_PORT);
             FileInputStream fileInputStream = new FileInputStream(filename);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            byte[] buffer = new byte[PACKET_SIZE];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                out.flush();


                socket.setSoTimeout(TIMEOUT);


                try {
                    in.readLine();
                } catch (SocketTimeoutException e) {
                     System.err.println("Timeout occurred. Retransmitting the packet...");

                }
            }

            System.out.println("File transfer completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {

        InetAddress serverAddress = InetAddress.getLoopbackAddress();
        String filename = "file.txt";

        connectionOrientedClient(filename, serverAddress);
   
    }
}

