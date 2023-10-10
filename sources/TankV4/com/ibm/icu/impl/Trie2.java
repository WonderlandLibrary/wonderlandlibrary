package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Trie2 implements Iterable {
   private static Trie2.ValueMapper defaultValueMapper = new Trie2.ValueMapper() {
      public int map(int var1) {
         return var1;
      }
   };
   Trie2.UTrie2Header header;
   char[] index;
   int data16;
   int[] data32;
   int indexLength;
   int dataLength;
   int index2NullOffset;
   int initialValue;
   int errorValue;
   int highStart;
   int highValueIndex;
   int dataNullOffset;
   int fHash;
   static final int UTRIE2_OPTIONS_VALUE_BITS_MASK = 15;
   static final int UTRIE2_SHIFT_1 = 11;
   static final int UTRIE2_SHIFT_2 = 5;
   static final int UTRIE2_SHIFT_1_2 = 6;
   static final int UTRIE2_OMITTED_BMP_INDEX_1_LENGTH = 32;
   static final int UTRIE2_CP_PER_INDEX_1_ENTRY = 2048;
   static final int UTRIE2_INDEX_2_BLOCK_LENGTH = 64;
   static final int UTRIE2_INDEX_2_MASK = 63;
   static final int UTRIE2_DATA_BLOCK_LENGTH = 32;
   static final int UTRIE2_DATA_MASK = 31;
   static final int UTRIE2_INDEX_SHIFT = 2;
   static final int UTRIE2_DATA_GRANULARITY = 4;
   static final int UTRIE2_INDEX_2_OFFSET = 0;
   static final int UTRIE2_LSCP_INDEX_2_OFFSET = 2048;
   static final int UTRIE2_LSCP_INDEX_2_LENGTH = 32;
   static final int UTRIE2_INDEX_2_BMP_LENGTH = 2080;
   static final int UTRIE2_UTF8_2B_INDEX_2_OFFSET = 2080;
   static final int UTRIE2_UTF8_2B_INDEX_2_LENGTH = 32;
   static final int UTRIE2_INDEX_1_OFFSET = 2112;
   static final int UTRIE2_MAX_INDEX_1_LENGTH = 512;
   static final int UTRIE2_BAD_UTF8_DATA_OFFSET = 128;
   static final int UTRIE2_DATA_START_OFFSET = 192;
   static final int UNEWTRIE2_INDEX_GAP_OFFSET = 2080;
   static final int UNEWTRIE2_INDEX_GAP_LENGTH = 576;
   static final int UNEWTRIE2_MAX_INDEX_2_LENGTH = 35488;
   static final int UNEWTRIE2_INDEX_1_LENGTH = 544;
   static final int UNEWTRIE2_MAX_DATA_LENGTH = 1115264;

   public static Trie2 createFromSerialized(InputStream var0) throws IOException {
      DataInputStream var1 = new DataInputStream(var0);
      boolean var2 = false;
      Trie2.UTrie2Header var3 = new Trie2.UTrie2Header();
      var3.signature = var1.readInt();
      switch(var3.signature) {
      case 845771348:
         var2 = true;
         var3.signature = Integer.reverseBytes(var3.signature);
         break;
      case 1416784178:
         var2 = false;
         break;
      default:
         throw new IllegalArgumentException("Stream does not contain a serialized UTrie2");
      }

      var3.options = swapShort(var2, var1.readUnsignedShort());
      var3.indexLength = swapShort(var2, var1.readUnsignedShort());
      var3.shiftedDataLength = swapShort(var2, var1.readUnsignedShort());
      var3.index2NullOffset = swapShort(var2, var1.readUnsignedShort());
      var3.dataNullOffset = swapShort(var2, var1.readUnsignedShort());
      var3.shiftedHighStart = swapShort(var2, var1.readUnsignedShort());
      if ((var3.options & 15) > 1) {
         throw new IllegalArgumentException("UTrie2 serialized format error.");
      } else {
         Trie2.ValueWidth var4;
         Object var5;
         if ((var3.options & 15) == 0) {
            var4 = Trie2.ValueWidth.BITS_16;
            var5 = new Trie2_16();
         } else {
            var4 = Trie2.ValueWidth.BITS_32;
            var5 = new Trie2_32();
         }

         ((Trie2)var5).header = var3;
         ((Trie2)var5).indexLength = var3.indexLength;
         ((Trie2)var5).dataLength = var3.shiftedDataLength << 2;
         ((Trie2)var5).index2NullOffset = var3.index2NullOffset;
         ((Trie2)var5).dataNullOffset = var3.dataNullOffset;
         ((Trie2)var5).highStart = var3.shiftedHighStart << 11;
         ((Trie2)var5).highValueIndex = ((Trie2)var5).dataLength - 4;
         if (var4 == Trie2.ValueWidth.BITS_16) {
            ((Trie2)var5).highValueIndex += ((Trie2)var5).indexLength;
         }

         int var6 = ((Trie2)var5).indexLength;
         if (var4 == Trie2.ValueWidth.BITS_16) {
            var6 += ((Trie2)var5).dataLength;
         }

         ((Trie2)var5).index = new char[var6];

         int var7;
         for(var7 = 0; var7 < ((Trie2)var5).indexLength; ++var7) {
            ((Trie2)var5).index[var7] = swapChar(var2, var1.readChar());
         }

         if (var4 == Trie2.ValueWidth.BITS_16) {
            ((Trie2)var5).data16 = ((Trie2)var5).indexLength;

            for(var7 = 0; var7 < ((Trie2)var5).dataLength; ++var7) {
               ((Trie2)var5).index[((Trie2)var5).data16 + var7] = swapChar(var2, var1.readChar());
            }
         } else {
            ((Trie2)var5).data32 = new int[((Trie2)var5).dataLength];

            for(var7 = 0; var7 < ((Trie2)var5).dataLength; ++var7) {
               ((Trie2)var5).data32[var7] = swapInt(var2, var1.readInt());
            }
         }

         switch(var4) {
         case BITS_16:
            ((Trie2)var5).data32 = null;
            ((Trie2)var5).initialValue = ((Trie2)var5).index[((Trie2)var5).dataNullOffset];
            ((Trie2)var5).errorValue = ((Trie2)var5).index[((Trie2)var5).data16 + 128];
            break;
         case BITS_32:
            ((Trie2)var5).data16 = 0;
            ((Trie2)var5).initialValue = ((Trie2)var5).data32[((Trie2)var5).dataNullOffset];
            ((Trie2)var5).errorValue = ((Trie2)var5).data32[128];
            break;
         default:
            throw new IllegalArgumentException("UTrie2 serialized format error.");
         }

         return (Trie2)var5;
      }
   }

   private static int swapShort(boolean var0, int var1) {
      return var0 ? Short.reverseBytes((short)var1) & '\uffff' : var1;
   }

   private static char swapChar(boolean var0, char var1) {
      return var0 ? (char)Short.reverseBytes((short)var1) : var1;
   }

   private static int swapInt(boolean var0, int var1) {
      return var0 ? Integer.reverseBytes(var1) : var1;
   }

   public static int getVersion(InputStream var0, boolean var1) throws IOException {
      if (!var0.markSupported()) {
         throw new IllegalArgumentException("Input stream must support mark().");
      } else {
         var0.mark(4);
         byte[] var2 = new byte[4];
         int var3 = var0.read(var2);
         var0.reset();
         if (var3 != var2.length) {
            return 0;
         } else if (var2[0] == 84 && var2[1] == 114 && var2[2] == 105 && var2[3] == 101) {
            return 1;
         } else if (var2[0] == 84 && var2[1] == 114 && var2[2] == 105 && var2[3] == 50) {
            return 2;
         } else {
            if (var1) {
               if (var2[0] == 101 && var2[1] == 105 && var2[2] == 114 && var2[3] == 84) {
                  return 1;
               }

               if (var2[0] == 50 && var2[1] == 105 && var2[2] == 114 && var2[3] == 84) {
                  return 2;
               }
            }

            return 0;
         }
      }
   }

   public abstract int get(int var1);

   public abstract int getFromU16SingleLead(char var1);

   public final boolean equals(Object var1) {
      if (!(var1 instanceof Trie2)) {
         return false;
      } else {
         Trie2 var2 = (Trie2)var1;
         Iterator var4 = var2.iterator();
         Iterator var5 = this.iterator();

         Trie2.Range var3;
         Trie2.Range var6;
         do {
            if (!var5.hasNext()) {
               if (var4.hasNext()) {
                  return false;
               }

               if (this.errorValue == var2.errorValue && this.initialValue == var2.initialValue) {
                  return true;
               }

               return false;
            }

            var6 = (Trie2.Range)var5.next();
            if (!var4.hasNext()) {
               return false;
            }

            var3 = (Trie2.Range)var4.next();
         } while(var6.equals(var3));

         return false;
      }
   }

   public int hashCode() {
      if (this.fHash == 0) {
         int var1 = initHash();

         Trie2.Range var3;
         for(Iterator var2 = this.iterator(); var2.hasNext(); var1 = hashInt(var1, var3.hashCode())) {
            var3 = (Trie2.Range)var2.next();
         }

         if (var1 == 0) {
            var1 = 1;
         }

         this.fHash = var1;
      }

      return this.fHash;
   }

   public Iterator iterator() {
      return this.iterator(defaultValueMapper);
   }

   public Iterator iterator(Trie2.ValueMapper var1) {
      return new Trie2.Trie2Iterator(this, var1);
   }

   public Iterator iteratorForLeadSurrogate(char var1, Trie2.ValueMapper var2) {
      return new Trie2.Trie2Iterator(this, var1, var2);
   }

   public Iterator iteratorForLeadSurrogate(char var1) {
      return new Trie2.Trie2Iterator(this, var1, defaultValueMapper);
   }

   protected int serializeHeader(DataOutputStream var1) throws IOException {
      byte var2 = 0;
      var1.writeInt(this.header.signature);
      var1.writeShort(this.header.options);
      var1.writeShort(this.header.indexLength);
      var1.writeShort(this.header.shiftedDataLength);
      var1.writeShort(this.header.index2NullOffset);
      var1.writeShort(this.header.dataNullOffset);
      var1.writeShort(this.header.shiftedHighStart);
      int var4 = var2 + 16;

      for(int var3 = 0; var3 < this.header.indexLength; ++var3) {
         var1.writeChar(this.index[var3]);
      }

      var4 += this.header.indexLength;
      return var4;
   }

   public Trie2.CharSequenceIterator charSequenceIterator(CharSequence var1, int var2) {
      return new Trie2.CharSequenceIterator(this, var1, var2);
   }

   int rangeEnd(int var1, int var2, int var3) {
      int var5 = Math.min(this.highStart, var2);

      int var4;
      for(var4 = var1 + 1; var4 < var5 && this.get(var4) == var3; ++var4) {
      }

      if (var4 >= this.highStart) {
         var4 = var2;
      }

      return var4 - 1;
   }

   private static int initHash() {
      return -2128831035;
   }

   private static int hashByte(int var0, int var1) {
      var0 *= 16777619;
      var0 ^= var1;
      return var0;
   }

   private static int hashUChar32(int var0, int var1) {
      var0 = hashByte(var0, var1 & 255);
      var0 = hashByte(var0, var1 >> 8 & 255);
      var0 = hashByte(var0, var1 >> 16);
      return var0;
   }

   private static int hashInt(int var0, int var1) {
      var0 = hashByte(var0, var1 & 255);
      var0 = hashByte(var0, var1 >> 8 & 255);
      var0 = hashByte(var0, var1 >> 16 & 255);
      var0 = hashByte(var0, var1 >> 24 & 255);
      return var0;
   }

   static int access$000() {
      return initHash();
   }

   static int access$100(int var0, int var1) {
      return hashUChar32(var0, var1);
   }

   static int access$200(int var0, int var1) {
      return hashInt(var0, var1);
   }

   static int access$300(int var0, int var1) {
      return hashByte(var0, var1);
   }

   class Trie2Iterator implements Iterator {
      private Trie2.ValueMapper mapper;
      private Trie2.Range returnValue;
      private int nextStart;
      private int limitCP;
      private boolean doingCodePoints;
      private boolean doLeadSurrogates;
      final Trie2 this$0;

      Trie2Iterator(Trie2 var1, Trie2.ValueMapper var2) {
         this.this$0 = var1;
         this.returnValue = new Trie2.Range();
         this.doingCodePoints = true;
         this.doLeadSurrogates = true;
         this.mapper = var2;
         this.nextStart = 0;
         this.limitCP = 1114112;
         this.doLeadSurrogates = true;
      }

      Trie2Iterator(Trie2 var1, char var2, Trie2.ValueMapper var3) {
         this.this$0 = var1;
         this.returnValue = new Trie2.Range();
         this.doingCodePoints = true;
         this.doLeadSurrogates = true;
         if (var2 >= '\ud800' && var2 <= '\udbff') {
            this.mapper = var3;
            this.nextStart = var2 - 'ퟀ' << 10;
            this.limitCP = this.nextStart + 1024;
            this.doLeadSurrogates = false;
         } else {
            throw new IllegalArgumentException("Bad lead surrogate value.");
         }
      }

      public Trie2.Range next() {
         if (this != false) {
            throw new NoSuchElementException();
         } else {
            if (this.nextStart >= this.limitCP) {
               this.doingCodePoints = false;
               this.nextStart = 55296;
            }

            boolean var1 = false;
            boolean var2 = false;
            boolean var3 = false;
            int var4;
            int var5;
            int var6;
            if (this.doingCodePoints) {
               var5 = this.this$0.get(this.nextStart);
               var6 = this.mapper.map(var5);

               for(var4 = this.this$0.rangeEnd(this.nextStart, this.limitCP, var5); var4 < this.limitCP - 1; var4 = this.this$0.rangeEnd(var4 + 1, this.limitCP, var5)) {
                  var5 = this.this$0.get(var4 + 1);
                  if (this.mapper.map(var5) != var6) {
                     break;
                  }
               }
            } else {
               var5 = this.this$0.getFromU16SingleLead((char)this.nextStart);
               var6 = this.mapper.map(var5);

               for(var4 = this.rangeEndLS((char)this.nextStart); var4 < 56319; var4 = this.rangeEndLS((char)(var4 + 1))) {
                  var5 = this.this$0.getFromU16SingleLead((char)(var4 + 1));
                  if (this.mapper.map(var5) != var6) {
                     break;
                  }
               }
            }

            this.returnValue.startCodePoint = this.nextStart;
            this.returnValue.endCodePoint = var4;
            this.returnValue.value = var6;
            this.returnValue.leadSurrogate = !this.doingCodePoints;
            this.nextStart = var4 + 1;
            return this.returnValue;
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      private int rangeEndLS(char var1) {
         if (var1 >= '\udbff') {
            return 56319;
         } else {
            int var3 = this.this$0.getFromU16SingleLead(var1);

            int var2;
            for(var2 = var1 + 1; var2 <= 56319 && this.this$0.getFromU16SingleLead((char)var2) == var3; ++var2) {
            }

            return var2 - 1;
         }
      }

      public Object next() {
         return this.next();
      }
   }

   static class UTrie2Header {
      int signature;
      int options;
      int indexLength;
      int shiftedDataLength;
      int index2NullOffset;
      int dataNullOffset;
      int shiftedHighStart;
   }

   static enum ValueWidth {
      BITS_16,
      BITS_32;

      private static final Trie2.ValueWidth[] $VALUES = new Trie2.ValueWidth[]{BITS_16, BITS_32};
   }

   public class CharSequenceIterator implements Iterator {
      private CharSequence text;
      private int textLength;
      private int index;
      private Trie2.CharSequenceValues fResults;
      final Trie2 this$0;

      CharSequenceIterator(Trie2 var1, CharSequence var2, int var3) {
         this.this$0 = var1;
         this.fResults = new Trie2.CharSequenceValues();
         this.text = var2;
         this.textLength = this.text.length();
         this.set(var3);
      }

      public void set(int var1) {
         if (var1 >= 0 && var1 <= this.textLength) {
            this.index = var1;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public final boolean hasNext() {
         return this.index < this.textLength;
      }

      public final boolean hasPrevious() {
         return this.index > 0;
      }

      public Trie2.CharSequenceValues next() {
         int var1 = Character.codePointAt(this.text, this.index);
         int var2 = this.this$0.get(var1);
         this.fResults.index = this.index;
         this.fResults.codePoint = var1;
         this.fResults.value = var2;
         ++this.index;
         if (var1 >= 65536) {
            ++this.index;
         }

         return this.fResults;
      }

      public Trie2.CharSequenceValues previous() {
         int var1 = Character.codePointBefore(this.text, this.index);
         int var2 = this.this$0.get(var1);
         --this.index;
         if (var1 >= 65536) {
            --this.index;
         }

         this.fResults.index = this.index;
         this.fResults.codePoint = var1;
         this.fResults.value = var2;
         return this.fResults;
      }

      public void remove() {
         throw new UnsupportedOperationException("Trie2.CharSequenceIterator does not support remove().");
      }

      public Object next() {
         return this.next();
      }
   }

   public static class CharSequenceValues {
      public int index;
      public int codePoint;
      public int value;
   }

   public interface ValueMapper {
      int map(int var1);
   }

   public static class Range {
      public int startCodePoint;
      public int endCodePoint;
      public int value;
      public boolean leadSurrogate;

      public boolean equals(Object var1) {
         if (var1 != null && var1.getClass().equals(this.getClass())) {
            Trie2.Range var2 = (Trie2.Range)var1;
            return this.startCodePoint == var2.startCodePoint && this.endCodePoint == var2.endCodePoint && this.value == var2.value && this.leadSurrogate == var2.leadSurrogate;
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = Trie2.access$000();
         var1 = Trie2.access$100(var1, this.startCodePoint);
         var1 = Trie2.access$100(var1, this.endCodePoint);
         var1 = Trie2.access$200(var1, this.value);
         var1 = Trie2.access$300(var1, this.leadSurrogate ? 1 : 0);
         return var1;
      }
   }
}
