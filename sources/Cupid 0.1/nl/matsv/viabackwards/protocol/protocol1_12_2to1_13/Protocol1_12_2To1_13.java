package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.BackwardsMappings;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.PaintingMapping;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.BlockItemPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.EntityPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.PlayerPacket1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.SoundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.PlayerPositionStorage1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.providers.Provider;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.gson.JsonObject;

public class Protocol1_12_2To1_13 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_12_1, ServerboundPackets1_13, ServerboundPackets1_12_1> {
  public static final BackwardsMappings MAPPINGS = new BackwardsMappings();
  
  private BlockItemPackets1_13 blockItemPackets;
  
  public Protocol1_12_2To1_13() {
    super(ClientboundPackets1_13.class, ClientboundPackets1_12_1.class, ServerboundPackets1_13.class, ServerboundPackets1_12_1.class);
  }
  
  protected void registerPackets() {
    executeAsyncAfterLoaded(Protocol1_13To1_12_2.class, () -> {
          MAPPINGS.load();
          PaintingMapping.init();
          Via.getManager().getProviders().register(BackwardsBlockEntityProvider.class, (Provider)new BackwardsBlockEntityProvider());
        });
    TranslatableRewriter translatableRewriter = new TranslatableRewriter(this) {
        protected void handleTranslate(JsonObject root, String translate) {
          String newTranslate = (String)this.newTranslatables.get(translate);
          if (newTranslate != null || (newTranslate = (String)Protocol1_12_2To1_13.this.getMappingData().getTranslateMappings().get(translate)) != null)
            root.addProperty("translate", newTranslate); 
        }
      };
    translatableRewriter.registerPing();
    translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_13.BOSSBAR);
    translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_13.CHAT_MESSAGE);
    translatableRewriter.registerLegacyOpenWindow((ClientboundPacketType)ClientboundPackets1_13.OPEN_WINDOW);
    translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_13.DISCONNECT);
    translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_13.COMBAT_EVENT);
    translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_13.TITLE);
    translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_13.TAB_LIST);
    (this.blockItemPackets = new BlockItemPackets1_13(this)).register();
    (new EntityPackets1_13(this)).register();
    (new PlayerPacket1_13(this)).register();
    (new SoundPackets1_13(this)).register();
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.DECLARE_COMMANDS);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.NBT_QUERY);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.CRAFT_RECIPE_RESPONSE);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.UNLOCK_RECIPES);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.ADVANCEMENTS);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.DECLARE_RECIPES);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.TAGS);
    cancelIncoming((ServerboundPacketType)ServerboundPackets1_12_1.CRAFT_RECIPE_REQUEST);
    cancelIncoming((ServerboundPacketType)ServerboundPackets1_12_1.RECIPE_BOOK_DATA);
  }
  
  public void init(UserConnection user) {
    if (!user.has(ClientWorld.class))
      user.put((StoredObject)new ClientWorld(user)); 
    if (!user.has(EntityTracker.class))
      user.put((StoredObject)new EntityTracker(user)); 
    ((EntityTracker)user.get(EntityTracker.class)).initProtocol(this);
    if (!user.has(BackwardsBlockStorage.class))
      user.put((StoredObject)new BackwardsBlockStorage(user)); 
    if (!user.has(TabCompleteStorage.class))
      user.put((StoredObject)new TabCompleteStorage(user)); 
    if (ViaBackwards.getConfig().isFix1_13FacePlayer() && !user.has(PlayerPositionStorage1_13.class))
      user.put((StoredObject)new PlayerPositionStorage1_13(user)); 
  }
  
  public BlockItemPackets1_13 getBlockItemPackets() {
    return this.blockItemPackets;
  }
  
  public BackwardsMappings getMappingData() {
    return MAPPINGS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\Protocol1_12_2To1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */