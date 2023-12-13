package org.newdawn.slick.openal;

import java.io.IOException;

interface AudioInputStream {
   int getChannels();

   int getRate();

   int read() throws IOException;

   int read(byte[] var1) throws IOException;

   int read(byte[] var1, int var2, int var3) throws IOException;

   boolean atEnd();

   void close() throws IOException;
}
