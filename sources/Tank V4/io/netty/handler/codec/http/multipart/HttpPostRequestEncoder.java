package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class HttpPostRequestEncoder implements ChunkedInput {
   private static final Map percentEncodings = new HashMap();
   private final HttpDataFactory factory;
   private final HttpRequest request;
   private final Charset charset;
   private boolean isChunked;
   private final List bodyListDatas;
   final List multipartHttpDatas;
   private final boolean isMultipart;
   String multipartDataBoundary;
   String multipartMixedBoundary;
   private boolean headerFinalized;
   private final HttpPostRequestEncoder.EncoderMode encoderMode;
   private boolean isLastChunk;
   private boolean isLastChunkSent;
   private FileUpload currentFileUpload;
   private boolean duringMixedMode;
   private long globalBodySize;
   private ListIterator iterator;
   private ByteBuf currentBuffer;
   private InterfaceHttpData currentData;
   private boolean isKey;

   public HttpPostRequestEncoder(HttpRequest var1, boolean var2) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      this(new DefaultHttpDataFactory(16384L), var1, var2, HttpConstants.DEFAULT_CHARSET, HttpPostRequestEncoder.EncoderMode.RFC1738);
   }

   public HttpPostRequestEncoder(HttpDataFactory var1, HttpRequest var2, boolean var3) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      this(var1, var2, var3, HttpConstants.DEFAULT_CHARSET, HttpPostRequestEncoder.EncoderMode.RFC1738);
   }

   public HttpPostRequestEncoder(HttpDataFactory var1, HttpRequest var2, boolean var3, Charset var4, HttpPostRequestEncoder.EncoderMode var5) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      this.isKey = true;
      if (var1 == null) {
         throw new NullPointerException("factory");
      } else if (var2 == null) {
         throw new NullPointerException("request");
      } else if (var4 == null) {
         throw new NullPointerException("charset");
      } else if (var2.getMethod() != HttpMethod.POST) {
         throw new HttpPostRequestEncoder.ErrorDataEncoderException("Cannot create a Encoder if not a POST");
      } else {
         this.request = var2;
         this.charset = var4;
         this.factory = var1;
         this.bodyListDatas = new ArrayList();
         this.isLastChunk = false;
         this.isLastChunkSent = false;
         this.isMultipart = var3;
         this.multipartHttpDatas = new ArrayList();
         this.encoderMode = var5;
         if (this.isMultipart) {
            this.initDataMultipart();
         }

      }
   }

   public void cleanFiles() {
      this.factory.cleanRequestHttpDatas(this.request);
   }

   public boolean isMultipart() {
      return this.isMultipart;
   }

   private void initDataMultipart() {
      this.multipartDataBoundary = getNewMultipartDelimiter();
   }

   private void initMixedMultipart() {
      this.multipartMixedBoundary = getNewMultipartDelimiter();
   }

   private static String getNewMultipartDelimiter() {
      return Long.toHexString(ThreadLocalRandom.current().nextLong()).toLowerCase();
   }

   public List getBodyListAttributes() {
      return this.bodyListDatas;
   }

   public void setBodyHttpDatas(List var1) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (var1 == null) {
         throw new NullPointerException("datas");
      } else {
         this.globalBodySize = 0L;
         this.bodyListDatas.clear();
         this.currentFileUpload = null;
         this.duringMixedMode = false;
         this.multipartHttpDatas.clear();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            InterfaceHttpData var3 = (InterfaceHttpData)var2.next();
            this.addBodyHttpData(var3);
         }

      }
   }

   public void addBodyAttribute(String var1, String var2) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         String var3 = var2;
         if (var2 == null) {
            var3 = "";
         }

         Attribute var4 = this.factory.createAttribute(this.request, var1, var3);
         this.addBodyHttpData(var4);
      }
   }

   public void addBodyFileUpload(String var1, File var2, String var3, boolean var4) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else if (var2 == null) {
         throw new NullPointerException("file");
      } else {
         String var5 = var3;
         String var6 = null;
         if (var3 == null) {
            if (var4) {
               var5 = "text/plain";
            } else {
               var5 = "application/octet-stream";
            }
         }

         if (!var4) {
            var6 = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value();
         }

         FileUpload var7 = this.factory.createFileUpload(this.request, var1, var2.getName(), var5, var6, (Charset)null, var2.length());

         try {
            var7.setContent(var2);
         } catch (IOException var9) {
            throw new HttpPostRequestEncoder.ErrorDataEncoderException(var9);
         }

         this.addBodyHttpData(var7);
      }
   }

   public void addBodyFileUploads(String var1, File[] var2, String[] var3, boolean[] var4) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (var2.length != var3.length && var2.length != var4.length) {
         throw new NullPointerException("Different array length");
      } else {
         for(int var5 = 0; var5 < var2.length; ++var5) {
            this.addBodyFileUpload(var1, var2[var5], var3[var5], var4[var5]);
         }

      }
   }

   public void addBodyHttpData(InterfaceHttpData var1) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (this.headerFinalized) {
         throw new HttpPostRequestEncoder.ErrorDataEncoderException("Cannot add value once finalized");
      } else if (var1 == null) {
         throw new NullPointerException("data");
      } else {
         this.bodyListDatas.add(var1);
         FileUpload var8;
         if (!this.isMultipart) {
            String var12;
            Attribute var14;
            String var15;
            if (var1 instanceof Attribute) {
               Attribute var9 = (Attribute)var1;

               try {
                  var12 = this.encodeAttribute(var9.getName(), this.charset);
                  var15 = this.encodeAttribute(var9.getValue(), this.charset);
                  var14 = this.factory.createAttribute(this.request, var12, var15);
                  this.multipartHttpDatas.add(var14);
                  this.globalBodySize += (long)(var14.getName().length() + 1) + var14.length() + 1L;
               } catch (IOException var7) {
                  throw new HttpPostRequestEncoder.ErrorDataEncoderException(var7);
               }
            } else if (var1 instanceof FileUpload) {
               var8 = (FileUpload)var1;
               var12 = this.encodeAttribute(var8.getName(), this.charset);
               var15 = this.encodeAttribute(var8.getFilename(), this.charset);
               var14 = this.factory.createAttribute(this.request, var12, var15);
               this.multipartHttpDatas.add(var14);
               this.globalBodySize += (long)(var14.getName().length() + 1) + var14.length() + 1L;
            }

         } else {
            if (var1 instanceof Attribute) {
               InternalAttribute var2;
               if (this.duringMixedMode) {
                  var2 = new InternalAttribute(this.charset);
                  var2.addValue("\r\n--" + this.multipartMixedBoundary + "--");
                  this.multipartHttpDatas.add(var2);
                  this.multipartMixedBoundary = null;
                  this.currentFileUpload = null;
                  this.duringMixedMode = false;
               }

               var2 = new InternalAttribute(this.charset);
               if (!this.multipartHttpDatas.isEmpty()) {
                  var2.addValue("\r\n");
               }

               var2.addValue("--" + this.multipartDataBoundary + "\r\n");
               Attribute var3 = (Attribute)var1;
               var2.addValue("Content-Disposition: form-data; name=\"" + var3.getName() + "\"\r\n");
               Charset var4 = var3.getCharset();
               if (var4 != null) {
                  var2.addValue("Content-Type: text/plain; charset=" + var4 + "\r\n");
               }

               var2.addValue("\r\n");
               this.multipartHttpDatas.add(var2);
               this.multipartHttpDatas.add(var1);
               this.globalBodySize += var3.length() + (long)var2.size();
            } else if (var1 instanceof FileUpload) {
               var8 = (FileUpload)var1;
               InternalAttribute var10 = new InternalAttribute(this.charset);
               if (!this.multipartHttpDatas.isEmpty()) {
                  var10.addValue("\r\n");
               }

               boolean var11;
               if (this.duringMixedMode) {
                  if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(var8.getName())) {
                     var11 = true;
                  } else {
                     var10.addValue("--" + this.multipartMixedBoundary + "--");
                     this.multipartHttpDatas.add(var10);
                     this.multipartMixedBoundary = null;
                     var10 = new InternalAttribute(this.charset);
                     var10.addValue("\r\n");
                     var11 = false;
                     this.currentFileUpload = var8;
                     this.duringMixedMode = false;
                  }
               } else if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(var8.getName())) {
                  this.initMixedMultipart();
                  InternalAttribute var5 = (InternalAttribute)this.multipartHttpDatas.get(this.multipartHttpDatas.size() - 2);
                  this.globalBodySize -= (long)var5.size();
                  StringBuilder var6 = new StringBuilder(139 + this.multipartDataBoundary.length() + this.multipartMixedBoundary.length() * 2 + var8.getFilename().length() + var8.getName().length());
                  var6.append("--");
                  var6.append(this.multipartDataBoundary);
                  var6.append("\r\n");
                  var6.append("Content-Disposition");
                  var6.append(": ");
                  var6.append("form-data");
                  var6.append("; ");
                  var6.append("name");
                  var6.append("=\"");
                  var6.append(var8.getName());
                  var6.append("\"\r\n");
                  var6.append("Content-Type");
                  var6.append(": ");
                  var6.append("multipart/mixed");
                  var6.append("; ");
                  var6.append("boundary");
                  var6.append('=');
                  var6.append(this.multipartMixedBoundary);
                  var6.append("\r\n\r\n");
                  var6.append("--");
                  var6.append(this.multipartMixedBoundary);
                  var6.append("\r\n");
                  var6.append("Content-Disposition");
                  var6.append(": ");
                  var6.append("attachment");
                  var6.append("; ");
                  var6.append("filename");
                  var6.append("=\"");
                  var6.append(var8.getFilename());
                  var6.append("\"\r\n");
                  var5.setValue(var6.toString(), 1);
                  var5.setValue("", 2);
                  this.globalBodySize += (long)var5.size();
                  var11 = true;
                  this.duringMixedMode = true;
               } else {
                  var11 = false;
                  this.currentFileUpload = var8;
                  this.duringMixedMode = false;
               }

               if (var11) {
                  var10.addValue("--" + this.multipartMixedBoundary + "\r\n");
                  var10.addValue("Content-Disposition: attachment; filename=\"" + var8.getFilename() + "\"\r\n");
               } else {
                  var10.addValue("--" + this.multipartDataBoundary + "\r\n");
                  var10.addValue("Content-Disposition: form-data; name=\"" + var8.getName() + "\"; " + "filename" + "=\"" + var8.getFilename() + "\"\r\n");
               }

               var10.addValue("Content-Type: " + var8.getContentType());
               String var13 = var8.getContentTransferEncoding();
               if (var13 != null && var13.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                  var10.addValue("\r\nContent-Transfer-Encoding: " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value() + "\r\n\r\n");
               } else if (var8.getCharset() != null) {
                  var10.addValue("; charset=" + var8.getCharset() + "\r\n\r\n");
               } else {
                  var10.addValue("\r\n\r\n");
               }

               this.multipartHttpDatas.add(var10);
               this.multipartHttpDatas.add(var1);
               this.globalBodySize += var8.length() + (long)var10.size();
            }

         }
      }
   }

   public HttpRequest finalizeRequest() throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (!this.headerFinalized) {
         if (this.isMultipart) {
            InternalAttribute var1 = new InternalAttribute(this.charset);
            if (this.duringMixedMode) {
               var1.addValue("\r\n--" + this.multipartMixedBoundary + "--");
            }

            var1.addValue("\r\n--" + this.multipartDataBoundary + "--\r\n");
            this.multipartHttpDatas.add(var1);
            this.multipartMixedBoundary = null;
            this.currentFileUpload = null;
            this.duringMixedMode = false;
            this.globalBodySize += (long)var1.size();
         }

         this.headerFinalized = true;
         HttpHeaders var9 = this.request.headers();
         List var2 = var9.getAll("Content-Type");
         List var3 = var9.getAll("Transfer-Encoding");
         if (var2 != null) {
            var9.remove("Content-Type");
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               String var6 = var5.toLowerCase();
               if (!var6.startsWith("multipart/form-data") && !var6.startsWith("application/x-www-form-urlencoded")) {
                  var9.add((String)"Content-Type", (Object)var5);
               }
            }
         }

         if (this.isMultipart) {
            String var10 = "multipart/form-data; boundary=" + this.multipartDataBoundary;
            var9.add((String)"Content-Type", (Object)var10);
         } else {
            var9.add((String)"Content-Type", (Object)"application/x-www-form-urlencoded");
         }

         long var11 = this.globalBodySize;
         if (this.isMultipart) {
            this.iterator = this.multipartHttpDatas.listIterator();
         } else {
            --var11;
            this.iterator = this.multipartHttpDatas.listIterator();
         }

         var9.set((String)"Content-Length", (Object)String.valueOf(var11));
         if (var11 <= 8096L && !this.isMultipart) {
            HttpContent var13 = this.nextChunk();
            if (this.request instanceof FullHttpRequest) {
               FullHttpRequest var14 = (FullHttpRequest)this.request;
               ByteBuf var8 = var13.content();
               if (var14.content() != var8) {
                  var14.content().clear().writeBytes(var8);
                  var8.release();
               }

               return var14;
            } else {
               return new HttpPostRequestEncoder.WrappedFullHttpRequest(this.request, var13);
            }
         } else {
            this.isChunked = true;
            if (var3 != null) {
               var9.remove("Transfer-Encoding");
               Iterator var12 = var3.iterator();

               while(var12.hasNext()) {
                  String var7 = (String)var12.next();
                  if (!var7.equalsIgnoreCase("chunked")) {
                     var9.add((String)"Transfer-Encoding", (Object)var7);
                  }
               }
            }

            HttpHeaders.setTransferEncodingChunked(this.request);
            return new HttpPostRequestEncoder.WrappedHttpRequest(this.request);
         }
      } else {
         throw new HttpPostRequestEncoder.ErrorDataEncoderException("Header already encoded");
      }
   }

   public boolean isChunked() {
      return this.isChunked;
   }

   private String encodeAttribute(String var1, Charset var2) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (var1 == null) {
         return "";
      } else {
         try {
            String var3 = URLEncoder.encode(var1, var2.name());
            Entry var5;
            String var6;
            if (this.encoderMode == HttpPostRequestEncoder.EncoderMode.RFC3986) {
               for(Iterator var4 = percentEncodings.entrySet().iterator(); var4.hasNext(); var3 = ((Pattern)var5.getKey()).matcher(var3).replaceAll(var6)) {
                  var5 = (Entry)var4.next();
                  var6 = (String)var5.getValue();
               }
            }

            return var3;
         } catch (UnsupportedEncodingException var7) {
            throw new HttpPostRequestEncoder.ErrorDataEncoderException(var2.name(), var7);
         }
      }
   }

   private ByteBuf fillByteBuf() {
      int var1 = this.currentBuffer.readableBytes();
      ByteBuf var2;
      if (var1 > 8096) {
         var2 = this.currentBuffer.slice(this.currentBuffer.readerIndex(), 8096);
         this.currentBuffer.skipBytes(8096);
         return var2;
      } else {
         var2 = this.currentBuffer;
         this.currentBuffer = null;
         return var2;
      }
   }

   private HttpContent encodeNextChunkMultipart(int var1) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (this.currentData == null) {
         return null;
      } else {
         ByteBuf var2;
         if (this.currentData instanceof InternalAttribute) {
            var2 = ((InternalAttribute)this.currentData).toByteBuf();
            this.currentData = null;
         } else {
            if (this.currentData instanceof Attribute) {
               try {
                  var2 = ((Attribute)this.currentData).getChunk(var1);
               } catch (IOException var5) {
                  throw new HttpPostRequestEncoder.ErrorDataEncoderException(var5);
               }
            } else {
               try {
                  var2 = ((HttpData)this.currentData).getChunk(var1);
               } catch (IOException var4) {
                  throw new HttpPostRequestEncoder.ErrorDataEncoderException(var4);
               }
            }

            if (var2.capacity() == 0) {
               this.currentData = null;
               return null;
            }
         }

         if (this.currentBuffer == null) {
            this.currentBuffer = var2;
         } else {
            this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, var2);
         }

         if (this.currentBuffer.readableBytes() < 8096) {
            this.currentData = null;
            return null;
         } else {
            var2 = this.fillByteBuf();
            return new DefaultHttpContent(var2);
         }
      }
   }

   private HttpContent encodeNextChunkUrlEncoded(int var1) throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (this.currentData == null) {
         return null;
      } else {
         int var2 = var1;
         ByteBuf var3;
         if (this.isKey) {
            String var4 = this.currentData.getName();
            var3 = Unpooled.wrappedBuffer(var4.getBytes());
            this.isKey = false;
            if (this.currentBuffer == null) {
               this.currentBuffer = Unpooled.wrappedBuffer(var3, Unpooled.wrappedBuffer("=".getBytes()));
               var2 = var1 - (var3.readableBytes() + 1);
            } else {
               this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, var3, Unpooled.wrappedBuffer("=".getBytes()));
               var2 = var1 - (var3.readableBytes() + 1);
            }

            if (this.currentBuffer.readableBytes() >= 8096) {
               var3 = this.fillByteBuf();
               return new DefaultHttpContent(var3);
            }
         }

         try {
            var3 = ((HttpData)this.currentData).getChunk(var2);
         } catch (IOException var5) {
            throw new HttpPostRequestEncoder.ErrorDataEncoderException(var5);
         }

         ByteBuf var6 = null;
         if (var3.readableBytes() < var2) {
            this.isKey = true;
            var6 = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes()) : null;
         }

         if (var3.capacity() == 0) {
            this.currentData = null;
            if (this.currentBuffer == null) {
               this.currentBuffer = var6;
            } else if (var6 != null) {
               this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, var6);
            }

            if (this.currentBuffer.readableBytes() >= 8096) {
               var3 = this.fillByteBuf();
               return new DefaultHttpContent(var3);
            } else {
               return null;
            }
         } else {
            if (this.currentBuffer == null) {
               if (var6 != null) {
                  this.currentBuffer = Unpooled.wrappedBuffer(var3, var6);
               } else {
                  this.currentBuffer = var3;
               }
            } else if (var6 != null) {
               this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, var3, var6);
            } else {
               this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, var3);
            }

            if (this.currentBuffer.readableBytes() < 8096) {
               this.currentData = null;
               this.isKey = true;
               return null;
            } else {
               var3 = this.fillByteBuf();
               return new DefaultHttpContent(var3);
            }
         }
      }
   }

   public void close() throws Exception {
   }

   public HttpContent readChunk(ChannelHandlerContext var1) throws Exception {
      return this.isLastChunkSent ? null : this.nextChunk();
   }

   private HttpContent nextChunk() throws HttpPostRequestEncoder.ErrorDataEncoderException {
      if (this.isLastChunk) {
         this.isLastChunkSent = true;
         return LastHttpContent.EMPTY_LAST_CONTENT;
      } else {
         int var2 = 8096;
         if (this.currentBuffer != null) {
            var2 -= this.currentBuffer.readableBytes();
         }

         ByteBuf var1;
         if (var2 <= 0) {
            var1 = this.fillByteBuf();
            return new DefaultHttpContent(var1);
         } else {
            HttpContent var3;
            if (this.currentData != null) {
               if (this.isMultipart) {
                  var3 = this.encodeNextChunkMultipart(var2);
                  if (var3 != null) {
                     return var3;
                  }
               } else {
                  var3 = this.encodeNextChunkUrlEncoded(var2);
                  if (var3 != null) {
                     return var3;
                  }
               }

               var2 = 8096 - this.currentBuffer.readableBytes();
            }

            if (!this.iterator.hasNext()) {
               this.isLastChunk = true;
               var1 = this.currentBuffer;
               this.currentBuffer = null;
               return new DefaultHttpContent(var1);
            } else {
               while(var2 > 0 && this.iterator.hasNext()) {
                  this.currentData = (InterfaceHttpData)this.iterator.next();
                  if (this.isMultipart) {
                     var3 = this.encodeNextChunkMultipart(var2);
                  } else {
                     var3 = this.encodeNextChunkUrlEncoded(var2);
                  }

                  if (var3 != null) {
                     return var3;
                  }

                  var2 = 8096 - this.currentBuffer.readableBytes();
               }

               this.isLastChunk = true;
               if (this.currentBuffer == null) {
                  this.isLastChunkSent = true;
                  return LastHttpContent.EMPTY_LAST_CONTENT;
               } else {
                  var1 = this.currentBuffer;
                  this.currentBuffer = null;
                  return new DefaultHttpContent(var1);
               }
            }
         }
      }
   }

   public boolean isEndOfInput() throws Exception {
      return this.isLastChunkSent;
   }

   public Object readChunk(ChannelHandlerContext var1) throws Exception {
      return this.readChunk(var1);
   }

   static {
      percentEncodings.put(Pattern.compile("\\*"), "%2A");
      percentEncodings.put(Pattern.compile("\\+"), "%20");
      percentEncodings.put(Pattern.compile("%7E"), "~");
   }

   private static final class WrappedFullHttpRequest extends HttpPostRequestEncoder.WrappedHttpRequest implements FullHttpRequest {
      private final HttpContent content;

      private WrappedFullHttpRequest(HttpRequest var1, HttpContent var2) {
         super(var1);
         this.content = var2;
      }

      public FullHttpRequest setProtocolVersion(HttpVersion var1) {
         super.setProtocolVersion(var1);
         return this;
      }

      public FullHttpRequest setMethod(HttpMethod var1) {
         super.setMethod(var1);
         return this;
      }

      public FullHttpRequest setUri(String var1) {
         super.setUri(var1);
         return this;
      }

      public FullHttpRequest copy() {
         DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy());
         var1.headers().set(this.headers());
         var1.trailingHeaders().set(this.trailingHeaders());
         return var1;
      }

      public FullHttpRequest duplicate() {
         DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate());
         var1.headers().set(this.headers());
         var1.trailingHeaders().set(this.trailingHeaders());
         return var1;
      }

      public FullHttpRequest retain(int var1) {
         this.content.retain(var1);
         return this;
      }

      public FullHttpRequest retain() {
         this.content.retain();
         return this;
      }

      public ByteBuf content() {
         return this.content.content();
      }

      public HttpHeaders trailingHeaders() {
         return this.content instanceof LastHttpContent ? ((LastHttpContent)this.content).trailingHeaders() : HttpHeaders.EMPTY_HEADERS;
      }

      public int refCnt() {
         return this.content.refCnt();
      }

      public boolean release() {
         return this.content.release();
      }

      public boolean release(int var1) {
         return this.content.release(var1);
      }

      public HttpRequest setUri(String var1) {
         return this.setUri(var1);
      }

      public HttpRequest setMethod(HttpMethod var1) {
         return this.setMethod(var1);
      }

      public HttpRequest setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      public HttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      public FullHttpMessage retain() {
         return this.retain();
      }

      public FullHttpMessage retain(int var1) {
         return this.retain(var1);
      }

      public FullHttpMessage copy() {
         return this.copy();
      }

      public LastHttpContent retain() {
         return this.retain();
      }

      public LastHttpContent retain(int var1) {
         return this.retain(var1);
      }

      public LastHttpContent copy() {
         return this.copy();
      }

      public HttpContent retain(int var1) {
         return this.retain(var1);
      }

      public HttpContent retain() {
         return this.retain();
      }

      public HttpContent duplicate() {
         return this.duplicate();
      }

      public HttpContent copy() {
         return this.copy();
      }

      public ByteBufHolder retain(int var1) {
         return this.retain(var1);
      }

      public ByteBufHolder retain() {
         return this.retain();
      }

      public ByteBufHolder duplicate() {
         return this.duplicate();
      }

      public ByteBufHolder copy() {
         return this.copy();
      }

      public ReferenceCounted retain(int var1) {
         return this.retain(var1);
      }

      public ReferenceCounted retain() {
         return this.retain();
      }

      WrappedFullHttpRequest(HttpRequest var1, HttpContent var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class WrappedHttpRequest implements HttpRequest {
      private final HttpRequest request;

      WrappedHttpRequest(HttpRequest var1) {
         this.request = var1;
      }

      public HttpRequest setProtocolVersion(HttpVersion var1) {
         this.request.setProtocolVersion(var1);
         return this;
      }

      public HttpRequest setMethod(HttpMethod var1) {
         this.request.setMethod(var1);
         return this;
      }

      public HttpRequest setUri(String var1) {
         this.request.setUri(var1);
         return this;
      }

      public HttpMethod getMethod() {
         return this.request.getMethod();
      }

      public String getUri() {
         return this.request.getUri();
      }

      public HttpVersion getProtocolVersion() {
         return this.request.getProtocolVersion();
      }

      public HttpHeaders headers() {
         return this.request.headers();
      }

      public DecoderResult getDecoderResult() {
         return this.request.getDecoderResult();
      }

      public void setDecoderResult(DecoderResult var1) {
         this.request.setDecoderResult(var1);
      }

      public HttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }
   }

   public static class ErrorDataEncoderException extends Exception {
      private static final long serialVersionUID = 5020247425493164465L;

      public ErrorDataEncoderException() {
      }

      public ErrorDataEncoderException(String var1) {
         super(var1);
      }

      public ErrorDataEncoderException(Throwable var1) {
         super(var1);
      }

      public ErrorDataEncoderException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   public static enum EncoderMode {
      RFC1738,
      RFC3986;

      private static final HttpPostRequestEncoder.EncoderMode[] $VALUES = new HttpPostRequestEncoder.EncoderMode[]{RFC1738, RFC3986};
   }
}
