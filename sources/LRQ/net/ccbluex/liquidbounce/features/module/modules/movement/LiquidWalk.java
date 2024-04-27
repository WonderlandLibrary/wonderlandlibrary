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
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "NCP", "AAC", "AAC3.3.11", "AACFly", "Spartan", "Dolphin"}, "NCP");
    private final BoolValue noJumpValue = new BoolValue("NoJump", false);
    private final FloatValue aacFlyValue = new FloatValue("AACFlyMotion", 0.5f, 0.1f, 1.0f);
    private boolean nextTick;

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        block24: {
            block26: {
                block25: {
                    thePlayer = MinecraftInstance.mc.getThePlayer();
                    if (thePlayer == null || thePlayer.isSneaking()) {
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

                            if (!BlockUtils.collideBlock(thePlayer.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                                public final boolean invoke(@Nullable Object p1) {
                                    return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                                }

                                public final KDeclarationContainer getOwner() {
                                    return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                                }

                                public final String getName() {
                                    return "isBlockLiquid";
                                }

                                public final String getSignature() {
                                    return "isBlockLiquid(Ljava/lang/Object;)Z";
                                }
                            })) || !thePlayer.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.AIR)) || thePlayer.isSneaking()) ** break;
                            thePlayer.setMotionY(0.08);
                            ** break;
                        }
                    }
                    blockPos = thePlayer.getPosition().down();
                    if (!thePlayer.getOnGround() && BlockUtils.getBlock(blockPos).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER)) || thePlayer.isInWater()) {
                        if (!thePlayer.getSprinting()) {
                            v1 = thePlayer;
                            v1.setMotionX(v1.getMotionX() * 0.99999);
                            v2 = thePlayer;
                            v2.setMotionY(v2.getMotionY() * 0.0);
                            v3 = thePlayer;
                            v3.setMotionZ(v3.getMotionZ() * 0.99999);
                            if (thePlayer.isCollidedHorizontally()) {
                                thePlayer.setMotionY((float)((int)(thePlayer.getPosY() - (double)((int)(thePlayer.getPosY() - (double)true)))) / 8.0f);
                            }
                        } else {
                            v4 = thePlayer;
                            v4.setMotionX(v4.getMotionX() * 0.99999);
                            v5 = thePlayer;
                            v5.setMotionY(v5.getMotionY() * 0.0);
                            v6 = thePlayer;
                            v6.setMotionZ(v6.getMotionZ() * 0.99999);
                            if (thePlayer.isCollidedHorizontally()) {
                                thePlayer.setMotionY((float)((int)(thePlayer.getPosY() - (double)((int)(thePlayer.getPosY() - (double)true)))) / 8.0f);
                            }
                        }
                        if (thePlayer.getFallDistance() >= (float)4) {
                            thePlayer.setMotionY(-0.004);
                        } else if (thePlayer.isInWater()) {
                            thePlayer.setMotionY(0.09);
                        }
                    }
                    if (thePlayer.getHurtTime() == 0) ** break;
                    thePlayer.setOnGround(false);
                    ** break;
                }
                if (!thePlayer.isInWater()) ** break;
                if (thePlayer.isCollidedHorizontally()) {
                    v7 = thePlayer;
                    v7.setMotionY(v7.getMotionY() + 0.15);
                    return;
                }
                block = BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)true, thePlayer.getPosZ()));
                blockUp = BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 1.1, thePlayer.getPosZ()));
                if (MinecraftInstance.classProvider.isBlockLiquid(blockUp)) {
                    thePlayer.setMotionY(0.1);
                } else if (MinecraftInstance.classProvider.isBlockLiquid(block)) {
                    thePlayer.setMotionY(0.0);
                }
                thePlayer.setOnGround(true);
                v8 = thePlayer;
                v8.setMotionX(v8.getMotionX() * 1.085);
                v9 = thePlayer;
                v9.setMotionZ(v9.getMotionZ() * 1.085);
                ** break;
            }
            if (!thePlayer.isInWater()) ** break;
            v10 = thePlayer;
            v10.setMotionX(v10.getMotionX() * 1.17);
            v11 = thePlayer;
            v11.setMotionZ(v11.getMotionZ() * 1.17);
            if (thePlayer.isCollidedHorizontally()) {
                thePlayer.setMotionY(0.24);
                ** break;
            }
            v12 = MinecraftInstance.mc.getTheWorld();
            if (v12 == null) {
                Intrinsics.throwNpe();
            }
            if (!(v12.getBlockState(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 1.0, thePlayer.getPosZ())).getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)) ^ true)) ** break;
            v13 = thePlayer;
            v13.setMotionY(v13.getMotionY() + 0.04);
            ** break;
        }
        if (!thePlayer.isInWater()) ** break;
        v14 = thePlayer;
        v14.setMotionY(v14.getMotionY() + 0.03999999910593033);
lbl98:
        // 19 sources

    }

    @EventTarget
    public final void onMove(MoveEvent event) {
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
                event.setY(((Number)this.aacFlyValue.get()).floatValue());
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
    public final void onBlockBB(BlockBBEvent event) {
        block8: {
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            if (!MinecraftInstance.classProvider.isBlockLiquid(event.getBlock())) break block8;
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            if (BlockUtils.collideBlock(v0.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                public final boolean invoke(@Nullable Object p1) {
                    return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                }

                public final KDeclarationContainer getOwner() {
                    return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                }

                public final String getName() {
                    return "isBlockLiquid";
                }

                public final String getSignature() {
                    return "isBlockLiquid(Ljava/lang/Object;)Z";
                }
            }))) break block8;
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

                    event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + (double)true, (double)event.getY() + (double)true, (double)event.getZ() + (double)true));
                }
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || !StringsKt.equals((String)((String)this.modeValue.get()), (String)"NCP", (boolean)true)) {
            return;
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(event.getPacket())) {
            ICPacketPlayer packetPlayer = event.getPacket().asCPacketPlayer();
            if (BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(thePlayer.getEntityBoundingBox().getMaxX(), thePlayer.getEntityBoundingBox().getMaxY(), thePlayer.getEntityBoundingBox().getMaxZ(), thePlayer.getEntityBoundingBox().getMinX(), thePlayer.getEntityBoundingBox().getMinY() - 0.01, thePlayer.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

                public final boolean invoke(@Nullable Object p1) {
                    return ((IClassProvider)this.receiver).isBlockLiquid(p1);
                }

                public final KDeclarationContainer getOwner() {
                    return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                }

                public final String getName() {
                    return "isBlockLiquid";
                }

                public final String getSignature() {
                    return "isBlockLiquid(Ljava/lang/Object;)Z";
                }
            }))) {
                boolean bl = this.nextTick = !this.nextTick;
                if (this.nextTick) {
                    ICPacketPlayer iCPacketPlayer = packetPlayer;
                    iCPacketPlayer.setY(iCPacketPlayer.getY() - 0.001);
                }
            }
        }
    }

    @EventTarget
    public final void onJump(JumpEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IBlock block = BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 0.01, thePlayer.getPosZ()));
        if (((Boolean)this.noJumpValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockLiquid(block)) {
            event.cancelEvent();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

