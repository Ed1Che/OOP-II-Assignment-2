import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class StreamingServer {
    private static final int PORT = 22222;
    private static final java.lang.String FILE_PATH = "Kira.m4a";

    public static void main(String[] args) {
        try  {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            System.out.println("Server started. Waiting for client connection...");


            File file = new File(FILE_PATH);
            FileInputStream fileInputStream = new FileInputStream(file);


            long fileSize = file.length();

            Random random = new Random();

            while (true) {

                int bufferSize = random.nextInt(1001) + 1000;
                byte[] buffer = new byte[bufferSize];


                int bytesRead = fileInputStream.read(buffer);
                if (bytesRead == -1) {

                    break;
                }


                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, InetAddress.getLoopbackAddress(), PORT);
                serverSocket.send(packet);
            }

            System.out.println("File sent to client.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
