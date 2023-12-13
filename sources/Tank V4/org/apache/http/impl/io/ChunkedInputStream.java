package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class ChunkedInputStream extends InputStream {
   private static final int CHUNK_LEN = 1;
   private static final int CHUNK_DATA = 2;
   private static final int CHUNK_CRLF = 3;
   private static final int BUFFER_SIZE = 2048;
   private final SessionInputBuffer in;
   private final CharArrayBuffer buffer;
   private int state;
   private int chunkSize;
   private int pos;
   private boolean eof = false;
   private boolean closed = false;
   private Header[] footers = new Header[0];

   public ChunkedInputStream(SessionInputBuffer var1) {
      this.in = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
      this.pos = 0;
      this.buffer = new CharArrayBuffer(16);
      this.state = 1;
   }

   public int available() throws IOException {
      if (this.in instanceof BufferInfo) {
         int var1 = ((BufferInfo)this.in).length();
         return Math.min(var1, this.chunkSize - this.pos);
      } else {
         return 0;
      }
   }

   public int read() throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.eof) {
         return -1;
      } else {
         if (this.state != 2) {
            this.nextChunk();
            if (this.eof) {
               return -1;
            }
         }

         int var1 = this.in.read();
         if (var1 != -1) {
            ++this.pos;
            if (this.pos >= this.chunkSize) {
               this.state = 3;
            }
         }

         return var1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.eof) {
         return -1;
      } else {
         if (this.state != 2) {
            this.nextChunk();
            if (this.eof) {
               return -1;
            }
         }

         int var4 = this.in.read(var1, var2, Math.min(var3, this.chunkSize - this.pos));
         if (var4 != -1) {
            this.pos += var4;
            if (this.pos >= this.chunkSize) {
               this.state = 3;
            }

            return var4;
         } else {
            this.eof = true;
            throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
         }
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   private void nextChunk() throws IOException {
      this.chunkSize = this.getChunkSize();
      if (this.chunkSize < 0) {
         throw new MalformedChunkCodingException("Negative chunk size");
      } else {
         this.state = 2;
         this.pos = 0;
         if (this.chunkSize == 0) {
            this.eof = true;
            this.parseTrailerHeaders();
         }

      }
   }

   private int getChunkSize() throws IOException {
      int var1 = this.state;
      switch(var1) {
      case 3:
         this.buffer.clear();
         int var2 = this.in.readLine(this.buffer);
         if (var2 == -1) {
            return 0;
         } else if (!this.buffer.isEmpty()) {
            throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
         } else {
            this.state = 1;
         }
      case 1:
         this.buffer.clear();
         int var3 = this.in.readLine(this.buffer);
         if (var3 == -1) {
            return 0;
         } else {
            int var4 = this.buffer.indexOf(59);
            if (var4 < 0) {
               var4 = this.buffer.length();
            }

            try {
               return Integer.parseInt(this.buffer.substringTrimmed(0, var4), 16);
            } catch (NumberFormatException var6) {
               throw new MalformedChunkCodingException("Bad chunk header");
            }
         }
      default:
         throw new IllegalStateException("Inconsistent codec state");
      }
   }

   private void parseTrailerHeaders() throws IOException {
      try {
         this.footers = AbstractMessageParser.parseHeaders(this.in, -1, -1, (LineParser)null);
      } catch (HttpException var3) {
         MalformedChunkCodingException var2 = new MalformedChunkCodingException("Invalid footer: " + var3.getMessage());
         var2.initCause(var3);
         throw var2;
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         if (!this.eof) {
            byte[] var1 = new byte[2048];

            while(this.read(var1) >= 0) {
            }
         }

         this.eof = true;
         this.closed = true;
      }

   }

   public Header[] getFooters() {
      return (Header[])this.footers.clone();
   }
}
