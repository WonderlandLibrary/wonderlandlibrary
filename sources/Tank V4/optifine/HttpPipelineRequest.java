package optifine;

public class HttpPipelineRequest {
   private HttpRequest httpRequest = null;
   private HttpListener httpListener = null;
   private boolean closed = false;

   public HttpPipelineRequest(HttpRequest var1, HttpListener var2) {
      this.httpRequest = var1;
      this.httpListener = var2;
   }

   public HttpRequest getHttpRequest() {
      return this.httpRequest;
   }

   public HttpListener getHttpListener() {
      return this.httpListener;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void setClosed(boolean var1) {
      this.closed = var1;
   }
}
