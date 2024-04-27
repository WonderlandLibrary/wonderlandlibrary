package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="WallClimb", description="Allows you to climb up walls like a spider.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\b\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\f0\r20HJ0\r20HJ0\r20HJ0\r20HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0X¢\n\u0000R\n0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/WallClimb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "checkerClimbMotionValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "clipMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "glitch", "", "modeValue", "waited", "", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "Pride"})
public final class WallClimb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Simple", "CheckerClimb", "Clip", "AAC3.3.12", "AACGlide"}, "Simple");
    private final ListValue clipMode = new ListValue("ClipMode", new String[]{"Jump", "Fast"}, "Fast");
    private final FloatValue checkerClimbMotionValue = new FloatValue("CheckerClimbMotion", 0.0f, 0.0f, 1.0f);
    private boolean glitch;
    private int waited;

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isInLava()) {
            return;
        }
        if (StringsKt.equals("simple", (String)this.modeValue.get(), true)) {
            event.setY(0.2);
            thePlayer.setMotionY(0.0);
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (event.getEventState() != EventState.POST || thePlayer == null) {
            return;
        }
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        block6 : switch (string3) {
            case "clip": {
                if (thePlayer.getMotionY() < 0.0) {
                    this.glitch = true;
                }
                if (!thePlayer.isCollidedHorizontally()) break;
                String string4 = (String)this.clipMode.get();
                boolean bl = false;
                String string5 = string4;
                if (string5 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string6 = string5.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string6, "(this as java.lang.String).toLowerCase()");
                string4 = string6;
                switch (string4.hashCode()) {
                    case 3135580: {
                        if (!string4.equals("fast")) break block6;
                        break;
                    }
                    case 3273774: {
                        if (!string4.equals("jump") || !thePlayer.getOnGround()) break block6;
                        thePlayer.jump();
                        break block6;
                    }
                }
                if (thePlayer.getOnGround()) {
                    thePlayer.setMotionY(0.42);
                    break;
                }
                if (!(thePlayer.getMotionY() < 0.0)) break;
                thePlayer.setMotionY(-0.3);
                break;
            }
            case "checkerclimb": {
                boolean isInsideBlock2 = BlockUtils.collideBlockIntersects(thePlayer.getEntityBoundingBox(), onUpdate.isInsideBlock.1.INSTANCE);
                float motion = ((Number)this.checkerClimbMotionValue.get()).floatValue();
                if (!isInsideBlock2 || motion == 0.0f) break;
                thePlayer.setMotionY(motion);
                break;
            }
            case "aac3.3.12": {
                if (thePlayer.isCollidedHorizontally() && !thePlayer.isOnLadder()) {
                    n = this.waited;
                    this.waited = n + 1;
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
            case "aacglide": {
                if (!thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder()) {
                    return;
                }
                thePlayer.setMotionY(-0.19);
                break;
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        String mode;
        Intrinsics.checkParameterIsNotNull(event, "event");
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
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "checkerclimb": {
                if (!((double)event.getY() > thePlayer.getPosY())) break;
                event.setBoundingBox(null);
                break;
            }
            case "clip": {
                if (event.getBlock() == null || MinecraftInstance.mc.getThePlayer() == null || !MinecraftInstance.classProvider.isBlockAir(event.getBlock()) || !((double)event.getY() < thePlayer.getPosY()) || !thePlayer.isCollidedHorizontally() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isInLava()) break;
                event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(thePlayer.getPosX(), (double)((int)thePlayer.getPosY()) - 1.0, thePlayer.getPosZ()));
                break;
            }
        }
    }
}
