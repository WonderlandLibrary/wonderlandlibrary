package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public abstract class AbstractHttpData extends AbstractReferenceCounted implements HttpData {
   private static final Pattern STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
   private static final Pattern REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
   protected final String name;
   protected long definedSize;
   protected long size;
   protected Charset charset;
   protected boolean completed;

   protected AbstractHttpData(String var1, Charset var2, long var3) {
      this.charset = HttpConstants.DEFAULT_CHARSET;
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         var1 = REPLACE_PATTERN.matcher(var1).replaceAll(" ");
         var1 = STRIP_PATTERN.matcher(var1).replaceAll("");
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("empty name");
         } else {
            this.name = var1;
            if (var2 != null) {
               this.setCharset(var2);
            }

            this.definedSize = var3;
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public Charset getCharset() {
      return this.charset;
   }

   public void setCharset(Charset var1) {
      if (var1 == null) {
         throw new NullPointerException("charset");
      } else {
         this.charset = var1;
      }
   }

   public long length() {
      return this.size;
   }

   public ByteBuf content() {
      try {
         return this.getByteBuf();
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   protected void deallocate() {
      this.delete();
   }

   public HttpData retain() {
      super.retain();
      return this;
   }

   public HttpData retain(int var1) {
      super.retain(var1);
      return this;
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   public ByteBufHolder retain(int var1) {
      return this.retain(var1);
   }

   public ByteBufHolder retain() {
      return this.retain();
   }
}
