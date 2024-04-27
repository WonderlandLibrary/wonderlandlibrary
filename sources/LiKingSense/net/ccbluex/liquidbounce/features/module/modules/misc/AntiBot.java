/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AntiBot", description="Prevents KillAura from attacking AntiCheat bots.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 .2\u00020\u0001:\u0001.B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020'H\u0007J\b\u0010(\u001a\u00020$H\u0016J\u0010\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020+H\u0007J\u0012\u0010,\u001a\u00020$2\b\u0010*\u001a\u0004\u0018\u00010-H\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/AntiBot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "air", "", "", "airValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "armorValue", "colorValue", "derpValue", "duplicateInTabValue", "duplicateInWorldValue", "entityIDValue", "ground", "groundValue", "healthValue", "hitted", "invalidGround", "", "invalidGroundValue", "invisible", "livingTimeTicksValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "livingTimeValue", "needHitValue", "pingValue", "playerName", "", "swing", "swingValue", "tabModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tabValue", "wasInvisibleValue", "clearAll", "", "onAttack", "e", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onDisable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Companion", "LiKingSense"})
public final class AntiBot
extends Module {
    public final BoolValue tabValue;
    public final ListValue tabModeValue;
    public final BoolValue entityIDValue;
    public final BoolValue colorValue;
    public final BoolValue livingTimeValue;
    public final IntegerValue livingTimeTicksValue;
    public final BoolValue groundValue;
    public final BoolValue airValue;
    public final BoolValue invalidGroundValue;
    public final BoolValue swingValue;
    public final BoolValue healthValue;
    public final BoolValue derpValue;
    public final BoolValue wasInvisibleValue;
    public final BoolValue armorValue;
    public final BoolValue pingValue;
    public final BoolValue needHitValue;
    public final BoolValue duplicateInWorldValue;
    public final BoolValue duplicateInTabValue;
    public final List<Integer> ground;
    public final List<Integer> air;
    public final Map<Integer, Integer> invalidGround;
    public final List<Integer> swing;
    public final List<Integer> invisible;
    public final List<Integer> hitted;
    public final List<String> playerName;
    public static final Companion Companion = new Companion(null);

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        ISPacketEntity packetEntity;
        IEntity entity;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        AntiBot antiBot = (AntiBot)LiquidBounce.INSTANCE.getModuleManager().getModule(AntiBot.class);
        if (MinecraftInstance.mc.getThePlayer() == null || MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntity(packet) && MinecraftInstance.classProvider.isEntityPlayer(entity = (packetEntity = packet.asSPacketEntity()).getEntity(MinecraftInstance.mc.getTheWorld())) && entity != null) {
            if (packetEntity.getOnGround() && !this.ground.contains(entity.getEntityId())) {
                this.ground.add(entity.getEntityId());
            }
            if (!packetEntity.getOnGround() && !this.air.contains(entity.getEntityId())) {
                this.air.add(entity.getEntityId());
            }
            if (packetEntity.getOnGround()) {
                if (entity.getPrevPosY() != entity.getPosY()) {
                    this.invalidGround.put(entity.getEntityId(), ((Number)this.invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() + 1);
                }
            } else {
                int currentVL = ((Number)this.invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() / 2;
                if (currentVL <= 0) {
                    this.invalidGround.remove(entity.getEntityId());
                } else {
                    this.invalidGround.put(entity.getEntityId(), currentVL);
                }
            }
            if (entity.isInvisible() && !this.invisible.contains(entity.getEntityId())) {
                this.invisible.add(entity.getEntityId());
            }
        }
        if (MinecraftInstance.classProvider.isSPacketAnimation(packet)) {
            ISPacketAnimation packetAnimation = packet.asSPacketAnimation();
            entity = MinecraftInstance.mc.getTheWorld().getEntityByID(packetAnimation.getEntityID());
            if (entity != null && MinecraftInstance.classProvider.isEntityLivingBase(entity) && packetAnimation.getAnimationType() == 0 && !this.swing.contains(entity.getEntityId())) {
                this.swing.add(entity.getEntityId());
            }
        }
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent e) {
        Intrinsics.checkParameterIsNotNull((Object)e, (String)"e");
        IEntity entity = e.getTargetEntity();
        if (entity != null && MinecraftInstance.classProvider.isEntityLivingBase(entity) && !this.hitted.contains(entity.getEntityId())) {
            this.hitted.add(entity.getEntityId());
        }
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    public final void clearAll() {
        this.hitted.clear();
        this.swing.clear();
        this.ground.clear();
        this.invalidGround.clear();
        this.invisible.clear();
    }

    /*
     * Exception decompiling
     */
    public AntiBot() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl54 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
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

    public static final /* synthetic */ BoolValue access$getColorValue$p(AntiBot $this) {
        return $this.colorValue;
    }

    public static final /* synthetic */ BoolValue access$getLivingTimeValue$p(AntiBot $this) {
        return $this.livingTimeValue;
    }

    public static final /* synthetic */ IntegerValue access$getLivingTimeTicksValue$p(AntiBot $this) {
        return $this.livingTimeTicksValue;
    }

    public static final /* synthetic */ BoolValue access$getGroundValue$p(AntiBot $this) {
        return $this.groundValue;
    }

    public static final /* synthetic */ List access$getGround$p(AntiBot $this) {
        return $this.ground;
    }

    public static final /* synthetic */ BoolValue access$getAirValue$p(AntiBot $this) {
        return $this.airValue;
    }

    public static final /* synthetic */ List access$getAir$p(AntiBot $this) {
        return $this.air;
    }

    public static final /* synthetic */ BoolValue access$getSwingValue$p(AntiBot $this) {
        return $this.swingValue;
    }

    public static final /* synthetic */ List access$getSwing$p(AntiBot $this) {
        return $this.swing;
    }

    public static final /* synthetic */ BoolValue access$getHealthValue$p(AntiBot $this) {
        return $this.healthValue;
    }

    public static final /* synthetic */ BoolValue access$getEntityIDValue$p(AntiBot $this) {
        return $this.entityIDValue;
    }

    public static final /* synthetic */ BoolValue access$getDerpValue$p(AntiBot $this) {
        return $this.derpValue;
    }

    public static final /* synthetic */ BoolValue access$getWasInvisibleValue$p(AntiBot $this) {
        return $this.wasInvisibleValue;
    }

    public static final /* synthetic */ List access$getInvisible$p(AntiBot $this) {
        return $this.invisible;
    }

    public static final /* synthetic */ BoolValue access$getArmorValue$p(AntiBot $this) {
        return $this.armorValue;
    }

    public static final /* synthetic */ BoolValue access$getPingValue$p(AntiBot $this) {
        return $this.pingValue;
    }

    public static final /* synthetic */ BoolValue access$getNeedHitValue$p(AntiBot $this) {
        return $this.needHitValue;
    }

    public static final /* synthetic */ List access$getHitted$p(AntiBot $this) {
        return $this.hitted;
    }

    public static final /* synthetic */ BoolValue access$getInvalidGroundValue$p(AntiBot $this) {
        return $this.invalidGroundValue;
    }

    public static final /* synthetic */ Map access$getInvalidGround$p(AntiBot $this) {
        return $this.invalidGround;
    }

    public static final /* synthetic */ BoolValue access$getTabValue$p(AntiBot $this) {
        return $this.tabValue;
    }

    public static final /* synthetic */ ListValue access$getTabModeValue$p(AntiBot $this) {
        return $this.tabModeValue;
    }

    public static final /* synthetic */ BoolValue access$getDuplicateInWorldValue$p(AntiBot $this) {
        return $this.duplicateInWorldValue;
    }

    public static final /* synthetic */ BoolValue access$getDuplicateInTabValue$p(AntiBot $this) {
        return $this.duplicateInTabValue;
    }

    @JvmStatic
    public static final boolean isBot(@NotNull IEntityLivingBase entity) {
        return Companion.isBot(entity);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/AntiBot$Companion;", "", "()V", "isBot", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "LiKingSense"})
    public static final class Companion {
        /*
         * Exception decompiling
         */
        @JvmStatic
        public final boolean isBot(@NotNull IEntityLivingBase entity) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl35 : INVOKESTATIC - null : Stack underflow
             *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }

        public Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

