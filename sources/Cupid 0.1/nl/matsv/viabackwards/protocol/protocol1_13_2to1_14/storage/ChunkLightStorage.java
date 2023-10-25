package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class ChunkLightStorage extends StoredObject {
  public static final byte[] FULL_LIGHT = new byte[2048];
  
  public static final byte[] EMPTY_LIGHT = new byte[2048];
  
  private static Constructor<?> fastUtilLongObjectHashMap;
  
  private final Map<Long, ChunkLight> storedLight = createLongObjectMap();
  
  static {
    Arrays.fill(FULL_LIGHT, (byte)-1);
    Arrays.fill(EMPTY_LIGHT, (byte)0);
    try {
      fastUtilLongObjectHashMap = Class.forName("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap").getConstructor(new Class[0]);
    } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {}
  }
  
  public ChunkLightStorage(UserConnection user) {
    super(user);
  }
  
  public void setStoredLight(byte[][] skyLight, byte[][] blockLight, int x, int z) {
    this.storedLight.put(Long.valueOf(getChunkSectionIndex(x, z)), new ChunkLight(skyLight, blockLight));
  }
  
  public ChunkLight getStoredLight(int x, int z) {
    return this.storedLight.get(Long.valueOf(getChunkSectionIndex(x, z)));
  }
  
  public void clear() {
    this.storedLight.clear();
  }
  
  public void unloadChunk(int x, int z) {
    this.storedLight.remove(Long.valueOf(getChunkSectionIndex(x, z)));
  }
  
  private long getChunkSectionIndex(int x, int z) {
    return (x & 0x3FFFFFFL) << 38L | z & 0x3FFFFFFL;
  }
  
  private Map<Long, ChunkLight> createLongObjectMap() {
    if (fastUtilLongObjectHashMap != null)
      try {
        return (Map<Long, ChunkLight>)fastUtilLongObjectHashMap.newInstance(new Object[0]);
      } catch (IllegalAccessException|InstantiationException|java.lang.reflect.InvocationTargetException e) {
        e.printStackTrace();
      }  
    return new HashMap<>();
  }
  
  public static class ChunkLight {
    private final byte[][] skyLight;
    
    private final byte[][] blockLight;
    
    public ChunkLight(byte[][] skyLight, byte[][] blockLight) {
      this.skyLight = skyLight;
      this.blockLight = blockLight;
    }
    
    public byte[][] getSkyLight() {
      return this.skyLight;
    }
    
    public byte[][] getBlockLight() {
      return this.blockLight;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13_2to1_14\storage\ChunkLightStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */