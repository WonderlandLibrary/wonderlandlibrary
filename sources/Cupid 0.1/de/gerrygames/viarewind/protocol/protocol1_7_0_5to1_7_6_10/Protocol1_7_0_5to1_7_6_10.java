package de.gerrygames.viarewind.protocol.protocol1_7_0_5to1_7_6_10;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;

public class Protocol1_7_0_5to1_7_6_10 extends Protocol {
  public static final ValueTransformer<String, String> REMOVE_DASHES = new ValueTransformer<String, String>(Type.STRING) {
      public String transform(PacketWrapper packetWrapper, String s) {
        return s.replace("-", "");
      }
    };
  
  protected void registerPackets() {
    registerOutgoing(State.LOGIN, 2, 2, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING, Protocol1_7_0_5to1_7_6_10.REMOVE_DASHES);
            map(Type.STRING);
          }
        });
    registerOutgoing(State.PLAY, 12, 12, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.STRING, Protocol1_7_0_5to1_7_6_10.REMOVE_DASHES);
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int size = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    for (int i = 0; i < size * 3; ) {
                      packetWrapper.read(Type.STRING);
                      i++;
                    } 
                  }
                });
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.BYTE);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Types1_7_6_10.METADATA_LIST);
          }
        });
    registerOutgoing(State.PLAY, 62, 62, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    byte mode = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    if (mode == 0 || mode == 2) {
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.BYTE);
                    } 
                    if (mode == 0 || mode == 3 || mode == 4) {
                      int size = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                      List<String> entryList = new ArrayList<>();
                      for (int i = 0; i < size; i++) {
                        String entry = (String)packetWrapper.read(Type.STRING);
                        if (entry != null) {
                          if (entry.length() > 16)
                            entry = entry.substring(0, 16); 
                          if (!entryList.contains(entry))
                            entryList.add(entry); 
                        } 
                      } 
                      packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)entryList.size()));
                      for (String entry : entryList)
                        packetWrapper.write(Type.STRING, entry); 
                    } 
                  }
                });
          }
        });
  }
  
  public void init(UserConnection userConnection) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_0_5to1_7_6_10\Protocol1_7_0_5to1_7_6_10.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */