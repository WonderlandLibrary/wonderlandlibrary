package com.sun.javafx.tk;

import javafx.scene.input.TransferMode;

public interface TKDropTargetListener {
   TransferMode dragEnter(double var1, double var3, double var5, double var7, TransferMode var9, TKClipboard var10);

   TransferMode dragOver(double var1, double var3, double var5, double var7, TransferMode var9);

   void dragExit(double var1, double var3, double var5, double var7);

   TransferMode drop(double var1, double var3, double var5, double var7, TransferMode var9);
}
