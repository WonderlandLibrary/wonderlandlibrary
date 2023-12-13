/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render.particles;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.misc.EvictingList;
import dev.rise.util.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

import java.util.List;

@ModuleInfo(name = "Particles", description = "Renders particles when you attack someone", category = Category.RENDER)
public final class Particles extends Module {

    private final NumberSetting amount = new NumberSetting("Amount", this, 10, 1, 20, 1);
    private final BooleanSetting physics = new BooleanSetting("Physics", this, true);

    private final List<Particle> particles = new EvictingList<>(100);
    private final TimeUtil timer = new TimeUtil();
    private EntityLivingBase target;

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTarget();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
            for (int i = 0; i < amount.getValue(); i++)
                particles.add(new Particle(new Vec3(target.posX + (Math.random() - 0.5) * 0.5, target.posY + Math.random() * 1 + 0.5, target.posZ + (Math.random() - 0.5) * 0.5)));

            target = null;
        }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (particles.isEmpty())
            return;

        for (int i = 0; i <= timer.getElapsedTime() / 1E+11; i++) {
            if (physics.isEnabled())
                particles.forEach(Particle::update);
            else
                particles.forEach(Particle::updateWithoutPhysics);
        }

        particles.removeIf(particle -> mc.thePlayer.getDistanceSq(particle.getPosition().xCoord, particle.getPosition().yCoord, particle.getPosition().zCoord) > 50 * 10);

        timer.reset();

        RenderUtil.renderParticles(particles);
    }
}