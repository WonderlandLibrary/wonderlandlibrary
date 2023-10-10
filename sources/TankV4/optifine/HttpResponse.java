package optifine;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
   private int status = 0;
   private String statusLine = null;
   private Map headers = new LinkedHashMap();
   private byte[] body = null;

   public HttpResponse(int var1, String var2, Map var3, byte[] var4) {
      this.status = var1;
      this.statusLine = var2;
      this.headers = var3;
      this.body = var4;
   }

   public int getStatus() {
      return this.status;
   }

   public String getStatusLine() {
      return this.statusLine;
   }

   public Map getHeaders() {
      return this.headers;
   }

   public String getHeader(String var1) {
      return (String)this.headers.get(var1);
   }

   public byte[] getBody() {
      return this.body;
   }
}
