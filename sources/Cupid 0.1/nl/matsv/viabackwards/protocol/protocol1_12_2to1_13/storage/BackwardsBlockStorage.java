package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.viaversion.libs.fastutil.ints.IntOpenHashSet;
import us.myles.viaversion.libs.fastutil.ints.IntSet;

public class BackwardsBlockStorage extends StoredObject {
  private static final IntSet WHITELIST = (IntSet)new IntOpenHashSet(779);
  
  static {
    int i;
    for (i = 5265; i <= 5286; i++)
      WHITELIST.add(i); 
    for (i = 0; i < 256; i++)
      WHITELIST.add(748 + i); 
    for (i = 6854; i <= 7173; i++)
      WHITELIST.add(i); 
    WHITELIST.add(1647);
    for (i = 5447; i <= 5566; i++)
      WHITELIST.add(i); 
    for (i = 1028; i <= 1039; i++)
      WHITELIST.add(i); 
    for (i = 1047; i <= 1082; i++)
      WHITELIST.add(i); 
    for (i = 1099; i <= 1110; i++)
      WHITELIST.add(i); 
  }
  
  private final Map<Position, Integer> blocks = new ConcurrentHashMap<>();
  
  public BackwardsBlockStorage(UserConnection user) {
    super(user);
  }
  
  public void checkAndStore(Position position, int block) {
    if (!WHITELIST.contains(block)) {
      this.blocks.remove(position);
      return;
    } 
    this.blocks.put(position, Integer.valueOf(block));
  }
  
  public boolean isWelcome(int block) {
    return WHITELIST.contains(block);
  }
  
  public Integer get(Position position) {
    return this.blocks.get(position);
  }
  
  public int remove(Position position) {
    return ((Integer)this.blocks.remove(position)).intValue();
  }
  
  public void clear() {
    this.blocks.clear();
  }
  
  public Map<Position, Integer> getBlocks() {
    return this.blocks;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\storage\BackwardsBlockStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */