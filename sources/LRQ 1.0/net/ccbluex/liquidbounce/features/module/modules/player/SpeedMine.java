/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.init.Blocks
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="SpeedMine", description="1145", category=ModuleCategory.PLAYER)
public final class SpeedMine
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Hypixel", "Packet", "NewPacket", "NewPacket2", "MoonX", "Tenacity"}, "NewPacket");
    private final FloatValue breakSpeedValue = new FloatValue("BreakSpeed", 1.2f, 1.0f, 1.5f);
    private final FloatValue tenacitySpeedValue = new FloatValue("Tenacity-Speed", 1.5f, 1.0f, 3.0f);
    private boolean bzs;
    private float bzx;
    private WBlockPos blockPos;
    private IEnumFacing facing;
    private WBlockPos pos;
    private boolean boost;
    private float damage;

    public final WBlockPos getBlockPos() {
        return this.blockPos;
    }

    public final void setBlockPos(@Nullable WBlockPos wBlockPos) {
        this.blockPos = wBlockPos;
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.removePotionEffectClient(MinecraftInstance.classProvider.getPotionEnum(PotionType.DIG_SLOWDOWN).getId());
    }

    @EventTarget
    public final void onMotion(MotionEvent event) {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"Tenacity", (boolean)true) && event.getEventState() == EventState.PRE) {
            MinecraftInstance.mc.getPlayerController().setBlockHitDelay(0);
            if (this.pos != null && this.boost) {
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                WBlockPos wBlockPos = this.pos;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                IIBlockState iIBlockState = iWorldClient.getBlockState(wBlockPos);
                if (iIBlockState == null) {
                    return;
                }
                IIBlockState blockState = iIBlockState;
                try {
                    IBlock iBlock = blockState.getBlock();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient2 == null) {
                        Intrinsics.throwNpe();
                    }
                    IWorld iWorld = iWorldClient2;
                    WBlockPos wBlockPos2 = this.pos;
                    if (wBlockPos2 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.damage += iBlock.getPlayerRelativeBlockHardness(iEntityPlayerSP, iWorld, wBlockPos2) * ((Number)this.tenacitySpeedValue.get()).floatValue();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                if (this.damage >= 1.0f) {
                    try {
                        IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient3 == null) {
                            Intrinsics.throwNpe();
                        }
                        iWorldClient3.setBlockState(this.pos, Blocks.field_150350_a.func_176223_P(), 11);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    WBlockPos wBlockPos3 = this.pos;
                    if (wBlockPos3 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEnumFacing iEnumFacing = this.facing;
                    if (iEnumFacing == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos3, iEnumFacing));
                    this.damage = 0.0f;
                    this.boost = false;
                }
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        ICPacketPlayerDigging packet;
        if (((String)this.modeValue.get()).equals("Hypixel") && MinecraftInstance.classProvider.isCPacketPlayerDigging(event.getPacket()) && !MinecraftInstance.mc.getPlayerController().extendedReach() && MinecraftInstance.mc.getPlayerController() != null) {
            packet = event.getPacket().asCPacketPlayerDigging();
            if (packet.getAction() == ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK) {
                this.bzs = true;
                this.blockPos = packet.getPosition();
                this.facing = packet.getFacing();
                this.bzx = 0.0f;
            } else if (packet.getAction() == ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK || packet.getAction() == ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK) {
                this.bzs = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"Tenacity", (boolean)true) && MinecraftInstance.classProvider.isCPacketPlayerDigging(event.getPacket())) {
            packet = event.getPacket().asCPacketPlayerDigging();
            if (packet.getAction() == ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK) {
                this.boost = true;
                this.pos = packet.getPosition();
                this.facing = packet.getFacing();
                this.damage = 0.0f;
            } else if (packet.getAction() == ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK || packet.getAction() == ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK) {
                this.boost = false;
                this.pos = null;
                this.facing = null;
            }
        }
    }

    @EventTarget
    private final void onUpdate(UpdateEvent e) {
        switch ((String)this.modeValue.get()) {
            case "Packet": {
                float f = MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP();
                if (f >= 0.1f && f <= 0.19f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if ((f = MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP()) >= 0.4f && f <= 0.49f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if (!((f = MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP()) >= 0.8f) || !(f <= 0.89f)) break;
                IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.9f);
                break;
            }
            case "NewPacket": {
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.1f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.4f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() != 0.7f) break;
                IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                break;
            }
            case "NewPacket2": {
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.2f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.4f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.6f) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.1f);
                }
                if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() != 0.8f) break;
                IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                iPlayerControllerMP.setCurBlockDamageMP(iPlayerControllerMP.getCurBlockDamageMP() + 0.2f);
                break;
            }
            case "Hypixel": {
                if (MinecraftInstance.mc.getPlayerController().extendedReach()) {
                    MinecraftInstance.mc.getPlayerController().setBlockHitDelay(0);
                    break;
                }
                if (!this.bzs) break;
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                WBlockPos wBlockPos = this.blockPos;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                IBlock block = iWorldClient.getBlockState(wBlockPos).getBlock();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient2 == null) {
                    Intrinsics.throwNpe();
                }
                IWorld iWorld = iWorldClient2;
                WBlockPos wBlockPos2 = this.blockPos;
                if (wBlockPos2 == null) {
                    Intrinsics.throwNpe();
                }
                this.bzx += (float)((double)block.getPlayerRelativeBlockHardness(iEntityPlayerSP, iWorld, wBlockPos2) * ((Number)this.breakSpeedValue.get()).doubleValue());
                if (!(this.bzx >= 1.0f)) break;
                IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient3 == null) {
                    Intrinsics.throwNpe();
                }
                iWorldClient3.setBlockState(this.blockPos, Blocks.field_150350_a.func_176223_P(), 11);
                INetworkManager iNetworkManager = MinecraftInstance.mc.getNetHandler().getNetworkManager();
                WBlockPos wBlockPos3 = this.blockPos;
                if (wBlockPos3 == null) {
                    Intrinsics.throwNpe();
                }
                IEnumFacing iEnumFacing = this.facing;
                if (iEnumFacing == null) {
                    Intrinsics.throwNpe();
                }
                iNetworkManager.sendPacket(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos3, iEnumFacing));
                this.bzx = 0.0f;
                this.bzs = false;
                break;
            }
            case "MoonX": {
                MinecraftInstance.mc.getPlayerController().setBlockHitDelay(0);
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                int n = MinecraftInstance.classProvider.getPotionEnum(PotionType.DIG_SPEED).getId();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.addPotionEffect(MinecraftInstance.classProvider.createPotionEffect(n, 100, iEntityPlayerSP2.getHeldItem() == null ? 1 : 0));
            }
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

