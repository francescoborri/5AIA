package it.itis.arezzo;
import java.net.*;
import java.io.*;

public class UDPecho extends Thread {
    //istanza della classe che rappresenta il socket che impiega UDP
    private DatagramSocket socket;

    /*
    costruttore della classe UDPecho al quale viene passata la porta
    e viene lanciata una eccezione in caso ci sia un errore di comunicazione
     */
    public UDPecho(int port) throws SocketException {
        //si crea un socket passando il numero di porta al costruttore
        socket = new DatagramSocket(port);
        //imposta il tempo di attesa massimo espresso in millisecondi
        socket.setSoTimeout(1000); // 1000ms = 1s
    }
    
    public void run() {
        //istanza della classe che rappresenta il datagram UDP
        DatagramPacket answer;
        //creazione di un array di byte della dimensione specificata
        byte[] buffer = new byte[8192];
        //creazione di un datagram UDP a partire dall’array di byte
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);

        //ciclo che viene eseguito fino a quando il thread non viene interrotto
        while (!Thread.interrupted()) {
            //si apre un try catch per il controllo degli errori
            try {
                //attesa ricezione datagram di richiesta (tempo massimo di attesa: 1s)
                socket.receive(request);
                //costruzione datagram di risposta (identico al datagram di richiesta)
                answer = new DatagramPacket( request.getData(), request.getLength(), request.getAddress(), request.getPort());
                //trasmissione datagram di risposta
                socket.send(answer);
            }
            catch (IOException exception) {
                //si stampa il messaggio relativo alla eccezione
                System.err.println(exception.getMessage());
            }
        }
        //chiusura del socket
        socket.close();
    }

    public static void main(String[] args) {
        //si dichiara una variabile intera
        int c;

        //si apre un try catch per il controllo degli errori
        try {
            /*
            si istanzia un server echo invocando il costruttore della
            classe soprastante, passandogli il numero di porta
             */
            UDPecho echoserver = new UDPecho(7);
            //si avvia il server
            echoserver.start();
            //legge un intero
            c = System.in.read();
            //viene interrotta l'esecuzione del servere
            echoserver.interrupt();
            //si attende fino la terminazione del metodo run
            echoserver.join();
        }
        catch (IOException exception) {
            //si stampa un messaggio di errore
            System.err.println("Errore!");
        }
        catch (InterruptedException exception) {
            //si stampa un messaggio di errore
            System.err.println("Errore!");
        }
    }
}
