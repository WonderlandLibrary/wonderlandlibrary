package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
   public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

   public void write(byte[] var1, int var2, int var3) {
   }

   public void write(int var1) {
   }

   public void write(byte[] var1) throws IOException {
   }
}
