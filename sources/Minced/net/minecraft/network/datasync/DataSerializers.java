// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.datasync;

import net.minecraft.block.Block;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import java.util.UUID;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.IntIdentityHashBiMap;

public class DataSerializers
{
    private static final IntIdentityHashBiMap<DataSerializer<?>> REGISTRY;
    public static final DataSerializer<Byte> BYTE;
    public static final DataSerializer<Integer> VARINT;
    public static final DataSerializer<Float> FLOAT;
    public static final DataSerializer<String> STRING;
    public static final DataSerializer<ITextComponent> TEXT_COMPONENT;
    public static final DataSerializer<ItemStack> ITEM_STACK;
    public static final DataSerializer<Optional<IBlockState>> OPTIONAL_BLOCK_STATE;
    public static final DataSerializer<Boolean> BOOLEAN;
    public static final DataSerializer<Rotations> ROTATIONS;
    public static final DataSerializer<BlockPos> BLOCK_POS;
    public static final DataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS;
    public static final DataSerializer<EnumFacing> FACING;
    public static final DataSerializer<Optional<UUID>> OPTIONAL_UNIQUE_ID;
    public static final DataSerializer<NBTTagCompound> COMPOUND_TAG;
    
    public static void registerSerializer(final DataSerializer<?> serializer) {
        DataSerializers.REGISTRY.add(serializer);
    }
    
    @Nullable
    public static DataSerializer<?> getSerializer(final int id) {
        return DataSerializers.REGISTRY.get(id);
    }
    
    public static int getSerializerId(final DataSerializer<?> serializer) {
        return DataSerializers.REGISTRY.getId(serializer);
    }
    
    static {
        REGISTRY = new IntIdentityHashBiMap<DataSerializer<?>>(16);
        BYTE = new DataSerializer<Byte>() {
            @Override
            public void write(final PacketBuffer buf, final Byte value) {
                buf.writeByte(value);
            }
            
            @Override
            public Byte read(final PacketBuffer buf) throws IOException {
                return buf.readByte();
            }
            
            @Override
            public DataParameter<Byte> createKey(final int id) {
                return new DataParameter<Byte>(id, this);
            }
            
            @Override
            public Byte copyValue(final Byte value) {
                return value;
            }
        };
        VARINT = new DataSerializer<Integer>() {
            @Override
            public void write(final PacketBuffer buf, final Integer value) {
                buf.writeVarInt(value);
            }
            
            @Override
            public Integer read(final PacketBuffer buf) throws IOException {
                return buf.readVarInt();
            }
            
            @Override
            public DataParameter<Integer> createKey(final int id) {
                return new DataParameter<Integer>(id, this);
            }
            
            @Override
            public Integer copyValue(final Integer value) {
                return value;
            }
        };
        FLOAT = new DataSerializer<Float>() {
            @Override
            public void write(final PacketBuffer buf, final Float value) {
                buf.writeFloat(value);
            }
            
            @Override
            public Float read(final PacketBuffer buf) throws IOException {
                return buf.readFloat();
            }
            
            @Override
            public DataParameter<Float> createKey(final int id) {
                return new DataParameter<Float>(id, this);
            }
            
            @Override
            public Float copyValue(final Float value) {
                return value;
            }
        };
        STRING = new DataSerializer<String>() {
            @Override
            public void write(final PacketBuffer buf, final String value) {
                buf.writeString(value);
            }
            
            @Override
            public String read(final PacketBuffer buf) throws IOException {
                return buf.readString(32767);
            }
            
            @Override
            public DataParameter<String> createKey(final int id) {
                return new DataParameter<String>(id, this);
            }
            
            @Override
            public String copyValue(final String value) {
                return value;
            }
        };
        TEXT_COMPONENT = new DataSerializer<ITextComponent>() {
            @Override
            public void write(final PacketBuffer buf, final ITextComponent value) {
                buf.writeTextComponent(value);
            }
            
            @Override
            public ITextComponent read(final PacketBuffer buf) throws IOException {
                return buf.readTextComponent();
            }
            
            @Override
            public DataParameter<ITextComponent> createKey(final int id) {
                return new DataParameter<ITextComponent>(id, this);
            }
            
            @Override
            public ITextComponent copyValue(final ITextComponent value) {
                return value.createCopy();
            }
        };
        ITEM_STACK = new DataSerializer<ItemStack>() {
            @Override
            public void write(final PacketBuffer buf, final ItemStack value) {
                buf.writeItemStack(value);
            }
            
            @Override
            public ItemStack read(final PacketBuffer buf) throws IOException {
                return buf.readItemStack();
            }
            
            @Override
            public DataParameter<ItemStack> createKey(final int id) {
                return new DataParameter<ItemStack>(id, this);
            }
            
            @Override
            public ItemStack copyValue(final ItemStack value) {
                return value.copy();
            }
        };
        OPTIONAL_BLOCK_STATE = new DataSerializer<Optional<IBlockState>>() {
            @Override
            public void write(final PacketBuffer buf, final Optional<IBlockState> value) {
                if (value.isPresent()) {
                    buf.writeVarInt(Block.getStateId((IBlockState)value.get()));
                }
                else {
                    buf.writeVarInt(0);
                }
            }
            
            @Override
            public Optional<IBlockState> read(final PacketBuffer buf) throws IOException {
                final int i = buf.readVarInt();
                return (Optional<IBlockState>)((i == 0) ? Optional.absent() : Optional.of((Object)Block.getStateById(i)));
            }
            
            @Override
            public DataParameter<Optional<IBlockState>> createKey(final int id) {
                return new DataParameter<Optional<IBlockState>>(id, this);
            }
            
            @Override
            public Optional<IBlockState> copyValue(final Optional<IBlockState> value) {
                return value;
            }
        };
        BOOLEAN = new DataSerializer<Boolean>() {
            @Override
            public void write(final PacketBuffer buf, final Boolean value) {
                buf.writeBoolean(value);
            }
            
            @Override
            public Boolean read(final PacketBuffer buf) throws IOException {
                return buf.readBoolean();
            }
            
            @Override
            public DataParameter<Boolean> createKey(final int id) {
                return new DataParameter<Boolean>(id, this);
            }
            
            @Override
            public Boolean copyValue(final Boolean value) {
                return value;
            }
        };
        ROTATIONS = new DataSerializer<Rotations>() {
            @Override
            public void write(final PacketBuffer buf, final Rotations value) {
                buf.writeFloat(value.getX());
                buf.writeFloat(value.getY());
                buf.writeFloat(value.getZ());
            }
            
            @Override
            public Rotations read(final PacketBuffer buf) throws IOException {
                return new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
            }
            
            @Override
            public DataParameter<Rotations> createKey(final int id) {
                return new DataParameter<Rotations>(id, this);
            }
            
            @Override
            public Rotations copyValue(final Rotations value) {
                return value;
            }
        };
        BLOCK_POS = new DataSerializer<BlockPos>() {
            @Override
            public void write(final PacketBuffer buf, final BlockPos value) {
                buf.writeBlockPos(value);
            }
            
            @Override
            public BlockPos read(final PacketBuffer buf) throws IOException {
                return buf.readBlockPos();
            }
            
            @Override
            public DataParameter<BlockPos> createKey(final int id) {
                return new DataParameter<BlockPos>(id, this);
            }
            
            @Override
            public BlockPos copyValue(final BlockPos value) {
                return value;
            }
        };
        OPTIONAL_BLOCK_POS = new DataSerializer<Optional<BlockPos>>() {
            @Override
            public void write(final PacketBuffer buf, final Optional<BlockPos> value) {
                buf.writeBoolean(value.isPresent());
                if (value.isPresent()) {
                    buf.writeBlockPos((BlockPos)value.get());
                }
            }
            
            @Override
            public Optional<BlockPos> read(final PacketBuffer buf) throws IOException {
                return (Optional<BlockPos>)(buf.readBoolean() ? Optional.of((Object)buf.readBlockPos()) : Optional.absent());
            }
            
            @Override
            public DataParameter<Optional<BlockPos>> createKey(final int id) {
                return new DataParameter<Optional<BlockPos>>(id, this);
            }
            
            @Override
            public Optional<BlockPos> copyValue(final Optional<BlockPos> value) {
                return value;
            }
        };
        FACING = new DataSerializer<EnumFacing>() {
            @Override
            public void write(final PacketBuffer buf, final EnumFacing value) {
                buf.writeEnumValue(value);
            }
            
            @Override
            public EnumFacing read(final PacketBuffer buf) throws IOException {
                return buf.readEnumValue(EnumFacing.class);
            }
            
            @Override
            public DataParameter<EnumFacing> createKey(final int id) {
                return new DataParameter<EnumFacing>(id, this);
            }
            
            @Override
            public EnumFacing copyValue(final EnumFacing value) {
                return value;
            }
        };
        OPTIONAL_UNIQUE_ID = new DataSerializer<Optional<UUID>>() {
            @Override
            public void write(final PacketBuffer buf, final Optional<UUID> value) {
                buf.writeBoolean(value.isPresent());
                if (value.isPresent()) {
                    buf.writeUniqueId((UUID)value.get());
                }
            }
            
            @Override
            public Optional<UUID> read(final PacketBuffer buf) throws IOException {
                return (Optional<UUID>)(buf.readBoolean() ? Optional.of((Object)buf.readUniqueId()) : Optional.absent());
            }
            
            @Override
            public DataParameter<Optional<UUID>> createKey(final int id) {
                return new DataParameter<Optional<UUID>>(id, this);
            }
            
            @Override
            public Optional<UUID> copyValue(final Optional<UUID> value) {
                return value;
            }
        };
        COMPOUND_TAG = new DataSerializer<NBTTagCompound>() {
            @Override
            public void write(final PacketBuffer buf, final NBTTagCompound value) {
                buf.writeCompoundTag(value);
            }
            
            @Override
            public NBTTagCompound read(final PacketBuffer buf) throws IOException {
                return buf.readCompoundTag();
            }
            
            @Override
            public DataParameter<NBTTagCompound> createKey(final int id) {
                return new DataParameter<NBTTagCompound>(id, this);
            }
            
            @Override
            public NBTTagCompound copyValue(final NBTTagCompound value) {
                return value.copy();
            }
        };
        registerSerializer(DataSerializers.BYTE);
        registerSerializer(DataSerializers.VARINT);
        registerSerializer(DataSerializers.FLOAT);
        registerSerializer(DataSerializers.STRING);
        registerSerializer(DataSerializers.TEXT_COMPONENT);
        registerSerializer(DataSerializers.ITEM_STACK);
        registerSerializer(DataSerializers.BOOLEAN);
        registerSerializer(DataSerializers.ROTATIONS);
        registerSerializer(DataSerializers.BLOCK_POS);
        registerSerializer(DataSerializers.OPTIONAL_BLOCK_POS);
        registerSerializer(DataSerializers.FACING);
        registerSerializer(DataSerializers.OPTIONAL_UNIQUE_ID);
        registerSerializer(DataSerializers.OPTIONAL_BLOCK_STATE);
        registerSerializer(DataSerializers.COMPOUND_TAG);
    }
}
