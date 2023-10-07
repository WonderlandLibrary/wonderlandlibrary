/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.imageout;

import java.io.IOException;
import java.io.OutputStream;
import me.kiras.aimwhere.libraries.slick.Image;

public interface ImageWriter {
    public void saveImage(Image var1, String var2, OutputStream var3, boolean var4) throws IOException;
}

