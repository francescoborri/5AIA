package it.itis.arezzo;

import java.net.InetAddress;

public class UDPclientAccesso {
    private final InetAddress address;
    private boolean ban;
    private int numeroAccessi;

    private final int numeroAccessiPerSecondoMax;
    private long tempoPrimoAccesso;


    public UDPclientAccesso(InetAddress address) {
        this.address = address;
        this.numeroAccessi=1;
        this.numeroAccessiPerSecondoMax =10000;
        this.tempoPrimoAccesso=System.currentTimeMillis();
    }

    public InetAddress getAddress() {
        return address;
    }

    public  boolean isBan() {
        return this.ban;
    }

    public  void setBan() {
        this.ban = true;
    }

    public int getNumeroAccessi() {
        return numeroAccessi;
    }

    public void setNumeroAccessi() {
        if(((System.currentTimeMillis())-tempoPrimoAccesso)<this.numeroAccessiPerSecondoMax){
           this.setBan();
        }else{
            this.tempoPrimoAccesso=System.currentTimeMillis();
            this.undoBan();
        }
    }

    private  void undoBan() {
        this.ban=false;
    }

    public int getNumeroAccessiPerSecondoMax() {
        return numeroAccessiPerSecondoMax;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UDPclientAccesso)) {
            return false;
        }
        UDPclientAccesso client = (UDPclientAccesso) obj;
        //System.out.println("prova"+ (client.getAddress().equals(this.getAddress())));
        return client.getAddress().equals(this.getAddress());
    }

    public void increNumAcc() {
        this.numeroAccessi++;
    }
}
