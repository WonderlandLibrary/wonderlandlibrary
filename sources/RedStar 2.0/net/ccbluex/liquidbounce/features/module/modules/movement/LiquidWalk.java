package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="LiquidWalk", description="Allows you to walk on water.", category=ModuleCategory.MOVEMENT, keyBind=36)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000R\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ020HJ020HJ020HJ02\b0HR0X¢\n\u0000R0¢\b\n\u0000\b\bR\t0\nX¢\n\u0000R0\fX¢\n\u0000R\r08VX¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/LiquidWalk;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacFlyValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "nextTick", "", "noJumpValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "tag", "", "getTag", "()Ljava/lang/String;", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onJump", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class LiquidWalk
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "NCP", "AAC", "AAC3.3.11", "AACFly", "Spartan", "Dolphin"}, "NCP");
    private final BoolValue noJumpValue = new BoolValue("NoJump", false);
    private final FloatValue aacFlyValue = new FloatValue("AACFlyMotion", 0.5f, 0.1f, 1.0f);
    private boolean nextTick;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        block26: {
            block28: {
                block27: {
                    block31: {
                        block30: {
                            block29: {
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
                                v1 = v0.toLowerCase();
                                Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
                                var3_3 = v1;
                                switch (var3_3.hashCode()) {
                                    case 1837070814: {
                                        if (!var3_3.equals("dolphin")) ** break;
                                        break block26;
                                    }
                                    case 96323: {
                                        if (!var3_3.equals("aac")) ** break;
                                        break;
                                    }
                                    case -2011701869: {
                                        if (!var3_3.equals("spartan")) ** break;
                                        break block27;
                                    }
                                    case 1492139161: {
                                        if (!var3_3.equals("aac3.3.11")) ** break;
                                        break block28;
                                    }
                                    case 108891: {
                                        if (!var3_3.equals("ncp")) ** break;
                                        ** GOTO lbl30
                                    }
                                    case 233102203: {
                                        if (!var3_3.equals("vanilla")) ** break;
lbl30:
                                        // 2 sources

                                        if (!BlockUtils.collideBlock(thePlayer.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
                                        }) || !thePlayer.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.AIR)) || thePlayer.isSneaking()) ** break;
                                        thePlayer.setMotionY(0.08);
                                        ** break;
                                    }
                                }
                                blockPos = thePlayer.getPosition().down();
                                if (thePlayer.getOnGround()) break block29;
                                $i$f$getBlock = false;
                                v2 = MinecraftInstance.mc.getTheWorld();
                                if (Intrinsics.areEqual(v2 != null && (v2 = v2.getBlockState(blockPos)) != null ? v2.getBlock() : null, MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER))) break block30;
                            }
                            if (!thePlayer.isInWater()) break block31;
                        }
                        if (!thePlayer.getSprinting()) {
                            v3 = thePlayer;
                            v3.setMotionX(v3.getMotionX() * 0.99999);
                            v4 = thePlayer;
                            v4.setMotionY(v4.getMotionY() * 0.0);
                            v5 = thePlayer;
                            v5.setMotionZ(v5.getMotionZ() * 0.99999);
                            if (thePlayer.isCollidedHorizontally()) {
                                thePlayer.setMotionY((float)((int)(thePlayer.getPosY() - (double)((int)(thePlayer.getPosY() - (double)true)))) / 8.0f);
                            }
                        } else {
                            v6 = thePlayer;
                            v6.setMotionX(v6.getMotionX() * 0.99999);
                            v7 = thePlayer;
                            v7.setMotionY(v7.getMotionY() * 0.0);
                            v8 = thePlayer;
                            v8.setMotionZ(v8.getMotionZ() * 0.99999);
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
                    v9 = thePlayer;
                    v9.setMotionY(v9.getMotionY() + 0.15);
                    return;
                }
                blockPos$iv = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)true, thePlayer.getPosZ());
                $i$f$getBlock = false;
                v10 = MinecraftInstance.mc.getTheWorld();
                block = v10 != null && (v10 = v10.getBlockState(blockPos$iv)) != null ? v10.getBlock() : null;
                blockPos$iv = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 1.1, thePlayer.getPosZ());
                $i$f$getBlock = false;
                v11 = MinecraftInstance.mc.getTheWorld();
                v12 = v11 != null && (v11 = v11.getBlockState(blockPos$iv)) != null ? v11.getBlock() : (blockUp = null);
                if (MinecraftInstance.classProvider.isBlockLiquid(blockUp)) {
                    thePlayer.setMotionY(0.1);
                } else if (MinecraftInstance.classProvider.isBlockLiquid(block)) {
                    thePlayer.setMotionY(0.0);
                }
                thePlayer.setOnGround(true);
                v13 = thePlayer;
                v13.setMotionX(v13.getMotionX() * 1.085);
                v14 = thePlayer;
                v14.setMotionZ(v14.getMotionZ() * 1.085);
                ** break;
            }
            if (!thePlayer.isInWater()) ** break;
            v15 = thePlayer;
            v15.setMotionX(v15.getMotionX() * 1.17);
            v16 = thePlayer;
            v16.setMotionZ(v16.getMotionZ() * 1.17);
            if (thePlayer.isCollidedHorizontally()) {
                thePlayer.setMotionY(0.24);
                ** break;
            }
            v17 = MinecraftInstance.mc.getTheWorld();
            if (v17 == null) {
                Intrinsics.throwNpe();
            }
            if (!(Intrinsics.areEqual(v17.getBlockState(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 1.0, thePlayer.getPosZ())).getBlock(), MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)) ^ true)) ** break;
            v18 = thePlayer;
            v18.setMotionY(v18.getMotionY() + 0.04);
            ** break;
        }
        if (!thePlayer.isInWater()) ** break;
        v19 = thePlayer;
        v19.setMotionY(v19.getMotionY() + 0.03999999910593033);
        ** break;
lbl114:
        // 19 sources

    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.modeValue.get();
        String string2 = "aacfly";
        boolean bl = false;
        String string3 = string;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
        String string5 = string4;
        if (Intrinsics.areEqual(string2, string5)) {
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
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        block8: {
            Intrinsics.checkParameterIsNotNull(event, "event");
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            if (!MinecraftInstance.classProvider.isBlockLiquid(event.getBlock())) break block8;
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            if (BlockUtils.collideBlock(v0.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
            v3 = v2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v3, "(this as java.lang.String).toLowerCase()");
            var2_2 = v3;
            switch (var2_2.hashCode()) {
                case 108891: {
                    if (!var2_2.equals("ncp")) break;
                    ** GOTO lbl27
                }
                case 233102203: {
                    if (!var2_2.equals("vanilla")) break;
lbl27:
                    // 2 sources

                    event.setBoundingBox(MinecraftInstance.classProvider.createAxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + (double)true, (double)event.getY() + (double)true, (double)event.getZ() + (double)true));
                    break;
                }
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || !StringsKt.equals((String)this.modeValue.get(), "NCP", true)) {
            return;
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(event.getPacket())) {
            ICPacketPlayer packetPlayer = event.getPacket().asCPacketPlayer();
            if (BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(thePlayer.getEntityBoundingBox().getMaxX(), thePlayer.getEntityBoundingBox().getMaxY(), thePlayer.getEntityBoundingBox().getMaxZ(), thePlayer.getEntityBoundingBox().getMinX(), thePlayer.getEntityBoundingBox().getMinY() - 0.01, thePlayer.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
            })) {
                boolean bl = this.nextTick = !this.nextTick;
                if (this.nextTick) {
                    ICPacketPlayer iCPacketPlayer = packetPlayer;
                    iCPacketPlayer.setY(iCPacketPlayer.getY() - 0.001);
                }
            }
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        IBlock block;
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        WBlockPos blockPos$iv = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 0.01, thePlayer.getPosZ());
        boolean $i$f$getBlock = false;
        Object object = MinecraftInstance.mc.getTheWorld();
        IBlock iBlock = object != null && (object = object.getBlockState(blockPos$iv)) != null ? object.getBlock() : (block = null);
        if (((Boolean)this.noJumpValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockLiquid(block)) {
            event.cancelEvent();
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
