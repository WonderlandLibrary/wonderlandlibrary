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
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
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
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoFall", description="Prevents you from taking fall damage.", category=ModuleCategory.PLAYER)
public final class NoFall
extends Module {
    private final TickTimer mlgTimer;
    private VecRotation currentMlgRotation;
    private final FloatValue minFallDistance;
    private final LinkedBlockingQueue packets;
    private int currentState;
    private int currentMlgItemIndex;
    private WBlockPos currentMlgBlock;
    private boolean fakelag;
    private final TickTimer spartanTimer;
    private boolean jumped;
    private boolean packetmodify;
    @JvmField
    public final ListValue modeValue = new ListValue("Mode", new String[]{"AAC4", "SpoofGround", "NoGround", "Packet", "MLG", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft", "Hypixel"}, "SpoofGround");

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

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        block22: {
            block21: {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (BlockUtils.collideBlock(iEntityPlayerSP.getEntityBoundingBox(), new Function1(MinecraftInstance.classProvider){

                    public Object invoke(Object object) {
                        return this.invoke(object);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }

                    static {
                    }

                    public final boolean invoke(@Nullable Object object) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(object);
                    }
                })) break block21;
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
                if (!BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP7.getEntityBoundingBox().getMinZ()), new Function1(MinecraftInstance.classProvider){

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    static {
                    }

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }

                    public Object invoke(Object object) {
                        return this.invoke(object);
                    }

                    public final boolean invoke(@Nullable Object object) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(object);
                    }
                })) break block22;
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
                                moveEvent.setX(0.0);
                                moveEvent.setZ(0.0);
                            }
                        }
                    }
                }
            }
        }
    }

    public NoFall() {
        this.minFallDistance = new FloatValue("MinMLGHeight", 5.0f, 2.0f, 50.0f);
        this.spartanTimer = new TickTimer();
        this.mlgTimer = new TickTimer();
        this.packets = new LinkedBlockingQueue();
    }

    @EventTarget(ignoreCondition=true)
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        block79: {
            block78: {
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
                if (!this.getState() || LiquidBounce.INSTANCE.getModuleManager().getModule(FreeCam.class).getState()) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (BlockUtils.collideBlock(iEntityPlayerSP3.getEntityBoundingBox(), new Function1(MinecraftInstance.classProvider){

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }

                    public final boolean invoke(@Nullable Object object) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(object);
                    }

                    static {
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }

                    public Object invoke(Object object) {
                        return this.invoke(object);
                    }
                })) break block78;
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP4.getEntityBoundingBox().getMaxX();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP5.getEntityBoundingBox().getMaxY();
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = iEntityPlayerSP6.getEntityBoundingBox().getMaxZ();
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                double d4 = iEntityPlayerSP7.getEntityBoundingBox().getMinX();
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityPlayerSP8.getEntityBoundingBox().getMinY() - 0.01;
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                if (!BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(d, d2, d3, d4, d5, iEntityPlayerSP9.getEntityBoundingBox().getMinZ()), new Function1(MinecraftInstance.classProvider){

                    public final KDeclarationContainer getOwner() {
                        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
                    }

                    static {
                    }

                    public Object invoke(Object object) {
                        return this.invoke(object);
                    }

                    public final String getSignature() {
                        return "isBlockLiquid(Ljava/lang/Object;)Z";
                    }

                    public final boolean invoke(@Nullable Object object) {
                        return ((IClassProvider)this.receiver).isBlockLiquid(object);
                    }

                    public final String getName() {
                        return "isBlockLiquid";
                    }
                })) break block79;
            }
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        block9 : switch (string2.toLowerCase()) {
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
                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP10 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP10.setOnGround(false);
                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP11 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP11.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
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
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP12.getFallDistance() < (float)2) {
                        IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP13 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP13.setMotionY(0.1);
                        this.currentState = 3;
                        return;
                    }
                }
                switch (this.currentState) {
                    case 3: {
                        IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP14 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP14.setMotionY(0.1);
                        this.currentState = 4;
                        break block9;
                    }
                    case 4: {
                        IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP15 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP15.setMotionY(0.1);
                        this.currentState = 5;
                        break block9;
                    }
                    case 5: {
                        IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP16 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP16.setMotionY(0.1);
                        this.currentState = 1;
                        break block9;
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
                IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP17 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP17.isOnLadder()) break;
                IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP18 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP18.isInWater()) break;
                IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP19 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP19.isInWeb()) break;
                IEntityPlayerSP iEntityPlayerSP20 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP20 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP20.setMotionY(-6);
                break;
            }
            case "aac3.3.11": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getFallDistance() > (float)2)) break;
                IEntityPlayerSP iEntityPlayerSP21 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP21 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP21.setMotionZ(0.0);
                IEntityPlayerSP iEntityPlayerSP22 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP22 == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP23 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP23 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP22.setMotionX(iEntityPlayerSP23.getMotionZ());
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP24 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP24 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP24.getPosX();
                IEntityPlayerSP iEntityPlayerSP25 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP25 == null) {
                    Intrinsics.throwNpe();
                }
                double d6 = iEntityPlayerSP25.getPosY() - 0.001;
                IEntityPlayerSP iEntityPlayerSP26 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP26 == null) {
                    Intrinsics.throwNpe();
                }
                double d7 = iEntityPlayerSP26.getPosZ();
                IEntityPlayerSP iEntityPlayerSP27 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP27 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d6, d7, iEntityPlayerSP27.getOnGround()));
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
                    IEntityPlayerSP iEntityPlayerSP28 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP28 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP28.getPosX();
                    double d8 = DoubleCompanionObject.INSTANCE.getNaN();
                    IEntityPlayerSP iEntityPlayerSP29 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP29 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d8, iEntityPlayerSP29.getPosZ(), false));
                }
                IEntityPlayerSP iEntityPlayerSP30 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP30 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP30.setFallDistance(-9999);
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
                IEntityPlayerSP iEntityPlayerSP31 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP31 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP31.getPosX();
                IEntityPlayerSP iEntityPlayerSP32 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP32 == null) {
                    Intrinsics.throwNpe();
                }
                double d9 = iEntityPlayerSP32.getPosY() + (double)10;
                IEntityPlayerSP iEntityPlayerSP33 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP33 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d9, iEntityPlayerSP33.getPosZ(), true));
                IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP34 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP34 == null) {
                    Intrinsics.throwNpe();
                }
                double d10 = iEntityPlayerSP34.getPosX();
                IEntityPlayerSP iEntityPlayerSP35 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP35 == null) {
                    Intrinsics.throwNpe();
                }
                double d11 = iEntityPlayerSP35.getPosY() - (double)10;
                IEntityPlayerSP iEntityPlayerSP36 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP36 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d10, d11, iEntityPlayerSP36.getPosZ(), true));
                this.spartanTimer.reset();
                break;
            }
        }
    }

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC4", (boolean)true)) {
            this.fakelag = false;
            this.packetmodify = false;
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    private final void onMotionUpdate(MotionEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl134 : ALOAD_0 - null : trying to set 0 previously set to 2
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInWater()) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        String string = (String)this.modeValue.get();
        if (StringsKt.equals((String)string, (String)"AAC4", (boolean)true) && MinecraftInstance.classProvider.isCPacketPlayer(iPacket) && this.fakelag) {
            packetEvent.cancelEvent();
            if (this.packetmodify) {
                iPacket.asCPacketPlayer().setOnGround(true);
                this.packetmodify = false;
            }
            this.packets.add(iPacket);
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(iPacket)) {
            ICPacketPlayer iCPacketPlayer = iPacket.asCPacketPlayer();
            if (StringsKt.equals((String)string, (String)"SpoofGround", (boolean)true)) {
                iCPacketPlayer.setOnGround(true);
            }
            if (StringsKt.equals((String)string, (String)"NoGround", (boolean)true)) {
                iCPacketPlayer.setOnGround(false);
            }
            if (StringsKt.equals((String)string, (String)"Hypixel", (boolean)true) && MinecraftInstance.mc.getThePlayer() != null) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)iEntityPlayerSP2.getFallDistance() > 1.5) {
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    iCPacketPlayer.setOnGround(iEntityPlayerSP3.getTicksExisted() % 2 == 0);
                }
            }
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onJump(@Nullable JumpEvent jumpEvent) {
        this.jumped = true;
    }

    public final boolean isBlockUnder() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getPosY() < 0.0) {
            return false;
        }
        int n = 0;
        while (true) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (n >= (int)iEntityPlayerSP2.getPosY() + 2) {
                return false;
            }
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP3.getEntityBoundingBox().offset(0.0, -n, 0.0);
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP4, iAxisAlignedBB).isEmpty()) {
                return true;
            }
            n += 2;
        }
    }
}

