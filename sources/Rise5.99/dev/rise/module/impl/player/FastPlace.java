/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.item.ItemBlock;
import org.apache.commons.lang3.RandomUtils;

/**
 * This module reduces/removes the minecraft right click delayer in order to
 * allow player to place blocks easily by just holding right click.
 */
@ModuleInfo(name = "FastPlace", description = "Changes the right click delay timer", category = Category.PLAYER)
public final class FastPlace extends Module {

    private final NumberSetting delay = new NumberSetting("FastPlace Delay", this, 0, 0, 4, 0.1);
    private final BooleanSetting blocksOnly = new BooleanSetting("Blocks Only", this, true);
    private final BooleanSetting randomize = new BooleanSetting("Randomize", this, false);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (blocksOnly.isEnabled() && !(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock))
            return;

        int delay = (int) this.delay.getValue();

        if (delay == 0) {
            if (randomize.isEnabled()) {
                delay = RandomUtils.nextInt(0, delay);
            }

            mc.rightClickDelayTimer = delay;
        } else {
            if (mc.rightClickDelayTimer > delay) {
                if (randomize.isEnabled()) {
                    delay = RandomUtils.nextInt(0, delay);
                }

                mc.rightClickDelayTimer = delay;
            }
        }
    }
}
