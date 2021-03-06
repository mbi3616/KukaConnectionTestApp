import java.net.*;

public class UDPConnection {
    public static void main(String args[]) throws Exception {

        start();

    }


    public static void start() throws Exception{
        DatagramSocket mySocket = new DatagramSocket(30333);

        //robot address (please adjust IP!)
        InetSocketAddress robotAddress = new InetSocketAddress("172.31.1.147", 30300);

        // get robot state
        byte[] msg = String.format("%d;0;Get_State;true", System.currentTimeMillis()).getBytes("UTF-8");
        mySocket.send(new DatagramPacket(msg, msg.length, robotAddress));

        // receive answer state message
        byte[] receiveData = new byte[508];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        mySocket.receive(receivePacket);

        // extract counter
        String[] stateMessage = new String(receivePacket.getData()).split(";");
        long counter = Long.parseLong(stateMessage[2]);

        // start application by sending a rising edge
        // (false->true) for App_Start
        msg = String.format("%d;%d;App_Start;false", System.currentTimeMillis(), ++counter).getBytes("UTF-8");
        mySocket.send(new DatagramPacket(msg, msg.length, robotAddress));
        msg = String.format("%d;%d;App_Start;true", System.currentTimeMillis(), ++counter).getBytes("UTF-8");
        mySocket.send(new DatagramPacket(msg, msg.length, robotAddress));
    }
}