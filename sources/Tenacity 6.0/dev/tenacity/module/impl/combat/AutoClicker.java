package dev.tenacity.module.impl.combat;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.misc.MathUtil;
import dev.tenacity.util.misc.TimerUtil;

public class AutoClicker extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Normal", "Drag");

    // Normal
    private final NumberSetting minCPS = new NumberSetting("Min CPS", 9, 0, 20, 0.01);
    private final NumberSetting maxCPS = new NumberSetting("Max CPS", 11, 0, 20, 0.01);

    // Drag
    private final NumberSetting time = new NumberSetting("Duration (MS)", 1200, 100, 2000, 100);
    private final NumberSetting delay = new NumberSetting("Delay (MS)", 300, 100, 1000, 100);

    // Randomization
    private final BooleanSetting randomizecps = new BooleanSetting("Randomize CPS", false);
    private final TimerUtil clickTimer = new TimerUtil();
    private final TimerUtil dragTimer = new TimerUtil();
    private final TimerUtil delayTimer = new TimerUtil();

    public AutoClicker() {
        super("AutoClicker", "Clicks for you.", ModuleCategory.COMBAT);
        minCPS.addParent(mode, setting -> setting.isMode("Normal"));
        maxCPS.addParent(mode, setting -> setting.isMode("Normal"));
        time.addParent(mode, setting -> setting.isMode("Drag"));
        delay.addParent(mode, setting -> setting.isMode("Drag"));
        initializeSettings(mode, minCPS, maxCPS, time, delay);
    }

    private final IEventListener<MotionEvent> onMotion = event -> {
        if(event.isPost()) return;

        if(minCPS.getCurrentValue() > maxCPS.getCurrentValue()) {
            minCPS.setCurrentValue(maxCPS.getCurrentValue());
        }

        if(maxCPS.getCurrentValue() < minCPS.getCurrentValue()) {
            maxCPS.setCurrentValue(minCPS.getCurrentValue());
        }

        final int maxValue = (int) ((minCPS.getMaxValue() - maxCPS.getCurrentValue()) * 20);
        final int minValue = (int) ((minCPS.getMaxValue() - minCPS.getCurrentValue()) * 20);
        long cps = MathUtil.nextInt(minValue, maxValue);

        switch (mode.getCurrentMode()) {
            case "Normal": {
                if(!mc.gameSettings.keyBindAttack.isKeyDown()) {
                    clickTimer.reset();
                    return;
                }
                if(clickTimer.hasTimeElapsed(cps, true)) {
                    if(mc.gameSettings.keyBindUseItem.isKeyDown()) return;
                    mc.leftClickCounter = 0;
                    mc.clickMouse();
                }
                if(clickTimer.hasTimeElapsed(cps -2, true)) {
                    if(mc.gameSettings.keyBindUseItem.isKeyDown()) return;
                    mc.leftClickCounter = 0;
                    mc.clickMouse();
                }
                break;
            }

            case "Drag": {
                if(!mc.gameSettings.keyBindAttack.isKeyDown()) {
                    clickTimer.reset();
                    dragTimer.reset();
                    delayTimer.reset();
                    return;
                }

                if(dragTimer.hasTimeElapsed((long) time.getCurrentValue(), false)) {
                    if(delayTimer.hasTimeElapsed((long) delay.getCurrentValue(), true)) {
                        dragTimer.reset();
                    }
                } else {
                    delayTimer.reset();

                    if(clickTimer.hasTimeElapsed(cps, true)) {
                        mc.leftClickCounter = 0;
                        mc.clickMouse();
                    }
                }
            }
        }
    };
    private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(mode.getCurrentMode());
}
