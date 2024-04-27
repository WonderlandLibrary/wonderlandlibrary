/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.render.SingleParticle;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Info(name="DamageParticle", spacedName="Damage Particle", description="Allows you to see targets damage.", category=Category.RENDER, cnName="\u4f24\u5bb3\u663e\u793a")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0017H\u0007J\u0010\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/dev/important/modules/module/modules/render/DamageParticle;", "Lnet/dev/important/modules/module/Module;", "()V", "aliveTicks", "Lnet/dev/important/value/IntegerValue;", "blue", "customColor", "Lnet/dev/important/value/BoolValue;", "green", "healthData", "Ljava/util/HashMap;", "", "", "particles", "Ljava/util/ArrayList;", "Lnet/dev/important/modules/module/modules/render/SingleParticle;", "red", "sizeValue", "onRender3d", "", "event", "Lnet/dev/important/event/Render3DEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "LiquidBounce"})
public final class DamageParticle
extends Module {
    @NotNull
    private final HashMap<Integer, Float> healthData = new HashMap();
    @NotNull
    private final ArrayList<SingleParticle> particles = new ArrayList();
    @NotNull
    private final IntegerValue aliveTicks = new IntegerValue("AliveTicks", 20, 10, 50);
    @NotNull
    private final IntegerValue sizeValue = new IntegerValue("Size", 3, 1, 7);
    @NotNull
    private final BoolValue customColor = new BoolValue("CustomColor", false);
    @NotNull
    private final IntegerValue red = new IntegerValue("Red", 255, 0, 255, new Function0<Boolean>(this){
        final /* synthetic */ DamageParticle this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)DamageParticle.access$getCustomColor$p(this.this$0).get();
        }
    });
    @NotNull
    private final IntegerValue green = new IntegerValue("Green", 255, 0, 255, new Function0<Boolean>(this){
        final /* synthetic */ DamageParticle this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)DamageParticle.access$getCustomColor$p(this.this$0).get();
        }
    });
    @NotNull
    private final IntegerValue blue = new IntegerValue("Blue", 255, 0, 255, new Function0<Boolean>(this){
        final /* synthetic */ DamageParticle this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)DamageParticle.access$getCustomColor$p(this.this$0).get();
        }
    });

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        ArrayList<SingleParticle> arrayList = this.particles;
        synchronized (arrayList) {
            boolean bl = false;
            for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityLivingBase) || !EntityUtils.isSelected(entity, true)) continue;
                Float f = this.healthData.getOrDefault(((EntityLivingBase)entity).func_145782_y(), Float.valueOf(((EntityLivingBase)entity).func_110138_aP()));
                Intrinsics.checkNotNullExpressionValue(f, "healthData.getOrDefault(\u2026ntityId,entity.maxHealth)");
                float lastHealth = ((Number)f).floatValue();
                ((Map)this.healthData).put(((EntityLivingBase)entity).func_145782_y(), Float.valueOf(((EntityLivingBase)entity).func_110143_aJ()));
                if (lastHealth == ((EntityLivingBase)entity).func_110143_aJ()) continue;
                String prefix = !((Boolean)this.customColor.get()).booleanValue() ? (lastHealth > ((EntityLivingBase)entity).func_110143_aJ() ? "\u00a7c" : "\u00a7a") : (lastHealth > ((EntityLivingBase)entity).func_110143_aJ() ? "-" : "+");
                this.particles.add(new SingleParticle(Intrinsics.stringPlus(prefix, new BigDecimal(Math.abs(lastHealth - ((EntityLivingBase)entity).func_110143_aJ())).setScale(1, 4).doubleValue()), entity.field_70165_t - 0.5 + (double)new Random(System.currentTimeMillis()).nextInt(5) * 0.1, ((EntityLivingBase)entity).func_174813_aQ().field_72338_b + (((EntityLivingBase)entity).func_174813_aQ().field_72337_e - ((EntityLivingBase)entity).func_174813_aQ().field_72338_b) / 2.0, entity.field_70161_v - 0.5 + (double)new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.1));
            }
            ArrayList<SingleParticle> needRemove = new ArrayList<SingleParticle>();
            for (SingleParticle particle : this.particles) {
                int n = particle.getTicks();
                particle.setTicks(n + 1);
                if (particle.getTicks() <= ((Number)this.aliveTicks.get()).intValue()) continue;
                needRemove.add(particle);
            }
            for (SingleParticle particle : needRemove) {
                this.particles.remove(particle);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public final void onRender3d(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        ArrayList<SingleParticle> arrayList = this.particles;
        synchronized (arrayList) {
            boolean bl = false;
            RenderManager renderManager = MinecraftInstance.mc.func_175598_ae();
            double size = ((Number)this.sizeValue.get()).doubleValue() * 0.01;
            for (SingleParticle particle : this.particles) {
                double n = particle.getPosX() - renderManager.field_78725_b;
                double n2 = particle.getPosY() - renderManager.field_78726_c;
                double n3 = particle.getPosZ() - renderManager.field_78723_d;
                GlStateManager.func_179094_E();
                GlStateManager.func_179088_q();
                GlStateManager.func_179136_a((float)1.0f, (float)-1500000.0f);
                GlStateManager.func_179109_b((float)((float)n), (float)((float)n2), (float)((float)n3));
                GlStateManager.func_179114_b((float)(-renderManager.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
                float textY = MinecraftInstance.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f;
                GlStateManager.func_179114_b((float)renderManager.field_78732_j, (float)textY, (float)0.0f, (float)0.0f);
                GlStateManager.func_179139_a((double)(-size), (double)(-size), (double)size);
                GL11.glDepthMask((boolean)false);
                MinecraftInstance.mc.field_71466_p.func_175063_a(particle.getStr(), (float)(-(MinecraftInstance.mc.field_71466_p.func_78256_a(particle.getStr()) / 2)), (float)(-(MinecraftInstance.mc.field_71466_p.field_78288_b - 1)), (Boolean)this.customColor.get() != false ? new Color(((Number)this.red.get()).intValue(), ((Number)this.green.get()).intValue(), ((Number)this.blue.get()).intValue()).getRGB() : 0);
                GL11.glColor4f((float)187.0f, (float)255.0f, (float)255.0f, (float)1.0f);
                GL11.glDepthMask((boolean)true);
                GlStateManager.func_179136_a((float)1.0f, (float)1500000.0f);
                GlStateManager.func_179113_r();
                GlStateManager.func_179121_F();
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.particles.clear();
        this.healthData.clear();
    }

    public static final /* synthetic */ BoolValue access$getCustomColor$p(DamageParticle $this) {
        return $this.customColor;
    }
}

