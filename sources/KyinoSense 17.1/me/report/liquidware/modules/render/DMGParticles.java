/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.modules.render;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.modules.render.Particle;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="DMGParticles", description="Allows you to see targets damage.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0014H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lme/report/liquidware/modules/render/DMGParticles;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aliveTicks", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "healthData", "Ljava/util/HashMap;", "", "", "particles", "Ljava/util/ArrayList;", "Lme/report/liquidware/modules/render/Particle;", "sizeValue", "onRender3d", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class DMGParticles
extends Module {
    private final HashMap<Integer, Float> healthData = new HashMap();
    private final ArrayList<Particle> particles = new ArrayList();
    private final IntegerValue aliveTicks = new IntegerValue("AliveTime", 1000, 1, 5000);
    private final IntegerValue sizeValue = new IntegerValue("Size", 3, 1, 7);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        ArrayList<Particle> arrayList = this.particles;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (arrayList) {
            boolean bl3 = false;
            for (Entity entity : DMGParticles.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityLivingBase) || !EntityUtils.isSelected(entity, true)) continue;
                Float f = this.healthData.getOrDefault(((EntityLivingBase)entity).func_145782_y(), Float.valueOf(((EntityLivingBase)entity).func_110138_aP()));
                Intrinsics.checkExpressionValueIsNotNull(f, "healthData.getOrDefault(\u2026ntityId,entity.maxHealth)");
                float lastHealth = ((Number)f).floatValue();
                ((Map)this.healthData).put(((EntityLivingBase)entity).func_145782_y(), Float.valueOf(((EntityLivingBase)entity).func_110143_aJ()));
                if (lastHealth == ((EntityLivingBase)entity).func_110143_aJ()) continue;
                String prefix = lastHealth > ((EntityLivingBase)entity).func_110143_aJ() ? "\u00a7e" : "\u00a7e";
                float f2 = lastHealth - ((EntityLivingBase)entity).func_110143_aJ();
                StringBuilder stringBuilder = new StringBuilder().append(prefix);
                ArrayList<Particle> arrayList2 = this.particles;
                boolean bl4 = false;
                float f3 = Math.abs(f2);
                double d = f3;
                double d2 = entity.field_70161_v - 0.5 + (double)new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.1;
                double d3 = ((EntityLivingBase)entity).func_174813_aQ().field_72338_b + (((EntityLivingBase)entity).func_174813_aQ().field_72337_e - ((EntityLivingBase)entity).func_174813_aQ().field_72338_b) / 2.0;
                double d4 = entity.field_70165_t - 0.5 + (double)new Random(System.currentTimeMillis()).nextInt(5) * 0.1;
                String string = stringBuilder.append(new BigDecimal(d).setScale(1, 4).doubleValue()).toString();
                arrayList2.add(new Particle(string, d4, d3, d2));
            }
            ArrayList<Particle> needRemove = new ArrayList<Particle>();
            Iterable $this$forEach$iv = this.particles;
            boolean $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                Particle it = (Particle)element$iv;
                boolean bl5 = false;
                Particle particle = it;
                int n = particle.getTicks();
                particle.setTicks(n + 1);
                if (it.getTicks() <= ((Number)this.aliveTicks.get()).intValue() / 50) continue;
                needRemove.add(it);
            }
            $this$forEach$iv = needRemove;
            $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                Particle it = (Particle)element$iv;
                boolean bl6 = false;
                this.particles.remove(it);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public final void onRender3d(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        ArrayList<Particle> arrayList = this.particles;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (arrayList) {
            boolean bl3 = false;
            Minecraft minecraft = DMGParticles.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            RenderManager renderManager = minecraft.func_175598_ae();
            double size = ((Number)this.sizeValue.get()).doubleValue() * 0.01;
            for (Particle particle : this.particles) {
                double n = particle.getPosX() - renderManager.field_78725_b;
                double n2 = particle.getPosY() - renderManager.field_78726_c;
                double n3 = particle.getPosZ() - renderManager.field_78723_d;
                GlStateManager.func_179094_E();
                GlStateManager.func_179088_q();
                GlStateManager.func_179136_a((float)1.0f, (float)-1500000.0f);
                GlStateManager.func_179109_b((float)((float)n), (float)((float)n2), (float)((float)n3));
                GlStateManager.func_179114_b((float)(-renderManager.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
                float textY = DMGParticles.access$getMc$p$s1046033730().field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f;
                GlStateManager.func_179114_b((float)renderManager.field_78732_j, (float)textY, (float)0.0f, (float)0.0f);
                GlStateManager.func_179139_a((double)(-size), (double)(-size), (double)size);
                GL11.glDepthMask((boolean)false);
                DMGParticles.access$getMc$p$s1046033730().field_71466_p.func_175063_a(particle.getStr(), (float)(-(DMGParticles.access$getMc$p$s1046033730().field_71466_p.func_78256_a(particle.getStr()) / 2)), (float)(-(DMGParticles.access$getMc$p$s1046033730().field_71466_p.field_78288_b - 1)), 0);
                GL11.glColor4f((float)255.0f, (float)255.0f, (float)255.0f, (float)1.0f);
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
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.particles.clear();
        this.healthData.clear();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

