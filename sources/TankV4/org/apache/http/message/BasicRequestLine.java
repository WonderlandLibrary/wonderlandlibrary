package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicRequestLine implements RequestLine, Cloneable, Serializable {
   private static final long serialVersionUID = 2810581718468737193L;
   private final ProtocolVersion protoversion;
   private final String method;
   private final String uri;

   public BasicRequestLine(String var1, String var2, ProtocolVersion var3) {
      this.method = (String)Args.notNull(var1, "Method");
      this.uri = (String)Args.notNull(var2, "URI");
      this.protoversion = (ProtocolVersion)Args.notNull(var3, "Version");
   }

   public String getMethod() {
      return this.method;
   }

   public ProtocolVersion getProtocolVersion() {
      return this.protoversion;
   }

   public String getUri() {
      return this.uri;
   }

   public String toString() {
      return BasicLineFormatter.INSTANCE.formatRequestLine((CharArrayBuffer)null, (RequestLine)this).toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
