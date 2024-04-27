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
package net.dev.important.modules.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.PathUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
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

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Info(name="TeleportAura", spacedName="Teleport Aura", description="Automatically attacks targets around you. (by tp to them and back)", category=Category.COMBAT, cnName="\u8fdc\u8ddd\u79bb\u6740\u622e")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010(\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020)H\u0016J\b\u0010+\u001a\u00020)H\u0016J\u0010\u0010,\u001a\u00020)2\u0006\u0010-\u001a\u00020.H\u0007J\u0010\u0010/\u001a\u00020)2\u0006\u0010-\u001a\u000200H\u0007J\u0010\u00101\u001a\u00020)2\u0006\u0010-\u001a\u000202H\u0007J\b\u00103\u001a\u00020)H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010$\u001a\u0012\u0012\u0004\u0012\u00020&0%j\b\u0012\u0004\u0012\u00020&`'X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2={"Lnet/dev/important/modules/module/modules/combat/TeleportAura;", "Lnet/dev/important/modules/module/Module;", "()V", "apsValue", "Lnet/dev/important/value/IntegerValue;", "attackDelay", "", "getAttackDelay", "()J", "auraMod", "Lnet/dev/important/modules/module/modules/combat/KillAura;", "clickTimer", "Lnet/dev/important/utils/timer/MSTimer;", "lastTarget", "Lnet/minecraft/entity/EntityLivingBase;", "getLastTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setLastTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "maxMoveDistValue", "Lnet/dev/important/value/FloatValue;", "maxTargetsValue", "noKillAuraValue", "Lnet/dev/important/value/BoolValue;", "noPureC03Value", "priorityValue", "Lnet/dev/important/value/ListValue;", "rangeValue", "renderValue", "swingValue", "tag", "", "getTag", "()Ljava/lang/String;", "thread", "Ljava/lang/Thread;", "tpVectors", "Ljava/util/ArrayList;", "Lnet/minecraft/util/Vec3;", "Lkotlin/collections/ArrayList;", "onDisable", "", "onEnable", "onInitialize", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "runAttack", "LiquidBounce"})
public final class TeleportAura
extends Module {
    @NotNull
    private final IntegerValue apsValue = new IntegerValue("APS", 1, 1, 10);
    @NotNull
    private final IntegerValue maxTargetsValue = new IntegerValue("MaxTargets", 2, 1, 8);
    @NotNull
    private final IntegerValue rangeValue = new IntegerValue("Range", 80, 10, 200, "m");
    @NotNull
    private final FloatValue maxMoveDistValue = new FloatValue("MaxMoveSpeed", 8.0f, 2.0f, 15.0f, "m");
    @NotNull
    private final ListValue swingValue;
    @NotNull
    private final BoolValue noPureC03Value;
    @NotNull
    private final BoolValue noKillAuraValue;
    @NotNull
    private final ListValue renderValue;
    @NotNull
    private final ListValue priorityValue;
    @NotNull
    private final MSTimer clickTimer;
    @NotNull
    private ArrayList<Vec3> tpVectors;
    @Nullable
    private Thread thread;
    @Nullable
    private EntityLivingBase lastTarget;
    private KillAura auraMod;

    public TeleportAura() {
        String[] stringArray = new String[]{"Normal", "Packet", "None"};
        this.swingValue = new ListValue("Swing", stringArray, "Normal");
        this.noPureC03Value = new BoolValue("NoStandingPackets", true);
        this.noKillAuraValue = new BoolValue("NoKillAura", true);
        stringArray = new String[]{"Box", "Lines", "None"};
        this.renderValue = new ListValue("Render", stringArray, "Box");
        stringArray = new String[]{"Health", "Distance", "LivingTime"};
        this.priorityValue = new ListValue("Priority", stringArray, "Distance");
        this.clickTimer = new MSTimer();
        this.tpVectors = new ArrayList();
    }

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
        Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
        Intrinsics.checkNotNull(module2);
        this.auraMod = (KillAura)module2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.noKillAuraValue.get()).booleanValue()) {
            KillAura killAura = this.auraMod;
            if (killAura == null) {
                Intrinsics.throwUninitializedPropertyAccessException("auraMod");
                killAura = null;
            }
            if (killAura.getTarget() != null) return;
        }
        if (!this.clickTimer.hasTimePassed(this.getAttackDelay())) {
            return;
        }
        if (this.thread != null) {
            Thread thread2 = this.thread;
            Intrinsics.checkNotNull(thread2);
            if (thread2.isAlive()) {
                this.clickTimer.reset();
                return;
            }
        }
        this.tpVectors.clear();
        this.clickTimer.reset();
        Thread thread3 = this.thread = new Thread(() -> TeleportAura.onUpdate$lambda-0(this));
        Intrinsics.checkNotNull(thread3);
        thread3.start();
    }

    private final void runAttack() {
        block33: {
            block32: {
                block31: {
                    if (!((Boolean)this.noKillAuraValue.get()).booleanValue()) break block31;
                    KillAura killAura = this.auraMod;
                    if (killAura == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("auraMod");
                        killAura = null;
                    }
                    if (killAura.getTarget() != null) break block32;
                }
                if (MinecraftInstance.mc.field_71439_g != null && MinecraftInstance.mc.field_71441_e != null) break block33;
            }
            return;
        }
        ArrayList<Entity> targets = new ArrayList<Entity>();
        int entityCount = 0;
        for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityLivingBase) || !EntityUtils.isSelected(entity, true) || !(MinecraftInstance.mc.field_71439_g.func_70032_d(entity) <= (float)((Number)this.rangeValue.get()).intValue())) continue;
            if (entityCount > ((Number)this.maxTargetsValue.get()).intValue()) break;
            targets.add(entity);
            int n = entityCount;
            entityCount = n + 1;
        }
        if (targets.isEmpty()) {
            this.lastTarget = null;
            return;
        }
        String string = ((String)this.priorityValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "distance": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        Comparable comparable = Float.valueOf(TeleportAura.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)it));
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        return ComparisonsKt.compareValues(comparable2, (Comparable)Float.valueOf(TeleportAura.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)it)));
                    }
                });
                break;
            }
            case "health": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        Comparable comparable = Float.valueOf(it.func_110143_aJ());
                        it = (EntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        return ComparisonsKt.compareValues(comparable2, (Comparable)Float.valueOf(it.func_110143_aJ()));
                    }
                });
                break;
            }
            case "livingtime": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EntityLivingBase it = (EntityLivingBase)a;
                        boolean bl = false;
                        it = (EntityLivingBase)b;
                        Comparable comparable = Integer.valueOf(-it.field_70173_aa);
                        bl = false;
                        return ComparisonsKt.compareValues(comparable, -it.field_70173_aa);
                    }
                });
            }
        }
        Iterable $this$forEach$iv = targets;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Vec3 point;
            Object element$iv2;
            EntityLivingBase it = (EntityLivingBase)element$iv;
            boolean bl = false;
            if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71441_e == null) {
                return;
            }
            ArrayList<Vec3> path = PathUtils.findTeleportPath((EntityLivingBase)MinecraftInstance.mc.field_71439_g, it, ((Number)this.maxMoveDistValue.get()).floatValue());
            if (((Boolean)this.noKillAuraValue.get()).booleanValue()) {
                KillAura killAura = this.auraMod;
                if (killAura == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("auraMod");
                    killAura = null;
                }
                if (killAura.getTarget() != null) {
                    return;
                }
            }
            Intrinsics.checkNotNullExpressionValue(path, "path");
            Object $this$forEach$iv2 = path;
            boolean $i$f$forEach2 = false;
            Iterator iterator2 = $this$forEach$iv2.iterator();
            while (iterator2.hasNext()) {
                element$iv2 = iterator2.next();
                point = (Vec3)element$iv2;
                boolean bl2 = false;
                this.tpVectors.add(point);
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(point.field_72450_a, point.field_72448_b, point.field_72449_c, true));
            }
            this.setLastTarget(it);
            iterator2 = ((String)this.swingValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(iterator2, "this as java.lang.String).toLowerCase()");
            $this$forEach$iv2 = iterator2;
            if (Intrinsics.areEqual($this$forEach$iv2, "normal")) {
                MinecraftInstance.mc.field_71439_g.func_71038_i();
            } else if (Intrinsics.areEqual($this$forEach$iv2, "packet")) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
            }
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)it, C02PacketUseEntity.Action.ATTACK));
            $this$forEach$iv2 = CollectionsKt.reversed((Iterable)path);
            $i$f$forEach2 = false;
            iterator2 = $this$forEach$iv2.iterator();
            while (iterator2.hasNext()) {
                element$iv2 = iterator2.next();
                point = (Vec3)element$iv2;
                boolean bl3 = false;
                if (StringsKt.equals((String)this.renderValue.get(), "lines", true)) {
                    this.tpVectors.add(point);
                }
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(point.field_72450_a, point.field_72448_b, point.field_72449_c, true));
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
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
        Intrinsics.checkNotNullParameter(event, "event");
        ArrayList<Vec3> arrayList = this.tpVectors;
        synchronized (arrayList) {
            boolean bl = false;
            if (StringsKt.equals((String)this.renderValue.get(), "none", true) || this.tpVectors.isEmpty()) {
                return;
            }
            double renderPosX = MinecraftInstance.mc.func_175598_ae().field_78730_l;
            double renderPosY = MinecraftInstance.mc.func_175598_ae().field_78731_m;
            double renderPosZ = MinecraftInstance.mc.func_175598_ae().field_78728_n;
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
            MinecraftInstance.mc.field_71460_t.func_78479_a(MinecraftInstance.mc.field_71428_T.field_74281_c, 2);
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
                    double height = MinecraftInstance.mc.field_71439_g.func_70047_e();
                    String string = ((String)this.renderValue.get()).toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                    String string2 = string;
                    if (Intrinsics.areEqual(string2, "box")) {
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
                        continue;
                    }
                    if (!Intrinsics.areEqual(string2, "lines")) continue;
                    GL11.glVertex3d((double)x, (double)y, (double)z);
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

    private static final void onUpdate$lambda-0(TeleportAura this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.runAttack();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

