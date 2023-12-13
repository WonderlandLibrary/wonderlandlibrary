// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font.test;

import org.lwjgl.util.vector.Vector2f;

public class CharInfo
{
    public int sourceX;
    public int sourceY;
    public int width;
    public int height;
    public Vector2f[] textureCoordinates;
    
    public CharInfo(final int sourceX, final int sourceY, final int width, final int height) {
        this.textureCoordinates = new Vector2f[4];
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.width = width;
        this.height = height;
    }
    
    public void calculateTextureCoordinates(final int fontWith, final int fontHeight) {
        final float x0 = this.sourceX / (float)fontWith;
        final float x2 = (this.sourceX + this.width) / (float)fontWith;
        final float y0 = this.sourceY / (float)fontHeight;
        final float y2 = (this.sourceY + this.height) / (float)fontHeight;
        this.textureCoordinates[0] = new Vector2f(x0, y0);
        this.textureCoordinates[1] = new Vector2f(x0, y2);
        this.textureCoordinates[2] = new Vector2f(x2, y0);
        this.textureCoordinates[3] = new Vector2f(x2, y2);
    }
}
