
import java.io.*;
import java.net.*;
import java.util.*;

public class server {
    public static void main(String args[]) throws IOException{
        DatagramSocket socket = new DatagramSocket(1234);
        byte[] receive = new byte[0xFFFF];
        socket.setSoTimeout(30000);

        while(true){
            try{
                Arrays.fill(receive, (byte) 0);
                DatagramPacket packet = new DatagramPacket(receive, receive.length);
                socket.receive(packet);
                InetAddress clientAddr = packet.getAddress();
                int port = packet.getPort();
        
                System.out.println("Client " + clientAddr + " connected!");
                String promptString = "Enter the first and second string separated by space: ";
                receive = promptString.getBytes();
                DatagramPacket packetPrompt = new DatagramPacket(receive, receive.length, clientAddr, port);
                socket.send(packetPrompt);

                socket.receive(packet);
                
                String msg = clientContent(receive, packet.getLength());
                String processed_msg = segregateANDconcatenate(msg);
                if(processed_msg == null){
                    System.out.println("Output -> null");
                    processed_msg = "Error: Invalid input format. Please enter two alphanumeric strings separated by a space.";
                }
            
                System.out.println("Client said: " + processed_msg);
                
                Arrays.fill(receive, (byte) 0);
                receive = processed_msg.getBytes();
                DatagramPacket response = new DatagramPacket(receive, receive.length, clientAddr, port);
            
                socket.send(response);
            }
            catch (SocketTimeoutException e){
                System.out.println("Disconnected for >= 30 seconds. Closing socket.");
                socket.close();
                break;
            }
        }
    }

    public static String segregateANDconcatenate(String ret) {
        if (ret == null || ret.trim().isEmpty()) {
            System.out.println("Empty input");
            return null;
        }
        String regex = "^[A-Za-z0-9]+\\s+[A-Za-z0-9]+$";
    
        if (ret.matches(regex)) {
            return ret.replaceAll("\\s+", "");
        } else {
            System.out.println("String Formatting Issue");
            return null;
        }
    }
    
    public static String clientContent(byte[] receive, int length){
        int i=0;
        StringBuilder ret = new StringBuilder();
        while(i < length && receive[i] != 0){
            ret.append((char) receive[i]);
            i++;
        }
        return ret.toString();
    }
}
