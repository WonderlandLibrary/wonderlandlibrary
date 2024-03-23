package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.ReachEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.util.impl.RandomUtil;

public class Reach extends Module {

    public FloatSetting chance;
    public DragSetting reach;

    public Reach() {
        super("Reach", Category.Misc, "Increases your reach");
        chance = setting("Chance", 60f, 0f, 100f).description("Chance to increase your reach");
        reach = setting("Reach", 0f, 0.1f, 0f, 2f).description("How much to increase your reach");
    }

    @EventListener
    public void event(ReachEvent reachEvent) {
        if (RandomUtil.INSTANCE.getRandom().nextFloat(100) <= chance.getObject()) {
            reachEvent.setReach(reach.getRandomValue());
            reachEvent.setCancelled();
        }
    }
}
