package me.offeex.bloomware.api.gui.screen.particle;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.module.client.Gui;
import me.offeex.bloomware.client.setting.settings.SettingNumber;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;



public class ParticleManager {
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();
    private final Random random = new Random();

    private void spawn() {
        Gui module = Gui.INSTANCE;
        for (int i = 0; i < module.getAmount().getValue() - particles.size(); i++) {
            Particle particle = generate(module);
            particles.add(particle);
        }
    }

    public void render(MatrixStack stack) {
        Gui module = Gui.INSTANCE;
        spawn();
        for (Particle p : particles) {
            p.move();
            p.updateMovement();
            p.draw(stack, module.getGlow().getToggled());
            if (p.getX() > Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() + 5 || p.getX() < -5 || p.getY() > Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() + 5 || p.getY() < -5)
                particles.remove(p);
        }
    }

    private Particle generate(Gui m) {
        return new Particle(
                random.nextInt(0, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth()),
                particles.size() < m.getAmount().getValue() ? random.nextInt(-Bloomware.INSTANCE.getMc().getWindow().getScaledHeight(), -5) : -5,
                randomDiff(m.getSizeValue(), m.getSizeDiff()),
                randomDiff(m.getSpeedValue(), m.getSpeedDiff()),
                randomDiff(m.getWindValue(), m.getWindDiff()),
                m.getColorMode().like("Static") ? m.getColorParticle().getColor() : ColorMutable.Companion.random());
    }

    private boolean diffCheck(SettingNumber value, SettingNumber diff) {
        return value.getValue() - diff.getValue() > 0 && diff.getValue() != 0;
    }

    private int randomDiff(SettingNumber value, SettingNumber diff) {
        return diffCheck(value, diff) ? random.nextInt((int) (value.getValue() - diff.getValue()), (int) (value.getValue() + diff.getValue())) : (int) value.getValue();
    }
}
