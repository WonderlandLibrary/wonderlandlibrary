// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

public class FontMetadataSection implements IMetadataSection
{
    private final float[] charWidths;
    private final float[] charLefts;
    private final float[] charSpacings;
    
    public FontMetadataSection(final float[] charWidthsIn, final float[] charLeftsIn, final float[] charSpacingsIn) {
        this.charWidths = charWidthsIn;
        this.charLefts = charLeftsIn;
        this.charSpacings = charSpacingsIn;
    }
}
