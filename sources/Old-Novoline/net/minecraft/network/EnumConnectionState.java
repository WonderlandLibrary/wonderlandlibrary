package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public enum EnumConnectionState {
    HANDSHAKING(-1) {
        {
            registerPacket(EnumPacketDirection.SERVERBOUND, C00Handshake.class);
        }
    },
    PLAY(0) {
        {
            registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketKeepAlive.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketJoinGame.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketChat.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketTimeUpdate.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S04PacketEntityEquipment.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S05PacketSpawnPosition.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S06PacketUpdateHealth.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S07PacketRespawn.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S08PacketPlayerPosLook.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S09PacketHeldItemChange.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0APacketUseBed.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0BPacketAnimation.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0CPacketSpawnPlayer.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0DPacketCollectItem.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0EPacketSpawnObject.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S0FPacketSpawnMob.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S10PacketSpawnPainting.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S11PacketSpawnExperienceOrb.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S12PacketEntityVelocity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S13PacketDestroyEntities.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S15PacketEntityRelMove.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S16PacketEntityLook.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S17PacketEntityLookMove.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S18PacketEntityTeleport.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityHeadLook.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityStatus.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1BPacketEntityAttach.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1CPacketEntityMetadata.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1DPacketEntityEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1EPacketRemoveEntityEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S1FPacketSetExperience.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S20PacketEntityProperties.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S21PacketChunkData.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S22PacketMultiBlockChange.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S23PacketBlockChange.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S24PacketBlockAction.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S25PacketBlockBreakAnim.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S26PacketMapChunkBulk.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S27PacketExplosion.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S28PacketEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S29PacketSoundEffect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2APacketParticles.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2BPacketChangeGameState.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2CPacketSpawnGlobalEntity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2DPacketOpenWindow.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2EPacketCloseWindow.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S2FPacketSetSlot.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S30PacketWindowItems.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S31PacketWindowProperty.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S32PacketConfirmTransaction.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S33PacketUpdateSign.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S34PacketMaps.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S35PacketUpdateTileEntity.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S36PacketSignEditorOpen.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S37PacketStatistics.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S38PacketPlayerListItem.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S39PacketPlayerAbilities.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3APacketTabComplete.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3BPacketScoreboardObjective.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3CPacketUpdateScore.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3DPacketDisplayScoreboard.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3EPacketTeams.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S3FPacketCustomPayload.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S40PacketDisconnect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S41PacketServerDifficulty.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S42PacketCombatEvent.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S43PacketCamera.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S44PacketWorldBorder.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S45PacketTitle.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S46PacketSetCompressionLevel.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S47PacketPlayerListHeaderFooter.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S48PacketResourcePackSend.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S49PacketUpdateEntityNBT.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, SPacketMoveVehicle.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketKeepAlive.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketChatMessage.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C02PacketUseEntity.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C04PacketPlayerPosition.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C05PacketPlayerLook.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C06PacketPlayerPosLook.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C07PacketPlayerDigging.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C08PacketPlayerBlockPlacement.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C09PacketHeldItemChange.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0APacketAnimation.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0BPacketEntityAction.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0CPacketInput.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0DPacketCloseWindow.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0EPacketClickWindow.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C0FPacketConfirmTransaction.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C10PacketCreativeInventoryAction.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C11PacketEnchantItem.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C12PacketUpdateSign.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C13PacketPlayerAbilities.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C14PacketTabComplete.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C15PacketClientSettings.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C16PacketClientStatus.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C17PacketCustomPayload.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C18PacketSpectate.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C19PacketResourcePackStatus.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, CPacketVehicleMove.class);
        }
    },
    STATUS(1) {
        {
            registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketServerQuery.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketServerInfo.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketPing.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketPong.class);
        }
    },
    LOGIN(2) {
        {
            registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketDisconnect.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketEncryptionRequest.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketLoginSuccess.class);
            registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketEnableCompression.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketLoginStart.class);
            registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketEncryptionResponse.class);
        }
    };

    private static final EnumConnectionState[] STATES_BY_ID = new EnumConnectionState[4];
    private static final Map<Class<? extends Packet<?>>, EnumConnectionState> STATES_BY_CLASS = Maps.newHashMap();
    private final int id;
    private Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet<?>>>> directionMaps = Maps.newEnumMap(EnumPacketDirection.class);

    EnumConnectionState(int protocolId) {
        this.id = protocolId;
    }

    public void registerPacket(EnumPacketDirection direction, Class<? extends Packet<?>> packetClass) {
        BiMap<Integer, Class<? extends Packet<?>>> bimap = directionMaps.computeIfAbsent(direction, k -> HashBiMap.create());

        if(bimap.containsValue(packetClass)) {
            String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
            LogManager.getLogger().fatal(s);
            throw new IllegalArgumentException(s);
        } else {
            bimap.put(bimap.size(), packetClass);
        }
    }

    public Integer getPacketId(EnumPacketDirection direction, Packet packetIn) {
        return (Integer) ((BiMap) directionMaps.get(direction)).inverse().get(packetIn.getClass());
    }

    public Packet getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
        Class<? extends Packet> oclass = (Class) ((BiMap) directionMaps.get(direction)).get(packetId);
        return oclass == null ? null : oclass.newInstance();
    }

    public int getId() {
        return id;
    }

    public static EnumConnectionState getById(int stateId) {
        return stateId >= -1 && stateId <= 2 ? STATES_BY_ID[stateId - -1] : null;
    }


    public static EnumConnectionState getFromPacket(Packet packetIn) {
        return STATES_BY_CLASS.get(packetIn.getClass());
    }

    static {
        for(EnumConnectionState enumconnectionstate : values()) {
            int i = enumconnectionstate.getId();
            if(i < -1 || i > 2) {
                throw new Error("Invalid protocol ID " + i);
            }

            STATES_BY_ID[i - -1] = enumconnectionstate;

            for(EnumPacketDirection enumpacketdirection : enumconnectionstate.directionMaps.keySet()) {
                for(Class<? extends Packet<?>> oclass : enumconnectionstate.directionMaps.get(enumpacketdirection).values()) {
                    if(STATES_BY_CLASS.containsKey(oclass) && STATES_BY_CLASS.get(oclass) != enumconnectionstate) {
                        throw new Error("Packet " + oclass + " is already assigned to protocol " + STATES_BY_CLASS
                                .get(oclass) + " - can't reassign to " + enumconnectionstate);
                    }

                    try {
                        oclass.newInstance();
                    } catch(Throwable var10) {
                        throw new Error("Packet " + oclass + " fails instantiation checks! " + oclass);
                    }

                    STATES_BY_CLASS.put(oclass, enumconnectionstate);
                }
            }
        }
    }
}
