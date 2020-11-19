package it.itis.arezzo;
import java.net.*;
import java.io.*;

public class UDPclient {
    //istanza della classe che rappresenta il socket che impiega UDP
    private DatagramSocket socket;

    /*
    costruttore della classe UDPclient dove viene lanciata
    una eccezione in caso ci sia un errore di comunicazione
     */
    public UDPclient() throws SocketException {
        //si crea un socket
        socket = new DatagramSocket();
        //imposta il tempo di attesa massimo espresso in millisecondi
        socket.setSoTimeout(1000); // 1000ms = 1s
    }

    /*
    metodo della classe UDPclient che se invocato
    consente di chiudere il socket (non restituisce niente)
     */
    public void close_socket() {
        socket.close();
    }

    /*
    metodo della classe UDPclient che se invocato
    trasmette un datagram contente una stringa codificata in ASCII a un server UDP
    individuato dall'indirizzo IP e dal numero di porta e attende al massimo per un secondo
    la risposta che restituisce (vengono passate 2 stringhe e un intero, inoltre vengono
    lanciate 3 eccezioni)
     */
    public String sendAndReceive( String request, String host, int port) throws UnknownHostException, IOException, SocketTimeoutException {
        //creazione di un array di byte
        byte[] buffer;
        //istanza della classe che rappresenta il datagram UDP
        DatagramPacket datagram;
        //si dichiara una variabile stringa
        String answer;
        //indirizzo IP del destinatario del datagram
        InetAddress address = InetAddress.getByName(host);
        
        //verifica chiusura socket
        if ( socket.isClosed()) {
            throw new IOException();
        }
        //trasformazione in array di byte della stringa
        buffer = request.getBytes("UTF-8");
        //costruzione datagram di richiesta
        datagram = new DatagramPacket( buffer, buffer.length, address, port);
        //trasmissione datagram di richiesta
        socket.send(datagram);
        //attesa ricezione datagram di richiesta (tempo massimo di attesa: 1s)
        socket.receive(datagram);
        //verifica indirizzo/porta provenienza datagram di risposta
        if ( datagram.getAddress().equals(address) && datagram.getPort() == port) {
            answer = new String( datagram.getData(), 0, datagram.getLength(), "ISO-8859-1");
        }
        else {
            throw new SocketTimeoutException();
        }
        //ritorna la stringa
        return answer;
    }
    
    public static void main(String args[]) {
        //si dichiara una variabile stringa
        String IP_address;
        //si dichiara una variabile intera
        int UDP_port;
        //si dichiara due variabili stringa
        String request, answer;
        //si dichiara una istanza della classe UDPclient
        UDPclient client;

        /*
        se la lunghezza dell'array è diversa da 3 allora si esegue
        le istruzioni dentro l'if altrimenti proseguo e si esegue quelle all'interno dell'else
         */
        if ( args.length != 3) {
            //si assegna un indirizzo ip alla variabile sopra dichiarata
            IP_address = "192.168.1.123";//127.0.0.1
            //si assegna una porta alla variabile sopra dichiarata
            UDP_port = 7;//7
            //si assegna una stringa alla variabile sopra dichiarata
            request = "Ciao!";
        }
        else {
            //si assegna alla variabile sopra dichiarata il valore prensente nella posizione 0 dell'array
            IP_address = args[0];
            /*
            si assegna alla variabile sopra dichiarata il valore
            prensente nella posizione 1 dell'array (utilizzando anche il parseInt)
             */
            UDP_port = Integer.parseInt(args[1]);
            //si assegna alla variabile sopra dichiarata il valore prensente nella posizione 2 dell'array
            request = args[2];
        }
        //si apre un try catch per il controllo degli errori
        try {
            //si crea un client richiamando il costruttore della classe corrispettiva
            client = new UDPclient();
            /*
            si assegna alla variabile sopra dichiarata la stringa restituita dall'esecuzione
            del metodo richiamato, al metodo vengono passate 2 stringhe e un intero
             */
            answer = client.sendAndReceive(request, IP_address, UDP_port);
            //si stampa la stringa ottenuta nella riga precedente
            System.out.println("Ricevuto in risposta: " + answer);
            //viene richiamato il metodo che permette la chiusura del socket
            client.close_socket();
        }
        catch (SocketException exception) {
            //si stampa un messaggio di errore
            System.err.println("Errore creazione socket!");
        }
        catch (UnknownHostException exception) {
            //si stampa un messaggio di errore
            System.err.println("Indirizzo IP errato!");
        }
        catch (SocketTimeoutException exception) {
            //si stampa un messaggio di errore
            System.err.println("Nessuna risposta dal server!");
        }
        catch (IOException exception) {
            //si stampa un messaggio di errore
            System.err.println("Errore generico di comunicazione!");
        }
    }
}
