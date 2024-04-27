/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallClimb;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="WallClimb", description="Allows you to climb up walls like a spider.", category=ModuleCategory.MOVEMENT)
public final class WallClimb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Simple", "CheckerClimb", "Clip", "AAC3.3.12", "AACGlide"}, "Simple");
    private final ListValue clipMode = new ListValue("ClipMode", new String[]{"Jump", "Fast"}, "Fast");
    private final FloatValue checkerClimbMotionValue = new FloatValue("CheckerClimbMotion", 0.0f, 0.0f, 1.0f);
    private boolean glitch;
    private int waited;

    @EventTarget
    public final void onUpdate(MotionEvent motionEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (motionEvent.getEventState() != EventState.POST || iEntityPlayerSP == null) {
            return;
        }
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        block6 : switch (string2.toLowerCase()) {
            case "clip": {
                if (iEntityPlayerSP.getMotionY() < 0.0) {
                    this.glitch = true;
                }
                if (!iEntityPlayerSP.isCollidedHorizontally()) break;
                String string3 = (String)this.clipMode.get();
                boolean bl = false;
                String string4 = string3;
                if (string4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string3 = string4.toLowerCase();
                switch (string3.hashCode()) {
                    case 3135580: {
                        if (!string3.equals("fast")) break block6;
                        break;
                    }
                    case 3273774: {
                        if (!string3.equals("jump") || !iEntityPlayerSP.getOnGround()) break block6;
                        iEntityPlayerSP.jump();
                        break block6;
                    }
                }
                if (iEntityPlayerSP.getOnGround()) {
                    iEntityPlayerSP.setMotionY(0.42);
                    break;
                }
                if (!(iEntityPlayerSP.getMotionY() < 0.0)) break;
                iEntityPlayerSP.setMotionY(-0.3);
                break;
            }
            case "checkerclimb": {
                n = BlockUtils.collideBlockIntersects(iEntityPlayerSP.getEntityBoundingBox(), onUpdate.isInsideBlock.1.INSTANCE);
                float f = ((Number)this.checkerClimbMotionValue.get()).floatValue();
                if (n == 0 || f == 0.0f) break;
                iEntityPlayerSP.setMotionY(f);
                break;
            }
            case "aac3.3.12": {
                if (iEntityPlayerSP.isCollidedHorizontally() && !iEntityPlayerSP.isOnLadder()) {
                    n = this.waited;
                    this.waited = n + 1;
                    if (this.waited == 1) {
                        iEntityPlayerSP.setMotionY(0.43);
                    }
                    if (this.waited == 12) {
                        iEntityPlayerSP.setMotionY(0.43);
                    }
                    if (this.waited == 23) {
                        iEntityPlayerSP.setMotionY(0.43);
                    }
                    if (this.waited == 29) {
                        iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY() + 0.5, iEntityPlayerSP.getPosZ());
                    }
                    if (this.waited < 30) break;
                    this.waited = 0;
                    break;
                }
                if (!iEntityPlayerSP.getOnGround()) break;
                this.waited = 0;
                break;
            }
            case "aacglide": {
                if (!iEntityPlayerSP.isCollidedHorizontally() || iEntityPlayerSP.isOnLadder()) {
                    return;
                }
                iEntityPlayerSP.setMotionY(-0.19);
                break;
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        if (MinecraftInstance.classProvider.isCPacketPlayer(packetEvent.getPacket())) {
            ICPacketPlayer iCPacketPlayer = packetEvent.getPacket().asCPacketPlayer();
            if (this.glitch) {
                float f = (float)MovementUtils.getDirection();
                double d = iCPacketPlayer.getX();
                ICPacketPlayer iCPacketPlayer2 = iCPacketPlayer;
                boolean bl = false;
                float f2 = (float)Math.sin(f);
                iCPacketPlayer2.setX(d - (double)f2 * 1.0E-8);
                d = iCPacketPlayer.getZ();
                iCPacketPlayer2 = iCPacketPlayer;
                bl = false;
                f2 = (float)Math.cos(f);
                iCPacketPlayer2.setZ(d + (double)f2 * 1.0E-8);
                this.glitch = false;
            }
        }
    }

    @EventTarget
    public final void onBlockBB(BlockBBEvent blockBBEvent) {
        String string;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        String string2 = string = (String)this.modeValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string3.toLowerCase()) {
            case "checkerclimb": {
                if (!((double)blockBBEvent.getY() > iEntityPlayerSP2.getPosY())) break;
                blockBBEvent.setBoundingBox(null);
                break;
            }
            case "clip": {
                if (blockBBEvent.getBlock() == null || MinecraftInstance.mc.getThePlayer() == null || !MinecraftInstance.classProvider.isBlockAir(blockBBEvent.getBlock()) || !((double)blockBBEvent.getY() < iEntityPlayerSP2.getPosY()) || !iEntityPlayerSP2.isCollidedHorizontally() || iEntityPlayerSP2.isOnLadder() || iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava()) break;
                blockBBEvent.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(iEntityPlayerSP2.getPosX(), (double)((int)iEntityPlayerSP2.getPosY()) - 1.0, iEntityPlayerSP2.getPosZ()));
                break;
            }
        }
    }

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!iEntityPlayerSP2.isCollidedHorizontally() || iEntityPlayerSP2.isOnLadder() || iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava()) {
            return;
        }
        if (StringsKt.equals((String)"simple", (String)((String)this.modeValue.get()), (boolean)true)) {
            moveEvent.setY(0.2);
            iEntityPlayerSP2.setMotionY(0.0);
        }
    }
}

