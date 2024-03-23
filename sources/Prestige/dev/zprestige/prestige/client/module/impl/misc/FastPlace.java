package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.api.mixin.IMinecraftClient;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import net.minecraft.item.BlockItem;

public class FastPlace extends Module {

    public BooleanSetting blocksOnly;
    public IntSetting minSpeed;
    public IntSetting maxSpeed;

    public FastPlace() {
        super("Fast Place", Category.Misc, "Allows you to place blocks faster");
        blocksOnly = setting("Blocks Only", false);
        minSpeed = setting("Min Speed", 0, 0, 4);
        maxSpeed = setting("Max Speed", 1, 0, 4);
    }

    @EventListener
    public void event(TickEvent event) {
        if (blocksOnly.getObject() && !(getMc().player.getMainHandStack().getItem() instanceof BlockItem) && !(getMc().player.getOffHandStack().getItem() instanceof BlockItem)) {
            return;
        }
        if (minSpeed.getObject() > ((Number) maxSpeed.getObject()).intValue()) {
            minSpeed.invokeValue(maxSpeed.getObject());
        }
        int n = Math.min(((IMinecraftClient)getMc()).getItemUseCooldown(), RandomUtil.INSTANCE.randomInRange(minSpeed.getObject(), maxSpeed.getObject()));
        ((IMinecraftClient)getMc()).setItemUseCooldown(n);
    }
}
