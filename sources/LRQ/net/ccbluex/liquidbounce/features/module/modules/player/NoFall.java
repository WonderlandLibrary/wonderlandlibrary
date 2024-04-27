/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.DoubleCompanionObject
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.Reflection
 *  kotlin.reflect.KDeclarationContainer
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.concurrent.LinkedBlockingQueue;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoFall", description="Prevents you from taking fall damage.", category=ModuleCategory.PLAYER)
public final class NoFall
extends Module {
    @JvmField
    public final ListValue modeValue = new ListValue("Mode", new String[]{"AAC4", "SpoofGround", "NoGround", "Packet", "MLG", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft", "Hypixel"}, "SpoofGround");
    private final FloatValue minFallDistance = new FloatValue("MinMLGHeight", 5.0f, 2.0f, 50.0f);
    private final TickTimer spartanTimer = new TickTimer();
    private final TickTimer mlgTimer = new TickTimer();
    private int currentState;
    private boolean jumped;
    private VecRotation currentMlgRotation;
    private int currentMlgItemIndex;
    private WBlockPos currentMlgBlock;
    private boolean fakelag;
    private boolean packetmodify;
    private final LinkedBlockingQueue<IPacket> packets = new LinkedBlockingQueue();

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC4", (boolean)true)) {
            this.fakelag = false;
            this.packetmodify = false;
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onUpdate(@Nullable UpdateEvent event) {
        block84: {
            block83: {
                block82: {
                    block81: {
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iEntityPlayerSP.getOnGround()) {
                            this.jumped = false;
                        }
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iEntityPlayerSP2.getMotionY() > 0.0) {
                            this.jumped = true;
                        }
                        if (!this.getState()) break block81;
                        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(FreeCam.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) break block82;
                    }
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (BlockUtils.collideBlock(iEntityPlayerSP.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
                }))) break block83;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP3.getEntityBoundingBox().getMaxX();
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP4.getEntityBoundingBox().getMaxY();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = iEntityPlayerSP5.getEntityBoundingBox().getMaxZ();
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                double d4 = iEntityPlayerSP6.getEntityBoundingBox().getMinX();
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityPlayerSP7.getEntityBoundingBox().getMinY() - 0.01;
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP8.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
                }))) break block84;
            }
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "packet": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getFallDistance() > 2.0f)) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case "cubecraft": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getFallDistance() > 2.0f)) break;
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP9.setOnGround(false);
                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP10 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP10.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case "aac": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getFallDistance() > 2.0f) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                    this.currentState = 2;
                } else if (this.currentState == 2) {
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP11.getFallDistance() < (float)2) {
                        IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP12 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP12.setMotionY(0.1);
                        this.currentState = 3;
                        return;
                    }
                }
                switch (this.currentState) {
                    case 3: {
                        IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP13 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP13.setMotionY(0.1);
                        this.currentState = 4;
                        break;
                    }
                    case 4: {
                        IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP14 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP14.setMotionY(0.1);
                        this.currentState = 5;
                        break;
                    }
                    case 5: {
                        IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP15 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP15.setMotionY(0.1);
                        this.currentState = 1;
                    }
                }
                break;
            }
            case "laac": {
                if (this.jumped) break;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP16 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP16.isOnLadder()) break;
                IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP17 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP17.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP18 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP18.isInWeb()) break;
                IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP19 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP19.setMotionY(-6);
                break;
            }
            case "aac3.3.11": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getFallDistance() > (float)2)) break;
                IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP20 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP20.setMotionZ(0.0);
                IEntityPlayerSP iEntityPlayerSP21 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP21 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP22 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP22 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP21.setMotionX(iEntityPlayerSP22.getMotionZ());
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP23 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP23.getPosX();
                IEntityPlayerSP iEntityPlayerSP24 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP24 == null) {
                    Intrinsics.throwNpe();
                }
                double d6 = iEntityPlayerSP24.getPosY() - 0.001;
                IEntityPlayerSP iEntityPlayerSP25 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP25 == null) {
                    Intrinsics.throwNpe();
                }
                double d7 = iEntityPlayerSP25.getPosZ();
                IEntityPlayerSP iEntityPlayerSP26 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP26 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d6, d7, iEntityPlayerSP26.getOnGround()));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case "aac3.3.15": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getFallDistance() > (float)2)) break;
                if (!MinecraftInstance.mc.isIntegratedServerRunning()) {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP27 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP27 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP27.getPosX();
                    double d8 = DoubleCompanionObject.INSTANCE.getNaN();
                    IEntityPlayerSP iEntityPlayerSP28 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP28 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d8, iEntityPlayerSP28.getPosZ(), false));
                }
                IEntityPlayerSP iEntityPlayerSP29 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP29 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP29.setFallDistance(-9999);
                break;
            }
            case "spartan": {
                this.spartanTimer.update();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!((double)iEntityPlayerSP.getFallDistance() > 1.5) || !this.spartanTimer.hasTimePassed(10)) break;
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP30 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP30 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP30.getPosX();
                IEntityPlayerSP iEntityPlayerSP31 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP31 == null) {
                    Intrinsics.throwNpe();
                }
                double d9 = iEntityPlayerSP31.getPosY() + (double)10;
                IEntityPlayerSP iEntityPlayerSP32 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP32 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d9, iEntityPlayerSP32.getPosZ(), true));
                IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP33 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP33 == null) {
                    Intrinsics.throwNpe();
                }
                double d10 = iEntityPlayerSP33.getPosX();
                IEntityPlayerSP iEntityPlayerSP34 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP34 == null) {
                    Intrinsics.throwNpe();
                }
                double d11 = iEntityPlayerSP34.getPosY() - (double)10;
                IEntityPlayerSP iEntityPlayerSP35 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP35 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d10, d11, iEntityPlayerSP35.getPosZ(), true));
                this.spartanTimer.reset();
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInWater()) {
            return;
        }
        IPacket packet = event.getPacket();
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals((String)mode, (String)"AAC4", (boolean)true) && MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.fakelag) {
            event.cancelEvent();
            if (this.packetmodify) {
                packet.asCPacketPlayer().setOnGround(true);
                this.packetmodify = false;
            }
            this.packets.add(packet);
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet)) {
            ICPacketPlayer playerPacket = packet.asCPacketPlayer();
            if (StringsKt.equals((String)mode, (String)"SpoofGround", (boolean)true)) {
                playerPacket.setOnGround(true);
            }
            if (StringsKt.equals((String)mode, (String)"NoGround", (boolean)true)) {
                playerPacket.setOnGround(false);
            }
            if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) && MinecraftInstance.mc.getThePlayer() != null) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)iEntityPlayerSP2.getFallDistance() > 1.5) {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    playerPacket.setOnGround(iEntityPlayerSP3.getTicksExisted() % 2 == 0);
                }
            }
        }
    }

    @EventTarget
    public final void onMove(MoveEvent event) {
        block22: {
            block21: {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (BlockUtils.collideBlock(iEntityPlayerSP.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
                }))) break block21;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP2.getEntityBoundingBox().getMaxX();
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP3.getEntityBoundingBox().getMaxY();
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = iEntityPlayerSP4.getEntityBoundingBox().getMaxZ();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                double d4 = iEntityPlayerSP5.getEntityBoundingBox().getMinX();
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityPlayerSP6.getEntityBoundingBox().getMinY() - 0.01;
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if (!BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP7.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
                }))) break block22;
            }
            return;
        }
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"laac", (boolean)true) && !this.jumped) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP8.isOnLadder()) {
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP9.isInWater()) {
                        IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP10 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!iEntityPlayerSP10.isInWeb()) {
                            IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP11 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (iEntityPlayerSP11.getMotionY() < 0.0) {
                                event.setX(0.0);
                                event.setZ(0.0);
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    private final void onMotionUpdate(MotionEvent event) {
        EventState eventState;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInWater()) {
            return;
        }
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC4", (boolean)true) && (eventState = event.getEventState()) == EventState.PRE) {
            if (!this.inVoid()) {
                if (this.fakelag) {
                    this.fakelag = false;
                    if (this.packets.size() > 0) {
                        for (IPacket packet : this.packets) {
                            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP2 == null) {
                                Intrinsics.throwNpe();
                            }
                            iEntityPlayerSP2.getSendQueue().addToSendQueue(packet);
                        }
                        this.packets.clear();
                    }
                }
                return;
            }
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP3.getOnGround() && this.fakelag) {
                this.fakelag = false;
                if (this.packets.size() > 0) {
                    for (IPacket packet : this.packets) {
                        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP4 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP4.getSendQueue().addToSendQueue(packet);
                    }
                    this.packets.clear();
                }
                return;
            }
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP5.getFallDistance() > (float)3 && this.fakelag) {
                this.packetmodify = true;
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.setFallDistance(0.0f);
            }
            if (this.inAir(4.0, 1.0)) {
                return;
            }
            if (!this.fakelag) {
                this.fakelag = true;
            }
        }
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"MLG", (boolean)true)) {
            if (event.getEventState() == EventState.PRE) {
                this.currentMlgRotation = null;
                this.mlgTimer.update();
                if (!this.mlgTimer.hasTimePassed(10)) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP7.getFallDistance() > ((Number)this.minFallDistance.get()).floatValue()) {
                    void z$iv;
                    void y$iv;
                    void x$iv22;
                    void this_$iv22;
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP8.getPosX();
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d2 = iEntityPlayerSP9.getPosY();
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP10.getPosZ();
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = iEntityPlayerSP11.getMotionX();
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d5 = iEntityPlayerSP12.getMotionY();
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d6 = iEntityPlayerSP13.getMotionZ();
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    float f = iEntityPlayerSP14.getRotationYaw();
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    float f2 = iEntityPlayerSP15.getMoveStrafing();
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    FallingPlayer fallingPlayer = new FallingPlayer(d, d2, d3, d4, d5, d6, f, f2, iEntityPlayerSP16.getMoveForward());
                    double maxDist = (double)MinecraftInstance.mc.getPlayerController().getBlockReachDistance() + 1.5;
                    IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP17 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d7 = 1.0 / iEntityPlayerSP17.getMotionY() * -maxDist;
                    Object object = fallingPlayer;
                    boolean bl = false;
                    double d8 = Math.ceil(d7);
                    FallingPlayer.CollisionResult collisionResult = ((FallingPlayer)object).findCollision((int)d8);
                    if (collisionResult == null) {
                        return;
                    }
                    FallingPlayer.CollisionResult collision = collisionResult;
                    IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP18 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d9 = iEntityPlayerSP18.getPosX();
                    IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP19 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d10 = iEntityPlayerSP19.getPosY();
                    IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP20 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d11 = d10 + (double)iEntityPlayerSP20.getEyeHeight();
                    IEntityPlayerSP iEntityPlayerSP21 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP21 == null) {
                        Intrinsics.throwNpe();
                    }
                    WVec3 wVec3 = new WVec3(collision.getPos());
                    double d12 = 0.5;
                    double d13 = 0.5;
                    double d14 = 0.5;
                    object = new WVec3(d9, d11, iEntityPlayerSP21.getPosZ());
                    boolean $i$f$addVector = false;
                    WVec3 wVec32 = new WVec3(this_$iv22.getXCoord() + x$iv22, this_$iv22.getYCoord() + y$iv, this_$iv22.getZCoord() + z$iv);
                    double this_$iv22 = 0.75;
                    double d15 = MinecraftInstance.mc.getPlayerController().getBlockReachDistance();
                    double d16 = ((WVec3)object).distanceTo(wVec32);
                    int n = 0;
                    double d17 = Math.sqrt(this_$iv22);
                    boolean ok = d16 < d15 + d17;
                    IEntityPlayerSP iEntityPlayerSP22 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP22 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d18 = iEntityPlayerSP22.getMotionY();
                    double d19 = collision.getPos().getY() + 1;
                    IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP23 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (d18 < d19 - iEntityPlayerSP23.getPosY()) {
                        ok = true;
                    }
                    if (!ok) {
                        return;
                    }
                    int index = -1;
                    int x$iv22 = 36;
                    n = 44;
                    while (x$iv22 <= n) {
                        IItem iItem;
                        void i;
                        IItemStack itemStack;
                        IEntityPlayerSP iEntityPlayerSP24 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP24 == null) {
                            Intrinsics.throwNpe();
                        }
                        if ((itemStack = iEntityPlayerSP24.getInventoryContainer().getSlot((int)i).getStack()) != null && (itemStack.getItem().equals(MinecraftInstance.classProvider.getItemEnum(ItemType.WATER_BUCKET)) || MinecraftInstance.classProvider.isItemBlock(itemStack.getItem()) && ((iItem = itemStack.getItem()) != null && (iItem = iItem.asItemBlock()) != null ? iItem.getBlock() : null).equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.WEB)))) {
                            index = i - 36;
                            IEntityPlayerSP iEntityPlayerSP25 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP25 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (iEntityPlayerSP25.getInventory().getCurrentItem() == index) break;
                        }
                        ++i;
                    }
                    if (index == -1) {
                        return;
                    }
                    this.currentMlgItemIndex = index;
                    this.currentMlgBlock = collision.getPos();
                    IEntityPlayerSP iEntityPlayerSP26 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP26 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP26.getInventory().getCurrentItem() != index) {
                        IEntityPlayerSP iEntityPlayerSP27 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP27 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP27.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(index));
                    }
                    VecRotation vecRotation = this.currentMlgRotation = RotationUtils.faceBlock(collision.getPos());
                    if (vecRotation == null) {
                        Intrinsics.throwNpe();
                    }
                    Rotation rotation = vecRotation.getRotation();
                    IEntityPlayerSP iEntityPlayerSP28 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP28 == null) {
                        Intrinsics.throwNpe();
                    }
                    rotation.toPlayer(iEntityPlayerSP28);
                }
            } else if (this.currentMlgRotation != null) {
                IItemStack stack;
                IEntityPlayerSP iEntityPlayerSP29 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP29 == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack iItemStack = stack = iEntityPlayerSP29.getInventory().getStackInSlot(this.currentMlgItemIndex + 36);
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemBucket(iItemStack.getItem())) {
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    IEntityPlayerSP iEntityPlayerSP30 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP30 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayer iEntityPlayer = iEntityPlayerSP30;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    iPlayerControllerMP.sendUseItem(iEntityPlayer, iWorldClient, stack);
                } else {
                    WVec3i dirVec = MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.UP).getDirectionVec();
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    IEntityPlayerSP iEntityPlayerSP31 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP31 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayer iEntityPlayer = iEntityPlayerSP31;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iPlayerControllerMP.sendUseItem(iEntityPlayer, iWorldClient, stack)) {
                        this.mlgTimer.reset();
                    }
                }
                IEntityPlayerSP iEntityPlayerSP32 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP32 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP32.getInventory().getCurrentItem() != this.currentMlgItemIndex) {
                    IEntityPlayerSP iEntityPlayerSP33 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP33 == null) {
                        Intrinsics.throwNpe();
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = iEntityPlayerSP33.getSendQueue();
                    IEntityPlayerSP iEntityPlayerSP34 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP34 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP34.getInventory().getCurrentItem()));
                }
            }
        }
    }

    public final boolean isBlockUnder() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while (true) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (off >= (int)iEntityPlayerSP2.getPosY() + 2) {
                return false;
            }
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = iEntityPlayerSP3.getEntityBoundingBox().offset(0.0, -off, 0.0);
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP4, bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
    }

    public final int getJumpEffect() {
        int n;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP))) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IPotionEffect iPotionEffect = iEntityPlayerSP2.getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP));
            if (iPotionEffect == null) {
                Intrinsics.throwNpe();
            }
            n = iPotionEffect.getAmplifier() + 1;
        } else {
            n = 0;
        }
        return n;
    }

    public final boolean inVoid() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while (true) {
            double d = off;
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (!(d < iEntityPlayerSP2.getPosY() + (double)2)) break;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            double d2 = iEntityPlayerSP3.getPosX();
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            double d3 = iEntityPlayerSP4.getPosY();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d4 = iEntityPlayerSP5.getPosZ();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            double d5 = iEntityPlayerSP6.getPosX();
            double d6 = off;
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = MinecraftInstance.classProvider.createAxisAlignedBB(d2, d3, d4, d5, d6, iEntityPlayerSP7.getPosZ());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP8, bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }

    public final boolean inAir(double height, double plus) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int off = 0;
        while ((double)off < height) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP2.getPosX();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            double d2 = iEntityPlayerSP3.getPosY();
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            double d3 = iEntityPlayerSP4.getPosZ();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            double d4 = iEntityPlayerSP5.getPosX();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            double d5 = iEntityPlayerSP6.getPosY() - (double)off;
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB bb = MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP7.getPosZ());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP8, bb).isEmpty()) {
                return true;
            }
            off += (int)plus;
        }
        return false;
    }

    @EventTarget(ignoreCondition=true)
    public final void onJump(@Nullable JumpEvent event) {
        this.jumped = true;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

