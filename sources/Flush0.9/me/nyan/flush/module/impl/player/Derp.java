package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.combat.Aura;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class Derp extends Module {
    private final BooleanSetting hideHead = new BooleanSetting("Hide Head", this,true),
            randomize = new BooleanSetting("Randomize", this, true);
    private final NumberSetting speed = new NumberSetting("Speed", this, 50, 1, 80,
            () -> !randomize.getValue());

    private float yawpitch;
    private final Random random = new Random();

    public Derp() {
        super("Derp", Category.PLAYER);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        float random = (float) MathHelper.getRandomDoubleInRange(this.random, -180, 180);

        if (yawpitch > 180) {
            yawpitch = -180;
        }

        yawpitch += speed.getValueInt();

        if ((!isEnabled(Aura.class) || getModule(Aura.class).rotations.is("none")) || getModule(Aura.class).target == null) {
            e.setYaw(randomize.getValue() ? random : yawpitch);
            e.setPitch(hideHead.getValue() ? 180 : randomize.getValue() ? random : yawpitch);
        }
    }
}
