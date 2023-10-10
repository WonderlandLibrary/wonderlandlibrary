// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.rewriter.ItemRewriter;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public abstract class ItemRewriterBase<T extends BackwardsProtocol> extends ItemRewriter<T>
{
    protected final String nbtTagName;
    protected final boolean jsonNameFormat;
    
    protected ItemRewriterBase(final T protocol, final boolean jsonNameFormat) {
        super(protocol);
        this.jsonNameFormat = jsonNameFormat;
        this.nbtTagName = "VB|" + protocol.getClass().getSimpleName();
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToServer(item);
        this.restoreDisplayTag(item);
        return item;
    }
    
    protected boolean hasBackupTag(final CompoundTag displayTag, final String tagName) {
        return displayTag.contains(this.nbtTagName + "|o" + tagName);
    }
    
    protected void saveStringTag(final CompoundTag displayTag, final StringTag original, final String name) {
        final String backupName = this.nbtTagName + "|o" + name;
        if (!displayTag.contains(backupName)) {
            displayTag.put(backupName, new StringTag(original.getValue()));
        }
    }
    
    protected void saveListTag(final CompoundTag displayTag, final ListTag original, final String name) {
        final String backupName = this.nbtTagName + "|o" + name;
        if (!displayTag.contains(backupName)) {
            final ListTag listTag = new ListTag();
            for (final Tag tag : original.getValue()) {
                listTag.add(tag.clone());
            }
            displayTag.put(backupName, listTag);
        }
    }
    
    protected void restoreDisplayTag(final Item item) {
        if (item.tag() == null) {
            return;
        }
        final CompoundTag display = item.tag().get("display");
        if (display != null) {
            if (display.remove(this.nbtTagName + "|customName") != null) {
                display.remove("Name");
            }
            else {
                this.restoreStringTag(display, "Name");
            }
            this.restoreListTag(display, "Lore");
        }
    }
    
    protected void restoreStringTag(final CompoundTag tag, final String tagName) {
        final StringTag original = tag.remove(this.nbtTagName + "|o" + tagName);
        if (original != null) {
            tag.put(tagName, new StringTag(original.getValue()));
        }
    }
    
    protected void restoreListTag(final CompoundTag tag, final String tagName) {
        final ListTag original = tag.remove(this.nbtTagName + "|o" + tagName);
        if (original != null) {
            tag.put(tagName, new ListTag(original.getValue()));
        }
    }
    
    public String getNbtTagName() {
        return this.nbtTagName;
    }
}
