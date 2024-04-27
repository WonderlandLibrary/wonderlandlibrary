/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render.miku.tessellate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.ccbluex.liquidbounce.utils.render.miku.tessellate.Tessellation;
import org.lwjgl.opengl.GL11;

public class BasicTess
implements Tessellation {
    int index;
    int[] raw = new int[capacity *= 6];
    ByteBuffer buffer;
    FloatBuffer fBuffer;
    IntBuffer iBuffer;
    private int colors;
    private float texU;
    private float texV;
    private boolean color;
    private boolean texture;

    BasicTess(int capacity) {
        this.buffer = ByteBuffer.allocateDirect(capacity * 4).order(ByteOrder.nativeOrder());
        this.fBuffer = this.buffer.asFloatBuffer();
        this.iBuffer = this.buffer.asIntBuffer();
    }

    @Override
    public Tessellation setColor(int color) {
        this.color = true;
        this.colors = color;
        return this;
    }

    @Override
    public Tessellation setTexture(float u, float v) {
        this.texture = true;
        this.texU = u;
        this.texV = v;
        return this;
    }

    @Override
    public Tessellation addVertex(float x, float y, float z) {
        int dex = this.index * 6;
        this.raw[dex] = Float.floatToRawIntBits(x);
        this.raw[dex + 1] = Float.floatToRawIntBits(y);
        this.raw[dex + 2] = Float.floatToRawIntBits(z);
        this.raw[dex + 3] = this.colors;
        this.raw[dex + 4] = Float.floatToRawIntBits(this.texU);
        this.raw[dex + 5] = Float.floatToRawIntBits(this.texV);
        ++this.index;
        return this;
    }

    /*
     * Exception decompiling
     */
    @Override
    public Tessellation bind() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl37 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public Tessellation pass(int mode) {
        GL11.glDrawArrays((int)mode, (int)0, (int)this.index);
        return this;
    }

    @Override
    public Tessellation unbind() {
        this.iBuffer.position(0);
        return this;
    }

    @Override
    public Tessellation reset() {
        this.iBuffer.clear();
        this.index = 0;
        this.color = false;
        this.texture = false;
        return this;
    }
}

