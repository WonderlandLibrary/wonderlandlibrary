/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.modules.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PathUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="TPAura", category=ModuleCategory.PLAYER, description="TPAura")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010)\u001a\u00020*H\u0016J\b\u0010+\u001a\u00020*H\u0016J\b\u0010,\u001a\u00020*H\u0016J\u0010\u0010-\u001a\u00020*2\u0006\u0010.\u001a\u00020/H\u0007J\u0010\u00100\u001a\u00020*2\u0006\u0010.\u001a\u000201H\u0007J\u0010\u00102\u001a\u00020*2\u0006\u0010.\u001a\u000203H\u0007J\b\u00104\u001a\u00020*H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\u00020 8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\"R\u0010\u0010#\u001a\u0004\u0018\u00010$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010%\u001a\u0012\u0012\u0004\u0012\u00020'0&j\b\u0012\u0004\u0012\u00020'`(X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2={"Lme/report/liquidware/modules/player/TPAura;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "apsValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "attackDelay", "", "getAttackDelay", "()J", "auraMod", "Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "clickTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "fovValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "lastTarget", "Lnet/minecraft/entity/EntityLivingBase;", "getLastTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setLastTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "maxMoveDistValue", "maxTargetsValue", "noKillAuraValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "noPureC03Value", "priorityValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "rangeValue", "renderValue", "swingValue", "tag", "", "getTag", "()Ljava/lang/String;", "thread", "Ljava/lang/Thread;", "tpVectors", "Ljava/util/ArrayList;", "Lnet/minecraft/util/Vec3;", "Lkotlin/collections/ArrayList;", "onDisable", "", "onEnable", "onInitialize", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "runAttack", "KyinoClient"})
public final class TPAura
extends Module {
    private final IntegerValue apsValue = new IntegerValue("APS", 1, 1, 10);
    private final IntegerValue maxTargetsValue = new IntegerValue("MaxTargets", 2, 1, 8);
    private final IntegerValue rangeValue = new IntegerValue("Range", 80, 10, 200, "m");
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f, "\u00b0");
    private final FloatValue maxMoveDistValue = new FloatValue("MaxMoveSpeed", 8.0f, 2.0f, 15.0f, "m");
    private final ListValue swingValue = new ListValue("Swing", new String[]{"Normal", "Packet", "None"}, "Normal");
    private final BoolValue noPureC03Value = new BoolValue("NoStandingPackets", true);
    private final BoolValue noKillAuraValue = new BoolValue("NoKillAura", true);
    private final ListValue renderValue = new ListValue("Render", new String[]{"Box", "Lines", "None"}, "Box");
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "LivingTime"}, "Distance");
    private final MSTimer clickTimer = new MSTimer();
    private ArrayList<Vec3> tpVectors;
    private Thread thread;
    @Nullable
    private EntityLivingBase lastTarget;
    private KillAura auraMod;

    @Nullable
    public final EntityLivingBase getLastTarget() {
        return this.lastTarget;
    }

    public final void setLastTarget(@Nullable EntityLivingBase entityLivingBase) {
        this.lastTarget = entityLivingBase;
    }

    private final long getAttackDelay() {
        return 1000L / (long)((Number)this.apsValue.get()).intValue();
    }

    @Override
    @NotNull
    public String getTag() {
        return "APS " + ((Number)this.apsValue.get()).intValue() + ", Range " + ((Number)this.rangeValue.get()).intValue();
    }

    @Override
    public void onEnable() {
        this.clickTimer.reset();
        this.tpVectors.clear();
        this.lastTarget = null;
    }

    @Override
    public void onDisable() {
        this.clickTimer.reset();
        this.tpVectors.clear();
        this.lastTarget = null;
    }

    @Override
    public void onInitialize() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        this.auraMod = (KillAura)module;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.noKillAuraValue.get()).booleanValue()) {
            KillAura killAura = this.auraMod;
            if (killAura == null) {
                Intrinsics.throwUninitializedPropertyAccessException("auraMod");
            }
            if (killAura.getTarget() != null) return;
        }
        if (!this.clickTimer.hasTimePassed(this.getAttackDelay())) {
            return;
        }
        if (this.thread != null) {
            Thread thread2 = this.thread;
            if (thread2 == null) {
                Intrinsics.throwNpe();
            }
            if (thread2.isAlive()) {
                this.clickTimer.reset();
                return;
            }
        }
        this.tpVectors.clear();
        this.clickTimer.reset();
        Thread thread3 = this.thread = new Thread(new Runnable(this){
            final /* synthetic */ TPAura this$0;

            public final void run() {
                TPAura.access$runAttack(this.this$0);
            }
            {
                this.this$0 = tPAura;
            }
        });
        if (thread3 == null) {
            Intrinsics.throwNpe();
        }
        thread3.start();
    }

    private final void runAttack() {
        Object entity2;
        block36: {
            block35: {
                block34: {
                    if (!((Boolean)this.noKillAuraValue.get()).booleanValue()) break block34;
                    KillAura killAura = this.auraMod;
                    if (killAura == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("auraMod");
                    }
                    if (killAura.getTarget() != null) break block35;
                }
                if (TPAura.access$getMc$p$s1046033730().field_71439_g != null && TPAura.access$getMc$p$s1046033730().field_71441_e != null) break block36;
            }
            return;
        }
        boolean bl = false;
        ArrayList<Object> targets = new ArrayList<Object>();
        int entityCount = 0;
        for (Object entity2 : TPAura.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            if (!(entity2 instanceof EntityLivingBase) || !EntityUtils.isSelected((Entity)entity2, true) || !(TPAura.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)entity2) <= ((Number)this.rangeValue.get()).floatValue()) || ((Number)this.fovValue.get()).floatValue() < 180.0f && RotationUtils.getRotationDifference((Entity)entity2) > ((Number)this.fovValue.get()).doubleValue()) continue;
            if (entityCount >= ((Number)this.maxTargetsValue.get()).intValue()) break;
            targets.add(entity2);
            ++entityCount;
        }
        if (targets.isEmpty()) {
            this.lastTarget = null;
            return;
        }
        entity2 = (String)this.priorityValue.get();
        boolean bl2 = false;
        Object object = entity2;
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string = ((String)object).toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string, "(this as java.lang.String).toLowerCase()");
        switch (string) {
            case "distance": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl3 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Float.valueOf(TPAura.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)it));
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Float f = Float.valueOf(TPAura.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)it));
                        return ComparisonsKt.compareValues(comparable2, (Comparable)f);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
                break;
            }
            case "health": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl3 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Float.valueOf(it.func_110143_aJ());
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Float f = Float.valueOf(it.func_110143_aJ());
                        return ComparisonsKt.compareValues(comparable2, (Comparable)f);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
                break;
            }
            case "livingtime": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl3 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl2 = false;
                        it = (EntityLivingBase)b;
                        Comparable comparable = Integer.valueOf(-it.field_70173_aa);
                        bl2 = false;
                        Integer n = -it.field_70173_aa;
                        return ComparisonsKt.compareValues(comparable, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
            }
        }
        Iterable $this$forEach$iv = targets;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Vec3 point;
            Object element$iv2;
            EntityLivingBase it = (EntityLivingBase)element$iv;
            boolean bl4 = false;
            if (TPAura.access$getMc$p$s1046033730().field_71439_g == null || TPAura.access$getMc$p$s1046033730().field_71441_e == null) {
                return;
            }
            ArrayList<Vec3> path = PathUtils.findTeleportPath((EntityLivingBase)TPAura.access$getMc$p$s1046033730().field_71439_g, it, ((Number)this.maxMoveDistValue.get()).floatValue());
            if (((Boolean)this.noKillAuraValue.get()).booleanValue()) {
                KillAura killAura = this.auraMod;
                if (killAura == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("auraMod");
                }
                if (killAura.getTarget() != null) {
                    return;
                }
            }
            ArrayList<Vec3> arrayList = path;
            Intrinsics.checkExpressionValueIsNotNull(arrayList, "path");
            Object $this$forEach$iv2 = arrayList;
            boolean $i$f$forEach2 = false;
            Iterator iterator2 = $this$forEach$iv2.iterator();
            while (iterator2.hasNext()) {
                element$iv2 = iterator2.next();
                point = (Vec3)element$iv2;
                boolean bl5 = false;
                this.tpVectors.add(point);
                Minecraft minecraft = TPAura.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(point.field_72450_a, point.field_72448_b, point.field_72449_c, true));
            }
            this.lastTarget = it;
            $this$forEach$iv2 = (String)this.swingValue.get();
            $i$f$forEach2 = false;
            Object object2 = $this$forEach$iv2;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string2 = ((String)object2).toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
            switch (string2) {
                case "normal": {
                    TPAura.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                    break;
                }
                case "packet": {
                    Minecraft minecraft = TPAura.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                }
            }
            Minecraft minecraft = TPAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)it, C02PacketUseEntity.Action.ATTACK));
            $this$forEach$iv2 = CollectionsKt.reversed((Iterable)path);
            $i$f$forEach2 = false;
            iterator2 = $this$forEach$iv2.iterator();
            while (iterator2.hasNext()) {
                element$iv2 = iterator2.next();
                point = (Vec3)element$iv2;
                boolean bl6 = false;
                if (StringsKt.equals((String)this.renderValue.get(), "lines", true)) {
                    this.tpVectors.add(point);
                }
                Minecraft minecraft2 = TPAura.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(point.field_72450_a, point.field_72448_b, point.field_72449_c, true));
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            this.clickTimer.reset();
        }
        if (((Boolean)this.noPureC03Value.get()).booleanValue() && packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
            event.cancelEvent();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        ArrayList<Vec3> arrayList = this.tpVectors;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (arrayList) {
            boolean bl3 = false;
            if (StringsKt.equals((String)this.renderValue.get(), "none", true) || this.tpVectors.isEmpty()) {
                return;
            }
            Minecraft minecraft = TPAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            double renderPosX = minecraft.func_175598_ae().field_78730_l;
            Minecraft minecraft2 = TPAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            double renderPosY = minecraft2.func_175598_ae().field_78731_m;
            Minecraft minecraft3 = TPAura.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
            double renderPosZ = minecraft3.func_175598_ae().field_78728_n;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glShadeModel((int)7425);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)2896);
            GL11.glDepthMask((boolean)false);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLoadIdentity();
            TPAura.access$getMc$p$s1046033730().field_71460_t.func_78479_a(TPAura.access$getMc$p$s1046033730().field_71428_T.field_74281_c, 2);
            RenderUtils.glColor(Color.WHITE);
            GL11.glLineWidth((float)1.0f);
            if (StringsKt.equals((String)this.renderValue.get(), "lines", true)) {
                GL11.glBegin((int)3);
            }
            try {
                for (Vec3 vec : this.tpVectors) {
                    double x = vec.field_72450_a - renderPosX;
                    double y = vec.field_72448_b - renderPosY;
                    double z = vec.field_72449_c - renderPosZ;
                    double width = 0.3;
                    double height = TPAura.access$getMc$p$s1046033730().field_71439_g.func_70047_e();
                    String string = (String)this.renderValue.get();
                    boolean bl4 = false;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string3 = string2.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
                    switch (string3) {
                        case "box": {
                            GL11.glBegin((int)3);
                            GL11.glVertex3d((double)(x - width), (double)y, (double)(z - width));
                            GL11.glVertex3d((double)(x - width), (double)y, (double)(z - width));
                            GL11.glVertex3d((double)(x - width), (double)(y + height), (double)(z - width));
                            GL11.glVertex3d((double)(x + width), (double)(y + height), (double)(z - width));
                            GL11.glVertex3d((double)(x + width), (double)y, (double)(z - width));
                            GL11.glVertex3d((double)(x - width), (double)y, (double)(z - width));
                            GL11.glVertex3d((double)(x - width), (double)y, (double)(z + width));
                            GL11.glEnd();
                            GL11.glBegin((int)3);
                            GL11.glVertex3d((double)(x + width), (double)y, (double)(z + width));
                            GL11.glVertex3d((double)(x + width), (double)(y + height), (double)(z + width));
                            GL11.glVertex3d((double)(x - width), (double)(y + height), (double)(z + width));
                            GL11.glVertex3d((double)(x - width), (double)y, (double)(z + width));
                            GL11.glVertex3d((double)(x + width), (double)y, (double)(z + width));
                            GL11.glVertex3d((double)(x + width), (double)y, (double)(z - width));
                            GL11.glEnd();
                            GL11.glBegin((int)3);
                            GL11.glVertex3d((double)(x + width), (double)(y + height), (double)(z + width));
                            GL11.glVertex3d((double)(x + width), (double)(y + height), (double)(z - width));
                            GL11.glEnd();
                            GL11.glBegin((int)3);
                            GL11.glVertex3d((double)(x - width), (double)(y + height), (double)(z + width));
                            GL11.glVertex3d((double)(x - width), (double)(y + height), (double)(z - width));
                            GL11.glEnd();
                            break;
                        }
                        case "lines": {
                            GL11.glVertex3d((double)x, (double)y, (double)z);
                        }
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (StringsKt.equals((String)this.renderValue.get(), "lines", true)) {
                GL11.glEnd();
            }
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Unit unit = Unit.INSTANCE;
        }
    }

    public TPAura() {
        TPAura tPAura = this;
        boolean bl = false;
        ArrayList arrayList = new ArrayList();
        tPAura.tpVectors = arrayList;
    }

    public static final /* synthetic */ void access$runAttack(TPAura $this) {
        $this.runAttack();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

