package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Spider", category=ModuleCategory.MOVEMENT, description="sda")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000H\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ\b0HJ020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000R0X¢\n\u0000R\f0\rX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Spider;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "glitch", "", "groundHeight", "", "heightValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "modifyBB", "motionValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "startHeight", "wasTimer", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onDisable", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Spider
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Collide", "Motion", "AAC4"}, "Collide");
    private final IntegerValue heightValue = new IntegerValue("Height", 2, 0, 10);
    private final FloatValue motionValue = new FloatValue("Motion", 0.42f, 0.1f, 1.0f);
    private double startHeight;
    private double groundHeight;
    private boolean modifyBB;
    private boolean glitch;
    private boolean wasTimer;

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block27: {
            block28: {
                block26: {
                    block25: {
                        Intrinsics.checkParameterIsNotNull(event, "event");
                        if (this.wasTimer) {
                            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                        }
                        v0 = MinecraftInstance.mc.getThePlayer();
                        if (v0 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!v0.isCollidedHorizontally() || !MinecraftInstance.mc.getGameSettings().getKeyBindForward().getPressed()) break block25;
                        v1 = MinecraftInstance.mc.getThePlayer();
                        if (v1 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!(v1.getPosY() - ((Number)this.heightValue.get()).doubleValue() > this.startHeight) || ((Number)this.heightValue.get()).intValue() <= 0) break block26;
                    }
                    v2 = MinecraftInstance.mc.getThePlayer();
                    if (v2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v2.getOnGround()) {
                        v3 = MinecraftInstance.mc.getThePlayer();
                        if (v3 == null) {
                            Intrinsics.throwNpe();
                        }
                        this.startHeight = v3.getPosY();
                        v4 = MinecraftInstance.mc.getThePlayer();
                        if (v4 == null) {
                            Intrinsics.throwNpe();
                        }
                        this.groundHeight = v4.getPosY();
                    }
                    this.modifyBB = false;
                    return;
                }
                if (!Intrinsics.areEqual((String)this.modeValue.get(), "AAC4")) break block27;
                v5 = MinecraftInstance.mc.getThePlayer();
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                if (v5.getMotionY() < 0.0) break block28;
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v6.getOnGround()) break block27;
            }
            this.glitch = true;
        }
        this.modifyBB = true;
        var2_2 = (String)this.modeValue.get();
        var3_3 = false;
        v7 = var2_2;
        if (v7 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v8 = v7.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v8, "(this as java.lang.String).toLowerCase()");
        var2_2 = v8;
        switch (var2_2.hashCode()) {
            case -1068318794: {
                if (!var2_2.equals("motion")) ** break;
                break;
            }
            case 949448766: {
                if (!var2_2.equals("collide")) ** break;
                ** GOTO lbl58
            }
            case 2986065: {
                if (!var2_2.equals("aac4")) ** break;
lbl58:
                // 2 sources

                v9 = MinecraftInstance.mc.getThePlayer();
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v9.getOnGround()) ** break;
                v10 = MinecraftInstance.mc.getThePlayer();
                if (v10 == null) {
                    Intrinsics.throwNpe();
                }
                v10.jump();
                v11 = MinecraftInstance.mc.getThePlayer();
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                this.groundHeight = v11.getPosY();
                if (!Intrinsics.areEqual((String)this.modeValue.get(), "AAC4")) ** break;
                this.wasTimer = true;
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.4f);
                ** break;
            }
        }
        v12 = MinecraftInstance.mc.getThePlayer();
        if (v12 == null) {
            Intrinsics.throwNpe();
        }
        v12.setMotionY(((Number)this.motionValue.get()).floatValue());
        ** break;
lbl79:
        // 8 sources

    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.glitch) {
            this.glitch = false;
            float yaw = (float)MovementUtils.getDirection();
            double d = packet.asCPacketPlayer().getX();
            ICPacketPlayer iCPacketPlayer = packet.asCPacketPlayer();
            boolean bl = false;
            float f = (float)Math.sin(yaw);
            iCPacketPlayer.setX(d - (double)f * 1.0E-8);
            d = packet.asCPacketPlayer().getZ();
            iCPacketPlayer = packet.asCPacketPlayer();
            bl = false;
            f = (float)Math.cos(yaw);
            iCPacketPlayer.setZ(d + (double)f * 1.0E-8);
        }
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.wasTimer = false;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        block25: {
            block24: {
                block23: {
                    block22: {
                        Intrinsics.checkParameterIsNotNull(event, "event");
                        v0 = MinecraftInstance.mc.getThePlayer();
                        if (v0 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!v0.isCollidedHorizontally() || !MinecraftInstance.mc.getGameSettings().getKeyBindForward().getPressed()) break block22;
                        v1 = MinecraftInstance.mc.getThePlayer();
                        if (v1 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!(v1.getPosY() - ((Number)this.heightValue.get()).doubleValue() > this.startHeight) || ((Number)this.heightValue.get()).intValue() <= 0) break block23;
                    }
                    return;
                }
                if (!this.modifyBB) break block24;
                v2 = MinecraftInstance.mc.getThePlayer();
                if (v2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v2.getMotionY() > 0.0)) break block25;
            }
            return;
        }
        var2_2 = (String)this.modeValue.get();
        var3_3 = false;
        v3 = var2_2;
        if (v3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v4 = v3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v4, "(this as java.lang.String).toLowerCase()");
        var2_2 = v4;
        switch (var2_2.hashCode()) {
            case 949448766: {
                if (!var2_2.equals("collide")) break;
                ** GOTO lbl35
            }
            case 2986065: {
                if (!var2_2.equals("aac4")) break;
lbl35:
                // 2 sources

                if (event.getBlock() == null || MinecraftInstance.mc.getThePlayer() == null || !MinecraftInstance.classProvider.isBlockAir(event.getBlock())) break;
                v5 = event.getY();
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(v5 < v6.getPosY())) break;
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v7.isCollidedHorizontally()) break;
                v8 = MinecraftInstance.mc.getThePlayer();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v8.isOnLadder()) break;
                v9 = MinecraftInstance.mc.getThePlayer();
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v9.isInWater()) break;
                v10 = MinecraftInstance.mc.getThePlayer();
                if (v10 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v10.isInLava()) break;
                v11 = MinecraftInstance.classProvider.createAxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                v12 = MinecraftInstance.mc.getThePlayer();
                if (v12 == null) {
                    Intrinsics.throwNpe();
                }
                v13 = v12.getPosX();
                v14 = MinecraftInstance.mc.getThePlayer();
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                v15 = (double)((int)v14.getPosY()) - 1.0;
                v16 = MinecraftInstance.mc.getThePlayer();
                if (v16 == null) {
                    Intrinsics.throwNpe();
                }
                event.setBoundingBox(v11.offset(v13, v15, v16.getPosZ()));
                break;
            }
        }
    }
}
