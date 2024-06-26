/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.miku.tessellate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.ccbluex.liquidbounce.utils.render.miku.tessellate.BasicTess;
import net.ccbluex.liquidbounce.utils.render.miku.tessellate.Tessellation;

public class ExpandingTess
extends BasicTess {
    private final float ratio;
    private final float factor;

    ExpandingTess(int initial, float ratio, float factor) {
        super(initial);
        this.ratio = ratio;
        this.factor = factor;
    }

    @Override
    public Tessellation addVertex(float x, float y, float z) {
        int capacity = this.raw.length;
        if ((float)(this.index * 6) >= (float)capacity * this.ratio) {
            capacity = (int)((float)capacity * this.factor);
            int[] newBuffer = new int[capacity];
            System.arraycopy(this.raw, 0, newBuffer, 0, this.raw.length);
            this.raw = newBuffer;
            this.buffer = ByteBuffer.allocateDirect(capacity * 4).order(ByteOrder.nativeOrder());
            this.iBuffer = this.buffer.asIntBuffer();
            this.fBuffer = this.buffer.asFloatBuffer();
        }
        return super.addVertex(x, y, z);
    }
}

