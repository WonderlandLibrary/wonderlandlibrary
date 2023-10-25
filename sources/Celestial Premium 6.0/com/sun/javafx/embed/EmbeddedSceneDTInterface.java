/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.embed;

import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import javafx.scene.input.TransferMode;

public interface EmbeddedSceneDTInterface {
    public TransferMode handleDragEnter(int var1, int var2, int var3, int var4, TransferMode var5, EmbeddedSceneDSInterface var6);

    public void handleDragLeave();

    public TransferMode handleDragDrop(int var1, int var2, int var3, int var4, TransferMode var5);

    public TransferMode handleDragOver(int var1, int var2, int var3, int var4, TransferMode var5);
}

