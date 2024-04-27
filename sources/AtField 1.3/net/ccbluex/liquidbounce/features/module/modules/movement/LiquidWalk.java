/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.Reflection
 *  kotlin.reflect.KDeclarationContainer
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="LiquidWalk", description="Allows you to walk on water.", category=ModuleCategory.MOVEMENT, keyBind=36)
public final class LiquidWalk
extends Module {
    private final BoolValue noJumpValue;
    private boolean nextTick;
    private final FloatValue aacFlyValue;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "NCP", "AAC", "AAC3.3.11", "AACFly", "Spartan", "Dolphin"}, "NCP");

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IBlock iBlock = BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() - 0.01, iEntityPlayerSP2.getPosZ()));
        if (((Boolean)this.noJumpValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockLiquid(iBlock)) {
            jumpEvent.cancelEvent();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        String string = (String)this.modeValue.get();
        String string2 = "aacfly";
        boolean bl = false;
        String string3 = string;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        if (string2.equals(string4)) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.isInWater()) {
                moveEvent.setY(((Number)this.aacFlyValue.get()).floatValue());
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.setMotionY(((Number)this.aacFlyValue.get()).floatValue());
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent var1_1) {
        block24: {
            block26: {
                block25: {
                    var2_2 = MinecraftInstance.mc.getThePlayer();
                    if (var2_2 == null || var2_2.isSneaking()) {
                        return;
                    }
                    var3_3 = (String)this.modeValue.get();
                    var4_4 = false;
                    v0 = var3_3;
                    if (v0 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    var3_3 = v0.toLowerCase();
                    switch (var3_3.hashCode()) {
                        case 1837070814: {
                            if (!var3_3.equals("dolphin")) ** break;
                            break block24;
                        }
                        case 96323: {
                            if (!var3_3.equals("aac")) ** break;
                            break;
                        }
                        case -2011701869: {
                            if (!var3_3.equals("spartan")) ** break;
                            break block25;
                        }
                        case 1492139161: {
                            if (!var3_3.equals("aac3.3.11")) ** break;
                            break block26;
                        }
                        case 108891: {
                            if (!var3_3.equals("ncp")) ** break;
                            ** GOTO lbl28
                        }
                        case 233102203: {
                            if (!var3_3.equals("vanilla")) ** break;
lbl28:
                            // 2 sources

                            if (!BlockUtils.collideBlock(var2_2.getEntityBoundingBox(), new Function1(MinecraftInstance.classProvider){

                                public final boolean invoke(@Nullable Object object) {
                                    return ((IClassProvider)this.receiver).isBlockLiquid(object);
                                }

                                static {
                                }

                                public final String getSignature() {
                                    return "isBlockLiquid(Ljava/lang/Object;)Z";
                                }

                                public final KDeclarationContainer getOwner() {
                                    return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                                }

                                public Object invoke(Object object) {
                                    return this.invoke(object);
                                }

                                public final String getName() {
                                    return "isBlockLiquid";
                                }
                            }) || !var2_2.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.AIR)) || var2_2.isSneaking()) ** break;
                            var2_2.setMotionY(0.08);
                            ** break;
                        }
                    }
                    var4_5 = var2_2.getPosition().down();
                    if (!var2_2.getOnGround() && BlockUtils.getBlock(var4_5).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER)) || var2_2.isInWater()) {
                        if (!var2_2.getSprinting()) {
                            v1 = var2_2;
                            v1.setMotionX(v1.getMotionX() * 0.99999);
                            v2 = var2_2;
                            v2.setMotionY(v2.getMotionY() * 0.0);
                            v3 = var2_2;
                            v3.setMotionZ(v3.getMotionZ() * 0.99999);
                            if (var2_2.isCollidedHorizontally()) {
                                var2_2.setMotionY((float)((int)(var2_2.getPosY() - (double)((int)(var2_2.getPosY() - (double)true)))) / 8.0f);
                            }
                        } else {
                            v4 = var2_2;
                            v4.setMotionX(v4.getMotionX() * 0.99999);
                            v5 = var2_2;
                            v5.setMotionY(v5.getMotionY() * 0.0);
                            v6 = var2_2;
                            v6.setMotionZ(v6.getMotionZ() * 0.99999);
                            if (var2_2.isCollidedHorizontally()) {
                                var2_2.setMotionY((float)((int)(var2_2.getPosY() - (double)((int)(var2_2.getPosY() - (double)true)))) / 8.0f);
                            }
                        }
                        if (var2_2.getFallDistance() >= (float)4) {
                            var2_2.setMotionY(-0.004);
                        } else if (var2_2.isInWater()) {
                            var2_2.setMotionY(0.09);
                        }
                    }
                    if (var2_2.getHurtTime() == 0) ** break;
                    var2_2.setOnGround(false);
                    ** break;
                }
                if (!var2_2.isInWater()) ** break;
                if (var2_2.isCollidedHorizontally()) {
                    v7 = var2_2;
                    v7.setMotionY(v7.getMotionY() + 0.15);
                    return;
                }
                var4_6 = BlockUtils.getBlock(new WBlockPos(var2_2.getPosX(), var2_2.getPosY() + (double)true, var2_2.getPosZ()));
                var5_7 = BlockUtils.getBlock(new WBlockPos(var2_2.getPosX(), var2_2.getPosY() + 1.1, var2_2.getPosZ()));
                if (MinecraftInstance.classProvider.isBlockLiquid(var5_7)) {
                    var2_2.setMotionY(0.1);
                } else if (MinecraftInstance.classProvider.isBlockLiquid(var4_6)) {
                    var2_2.setMotionY(0.0);
                }
                var2_2.setOnGround(true);
                v8 = var2_2;
                v8.setMotionX(v8.getMotionX() * 1.085);
                v9 = var2_2;
                v9.setMotionZ(v9.getMotionZ() * 1.085);
                ** break;
            }
            if (!var2_2.isInWater()) ** break;
            v10 = var2_2;
            v10.setMotionX(v10.getMotionX() * 1.17);
            v11 = var2_2;
            v11.setMotionZ(v11.getMotionZ() * 1.17);
            if (var2_2.isCollidedHorizontally()) {
                var2_2.setMotionY(0.24);
                ** break;
            }
            v12 = MinecraftInstance.mc.getTheWorld();
            if (v12 == null) {
                Intrinsics.throwNpe();
            }
            if (!(v12.getBlockState(new WBlockPos(var2_2.getPosX(), var2_2.getPosY() + 1.0, var2_2.getPosZ())).getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)) ^ true)) ** break;
            v13 = var2_2;
            v13.setMotionY(v13.getMotionY() + 0.04);
            ** break;
        }
        if (!var2_2.isInWater()) ** break;
        v14 = var2_2;
        v14.setMotionY(v14.getMotionY() + 0.03999999910593033);
        ** break;
lbl99:
        // 19 sources

    }

    public LiquidWalk() {
        this.noJumpValue = new BoolValue("NoJump", false);
        this.aacFlyValue = new FloatValue("AACFlyMotion", 0.5f, 0.1f, 1.0f);
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null || !StringsKt.equals((String)((String)this.modeValue.get()), (String)"NCP", (boolean)true)) {
            return;
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(packetEvent.getPacket())) {
            ICPacketPlayer iCPacketPlayer = packetEvent.getPacket().asCPacketPlayer();
            if (BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(iEntityPlayerSP.getEntityBoundingBox().getMaxX(), iEntityPlayerSP.getEntityBoundingBox().getMaxY(), iEntityPlayerSP.getEntityBoundingBox().getMaxZ(), iEntityPlayerSP.getEntityBoundingBox().getMinX(), iEntityPlayerSP.getEntityBoundingBox().getMinY() - 0.01, iEntityPlayerSP.getEntityBoundingBox().getMinZ()), new Function1(MinecraftInstance.classProvider){

                public Object invoke(Object object) {
                    return this.invoke(object);
                }

                public final String getSignature() {
                    return "isBlockLiquid(Ljava/lang/Object;)Z";
                }

                public final String getName() {
                    return "isBlockLiquid";
                }

                public final boolean invoke(@Nullable Object object) {
                    return ((IClassProvider)this.receiver).isBlockLiquid(object);
                }

                public final KDeclarationContainer getOwner() {
                    return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                }

                static {
                }
            })) {
                boolean bl = this.nextTick = !this.nextTick;
                if (this.nextTick) {
                    ICPacketPlayer iCPacketPlayer2 = iCPacketPlayer;
                    iCPacketPlayer2.setY(iCPacketPlayer2.getY() - 0.001);
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onBlockBB(BlockBBEvent var1_1) {
        block8: {
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            if (!MinecraftInstance.classProvider.isBlockLiquid(var1_1.getBlock())) break block8;
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            if (BlockUtils.collideBlock(v0.getEntityBoundingBox(), new Function1(MinecraftInstance.classProvider){

                public final String getName() {
                    return "isBlockLiquid";
                }

                static {
                }

                public Object invoke(Object object) {
                    return this.invoke(object);
                }

                public final boolean invoke(@Nullable Object object) {
                    return ((IClassProvider)this.receiver).isBlockLiquid(object);
                }

                public final String getSignature() {
                    return "isBlockLiquid(Ljava/lang/Object;)Z";
                }

                public final KDeclarationContainer getOwner() {
                    return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                }
            })) break block8;
            v1 = MinecraftInstance.mc.getThePlayer();
            if (v1 == null) {
                Intrinsics.throwNpe();
            }
            if (v1.isSneaking()) break block8;
            var2_2 = (String)this.modeValue.get();
            var3_3 = false;
            v2 = var2_2;
            if (v2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var2_2 = v2.toLowerCase();
            switch (var2_2.hashCode()) {
                case 108891: {
                    if (!var2_2.equals("ncp")) break;
                    ** GOTO lbl24
                }
                case 233102203: {
                    if (!var2_2.equals("vanilla")) break;
lbl24:
                    // 2 sources

                    var1_1.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(var1_1.getX(), var1_1.getY(), var1_1.getZ(), (double)var1_1.getX() + (double)true, (double)var1_1.getY() + (double)true, (double)var1_1.getZ() + (double)true));
                    break;
                }
            }
        }
    }
}

