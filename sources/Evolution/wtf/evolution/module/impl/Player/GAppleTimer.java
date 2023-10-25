package wtf.evolution.module.impl.Player;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import org.lwjgl.input.Mouse;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.event.events.impl.GAppleEatEvent;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "GAppleTimer", type = Category.Player)
public class GAppleTimer extends Module {

    private final SliderSetting cooldown = new SliderSetting("Cooldown", 55, 0, 100, 10).call(this);
    private boolean isEated;

    @Override
    public void onDisable() {
        isEated = false;
    }

    @EventTarget
    public void onEat(GAppleEatEvent event) {
        isEated = true;
        if( mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)
                && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            event.cancel();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (isEated) {
            mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, (int) cooldown.get());
            isEated = false;
        }
        if (mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)
                && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            mc.gameSettings.keyBindUseItem.pressed = (false);
        } else if (Mouse.isButtonDown(1) && !(mc.currentScreen instanceof GuiContainer)) {
            mc.gameSettings.keyBindUseItem.pressed = (true);
        }
    }

}
