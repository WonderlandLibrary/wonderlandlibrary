// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.image.BufferedImage;

public final class FontGlyph
{
    private BufferedImage image;
    private int page;
    private int x;
    private int y;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private int xAdvance;
    private final Map<Character, Integer> kernings;
    
    public final BufferedImage getImage() {
        return this.image;
    }
    
    public final void setImage(final BufferedImage <set-?>) {
        this.image = <set-?>;
    }
    
    public final int getPage() {
        return this.page;
    }
    
    public final void setPage(final int <set-?>) {
        this.page = <set-?>;
    }
    
    public final int getX() {
        return this.x;
    }
    
    public final void setX(final int <set-?>) {
        this.x = <set-?>;
    }
    
    public final int getY() {
        return this.y;
    }
    
    public final void setY(final int <set-?>) {
        this.y = <set-?>;
    }
    
    public final int getWidth() {
        return this.width;
    }
    
    public final void setWidth(final int <set-?>) {
        this.width = <set-?>;
    }
    
    public final int getHeight() {
        return this.height;
    }
    
    public final void setHeight(final int <set-?>) {
        this.height = <set-?>;
    }
    
    public final int getXOffset() {
        return this.xOffset;
    }
    
    public final void setXOffset(final int <set-?>) {
        this.xOffset = <set-?>;
    }
    
    public final int getYOffset() {
        return this.yOffset;
    }
    
    public final void setYOffset(final int <set-?>) {
        this.yOffset = <set-?>;
    }
    
    public final int getXAdvance() {
        return this.xAdvance;
    }
    
    public final void setXAdvance(final int <set-?>) {
        this.xAdvance = <set-?>;
    }
    
    public final Map<Character, Integer> getKernings() {
        return this.kernings;
    }
    
    public FontGlyph() {
        this.kernings = new LinkedHashMap<Character, Integer>();
    }
    
    static {
        new Companion((byte)0);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
