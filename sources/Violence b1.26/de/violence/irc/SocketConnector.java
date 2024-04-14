package de.violence.irc;

import de.violence.Violence;
import de.violence.save.manager.FileManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.client.Minecraft;

public class SocketConnector {
   private Socket socket;
   private OutputStream outputStream;
   private InputStream inputStream;
   private BufferedReader bufferedReader;

   public void onLoad() {
      try {
         this.connectSocket();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void connectSocket() {
      (new Thread(new Runnable() {
         public void run() {
            try {
               SocketConnector.this.socket = new Socket("skidclient.de", 8080);
               SocketConnector.this.outputStream = SocketConnector.this.socket.getOutputStream();
               SocketConnector.this.inputStream = SocketConnector.this.socket.getInputStream();
               SocketConnector.this.bufferedReader = new BufferedReader(new InputStreamReader(SocketConnector.this.inputStream, "UTF-8"));
               SocketConnector.this.writeOutput("uuid$" + FileManager.gui.getString("uuid"));
               Thread.sleep(500L);
               SocketConnector.this.writeOutput("ver$" + Violence.VERSION);
               Thread.sleep(500L);
               SocketConnector.this.writeOutput("ign$" + Minecraft.getMinecraft().getSession().getUsername());

               while(true) {
                  String e;
                  do {
                     e = SocketConnector.this.readStream();
                  } while(e.equalsIgnoreCase("error"));

                  if(e != null) {
                     SocketConnector.this.handleMessage(e);
                  }
               }
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }
      })).start();
      this.keepAlive();
   }

   private void handleMessage(String s) {
      Violence.getPacketManager().handle(s);
   }

   private void keepAlive() {
      (new Timer()).schedule(new TimerTask() {
         public void run() {
            if(SocketConnector.this.socket == null || !SocketConnector.this.socket.isConnected() || SocketConnector.this.socket.isClosed()) {
               System.out.println("IRC: Reconnecting...");
               SocketConnector.this.connectSocket();
            }

            SocketConnector.this.writeOutput("keepAlive$me");
         }
      }, 10000L, 10000L);
   }

   private String readStream() {
      try {
         return this.bufferedReader.readLine();
      } catch (Exception var2) {
         var2.printStackTrace();
         return "error";
      }
   }

   public void writeOutput(String s) {
      try {
         this.outputStream.write((s + "\n").getBytes("UTF-8"));
         this.outputStream.flush();
      } catch (Exception var3) {
         ;
      }

   }
}
