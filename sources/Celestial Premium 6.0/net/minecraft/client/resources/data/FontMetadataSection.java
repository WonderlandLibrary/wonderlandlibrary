/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;

public class FontMetadataSection
implements IMetadataSection {
    private final float[] charWidths;
    private final float[] charLefts;
    private final float[] charSpacings;

    public FontMetadataSection(float[] charWidthsIn, float[] charLeftsIn, float[] charSpacingsIn) {
        this.charWidths = charWidthsIn;
        this.charLefts = charLeftsIn;
        this.charSpacings = charSpacingsIn;
    }
}

