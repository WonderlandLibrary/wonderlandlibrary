package com.sun.webkit.network;

import java.nio.ByteBuffer;

abstract class URLLoaderBase {
   public static final int ALLOW_UNASSIGNED = 1;

   protected abstract void fwkCancel();

   protected static native void twkDidSendData(long var0, long var2, long var4);

   protected static native void twkWillSendRequest(int var0, String var1, String var2, long var3, String var5, String var6, long var7);

   protected static native void twkDidReceiveResponse(int var0, String var1, String var2, long var3, String var5, String var6, long var7);

   protected static native void twkDidReceiveData(ByteBuffer var0, int var1, int var2, long var3);

   protected static native void twkDidFinishLoading(long var0);

   protected static native void twkDidFail(int var0, String var1, String var2, long var3);
}
