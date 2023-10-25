/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.TransferMode
 */
package com.sun.javafx.embed;

import java.util.Set;
import javafx.scene.input.TransferMode;

public interface EmbeddedSceneDSInterface {
    public Set<TransferMode> getSupportedActions();

    public Object getData(String var1);

    public String[] getMimeTypes();

    public boolean isMimeTypeAvailable(String var1);

    public void dragDropEnd(TransferMode var1);
}

