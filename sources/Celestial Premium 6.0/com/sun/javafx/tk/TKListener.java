/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.TKStage;
import java.util.List;

public interface TKListener {
    public void changedTopLevelWindows(List<TKStage> var1);

    public void exitedLastNestedLoop();
}

