package com.sun.webkit.graphics;

public interface WCPathIterator {
   int SEG_MOVETO = 0;
   int SEG_LINETO = 1;
   int SEG_QUADTO = 2;
   int SEG_CUBICTO = 3;
   int SEG_CLOSE = 4;

   int getWindingRule();

   boolean isDone();

   void next();

   int currentSegment(double[] var1);
}
