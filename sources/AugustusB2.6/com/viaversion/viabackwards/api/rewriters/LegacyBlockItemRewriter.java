// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import java.util.HashMap;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viabackwards.utils.Block;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.BlockColors;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.api.data.MappedLegacyBlockItem;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import java.util.Map;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public abstract class LegacyBlockItemRewriter<T extends BackwardsProtocol> extends ItemRewriterBase<T>
{
    private static final Map<String, Int2ObjectMap<MappedLegacyBlockItem>> LEGACY_MAPPINGS;
    protected final Int2ObjectMap<MappedLegacyBlockItem> replacementData;
    
    protected LegacyBlockItemRewriter(final T protocol) {
        super(protocol, false);
        this.replacementData = LegacyBlockItemRewriter.LEGACY_MAPPINGS.get(protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        final MappedLegacyBlockItem data = this.replacementData.get(item.identifier());
        if (data == null) {
            return super.handleItemToClient(item);
        }
        final short originalData = item.data();
        item.setIdentifier(data.getId());
        if (data.getData() != -1) {
            item.setData(data.getData());
        }
        if (data.getName() != null) {
            if (item.tag() == null) {
                item.setTag(new CompoundTag());
            }
            CompoundTag display = item.tag().get("display");
            if (display == null) {
                item.tag().put("display", display = new CompoundTag());
            }
            StringTag nameTag = display.get("Name");
            if (nameTag == null) {
                display.put("Name", nameTag = new StringTag(data.getName()));
                display.put(this.nbtTagName + "|customName", new ByteTag());
            }
            final String value = nameTag.getValue();
            if (value.contains("%vb_color%")) {
                display.put("Name", new StringTag(value.replace("%vb_color%", BlockColors.get(originalData))));
            }
        }
        return item;
    }
    
    public int handleBlockID(final int idx) {
        final int type = idx >> 4;
        final int meta = idx & 0xF;
        final Block b = this.handleBlock(type, meta);
        if (b == null) {
            return idx;
        }
        return b.getId() << 4 | (b.getData() & 0xF);
    }
    
    public Block handleBlock(final int blockId, final int data) {
        final MappedLegacyBlockItem settings = this.replacementData.get(blockId);
        if (settings == null || !settings.isBlock()) {
            return null;
        }
        final Block block = settings.getBlock();
        if (block.getData() == -1) {
            return block.withData(data);
        }
        return block;
    }
    
    protected void handleChunk(final Chunk chunk) {
        final Map<Pos, CompoundTag> tags = new HashMap<Pos, CompoundTag>();
        for (final CompoundTag tag : chunk.getBlockEntities()) {
            final Tag xTag;
            final Tag yTag;
            if ((xTag = tag.get("x")) != null && (yTag = tag.get("y")) != null) {
                final Tag zTag;
                if ((zTag = tag.get("z")) == null) {
                    continue;
                }
                final Pos pos = new Pos(((NumberTag)xTag).asInt() & 0xF, ((NumberTag)yTag).asInt(), ((NumberTag)zTag).asInt() & 0xF);
                tags.put(pos, tag);
                if (pos.getY() < 0) {
                    continue;
                }
                if (pos.getY() > 255) {
                    continue;
                }
                final ChunkSection section = chunk.getSections()[pos.getY() >> 4];
                if (section == null) {
                    continue;
                }
                final int block = section.getFlatBlock(pos.getX(), pos.getY() & 0xF, pos.getZ());
                final int btype = block >> 4;
                final MappedLegacyBlockItem settings = this.replacementData.get(btype);
                if (settings == null || !settings.hasBlockEntityHandler()) {
                    continue;
                }
                settings.getBlockEntityHandler().handleOrNewCompoundTag(block, tag);
            }
        }
        for (int i = 0; i < chunk.getSections().length; ++i) {
            final ChunkSection section2 = chunk.getSections()[i];
            if (section2 != null) {
                boolean hasBlockEntityHandler = false;
                for (int j = 0; j < section2.getPaletteSize(); ++j) {
                    final int block2 = section2.getPaletteEntry(j);
                    final int btype2 = block2 >> 4;
                    final int meta = block2 & 0xF;
                    final Block b = this.handleBlock(btype2, meta);
                    if (b != null) {
                        section2.setPaletteEntry(j, b.getId() << 4 | (b.getData() & 0xF));
                    }
                    if (!hasBlockEntityHandler) {
                        final MappedLegacyBlockItem settings2 = this.replacementData.get(btype2);
                        if (settings2 != null && settings2.hasBlockEntityHandler()) {
                            hasBlockEntityHandler = true;
                        }
                    }
                }
                if (hasBlockEntityHandler) {
                    for (int x = 0; x < 16; ++x) {
                        for (int y = 0; y < 16; ++y) {
                            for (int z = 0; z < 16; ++z) {
                                final int block3 = section2.getFlatBlock(x, y, z);
                                final int btype3 = block3 >> 4;
                                final int meta2 = block3 & 0xF;
                                final MappedLegacyBlockItem settings = this.replacementData.get(btype3);
                                if (settings != null) {
                                    if (settings.hasBlockEntityHandler()) {
                                        final Pos pos2 = new Pos(x, y + (i << 4), z);
                                        if (!tags.containsKey(pos2)) {
                                            final CompoundTag tag2 = new CompoundTag();
                                            tag2.put("x", new IntTag(x + (chunk.getX() << 4)));
                                            tag2.put("y", new IntTag(y + (i << 4)));
                                            tag2.put("z", new IntTag(z + (chunk.getZ() << 4)));
                                            settings.getBlockEntityHandler().handleOrNewCompoundTag(block3, tag2);
                                            chunk.getBlockEntities().add(tag2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected CompoundTag getNamedTag(String text) {
        final CompoundTag tag = new CompoundTag();
        tag.put("display", new CompoundTag());
        text = "§r" + text;
        tag.get("display").put("Name", new StringTag(this.jsonNameFormat ? ChatRewriter.legacyTextToJsonString(text) : text));
        return tag;
    }
    
    static {
        LEGACY_MAPPINGS = new HashMap<String, Int2ObjectMap<MappedLegacyBlockItem>>();
        final JsonObject jsonObject = VBMappingDataLoader.loadFromDataDir("legacy-mappings.json");
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final Int2ObjectMap<MappedLegacyBlockItem> mappings = new Int2ObjectOpenHashMap<MappedLegacyBlockItem>(8);
            LegacyBlockItemRewriter.LEGACY_MAPPINGS.put(entry.getKey(), mappings);
            for (final Map.Entry<String, JsonElement> dataEntry : entry.getValue().getAsJsonObject().entrySet()) {
                final JsonObject object = dataEntry.getValue().getAsJsonObject();
                final int id = object.getAsJsonPrimitive("id").getAsInt();
                final JsonPrimitive jsonData = object.getAsJsonPrimitive("data");
                final short data = (short)((jsonData != null) ? jsonData.getAsShort() : 0);
                final String name = object.getAsJsonPrimitive("name").getAsString();
                final JsonPrimitive blockField = object.getAsJsonPrimitive("block");
                final boolean block = blockField != null && blockField.getAsBoolean();
                if (dataEntry.getKey().indexOf(45) != -1) {
                    final String[] split = dataEntry.getKey().split("-", 2);
                    final int from = Integer.parseInt(split[0]);
                    final int to = Integer.parseInt(split[1]);
                    if (name.contains("%color%")) {
                        for (int i = from; i <= to; ++i) {
                            mappings.put(i, new MappedLegacyBlockItem(id, data, name.replace("%color%", BlockColors.get(i - from)), block));
                        }
                    }
                    else {
                        final MappedLegacyBlockItem mappedBlockItem = new MappedLegacyBlockItem(id, data, name, block);
                        for (int j = from; j <= to; ++j) {
                            mappings.put(j, mappedBlockItem);
                        }
                    }
                }
                else {
                    mappings.put(Integer.parseInt(dataEntry.getKey()), new MappedLegacyBlockItem(id, data, name, block));
                }
            }
        }
    }
    
    private static final class Pos
    {
        private final int x;
        private final short y;
        private final int z;
        
        private Pos(final int x, final int y, final int z) {
            this.x = x;
            this.y = (short)y;
            this.z = z;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public int getZ() {
            return this.z;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Pos pos = (Pos)o;
            return this.x == pos.x && this.y == pos.y && this.z == pos.z;
        }
        
        @Override
        public int hashCode() {
            int result = this.x;
            result = 31 * result + this.y;
            result = 31 * result + this.z;
            return result;
        }
        
        @Override
        public String toString() {
            return "Pos{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
        }
    }
}
