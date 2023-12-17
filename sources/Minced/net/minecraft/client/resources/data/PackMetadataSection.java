// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import net.minecraft.util.text.ITextComponent;

public class PackMetadataSection implements IMetadataSection
{
    private final ITextComponent packDescription;
    private final int packFormat;
    
    public PackMetadataSection(final ITextComponent packDescriptionIn, final int packFormatIn) {
        this.packDescription = packDescriptionIn;
        this.packFormat = packFormatIn;
    }
    
    public ITextComponent getPackDescription() {
        return this.packDescription;
    }
    
    public int getPackFormat() {
        return this.packFormat;
    }
}
