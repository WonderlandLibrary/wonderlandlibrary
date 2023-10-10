package javassist.tools.web;

import java.io.IOException;
import java.net.Socket;

class ServiceThread extends Thread {
   Webserver web;
   Socket sock;

   public ServiceThread(Webserver var1, Socket var2) {
      this.web = var1;
      this.sock = var2;
   }

   public void run() {
      try {
         this.web.process(this.sock);
      } catch (IOException var2) {
      }

   }
}
