// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;

public enum MetaType1_7_6_10 implements MetaType
{
    Byte(0, (Type)Type.BYTE), 
    Short(1, (Type)Type.SHORT), 
    Int(2, (Type)Type.INT), 
    Float(3, (Type)Type.FLOAT), 
    String(4, (Type)Type.STRING), 
    Slot(5, (Type)Types1_7_6_10.COMPRESSED_NBT_ITEM), 
    Position(6, (Type)Type.VECTOR), 
    NonExistent(-1, (Type)Type.NOTHING);
    
    private final int typeID;
    private final Type type;
    
    public static MetaType1_7_6_10 byId(final int id) {
        return values()[id];
    }
    
    private MetaType1_7_6_10(final int typeID, final Type type) {
        this.typeID = typeID;
        this.type = type;
    }
    
    @Override
    public int typeId() {
        return this.typeID;
    }
    
    @Override
    public Type type() {
        return this.type;
    }
}
