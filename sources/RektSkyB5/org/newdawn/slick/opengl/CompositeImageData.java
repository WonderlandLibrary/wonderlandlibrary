/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.newdawn.slick.opengl.CompositeIOException;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.util.Log;

public class CompositeImageData
implements LoadableImageData {
    private ArrayList sources = new ArrayList();
    private LoadableImageData picked;

    public void add(LoadableImageData data) {
        this.sources.add(data);
    }

    public ByteBuffer loadImage(InputStream fis) throws IOException {
        return this.loadImage(fis, false, null);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }

    public ByteBuffer loadImage(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
        CompositeIOException exception = new CompositeIOException();
        ByteBuffer buffer = null;
        BufferedInputStream in = new BufferedInputStream(is, is.available());
        in.mark(is.available());
        for (int i2 = 0; i2 < this.sources.size(); ++i2) {
            in.reset();
            try {
                LoadableImageData data = (LoadableImageData)this.sources.get(i2);
                buffer = data.loadImage(in, flipped, forceAlpha, transparent);
                this.picked = data;
                break;
            }
            catch (Exception e2) {
                Log.warn(this.sources.get(i2).getClass() + " failed to read the data", e2);
                exception.addException(e2);
                continue;
            }
        }
        if (this.picked == null) {
            throw exception;
        }
        return buffer;
    }

    private void checkPicked() {
        if (this.picked == null) {
            throw new RuntimeException("Attempt to make use of uninitialised or invalid composite image data");
        }
    }

    public int getDepth() {
        this.checkPicked();
        return this.picked.getDepth();
    }

    public int getHeight() {
        this.checkPicked();
        return this.picked.getHeight();
    }

    public ByteBuffer getImageBufferData() {
        this.checkPicked();
        return this.picked.getImageBufferData();
    }

    public int getTexHeight() {
        this.checkPicked();
        return this.picked.getTexHeight();
    }

    public int getTexWidth() {
        this.checkPicked();
        return this.picked.getTexWidth();
    }

    public int getWidth() {
        this.checkPicked();
        return this.picked.getWidth();
    }

    public void configureEdging(boolean edging) {
        for (int i2 = 0; i2 < this.sources.size(); ++i2) {
            ((LoadableImageData)this.sources.get(i2)).configureEdging(edging);
        }
    }
}

