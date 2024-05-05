package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
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
import net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
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
import org.apache.logging.log4j.Logger;

public enum EnumConnectionState
{
  private static final TIntObjectMap STATES_BY_ID;
  private static final Map STATES_BY_CLASS;
  private final int id;
  private final Map directionMaps;
  private static final EnumConnectionState[] $VALUES;
  private static final String __OBFID = "CL_00001245";
  
  private EnumConnectionState(String p_i45152_1_, int p_i45152_2_, int protocolId)
  {
    this.directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
    this.id = protocolId;
  }
  
  protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class packetClass)
  {
    Object var3 = this.directionMaps.get(direction);
    if (var3 == null)
    {
      var3 = HashBiMap.create();
      this.directionMaps.put(direction, var3);
    }
    if (((BiMap)var3).containsValue(packetClass))
    {
      String var4 = direction + " packet " + packetClass + " is already known to ID " + ((BiMap)var3).inverse().get(packetClass);
      LogManager.getLogger().fatal(var4);
      throw new IllegalArgumentException(var4);
    }
    ((BiMap)var3).put(Integer.valueOf(((BiMap)var3).size()), packetClass);
    return this;
  }
  
  public Integer getPacketId(EnumPacketDirection direction, Packet<INetHandler> packetIn)
  {
    return (Integer)((BiMap)this.directionMaps.get(direction)).inverse().get(packetIn.getClass());
  }
  
  public Packet<INetHandler> getPacket(EnumPacketDirection direction, int packetId)
    throws InstantiationException, IllegalAccessException
  {
    Class var3 = (Class)((BiMap)this.directionMaps.get(direction)).get(Integer.valueOf(packetId));
    return var3 == null ? null : (Packet)var3.newInstance();
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public static EnumConnectionState getById(int stateId)
  {
    return (EnumConnectionState)STATES_BY_ID.get(stateId);
  }
  
  public static EnumConnectionState getFromPacket(Packet<INetHandler> packetIn)
  {
    return (EnumConnectionState)STATES_BY_CLASS.get(packetIn.getClass());
  }
  
  private EnumConnectionState(String p_i46000_1_, int p_i46000_2_, int p_i46000_3_, Object p_i46000_4_)
  {
    this(p_i46000_1_, p_i46000_2_, p_i46000_3_);
  }
  
  static
  {
    STATES_BY_ID = new TIntObjectHashMap();
    STATES_BY_CLASS = Maps.newHashMap();
    
    $VALUES = new EnumConnectionState[] { HANDSHAKING, PLAY, STATUS, LOGIN };
    
    EnumConnectionState[] var0 = values();
    int var1 = var0.length;
    for (EnumConnectionState var3 : var0)
    {
      STATES_BY_ID.put(var3.getId(), var3);
      Iterator var4 = var3.directionMaps.keySet().iterator();
      while (var4.hasNext())
      {
        EnumPacketDirection var5 = (EnumPacketDirection)var4.next();
        Class var7;
        for (Iterator var6 = ((BiMap)var3.directionMaps.get(var5)).values().iterator(); var6.hasNext(); STATES_BY_CLASS.put(var7, var3))
        {
          var7 = (Class)var6.next();
          if ((STATES_BY_CLASS.containsKey(var7)) && (STATES_BY_CLASS.get(var7) != var3)) {
            throw new Error("Packet " + var7 + " is already assigned to protocol " + STATES_BY_CLASS.get(var7) + " - can't reassign to " + var3);
          }
          try
          {
            var7.newInstance();
          }
          catch (Throwable var9)
          {
            throw new Error("Packet " + var7 + " fails instantiation checks! " + var7);
          }
        }
      }
    }
  }
}
