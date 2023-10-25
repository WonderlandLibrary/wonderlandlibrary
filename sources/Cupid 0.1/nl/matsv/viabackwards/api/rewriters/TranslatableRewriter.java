package nl.matsv.viabackwards.api.rewriters;

import java.util.HashMap;
import java.util.Map;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.VBMappingDataLoader;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;

public class TranslatableRewriter extends ComponentRewriter {
  private static final Map<String, Map<String, String>> TRANSLATABLES = new HashMap<>();
  
  protected final Map<String, String> newTranslatables;
  
  public static void loadTranslatables() {
    JsonObject jsonObject = VBMappingDataLoader.loadData("translation-mappings.json");
    for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)jsonObject.entrySet()) {
      Map<String, String> versionMappings = new HashMap<>();
      TRANSLATABLES.put(entry.getKey(), versionMappings);
      for (Map.Entry<String, JsonElement> translationEntry : (Iterable<Map.Entry<String, JsonElement>>)((JsonElement)entry.getValue()).getAsJsonObject().entrySet())
        versionMappings.put(translationEntry.getKey(), ((JsonElement)translationEntry.getValue()).getAsString()); 
    } 
  }
  
  public TranslatableRewriter(BackwardsProtocol protocol) {
    this(protocol, protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
  }
  
  public TranslatableRewriter(BackwardsProtocol protocol, String sectionIdentifier) {
    super((Protocol)protocol);
    Map<String, String> newTranslatables = TRANSLATABLES.get(sectionIdentifier);
    if (newTranslatables == null) {
      ViaBackwards.getPlatform().getLogger().warning("Error loading " + sectionIdentifier + " translatables!");
      this.newTranslatables = new HashMap<>();
    } else {
      this.newTranslatables = newTranslatables;
    } 
  }
  
  public void registerPing() {
    this.protocol.registerOutgoing(State.LOGIN, 0, 0, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
          }
        });
  }
  
  public void registerDisconnect(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
          }
        });
  }
  
  public void registerChatMessage(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
          }
        });
  }
  
  public void registerLegacyOpenWindow(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
          }
        });
  }
  
  public void registerOpenWindow(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
          }
        });
  }
  
  public void registerTabList(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT));
                  TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT));
                });
          }
        });
  }
  
  protected void handleTranslate(JsonObject root, String translate) {
    String newTranslate = this.newTranslatables.get(translate);
    if (newTranslate != null)
      root.addProperty("translate", newTranslate); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\TranslatableRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */