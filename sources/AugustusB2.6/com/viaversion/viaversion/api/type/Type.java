// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type;

import com.viaversion.viaversion.api.type.types.minecraft.FlatVarIntItemArrayType;
import com.viaversion.viaversion.api.type.types.minecraft.FlatItemArrayType;
import com.viaversion.viaversion.api.type.types.minecraft.FlatVarIntItemType;
import com.viaversion.viaversion.api.type.types.minecraft.FlatItemType;
import com.viaversion.viaversion.api.type.types.minecraft.ItemArrayType;
import com.viaversion.viaversion.api.type.types.minecraft.ItemType;
import com.viaversion.viaversion.api.type.types.minecraft.VillagerDataType;
import com.viaversion.viaversion.api.type.types.minecraft.VarLongBlockChangeRecordType;
import com.viaversion.viaversion.api.type.types.minecraft.BlockChangeRecordType;
import com.viaversion.viaversion.api.type.types.minecraft.OptPosition1_14Type;
import com.viaversion.viaversion.api.type.types.minecraft.OptPositionType;
import com.viaversion.viaversion.api.type.types.minecraft.NBTType;
import com.viaversion.viaversion.api.type.types.minecraft.VectorType;
import com.viaversion.viaversion.api.type.types.minecraft.EulerAngleType;
import com.viaversion.viaversion.api.type.types.minecraft.Position1_14Type;
import com.viaversion.viaversion.api.type.types.minecraft.PositionType;
import com.viaversion.viaversion.api.type.types.VarIntArrayType;
import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
import com.viaversion.viaversion.api.type.types.minecraft.OptUUIDType;
import com.viaversion.viaversion.api.type.types.UUIDType;
import com.viaversion.viaversion.api.type.types.ArrayType;
import com.viaversion.viaversion.api.type.types.StringType;
import com.viaversion.viaversion.api.type.types.minecraft.OptionalComponentType;
import com.viaversion.viaversion.api.type.types.ComponentType;
import com.viaversion.viaversion.api.type.types.LongArrayType;
import com.viaversion.viaversion.api.type.types.RemainingBytesType;
import com.viaversion.viaversion.api.type.types.ShortByteArrayType;
import com.viaversion.viaversion.api.type.types.ByteArrayType;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.VillagerData;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.Vector;
import com.viaversion.viaversion.api.minecraft.EulerAngle;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.types.VoidType;
import com.viaversion.viaversion.api.type.types.VarLongType;
import com.viaversion.viaversion.api.type.types.minecraft.OptionalVarIntType;
import com.viaversion.viaversion.api.type.types.VarIntType;
import java.util.UUID;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.types.BooleanType;
import com.viaversion.viaversion.api.type.types.LongType;
import com.viaversion.viaversion.api.type.types.DoubleType;
import com.viaversion.viaversion.api.type.types.FloatType;
import com.viaversion.viaversion.api.type.types.IntType;
import com.viaversion.viaversion.api.type.types.UnsignedShortType;
import com.viaversion.viaversion.api.type.types.ShortType;
import com.viaversion.viaversion.api.type.types.UnsignedByteType;
import com.viaversion.viaversion.api.type.types.ByteType;

public abstract class Type<T> implements ByteBufReader<T>, ByteBufWriter<T>
{
    public static final ByteType BYTE;
    public static final UnsignedByteType UNSIGNED_BYTE;
    public static final Type<byte[]> BYTE_ARRAY_PRIMITIVE;
    public static final Type<byte[]> SHORT_BYTE_ARRAY;
    public static final Type<byte[]> REMAINING_BYTES;
    public static final ShortType SHORT;
    public static final UnsignedShortType UNSIGNED_SHORT;
    public static final IntType INT;
    public static final FloatType FLOAT;
    public static final DoubleType DOUBLE;
    public static final LongType LONG;
    public static final Type<long[]> LONG_ARRAY_PRIMITIVE;
    public static final BooleanType BOOLEAN;
    public static final Type<JsonElement> COMPONENT;
    public static final Type<JsonElement> OPTIONAL_COMPONENT;
    public static final Type<String> STRING;
    public static final Type<String[]> STRING_ARRAY;
    public static final Type<UUID> UUID;
    public static final Type<UUID> OPTIONAL_UUID;
    public static final Type<UUID> UUID_INT_ARRAY;
    public static final Type<UUID[]> UUID_ARRAY;
    public static final VarIntType VAR_INT;
    public static final OptionalVarIntType OPTIONAL_VAR_INT;
    public static final Type<int[]> VAR_INT_ARRAY_PRIMITIVE;
    public static final VarLongType VAR_LONG;
    @Deprecated
    public static final Type<Byte[]> BYTE_ARRAY;
    @Deprecated
    public static final Type<Short[]> UNSIGNED_BYTE_ARRAY;
    @Deprecated
    public static final Type<Boolean[]> BOOLEAN_ARRAY;
    @Deprecated
    public static final Type<Integer[]> INT_ARRAY;
    @Deprecated
    public static final Type<Short[]> SHORT_ARRAY;
    @Deprecated
    public static final Type<Integer[]> UNSIGNED_SHORT_ARRAY;
    @Deprecated
    public static final Type<Double[]> DOUBLE_ARRAY;
    @Deprecated
    public static final Type<Long[]> LONG_ARRAY;
    @Deprecated
    public static final Type<Float[]> FLOAT_ARRAY;
    @Deprecated
    public static final Type<Integer[]> VAR_INT_ARRAY;
    @Deprecated
    public static final Type<Long[]> VAR_LONG_ARRAY;
    public static final VoidType NOTHING;
    public static final Type<Position> POSITION;
    public static final Type<Position> POSITION1_14;
    public static final Type<EulerAngle> ROTATION;
    public static final Type<Vector> VECTOR;
    public static final Type<CompoundTag> NBT;
    public static final Type<CompoundTag[]> NBT_ARRAY;
    public static final Type<Position> OPTIONAL_POSITION;
    public static final Type<Position> OPTIONAL_POSITION_1_14;
    public static final Type<BlockChangeRecord> BLOCK_CHANGE_RECORD;
    public static final Type<BlockChangeRecord[]> BLOCK_CHANGE_RECORD_ARRAY;
    public static final Type<BlockChangeRecord> VAR_LONG_BLOCK_CHANGE_RECORD;
    public static final Type<BlockChangeRecord[]> VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY;
    public static final Type<VillagerData> VILLAGER_DATA;
    public static final Type<Item> ITEM;
    public static final Type<Item[]> ITEM_ARRAY;
    public static final Type<Item> FLAT_ITEM;
    public static final Type<Item> FLAT_VAR_INT_ITEM;
    public static final Type<Item[]> FLAT_ITEM_ARRAY;
    public static final Type<Item[]> FLAT_VAR_INT_ITEM_ARRAY;
    public static final Type<Item[]> FLAT_ITEM_ARRAY_VAR_INT;
    public static final Type<Item[]> FLAT_VAR_INT_ITEM_ARRAY_VAR_INT;
    private final Class<? super T> outputClass;
    private final String typeName;
    
    protected Type(final Class<? super T> outputClass) {
        this(outputClass.getSimpleName(), outputClass);
    }
    
    protected Type(final String typeName, final Class<? super T> outputClass) {
        this.outputClass = outputClass;
        this.typeName = typeName;
    }
    
    public Class<? super T> getOutputClass() {
        return this.outputClass;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public Class<? extends Type> getBaseClass() {
        return this.getClass();
    }
    
    @Override
    public String toString() {
        return "Type|" + this.typeName;
    }
    
    static {
        BYTE = new ByteType();
        UNSIGNED_BYTE = new UnsignedByteType();
        BYTE_ARRAY_PRIMITIVE = new ByteArrayType();
        SHORT_BYTE_ARRAY = new ShortByteArrayType();
        REMAINING_BYTES = new RemainingBytesType();
        SHORT = new ShortType();
        UNSIGNED_SHORT = new UnsignedShortType();
        INT = new IntType();
        FLOAT = new FloatType();
        DOUBLE = new DoubleType();
        LONG = new LongType();
        LONG_ARRAY_PRIMITIVE = new LongArrayType();
        BOOLEAN = new BooleanType();
        COMPONENT = new ComponentType();
        OPTIONAL_COMPONENT = new OptionalComponentType();
        STRING = new StringType();
        STRING_ARRAY = new ArrayType((Type<Object>)Type.STRING);
        UUID = new UUIDType();
        OPTIONAL_UUID = new OptUUIDType();
        UUID_INT_ARRAY = new UUIDIntArrayType();
        UUID_ARRAY = new ArrayType((Type<Object>)Type.UUID);
        VAR_INT = new VarIntType();
        OPTIONAL_VAR_INT = new OptionalVarIntType();
        VAR_INT_ARRAY_PRIMITIVE = new VarIntArrayType();
        VAR_LONG = new VarLongType();
        BYTE_ARRAY = new ArrayType((Type<Object>)Type.BYTE);
        UNSIGNED_BYTE_ARRAY = new ArrayType((Type<Object>)Type.UNSIGNED_BYTE);
        BOOLEAN_ARRAY = new ArrayType((Type<Object>)Type.BOOLEAN);
        INT_ARRAY = new ArrayType((Type<Object>)Type.INT);
        SHORT_ARRAY = new ArrayType((Type<Object>)Type.SHORT);
        UNSIGNED_SHORT_ARRAY = new ArrayType((Type<Object>)Type.UNSIGNED_SHORT);
        DOUBLE_ARRAY = new ArrayType((Type<Object>)Type.DOUBLE);
        LONG_ARRAY = new ArrayType((Type<Object>)Type.LONG);
        FLOAT_ARRAY = new ArrayType((Type<Object>)Type.FLOAT);
        VAR_INT_ARRAY = new ArrayType((Type<Object>)Type.VAR_INT);
        VAR_LONG_ARRAY = new ArrayType((Type<Object>)Type.VAR_LONG);
        NOTHING = new VoidType();
        POSITION = new PositionType();
        POSITION1_14 = new Position1_14Type();
        ROTATION = new EulerAngleType();
        VECTOR = new VectorType();
        NBT = new NBTType();
        NBT_ARRAY = new ArrayType((Type<Object>)Type.NBT);
        OPTIONAL_POSITION = new OptPositionType();
        OPTIONAL_POSITION_1_14 = new OptPosition1_14Type();
        BLOCK_CHANGE_RECORD = new BlockChangeRecordType();
        BLOCK_CHANGE_RECORD_ARRAY = new ArrayType((Type<Object>)Type.BLOCK_CHANGE_RECORD);
        VAR_LONG_BLOCK_CHANGE_RECORD = new VarLongBlockChangeRecordType();
        VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY = new ArrayType((Type<Object>)Type.VAR_LONG_BLOCK_CHANGE_RECORD);
        VILLAGER_DATA = new VillagerDataType();
        ITEM = new ItemType();
        ITEM_ARRAY = new ItemArrayType();
        FLAT_ITEM = new FlatItemType();
        FLAT_VAR_INT_ITEM = new FlatVarIntItemType();
        FLAT_ITEM_ARRAY = new FlatItemArrayType();
        FLAT_VAR_INT_ITEM_ARRAY = new FlatVarIntItemArrayType();
        FLAT_ITEM_ARRAY_VAR_INT = new ArrayType((Type<Object>)Type.FLAT_ITEM);
        FLAT_VAR_INT_ITEM_ARRAY_VAR_INT = new ArrayType((Type<Object>)Type.FLAT_VAR_INT_ITEM);
    }
}
