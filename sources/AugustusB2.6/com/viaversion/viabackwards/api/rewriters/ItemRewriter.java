// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viabackwards.api.data.MappedItem;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public abstract class ItemRewriter<T extends BackwardsProtocol> extends ItemRewriterBase<T>
{
    private final TranslatableRewriter translatableRewriter;
    
    protected ItemRewriter(final T protocol, final TranslatableRewriter translatableRewriter) {
        super(protocol, true);
        this.translatableRewriter = translatableRewriter;
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag display = (item.tag() != null) ? item.tag().get("display") : null;
        if (this.translatableRewriter != null && display != null) {
            final StringTag name = display.get("Name");
            if (name != null) {
                final String newValue = this.translatableRewriter.processText(name.getValue()).toString();
                if (!newValue.equals(name.getValue())) {
                    this.saveStringTag(display, name, "Name");
                }
                name.setValue(newValue);
            }
            final ListTag lore = display.get("Lore");
            if (lore != null) {
                boolean changed = false;
                for (final Tag loreEntryTag : lore) {
                    if (!(loreEntryTag instanceof StringTag)) {
                        continue;
                    }
                    final StringTag loreEntry = (StringTag)loreEntryTag;
                    final String newValue2 = this.translatableRewriter.processText(loreEntry.getValue()).toString();
                    if (!changed && !newValue2.equals(loreEntry.getValue())) {
                        changed = true;
                        this.saveListTag(display, lore, "Lore");
                    }
                    loreEntry.setValue(newValue2);
                }
            }
        }
        final MappedItem data = this.protocol.getMappingData().getMappedItem(item.identifier());
        if (data == null) {
            return super.handleItemToClient(item);
        }
        if (item.tag() == null) {
            item.setTag(new CompoundTag());
        }
        item.tag().put(this.nbtTagName + "|id", new IntTag(item.identifier()));
        item.setIdentifier(data.getId());
        if (display == null) {
            item.tag().put("display", display = new CompoundTag());
        }
        if (!display.contains("Name")) {
            display.put("Name", new StringTag(data.getJsonName()));
            display.put(this.nbtTagName + "|customName", new ByteTag());
        }
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        if (item.tag() != null) {
            final IntTag originalId = item.tag().remove(this.nbtTagName + "|id");
            if (originalId != null) {
                item.setIdentifier(originalId.asInt());
            }
        }
        return item;
    }
}
