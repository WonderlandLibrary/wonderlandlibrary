/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DResource;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.ps.Shader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

final class D3DShader
extends D3DResource
implements Shader {
    private static IntBuffer itmp;
    private static FloatBuffer ftmp;
    private final Map<String, Integer> registers;
    private boolean valid;

    D3DShader(D3DContext d3DContext, long l, Map<String, Integer> map) {
        super(new D3DResource.D3DRecord(d3DContext, l));
        this.valid = l != 0L;
        this.registers = map;
    }

    static native long init(long var0, ByteBuffer var2, int var3, boolean var4, boolean var5);

    private static native int enable(long var0, long var2);

    private static native int disable(long var0, long var2);

    private static native int setConstantsF(long var0, long var2, int var4, FloatBuffer var5, int var6, int var7);

    private static native int setConstantsI(long var0, long var2, int var4, IntBuffer var5, int var6, int var7);

    private static native int nGetRegister(long var0, long var2, String var4);

    @Override
    public void enable() {
        int n = D3DShader.enable(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource());
        this.valid &= n >= 0;
        D3DContext.validate(n);
    }

    @Override
    public void disable() {
        int n = D3DShader.disable(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource());
        this.valid &= n >= 0;
        D3DContext.validate(n);
    }

    private static void checkTmpIntBuf() {
        if (itmp == null) {
            itmp = BufferUtil.newIntBuffer(4);
        }
        itmp.clear();
    }

    @Override
    public void setConstant(String string, int n) {
        this.setConstant(string, (float)n);
    }

    @Override
    public void setConstant(String string, int n, int n2) {
        this.setConstant(string, (float)n, (float)n2);
    }

    @Override
    public void setConstant(String string, int n, int n2, int n3) {
        this.setConstant(string, (float)n, (float)n2, (float)n3);
    }

    @Override
    public void setConstant(String string, int n, int n2, int n3, int n4) {
        this.setConstant(string, (float)n, (float)n2, (float)n3, (float)n4);
    }

    @Override
    public void setConstants(String string, IntBuffer intBuffer, int n, int n2) {
        throw new InternalError("Not yet implemented");
    }

    private static void checkTmpFloatBuf() {
        if (ftmp == null) {
            ftmp = BufferUtil.newFloatBuffer(4);
        }
        ftmp.clear();
    }

    @Override
    public void setConstant(String string, float f) {
        D3DShader.checkTmpFloatBuf();
        ftmp.put(f);
        this.setConstants(string, ftmp, 0, 1);
    }

    @Override
    public void setConstant(String string, float f, float f2) {
        D3DShader.checkTmpFloatBuf();
        ftmp.put(f);
        ftmp.put(f2);
        this.setConstants(string, ftmp, 0, 1);
    }

    @Override
    public void setConstant(String string, float f, float f2, float f3) {
        D3DShader.checkTmpFloatBuf();
        ftmp.put(f);
        ftmp.put(f2);
        ftmp.put(f3);
        this.setConstants(string, ftmp, 0, 1);
    }

    @Override
    public void setConstant(String string, float f, float f2, float f3, float f4) {
        D3DShader.checkTmpFloatBuf();
        ftmp.put(f);
        ftmp.put(f2);
        ftmp.put(f3);
        ftmp.put(f4);
        this.setConstants(string, ftmp, 0, 1);
    }

    @Override
    public void setConstants(String string, FloatBuffer floatBuffer, int n, int n2) {
        int n3 = D3DShader.setConstantsF(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource(), this.getRegister(string), floatBuffer, n, n2);
        this.valid &= n3 >= 0;
        D3DContext.validate(n3);
    }

    private int getRegister(String string) {
        Integer n = this.registers.get(string);
        if (n == null) {
            int n2 = D3DShader.nGetRegister(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource(), string);
            if (n2 < 0) {
                throw new IllegalArgumentException("Register not found for: " + string);
            }
            this.registers.put(string, n2);
            return n2;
        }
        return n;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.valid = false;
    }
}

