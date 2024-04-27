/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

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

@ModuleInfo(name="FastClimb", description="Allows you to climb up ladders and vines faster.", category=ModuleCategory.MOVEMENT)
public final class FastClimb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Clip", "AAC3.0.0", "AAC3.0.5", "SAAC3.1.2", "AAC3.1.2"}, "Vanilla");
    private final FloatValue speedValue = new FloatValue("Speed", 0.2872f, 0.01f, 5.0f);

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMove(MoveEvent event) {
        int n;
        int x2;
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
    public final void onBlockBB(BlockBBEvent event) {
        if (MinecraftInstance.mc.getThePlayer() != null && (MinecraftInstance.classProvider.isBlockLadder(event.getBlock()) || MinecraftInstance.classProvider.isBlockVine(event.getBlock())) && StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC3.0.5", (boolean)true)) {
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
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

