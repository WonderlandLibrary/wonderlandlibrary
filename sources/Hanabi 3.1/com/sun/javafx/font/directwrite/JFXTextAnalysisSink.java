package com.sun.javafx.font.directwrite;

class JFXTextAnalysisSink extends IUnknown {
   JFXTextAnalysisSink(long var1) {
      super(var1);
   }

   boolean Next() {
      return OS.Next(this.ptr);
   }

   int GetStart() {
      return OS.GetStart(this.ptr);
   }

   int GetLength() {
      return OS.GetLength(this.ptr);
   }

   DWRITE_SCRIPT_ANALYSIS GetAnalysis() {
      return OS.GetAnalysis(this.ptr);
   }
}
