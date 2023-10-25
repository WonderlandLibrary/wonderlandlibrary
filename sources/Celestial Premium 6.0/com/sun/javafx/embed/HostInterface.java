/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.embed;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedStageInterface;

public interface HostInterface {
    public void setEmbeddedStage(EmbeddedStageInterface var1);

    public void setEmbeddedScene(EmbeddedSceneInterface var1);

    public boolean requestFocus();

    public boolean traverseFocusOut(boolean var1);

    public void repaint();

    public void setPreferredSize(int var1, int var2);

    public void setEnabled(boolean var1);

    public void setCursor(CursorFrame var1);

    public boolean grabFocus();

    public void ungrabFocus();
}

