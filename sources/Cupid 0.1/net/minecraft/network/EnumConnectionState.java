package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import org.apache.logging.log4j.LogManager;

public enum EnumConnectionState {
  HANDSHAKING(-1) {
    EnumConnectionState(int protocolId) {
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C00Handshake.class);
    }
  },
  PLAY(0) {
    EnumConnectionState(int protocolId) {
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S00PacketKeepAlive.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S01PacketJoinGame.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S02PacketChat.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S03PacketTimeUpdate.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S04PacketEntityEquipment.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S05PacketSpawnPosition.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S06PacketUpdateHealth.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S07PacketRespawn.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S08PacketPlayerPosLook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S09PacketHeldItemChange.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S0APacketUseBed.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S0BPacketAnimation.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S0CPacketSpawnPlayer.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S0DPacketCollectItem.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S0EPacketSpawnObject.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S0FPacketSpawnMob.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S10PacketSpawnPainting.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S11PacketSpawnExperienceOrb.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S12PacketEntityVelocity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S13PacketDestroyEntities.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S14PacketEntity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S14PacketEntity.S15PacketEntityRelMove.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S14PacketEntity.S16PacketEntityLook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S14PacketEntity.S17PacketEntityLookMove.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S18PacketEntityTeleport.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S19PacketEntityHeadLook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S19PacketEntityStatus.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S1BPacketEntityAttach.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S1CPacketEntityMetadata.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S1DPacketEntityEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S1EPacketRemoveEntityEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S1FPacketSetExperience.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S20PacketEntityProperties.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S21PacketChunkData.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S22PacketMultiBlockChange.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S23PacketBlockChange.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S24PacketBlockAction.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S25PacketBlockBreakAnim.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S26PacketMapChunkBulk.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S27PacketExplosion.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S28PacketEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S29PacketSoundEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S2APacketParticles.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S2BPacketChangeGameState.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S2CPacketSpawnGlobalEntity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S2DPacketOpenWindow.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S2EPacketCloseWindow.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S2FPacketSetSlot.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S30PacketWindowItems.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S31PacketWindowProperty.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S32PacketConfirmTransaction.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S33PacketUpdateSign.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S34PacketMaps.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S35PacketUpdateTileEntity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S36PacketSignEditorOpen.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S37PacketStatistics.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S38PacketPlayerListItem.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S39PacketPlayerAbilities.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S3APacketTabComplete.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S3BPacketScoreboardObjective.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S3CPacketUpdateScore.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S3DPacketDisplayScoreboard.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S3EPacketTeams.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S3FPacketCustomPayload.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S40PacketDisconnect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S41PacketServerDifficulty.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S42PacketCombatEvent.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S43PacketCamera.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S44PacketWorldBorder.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S45PacketTitle.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S46PacketSetCompressionLevel.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S47PacketPlayerListHeaderFooter.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S48PacketResourcePackSend.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S49PacketUpdateEntityNBT.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C00PacketKeepAlive.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C01PacketChatMessage.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C02PacketUseEntity.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C03PacketPlayer.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C03PacketPlayer.C04PacketPlayerPosition.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C03PacketPlayer.C05PacketPlayerLook.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C03PacketPlayer.C06PacketPlayerPosLook.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C07PacketPlayerDigging.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C08PacketPlayerBlockPlacement.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C09PacketHeldItemChange.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C0APacketAnimation.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C0BPacketEntityAction.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C0CPacketInput.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C0DPacketCloseWindow.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C0EPacketClickWindow.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C0FPacketConfirmTransaction.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C10PacketCreativeInventoryAction.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C11PacketEnchantItem.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C12PacketUpdateSign.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C13PacketPlayerAbilities.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C14PacketTabComplete.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C15PacketClientSettings.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C16PacketClientStatus.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C17PacketCustomPayload.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C18PacketSpectate.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C19PacketResourcePackStatus.class);
    }
  },
  STATUS(1) {
    EnumConnectionState(int protocolId) {
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C00PacketServerQuery.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S00PacketServerInfo.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C01PacketPing.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S01PacketPong.class);
    }
  },
  LOGIN(2) {
    EnumConnectionState(int protocolId) {
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S00PacketDisconnect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S01PacketEncryptionRequest.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S02PacketLoginSuccess.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)S03PacketEnableCompression.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C00PacketLoginStart.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C01PacketEncryptionResponse.class);
    }
  };
  
  private static int field_181136_e;
  
  private static int field_181137_f;
  
  private static final EnumConnectionState[] STATES_BY_ID;
  
  private static final Map<Class<? extends Packet>, EnumConnectionState> STATES_BY_CLASS;
  
  private final int id;
  
  private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>> directionMaps;
  
  static {
    field_181136_e = -1;
    field_181137_f = 2;
    STATES_BY_ID = new EnumConnectionState[field_181137_f - field_181136_e + 1];
    STATES_BY_CLASS = Maps.newHashMap();
    for (EnumConnectionState enumconnectionstate : values()) {
      int i = enumconnectionstate.getId();
      if (i < field_181136_e || i > field_181137_f)
        throw new Error("Invalid protocol ID " + Integer.toString(i)); 
      STATES_BY_ID[i - field_181136_e] = enumconnectionstate;
      for (EnumPacketDirection enumpacketdirection : enumconnectionstate.directionMaps.keySet()) {
        for (Class<? extends Packet> oclass : (Iterable<Class<? extends Packet>>)((BiMap)enumconnectionstate.directionMaps.get(enumpacketdirection)).values()) {
          if (STATES_BY_CLASS.containsKey(oclass) && STATES_BY_CLASS.get(oclass) != enumconnectionstate)
            throw new Error("Packet " + oclass + " is already assigned to protocol " + STATES_BY_CLASS.get(oclass) + " - can't reassign to " + enumconnectionstate); 
          try {
            oclass.newInstance();
          } catch (Throwable var10) {
            throw new Error("Packet " + oclass + " fails instantiation checks! " + oclass);
          } 
          STATES_BY_CLASS.put(oclass, enumconnectionstate);
        } 
      } 
    } 
  }
  
  EnumConnectionState(int protocolId) {
    this.directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
    this.id = protocolId;
  }
  
  protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet> packetClass) {
    HashBiMap hashBiMap;
    BiMap<Integer, Class<? extends Packet>> bimap = this.directionMaps.get(direction);
    if (bimap == null) {
      hashBiMap = HashBiMap.create();
      this.directionMaps.put(direction, hashBiMap);
    } 
    if (hashBiMap.containsValue(packetClass)) {
      String s = direction + " packet " + packetClass + " is already known to ID " + hashBiMap.inverse().get(packetClass);
      LogManager.getLogger().fatal(s);
      throw new IllegalArgumentException(s);
    } 
    hashBiMap.put(Integer.valueOf(hashBiMap.size()), packetClass);
    return this;
  }
  
  public Integer getPacketId(EnumPacketDirection direction, Packet packetIn) {
    return (Integer)((BiMap)this.directionMaps.get(direction)).inverse().get(packetIn.getClass());
  }
  
  public Packet getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
    Class<? extends Packet> oclass = (Class<? extends Packet>)((BiMap)this.directionMaps.get(direction)).get(Integer.valueOf(packetId));
    return (oclass == null) ? null : oclass.newInstance();
  }
  
  public int getId() {
    return this.id;
  }
  
  public static EnumConnectionState getById(int stateId) {
    return (stateId >= field_181136_e && stateId <= field_181137_f) ? STATES_BY_ID[stateId - field_181136_e] : null;
  }
  
  public static EnumConnectionState getFromPacket(Packet packetIn) {
    return STATES_BY_CLASS.get(packetIn.getClass());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\EnumConnectionState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */