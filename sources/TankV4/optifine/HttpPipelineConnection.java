package optifine;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class HttpPipelineConnection {
   private String host;
   private int port;
   private Proxy proxy;
   private List listRequests;
   private List listRequestsSend;
   private Socket socket;
   private InputStream inputStream;
   private OutputStream outputStream;
   private HttpPipelineSender httpPipelineSender;
   private HttpPipelineReceiver httpPipelineReceiver;
   private int countRequests;
   private boolean responseReceived;
   private long keepaliveTimeoutMs;
   private int keepaliveMaxCount;
   private long timeLastActivityMs;
   private boolean terminated;
   private static final String LF = "\n";
   public static final int TIMEOUT_CONNECT_MS = 5000;
   public static final int TIMEOUT_READ_MS = 5000;
   private static final Pattern patternFullUrl = Pattern.compile("^[a-zA-Z]+://.*");

   public HttpPipelineConnection(String var1, int var2) {
      this(var1, var2, Proxy.NO_PROXY);
   }

   public HttpPipelineConnection(String var1, int var2, Proxy var3) {
      this.host = null;
      this.port = 0;
      this.proxy = Proxy.NO_PROXY;
      this.listRequests = new LinkedList();
      this.listRequestsSend = new LinkedList();
      this.socket = null;
      this.inputStream = null;
      this.outputStream = null;
      this.httpPipelineSender = null;
      this.httpPipelineReceiver = null;
      this.countRequests = 0;
      this.responseReceived = false;
      this.keepaliveTimeoutMs = 5000L;
      this.keepaliveMaxCount = 1000;
      this.timeLastActivityMs = System.currentTimeMillis();
      this.terminated = false;
      this.host = var1;
      this.port = var2;
      this.proxy = var3;
      this.httpPipelineSender = new HttpPipelineSender(this);
      this.httpPipelineSender.start();
      this.httpPipelineReceiver = new HttpPipelineReceiver(this);
      this.httpPipelineReceiver.start();
   }

   public synchronized boolean addRequest(HttpPipelineRequest var1) {
      if (this != false) {
         return false;
      } else {
         this.addRequest(var1, this.listRequests);
         this.addRequest(var1, this.listRequestsSend);
         ++this.countRequests;
         return true;
      }
   }

   private void addRequest(HttpPipelineRequest var1, List var2) {
      var2.add(var1);
      this.notifyAll();
   }

   public synchronized void setSocket(Socket var1) throws IOException {
      if (!this.terminated) {
         if (this.socket != null) {
            throw new IllegalArgumentException("Already connected");
         }

         this.socket = var1;
         this.socket.setTcpNoDelay(true);
         this.inputStream = this.socket.getInputStream();
         this.outputStream = new BufferedOutputStream(this.socket.getOutputStream());
         this.onActivity();
         this.notifyAll();
      }

   }

   public synchronized OutputStream getOutputStream() throws IOException, InterruptedException {
      while(this.outputStream == null) {
         this.checkTimeout();
         this.wait(1000L);
      }

      return this.outputStream;
   }

   public synchronized InputStream getInputStream() throws IOException, InterruptedException {
      while(this.inputStream == null) {
         this.checkTimeout();
         this.wait(1000L);
      }

      return this.inputStream;
   }

   public synchronized HttpPipelineRequest getNextRequestSend() throws InterruptedException, IOException {
      if (this.listRequestsSend.size() <= 0 && this.outputStream != null) {
         this.outputStream.flush();
      }

      return this.getNextRequest(this.listRequestsSend, true);
   }

   public synchronized HttpPipelineRequest getNextRequestReceive() throws InterruptedException {
      return this.getNextRequest(this.listRequests, false);
   }

   private HttpPipelineRequest getNextRequest(List var1, boolean var2) throws InterruptedException {
      while(var1.size() <= 0) {
         this.checkTimeout();
         this.wait(1000L);
      }

      this.onActivity();
      if (var2) {
         return (HttpPipelineRequest)var1.remove(0);
      } else {
         return (HttpPipelineRequest)var1.get(0);
      }
   }

   private void checkTimeout() {
      if (this.socket != null) {
         long var1 = this.keepaliveTimeoutMs;
         if (this.listRequests.size() > 0) {
            var1 = 5000L;
         }

         long var3 = System.currentTimeMillis();
         if (var3 > this.timeLastActivityMs + var1) {
            this.terminate(new InterruptedException("Timeout " + var1));
         }
      }

   }

   private void onActivity() {
      this.timeLastActivityMs = System.currentTimeMillis();
   }

   public synchronized void onRequestSent(HttpPipelineRequest var1) {
      if (!this.terminated) {
         this.onActivity();
      }

   }

   public synchronized void onResponseReceived(HttpPipelineRequest var1, HttpResponse var2) {
      if (!this.terminated) {
         this.responseReceived = true;
         this.onActivity();
         if (this.listRequests.size() <= 0 || this.listRequests.get(0) != var1) {
            throw new IllegalArgumentException("Response out of order: " + var1);
         }

         this.listRequests.remove(0);
         var1.setClosed(true);
         String var3 = var2.getHeader("Location");
         if (var2.getStatus() / 100 == 3 && var3 != null && var1.getHttpRequest().getRedirects() < 5) {
            try {
               var3 = this.normalizeUrl(var3, var1.getHttpRequest());
               HttpRequest var7 = HttpPipeline.makeRequest(var3, var1.getHttpRequest().getProxy());
               var7.setRedirects(var1.getHttpRequest().getRedirects() + 1);
               HttpPipelineRequest var5 = new HttpPipelineRequest(var7, var1.getHttpListener());
               HttpPipeline.addRequest(var5);
            } catch (IOException var6) {
               var1.getHttpListener().failed(var1.getHttpRequest(), var6);
            }
         } else {
            HttpListener var4 = var1.getHttpListener();
            var4.finished(var1.getHttpRequest(), var2);
         }

         this.checkResponseHeader(var2);
      }

   }

   private String normalizeUrl(String var1, HttpRequest var2) {
      if (patternFullUrl.matcher(var1).matches()) {
         return var1;
      } else if (var1.startsWith("//")) {
         return "http:" + var1;
      } else {
         String var3 = var2.getHost();
         if (var2.getPort() != 80) {
            var3 = var3 + ":" + var2.getPort();
         }

         if (var1.startsWith("/")) {
            return "http://" + var3 + var1;
         } else {
            String var4 = var2.getFile();
            int var5 = var4.lastIndexOf("/");
            return var5 >= 0 ? "http://" + var3 + var4.substring(0, var5 + 1) + var1 : "http://" + var3 + "/" + var1;
         }
      }
   }

   private void checkResponseHeader(HttpResponse var1) {
      String var2 = var1.getHeader("Connection");
      if (var2 != null && !var2.toLowerCase().equals("keep-alive")) {
         this.terminate(new EOFException("Connection not keep-alive"));
      }

      String var3 = var1.getHeader("Keep-Alive");
      if (var3 != null) {
         String[] var4 = Config.tokenize(var3, ",;");

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            String[] var7 = this.split(var6, '=');
            if (var7.length >= 2) {
               int var8;
               if (var7[0].equals("timeout")) {
                  var8 = Config.parseInt(var7[1], -1);
                  if (var8 > 0) {
                     this.keepaliveTimeoutMs = (long)(var8 * 1000);
                  }
               }

               if (var7[0].equals("max")) {
                  var8 = Config.parseInt(var7[1], -1);
                  if (var8 > 0) {
                     this.keepaliveMaxCount = var8;
                  }
               }
            }
         }
      }

   }

   private String[] split(String var1, char var2) {
      int var3 = var1.indexOf(var2);
      if (var3 < 0) {
         return new String[]{var1};
      } else {
         String var4 = var1.substring(0, var3);
         String var5 = var1.substring(var3 + 1);
         return new String[]{var4, var5};
      }
   }

   public synchronized void onExceptionSend(HttpPipelineRequest var1, Exception var2) {
      this.terminate(var2);
   }

   public synchronized void onExceptionReceive(HttpPipelineRequest var1, Exception var2) {
      this.terminate(var2);
   }

   private synchronized void terminate(Exception var1) {
      if (!this.terminated) {
         this.terminated = true;
         this.terminateRequests(var1);
         if (this.httpPipelineSender != null) {
            this.httpPipelineSender.interrupt();
         }

         if (this.httpPipelineReceiver != null) {
            this.httpPipelineReceiver.interrupt();
         }

         try {
            if (this.socket != null) {
               this.socket.close();
            }
         } catch (IOException var3) {
         }

         this.socket = null;
         this.inputStream = null;
         this.outputStream = null;
      }

   }

   private void terminateRequests(Exception var1) {
      if (this.listRequests.size() > 0) {
         HttpPipelineRequest var2;
         if (!this.responseReceived) {
            var2 = (HttpPipelineRequest)this.listRequests.remove(0);
            var2.getHttpListener().failed(var2.getHttpRequest(), var1);
            var2.setClosed(true);
         }

         while(this.listRequests.size() > 0) {
            var2 = (HttpPipelineRequest)this.listRequests.remove(0);
            HttpPipeline.addRequest(var2);
         }
      }

   }

   public int getCountRequests() {
      return this.countRequests;
   }

   public synchronized boolean hasActiveRequests() {
      return this.listRequests.size() > 0;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public Proxy getProxy() {
      return this.proxy;
   }
}
