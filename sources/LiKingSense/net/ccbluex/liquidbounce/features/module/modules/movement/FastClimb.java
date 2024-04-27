/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
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
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0012H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/FastClimb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "LiKingSense"})
public final class FastClimb
extends Module {
    @NotNull
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Clip", "AAC3.0.0", "AAC3.0.5", "SAAC3.1.2", "AAC3.1.2"}, "Vanilla");
    public final FloatValue speedValue = new FloatValue("Speed", 0.2872f, 0.01f, 5.0f);

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
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        String mode = (String)this.modeValue.get();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (StringsKt.equals((String)mode, (String)"Vanilla", (boolean)true) && thePlayer.isCollidedHorizontally() && thePlayer.isOnLadder()) {
            event.setY(((Number)this.speedValue.get()).floatValue());
            thePlayer.setMotionY(0.0);
        } else if (StringsKt.equals((String)mode, (String)"AAC3.0.0", (boolean)true) && thePlayer.isCollidedHorizontally()) {
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
            IBlock block = BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX() + x2, thePlayer.getPosY(), thePlayer.getPosZ() + z));
            if (MinecraftInstance.classProvider.isBlockLadder(block) || MinecraftInstance.classProvider.isBlockVine(block)) {
                event.setY(0.5);
                thePlayer.setMotionY(0.0);
            }
        } else if (StringsKt.equals((String)mode, (String)"AAC3.0.5", (boolean)true) && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && BlockUtils.collideBlockIntersects(thePlayer.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)onMove.1.INSTANCE))) {
            event.setX(0.0);
            event.setY(0.5);
            event.setZ(0.0);
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionY(0.0);
            thePlayer.setMotionZ(0.0);
        } else if (StringsKt.equals((String)mode, (String)"SAAC3.1.2", (boolean)true) && thePlayer.isCollidedHorizontally() && thePlayer.isOnLadder()) {
            event.setY(0.1649);
            thePlayer.setMotionY(0.0);
        } else if (StringsKt.equals((String)mode, (String)"AAC3.1.2", (boolean)true) && thePlayer.isCollidedHorizontally() && thePlayer.isOnLadder()) {
            event.setY(0.1699);
            thePlayer.setMotionY(0.0);
        } else if (StringsKt.equals((String)mode, (String)"Clip", (boolean)true) && thePlayer.isOnLadder() && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && (x2 = (int)thePlayer.getPosY()) <= (n = (int)thePlayer.getPosY() + 8)) {
            while (true) {
                void i;
                IBlock block;
                if (!MinecraftInstance.classProvider.isBlockLadder(block = BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), (double)i, thePlayer.getPosZ())))) {
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
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer() != null && (MinecraftInstance.classProvider.isBlockLadder(event.getBlock()) || MinecraftInstance.classProvider.isBlockVine(event.getBlock())) && StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC3.0.5", (boolean)true) && MinecraftInstance.mc.getThePlayer().isOnLadder()) {
            event.setBoundingBox(null);
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

