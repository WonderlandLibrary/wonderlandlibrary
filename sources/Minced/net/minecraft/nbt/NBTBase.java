// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public abstract class NBTBase
{
    public static final String[] NBT_TYPES;
    
    abstract void write(final DataOutput p0) throws IOException;
    
    abstract void read(final DataInput p0, final int p1, final NBTSizeTracker p2) throws IOException;
    
    @Override
    public abstract String toString();
    
    public abstract byte getId();
    
    protected static NBTBase create(final byte id) {
        switch (id) {
            case 0: {
                return new NBTTagEnd();
            }
            case 1: {
                return new NBTTagByte();
            }
            case 2: {
                return new NBTTagShort();
            }
            case 3: {
                return new NBTTagInt();
            }
            case 4: {
                return new NBTTagLong();
            }
            case 5: {
                return new NBTTagFloat();
            }
            case 6: {
                return new NBTTagDouble();
            }
            case 7: {
                return new NBTTagByteArray();
            }
            case 8: {
                return new NBTTagString();
            }
            case 9: {
                return new NBTTagList();
            }
            case 10: {
                return new NBTTagCompound();
            }
            case 11: {
                return new NBTTagIntArray();
            }
            case 12: {
                return new NBTTagLongArray();
            }
            default: {
                return null;
            }
        }
    }
    
    public static String getTypeName(final int id) {
        switch (id) {
            case 0: {
                return "TAG_End";
            }
            case 1: {
                return "TAG_Byte";
            }
            case 2: {
                return "TAG_Short";
            }
            case 3: {
                return "TAG_Int";
            }
            case 4: {
                return "TAG_Long";
            }
            case 5: {
                return "TAG_Float";
            }
            case 6: {
                return "TAG_Double";
            }
            case 7: {
                return "TAG_Byte_Array";
            }
            case 8: {
                return "TAG_String";
            }
            case 9: {
                return "TAG_List";
            }
            case 10: {
                return "TAG_Compound";
            }
            case 11: {
                return "TAG_Int_Array";
            }
            case 12: {
                return "TAG_Long_Array";
            }
            case 99: {
                return "Any Numeric Tag";
            }
            default: {
                return "UNKNOWN";
            }
        }
    }
    
    public abstract NBTBase copy();
    
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof NBTBase && this.getId() == ((NBTBase)p_equals_1_).getId();
    }
    
    @Override
    public int hashCode() {
        return this.getId();
    }
    
    protected String getString() {
        return this.toString();
    }
    
    static {
        NBT_TYPES = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]", "LONG[]" };
    }
}
