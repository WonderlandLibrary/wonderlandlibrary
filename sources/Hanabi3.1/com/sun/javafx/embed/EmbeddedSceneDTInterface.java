package com.sun.javafx.embed;

import javafx.scene.input.TransferMode;

public interface EmbeddedSceneDTInterface {
   TransferMode handleDragEnter(int var1, int var2, int var3, int var4, TransferMode var5, EmbeddedSceneDSInterface var6);

   void handleDragLeave();

   TransferMode handleDragDrop(int var1, int var2, int var3, int var4, TransferMode var5);

   TransferMode handleDragOver(int var1, int var2, int var3, int var4, TransferMode var5);
}
