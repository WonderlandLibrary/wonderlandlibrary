/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.realms.RealmsBufferBuilder;
import net.minecraft.realms.RealmsVertexFormat;

public class Tezzelator {
    public static Tessellator t = Tessellator.getInstance();
    public static final Tezzelator instance = new Tezzelator();

    public void color(float f, float f2, float f3, float f4) {
        t.getWorldRenderer().color(f, f2, f3, f4);
    }

    public Tezzelator vertex(double d, double d2, double d3) {
        t.getWorldRenderer().pos(d, d2, d3);
        return this;
    }

    public void offset(double d, double d2, double d3) {
        t.getWorldRenderer().setTranslation(d, d2, d3);
    }

    public void normal(float f, float f2, float f3) {
        t.getWorldRenderer().normal(f, f2, f3);
    }

    public Tezzelator tex(double d, double d2) {
        t.getWorldRenderer().tex(d, d2);
        return this;
    }

    public void tex2(short s, short s2) {
        t.getWorldRenderer().lightmap(s, s2);
    }

    public void end() {
        t.draw();
    }

    public void begin(int n, RealmsVertexFormat realmsVertexFormat) {
        t.getWorldRenderer().begin(n, realmsVertexFormat.getVertexFormat());
    }

    public void endVertex() {
        t.getWorldRenderer().endVertex();
    }

    public RealmsBufferBuilder color(int n, int n2, int n3, int n4) {
        return new RealmsBufferBuilder(t.getWorldRenderer().color(n, n2, n3, n4));
    }
}

