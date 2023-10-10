package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.AppendableCharSequence;
import java.util.List;

public abstract class HttpObjectDecoder extends ReplayingDecoder {
   private final int maxInitialLineLength;
   private final int maxHeaderSize;
   private final int maxChunkSize;
   private final boolean chunkedSupported;
   protected final boolean validateHeaders;
   private final AppendableCharSequence seq;
   private final HttpObjectDecoder.HeaderParser headerParser;
   private final HttpObjectDecoder.LineParser lineParser;
   private HttpMessage message;
   private long chunkSize;
   private int headerSize;
   private long contentLength;
   static final boolean $assertionsDisabled = !HttpObjectDecoder.class.desiredAssertionStatus();

   protected HttpObjectDecoder() {
      this(4096, 8192, 8192, true);
   }

   protected HttpObjectDecoder(int var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, true);
   }

   protected HttpObjectDecoder(int var1, int var2, int var3, boolean var4, boolean var5) {
      super(HttpObjectDecoder.State.SKIP_CONTROL_CHARS);
      this.seq = new AppendableCharSequence(128);
      this.headerParser = new HttpObjectDecoder.HeaderParser(this, this.seq);
      this.lineParser = new HttpObjectDecoder.LineParser(this, this.seq);
      this.contentLength = Long.MIN_VALUE;
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + var1);
      } else if (var2 <= 0) {
         throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + var2);
      } else if (var3 <= 0) {
         throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + var3);
      } else {
         this.maxInitialLineLength = var1;
         this.maxHeaderSize = var2;
         this.maxChunkSize = var3;
         this.chunkedSupported = var4;
         this.validateHeaders = var5;
      }
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      int var5;
      int var12;
      switch((HttpObjectDecoder.State)this.state()) {
      case SKIP_CONTROL_CHARS:
         skipControlCharacters(var2);
         this.checkpoint(HttpObjectDecoder.State.READ_INITIAL);
         this.checkpoint();
      case READ_INITIAL:
         try {
            String[] var15 = splitInitialLine(this.lineParser.parse(var2));
            if (var15.length < 3) {
               this.checkpoint(HttpObjectDecoder.State.SKIP_CONTROL_CHARS);
               return;
            }

            this.message = this.createMessage(var15);
            this.checkpoint(HttpObjectDecoder.State.READ_HEADER);
         } catch (Exception var9) {
            var3.add(this.invalidMessage(var9));
            return;
         }
      case READ_HEADER:
         break;
      case READ_VARIABLE_LENGTH_CONTENT:
         var12 = Math.min(this.actualReadableBytes(), this.maxChunkSize);
         if (var12 > 0) {
            ByteBuf var17 = ByteBufUtil.readBytes(var1.alloc(), var2, var12);
            if (var2.isReadable()) {
               var3.add(new DefaultHttpContent(var17));
            } else {
               var3.add(new DefaultLastHttpContent(var17, this.validateHeaders));
               this.reset();
            }
         } else if (!var2.isReadable()) {
            var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
            this.reset();
         }

         return;
      case READ_FIXED_LENGTH_CONTENT:
         var12 = this.actualReadableBytes();
         if (var12 == 0) {
            return;
         }

         var5 = Math.min(var12, this.maxChunkSize);
         if ((long)var5 > this.chunkSize) {
            var5 = (int)this.chunkSize;
         }

         ByteBuf var6 = ByteBufUtil.readBytes(var1.alloc(), var2, var5);
         this.chunkSize -= (long)var5;
         if (this.chunkSize == 0L) {
            var3.add(new DefaultLastHttpContent(var6, this.validateHeaders));
            this.reset();
         } else {
            var3.add(new DefaultHttpContent(var6));
         }

         return;
      case READ_CHUNK_SIZE:
         try {
            AppendableCharSequence var11 = this.lineParser.parse(var2);
            var5 = getChunkSize(var11.toString());
            this.chunkSize = (long)var5;
            if (var5 == 0) {
               this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_FOOTER);
               return;
            }

            this.checkpoint(HttpObjectDecoder.State.READ_CHUNKED_CONTENT);
         } catch (Exception var8) {
            var3.add(this.invalidChunk(var8));
            return;
         }
      case READ_CHUNKED_CONTENT:
         if (!$assertionsDisabled && this.chunkSize > 2147483647L) {
            throw new AssertionError();
         }

         var12 = Math.min((int)this.chunkSize, this.maxChunkSize);
         DefaultHttpContent var14 = new DefaultHttpContent(ByteBufUtil.readBytes(var1.alloc(), var2, var12));
         this.chunkSize -= (long)var12;
         var3.add(var14);
         if (this.chunkSize != 0L) {
            return;
         }

         this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_DELIMITER);
      case READ_CHUNK_DELIMITER:
         do {
            while(true) {
               byte var13 = var2.readByte();
               if (var13 == 13) {
                  break;
               }

               if (var13 == 10) {
                  this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_SIZE);
                  return;
               }

               this.checkpoint();
            }
         } while(var2.readByte() != 10);

         this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_SIZE);
         return;
      case READ_CHUNK_FOOTER:
         try {
            LastHttpContent var4 = this.readTrailingHeaders(var2);
            var3.add(var4);
            this.reset();
            return;
         } catch (Exception var7) {
            var3.add(this.invalidChunk(var7));
            return;
         }
      case BAD_MESSAGE:
         var2.skipBytes(this.actualReadableBytes());
      case UPGRADED:
      default:
         return;
      }

      try {
         HttpObjectDecoder.State var16 = this.readHeaders(var2);
         this.checkpoint(var16);
         if (var16 == HttpObjectDecoder.State.READ_CHUNK_SIZE) {
            if (!this.chunkedSupported) {
               throw new IllegalArgumentException("Chunked messages not supported");
            } else {
               var3.add(this.message);
            }
         } else if (var16 == HttpObjectDecoder.State.SKIP_CONTROL_CHARS) {
            var3.add(this.message);
            var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
            this.reset();
         } else {
            long var18 = this.contentLength();
            if (var18 == 0L || var18 == -1L && this.isDecodingRequest()) {
               var3.add(this.message);
               var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
               this.reset();
            } else if (!$assertionsDisabled && var16 != HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT && var16 != HttpObjectDecoder.State.READ_VARIABLE_LENGTH_CONTENT) {
               throw new AssertionError();
            } else {
               var3.add(this.message);
               if (var16 == HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT) {
                  this.chunkSize = var18;
               }

            }
         }
      } catch (Exception var10) {
         var3.add(this.invalidMessage(var10));
      }
   }

   protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      this.decode(var1, var2, var3);
      if (this.message != null) {
         boolean var4;
         if (this.isDecodingRequest()) {
            var4 = true;
         } else {
            var4 = this.contentLength() > 0L;
         }

         this.reset();
         if (!var4) {
            var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
         }
      }

   }

   private void reset() {
      HttpMessage var1 = this.message;
      this.message = null;
      this.contentLength = Long.MIN_VALUE;
      if (!this.isDecodingRequest()) {
         HttpResponse var2 = (HttpResponse)var1;
         if (var2 != null && var2.getStatus().code() == 101) {
            this.checkpoint(HttpObjectDecoder.State.UPGRADED);
            return;
         }
      }

      this.checkpoint(HttpObjectDecoder.State.SKIP_CONTROL_CHARS);
   }

   private HttpMessage invalidMessage(Exception var1) {
      this.checkpoint(HttpObjectDecoder.State.BAD_MESSAGE);
      if (this.message != null) {
         this.message.setDecoderResult(DecoderResult.failure(var1));
      } else {
         this.message = this.createInvalidMessage();
         this.message.setDecoderResult(DecoderResult.failure(var1));
      }

      HttpMessage var2 = this.message;
      this.message = null;
      return var2;
   }

   private HttpContent invalidChunk(Exception var1) {
      this.checkpoint(HttpObjectDecoder.State.BAD_MESSAGE);
      DefaultHttpContent var2 = new DefaultHttpContent(Unpooled.EMPTY_BUFFER);
      var2.setDecoderResult(DecoderResult.failure(var1));
      return var2;
   }

   private static void skipControlCharacters(ByteBuf var0) {
      char var1;
      do {
         var1 = (char)var0.readUnsignedByte();
      } while(Character.isISOControl(var1) || Character.isWhitespace(var1));

      var0.readerIndex(var0.readerIndex() - 1);
   }

   private HttpObjectDecoder.State readHeaders(ByteBuf var1) {
      this.headerSize = 0;
      HttpMessage var2 = this.message;
      HttpHeaders var3 = var2.headers();
      AppendableCharSequence var4 = this.headerParser.parse(var1);
      String var5 = null;
      String var6 = null;
      if (var4.length() > 0) {
         var3.clear();

         do {
            char var7 = var4.charAt(0);
            if (var5 == null || var7 != ' ' && var7 != '\t') {
               if (var5 != null) {
                  var3.add((String)var5, (Object)var6);
               }

               String[] var8 = splitHeader(var4);
               var5 = var8[0];
               var6 = var8[1];
            } else {
               var6 = var6 + ' ' + var4.toString().trim();
            }

            var4 = this.headerParser.parse(var1);
         } while(var4.length() > 0);

         if (var5 != null) {
            var3.add((String)var5, (Object)var6);
         }
      }

      HttpObjectDecoder.State var9;
      if (var2 != false) {
         HttpHeaders.removeTransferEncodingChunked(var2);
         var9 = HttpObjectDecoder.State.SKIP_CONTROL_CHARS;
      } else if (HttpHeaders.isTransferEncodingChunked(var2)) {
         var9 = HttpObjectDecoder.State.READ_CHUNK_SIZE;
      } else if (this.contentLength() >= 0L) {
         var9 = HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT;
      } else {
         var9 = HttpObjectDecoder.State.READ_VARIABLE_LENGTH_CONTENT;
      }

      return var9;
   }

   private long contentLength() {
      if (this.contentLength == Long.MIN_VALUE) {
         this.contentLength = HttpHeaders.getContentLength(this.message, -1L);
      }

      return this.contentLength;
   }

   private LastHttpContent readTrailingHeaders(ByteBuf var1) {
      this.headerSize = 0;
      AppendableCharSequence var2 = this.headerParser.parse(var1);
      String var3 = null;
      if (var2.length() <= 0) {
         return LastHttpContent.EMPTY_LAST_CONTENT;
      } else {
         DefaultLastHttpContent var4 = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);

         do {
            char var5 = var2.charAt(0);
            if (var3 != null && (var5 == ' ' || var5 == '\t')) {
               List var9 = var4.trailingHeaders().getAll(var3);
               if (!var9.isEmpty()) {
                  int var10 = var9.size() - 1;
                  String var8 = (String)var9.get(var10) + var2.toString().trim();
                  var9.set(var10, var8);
               }
            } else {
               String[] var6 = splitHeader(var2);
               String var7 = var6[0];
               if (!HttpHeaders.equalsIgnoreCase(var7, "Content-Length") && !HttpHeaders.equalsIgnoreCase(var7, "Transfer-Encoding") && !HttpHeaders.equalsIgnoreCase(var7, "Trailer")) {
                  var4.trailingHeaders().add((String)var7, (Object)var6[1]);
               }

               var3 = var7;
            }

            var2 = this.headerParser.parse(var1);
         } while(var2.length() > 0);

         return var4;
      }
   }

   protected abstract boolean isDecodingRequest();

   protected abstract HttpMessage createMessage(String[] var1) throws Exception;

   protected abstract HttpMessage createInvalidMessage();

   private static int getChunkSize(String var0) {
      var0 = var0.trim();

      for(int var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if (var2 == ';' || Character.isWhitespace(var2) || Character.isISOControl(var2)) {
            var0 = var0.substring(0, var1);
            break;
         }
      }

      return Integer.parseInt(var0, 16);
   }

   private static String[] splitInitialLine(AppendableCharSequence var0) {
      int var1 = findNonWhitespace(var0, 0);
      int var2 = findWhitespace(var0, var1);
      int var3 = findNonWhitespace(var0, var2);
      int var4 = findWhitespace(var0, var3);
      int var5 = findNonWhitespace(var0, var4);
      int var6 = findEndOfString(var0);
      return new String[]{var0.substring(var1, var2), var0.substring(var3, var4), var5 < var6 ? var0.substring(var5, var6) : ""};
   }

   private static String[] splitHeader(AppendableCharSequence var0) {
      int var1 = var0.length();
      int var2 = findNonWhitespace(var0, 0);

      int var3;
      for(var3 = var2; var3 < var1; ++var3) {
         char var7 = var0.charAt(var3);
         if (var7 == ':' || Character.isWhitespace(var7)) {
            break;
         }
      }

      int var4;
      for(var4 = var3; var4 < var1; ++var4) {
         if (var0.charAt(var4) == ':') {
            ++var4;
            break;
         }
      }

      int var5 = findNonWhitespace(var0, var4);
      if (var5 == var1) {
         return new String[]{var0.substring(var2, var3), ""};
      } else {
         int var6 = findEndOfString(var0);
         return new String[]{var0.substring(var2, var3), var0.substring(var5, var6)};
      }
   }

   private static int findNonWhitespace(CharSequence var0, int var1) {
      int var2;
      for(var2 = var1; var2 < var0.length() && Character.isWhitespace(var0.charAt(var2)); ++var2) {
      }

      return var2;
   }

   private static int findWhitespace(CharSequence var0, int var1) {
      int var2;
      for(var2 = var1; var2 < var0.length() && !Character.isWhitespace(var0.charAt(var2)); ++var2) {
      }

      return var2;
   }

   private static int findEndOfString(CharSequence var0) {
      int var1;
      for(var1 = var0.length(); var1 > 0 && Character.isWhitespace(var0.charAt(var1 - 1)); --var1) {
      }

      return var1;
   }

   static int access$002(HttpObjectDecoder var0, int var1) {
      return var0.headerSize = var1;
   }

   static int access$008(HttpObjectDecoder var0) {
      return var0.headerSize++;
   }

   static int access$000(HttpObjectDecoder var0) {
      return var0.headerSize;
   }

   static int access$100(HttpObjectDecoder var0) {
      return var0.maxHeaderSize;
   }

   static int access$200(HttpObjectDecoder var0) {
      return var0.maxInitialLineLength;
   }

   private final class LineParser implements ByteBufProcessor {
      private final AppendableCharSequence seq;
      private int size;
      final HttpObjectDecoder this$0;

      LineParser(HttpObjectDecoder var1, AppendableCharSequence var2) {
         this.this$0 = var1;
         this.seq = var2;
      }

      public AppendableCharSequence parse(ByteBuf var1) {
         this.seq.reset();
         this.size = 0;
         int var2 = var1.forEachByte(this);
         var1.readerIndex(var2 + 1);
         return this.seq;
      }

      public boolean process(byte var1) throws Exception {
         char var2 = (char)var1;
         if (var2 == '\r') {
            return true;
         } else if (var2 == '\n') {
            return false;
         } else if (this.size >= HttpObjectDecoder.access$200(this.this$0)) {
            throw new TooLongFrameException("An HTTP line is larger than " + HttpObjectDecoder.access$200(this.this$0) + " bytes.");
         } else {
            ++this.size;
            this.seq.append(var2);
            return true;
         }
      }
   }

   private final class HeaderParser implements ByteBufProcessor {
      private final AppendableCharSequence seq;
      final HttpObjectDecoder this$0;

      HeaderParser(HttpObjectDecoder var1, AppendableCharSequence var2) {
         this.this$0 = var1;
         this.seq = var2;
      }

      public AppendableCharSequence parse(ByteBuf var1) {
         this.seq.reset();
         HttpObjectDecoder.access$002(this.this$0, 0);
         int var2 = var1.forEachByte(this);
         var1.readerIndex(var2 + 1);
         return this.seq;
      }

      public boolean process(byte var1) throws Exception {
         char var2 = (char)var1;
         HttpObjectDecoder.access$008(this.this$0);
         if (var2 == '\r') {
            return true;
         } else if (var2 == '\n') {
            return false;
         } else if (HttpObjectDecoder.access$000(this.this$0) >= HttpObjectDecoder.access$100(this.this$0)) {
            throw new TooLongFrameException("HTTP header is larger than " + HttpObjectDecoder.access$100(this.this$0) + " bytes.");
         } else {
            this.seq.append(var2);
            return true;
         }
      }
   }

   static enum State {
      SKIP_CONTROL_CHARS,
      READ_INITIAL,
      READ_HEADER,
      READ_VARIABLE_LENGTH_CONTENT,
      READ_FIXED_LENGTH_CONTENT,
      READ_CHUNK_SIZE,
      READ_CHUNKED_CONTENT,
      READ_CHUNK_DELIMITER,
      READ_CHUNK_FOOTER,
      BAD_MESSAGE,
      UPGRADED;

      private static final HttpObjectDecoder.State[] $VALUES = new HttpObjectDecoder.State[]{SKIP_CONTROL_CHARS, READ_INITIAL, READ_HEADER, READ_VARIABLE_LENGTH_CONTENT, READ_FIXED_LENGTH_CONTENT, READ_CHUNK_SIZE, READ_CHUNKED_CONTENT, READ_CHUNK_DELIMITER, READ_CHUNK_FOOTER, BAD_MESSAGE, UPGRADED};
   }
}
