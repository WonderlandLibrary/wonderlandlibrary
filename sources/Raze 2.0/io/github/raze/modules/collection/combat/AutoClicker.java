package io.github.raze.modules.collection.combat;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.TimeUtil;
import org.lwjgl.input.Mouse;

import java.util.Random;

public class AutoClicker extends AbstractModule {

    private final ArraySetting mode;
    private final NumberSetting cps, randomizer;
    private final BooleanSetting randomize,holdToClick,blockHit;

    private final TimeUtil timer;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks when holding down the attack button.", ModuleCategory.COMBAT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Clicking Mode", "Left", "Left", "Right"),

                cps = new NumberSetting(this, "CPS", 0, 30, 12),

                randomize = new BooleanSetting(this, "Randomize", false),
                randomizer = new NumberSetting(this, "Randomizer", 0, 7, 3)
                        .setHidden(() -> !randomize.get()),

                holdToClick = new BooleanSetting(this, "Hold to Click", true),
                blockHit = new BooleanSetting(this, "Allow BlockHitting", false)

        );
        timer = new TimeUtil();

    }

    public void click() {
        if (holdToClick.get()) {
            switch (mode.get()) {
                case "Right":
                    if (Mouse.isButtonDown(1))
                        mc.rightClickMouse();
                    break;
                case "Left":
                    if (Mouse.isButtonDown(0))
                        mc.clickMouse();
                    break;
            }
        } else {
            switch (mode.get()) {
                case "Right":
                    mc.rightClickMouse();
                    break;
                case "Left":
                    mc.clickMouse();
                    break;
            }
        }
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        Random random = new Random();
        int currentSpeed = cps.get().intValue();
        int newSpeed;

        if (randomize.get()) {
            int randomNum = random.nextInt(5);
            newSpeed = (currentSpeed + randomNum - randomizer.get().intValue());
        } else {
            newSpeed = currentSpeed;
        }

        int maxSpeed = cps.max().intValue();

        if (newSpeed > maxSpeed) {
            newSpeed = maxSpeed;
        } else if (newSpeed < 0) {
            newSpeed = 0;
        }


        if(mc.currentScreen != null)
            return;
        if(blockHit.get()) {
            if (timer.elapsed((1000 / newSpeed), true)) {
                click();
            }
        } else {
            if(!mc.thePlayer.isBlocking() && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isEating()) {
                if (timer.elapsed((1000 / newSpeed), true)) {
                    click();
                }
            }
        }

    }

}
