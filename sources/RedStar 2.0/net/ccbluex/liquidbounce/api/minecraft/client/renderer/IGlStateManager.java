package net.ccbluex.liquidbounce.api.minecraft.client.renderer;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\u0000\n\b\n\b\bf\u000020J020H&J\b0H&J\b0H&J\b\b0H&J\b\t0H&J\b\n0H&J\b0H&J\b\f0H&J\b\r0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J(020202020H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IGlStateManager;", "", "bindTexture", "", "textureID", "", "disableBlend", "disableCull", "disableLighting", "disableRescaleNormal", "disableTexture2D", "enableAlpha", "enableBlend", "enableColorMaterial", "enableTexture2D", "popAttrib", "popMatrix", "pushAttrib", "pushMatrix", "resetColor", "tryBlendFuncSeparate", "glSrcAlpha", "glOneMinusSrcAlpha", "glOne", "glZero", "Pride"})
public interface IGlStateManager {
    public void bindTexture(int var1);

    public void resetColor();

    public void enableTexture2D();

    public void enableBlend();

    public void tryBlendFuncSeparate(int var1, int var2, int var3, int var4);

    public void disableTexture2D();

    public void disableBlend();

    public void enableAlpha();

    public void disableLighting();

    public void disableCull();

    public void enableColorMaterial();

    public void disableRescaleNormal();

    public void pushMatrix();

    public void pushAttrib();

    public void popMatrix();

    public void popAttrib();
}
