/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.Renderer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class FloatMap {
    private final int width;
    private final int height;
    private final FloatBuffer buf;
    private boolean cacheValid;
    private Map<FilterContext, Entry> cache;

    public FloatMap(int n, int n2) {
        if (n <= 0 || n > 4096 || n2 <= 0 || n2 > 4096) {
            throw new IllegalArgumentException("Width and height must be in the range [1, 4096]");
        }
        this.width = n;
        this.height = n2;
        int n3 = n * n2 * 4;
        this.buf = FloatBuffer.wrap(new float[n3]);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float[] getData() {
        return this.buf.array();
    }

    public FloatBuffer getBuffer() {
        return this.buf;
    }

    public float getSample(int n, int n2, int n3) {
        return this.buf.get((n + n2 * this.width) * 4 + n3);
    }

    public void setSample(int n, int n2, int n3, float f) {
        this.buf.put((n + n2 * this.width) * 4 + n3, f);
        this.cacheValid = false;
    }

    public void setSamples(int n, int n2, float f) {
        int n3 = (n + n2 * this.width) * 4;
        this.buf.put(n3 + 0, f);
        this.cacheValid = false;
    }

    public void setSamples(int n, int n2, float f, float f2) {
        int n3 = (n + n2 * this.width) * 4;
        this.buf.put(n3 + 0, f);
        this.buf.put(n3 + 1, f2);
        this.cacheValid = false;
    }

    public void setSamples(int n, int n2, float f, float f2, float f3) {
        int n3 = (n + n2 * this.width) * 4;
        this.buf.put(n3 + 0, f);
        this.buf.put(n3 + 1, f2);
        this.buf.put(n3 + 2, f3);
        this.cacheValid = false;
    }

    public void setSamples(int n, int n2, float f, float f2, float f3, float f4) {
        int n3 = (n + n2 * this.width) * 4;
        this.buf.put(n3 + 0, f);
        this.buf.put(n3 + 1, f2);
        this.buf.put(n3 + 2, f3);
        this.buf.put(n3 + 3, f4);
        this.cacheValid = false;
    }

    public void put(float[] arrf) {
        this.buf.rewind();
        this.buf.put(arrf);
        this.buf.rewind();
        this.cacheValid = false;
    }

    public LockableResource getAccelData(FilterContext filterContext) {
        Entry entry2;
        if (this.cache == null) {
            this.cache = new HashMap<FilterContext, Entry>();
        } else if (!this.cacheValid) {
            for (Entry entry2 : this.cache.values()) {
                entry2.valid = false;
            }
            this.cacheValid = true;
        }
        Renderer renderer = Renderer.getRenderer(filterContext);
        entry2 = this.cache.get(filterContext);
        if (entry2 != null) {
            entry2.texture.lock();
            if (entry2.texture.isLost()) {
                entry2.texture.unlock();
                this.cache.remove(filterContext);
                entry2 = null;
            }
        }
        if (entry2 == null) {
            LockableResource lockableResource = renderer.createFloatTexture(this.width, this.height);
            entry2 = new Entry(lockableResource);
            this.cache.put(filterContext, entry2);
        }
        if (!entry2.valid) {
            renderer.updateFloatTexture(entry2.texture, this);
            entry2.valid = true;
        }
        return entry2.texture;
    }

    private static class Entry {
        LockableResource texture;
        boolean valid;

        Entry(LockableResource lockableResource) {
            this.texture = lockableResource;
        }
    }
}

