package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.Rewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;

public class PlayerPackets1_14 extends Rewriter<Protocol1_13_2To1_14> {
  public PlayerPackets1_14(Protocol1_13_2To1_14 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SERVER_DIFFICULTY, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map(Type.BOOLEAN, Type.NOTHING);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_SIGN_EDITOR, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION1_14, Type.POSITION);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.QUERY_BLOCK_NBT, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.POSITION, Type.POSITION1_14);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.PLAYER_DIGGING, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.POSITION, Type.POSITION1_14);
            map(Type.BYTE);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.RECIPE_BOOK_DATA, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int type = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    if (type == 0) {
                      wrapper.passthrough(Type.STRING);
                    } else if (type == 1) {
                      wrapper.passthrough(Type.BOOLEAN);
                      wrapper.passthrough(Type.BOOLEAN);
                      wrapper.passthrough(Type.BOOLEAN);
                      wrapper.passthrough(Type.BOOLEAN);
                      wrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                      wrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                      wrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                      wrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                    } 
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.UPDATE_COMMAND_BLOCK, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION, Type.POSITION1_14);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.UPDATE_STRUCTURE_BLOCK, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION, Type.POSITION1_14);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.UPDATE_SIGN, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION, Type.POSITION1_14);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Position position = (Position)wrapper.read(Type.POSITION);
                    int face = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int hand = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    float x = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
                    float y = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
                    float z = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(hand));
                    wrapper.write(Type.POSITION1_14, position);
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(face));
                    wrapper.write((Type)Type.FLOAT, Float.valueOf(x));
                    wrapper.write((Type)Type.FLOAT, Float.valueOf(y));
                    wrapper.write((Type)Type.FLOAT, Float.valueOf(z));
                    wrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13_2to1_14\packets\PlayerPackets1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */