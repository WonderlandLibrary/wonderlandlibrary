package viaversion.viabackwards.api.data;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import net.md_5.bungee.api.ChatColor;
import viaversion.viabackwards.utils.Block;
import org.jetbrains.annotations.Nullable;

public class MappedLegacyBlockItem {

    private final int id;
    private final short data;
    private final String name;
    private final Block block;
    private BlockEntityHandler blockEntityHandler;

    public MappedLegacyBlockItem(int id, short data, @Nullable String name, boolean block) {
        this.id = id;
        this.data = data;
        this.name = name != null ? ChatColor.RESET + name : null;
        this.block = block ? new Block(id, data) : null;
    }

    public int getId() {
        return id;
    }

    public short getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public boolean isBlock() {
        return block != null;
    }

    public Block getBlock() {
        return block;
    }

    public boolean hasBlockEntityHandler() {
        return blockEntityHandler != null;
    }

    @Nullable
    public BlockEntityHandler getBlockEntityHandler() {
        return blockEntityHandler;
    }

    public void setBlockEntityHandler(@Nullable BlockEntityHandler blockEntityHandler) {
        this.blockEntityHandler = blockEntityHandler;
    }

    @FunctionalInterface
    public interface BlockEntityHandler {

        CompoundTag handleOrNewCompoundTag(int block, CompoundTag tag);
    }
}
