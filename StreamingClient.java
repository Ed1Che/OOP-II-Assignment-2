import java.io.*;
import java.net.*;

public class StreamingClient {
    private static final int PORT = 22222;
    private static final int BUFFER_SIZE = 1024;
    private static final String OUTPUT_FILE = "received_media.m4a";

    public static void main(String[] args) {
        try {

            FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE);


            DatagramSocket clientSocket = new DatagramSocket();


            byte[] requestData = "SendMedia".getBytes();
            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, InetAddress.getLoopbackAddress(), PORT);
            clientSocket.send(requestPacket);

            System.out.println("Request sent to server. Receiving media...");


            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            while (true) {
                clientSocket.receive(receivedPacket);


                if (new String(receivedPacket.getData()).trim().equals("END"))
                    break;


                outputStream.write(receivedPacket.getData(), 0, receivedPacket.getLength());
            }

           
            outputStream.close();
            clientSocket.close();

            System.out.println("Media file received and saved as: " + OUTPUT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
