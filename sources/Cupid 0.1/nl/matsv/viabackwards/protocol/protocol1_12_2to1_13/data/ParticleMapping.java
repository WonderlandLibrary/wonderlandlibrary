package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.util.List;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.Particle;

public class ParticleMapping {
  private static final ParticleData[] particles;
  
  static {
    ParticleHandler blockHandler = new ParticleHandler() {
        public int[] rewrite(Protocol1_12_2To1_13 protocol, PacketWrapper wrapper) throws Exception {
          return rewrite(((Integer)wrapper.read((Type)Type.VAR_INT)).intValue());
        }
        
        public int[] rewrite(Protocol1_12_2To1_13 protocol, List<Particle.ParticleData> data) {
          return rewrite(((Integer)((Particle.ParticleData)data.get(0)).getValue()).intValue());
        }
        
        private int[] rewrite(int newType) {
          int blockType = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(newType);
          int type = blockType >> 4;
          int meta = blockType & 0xF;
          return new int[] { type + (meta << 12) };
        }
        
        public boolean isBlockHandler() {
          return true;
        }
      };
    particles = new ParticleData[] { 
        rewrite(16), rewrite(20), rewrite(35), rewrite(37, blockHandler), rewrite(4), rewrite(29), rewrite(9), rewrite(44), rewrite(42), rewrite(19), 
        rewrite(18), rewrite(30, new ParticleHandler() {
            public int[] rewrite(Protocol1_12_2To1_13 protocol, PacketWrapper wrapper) throws Exception {
              float r = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
              float g = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
              float b = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
              float scale = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
              wrapper.set((Type)Type.FLOAT, 3, Float.valueOf(r));
              wrapper.set((Type)Type.FLOAT, 4, Float.valueOf(g));
              wrapper.set((Type)Type.FLOAT, 5, Float.valueOf(b));
              wrapper.set((Type)Type.FLOAT, 6, Float.valueOf(scale));
              wrapper.set(Type.INT, 1, Integer.valueOf(0));
              return null;
            }
            
            public int[] rewrite(Protocol1_12_2To1_13 protocol, List<Particle.ParticleData> data) {
              return null;
            }
          }), rewrite(13), rewrite(41), rewrite(10), rewrite(25), rewrite(43), rewrite(15), rewrite(2), rewrite(1), 
        rewrite(46, blockHandler), rewrite(3), rewrite(6), rewrite(26), rewrite(21), rewrite(34), rewrite(14), rewrite(36, new ParticleHandler() {
            public int[] rewrite(Protocol1_12_2To1_13 protocol, PacketWrapper wrapper) throws Exception {
              return rewrite(protocol, (Item)wrapper.read(Type.FLAT_ITEM));
            }
            
            public int[] rewrite(Protocol1_12_2To1_13 protocol, List<Particle.ParticleData> data) {
              return rewrite(protocol, (Item)((Particle.ParticleData)data.get(0)).getValue());
            }
            
            private int[] rewrite(Protocol1_12_2To1_13 protocol, Item newItem) {
              Item item = protocol.getBlockItemPackets().handleItemToClient(newItem);
              return new int[] { item.getIdentifier(), item.getData() };
            }
          }), rewrite(33), rewrite(31), 
        rewrite(12), rewrite(27), rewrite(22), rewrite(23), rewrite(0), rewrite(24), rewrite(39), rewrite(11), rewrite(48), rewrite(12), 
        rewrite(45), rewrite(47), rewrite(7), rewrite(5), rewrite(17), rewrite(4), rewrite(4), rewrite(4), rewrite(18), rewrite(18) };
  }
  
  public static ParticleData getMapping(int id) {
    return particles[id];
  }
  
  private static ParticleData rewrite(int replacementId) {
    return new ParticleData(replacementId);
  }
  
  private static ParticleData rewrite(int replacementId, ParticleHandler handler) {
    return new ParticleData(replacementId, handler);
  }
  
  public static interface ParticleHandler {
    int[] rewrite(Protocol1_12_2To1_13 param1Protocol1_12_2To1_13, PacketWrapper param1PacketWrapper) throws Exception;
    
    int[] rewrite(Protocol1_12_2To1_13 param1Protocol1_12_2To1_13, List<Particle.ParticleData> param1List);
    
    default boolean isBlockHandler() {
      return false;
    }
  }
  
  public static final class ParticleData {
    private final int historyId;
    
    private final ParticleMapping.ParticleHandler handler;
    
    private ParticleData(int historyId, ParticleMapping.ParticleHandler handler) {
      this.historyId = historyId;
      this.handler = handler;
    }
    
    private ParticleData(int historyId) {
      this(historyId, (ParticleMapping.ParticleHandler)null);
    }
    
    public int[] rewriteData(Protocol1_12_2To1_13 protocol, PacketWrapper wrapper) throws Exception {
      if (this.handler == null)
        return null; 
      return this.handler.rewrite(protocol, wrapper);
    }
    
    public int[] rewriteMeta(Protocol1_12_2To1_13 protocol, List<Particle.ParticleData> data) {
      if (this.handler == null)
        return null; 
      return this.handler.rewrite(protocol, data);
    }
    
    public int getHistoryId() {
      return this.historyId;
    }
    
    public ParticleMapping.ParticleHandler getHandler() {
      return this.handler;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\data\ParticleMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */