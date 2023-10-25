/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Util;

class PixelStoreState
extends Util {
    public int unpackRowLength;
    public int unpackAlignment;
    public int unpackSkipRows;
    public int unpackSkipPixels;
    public int packRowLength;
    public int packAlignment;
    public int packSkipRows;
    public int packSkipPixels;

    PixelStoreState() {
        this.load();
    }

    public void load() {
        this.unpackRowLength = PixelStoreState.glGetIntegerv(3314);
        this.unpackAlignment = PixelStoreState.glGetIntegerv(3317);
        this.unpackSkipRows = PixelStoreState.glGetIntegerv(3315);
        this.unpackSkipPixels = PixelStoreState.glGetIntegerv(3316);
        this.packRowLength = PixelStoreState.glGetIntegerv(3330);
        this.packAlignment = PixelStoreState.glGetIntegerv(3333);
        this.packSkipRows = PixelStoreState.glGetIntegerv(3331);
        this.packSkipPixels = PixelStoreState.glGetIntegerv(3332);
    }

    public void save() {
        GL11.glPixelStorei(3314, this.unpackRowLength);
        GL11.glPixelStorei(3317, this.unpackAlignment);
        GL11.glPixelStorei(3315, this.unpackSkipRows);
        GL11.glPixelStorei(3316, this.unpackSkipPixels);
        GL11.glPixelStorei(3330, this.packRowLength);
        GL11.glPixelStorei(3333, this.packAlignment);
        GL11.glPixelStorei(3331, this.packSkipRows);
        GL11.glPixelStorei(3332, this.packSkipPixels);
    }
}

