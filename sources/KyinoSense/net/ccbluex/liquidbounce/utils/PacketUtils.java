/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.server.S00PacketKeepAlive
 *  net.minecraft.network.play.server.S01PacketJoinGame
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S03PacketTimeUpdate
 *  net.minecraft.network.play.server.S04PacketEntityEquipment
 *  net.minecraft.network.play.server.S05PacketSpawnPosition
 *  net.minecraft.network.play.server.S06PacketUpdateHealth
 *  net.minecraft.network.play.server.S07PacketRespawn
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.network.play.server.S09PacketHeldItemChange
 *  net.minecraft.network.play.server.S0APacketUseBed
 *  net.minecraft.network.play.server.S0BPacketAnimation
 *  net.minecraft.network.play.server.S0CPacketSpawnPlayer
 *  net.minecraft.network.play.server.S0DPacketCollectItem
 *  net.minecraft.network.play.server.S0EPacketSpawnObject
 *  net.minecraft.network.play.server.S0FPacketSpawnMob
 *  net.minecraft.network.play.server.S10PacketSpawnPainting
 *  net.minecraft.network.play.server.S11PacketSpawnExperienceOrb
 *  net.minecraft.network.play.server.S12PacketEntityVelocity
 *  net.minecraft.network.play.server.S13PacketDestroyEntities
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S18PacketEntityTeleport
 *  net.minecraft.network.play.server.S19PacketEntityHeadLook
 *  net.minecraft.network.play.server.S19PacketEntityStatus
 *  net.minecraft.network.play.server.S1BPacketEntityAttach
 *  net.minecraft.network.play.server.S1CPacketEntityMetadata
 *  net.minecraft.network.play.server.S1DPacketEntityEffect
 *  net.minecraft.network.play.server.S1EPacketRemoveEntityEffect
 *  net.minecraft.network.play.server.S1FPacketSetExperience
 *  net.minecraft.network.play.server.S20PacketEntityProperties
 *  net.minecraft.network.play.server.S21PacketChunkData
 *  net.minecraft.network.play.server.S22PacketMultiBlockChange
 *  net.minecraft.network.play.server.S23PacketBlockChange
 *  net.minecraft.network.play.server.S24PacketBlockAction
 *  net.minecraft.network.play.server.S25PacketBlockBreakAnim
 *  net.minecraft.network.play.server.S26PacketMapChunkBulk
 *  net.minecraft.network.play.server.S27PacketExplosion
 *  net.minecraft.network.play.server.S28PacketEffect
 *  net.minecraft.network.play.server.S29PacketSoundEffect
 *  net.minecraft.network.play.server.S2APacketParticles
 *  net.minecraft.network.play.server.S2BPacketChangeGameState
 *  net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
 *  net.minecraft.network.play.server.S2DPacketOpenWindow
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  net.minecraft.network.play.server.S30PacketWindowItems
 *  net.minecraft.network.play.server.S31PacketWindowProperty
 *  net.minecraft.network.play.server.S32PacketConfirmTransaction
 *  net.minecraft.network.play.server.S33PacketUpdateSign
 *  net.minecraft.network.play.server.S34PacketMaps
 *  net.minecraft.network.play.server.S35PacketUpdateTileEntity
 *  net.minecraft.network.play.server.S36PacketSignEditorOpen
 *  net.minecraft.network.play.server.S37PacketStatistics
 *  net.minecraft.network.play.server.S38PacketPlayerListItem
 *  net.minecraft.network.play.server.S39PacketPlayerAbilities
 *  net.minecraft.network.play.server.S3APacketTabComplete
 *  net.minecraft.network.play.server.S3BPacketScoreboardObjective
 *  net.minecraft.network.play.server.S3CPacketUpdateScore
 *  net.minecraft.network.play.server.S3DPacketDisplayScoreboard
 *  net.minecraft.network.play.server.S3EPacketTeams
 *  net.minecraft.network.play.server.S3FPacketCustomPayload
 *  net.minecraft.network.play.server.S40PacketDisconnect
 *  net.minecraft.network.play.server.S41PacketServerDifficulty
 *  net.minecraft.network.play.server.S42PacketCombatEvent
 *  net.minecraft.network.play.server.S43PacketCamera
 *  net.minecraft.network.play.server.S44PacketWorldBorder
 *  net.minecraft.network.play.server.S45PacketTitle
 *  net.minecraft.network.play.server.S46PacketSetCompressionLevel
 *  net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter
 *  net.minecraft.network.play.server.S48PacketResourcePackSend
 *  net.minecraft.network.play.server.S49PacketUpdateEntityNBT
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001)B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u001c\u001a\u00020\u001d2\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0007J\u0018\u0010\u001f\u001a\u00020 2\u000e\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010!0\u0016H\u0007J\u0014\u0010\"\u001a\u00020#2\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0007J\b\u0010$\u001a\u00020#H\u0007J\u0012\u0010%\u001a\u00020 2\b\u0010&\u001a\u0004\u0018\u00010'H\u0007J\u0014\u0010(\u001a\u00020 2\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0007R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\u0014\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00160\u0015j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0016`\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2={"Lnet/ccbluex/liquidbounce/utils/PacketUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "avgInBound", "", "getAvgInBound", "()I", "setAvgInBound", "(I)V", "avgOutBound", "getAvgOutBound", "setAvgOutBound", "inBound", "getInBound", "setInBound", "outBound", "getOutBound", "setOutBound", "packetTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "packets", "Ljava/util/ArrayList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "Lkotlin/collections/ArrayList;", "transCount", "wdTimer", "wdVL", "getPacketType", "Lnet/ccbluex/liquidbounce/utils/PacketUtils$PacketType;", "packet", "handlePacket", "", "Lnet/minecraft/network/play/INetHandlerPlayClient;", "handleSendPacket", "", "isWatchdogActive", "onTick", "event", "Lnet/ccbluex/liquidbounce/event/TickEvent;", "sendPacketNoEvent", "PacketType", "KyinoClient"})
public final class PacketUtils
extends MinecraftInstance {
    private static final ArrayList<Packet<INetHandlerPlayServer>> packets;
    private static int outBound;
    private static int inBound;
    private static int avgInBound;
    private static int avgOutBound;
    private static final MSTimer packetTimer;
    private static final MSTimer wdTimer;
    private static int transCount;
    private static int wdVL;
    public static final PacketUtils INSTANCE;

    public final int getOutBound() {
        return outBound;
    }

    public final void setOutBound(int n) {
        outBound = n;
    }

    public final int getInBound() {
        return inBound;
    }

    public final void setInBound(int n) {
        inBound = n;
    }

    public final int getAvgInBound() {
        return avgInBound;
    }

    public final void setAvgInBound(int n) {
        avgInBound = n;
    }

    public final int getAvgOutBound() {
        return avgOutBound;
    }

    public final void setAvgOutBound(int n) {
        avgOutBound = n;
    }

    @JvmStatic
    public static final boolean handleSendPacket(@NotNull Packet<?> packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        if (CollectionsKt.contains((Iterable)packets, packet)) {
            Collection collection = packets;
            boolean bl = false;
            Collection collection2 = collection;
            if (collection2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
            }
            TypeIntrinsics.asMutableCollection(collection2).remove(packet);
            return true;
        }
        return false;
    }

    @JvmStatic
    public static final boolean isWatchdogActive() {
        return wdVL >= 8;
    }

    @JvmStatic
    public static final void sendPacketNoEvent(@NotNull Packet<?> packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        packets.add(packet);
        Minecraft minecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a(packet);
    }

    @JvmStatic
    public static final void handlePacket(@NotNull Packet<INetHandlerPlayClient> packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        Minecraft minecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        NetHandlerPlayClient netHandler = minecraft.func_147114_u();
        if (packet instanceof S00PacketKeepAlive) {
            netHandler.func_147272_a((S00PacketKeepAlive)packet);
        } else if (packet instanceof S01PacketJoinGame) {
            netHandler.func_147282_a((S01PacketJoinGame)packet);
        } else if (packet instanceof S02PacketChat) {
            netHandler.func_147251_a((S02PacketChat)packet);
        } else if (packet instanceof S03PacketTimeUpdate) {
            netHandler.func_147285_a((S03PacketTimeUpdate)packet);
        } else if (packet instanceof S04PacketEntityEquipment) {
            netHandler.func_147242_a((S04PacketEntityEquipment)packet);
        } else if (packet instanceof S05PacketSpawnPosition) {
            netHandler.func_147271_a((S05PacketSpawnPosition)packet);
        } else if (packet instanceof S06PacketUpdateHealth) {
            netHandler.func_147249_a((S06PacketUpdateHealth)packet);
        } else if (packet instanceof S07PacketRespawn) {
            netHandler.func_147280_a((S07PacketRespawn)packet);
        } else if (packet instanceof S08PacketPlayerPosLook) {
            netHandler.func_147258_a((S08PacketPlayerPosLook)packet);
        } else if (packet instanceof S09PacketHeldItemChange) {
            netHandler.func_147257_a((S09PacketHeldItemChange)packet);
        } else if (packet instanceof S10PacketSpawnPainting) {
            netHandler.func_147288_a((S10PacketSpawnPainting)packet);
        } else if (packet instanceof S0APacketUseBed) {
            netHandler.func_147278_a((S0APacketUseBed)packet);
        } else if (packet instanceof S0BPacketAnimation) {
            netHandler.func_147279_a((S0BPacketAnimation)packet);
        } else if (packet instanceof S0CPacketSpawnPlayer) {
            netHandler.func_147237_a((S0CPacketSpawnPlayer)packet);
        } else if (packet instanceof S0DPacketCollectItem) {
            netHandler.func_147246_a((S0DPacketCollectItem)packet);
        } else if (packet instanceof S0EPacketSpawnObject) {
            netHandler.func_147235_a((S0EPacketSpawnObject)packet);
        } else if (packet instanceof S0FPacketSpawnMob) {
            netHandler.func_147281_a((S0FPacketSpawnMob)packet);
        } else if (packet instanceof S11PacketSpawnExperienceOrb) {
            netHandler.func_147286_a((S11PacketSpawnExperienceOrb)packet);
        } else if (packet instanceof S12PacketEntityVelocity) {
            netHandler.func_147244_a((S12PacketEntityVelocity)packet);
        } else if (packet instanceof S13PacketDestroyEntities) {
            netHandler.func_147238_a((S13PacketDestroyEntities)packet);
        } else if (packet instanceof S14PacketEntity) {
            netHandler.func_147259_a((S14PacketEntity)packet);
        } else if (packet instanceof S18PacketEntityTeleport) {
            netHandler.func_147275_a((S18PacketEntityTeleport)packet);
        } else if (packet instanceof S19PacketEntityStatus) {
            netHandler.func_147236_a((S19PacketEntityStatus)packet);
        } else if (packet instanceof S19PacketEntityHeadLook) {
            netHandler.func_147267_a((S19PacketEntityHeadLook)packet);
        } else if (packet instanceof S1BPacketEntityAttach) {
            netHandler.func_147243_a((S1BPacketEntityAttach)packet);
        } else if (packet instanceof S1CPacketEntityMetadata) {
            netHandler.func_147284_a((S1CPacketEntityMetadata)packet);
        } else if (packet instanceof S1DPacketEntityEffect) {
            netHandler.func_147260_a((S1DPacketEntityEffect)packet);
        } else if (packet instanceof S1EPacketRemoveEntityEffect) {
            netHandler.func_147262_a((S1EPacketRemoveEntityEffect)packet);
        } else if (packet instanceof S1FPacketSetExperience) {
            netHandler.func_147295_a((S1FPacketSetExperience)packet);
        } else if (packet instanceof S20PacketEntityProperties) {
            netHandler.func_147290_a((S20PacketEntityProperties)packet);
        } else if (packet instanceof S21PacketChunkData) {
            netHandler.func_147263_a((S21PacketChunkData)packet);
        } else if (packet instanceof S22PacketMultiBlockChange) {
            netHandler.func_147287_a((S22PacketMultiBlockChange)packet);
        } else if (packet instanceof S23PacketBlockChange) {
            netHandler.func_147234_a((S23PacketBlockChange)packet);
        } else if (packet instanceof S24PacketBlockAction) {
            netHandler.func_147261_a((S24PacketBlockAction)packet);
        } else if (packet instanceof S25PacketBlockBreakAnim) {
            netHandler.func_147294_a((S25PacketBlockBreakAnim)packet);
        } else if (packet instanceof S26PacketMapChunkBulk) {
            netHandler.func_147269_a((S26PacketMapChunkBulk)packet);
        } else if (packet instanceof S27PacketExplosion) {
            netHandler.func_147283_a((S27PacketExplosion)packet);
        } else if (packet instanceof S28PacketEffect) {
            netHandler.func_147277_a((S28PacketEffect)packet);
        } else if (packet instanceof S29PacketSoundEffect) {
            netHandler.func_147255_a((S29PacketSoundEffect)packet);
        } else if (packet instanceof S2APacketParticles) {
            netHandler.func_147289_a((S2APacketParticles)packet);
        } else if (packet instanceof S2BPacketChangeGameState) {
            netHandler.func_147252_a((S2BPacketChangeGameState)packet);
        } else if (packet instanceof S2CPacketSpawnGlobalEntity) {
            netHandler.func_147292_a((S2CPacketSpawnGlobalEntity)packet);
        } else if (packet instanceof S2DPacketOpenWindow) {
            netHandler.func_147265_a((S2DPacketOpenWindow)packet);
        } else if (packet instanceof S2EPacketCloseWindow) {
            netHandler.func_147276_a((S2EPacketCloseWindow)packet);
        } else if (packet instanceof S2FPacketSetSlot) {
            netHandler.func_147266_a((S2FPacketSetSlot)packet);
        } else if (packet instanceof S30PacketWindowItems) {
            netHandler.func_147241_a((S30PacketWindowItems)packet);
        } else if (packet instanceof S31PacketWindowProperty) {
            netHandler.func_147245_a((S31PacketWindowProperty)packet);
        } else if (packet instanceof S32PacketConfirmTransaction) {
            netHandler.func_147239_a((S32PacketConfirmTransaction)packet);
        } else if (packet instanceof S33PacketUpdateSign) {
            netHandler.func_147248_a((S33PacketUpdateSign)packet);
        } else if (packet instanceof S34PacketMaps) {
            netHandler.func_147264_a((S34PacketMaps)packet);
        } else if (packet instanceof S35PacketUpdateTileEntity) {
            netHandler.func_147273_a((S35PacketUpdateTileEntity)packet);
        } else if (packet instanceof S36PacketSignEditorOpen) {
            netHandler.func_147268_a((S36PacketSignEditorOpen)packet);
        } else if (packet instanceof S37PacketStatistics) {
            netHandler.func_147293_a((S37PacketStatistics)packet);
        } else if (packet instanceof S38PacketPlayerListItem) {
            netHandler.func_147256_a((S38PacketPlayerListItem)packet);
        } else if (packet instanceof S39PacketPlayerAbilities) {
            netHandler.func_147270_a((S39PacketPlayerAbilities)packet);
        } else if (packet instanceof S3APacketTabComplete) {
            netHandler.func_147274_a((S3APacketTabComplete)packet);
        } else if (packet instanceof S3BPacketScoreboardObjective) {
            netHandler.func_147291_a((S3BPacketScoreboardObjective)packet);
        } else if (packet instanceof S3CPacketUpdateScore) {
            netHandler.func_147250_a((S3CPacketUpdateScore)packet);
        } else if (packet instanceof S3DPacketDisplayScoreboard) {
            netHandler.func_147254_a((S3DPacketDisplayScoreboard)packet);
        } else if (packet instanceof S3EPacketTeams) {
            netHandler.func_147247_a((S3EPacketTeams)packet);
        } else if (packet instanceof S3FPacketCustomPayload) {
            netHandler.func_147240_a((S3FPacketCustomPayload)packet);
        } else if (packet instanceof S40PacketDisconnect) {
            netHandler.func_147253_a((S40PacketDisconnect)packet);
        } else if (packet instanceof S41PacketServerDifficulty) {
            netHandler.func_175101_a((S41PacketServerDifficulty)packet);
        } else if (packet instanceof S42PacketCombatEvent) {
            netHandler.func_175098_a((S42PacketCombatEvent)packet);
        } else if (packet instanceof S43PacketCamera) {
            netHandler.func_175094_a((S43PacketCamera)packet);
        } else if (packet instanceof S44PacketWorldBorder) {
            netHandler.func_175093_a((S44PacketWorldBorder)packet);
        } else if (packet instanceof S45PacketTitle) {
            netHandler.func_175099_a((S45PacketTitle)packet);
        } else if (packet instanceof S46PacketSetCompressionLevel) {
            netHandler.func_175100_a((S46PacketSetCompressionLevel)packet);
        } else if (packet instanceof S47PacketPlayerListHeaderFooter) {
            netHandler.func_175096_a((S47PacketPlayerListHeaderFooter)packet);
        } else if (packet instanceof S48PacketResourcePackSend) {
            netHandler.func_175095_a((S48PacketResourcePackSend)packet);
        } else if (packet instanceof S49PacketUpdateEntityNBT) {
            netHandler.func_175097_a((S49PacketUpdateEntityNBT)packet);
        } else {
            throw (Throwable)new IllegalArgumentException("Unable to match packet type to handle: " + packet.getClass());
        }
    }

    @EventTarget
    public final void onTick(@Nullable TickEvent event) {
        if (packetTimer.hasTimePassed(1000L)) {
            avgInBound = inBound;
            avgOutBound = outBound;
            inBound = outBound = 0;
            packetTimer.reset();
        }
        if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71441_e == null) {
            wdVL = 0;
            transCount = 0;
            wdTimer.reset();
        } else if (wdTimer.hasTimePassed(100L)) {
            int n = transCount > 0 ? 1 : -1;
            transCount = 0;
            if ((wdVL += n) > 10) {
                wdVL = 10;
            }
            if (wdVL < 0) {
                wdVL = 0;
            }
            wdTimer.reset();
        }
    }

    @JvmStatic
    @NotNull
    public static final PacketType getPacketType(@NotNull Packet<?> packet) {
        String className;
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        String string = className = packet.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(string, "className");
        if (StringsKt.startsWith(string, "C", true)) {
            return PacketType.CLIENTSIDE;
        }
        if (StringsKt.startsWith(className, "S", true)) {
            return PacketType.SERVERSIDE;
        }
        return PacketType.UNKNOWN;
    }

    private PacketUtils() {
    }

    static {
        PacketUtils packetUtils;
        INSTANCE = packetUtils = new PacketUtils();
        packets = new ArrayList();
        packetTimer = new MSTimer();
        wdTimer = new MSTimer();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/utils/PacketUtils$PacketType;", "", "(Ljava/lang/String;I)V", "SERVERSIDE", "CLIENTSIDE", "UNKNOWN", "KyinoClient"})
    public static final class PacketType
    extends Enum<PacketType> {
        public static final /* enum */ PacketType SERVERSIDE;
        public static final /* enum */ PacketType CLIENTSIDE;
        public static final /* enum */ PacketType UNKNOWN;
        private static final /* synthetic */ PacketType[] $VALUES;

        static {
            PacketType[] packetTypeArray = new PacketType[3];
            PacketType[] packetTypeArray2 = packetTypeArray;
            packetTypeArray[0] = SERVERSIDE = new PacketType();
            packetTypeArray[1] = CLIENTSIDE = new PacketType();
            packetTypeArray[2] = UNKNOWN = new PacketType();
            $VALUES = packetTypeArray;
        }

        public static PacketType[] values() {
            return (PacketType[])$VALUES.clone();
        }

        public static PacketType valueOf(String string) {
            return Enum.valueOf(PacketType.class, string);
        }
    }
}

