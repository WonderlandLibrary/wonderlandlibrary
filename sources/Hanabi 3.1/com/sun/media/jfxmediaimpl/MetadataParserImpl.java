package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MetadataListener;
import com.sun.media.jfxmedia.locator.ConnectionHolder;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.EOFException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public abstract class MetadataParserImpl extends Thread implements MetadataParser {
   private String[] FLV_VIDEO_CODEC_NAME = new String[]{"Unsupported", "JPEG Video (Unsupported)", "Sorenson H.263 Video", "Flash Screen Video", "On2 VP6 Video", "On2 VP6-Alpha Video", "Unsupported", "H.264 Video", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported", "Unsupported"};
   private final List listeners = new ArrayList();
   private Map metadata = new HashMap();
   private Locator locator = null;
   private ConnectionHolder connectionHolder = null;
   private ByteBuffer buffer = null;
   private Map rawMetaMap = null;
   protected ByteBuffer rawMetaBlob = null;
   private boolean parsingRawMetadata = false;
   private int length = 0;
   private int index = 0;
   private int streamPosition = 0;

   public MetadataParserImpl(Locator var1) {
      this.locator = var1;
   }

   public void addListener(MetadataListener var1) {
      synchronized(this.listeners) {
         if (var1 != null) {
            this.listeners.add(new WeakReference(var1));
         }

      }
   }

   public void removeListener(MetadataListener var1) {
      synchronized(this.listeners) {
         if (var1 != null) {
            ListIterator var3 = this.listeners.listIterator();

            while(true) {
               MetadataListener var4;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  var4 = (MetadataListener)((WeakReference)var3.next()).get();
               } while(var4 != null && var4 != var1);

               var3.remove();
            }
         }
      }
   }

   public void startParser() throws IOException {
      this.start();
   }

   public void stopParser() {
      if (this.connectionHolder != null) {
         this.connectionHolder.closeConnection();
      }

   }

   public void run() {
      try {
         this.connectionHolder = this.locator.createConnectionHolder();
         this.parse();
      } catch (IOException var2) {
      }

   }

   protected abstract void parse();

   protected void addMetadataItem(String var1, Object var2) {
      this.metadata.put(var1, var2);
   }

   protected void done() {
      synchronized(this.listeners) {
         if (!this.metadata.isEmpty()) {
            ListIterator var2 = this.listeners.listIterator();

            while(var2.hasNext()) {
               MetadataListener var3 = (MetadataListener)((WeakReference)var2.next()).get();
               if (var3 != null) {
                  var3.onMetadata(this.metadata);
               } else {
                  var2.remove();
               }
            }
         }

      }
   }

   protected int getStreamPosition() {
      return this.parsingRawMetadata ? this.rawMetaBlob.position() : this.streamPosition;
   }

   protected void startRawMetadata(int var1) {
      this.rawMetaBlob = ByteBuffer.allocate(var1);
   }

   private void adjustRawMetadataSize(int var1) {
      if (this.rawMetaBlob.remaining() < var1) {
         int var2 = this.rawMetaBlob.position();
         int var3 = var2 + var1;
         ByteBuffer var4 = ByteBuffer.allocate(var3);
         this.rawMetaBlob.position(0);
         var4.put(this.rawMetaBlob.array(), 0, var2);
         this.rawMetaBlob = var4;
      }

   }

   protected void readRawMetadata(int var1) throws IOException {
      byte[] var2 = this.getBytes(var1);
      this.adjustRawMetadataSize(var1);
      if (null != var2) {
         this.rawMetaBlob.put(var2);
      }

   }

   protected void stuffRawMetadata(byte[] var1, int var2, int var3) {
      if (null != this.rawMetaBlob) {
         this.adjustRawMetadataSize(var3);
         this.rawMetaBlob.put(var1, var2, var3);
      }

   }

   protected void disposeRawMetadata() {
      this.parsingRawMetadata = false;
      this.rawMetaBlob = null;
   }

   protected void setParseRawMetadata(boolean var1) {
      if (null == this.rawMetaBlob) {
         this.parsingRawMetadata = false;
      } else {
         if (var1) {
            this.rawMetaBlob.position(0);
         }

         this.parsingRawMetadata = var1;
      }
   }

   protected void addRawMetadata(String var1) {
      if (null != this.rawMetaBlob) {
         if (null == this.rawMetaMap) {
            this.rawMetaMap = new HashMap();
            this.metadata.put("raw metadata", Collections.unmodifiableMap(this.rawMetaMap));
         }

         this.rawMetaMap.put(var1, this.rawMetaBlob.asReadOnlyBuffer());
      }
   }

   protected void skipBytes(int var1) throws IOException, EOFException {
      if (this.parsingRawMetadata) {
         this.rawMetaBlob.position(this.rawMetaBlob.position() + var1);
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            this.getNextByte();
         }

      }
   }

   protected byte getNextByte() throws IOException, EOFException {
      if (this.parsingRawMetadata) {
         return this.rawMetaBlob.get();
      } else {
         if (this.buffer == null) {
            this.buffer = this.connectionHolder.getBuffer();
            this.length = this.connectionHolder.readNextBlock();
         }

         if (this.index >= this.length) {
            this.length = this.connectionHolder.readNextBlock();
            if (this.length < 1) {
               throw new EOFException();
            }

            this.index = 0;
         }

         byte var1 = this.buffer.get(this.index);
         ++this.index;
         ++this.streamPosition;
         return var1;
      }
   }

   protected byte[] getBytes(int var1) throws IOException, EOFException {
      byte[] var2 = new byte[var1];
      if (this.parsingRawMetadata) {
         this.rawMetaBlob.get(var2);
         return var2;
      } else {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = this.getNextByte();
         }

         return var2;
      }
   }

   protected long getLong() throws IOException, EOFException {
      if (this.parsingRawMetadata) {
         return this.rawMetaBlob.getLong();
      } else {
         long var1 = 0L;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         var1 <<= 8;
         var1 |= (long)(this.getNextByte() & 255);
         return var1;
      }
   }

   protected int getInteger() throws IOException, EOFException {
      if (this.parsingRawMetadata) {
         return this.rawMetaBlob.getInt();
      } else {
         int var1 = 0;
         var1 |= this.getNextByte() & 255;
         var1 <<= 8;
         var1 |= this.getNextByte() & 255;
         var1 <<= 8;
         var1 |= this.getNextByte() & 255;
         var1 <<= 8;
         var1 |= this.getNextByte() & 255;
         return var1;
      }
   }

   protected short getShort() throws IOException, EOFException {
      if (this.parsingRawMetadata) {
         return this.rawMetaBlob.getShort();
      } else {
         short var1 = 0;
         var1 = (short)(var1 | this.getNextByte() & 255);
         var1 = (short)(var1 << 8);
         var1 = (short)(var1 | this.getNextByte() & 255);
         return var1;
      }
   }

   protected double getDouble() throws IOException, EOFException {
      if (this.parsingRawMetadata) {
         return this.rawMetaBlob.getDouble();
      } else {
         long var1 = this.getLong();
         return Double.longBitsToDouble(var1);
      }
   }

   protected String getString(int var1, Charset var2) throws IOException, EOFException {
      byte[] var3 = this.getBytes(var1);
      return new String(var3, 0, var1, var2);
   }

   protected int getU24() throws IOException, EOFException {
      int var1 = 0;
      var1 |= this.getNextByte() & 255;
      var1 <<= 8;
      var1 |= this.getNextByte() & 255;
      var1 <<= 8;
      var1 |= this.getNextByte() & 255;
      return var1;
   }

   protected Object convertValue(String var1, Object var2) {
      Double var3;
      if (var1.equals("duration") && var2 instanceof Double) {
         var3 = (Double)var2 * 1000.0;
         return var3.longValue();
      } else {
         String var5;
         if (var1.equals("duration") && var2 instanceof String) {
            var5 = (String)var2;
            return Long.valueOf(var5.trim());
         } else if (!var1.equals("width") && !var1.equals("height")) {
            if (var1.equals("framerate")) {
               return var2;
            } else if (var1.equals("videocodecid")) {
               int var6 = ((Double)var2).intValue();
               return var6 < this.FLV_VIDEO_CODEC_NAME.length ? this.FLV_VIDEO_CODEC_NAME[var6] : null;
            } else if (var1.equals("audiocodecid")) {
               return "MPEG 1 Audio";
            } else if (var1.equals("creationdate")) {
               return ((String)var2).trim();
            } else {
               String[] var4;
               if (!var1.equals("track number") && !var1.equals("disc number")) {
                  if (!var1.equals("track count") && !var1.equals("disc count")) {
                     if (var1.equals("album")) {
                        return var2;
                     }

                     if (var1.equals("artist")) {
                        return var2;
                     }

                     if (var1.equals("genre")) {
                        return var2;
                     }

                     if (var1.equals("title")) {
                        return var2;
                     }

                     if (var1.equals("album artist")) {
                        return var2;
                     }

                     if (var1.equals("comment")) {
                        return var2;
                     }

                     if (var1.equals("composer")) {
                        return var2;
                     }

                     if (var1.equals("year")) {
                        var5 = (String)var2;
                        return Integer.valueOf(var5.trim());
                     }
                  } else {
                     var4 = ((String)var2).split("/");
                     if (var4.length == 2) {
                        return Integer.valueOf(var4[1].trim());
                     }
                  }
               } else {
                  var4 = ((String)var2).split("/");
                  if (var4.length == 2) {
                     return Integer.valueOf(var4[0].trim());
                  }
               }

               return null;
            }
         } else {
            var3 = (Double)var2;
            return var3.intValue();
         }
      }
   }
}
