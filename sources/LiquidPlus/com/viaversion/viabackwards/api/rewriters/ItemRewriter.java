/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viabackwards.api.data.MappedItem;
import com.viaversion.viabackwards.api.rewriters.ItemRewriterBase;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ItemRewriter<T extends BackwardsProtocol>
extends ItemRewriterBase<T> {
    protected ItemRewriter(T protocol) {
        super(protocol, true);
    }

    @Override
    public @Nullable Item handleItemToClient(@Nullable Item item) {
        MappedItem data;
        CompoundTag display;
        if (item == null) {
            return null;
        }
        CompoundTag compoundTag = display = item.tag() != null ? (CompoundTag)item.tag().get("display") : null;
        if (((BackwardsProtocol)this.protocol).getTranslatableRewriter() != null && display != null) {
            ListTag lore;
            StringTag name = (StringTag)display.get("Name");
            if (name != null) {
                String newValue = ((BackwardsProtocol)this.protocol).getTranslatableRewriter().processText(name.getValue()).toString();
                if (!newValue.equals(name.getValue())) {
                    this.saveStringTag(display, name, "Name");
                }
                name.setValue(newValue);
            }
            if ((lore = (ListTag)display.get("Lore")) != null) {
                boolean changed = false;
                for (Tag loreEntryTag : lore) {
                    if (!(loreEntryTag instanceof StringTag)) continue;
                    StringTag loreEntry = (StringTag)loreEntryTag;
                    String newValue = ((BackwardsProtocol)this.protocol).getTranslatableRewriter().processText(loreEntry.getValue()).toString();
                    if (!changed && !newValue.equals(loreEntry.getValue())) {
                        changed = true;
                        this.saveListTag(display, lore, "Lore");
                    }
                    loreEntry.setValue(newValue);
                }
            }
        }
        if ((data = ((BackwardsProtocol)this.protocol).getMappingData().getMappedItem(item.identifier())) == null) {
            return super.handleItemToClient(item);
        }
        if (item.tag() == null) {
            item.setTag(new CompoundTag());
        }
        item.tag().put(this.nbtTagName + "|id", new IntTag(item.identifier()));
        item.setIdentifier(data.getId());
        if (display == null) {
            display = new CompoundTag();
            item.tag().put("display", display);
        }
        if (!display.contains("Name")) {
            display.put("Name", new StringTag(data.getJsonName()));
            display.put(this.nbtTagName + "|customName", new ByteTag());
        }
        return item;
    }

    @Override
    public @Nullable Item handleItemToServer(@Nullable Item item) {
        IntTag originalId;
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        if (item.tag() != null && (originalId = (IntTag)item.tag().remove(this.nbtTagName + "|id")) != null) {
            item.setIdentifier(originalId.asInt());
        }
        return item;
    }

    @Override
    public void registerAdvancements(ClientboundPacketType packetType, final Type<Item> type) {
        ((BackwardsProtocol)this.protocol).registerClientbound(packetType, new PacketRemapper(){

            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.BOOLEAN);
                    int size = wrapper.passthrough(Type.VAR_INT);
                    for (int i = 0; i < size; ++i) {
                        wrapper.passthrough(Type.STRING);
                        if (wrapper.passthrough(Type.BOOLEAN).booleanValue()) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (wrapper.passthrough(Type.BOOLEAN).booleanValue()) {
                            JsonElement title = wrapper.passthrough(Type.COMPONENT);
                            JsonElement description = wrapper.passthrough(Type.COMPONENT);
                            TranslatableRewriter translatableRewriter = ((BackwardsProtocol)ItemRewriter.this.protocol).getTranslatableRewriter();
                            if (translatableRewriter != null) {
                                translatableRewriter.processText(title);
                                translatableRewriter.processText(description);
                            }
                            ItemRewriter.this.handleItemToClient((Item)wrapper.passthrough(type));
                            wrapper.passthrough(Type.VAR_INT);
                            int flags = wrapper.passthrough(Type.INT);
                            if ((flags & 1) != 0) {
                                wrapper.passthrough(Type.STRING);
                            }
                            wrapper.passthrough(Type.FLOAT);
                            wrapper.passthrough(Type.FLOAT);
                        }
                        wrapper.passthrough(Type.STRING_ARRAY);
                        int arrayLength = wrapper.passthrough(Type.VAR_INT);
                        for (int array = 0; array < arrayLength; ++array) {
                            wrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
    }
}

