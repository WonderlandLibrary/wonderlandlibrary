package org.lwjgl.opencl;

interface InfoUtil {
   int getInfoInt(CLObject var1, int var2);

   long getInfoSize(CLObject var1, int var2);

   long[] getInfoSizeArray(CLObject var1, int var2);

   long getInfoLong(CLObject var1, int var2);

   String getInfoString(CLObject var1, int var2);
}
