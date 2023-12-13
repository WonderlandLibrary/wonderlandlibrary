package com.google.zxing;

import java.util.Map;

public interface Reader {
   Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException;

   Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, ChecksumException, FormatException;

   void reset();
}
