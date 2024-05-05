// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.prot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client
{
    private Socket socket;
    public DataOutputStream out;
    public DataInputStream in;
    
    public void connect(final String ip, final int port) {
        try {
            this.socket = new Socket(ip, port);
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        }
        catch (Exception ex) {
            bib.z().n();
        }
    }
    
    public void writeUTF(final String s) {
        try {
            this.out.writeUTF(s);
        }
        catch (Exception ex) {
            bib.z().n();
        }
    }
    
    public String readUTF() {
        try {
            return this.in.readUTF();
        }
        catch (Exception ex) {
            bib.z().n();
            return "-999";
        }
    }
    
    public static blk getScreen(final String s) {
        try {
            return (blk)Class.forName(s).newInstance();
        }
        catch (Exception ex) {
            bib.z().n();
            return null;
        }
    }
    
    public Socket getSocket() {
        return this.socket;
    }
}
