/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.muffin;

import java.io.IOException;
import java.util.HashMap;

public interface Muffin {
    public void saveFile(HashMap var1, String var2) throws IOException;

    public HashMap loadFile(String var1) throws IOException;
}

