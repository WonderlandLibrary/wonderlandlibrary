// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks;

import com.viaversion.viaversion.api.minecraft.chunks.NibbleArray;

public class ExtendedBlockStorage
{
    private int yBase;
    private byte[] blockLSBArray;
    private NibbleArray blockMSBArray;
    private NibbleArray blockMetadataArray;
    private NibbleArray blocklightArray;
    private NibbleArray skylightArray;
    
    public ExtendedBlockStorage(final int paramInt, final boolean paramBoolean) {
        this.yBase = paramInt;
        this.blockLSBArray = new byte[4096];
        this.blockMetadataArray = new NibbleArray(this.blockLSBArray.length);
        this.blocklightArray = new NibbleArray(this.blockLSBArray.length);
        if (paramBoolean) {
            this.skylightArray = new NibbleArray(this.blockLSBArray.length);
        }
    }
    
    public int getExtBlockMetadata(final int paramInt1, final int paramInt2, final int paramInt3) {
        return this.blockMetadataArray.get(paramInt1, paramInt2, paramInt3);
    }
    
    public void setExtBlockMetadata(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4) {
        this.blockMetadataArray.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public int getYLocation() {
        return this.yBase;
    }
    
    public void setExtSkylightValue(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4) {
        this.skylightArray.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public int getExtSkylightValue(final int paramInt1, final int paramInt2, final int paramInt3) {
        return this.skylightArray.get(paramInt1, paramInt2, paramInt3);
    }
    
    public void setExtBlocklightValue(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4) {
        this.blocklightArray.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public int getExtBlocklightValue(final int paramInt1, final int paramInt2, final int paramInt3) {
        return this.blocklightArray.get(paramInt1, paramInt2, paramInt3);
    }
    
    public byte[] getBlockLSBArray() {
        return this.blockLSBArray;
    }
    
    public boolean isEmpty() {
        return this.blockMSBArray == null;
    }
    
    public void clearMSBArray() {
        this.blockMSBArray = null;
    }
    
    public NibbleArray getBlockMSBArray() {
        return this.blockMSBArray;
    }
    
    public NibbleArray getMetadataArray() {
        return this.blockMetadataArray;
    }
    
    public NibbleArray getBlocklightArray() {
        return this.blocklightArray;
    }
    
    public NibbleArray getSkylightArray() {
        return this.skylightArray;
    }
    
    public void setBlockLSBArray(final byte[] paramArrayOfByte) {
        this.blockLSBArray = paramArrayOfByte;
    }
    
    public void setBlockMSBArray(final NibbleArray paramNibbleArray) {
        this.blockMSBArray = paramNibbleArray;
    }
    
    public void setBlockMetadataArray(final NibbleArray paramNibbleArray) {
        this.blockMetadataArray = paramNibbleArray;
    }
    
    public void setBlocklightArray(final NibbleArray paramNibbleArray) {
        this.blocklightArray = paramNibbleArray;
    }
    
    public void setSkylightArray(final NibbleArray paramNibbleArray) {
        this.skylightArray = paramNibbleArray;
    }
    
    public NibbleArray createBlockMSBArray() {
        return this.blockMSBArray = new NibbleArray(this.blockLSBArray.length);
    }
}
