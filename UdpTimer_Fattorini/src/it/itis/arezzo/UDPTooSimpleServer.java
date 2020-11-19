package it.itis.arezzo;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UDPTooSimpleServer {

    private static ArrayList<UDPclientAccesso> clients= new ArrayList<>();


    public static void main(String[] args) {

        Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(UDPclientAccesso client:clients){
                    client.setNumeroAccessi();
                }
            }
        }, 0,1000);

        int c;
        DatagramSocket socket=null;

        try {
            socket = new DatagramSocket(7);
            while(true){
                //socket.setSoTimeout(1000); // 1000ms = 1s
                DatagramPacket answer;
                // creazione di un array di byte della dimensione specificata
                byte[] buffer = new byte[8192];
                // creazione di un datagram UDP a partire dall’array di byte
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                UDPclientAccesso client=new UDPclientAccesso(request.getAddress());

                if(aggiungiClient(client)){
                    String requestReceived = new String( request.getData(), 0, request.getLength(), "ISO-8859-1");
                    System.out.println("Ricevuto REQ: " + requestReceived);
                    // costruzione datagram di risposta (identico al datagram di richiesta)
                    answer = new DatagramPacket( requestReceived.toUpperCase().getBytes(), request.getLength(), request.getAddress(), request.getPort());
                    // trasmissione datagram di risposta
                    socket.send(answer);
                }else{
                    DatagramPacket answerBan;
                    String mess="Sei stato bannato a causa delle tue continue richieste!";
                    answerBan =new DatagramPacket(mess.getBytes(),mess.length(),request.getAddress(), request.getPort());
                    socket.send(answerBan);
                }

            }
        }
        catch (Exception exception) {
            System.err.println("Errore!");
            System.err.println(exception.getMessage());
        }
        finally{
            if(socket!=null){
                socket.close(); // chiusura del socket
            }
        }
    }

    public static boolean aggiungiClient(UDPclientAccesso client){
            if(!clients.contains(client)){
                clients.add(client);
                return true;
            }else{
                if(!clients.get(clients.indexOf(client)).isBan()){
                    clients.get(clients.indexOf(client)).increNumAcc();
                    return true;
                }else{
                    return false;
                }
            }
    }

}
