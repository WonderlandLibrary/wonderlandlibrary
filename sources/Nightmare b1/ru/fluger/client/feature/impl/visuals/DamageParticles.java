// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.helpers.render.RenderHelper;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import java.util.Iterator;
import com.ibm.icu.math.BigDecimal;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.RespawnEvent;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ru.fluger.client.feature.impl.Type;
import java.util.List;
import java.util.Map;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class DamageParticles extends Feature
{
    public TimerHelper timerHelper;
    public NumberSetting deleteAfter;
    public ColorSetting colorOnHit;
    public ColorSetting colorOnHeal;
    private final Map<Integer, Float> hpData;
    private final List<Particle> particles;
    
    public DamageParticles() {
        super("DamageParticles", "\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0430\u0435\u0442 \u0434\u0430\u043c\u0430\u0433-\u043f\u0430\u0440\u0442\u0438\u043a\u043b\u044b \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Visuals);
        this.timerHelper = new TimerHelper();
        this.hpData = (Map<Integer, Float>)Maps.newHashMap();
        this.particles = (List<Particle>)Lists.newArrayList();
        this.deleteAfter = new NumberSetting("Delete After", 7.0f, 1.0f, 20.0f, 1.0f, () -> true);
        this.colorOnHit = new ColorSetting("Color on Hit", new Color(1, 182, 83, 25).getRGB(), () -> true);
        this.colorOnHeal = new ColorSetting("Color on Heal", new Color(1, 182, 149, 25).getRGB(), () -> true);
        this.addSettings(this.deleteAfter, this.colorOnHit, this.colorOnHeal);
    }
    
    @EventTarget
    public void onRespawn(final RespawnEvent event) {
        this.particles.clear();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        for (final vg entity : DamageParticles.mc.f.e) {
            if (entity instanceof vp) {
                final vp ent = (vp)entity;
                final double lastHp = this.hpData.getOrDefault(ent.S(), ent.cj());
                this.hpData.remove(entity.S());
                this.hpData.put(entity.S(), ent.cd());
                if (lastHp == ent.cd()) {
                    continue;
                }
                Color color;
                if (lastHp > ent.cd()) {
                    color = new Color(this.colorOnHit.getColor());
                }
                else {
                    color = new Color(this.colorOnHeal.getColor());
                }
                final bhe loc = new bhe(entity.p + Math.random() * 0.5 * ((Math.random() > 0.5) ? -1 : 1), entity.bw().b + (entity.bw().e - entity.bw().b) * 0.5, entity.r + Math.random() * 0.5 * ((Math.random() > 0.5) ? -1 : 1));
                final double str = new BigDecimal(Math.abs(lastHp - ent.cd())).setScale(1, 4).doubleValue();
                this.particles.add(new Particle("" + str, loc.b, loc.c, loc.d, color));
            }
        }
    }
    
    @EventTarget
    public void onRender3d(final EventRender3D e) {
        if (this.timerHelper.hasReached(this.deleteAfter.getCurrentValue() * 300.0f)) {
            this.particles.clear();
            this.timerHelper.reset();
        }
        if (!this.particles.isEmpty()) {
            for (final Particle p : this.particles) {
                if (p != null) {
                    bus.G();
                    bus.s();
                    bus.a(1.0f, -1500000.0f);
                    bus.b(p.posX - DamageParticles.mc.ac().o, p.posY - DamageParticles.mc.ac().p, p.posZ - DamageParticles.mc.ac().q);
                    bus.b(-DamageParticles.mc.ac().e, 0.0f, 1.0f, 0.0f);
                    bus.b(DamageParticles.mc.ac().f, (DamageParticles.mc.t.aw == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
                    bus.a(-0.03, -0.03, 0.03);
                    GL11.glDepthMask(false);
                    DamageParticles.mc.k.drawBlurredString(p.str, (float)(-DamageParticles.mc.k.a(p.str) * 0.5), -DamageParticles.mc.k.a + 1, 8, RenderHelper.injectAlpha(p.color, 100), p.color.getRGB());
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glDepthMask(true);
                    bus.a(1.0f, 1500000.0f);
                    bus.t();
                    bus.I();
                    bus.H();
                }
            }
        }
    }
    
    class Particle
    {
        public String str;
        public double posX;
        public double posY;
        public double posZ;
        public Color color;
        public int ticks;
        
        public Particle(final String str, final double posX, final double posY, final double posZ, final Color color) {
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.color = color;
            this.ticks = 0;
        }
    }
}
