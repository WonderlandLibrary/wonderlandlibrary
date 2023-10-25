/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;

public class FontMetadataSection
implements IMetadataSection {
    private final float[] charLefts;
    private final float[] charWidths;
    private final float[] charSpacings;

    public FontMetadataSection(float[] fArray, float[] fArray2, float[] fArray3) {
        this.charWidths = fArray;
        this.charLefts = fArray2;
        this.charSpacings = fArray3;
    }
}

