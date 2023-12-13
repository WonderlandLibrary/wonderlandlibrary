package optifine;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

public class HttpPipelineSender extends Thread {
   private HttpPipelineConnection httpPipelineConnection = null;
   private static final String CRLF = "\r\n";
   private static Charset ASCII = Charset.forName("ASCII");

   public HttpPipelineSender(HttpPipelineConnection var1) {
      super("HttpPipelineSender");
      this.httpPipelineConnection = var1;
   }

   public void run() {
      HttpPipelineRequest var1 = null;

      try {
         this.connect();

         while(!Thread.interrupted()) {
            var1 = this.httpPipelineConnection.getNextRequestSend();
            HttpRequest var2 = var1.getHttpRequest();
            OutputStream var3 = this.httpPipelineConnection.getOutputStream();
            this.writeRequest(var2, var3);
            this.httpPipelineConnection.onRequestSent(var1);
         }
      } catch (InterruptedException var4) {
         return;
      } catch (Exception var5) {
         this.httpPipelineConnection.onExceptionSend(var1, var5);
      }

   }

   private void connect() throws IOException {
      String var1 = this.httpPipelineConnection.getHost();
      int var2 = this.httpPipelineConnection.getPort();
      Proxy var3 = this.httpPipelineConnection.getProxy();
      Socket var4 = new Socket(var3);
      var4.connect(new InetSocketAddress(var1, var2), 5000);
      this.httpPipelineConnection.setSocket(var4);
   }

   private void writeRequest(HttpRequest var1, OutputStream var2) throws IOException {
      this.write(var2, var1.getMethod() + " " + var1.getFile() + " " + var1.getHttp() + "\r\n");
      Map var3 = var1.getHeaders();
      Iterator var5 = var3.keySet().iterator();

      while(var5.hasNext()) {
         String var4 = (String)var5.next();
         String var6 = (String)var1.getHeaders().get(var4);
         this.write(var2, var4 + ": " + var6 + "\r\n");
      }

      this.write(var2, "\r\n");
   }

   private void write(OutputStream var1, String var2) throws IOException {
      byte[] var3 = var2.getBytes(ASCII);
      var1.write(var3);
   }
}
