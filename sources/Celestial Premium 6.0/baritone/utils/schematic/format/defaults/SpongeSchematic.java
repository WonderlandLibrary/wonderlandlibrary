/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
 */
package baritone.utils.schematic.format.defaults;

import baritone.utils.schematic.StaticSchematic;
import baritone.utils.type.VarInt;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public final class SpongeSchematic
extends StaticSchematic {
    public SpongeSchematic(NBTTagCompound nbt) {
        this.x = nbt.getInteger("Width");
        this.y = nbt.getInteger("Height");
        this.z = nbt.getInteger("Length");
        this.states = new IBlockState[this.x][this.z][this.y];
        Int2ObjectArrayMap palette = new Int2ObjectArrayMap();
        NBTTagCompound paletteTag = nbt.getCompoundTag("Palette");
        for (String tag : paletteTag.getKeySet()) {
            int index = paletteTag.getInteger(tag);
            SerializedBlockState serializedState = SerializedBlockState.getFromString(tag);
            if (serializedState == null) {
                throw new IllegalArgumentException("Unable to parse palette tag");
            }
            IBlockState state = serializedState.deserialize();
            if (state == null) {
                throw new IllegalArgumentException("Unable to deserialize palette tag");
            }
            palette.put(index, (Object)state);
        }
        byte[] rawBlockData = nbt.getByteArray("BlockData");
        int[] blockData = new int[this.x * this.y * this.z];
        int offset = 0;
        for (int i = 0; i < blockData.length; ++i) {
            if (offset >= rawBlockData.length) {
                throw new IllegalArgumentException("No remaining bytes in BlockData for complete schematic");
            }
            VarInt varInt = VarInt.read(rawBlockData, offset);
            blockData[i] = varInt.getValue();
            offset += varInt.getSize();
        }
        for (int y = 0; y < this.y; ++y) {
            for (int z = 0; z < this.z; ++z) {
                for (int x = 0; x < this.x; ++x) {
                    int index = (y * this.z + z) * this.x + x;
                    IBlockState state = (IBlockState)palette.get(blockData[index]);
                    if (state == null) {
                        throw new IllegalArgumentException("Invalid Palette Index " + index);
                    }
                    this.states[x][z][y] = state;
                }
            }
        }
    }

    private static final class SerializedBlockState {
        private static final Pattern REGEX = Pattern.compile("(?<location>(\\w+:)?\\w+)(\\[(?<properties>(\\w+=\\w+,?)+)])?");
        private final ResourceLocation resourceLocation;
        private final Map<String, String> properties;
        private IBlockState blockState;

        private SerializedBlockState(ResourceLocation resourceLocation, Map<String, String> properties) {
            this.resourceLocation = resourceLocation;
            this.properties = properties;
        }

        private IBlockState deserialize() {
            if (this.blockState == null) {
                Block block = Block.REGISTRY.getObject(this.resourceLocation);
                this.blockState = block.getDefaultState();
                this.properties.keySet().stream().sorted(String::compareTo).forEachOrdered(key -> {
                    IProperty<?> property = block.getBlockState().getProperty((String)key);
                    if (property != null) {
                        this.blockState = SerializedBlockState.setPropertyValue(this.blockState, property, this.properties.get(key));
                    }
                });
            }
            return this.blockState;
        }

        private static SerializedBlockState getFromString(String s) {
            Matcher m = REGEX.matcher(s);
            if (!m.matches()) {
                return null;
            }
            try {
                String location = m.group("location");
                String properties = m.group("properties");
                ResourceLocation resourceLocation = new ResourceLocation(location);
                HashMap<String, String> propertiesMap = new HashMap<String, String>();
                if (properties != null) {
                    for (String property : properties.split(",")) {
                        String[] split = property.split("=");
                        propertiesMap.put(split[0], split[1]);
                    }
                }
                return new SerializedBlockState(resourceLocation, propertiesMap);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private static <T extends Comparable<T>> IBlockState setPropertyValue(IBlockState state, IProperty<T> property, String value) {
            Optional<T> parsed = property.parseValue(value).toJavaUtil();
            if (parsed.isPresent()) {
                return state.withProperty(property, (Comparable)parsed.get());
            }
            throw new IllegalArgumentException("Invalid value for property " + property);
        }
    }
}

