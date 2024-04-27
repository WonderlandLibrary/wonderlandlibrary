/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

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

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onBlockBB(BlockBBEvent blockBBEvent) {
        if (MinecraftInstance.mc.getThePlayer() != null && (MinecraftInstance.classProvider.isBlockLadder(blockBBEvent.getBlock()) || MinecraftInstance.classProvider.isBlockVine(blockBBEvent.getBlock())) && StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC3.0.5", (boolean)true)) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.isOnLadder()) {
                blockBBEvent.setBoundingBox(null);
            }
        }
    }

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        int n;
        int n2;
        String string = (String)this.modeValue.get();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (StringsKt.equals((String)string, (String)"Vanilla", (boolean)true) && iEntityPlayerSP2.isCollidedHorizontally() && iEntityPlayerSP2.isOnLadder()) {
            moveEvent.setY(((Number)this.speedValue.get()).floatValue());
            iEntityPlayerSP2.setMotionY(0.0);
        } else if (StringsKt.equals((String)string, (String)"AAC3.0.0", (boolean)true) && iEntityPlayerSP2.isCollidedHorizontally()) {
            double d = 0.0;
            double d2 = 0.0;
            IEnumFacing iEnumFacing = iEntityPlayerSP2.getHorizontalFacing();
            if (iEnumFacing.isNorth()) {
                d2 = -0.99;
            } else if (iEnumFacing.isEast()) {
                d = 0.99;
            } else if (iEnumFacing.isSouth()) {
                d2 = 0.99;
            } else if (iEnumFacing.isWest()) {
                d = -0.99;
            }
            IBlock iBlock = BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX() + d, iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ() + d2));
            if (MinecraftInstance.classProvider.isBlockLadder(iBlock) || MinecraftInstance.classProvider.isBlockVine(iBlock)) {
                moveEvent.setY(0.5);
                iEntityPlayerSP2.setMotionY(0.0);
            }
        } else if (StringsKt.equals((String)string, (String)"AAC3.0.5", (boolean)true) && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && BlockUtils.collideBlockIntersects(iEntityPlayerSP2.getEntityBoundingBox(), onMove.1.INSTANCE)) {
            moveEvent.setX(0.0);
            moveEvent.setY(0.5);
            moveEvent.setZ(0.0);
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionY(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        } else if (StringsKt.equals((String)string, (String)"SAAC3.1.2", (boolean)true) && iEntityPlayerSP2.isCollidedHorizontally() && iEntityPlayerSP2.isOnLadder()) {
            moveEvent.setY(0.1649);
            iEntityPlayerSP2.setMotionY(0.0);
        } else if (StringsKt.equals((String)string, (String)"AAC3.1.2", (boolean)true) && iEntityPlayerSP2.isCollidedHorizontally() && iEntityPlayerSP2.isOnLadder()) {
            moveEvent.setY(0.1699);
            iEntityPlayerSP2.setMotionY(0.0);
        } else if (StringsKt.equals((String)string, (String)"Clip", (boolean)true) && iEntityPlayerSP2.isOnLadder() && MinecraftInstance.mc.getGameSettings().getKeyBindForward().isKeyDown() && (n2 = (int)iEntityPlayerSP2.getPosY()) <= (n = (int)iEntityPlayerSP2.getPosY() + 8)) {
            while (true) {
                IBlock iBlock;
                if (!MinecraftInstance.classProvider.isBlockLadder(iBlock = BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), (double)n2, iEntityPlayerSP2.getPosZ())))) {
                    double d = 0.0;
                    double d3 = 0.0;
                    IEnumFacing iEnumFacing = iEntityPlayerSP2.getHorizontalFacing();
                    if (iEnumFacing.isNorth()) {
                        d3 = -1.0;
                    } else if (iEnumFacing.isEast()) {
                        d = 1.0;
                    } else if (iEnumFacing.isSouth()) {
                        d3 = 1.0;
                    } else if (iEnumFacing.isWest()) {
                        d = -1.0;
                    }
                    iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX() + d, n2, iEntityPlayerSP2.getPosZ() + d3);
                    break;
                }
                iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX(), n2, iEntityPlayerSP2.getPosZ());
                if (n2 == n) break;
                ++n2;
            }
        }
    }
}

