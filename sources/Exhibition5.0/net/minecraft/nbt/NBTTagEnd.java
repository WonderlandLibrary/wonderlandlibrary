// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.DataInput;

public class NBTTagEnd extends NBTBase
{
    private static final String __OBFID = "CL_00001219";
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
    }
    
    @Override
    public byte getId() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "END";
    }
    
    @Override
    public NBTBase copy() {
        return new NBTTagEnd();
    }
}
