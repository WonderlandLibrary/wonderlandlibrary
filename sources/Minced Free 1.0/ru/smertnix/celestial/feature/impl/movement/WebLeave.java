package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventMove;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class WebLeave extends Feature {
    public NumberSetting webSpeed;
    public WebLeave() {
        super("Web Leave", "Автоматически подкидывает в небо в паутинке", FeatureCategory.Movement);
        webSpeed = new NumberSetting("Motion Value", 10, 1, 10, 1, () -> true);
        addSettings(webSpeed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.6, mc.player.posZ);
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if (mc.player.isInWeb) {
            	 mc.player.isInWeb = false;
                mc.player.motionY += webSpeed.getNumberValue();
            } else if (Block.getIdFromBlock(block) == 30) {
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    return;
                mc.player.isInWeb = false;
                mc.gameSettings.keyBindJump.pressed = false;
            }
    }
}
