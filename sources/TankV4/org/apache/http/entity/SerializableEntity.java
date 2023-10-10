package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class SerializableEntity extends AbstractHttpEntity {
   private byte[] objSer;
   private Serializable objRef;

   public SerializableEntity(Serializable var1, boolean var2) throws IOException {
      Args.notNull(var1, "Source object");
      if (var2) {
         this.createBytes(var1);
      } else {
         this.objRef = var1;
      }

   }

   public SerializableEntity(Serializable var1) {
      Args.notNull(var1, "Source object");
      this.objRef = var1;
   }

   private void createBytes(Serializable var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      ObjectOutputStream var3 = new ObjectOutputStream(var2);
      var3.writeObject(var1);
      var3.flush();
      this.objSer = var2.toByteArray();
   }

   public InputStream getContent() throws IOException, IllegalStateException {
      if (this.objSer == null) {
         this.createBytes(this.objRef);
      }

      return new ByteArrayInputStream(this.objSer);
   }

   public long getContentLength() {
      return this.objSer == null ? -1L : (long)this.objSer.length;
   }

   public boolean isRepeatable() {
      return true;
   }

   public boolean isStreaming() {
      return this.objSer == null;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      if (this.objSer == null) {
         ObjectOutputStream var2 = new ObjectOutputStream(var1);
         var2.writeObject(this.objRef);
         var2.flush();
      } else {
         var1.write(this.objSer);
         var1.flush();
      }

   }
}
