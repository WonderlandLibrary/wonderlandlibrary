package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventWebSolid;
import ru.smertnix.celestial.event.events.impl.player.EventMove;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class NoWeb extends Feature {
	public static BooleanSetting solid = new BooleanSetting("Solid", true, () -> true);
    public NoWeb() {
        super("Anti Web", "Вы можете двигаться в паутинке на Matrix AntiCheat", FeatureCategory.Movement);
        addSettings(solid);
    }


    @EventTarget
    public void onMove(EventMove event) {
        if (isEnabled()) {
                if (mc.player.isInWeb) {
                    mc.player.motionY += 2F;
                } else {
                    if (mc.gameSettings.keyBindJump.isKeyDown())
                        return;
                    mc.player.isInWeb = false;
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    return;
                }
                if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementUtils.setEventSpeed(event, 0.483);
                }
        }
    }
    @EventTarget
    public void onWebSolid(EventWebSolid event) {
    	if (solid.getBoolValue())
        event.setCancelled(true);
    }
}
