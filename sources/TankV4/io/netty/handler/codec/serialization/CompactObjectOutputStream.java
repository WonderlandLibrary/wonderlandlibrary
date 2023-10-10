package io.netty.handler.codec.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

class CompactObjectOutputStream extends ObjectOutputStream {
   static final int TYPE_FAT_DESCRIPTOR = 0;
   static final int TYPE_THIN_DESCRIPTOR = 1;

   CompactObjectOutputStream(OutputStream var1) throws IOException {
      super(var1);
   }

   protected void writeStreamHeader() throws IOException {
      this.writeByte(5);
   }

   protected void writeClassDescriptor(ObjectStreamClass var1) throws IOException {
      Class var2 = var1.forClass();
      if (!var2.isPrimitive() && !var2.isArray() && !var2.isInterface() && var1.getSerialVersionUID() != 0L) {
         this.write(1);
         this.writeUTF(var1.getName());
      } else {
         this.write(0);
         super.writeClassDescriptor(var1);
      }

   }
}
