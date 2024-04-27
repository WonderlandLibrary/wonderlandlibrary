/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.DoubleCompanionObject
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.Reflection
 *  kotlin.reflect.KDeclarationContainer
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.concurrent.LinkedBlockingQueue;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoFall", description="Prevents you from taking fall damage.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u001c\u001a\u00020\u0006J\u0016\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001fJ\u0006\u0010!\u001a\u00020\u000bJ\u0006\u0010\"\u001a\u00020\u000bJ\b\u0010#\u001a\u00020$H\u0016J\u0012\u0010%\u001a\u00020$2\b\u0010&\u001a\u0004\u0018\u00010'H\u0007J\u0010\u0010(\u001a\u00020$2\u0006\u0010&\u001a\u00020)H\u0003J\u0010\u0010*\u001a\u00020$2\u0006\u0010&\u001a\u00020+H\u0007J\u0010\u0010,\u001a\u00020$2\u0006\u0010&\u001a\u00020-H\u0007J\u0012\u0010.\u001a\u00020$2\b\u0010&\u001a\u0004\u0018\u00010/H\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u00128\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00198VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001b\u00a8\u00060"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/NoFall;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "currentMlgBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "currentMlgItemIndex", "", "currentMlgRotation", "Lnet/ccbluex/liquidbounce/utils/VecRotation;", "currentState", "fakelag", "", "jumped", "minFallDistance", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "mlgTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "packetmodify", "packets", "Ljava/util/concurrent/LinkedBlockingQueue;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "spartanTimer", "tag", "", "getTag", "()Ljava/lang/String;", "getJumpEffect", "inAir", "height", "", "plus", "inVoid", "isBlockUnder", "onEnable", "", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotionUpdate", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class NoFall
extends Module {
    @JvmField
    @NotNull
    public final ListValue modeValue = new ListValue("Mode", new String[]{"AAC4", "SpoofGround", "NoGround", "Packet", "MLG", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft", "Hypixel"}, "SpoofGround");
    public final FloatValue minFallDistance = new FloatValue("MinMLGHeight", 5.0f, 2.0f, 50.0f);
    public final TickTimer spartanTimer = new TickTimer();
    public final TickTimer mlgTimer = new TickTimer();
    public int currentState;
    public boolean jumped;
    public VecRotation currentMlgRotation;
    public int currentMlgItemIndex;
    public WBlockPos currentMlgBlock;
    public boolean fakelag;
    public boolean packetmodify;
    public final LinkedBlockingQueue<IPacket> packets = new LinkedBlockingQueue();

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"AAC4", (boolean)true)) {
            this.fakelag = 0;
            this.packetmodify = 0;
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onUpdate(@Nullable UpdateEvent event) {
        String string;
        if (MinecraftInstance.mc.getThePlayer().getOnGround()) {
            this.jumped = 0;
        }
        if (MinecraftInstance.mc.getThePlayer().getMotionY() > (double)0) {
            this.jumped = 1;
        }
        if (!this.getState()) {
            return;
        }
        if (BlockUtils.collideBlock(MinecraftInstance.mc.getThePlayer().getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
        })) || BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMaxX(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMaxY(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMaxZ(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinX(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinY() - 0.01, MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
            return;
        }
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        block9 : switch (string3) {
            case "packet": {
                if (!(MinecraftInstance.mc.getThePlayer().getFallDistance() > 2.0f)) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case "cubecraft": {
                if (!(MinecraftInstance.mc.getThePlayer().getFallDistance() > 2.0f)) break;
                MinecraftInstance.mc.getThePlayer().setOnGround(false);
                MinecraftInstance.mc.getThePlayer().getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case "aac": {
                if (MinecraftInstance.mc.getThePlayer().getFallDistance() > 2.0f) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                    this.currentState = 2;
                } else if (this.currentState == 2 && MinecraftInstance.mc.getThePlayer().getFallDistance() < (float)2) {
                    MinecraftInstance.mc.getThePlayer().setMotionY(0.1);
                    this.currentState = 3;
                    return;
                }
                switch (this.currentState) {
                    case 3: {
                        MinecraftInstance.mc.getThePlayer().setMotionY(0.1);
                        this.currentState = 4;
                        break block9;
                    }
                    case 4: {
                        MinecraftInstance.mc.getThePlayer().setMotionY(0.1);
                        this.currentState = 5;
                        break block9;
                    }
                    case 5: {
                        MinecraftInstance.mc.getThePlayer().setMotionY(0.1);
                        this.currentState = 1;
                        break block9;
                    }
                }
                break;
            }
            case "laac": {
                if (this.jumped || !MinecraftInstance.mc.getThePlayer().getOnGround() || MinecraftInstance.mc.getThePlayer().isOnLadder() || MinecraftInstance.mc.getThePlayer().isInWater() || MinecraftInstance.mc.getThePlayer().isInWeb()) break;
                MinecraftInstance.mc.getThePlayer().setMotionY(-6);
                break;
            }
            case "aac3.3.11": {
                if (!(MinecraftInstance.mc.getThePlayer().getFallDistance() > (float)2)) break;
                MinecraftInstance.mc.getThePlayer().setMotionZ(0.0);
                MinecraftInstance.mc.getThePlayer().setMotionX(MinecraftInstance.mc.getThePlayer().getMotionZ());
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY() - 0.001, MinecraftInstance.mc.getThePlayer().getPosZ(), MinecraftInstance.mc.getThePlayer().getOnGround()));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(true));
                break;
            }
            case "aac3.3.15": {
                if (!(MinecraftInstance.mc.getThePlayer().getFallDistance() > (float)2)) break;
                if (!MinecraftInstance.mc.isIntegratedServerRunning()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), DoubleCompanionObject.INSTANCE.getNaN(), MinecraftInstance.mc.getThePlayer().getPosZ(), false));
                }
                MinecraftInstance.mc.getThePlayer().setFallDistance(-9999);
                break;
            }
            case "spartan": {
                this.spartanTimer.update();
                if (!((double)MinecraftInstance.mc.getThePlayer().getFallDistance() > 1.5) || !this.spartanTimer.hasTimePassed(10)) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY() + (double)10, MinecraftInstance.mc.getThePlayer().getPosZ(), true));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY() - (double)10, MinecraftInstance.mc.getThePlayer().getPosZ(), true));
                this.spartanTimer.reset();
                break;
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (MinecraftInstance.mc.getThePlayer().isInWater()) {
            return;
        }
        IPacket packet = event.getPacket();
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals((String)mode, (String)"AAC4", (boolean)true) && MinecraftInstance.classProvider.isCPacketPlayer(packet) && this.fakelag) {
            event.cancelEvent();
            if (this.packetmodify) {
                packet.asCPacketPlayer().setOnGround(true);
                this.packetmodify = 0;
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
            if (StringsKt.equals((String)mode, (String)"Hypixel", (boolean)true) && MinecraftInstance.mc.getThePlayer() != null && (double)MinecraftInstance.mc.getThePlayer().getFallDistance() > 1.5) {
                playerPacket.setOnGround(MinecraftInstance.mc.getThePlayer().getTicksExisted() % 2 == 0);
            }
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (BlockUtils.collideBlock(MinecraftInstance.mc.getThePlayer().getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
        })) || BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMaxX(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMaxY(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMaxZ(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinX(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinY() - 0.01, MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

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
            return;
        }
        if (!(!StringsKt.equals((String)((String)this.modeValue.get()), (String)"laac", (boolean)true) || this.jumped || MinecraftInstance.mc.getThePlayer().getOnGround() || MinecraftInstance.mc.getThePlayer().isOnLadder() || MinecraftInstance.mc.getThePlayer().isInWater() || MinecraftInstance.mc.getThePlayer().isInWeb() || !(MinecraftInstance.mc.getThePlayer().getMotionY() < 0.0))) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onMotionUpdate(MotionEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
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

    /*
     * WARNING - void declaration
     */
    public final boolean isBlockUnder() {
        if (MinecraftInstance.mc.getThePlayer().getPosY() < 0.0) {
            return false;
        }
        void off;
        while (off < (int)MinecraftInstance.mc.getThePlayer().getPosY() + 2) {
            IAxisAlignedBB bb = MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().offset(0.0, (double)(-off), 0.0);
            if (!MinecraftInstance.mc.getTheWorld().getCollidingBoundingBoxes(MinecraftInstance.mc.getThePlayer(), bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }

    public final int getJumpEffect() {
        return MinecraftInstance.mc.getThePlayer().isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP)) ? MinecraftInstance.mc.getThePlayer().getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.JUMP)).getAmplifier() + 1 : 0;
    }

    /*
     * WARNING - void declaration
     */
    public final boolean inVoid() {
        void off;
        if (MinecraftInstance.mc.getThePlayer().getPosY() < (double)0) {
            return false;
        }
        while ((double)off < MinecraftInstance.mc.getThePlayer().getPosY() + (double)2) {
            IAxisAlignedBB bb = MinecraftInstance.classProvider.createAxisAlignedBB(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY(), MinecraftInstance.mc.getThePlayer().getPosZ(), MinecraftInstance.mc.getThePlayer().getPosX(), (double)off, MinecraftInstance.mc.getThePlayer().getPosZ());
            if (!MinecraftInstance.mc.getTheWorld().getCollidingBoundingBoxes(MinecraftInstance.mc.getThePlayer(), bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public final boolean inAir(double height, double plus) {
        void off;
        if (MinecraftInstance.mc.getThePlayer().getPosY() < (double)0) {
            return false;
        }
        while ((double)off < height) {
            IAxisAlignedBB bb = MinecraftInstance.classProvider.createAxisAlignedBB(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY(), MinecraftInstance.mc.getThePlayer().getPosZ(), MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY() - (double)off, MinecraftInstance.mc.getThePlayer().getPosZ());
            if (!MinecraftInstance.mc.getTheWorld().getCollidingBoundingBoxes(MinecraftInstance.mc.getThePlayer(), bb).isEmpty()) {
                return true;
            }
            off += (int)plus;
        }
        return false;
    }

    @EventTarget(ignoreCondition=true)
    public final void onJump(@Nullable JumpEvent event) {
        this.jumped = 1;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

