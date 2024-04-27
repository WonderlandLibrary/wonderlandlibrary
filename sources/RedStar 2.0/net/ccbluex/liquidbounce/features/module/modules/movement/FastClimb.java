package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FastClimb", description="Allows you to climb up ladders and vines faster.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\r020HJ020HR0¢\b\n\u0000\bR0\bX¢\n\u0000R\t0\n8VX¢\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/FastClimb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "Pride"})
public final class FastClimb
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Clip", "AAC3.0.0", "AAC3.0.5", "SAAC3.1.2", "AAC3.1.2"}, "Vanilla");
    private final FloatValue speedValue = new FloatValue("Speed", 0.2872f, 0.01f, 5.0f);

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        int n;
        int x2;
        Intrinsics.checkParameterIsNotNull(event, "event");
        String mode = (String)this.modeValue.get();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (StringsKt.equals(mode, "Vanilla", true) && thePlayer.isCollidedHorizontally() && thePlayer.isOnLadder()) {
            event.setY(((Number)this.speedValue.get()).floatValue());
            thePlayer.setMotionY(0.0);
        } else if (StringsKt.equals(mode, "AAC3.0.0", true) && thePlayer.isCollidedHorizontally()) {
            IBlock block;
            double x2 = 0.0;
            double z = 0.0;
            IEnumFacing horizontalFacing = thePlayer.getHorizontalFacing();
            if (horizontalFacing.isNorth()) {
                z = -0.99;
            } else if (horizontalFacing.isEast()) {
                x2 = 0.99;
            } else if (horizontalFacing.isSouth()) {
                z = 0.99;
            } else if (horizontalFacing.isWest()) {
                x2 = -0.99;
            }
            WBlockPos blockPos$iv = new WBlockPos(thePlayer.getPosX() + x2, thePlayer.getPosY(), thePlayer.getPosZ() + z);
            boolean $i$f$getBlock = false;
            Object object = MinecraftInstance.mc.getTheWorld();
            IBlock iBlock = object != null && (object = object.getBlockState(blockPos$iv)) != null ? object.getBlock() : (block = null);
            if (MinecraftInstance.classProvider.isBlockLadder(block) || MinecraftInstance.classProvider.isBlockVine(block)) {
                event.setY(0.5);
                thePlayer.setMotionY(0.0);
            }
        } else if (StringsKt.equals(mode, "AAC3.0.5", true) && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && BlockUtils.collideBlockIntersects(thePlayer.getEntityBoundingBox(), onMove.1.INSTANCE)) {
            event.setX(0.0);
            event.setY(0.5);
            event.setZ(0.0);
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionY(0.0);
            thePlayer.setMotionZ(0.0);
        } else if (StringsKt.equals(mode, "SAAC3.1.2", true) && thePlayer.isCollidedHorizontally() && thePlayer.isOnLadder()) {
            event.setY(0.1649);
            thePlayer.setMotionY(0.0);
        } else if (StringsKt.equals(mode, "AAC3.1.2", true) && thePlayer.isCollidedHorizontally() && thePlayer.isOnLadder()) {
            event.setY(0.1699);
            thePlayer.setMotionY(0.0);
        } else if (StringsKt.equals(mode, "Clip", true) && thePlayer.isOnLadder() && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && (x2 = (int)thePlayer.getPosY()) <= (n = (int)thePlayer.getPosY() + 8)) {
            while (true) {
                IBlock block;
                void i;
                WBlockPos blockPos$iv = new WBlockPos(thePlayer.getPosX(), (double)i, thePlayer.getPosZ());
                boolean $i$f$getBlock = false;
                Object object = MinecraftInstance.mc.getTheWorld();
                IBlock iBlock = object != null && (object = object.getBlockState(blockPos$iv)) != null ? object.getBlock() : (block = null);
                if (!MinecraftInstance.classProvider.isBlockLadder(block)) {
                    double x3 = 0.0;
                    double z = 0.0;
                    IEnumFacing horizontalFacing = thePlayer.getHorizontalFacing();
                    if (horizontalFacing.isNorth()) {
                        z = -1.0;
                    } else if (horizontalFacing.isEast()) {
                        x3 = 1.0;
                    } else if (horizontalFacing.isSouth()) {
                        z = 1.0;
                    } else if (horizontalFacing.isWest()) {
                        x3 = -1.0;
                    }
                    thePlayer.setPosition(thePlayer.getPosX() + x3, (double)i, thePlayer.getPosZ() + z);
                    break;
                }
                thePlayer.setPosition(thePlayer.getPosX(), (double)i, thePlayer.getPosZ());
                if (i == n) break;
                ++i;
            }
        }
    }

    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MinecraftInstance.mc.getThePlayer() != null && (MinecraftInstance.classProvider.isBlockLadder(event.getBlock()) || MinecraftInstance.classProvider.isBlockVine(event.getBlock())) && StringsKt.equals((String)this.modeValue.get(), "AAC3.0.5", true)) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.isOnLadder()) {
                event.setBoundingBox(null);
            }
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
