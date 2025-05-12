import java.io.*;
import java.net.*;
import java.util.*;


public class client {
    public static void main(String args[]) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        Scanner scan = new Scanner(System.in);
        InetAddress ip = InetAddress.getLocalHost();
        byte[] Buf = new byte[0xFFFF];

        String initialMsg = "hello";
        Buf = initialMsg.getBytes();
        DatagramPacket initialPacket = new DatagramPacket(Buf, Buf.length, ip, 1234);
        socket.send(initialPacket);

        byte[] receivedBuf = new byte[0xFFFF];
        DatagramPacket responsePrompt = new DatagramPacket(receivedBuf, receivedBuf.length);
        socket.receive(responsePrompt);
        
        String responseString = new String(responsePrompt.getData(), 0, responsePrompt.getLength());
        System.out.println(responseString);

        String line = scan.nextLine();
        Buf = line.getBytes();

        DatagramPacket packet = new DatagramPacket(Buf, Buf.length, ip, 1234);
        socket.send(packet);
        
        Arrays.fill(receivedBuf, (byte) 0);
        DatagramPacket responseData = new DatagramPacket(receivedBuf, receivedBuf.length);
        socket.receive(responseData);

        String response_str = new String(responseData.getData(), 0, responseData.getLength());
        System.out.println("Server Replied:- " + response_str);

        socket.close();
        scan.close();
    }
}

