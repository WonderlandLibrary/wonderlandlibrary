/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.font;

public interface FontRenderer {
    public float drawString(CharSequence var1, float var2, float var3, int var4, boolean var5);

    public float drawString(CharSequence var1, double var2, double var4, int var6, boolean var7);

    public String trimStringToWidth(CharSequence var1, int var2, boolean var3);

    public int stringWidth(CharSequence var1);

    public float charWidth(char var1);

    public String getName();

    public int getHeight();

    public boolean isAntiAlias();

    public boolean isFractionalMetrics();

    default public float drawString(CharSequence text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    default public float drawString(CharSequence text, int x, int y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    default public String trimStringToWidth(CharSequence text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    default public float drawCenteredString(CharSequence text, float x, float y, int color, boolean dropShadow) {
        return this.drawString(text, x - (float)this.stringWidth(text) / 2.0f, y, color, dropShadow);
    }

    default public float getMiddleOfBox(float boxHeight) {
        return boxHeight / 2.0f - (float)this.getHeight() / 2.0f;
    }

    default public float drawCenteredString(CharSequence text, float x, float y, int color) {
        return this.drawCenteredString(text, x, y, color, false);
    }
}

