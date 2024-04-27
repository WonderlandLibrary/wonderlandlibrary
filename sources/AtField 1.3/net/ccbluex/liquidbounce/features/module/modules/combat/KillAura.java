/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.network.play.server.SPacketSpawnGlobalEntity
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="KillAura", category=ModuleCategory.COMBAT, keyBind=19, description="Skid by More1.0")
public final class KillAura
extends Module {
    private final IntegerValue hurtTimeValue;
    private IEntityLivingBase syncEntity;
    private final FloatValue BlockRangeValue;
    private final BoolValue afterAttackValue;
    private final IntegerValue limitedMultiTargetsValue;
    private IEntityLivingBase currentTarget;
    private final BoolValue aacValue;
    private final FloatValue minTurnSpeed;
    private final BoolValue keepSprintValue;
    private final BoolValue circleValue;
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final KillAura this$0;
        {
            this.this$0 = killAura;
            super(string, n, n2, n3);
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        static {
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
    };
    private int clicks;
    private final BoolValue lightingValue;
    private final IntegerValue noInventoryDelayValue;
    private final ListValue blockModeValue;
    private final IntegerValue circleAlpha;
    private boolean hitable;
    private String list;
    private final BoolValue raycastIgnoredValue;
    private final BoolValue lightingSoundValue;
    private boolean blockingStatus;
    private final ListValue lightingModeValue;
    private final BoolValue silentRotationValue;
    private final ListValue rotations;
    private final BoolValue raycastValue;
    private final BoolValue fakeSharpValue;
    private IEntityLivingBase lastTarget;
    private final BoolValue predictValue;
    private final BoolValue livingRaycastValue;
    private final BoolValue fakeSwingValue;
    private final IntegerValue jelloGreen;
    private final FloatValue fovValue;
    private final FloatValue cooldownValue;
    private final IntegerValue circleBlue;
    private final FloatValue minPredictSize;
    private long containerOpen;
    private final FloatValue rangeSprintReducementValue;
    public static final Companion Companion = new Companion(null);
    private final FloatValue rangeValue;
    private final BoolValue swingValue;
    private final IntegerValue jelloBlue;
    private IEntityLivingBase target;
    private final FloatValue maxTurnSpeed;
    private final BoolValue hitableValue;
    private final BoolValue outborderValue;
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final KillAura this$0;

        static {
        }
        {
            this.this$0 = killAura;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (n3 < n2) {
                this.set((Object)n3);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
    };
    private final FloatValue maxPredictSize;
    private final IntegerValue circleRed;
    private final FloatValue throughWallsRangeValue;
    private final IntegerValue jelloRed;
    private final IntegerValue circleAccuracy;
    private static int killCounts;
    private final FloatValue failRateValue;
    private final ListValue priorityValue;
    private final ListValue rotationStrafeValue;
    private final ListValue markValue;
    private final MSTimer attackTimer;
    private final IntegerValue circleGreen;
    private long attackDelay;
    private final BoolValue randomCenterValue;
    private final BoolValue noInventoryAttackValue;
    private final IntegerValue switchDelayValue;
    private final BoolValue interactAutoBlockValue;
    private final ListValue targetModeValue;
    private final MSTimer switchTimer;
    private final BoolValue stopSprintValue;
    private final List prevTargetEntities;

    private final float getRange(IEntity iEntity) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity) >= ((Number)this.BlockRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.rangeValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        return f - (iEntityPlayerSP2.getSprinting() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    public static final FloatValue access$getMaxTurnSpeed$p(KillAura killAura) {
        return killAura.maxTurnSpeed;
    }

    public static final void setKillCounts(int n) {
        Companion companion = Companion;
        killCounts = n;
    }

    public static final FloatValue access$getMinTurnSpeed$p(KillAura killAura) {
        return killAura.minTurnSpeed;
    }

    /*
     * Exception decompiling
     */
    private final void updateTarget() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl48 : ALOAD - null : trying to set 1 previously set to 0
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

    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public static final BoolValue access$getRaycastIgnoredValue$p(KillAura killAura) {
        return killAura.raycastIgnoredValue;
    }

    public static final void access$setKillCounts$cp(int n) {
        killCounts = n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCancelRun() {
        boolean bl = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isSpectator()) return true;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura.access$isAlive(this, iEntityPlayerSP2)) return true;
        if (LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) return true;
        if (!LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) return false;
        return true;
    }

    public static final boolean access$isAlive(KillAura killAura, IEntityLivingBase iEntityLivingBase) {
        return killAura.isAlive(iEntityLivingBase);
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent var1_1) {
        if (((Boolean)this.circleValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            v1 = v0.getLastTickPosX();
            v2 = MinecraftInstance.mc.getThePlayer();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            v3 = v2.getPosX();
            v4 = MinecraftInstance.mc.getThePlayer();
            if (v4 == null) {
                Intrinsics.throwNpe();
            }
            v5 = v1 + (v3 - v4.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
            v6 = MinecraftInstance.mc.getThePlayer();
            if (v6 == null) {
                Intrinsics.throwNpe();
            }
            v7 = v6.getLastTickPosY();
            v8 = MinecraftInstance.mc.getThePlayer();
            if (v8 == null) {
                Intrinsics.throwNpe();
            }
            v9 = v8.getPosY();
            v10 = MinecraftInstance.mc.getThePlayer();
            if (v10 == null) {
                Intrinsics.throwNpe();
            }
            v11 = v7 + (v9 - v10.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
            v12 = MinecraftInstance.mc.getThePlayer();
            if (v12 == null) {
                Intrinsics.throwNpe();
            }
            v13 = v12.getLastTickPosZ();
            v14 = MinecraftInstance.mc.getThePlayer();
            if (v14 == null) {
                Intrinsics.throwNpe();
            }
            v15 = v14.getPosZ();
            v16 = MinecraftInstance.mc.getThePlayer();
            if (v16 == null) {
                Intrinsics.throwNpe();
            }
            GL11.glTranslated((double)v5, (double)v11, (double)(v13 + (v15 - v16.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ()));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.circleRed.get()).intValue() / 255.0f), (float)((float)((Number)this.circleGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.circleBlue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleAlpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            var5_2 = 0;
            v17 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var5_2, 360)), (int)(61 - ((Number)this.circleAccuracy.get()).intValue()));
            var2_7 = v17.getFirst();
            var3_12 = v17.getLast();
            var4_16 = v17.getStep();
            v18 = var2_7;
            v19 = var3_12;
            if (var4_16 >= 0 ? v18 <= v19 : v18 >= v19) {
                while (true) {
                    var5_3 = (double)var2_7 * 3.141592653589793 / 180.0;
                    var7_17 = false;
                    v20 = (float)Math.cos(var5_3) * ((Number)this.rangeValue.get()).floatValue();
                    var5_3 = (double)var2_7 * 3.141592653589793 / 180.0;
                    var30_20 = v20;
                    var7_17 = false;
                    var31_23 = Math.sin(var5_3);
                    GL11.glVertex2f((float)var30_20, (float)((float)var31_23 * ((Number)this.rangeValue.get()).floatValue()));
                    if (var2_7 == var3_12) break;
                    var2_7 += var4_16;
                }
            }
            var2_8 = 6.283185307179586;
            var4_16 = 0;
            v21 = (float)Math.cos(var2_8) * ((Number)this.rangeValue.get()).floatValue();
            var2_8 = 6.283185307179586;
            var30_20 = v21;
            var4_16 = 0;
            var31_23 = Math.sin(var2_8);
            GL11.glVertex2f((float)var30_20, (float)((float)var31_23 * ((Number)this.rangeValue.get()).floatValue()));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        var2_9 = this;
        var3_12 = 0;
        v22 = MinecraftInstance.mc.getThePlayer();
        if (v22 == null) {
            Intrinsics.throwNpe();
        }
        if (v22.isSpectator()) ** GOTO lbl-1000
        v23 = MinecraftInstance.mc.getThePlayer();
        if (v23 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura.access$isAlive((KillAura)var2_9, v23) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v24 = true;
        } else {
            v24 = false;
        }
        if (v24) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target == null) {
            return;
        }
        var2_9 = (String)this.markValue.get();
        var3_12 = 0;
        v25 = var2_9;
        if (v25 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var2_9 = v25.toLowerCase();
        tmp = -1;
        switch (var2_9.hashCode()) {
            case 112785: {
                if (!var2_9.equals("red")) break;
                tmp = 1;
                break;
            }
            case 101009364: {
                if (!var2_9.equals("jello")) break;
                tmp = 2;
                break;
            }
            case 101234: {
                if (!var2_9.equals("fdp")) break;
                tmp = 3;
                break;
            }
            case -1102567108: {
                if (!var2_9.equals("liquid")) break;
                tmp = 4;
                break;
            }
            case 3530364: {
                if (!var2_9.equals("sims")) break;
                tmp = 5;
                break;
            }
            case 93832333: {
                if (!var2_9.equals("block")) break;
                tmp = 6;
                break;
            }
            case 3443503: {
                if (!var2_9.equals("plat")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                v26 = this.target;
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                v27 = v26;
                v28 = this.target;
                if (v28 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v27, v28.getHurtTime() <= 0 ? new Color(37, 126, 255, 170) : new Color(255, 0, 0, 170));
                break;
            }
            case 7: {
                v29 = this.target;
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v29, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
                break;
            }
            case 6: {
                v30 = this.target;
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                var3_13 = v30.getEntityBoundingBox();
                v31 = this.target;
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                v31.setEntityBoundingBox(var3_13.expand(0.2, 0.2, 0.2));
                v32 = this.target;
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                v33 = v32;
                v34 = this.target;
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v33, v34.getHurtTime() <= 0 ? Color.GREEN : Color.RED, true);
                v35 = this.target;
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                v35.setEntityBoundingBox(var3_13);
                break;
            }
            case 1: {
                v36 = this.target;
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v37 = v36;
                v38 = this.target;
                if (v38 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v37, v38.getHurtTime() <= 0 ? new Color(255, 255, 255, 255) : new Color(124, 215, 255, 255));
                break;
            }
            case 5: {
                var3_14 = 0.15f;
                var4_16 = 4;
                GL11.glPushMatrix();
                v39 = this.target;
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                v40 = v39.getLastTickPosX();
                v41 = this.target;
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                v42 = v41.getPosX();
                v43 = this.target;
                if (v43 == null) {
                    Intrinsics.throwNpe();
                }
                v44 = v40 + (v42 - v43.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v45 = this.target;
                if (v45 == null) {
                    Intrinsics.throwNpe();
                }
                v46 = v45.getLastTickPosY();
                v47 = this.target;
                if (v47 == null) {
                    Intrinsics.throwNpe();
                }
                v48 = v47.getPosY();
                v49 = this.target;
                if (v49 == null) {
                    Intrinsics.throwNpe();
                }
                v50 = v46 + (v48 - v49.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                v51 = this.target;
                if (v51 == null) {
                    Intrinsics.throwNpe();
                }
                v52 = v50 + (double)v51.getHeight() * 1.1;
                v53 = this.target;
                if (v53 == null) {
                    Intrinsics.throwNpe();
                }
                v54 = v53.getLastTickPosZ();
                v55 = this.target;
                if (v55 == null) {
                    Intrinsics.throwNpe();
                }
                v56 = v55.getPosZ();
                v57 = this.target;
                if (v57 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glTranslated((double)v44, (double)v52, (double)(v54 + (v56 - v57.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                v58 = this.target;
                if (v58 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(-v58.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
                v59 = MinecraftInstance.mc.getThePlayer();
                if (v59 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(((float)v59.getTicksExisted() + MinecraftInstance.mc.getTimer().getRenderPartialTicks()) * (float)5), (float)0.0f, (float)1.0f, (float)0.0f);
                v60 = this.target;
                if (v60 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.glColor(v60.getHurtTime() <= 0 ? new Color(80, 255, 80) : new Color(255, 0, 0));
                RenderUtils.enableSmoothLine(1.5f);
                var5_4 = new Cylinder();
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                var5_4.draw(0.0f, var3_14, 0.3f, var4_16, 1);
                var5_4.setDrawStyle(100012);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                var5_4.draw(var3_14, 0.0f, 0.3f, var4_16, 1);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslated((double)0.0, (double)0.0, (double)-0.3);
                var5_4.draw(0.0f, var3_14, 0.3f, var4_16, 1);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                var5_4.draw(var3_14, 0.0f, 0.3f, var4_16, 1);
                RenderUtils.disableSmoothLine();
                GL11.glPopMatrix();
                break;
            }
            case 3: {
                var3_12 = (int)(System.currentTimeMillis() % (long)1500);
                var4_16 = var3_12 > 750 ? 1 : 0;
                var5_5 = (double)var3_12 / 750.0;
                var5_5 = var4_16 == 0 ? (double)true - var5_5 : (var5_5 -= (double)true);
                var5_5 = EaseUtils.INSTANCE.easeInOutQuad(var5_5);
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glHint((int)3154, (int)4354);
                GL11.glHint((int)3155, (int)4354);
                GL11.glHint((int)3153, (int)4354);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                v61 = this.target;
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                var7_18 = v61.getEntityBoundingBox();
                var8_24 = var7_18.getMaxX() - var7_18.getMinX() + 0.3;
                var10_27 = var7_18.getMaxY() - var7_18.getMinY();
                v62 = this.target;
                if (v62 == null) {
                    Intrinsics.throwNpe();
                }
                v63 = v62.getLastTickPosX();
                v64 = this.target;
                if (v64 == null) {
                    Intrinsics.throwNpe();
                }
                v65 = v64.getPosX();
                v66 = this.target;
                if (v66 == null) {
                    Intrinsics.throwNpe();
                }
                var12_28 = v63 + (v65 - v66.getLastTickPosX()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v67 = this.target;
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                v68 = v67.getLastTickPosY();
                v69 = this.target;
                if (v69 == null) {
                    Intrinsics.throwNpe();
                }
                v70 = v69.getPosY();
                v71 = this.target;
                if (v71 == null) {
                    Intrinsics.throwNpe();
                }
                var14_29 = v68 + (v70 - v71.getLastTickPosY()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + var10_27 * var5_5;
                v72 = this.target;
                if (v72 == null) {
                    Intrinsics.throwNpe();
                }
                v73 = v72.getLastTickPosZ();
                v74 = this.target;
                if (v74 == null) {
                    Intrinsics.throwNpe();
                }
                v75 = v74.getPosZ();
                v76 = this.target;
                if (v76 == null) {
                    Intrinsics.throwNpe();
                }
                var16_30 = v73 + (v75 - v76.getLastTickPosZ()) * (double)var1_1.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(var8_24 * (double)5.0f)));
                GL11.glBegin((int)3);
                var19_32 = 360;
                for (var18_31 = 0; var18_31 <= var19_32; ++var18_31) {
                    v77 = MinecraftInstance.mc.getThePlayer();
                    if (v77 == null) {
                        Intrinsics.throwNpe();
                    }
                    var21_42 = (double)var18_31 / 50.0 * 1.75;
                    var32_35 = (double)v77.getTicksExisted() / 70.0;
                    var23_45 = false;
                    var34_36 = Math.sin(var21_42);
                    var41_39 = Color.HSBtoRGB((float)(var32_35 + var34_36) % 1.0f, 0.7f, 1.0f);
                    var20_40 = new Color(var41_39);
                    GL11.glColor3f((float)((float)var20_40.getRed() / 255.0f), (float)((float)var20_40.getGreen() / 255.0f), (float)((float)var20_40.getBlue() / 255.0f));
                    var21_42 = (double)var18_31 * 6.283185307179586 / 45.0;
                    var32_35 = var8_24;
                    var30_21 = var12_28;
                    var23_45 = false;
                    var34_36 = Math.cos(var21_42);
                    v78 = var30_21 + var32_35 * var34_36;
                    var21_42 = (double)var18_31 * 6.283185307179586 / 45.0;
                    var36_37 = var8_24;
                    var34_36 = var16_30;
                    var32_35 = var14_29;
                    var30_21 = v78;
                    var23_45 = false;
                    var38_38 = Math.sin(var21_42);
                    GL11.glVertex3d((double)var30_21, (double)var32_35, (double)(var34_36 + var36_37 * var38_38));
                }
                GL11.glEnd();
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
                break;
            }
            case 2: {
                var3_12 = (int)(System.currentTimeMillis() % (long)2000);
                var4_16 = var3_12 > 1000 ? 1 : 0;
                var5_6 = (double)var3_12 / 1000.0;
                var5_6 = var4_16 == 0 ? (double)true - var5_6 : (var5_6 -= (double)true);
                var5_6 = EaseUtils.INSTANCE.easeInOutQuad(var5_6);
                var8_25 = false;
                var7_19 = new ArrayList<E>();
                v79 = this.target;
                if (v79 == null) {
                    Intrinsics.throwNpe();
                }
                var8_26 = v79.getEntityBoundingBox();
                var9_47 = var8_26.getMaxX() - var8_26.getMinX();
                var11_48 = var8_26.getMaxY() - var8_26.getMinY();
                v80 = this.target;
                if (v80 == null) {
                    Intrinsics.throwNpe();
                }
                v81 = v80.getLastTickPosX();
                v82 = this.target;
                if (v82 == null) {
                    Intrinsics.throwNpe();
                }
                v83 = v82.getPosX();
                v84 = this.target;
                if (v84 == null) {
                    Intrinsics.throwNpe();
                }
                var13_49 = v81 + (v83 - v84.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v85 = this.target;
                if (v85 == null) {
                    Intrinsics.throwNpe();
                }
                v86 = v85.getLastTickPosY();
                v87 = this.target;
                if (v87 == null) {
                    Intrinsics.throwNpe();
                }
                v88 = v87.getPosY();
                v89 = this.target;
                if (v89 == null) {
                    Intrinsics.throwNpe();
                }
                var15_50 = v86 + (v88 - v89.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var15_50 = var4_16 != 0 ? (var15_50 -= 0.5) : (var15_50 += 0.5);
                v90 = this.target;
                if (v90 == null) {
                    Intrinsics.throwNpe();
                }
                v91 = v90.getLastTickPosZ();
                v92 = this.target;
                if (v92 == null) {
                    Intrinsics.throwNpe();
                }
                v93 = v92.getPosZ();
                v94 = this.target;
                if (v94 == null) {
                    Intrinsics.throwNpe();
                }
                var17_51 = v91 + (v93 - v94.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var22_52 = 0;
                v95 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var22_52, 360)), (int)7);
                var19_33 = v95.getFirst();
                var20_41 = v95.getLast();
                var21_43 = v95.getStep();
                v96 = var19_33;
                v97 = var20_41;
                if (var21_43 >= 0 ? v96 <= v97 : v96 >= v97) {
                    while (true) {
                        var22_53 = (double)var19_33 * 3.141592653589793 / (double)180.0f;
                        var33_55 = var13_49;
                        var30_22 = var7_19;
                        var24_54 = 0;
                        var35_56 = Math.sin(var22_53);
                        v98 = var33_55 - var35_56 * var9_47;
                        var22_53 = (double)var19_33 * 3.141592653589793 / (double)180.0f;
                        var37_57 = var17_51;
                        var35_56 = var15_50 + var11_48 * var5_6;
                        var33_55 = v98;
                        var24_54 = 0;
                        var39_58 = Math.cos(var22_53);
                        var42_59 = var37_57 + var39_58 * var9_47;
                        var44_60 = var35_56;
                        var46_61 = var33_55;
                        var30_22.add(new WVec3(var46_61, var44_60, var42_59));
                        if (var19_33 == var20_41) break;
                        var19_33 += var21_43;
                    }
                }
                var7_19.add(var7_19.get(0));
                MinecraftInstance.mc.getEntityRenderer().disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)3042);
                GL11.glDisable((int)2929);
                GL11.glBegin((int)3);
                var19_34 = (var5_6 > 0.5 ? (double)true - var5_6 : var5_6) * (double)2;
                var21_44 = var11_48 / (double)60 * (double)20 * ((double)true - var19_34) * (double)(var4_16 != 0 ? -1 : 1);
                var24_54 = 20;
                for (var23_46 = 0; var23_46 <= var24_54; ++var23_46) {
                    var25_62 = var11_48 / (double)60.0f * (double)var23_46 * var19_34;
                    if (var4_16 != 0) {
                        var25_62 = -var25_62;
                    }
                    var27_63 = (WVec3)var7_19.get(0);
                    GL11.glVertex3d((double)(var27_63.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(var27_63.getYCoord() - var25_62 - var21_44 - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(var27_63.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    GL11.glColor4f((float)((float)((Number)this.jelloRed.get()).intValue() / 255.0f), (float)((float)((Number)this.jelloGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.jelloBlue.get()).intValue() / 255.0f), (float)(0.7f * ((float)var23_46 / 20.0f)));
                    for (WVec3 var28_64 : var7_19) {
                        GL11.glVertex3d((double)(var28_64.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(var28_64.getYCoord() - var25_62 - var21_44 - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(var28_64.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    }
                    GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                }
                GL11.glEnd();
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
                break;
            }
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v99 = this.currentTarget;
            if (v99 == null) {
                Intrinsics.throwNpe();
            }
            if (v99.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var2_10 = this.clicks;
                this.clicks = var2_10 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
        var2_11 = (String)this.markValue.get();
        var3_15 = false;
        v100 = var2_11;
        if (v100 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (v100.toLowerCase().equals("LiYing") && !StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Multi", (boolean)true)) {
            RenderUtils.drawCircleESP(this.target, 0.67, Color.RED.getRGB(), true);
        }
    }

    public final void setList(String string) {
        this.list = string;
    }

    public final IntegerValue getHurtTimeValue() {
        return this.hurtTimeValue;
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.lastTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
    }

    public final BoolValue getKeepSprintValue() {
        return this.keepSprintValue;
    }

    public final String getList() {
        return this.list;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    private final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.BlockRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
    }

    public final void setSyncEntity(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.syncEntity = iEntityLivingBase;
    }

    public static final long access$getAttackDelay$p(KillAura killAura) {
        return killAura.attackDelay;
    }

    public static final IntegerValue access$getMinCPS$p(KillAura killAura) {
        return killAura.minCPS;
    }

    private final void updateHitable() {
        if (((Boolean)this.hitableValue.get()).booleanValue()) {
            this.hitable = true;
            return;
        }
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
            this.hitable = true;
            return;
        }
        double d = this.getMaxRange();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = iEntityPlayerSP;
        IEntityLivingBase iEntityLivingBase = this.target;
        if (iEntityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(iEntity, iEntityLivingBase);
        boolean bl = false;
        double d3 = Math.min(d, d2) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            IEntity iEntity2 = RaycastUtils.raycastEntity(d3, new RaycastUtils.EntityFilter(this){
                final KillAura this$0;

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public boolean canRaycast(@Nullable IEntity iEntity) {
                    if (((Boolean)KillAura.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(iEntity)) return false;
                        if (MinecraftInstance.classProvider.isEntityArmorStand(iEntity)) return false;
                    }
                    if (KillAura.access$isEnemy(this.this$0, iEntity)) return true;
                    if ((Boolean)KillAura.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura.access$getAacValue$p(this.this$0).get() == false) return false;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntity iEntity2 = iEntity;
                    if (iEntity2 == null) {
                        Intrinsics.throwNpe();
                    }
                    Collection collection = iWorldClient.getEntitiesWithinAABBExcludingEntity(iEntity, iEntity2.getEntityBoundingBox());
                    boolean bl = false;
                    if (collection.isEmpty()) return false;
                    return true;
                }
                {
                    this.this$0 = killAura;
                }

                static {
                }
            });
            if (((Boolean)this.raycastValue.get()).booleanValue() && iEntity2 != null && MinecraftInstance.classProvider.isEntityLivingBase(iEntity2) && (LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class).getState() || !MinecraftInstance.classProvider.isEntityPlayer(iEntity2) || !PlayerExtensionKt.isClientFriend(iEntity2.asEntityPlayer()))) {
                this.currentTarget = iEntity2.asEntityLivingBase();
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? this.currentTarget.equals(iEntity2) : true;
        } else {
            this.hitable = RotationUtils.isFaced(this.currentTarget, d3);
        }
    }

    public final IEntityLivingBase getSyncEntity() {
        return this.syncEntity;
    }

    /*
     * Unable to fully structure code
     */
    private final void attackEntity(IEntityLivingBase var1_1) {
        block25: {
            block26: {
                v0 = MinecraftInstance.mc.getThePlayer();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                if ((var2_2 = v0).isBlocking() || this.blockingStatus) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                    if (((Boolean)this.afterAttackValue.get()).booleanValue()) {
                        this.blockingStatus = false;
                    }
                }
                LiquidBounce.INSTANCE.getEventManager().callEvent(new AttackEvent(var1_1));
                if (((Boolean)this.swingValue.get()).booleanValue()) {
                    // empty if block
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)var1_1, ICPacketUseEntity.WAction.ATTACK));
                if (((Boolean)this.swingValue.get()).booleanValue()) {
                    var2_2.swingItem();
                }
                if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
                    if (!(!(var2_2.getFallDistance() > 0.0f) || var2_2.getOnGround() || var2_2.isOnLadder() || var2_2.isInWater() || var2_2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) || var2_2.isRiding())) {
                        var2_2.onCriticalHit(var1_1);
                    }
                    if (KillAura.access$getFunctions$p$s1046033730().getModifierForCreature(var2_2.getHeldItem(), var1_1.getCreatureAttribute()) > 0.0f) {
                        var2_2.onEnchantmentCritical(var1_1);
                    }
                } else if (MinecraftInstance.mc.getPlayerController().getCurrentGameType() != IWorldSettings.WGameType.SPECTATOR) {
                    var2_2.attackTargetEntityWithCurrentItem(var1_1);
                }
                v1 = LiquidBounce.INSTANCE.getModuleManager().get(Criticals.class);
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Criticals");
                }
                var3_3 = (Criticals)v1;
                var5_6 = 2;
                for (var4_4 = 0; var4_4 <= var5_6; ++var4_4) {
                    if (var2_2.getFallDistance() > 0.0f && var2_2.getOnGround() == false && var2_2.isOnLadder() == false && var2_2.isInWater() == false && var2_2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) == false && var2_2.getRidingEntity() == null || var3_3.getState() && var3_3.getMsTimer().hasTimePassed(((Number)var3_3.getDelayValue().get()).intValue()) && !var2_2.isInWater() && !var2_2.isInLava() && !var2_2.isInWeb()) {
                        v2 = this.target;
                        if (v2 == null) {
                            Intrinsics.throwNpe();
                        }
                        var2_2.onCriticalHit(v2);
                    }
                    v3 = KillAura.access$getFunctions$p$s1046033730();
                    v4 = var2_2.getHeldItem();
                    v5 = this.target;
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v3.getModifierForCreature(v4, v5.getCreatureAttribute()) > 0.0f) && !((Boolean)this.fakeSharpValue.get()).booleanValue()) continue;
                    v6 = this.target;
                    if (v6 == null) {
                        Intrinsics.throwNpe();
                    }
                    var2_2.onEnchantmentCritical(v6);
                }
                if (!StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"Packet", (boolean)true)) break block25;
                if (var2_2.isBlocking()) break block26;
                var4_5 = this;
                var5_6 = 0;
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                if (v7.getHeldItem() == null) ** GOTO lbl-1000
                v8 = MinecraftInstance.mc.getThePlayer();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                v9 = v8.getHeldItem();
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemSword(v9.getItem())) {
                    v10 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v10 = false;
                }
                if (!v10) break block25;
            }
            this.startBlocking(var1_1, (Boolean)this.interactAutoBlockValue.get());
        }
        var2_2.resetCooldown();
        var2_2.resetCooldown();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(MotionEvent var1_1) {
        block19: {
            block20: {
                if (((Boolean)this.stopSprintValue.get()).booleanValue()) {
                    v0 = MinecraftInstance.mc.getThePlayer();
                    if (v0 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v0.getOnGround()) {
                        this.keepSprintValue.set(true);
                    } else {
                        this.keepSprintValue.set(false);
                    }
                }
                if (var1_1.getEventState() != EventState.POST) break block19;
                if (this.target == null) {
                    return;
                }
                if (this.currentTarget == null) {
                    return;
                }
                this.updateHitable();
                if (!StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"AfterTick", (boolean)true)) break block20;
                var2_2 = this;
                var3_3 = false;
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getHeldItem() == null) ** GOTO lbl-1000
                v2 = MinecraftInstance.mc.getThePlayer();
                if (v2 == null) {
                    Intrinsics.throwNpe();
                }
                v3 = v2.getHeldItem();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemSword(v3.getItem())) {
                    v4 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v4 = false;
                }
                if (v4) {
                    v5 = this.currentTarget;
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.startBlocking(v5, this.hitable);
                }
            }
            if (this.switchTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) || ((String)this.targetModeValue.get()).equals("Switch") ^ true) {
                if (((Boolean)this.aacValue.get()).booleanValue()) {
                    v6 = this.target;
                    if (v6 == null) {
                        Intrinsics.throwNpe();
                    }
                    v7 = v6.getEntityId();
                } else {
                    v8 = this.currentTarget;
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    v7 = v8.getEntityId();
                }
                this.prevTargetEntities.add(v7);
                this.switchTimer.reset();
            }
            return;
        }
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            this.update();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onStrafe(StrafeEvent strafeEvent) {
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            return;
        }
        this.update();
        if (this.currentTarget == null || RotationUtils.targetRotation == null) return;
        String string = (String)this.rotationStrafeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case -902327211: {
                if (!string.equals("silent")) return;
                break;
            }
            case -891986231: {
                if (!string.equals("strict")) return;
                Rotation rotation = RotationUtils.targetRotation;
                if (rotation == null) {
                    return;
                }
                Rotation rotation2 = rotation;
                float f = rotation2.component1();
                float f2 = strafeEvent.getStrafe();
                float f3 = strafeEvent.getForward();
                float f4 = strafeEvent.getFriction();
                float f5 = f2 * f2 + f3 * f3;
                if (f5 >= 1.0E-4f) {
                    IEntityPlayerSP iEntityPlayerSP;
                    boolean bl2 = false;
                    if ((f5 = (float)Math.sqrt(f5)) < 1.0f) {
                        f5 = 1.0f;
                    }
                    f5 = f4 / f5;
                    f2 *= f5;
                    f3 *= f5;
                    float f6 = (float)((double)f * Math.PI / (double)180.0f);
                    boolean bl3 = false;
                    float f7 = (float)Math.sin(f6);
                    float f8 = (float)((double)f * Math.PI / (double)180.0f);
                    boolean bl4 = false;
                    f6 = (float)Math.cos(f8);
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() + (double)(f2 * f6 - f3 * f7));
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP;
                    iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() + (double)(f3 * f6 + f2 * f7));
                }
                strafeEvent.cancelEvent();
                return;
            }
        }
        this.update();
        RotationUtils.targetRotation.applyStrafeToPlayer(strafeEvent);
        strafeEvent.cancelEvent();
        return;
    }

    public static final FloatValue access$getMinPredictSize$p(KillAura killAura) {
        return killAura.minPredictSize;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent var1_1) {
        if (((Boolean)this.lightingValue.get()).booleanValue()) {
            var2_2 = (String)this.lightingModeValue.get();
            var3_4 = false;
            v0 = var2_2;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var2_2 = v0.toLowerCase();
            switch (var2_2.hashCode()) {
                case -1407259064: {
                    if (!var2_2.equals("attack")) ** break;
                    break;
                }
                case 3079268: {
                    if (!var2_2.equals("dead")) ** break;
                    if (this.target != null) {
                        if (this.lastTarget == null) {
                            v1 = this.target;
                        } else {
                            v2 = this.lastTarget;
                            if (v2 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v2.getHealth() <= (float)false) {
                                v3 = MinecraftInstance.mc.getNetHandler2();
                                v4 = (World)MinecraftInstance.mc2.field_71441_e;
                                v5 = this.lastTarget;
                                if (v5 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v6 = v5.getPosX();
                                v7 = this.lastTarget;
                                if (v7 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v8 = v7.getPosY();
                                v9 = this.lastTarget;
                                if (v9 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v3.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v4, v6, v8, v9.getPosZ(), true)));
                                if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                                }
                            }
                            v1 = this.target;
                        }
                        this.lastTarget = v1;
                        ** break;
                    }
                    if (this.lastTarget == null) ** break;
                    v10 = this.lastTarget;
                    if (v10 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v10.getHealth() <= (float)false)) ** break;
                    v11 = MinecraftInstance.mc.getNetHandler2();
                    v12 = (World)MinecraftInstance.mc2.field_71441_e;
                    v13 = this.lastTarget;
                    if (v13 == null) {
                        Intrinsics.throwNpe();
                    }
                    v14 = v13.getPosX();
                    v15 = this.lastTarget;
                    if (v15 == null) {
                        Intrinsics.throwNpe();
                    }
                    v16 = v15.getPosY();
                    v17 = this.lastTarget;
                    if (v17 == null) {
                        Intrinsics.throwNpe();
                    }
                    v11.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v12, v14, v16, v17.getPosZ(), true)));
                    if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                        MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                    }
                    this.lastTarget = this.target;
                    ** break;
                }
            }
            v18 = MinecraftInstance.mc.getNetHandler2();
            v19 = (World)MinecraftInstance.mc2.field_71441_e;
            v20 = this.target;
            if (v20 == null) {
                Intrinsics.throwNpe();
            }
            v21 = v20.getPosX();
            v22 = this.target;
            if (v22 == null) {
                Intrinsics.throwNpe();
            }
            v23 = v22.getPosY();
            v24 = this.target;
            if (v24 == null) {
                Intrinsics.throwNpe();
            }
            v18.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v19, v21, v23, v24.getPosZ(), true)));
            if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) ** break;
            MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
            ** break;
        }
lbl81:
        // 11 sources

        if (this.syncEntity != null) {
            v25 = this.syncEntity;
            if (v25 == null) {
                Intrinsics.throwNpe();
            }
            if (v25.isDead()) {
                ++KillAura.killCounts;
                this.syncEntity = null;
            }
        }
        var2_2 = this;
        var3_4 = false;
        v26 = MinecraftInstance.mc.getThePlayer();
        if (v26 == null) {
            Intrinsics.throwNpe();
        }
        if (v26.isSpectator()) ** GOTO lbl-1000
        v27 = MinecraftInstance.mc.getThePlayer();
        if (v27 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura.access$isAlive((KillAura)var2_2, v27) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v28 = true;
        } else {
            v28 = false;
        }
        if (v28) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target != null && this.currentTarget != null) {
            v29 = MinecraftInstance.mc.getThePlayer();
            if (v29 == null) {
                Intrinsics.throwNpe();
            }
            if (v29.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
                while (this.clicks > 0) {
                    this.runAttack();
                    var2_3 = this.clicks;
                    this.clicks = var2_3 + -1;
                }
            }
        }
    }

    public static final BoolValue access$getLivingRaycastValue$p(KillAura killAura) {
        return killAura.livingRaycastValue;
    }

    public static final int access$getKillCounts$cp() {
        return killCounts;
    }

    public static final int getKillCounts() {
        Companion companion = Companion;
        return killCounts;
    }

    /*
     * Exception decompiling
     */
    public final void update() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl89 : RETURN - null : trying to set 0 previously set to 1
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
        return (String)this.targetModeValue.get();
    }

    public static final void access$setAttackDelay$p(KillAura killAura, long l) {
        killAura.attackDelay = l;
    }

    public static final boolean access$isEnemy(KillAura killAura, IEntity iEntity) {
        return killAura.isEnemy(iEntity);
    }

    @EventTarget
    public final void onEntityMove(EntityMovementEvent entityMovementEvent) {
        IEntity iEntity = entityMovementEvent.getMovedEntity();
        if (this.target == null || iEntity.equals(this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public static final FloatValue access$getMaxPredictSize$p(KillAura killAura) {
        return killAura.maxPredictSize;
    }

    private final void startBlocking(IEntity iEntity, boolean bl) {
        Object object;
        if (bl) {
            IEntity iEntity2 = MinecraftInstance.mc.getRenderViewEntity();
            object = iEntity2 != null ? iEntity2.getPositionEyes(1.0f) : null;
            double d = iEntity.getCollisionBorderSize();
            IAxisAlignedBB iAxisAlignedBB = iEntity.getEntityBoundingBox().expand(d, d, d);
            Rotation rotation = RotationUtils.targetRotation;
            if (rotation == null) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                float f = iEntityPlayerSP.getRotationYaw();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                rotation = new Rotation(f, iEntityPlayerSP2.getRotationPitch());
            }
            Rotation rotation2 = rotation;
            float f = rotation2.component1();
            float f2 = rotation2.component2();
            float f3 = -f * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            float f4 = (float)Math.cos(f3);
            float f5 = -f * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl3 = false;
            f3 = (float)Math.sin(f5);
            float f6 = -f2 * ((float)Math.PI / 180);
            boolean bl4 = false;
            f5 = -((float)Math.cos(f6));
            float f7 = -f2 * ((float)Math.PI / 180);
            boolean bl5 = false;
            f6 = (float)Math.sin(f7);
            double d2 = this.getMaxRange();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d3 = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, iEntity);
            boolean bl6 = false;
            double d4 = Math.min(d2, d3) + 1.0;
            Object object2 = object;
            if (object2 == null) {
                Intrinsics.throwNpe();
            }
            Object object3 = object2;
            d3 = (double)(f3 * f5) * d4;
            double d5 = (double)f6 * d4;
            double d6 = (double)(f4 * f5) * d4;
            boolean bl7 = false;
            WVec3 wVec3 = new WVec3(((WVec3)object3).getXCoord() + d3, ((WVec3)object3).getYCoord() + d5, ((WVec3)object3).getZCoord() + d6);
            IMovingObjectPosition iMovingObjectPosition = iAxisAlignedBB.calculateIntercept((WVec3)object, wVec3);
            if (iMovingObjectPosition == null) {
                return;
            }
            object3 = iMovingObjectPosition;
            WVec3 wVec32 = object3.getHitVec();
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, new WVec3(wVec32.getXCoord() - iEntity.getPosX(), wVec32.getYCoord() - iEntity.getPosY(), wVec32.getZCoord() - iEntity.getPosZ())));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.INTERACT));
        }
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        object = iEntityPlayerSP.getInventory().getCurrentItemInHand();
        WEnumHand wEnumHand = WEnumHand.MAIN_HAND;
        IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
        boolean bl8 = false;
        IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
        iINetHandlerPlayClient2.addToSendQueue(iPacket);
        this.blockingStatus = true;
    }

    public static final IntegerValue access$getMaxCPS$p(KillAura killAura) {
        return killAura.maxCPS;
    }

    public static final BoolValue access$getAacValue$p(KillAura killAura) {
        return killAura.aacValue;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCanBlock() {
        boolean bl = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getHeldItem() == null) return false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (!MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) return false;
        return true;
    }

    public KillAura() {
        List list;
        this.hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
        this.cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
        this.rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
        this.BlockRangeValue = new FloatValue("BlockRange", 3.0f, 0.0f, 8.0f);
        this.throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
        this.rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
        this.priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime", "HYT"}, "Distance");
        this.targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
        this.switchDelayValue = new IntegerValue("SwitchDelay", 700, 0, 2000);
        this.swingValue = new BoolValue("Swing", true);
        this.keepSprintValue = new BoolValue("KeepSprint", true);
        this.afterAttackValue = new BoolValue("AutoBlock-AfterAttack", false);
        this.blockModeValue = new ListValue("AutoBlock", new String[]{"Off", "Packet", "AfterTick", "Nore"}, "Packet");
        this.interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
        this.raycastValue = new BoolValue("RayCast", true);
        this.raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
        this.livingRaycastValue = new BoolValue("LivingRayCast", true);
        this.aacValue = new BoolValue("AAC", false);
        this.rotations = new ListValue("RotationMode", new String[]{"Vanilla", "BackTrack", "Test", "HytRotation"}, "Test");
        this.silentRotationValue = new BoolValue("SilentRotation", true);
        this.rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
        this.randomCenterValue = new BoolValue("RandomCenter", true);
        this.outborderValue = new BoolValue("Outborder", false);
        this.maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura this$0;
            {
                this.this$0 = killAura;
                super(string, f, f2, f3);
            }

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
        };
        this.minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
            final KillAura this$0;

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = killAura;
                super(string, f, f2, f3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            static {
            }
        };
        this.predictValue = new BoolValue("Predict", true);
        this.maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura this$0;

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura.access$getMinPredictSize$p(this.this$0).get()).floatValue();
                if (f3 > f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }
            {
                this.this$0 = killAura;
                super(string, f, f2, f3);
            }
        };
        this.minPredictSize = new FloatValue(this, "MinPredictSize", 1.0f, 0.1f, 5.0f){
            final KillAura this$0;

            static {
            }
            {
                this.this$0 = killAura;
                super(string, f, f2, f3);
            }

            protected void onChanged(float f, float f2) {
                float f3 = ((Number)KillAura.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
                if (f3 < f2) {
                    this.set((Object)Float.valueOf(f3));
                }
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).floatValue(), ((Number)object2).floatValue());
            }
        };
        this.lightingValue = new BoolValue("Lighting", false);
        this.lightingModeValue = new ListValue("Lighting-Mode", new String[]{"Dead", "Attack"}, "Dead");
        this.lightingSoundValue = new BoolValue("Lighting-Sound", true);
        this.fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
        this.hitableValue = new BoolValue("AlwaysHitable", true);
        this.failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
        this.stopSprintValue = new BoolValue("StopSprintOnAir", true);
        this.fakeSwingValue = new BoolValue("FakeSwing", true);
        this.noInventoryAttackValue = new BoolValue("NoInvAttack", false);
        this.noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
        this.limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
        this.markValue = new ListValue("Mark", new String[]{"Liquid", "FDP", "Block", "Jello", "Plat", "Red", "Sims", "None", "LiYing"}, "FDP");
        this.jelloRed = new IntegerValue("jelloRed", 255, 0, 255);
        this.jelloGreen = new IntegerValue("jelloGreen", 255, 0, 255);
        this.jelloBlue = new IntegerValue("jelloBlue", 255, 0, 255);
        this.fakeSharpValue = new BoolValue("FakeSharp", true);
        this.circleValue = new BoolValue("Circle", true);
        this.circleRed = new IntegerValue("CircleRed", 255, 0, 255);
        this.circleGreen = new IntegerValue("CircleGreen", 255, 0, 255);
        this.circleBlue = new IntegerValue("CircleBlue", 255, 0, 255);
        this.circleAlpha = new IntegerValue("CircleAlpha", 255, 0, 255);
        this.circleAccuracy = new IntegerValue("CircleAccuracy", 15, 0, 60);
        KillAura killAura = this;
        boolean bl = false;
        killAura.prevTargetEntities = list = (List)new ArrayList();
        this.attackTimer = new MSTimer();
        this.switchTimer = new MSTimer();
        this.containerOpen = -1L;
        this.list = "";
    }

    private final void stopBlocking() {
        if (this.blockingStatus) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            this.blockingStatus = false;
        }
    }

    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    /*
     * Exception decompiling
     */
    private final void runAttack() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl177 : ALOAD_0 - null : trying to set 1 previously set to 0
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

    public static final class Companion {
        public final void setKillCounts(int n) {
            KillAura.access$setKillCounts$cp(n);
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int getKillCounts() {
            return KillAura.access$getKillCounts$cp();
        }

        @JvmStatic
        public static void killCounts$annotations() {
        }

        private Companion() {
        }
    }
}

