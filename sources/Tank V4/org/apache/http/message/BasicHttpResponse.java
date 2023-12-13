package org.apache.http.message;

import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {
   private StatusLine statusline;
   private ProtocolVersion ver;
   private int code;
   private String reasonPhrase;
   private HttpEntity entity;
   private final ReasonPhraseCatalog reasonCatalog;
   private Locale locale;

   public BasicHttpResponse(StatusLine var1, ReasonPhraseCatalog var2, Locale var3) {
      this.statusline = (StatusLine)Args.notNull(var1, "Status line");
      this.ver = var1.getProtocolVersion();
      this.code = var1.getStatusCode();
      this.reasonPhrase = var1.getReasonPhrase();
      this.reasonCatalog = var2;
      this.locale = var3;
   }

   public BasicHttpResponse(StatusLine var1) {
      this.statusline = (StatusLine)Args.notNull(var1, "Status line");
      this.ver = var1.getProtocolVersion();
      this.code = var1.getStatusCode();
      this.reasonPhrase = var1.getReasonPhrase();
      this.reasonCatalog = null;
      this.locale = null;
   }

   public BasicHttpResponse(ProtocolVersion var1, int var2, String var3) {
      Args.notNegative(var2, "Status code");
      this.statusline = null;
      this.ver = var1;
      this.code = var2;
      this.reasonPhrase = var3;
      this.reasonCatalog = null;
      this.locale = null;
   }

   public ProtocolVersion getProtocolVersion() {
      return this.ver;
   }

   public StatusLine getStatusLine() {
      if (this.statusline == null) {
         this.statusline = new BasicStatusLine((ProtocolVersion)(this.ver != null ? this.ver : HttpVersion.HTTP_1_1), this.code, this.reasonPhrase != null ? this.reasonPhrase : this.getReason(this.code));
      }

      return this.statusline;
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public Locale getLocale() {
      return this.locale;
   }

   public void setStatusLine(StatusLine var1) {
      this.statusline = (StatusLine)Args.notNull(var1, "Status line");
      this.ver = var1.getProtocolVersion();
      this.code = var1.getStatusCode();
      this.reasonPhrase = var1.getReasonPhrase();
   }

   public void setStatusLine(ProtocolVersion var1, int var2) {
      Args.notNegative(var2, "Status code");
      this.statusline = null;
      this.ver = var1;
      this.code = var2;
      this.reasonPhrase = null;
   }

   public void setStatusLine(ProtocolVersion var1, int var2, String var3) {
      Args.notNegative(var2, "Status code");
      this.statusline = null;
      this.ver = var1;
      this.code = var2;
      this.reasonPhrase = var3;
   }

   public void setStatusCode(int var1) {
      Args.notNegative(var1, "Status code");
      this.statusline = null;
      this.code = var1;
      this.reasonPhrase = null;
   }

   public void setReasonPhrase(String var1) {
      this.statusline = null;
      this.reasonPhrase = var1;
   }

   public void setEntity(HttpEntity var1) {
      this.entity = var1;
   }

   public void setLocale(Locale var1) {
      this.locale = (Locale)Args.notNull(var1, "Locale");
      this.statusline = null;
   }

   protected String getReason(int var1) {
      return this.reasonCatalog != null ? this.reasonCatalog.getReason(var1, this.locale != null ? this.locale : Locale.getDefault()) : null;
   }

   public String toString() {
      return this.getStatusLine() + " " + this.headergroup;
   }
}
