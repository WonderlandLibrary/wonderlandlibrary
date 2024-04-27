/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="JumpCircle", category=ModuleCategory.RENDER, description="\u8df3\u8dc3\u663e\u793a")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0002\u001e\u001fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0012\u0010\u0019\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0007J\u0012\u0010\u001c\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u001dH\u0007R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/JumpCircle;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "allplayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getAllplayer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setAllplayer", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "allplayerlastOnGround", "", "blueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "circles", "Ljava/util/concurrent/CopyOnWriteArrayList;", "Lnet/ccbluex/liquidbounce/features/module/modules/render/JumpCircle$Circle;", "greenValue", "lastOnGround", "radiusValue", "redValue", "strengthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "widthValue", "onEnable", "", "onRender3D", "ignored", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Circle", "Companion", "LiKingSense"})
public final class JumpCircle
extends Module {
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue radiusValue;
    private final FloatValue widthValue;
    private final FloatValue strengthValue;
    private final CopyOnWriteArrayList<Circle> circles;
    private boolean lastOnGround;
    private boolean allplayerlastOnGround;
    @Nullable
    private IEntityLivingBase allplayer;
    @NotNull
    private static final BoolValue onlyself;
    public static final Companion Companion;

    @Override
    public void onEnable() {
        this.lastOnGround = true;
        this.allplayerlastOnGround = true;
    }

    @Nullable
    public final IEntityLivingBase getAllplayer() {
        return this.allplayer;
    }

    public final void setAllplayer(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.allplayer = iEntityLivingBase;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent ignored) {
        if (((Boolean)onlyself.get()).booleanValue()) {
            if (MinecraftInstance.mc.getThePlayer().getOnGround() && !this.lastOnGround) {
                this.circles.add(new Circle(MinecraftInstance.mc.getThePlayer().getPosX(), MinecraftInstance.mc.getThePlayer().getPosY(), MinecraftInstance.mc.getThePlayer().getPosZ(), MinecraftInstance.mc.getThePlayer().getLastTickPosX(), MinecraftInstance.mc.getThePlayer().getLastTickPosY(), MinecraftInstance.mc.getThePlayer().getLastTickPosZ(), ((Number)this.widthValue.get()).floatValue()));
            }
        } else if (this.allplayer.getOnGround() && !this.allplayerlastOnGround) {
            this.circles.add(new Circle(this.allplayer.getPosX(), this.allplayer.getPosY(), this.allplayer.getPosZ(), this.allplayer.getLastTickPosX(), this.allplayer.getLastTickPosY(), this.allplayer.getLastTickPosZ(), ((Number)this.widthValue.get()).floatValue()));
        }
        this.lastOnGround = MinecraftInstance.mc.getThePlayer().getOnGround();
        this.allplayerlastOnGround = this.allplayer.getOnGround();
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent ignored) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl157 : GOTO - null : trying to set 1 previously set to 0
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

    /*
     * Exception decompiling
     */
    public JumpCircle() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl9 : PUTFIELD - null : Stack underflow
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

    static {
        Companion = new Companion(null);
        onlyself = new BoolValue("OnlySelf", true);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0018\b\u0000\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u000e\u0010!\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u0003R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000fR\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\r\"\u0004\b\u0017\u0010\u000fR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000fR\u001a\u0010\u001a\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001e\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/JumpCircle$Circle;", "", "posX", "", "posY", "posZ", "lastTickPosX", "lastTickPosY", "lastTickPosZ", "width", "", "(DDDDDDF)V", "getLastTickPosX", "()D", "setLastTickPosX", "(D)V", "getLastTickPosY", "setLastTickPosY", "getLastTickPosZ", "setLastTickPosZ", "getPosX", "setPosX", "getPosY", "setPosY", "getPosZ", "setPosZ", "radius", "getRadius", "()F", "setRadius", "(F)V", "getWidth", "setWidth", "add", "LiKingSense"})
    public static final class Circle {
        private float radius;
        private double posX;
        private double posY;
        private double posZ;
        private double lastTickPosX;
        private double lastTickPosY;
        private double lastTickPosZ;
        private float width;

        public final float getRadius() {
            return this.radius;
        }

        public final void setRadius(float f) {
            this.radius = f;
        }

        public final double add(double radius) {
            this.radius += (float)radius;
            return this.radius;
        }

        public final double getPosX() {
            return this.posX;
        }

        public final void setPosX(double d) {
            this.posX = d;
        }

        public final double getPosY() {
            return this.posY;
        }

        public final void setPosY(double d) {
            this.posY = d;
        }

        public final double getPosZ() {
            return this.posZ;
        }

        public final void setPosZ(double d) {
            this.posZ = d;
        }

        public final double getLastTickPosX() {
            return this.lastTickPosX;
        }

        public final void setLastTickPosX(double d) {
            this.lastTickPosX = d;
        }

        public final double getLastTickPosY() {
            return this.lastTickPosY;
        }

        public final void setLastTickPosY(double d) {
            this.lastTickPosY = d;
        }

        public final double getLastTickPosZ() {
            return this.lastTickPosZ;
        }

        public final void setLastTickPosZ(double d) {
            this.lastTickPosZ = d;
        }

        public final float getWidth() {
            return this.width;
        }

        public final void setWidth(float f) {
            this.width = f;
        }

        public Circle(double posX, double posY, double posZ, double lastTickPosX, double lastTickPosY, double lastTickPosZ, float width) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.lastTickPosX = lastTickPosX;
            this.lastTickPosY = lastTickPosY;
            this.lastTickPosZ = lastTickPosZ;
            this.width = width;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/JumpCircle$Companion;", "", "()V", "onlyself", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getOnlyself", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "LiKingSense"})
    public static final class Companion {
        @NotNull
        public final BoolValue getOnlyself() {
            return onlyself;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

