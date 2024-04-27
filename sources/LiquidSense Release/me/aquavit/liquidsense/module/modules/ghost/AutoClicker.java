package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.settings.KeyBinding;

import java.util.Random;

@ModuleInfo(name = "AutoClicker", description = "Constantly clicks when holding down a mouse button.", category = ModuleCategory.GHOST)
public class AutoClicker extends Module {

    private IntegerValue maxCPSValue = new IntegerValue("MaxCPS", 8, 1, 20) {

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            final int minCPS = minCPSValue.get();
            if (minCPS > newValue)
                set(minCPS);
        }

    };

    private IntegerValue minCPSValue = new IntegerValue("MinCPS", 5, 1, 20) {

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            final int  maxCPS = maxCPSValue.get();
            if (maxCPS < newValue)
                set(maxCPS);
        }

    };

    private BoolValue rightValue = new BoolValue("Right", true);
    private BoolValue leftValue = new BoolValue("Left", true);
    private BoolValue jitterValue = new BoolValue("Jitter", false);

    private long rightDelay = TimeUtils.randomClickDelay(minCPSValue.get(), maxCPSValue.get());
    private long rightLastSwing = 0L;
    private long leftDelay = TimeUtils.randomClickDelay(minCPSValue.get(), maxCPSValue.get());
    private long leftLastSwing = 0L;

    @EventTarget
    public void onRender(Render3DEvent event) {
        // Left click
        if (mc.gameSettings.keyBindAttack.isKeyDown() && leftValue.get() &&
                System.currentTimeMillis() - leftLastSwing >= leftDelay) {
            KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode()) ;// Minecraft Click Handling

            leftLastSwing = System.currentTimeMillis();
            leftDelay = TimeUtils.randomClickDelay(minCPSValue.get(), maxCPSValue.get());
        }

        // Right click
        if (mc.gameSettings.keyBindUseItem.isKeyDown() && !mc.thePlayer.isUsingItem() && rightValue.get() &&
                System.currentTimeMillis() - rightLastSwing >= rightDelay) {
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode()); // Minecraft Click Handling

            rightLastSwing = System.currentTimeMillis();
            rightDelay = TimeUtils.randomClickDelay(minCPSValue.get(), maxCPSValue.get());
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (jitterValue.get() && (leftValue.get() && mc.gameSettings.keyBindAttack.isKeyDown() || rightValue.get() && mc.gameSettings.keyBindUseItem.isKeyDown() && !mc.thePlayer.isUsingItem())) {
            if (new Random().nextBoolean()) mc.thePlayer.rotationYaw += new Random().nextBoolean() ? -RandomUtils.nextFloat(0F, 1F) : RandomUtils.nextFloat(0F, 1F);

            if (new Random().nextBoolean()) {
                mc.thePlayer.rotationPitch += new Random().nextBoolean() ? -RandomUtils.nextFloat(0F, 1F) : RandomUtils.nextFloat(0F, 1F);

                // Make sure pitch is not going into unlegit values
                if (mc.thePlayer.rotationPitch > 90)
                    mc.thePlayer.rotationPitch = 90F;
                else if (mc.thePlayer.rotationPitch < -90)
                    mc.thePlayer.rotationPitch = -90F;
            }
        }
    }
}
