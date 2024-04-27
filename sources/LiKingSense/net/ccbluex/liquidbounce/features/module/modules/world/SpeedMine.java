/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="SpeedMine", description="faq", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0012\u0010\u0017\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0018H\u0007R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/SpeedMine;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getBlockPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "setBlockPos", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;)V", "bzs", "", "bzx", "", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getFacing", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "setFacing", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;)V", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class SpeedMine
extends Module {
    public boolean bzs;
    public float bzx;
    @Nullable
    public WBlockPos blockPos;
    @Nullable
    public IEnumFacing facing;

    @Nullable
    public final WBlockPos getBlockPos() {
        return this.blockPos;
    }

    public final void setBlockPos(@Nullable WBlockPos wBlockPos) {
        this.blockPos = wBlockPos;
    }

    @Nullable
    public final IEnumFacing getFacing() {
        return this.facing;
    }

    public final void setFacing(@Nullable IEnumFacing iEnumFacing) {
        this.facing = iEnumFacing;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerDigging(packet)) {
            IPacket packets = packet.asCPacketPlayerDigging();
            if (packet.asCPacketPlayerDigging().getAction() == ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK) {
                this.bzs = 1;
                this.blockPos = packets.asCPacketPlayerDigging().getPosition();
                this.facing = packet.asCPacketPlayerDigging().getFacing();
                this.bzx = 0.0f;
            } else if (packet.asCPacketPlayerDigging().getAction() == ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK || packet.asCPacketPlayerDigging().getAction() == ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK) {
                this.bzs = 0;
                this.blockPos = null;
                this.facing = null;
            }
        }
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        if (MinecraftInstance.mc.getPlayerController().extendedReach()) {
            MinecraftInstance.mc.getPlayerController().setBlockHitDelay(0);
        } else if (this.bzs) {
            IBlock block = MinecraftInstance.mc.getTheWorld().getBlockState(this.blockPos).getBlock();
            this.bzx += (float)((double)block.getPlayerRelativeBlockHardness(MinecraftInstance.mc.getThePlayer(), MinecraftInstance.mc.getTheWorld(), this.blockPos) * 1.4);
            if (this.bzx >= 1.0f) {
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                Block block2 = Blocks.field_150350_a;
                Intrinsics.checkExpressionValueIsNotNull((Object)block2, (String)"Blocks.AIR");
                iWorldClient.setBlockState(this.blockPos, block2.func_176223_P(), 11);
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                this.bzx = 0.0f;
                this.bzs = 0;
            }
        }
    }
}

