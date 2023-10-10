package io.netty.handler.codec.serialization;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

class CompactObjectInputStream extends ObjectInputStream {
   private final ClassResolver classResolver;

   CompactObjectInputStream(InputStream var1, ClassResolver var2) throws IOException {
      super(var1);
      this.classResolver = var2;
   }

   protected void readStreamHeader() throws IOException {
      int var1 = this.readByte() & 255;
      if (var1 != 5) {
         throw new StreamCorruptedException("Unsupported version: " + var1);
      }
   }

   protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
      int var1 = this.read();
      if (var1 < 0) {
         throw new EOFException();
      } else {
         switch(var1) {
         case 0:
            return super.readClassDescriptor();
         case 1:
            String var2 = this.readUTF();
            Class var3 = this.classResolver.resolve(var2);
            return ObjectStreamClass.lookupAny(var3);
         default:
            throw new StreamCorruptedException("Unexpected class descriptor type: " + var1);
         }
      }
   }

   protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
      Class var2;
      try {
         var2 = this.classResolver.resolve(var1.getName());
      } catch (ClassNotFoundException var4) {
         var2 = super.resolveClass(var1);
      }

      return var2;
   }
}
