package viaversion.viabackwards.api.rewriters;

import com.github.steveice10.opennbt.conversion.builtin.CompoundTagConverter;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import viaversion.viabackwards.api.BackwardsProtocol;
import org.jetbrains.annotations.Nullable;
import viaversion.viaversion.api.minecraft.item.Item;

public abstract class ItemRewriterBase<T extends BackwardsProtocol> extends Rewriter<T> {

    protected static final CompoundTagConverter CONVERTER = new CompoundTagConverter();
    protected final String nbtTagName;
    protected final boolean jsonNameFormat;

    protected ItemRewriterBase(T protocol, boolean jsonNameFormat) {
        super(protocol);
        this.jsonNameFormat = jsonNameFormat;
        nbtTagName = "VB|" + protocol.getClass().getCanonicalName();
    }

    @Nullable
    public Item handleItemToClient(Item item) {
        if (item == null) return null;
        if (protocol.getMappingData() != null && protocol.getMappingData().getItemMappings() != null) {
            item.setIdentifier(protocol.getMappingData().getNewItemId(item.getIdentifier()));
        }
        return item;
    }

    @Nullable
    public Item handleItemToServer(Item item) {
        if (item == null) return null;
        if (protocol.getMappingData() != null && protocol.getMappingData().getItemMappings() != null) {
            item.setIdentifier(protocol.getMappingData().getOldItemId(item.getIdentifier()));
        }
        restoreDisplayTag(item);
        return item;
    }

    protected void saveNameTag(CompoundTag displayTag, StringTag original) {
        displayTag.put(new StringTag(nbtTagName + "|o" + original.getName(), original.getValue()));
    }

    protected void saveLoreTag(CompoundTag displayTag, ListTag original) {
        displayTag.put(new ListTag(nbtTagName + "|o" + original.getName(), original.getValue()));
    }

    protected void restoreDisplayTag(Item item) {
        if (item.getTag() == null) return;

        CompoundTag display = item.getTag().get("display");
        if (display != null) {
            // Remove custom name / restore original name
            if (display.remove(nbtTagName + "|customName") != null) {
                display.remove("Name");
            } else {
                restoreDisplayTag(display, "Name");
            }

            // Restore lore
            restoreDisplayTag(display, "Lore");
        }
    }

    protected void restoreDisplayTag(CompoundTag displayTag, String tagName) {
        Tag original = displayTag.remove(nbtTagName + "|o" + tagName);
        if (original != null) {
            displayTag.put(original);
        }
    }
}
