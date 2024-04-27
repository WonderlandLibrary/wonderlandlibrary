/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function1
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
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
    public final void onMove(MoveEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isInLava()) {
            return;
        }
        if (StringsKt.equals((String)"simple", (String)((String)this.modeValue.get()), (boolean)true)) {
            event.setY(0.2);
            thePlayer.setMotionY(0.0);
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(MotionEvent event) {
        thePlayer = MinecraftInstance.mc.getThePlayer();
        if (event.getEventState() != EventState.POST || thePlayer == null) {
            return;
        }
        var3_3 = (String)this.modeValue.get();
        var4_4 = 0;
        v0 = var3_3;
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var3_3 = v0.toLowerCase();
        tmp = -1;
        switch (var3_3.hashCode()) {
            case 1492139162: {
                if (!var3_3.equals("aac3.3.12")) break;
                tmp = 1;
                break;
            }
            case 1076723744: {
                if (!var3_3.equals("checkerclimb")) break;
                tmp = 2;
                break;
            }
            case 3056464: {
                if (!var3_3.equals("clip")) break;
                tmp = 3;
                break;
            }
            case 375151938: {
                if (!var3_3.equals("aacglide")) break;
                tmp = 4;
                break;
            }
        }
        switch (tmp) {
            case 3: {
                if (thePlayer.getMotionY() < (double)false) {
                    this.glitch = true;
                }
                if (!thePlayer.isCollidedHorizontally()) break;
                var4_5 = (String)this.clipMode.get();
                var5_6 = false;
                v1 = var4_5;
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var4_5 = v1.toLowerCase();
                switch (var4_5.hashCode()) {
                    case 3135580: {
                        if (!var4_5.equals("fast")) ** break;
                        break;
                    }
                    case 3273774: {
                        if (!var4_5.equals("jump") || !thePlayer.getOnGround()) ** break;
                        thePlayer.jump();
                        ** break;
                    }
                }
                if (thePlayer.getOnGround()) {
                    thePlayer.setMotionY(0.42);
                    break;
                }
                if (!(thePlayer.getMotionY() < (double)false)) break;
                thePlayer.setMotionY(-0.3);
lbl53:
                // 5 sources

                break;
            }
            case 2: {
                isInsideBlock = BlockUtils.collideBlockIntersects(thePlayer.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)onUpdate.isInsideBlock.1.INSTANCE));
                motion = ((Number)this.checkerClimbMotionValue.get()).floatValue();
                if (!isInsideBlock || motion == 0.0f) break;
                thePlayer.setMotionY(motion);
                break;
            }
            case 1: {
                if (thePlayer.isCollidedHorizontally() && !thePlayer.isOnLadder()) {
                    var4_4 = this.waited;
                    this.waited = var4_4 + 1;
                    if (this.waited == 1) {
                        thePlayer.setMotionY(0.43);
                    }
                    if (this.waited == 12) {
                        thePlayer.setMotionY(0.43);
                    }
                    if (this.waited == 23) {
                        thePlayer.setMotionY(0.43);
                    }
                    if (this.waited == 29) {
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.5, thePlayer.getPosZ());
                    }
                    if (this.waited < 30) break;
                    this.waited = 0;
                    break;
                }
                if (!thePlayer.getOnGround()) break;
                this.waited = 0;
                break;
            }
            case 4: {
                if (!thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder()) {
                    return;
                }
                thePlayer.setMotionY(-0.19);
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        if (MinecraftInstance.classProvider.isCPacketPlayer(event.getPacket())) {
            ICPacketPlayer packetPlayer = event.getPacket().asCPacketPlayer();
            if (this.glitch) {
                float yaw = (float)MovementUtils.getDirection();
                double d = packetPlayer.getX();
                ICPacketPlayer iCPacketPlayer = packetPlayer;
                boolean bl = false;
                float f = (float)Math.sin(yaw);
                iCPacketPlayer.setX(d - (double)f * 1.0E-8);
                d = packetPlayer.getZ();
                iCPacketPlayer = packetPlayer;
                bl = false;
                f = (float)Math.cos(yaw);
                iCPacketPlayer.setZ(d + (double)f * 1.0E-8);
                this.glitch = false;
            }
        }
    }

    @EventTarget
    public final void onBlockBB(BlockBBEvent event) {
        String mode;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String string = mode = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "checkerclimb": {
                if (!((double)event.getY() > thePlayer.getPosY())) break;
                event.setBoundingBox(null);
                break;
            }
            case "clip": {
                if (event.getBlock() == null || MinecraftInstance.mc.getThePlayer() == null || !MinecraftInstance.classProvider.isBlockAir(event.getBlock()) || !((double)event.getY() < thePlayer.getPosY()) || !thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isInLava()) break;
                event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(thePlayer.getPosX(), (double)((int)thePlayer.getPosY()) - 1.0, thePlayer.getPosZ()));
            }
        }
    }
}

