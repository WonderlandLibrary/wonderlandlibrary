package nl.matsv.viabackwards.api.data;

import nl.matsv.viabackwards.utils.Block;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public class MappedLegacyBlockItem {
  private final int id;
  
  private final short data;
  
  private final String name;
  
  private final Block block;
  
  private BlockEntityHandler blockEntityHandler;
  
  public MappedLegacyBlockItem(int id, short data, @Nullable String name, boolean block) {
    this.id = id;
    this.data = data;
    this.name = (name != null) ? (ChatColor.RESET + name) : null;
    this.block = block ? new Block(id, data) : null;
  }
  
  public int getId() {
    return this.id;
  }
  
  public short getData() {
    return this.data;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isBlock() {
    return (this.block != null);
  }
  
  public Block getBlock() {
    return this.block;
  }
  
  public boolean hasBlockEntityHandler() {
    return (this.blockEntityHandler != null);
  }
  
  @Nullable
  public BlockEntityHandler getBlockEntityHandler() {
    return this.blockEntityHandler;
  }
  
  public void setBlockEntityHandler(@Nullable BlockEntityHandler blockEntityHandler) {
    this.blockEntityHandler = blockEntityHandler;
  }
  
  @FunctionalInterface
  public static interface BlockEntityHandler {
    CompoundTag handleOrNewCompoundTag(int param1Int, CompoundTag param1CompoundTag);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\data\MappedLegacyBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */