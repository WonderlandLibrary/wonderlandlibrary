package ru.smertnix.celestial.feature.impl.movement;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class LongJump extends Feature {
	public int ticks2;
    public ListSetting mode;
    public boolean a;
    public BooleanSetting autoJump = new BooleanSetting("Auto Jump", true, () -> true);
    public BooleanSetting autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
    private final NumberSetting ticks = new NumberSetting("Disable Ticks", 10, 9, 150, 1, () -> true);
    private final NumberSetting boost = new NumberSetting("Boost Multiplier", 0.25f, 0.1f, 0.42f, 0.01f, () -> true);

    public LongJump() {
        super("Long Jump", "Автоматически бустит после получения урона(Работает на SunRise)", FeatureCategory.Movement);
        addSettings(autoJump, autoDisable, ticks, boost);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
   		 if (mc.player.hurtTime > 0) {
   			 a = true;
   		 }
   		 if (a) {
   			 if (mc.player.onGround) {
   				 mc.player.jump();
   			 }
   			 if (mc.player.ticksExisted % 2 == 0)
   			 ticks2+=10f;
   			 mc.player.speedInAir = boost.getNumberValue();
   			 if (ticks2 > ticks.getNumberValue() && autoDisable.getBoolValue()) {
   				 toggle();
   				 a = false;
   			 }
   		 } else {
   			 mc.player.speedInAir = 0.02f;
   		 }
    }
    @Override
    public void onDisable() {
    	a = false;
    	ticks2 = 0;
    	mc.player.speedInAir = 0.02f;
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }
}
