package org.apache.http.client.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

@NotThreadSafe
public class EntityBuilder {
   private String text;
   private byte[] binary;
   private InputStream stream;
   private List parameters;
   private Serializable serializable;
   private File file;
   private ContentType contentType;
   private String contentEncoding;
   private boolean chunked;
   private boolean gzipCompress;

   EntityBuilder() {
   }

   public static EntityBuilder create() {
      return new EntityBuilder();
   }

   private void clearContent() {
      this.text = null;
      this.binary = null;
      this.stream = null;
      this.parameters = null;
      this.serializable = null;
      this.file = null;
   }

   public String getText() {
      return this.text;
   }

   public EntityBuilder setText(String var1) {
      this.clearContent();
      this.text = var1;
      return this;
   }

   public byte[] getBinary() {
      return this.binary;
   }

   public EntityBuilder setBinary(byte[] var1) {
      this.clearContent();
      this.binary = var1;
      return this;
   }

   public InputStream getStream() {
      return this.stream;
   }

   public EntityBuilder setStream(InputStream var1) {
      this.clearContent();
      this.stream = var1;
      return this;
   }

   public List getParameters() {
      return this.parameters;
   }

   public EntityBuilder setParameters(List var1) {
      this.clearContent();
      this.parameters = var1;
      return this;
   }

   public EntityBuilder setParameters(NameValuePair... var1) {
      return this.setParameters(Arrays.asList(var1));
   }

   public Serializable getSerializable() {
      return this.serializable;
   }

   public EntityBuilder setSerializable(Serializable var1) {
      this.clearContent();
      this.serializable = var1;
      return this;
   }

   public File getFile() {
      return this.file;
   }

   public EntityBuilder setFile(File var1) {
      this.clearContent();
      this.file = var1;
      return this;
   }

   public ContentType getContentType() {
      return this.contentType;
   }

   public EntityBuilder setContentType(ContentType var1) {
      this.contentType = var1;
      return this;
   }

   public String getContentEncoding() {
      return this.contentEncoding;
   }

   public EntityBuilder setContentEncoding(String var1) {
      this.contentEncoding = var1;
      return this;
   }

   public boolean isChunked() {
      return this.chunked;
   }

   public EntityBuilder chunked() {
      this.chunked = true;
      return this;
   }

   public boolean isGzipCompress() {
      return this.gzipCompress;
   }

   public EntityBuilder gzipCompress() {
      this.gzipCompress = true;
      return this;
   }

   private ContentType getContentOrDefault(ContentType var1) {
      return this.contentType != null ? this.contentType : var1;
   }

   public HttpEntity build() {
      Object var1;
      if (this.text != null) {
         var1 = new StringEntity(this.text, this.getContentOrDefault(ContentType.DEFAULT_TEXT));
      } else if (this.binary != null) {
         var1 = new ByteArrayEntity(this.binary, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
      } else if (this.stream != null) {
         var1 = new InputStreamEntity(this.stream, 1L, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
      } else if (this.parameters != null) {
         var1 = new UrlEncodedFormEntity(this.parameters, this.contentType != null ? this.contentType.getCharset() : null);
      } else if (this.serializable != null) {
         var1 = new SerializableEntity(this.serializable);
         ((AbstractHttpEntity)var1).setContentType(ContentType.DEFAULT_BINARY.toString());
      } else if (this.file != null) {
         var1 = new FileEntity(this.file, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
      } else {
         var1 = new BasicHttpEntity();
      }

      if (((AbstractHttpEntity)var1).getContentType() != null && this.contentType != null) {
         ((AbstractHttpEntity)var1).setContentType(this.contentType.toString());
      }

      ((AbstractHttpEntity)var1).setContentEncoding(this.contentEncoding);
      ((AbstractHttpEntity)var1).setChunked(this.chunked);
      return (HttpEntity)(this.gzipCompress ? new GzipCompressingEntity((HttpEntity)var1) : var1);
   }
}
