package com.sun.webkit.network;

import java.nio.ByteBuffer;

interface ByteBufferAllocator {
   ByteBuffer allocate() throws InterruptedException;

   void release(ByteBuffer var1);
}
