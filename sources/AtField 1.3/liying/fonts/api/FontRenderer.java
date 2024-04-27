/*
 * Decompiled with CFR 0.152.
 */
package liying.fonts.api;

public interface FontRenderer {
    public int getHeight();

    public float charWidth(char var1);

    public int stringWidth(CharSequence var1);

    default public float drawString(CharSequence charSequence, float f, float f2, int n) {
        return this.drawString(charSequence, f, f2, n, false);
    }

    default public String trimStringToWidth(CharSequence charSequence, int n) {
        return this.trimStringToWidth(charSequence, n, false);
    }

    public float drawString(CharSequence var1, double var2, double var4, int var6, boolean var7);

    public boolean isAntiAlias();

    public float drawString(CharSequence var1, float var2, float var3, int var4, boolean var5);

    default public float getMiddleOfBox(float f) {
        return f / 2.0f - (float)this.getHeight() / 2.0f;
    }

    public String trimStringToWidth(CharSequence var1, int var2, boolean var3);

    default public float drawString(CharSequence charSequence, int n, int n2, int n3) {
        return this.drawString(charSequence, n, n2, n3, false);
    }

    default public float drawCenteredString(CharSequence charSequence, float f, float f2, int n) {
        return this.drawCenteredString(charSequence, f, f2, n, false);
    }

    public String getName();

    default public float drawCenteredString(CharSequence charSequence, float f, float f2, int n, boolean bl) {
        return this.drawString(charSequence, f - (float)this.stringWidth(charSequence) / 2.0f, f2, n, bl);
    }

    public boolean isFractionalMetrics();
}

