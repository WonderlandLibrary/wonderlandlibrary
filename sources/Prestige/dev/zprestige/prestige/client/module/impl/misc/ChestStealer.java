/*
on render? I wonder what we are rendering - thnkscj
*/
package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BindSetting;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class ChestStealer extends Module {

    public DragSetting delay;
    public BooleanSetting close;
    public BindSetting key;
    public TimerUtil timer;

    public ChestStealer() {
        super("Chest Stealer", Category.Misc, "Steals items out of chests");
        delay = setting("Delay", 100, 200, 0, 1000).description("Delay between each action");
        close = setting("Close", true).description("Closes the chest when it's empty");
        key = setting("Activate Key", -1).description("Key to activate the chest stealer");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (key.getObject() == -1 || GLFW.glfwGetKey(getMc().getWindow().getHandle(), key.getObject()) != 1) {
            return;
        }
        if (timer.delay(delay)) {
            if (getMc().player.currentScreenHandler instanceof GenericContainerScreenHandler container) {
                for (int i = 0; i < container.getInventory().size(); i++) {
                    Slot slot = container.getSlot(i);
                    if (slot.hasStack()) {
                        getMc().interactionManager.clickSlot(getMc().player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, getMc().player);
                        timer.reset();
                        delay.setValue();
                        return;
                    }
                }
                if (hasItem(container) && close.getObject()) {
                    getMc().player.closeScreen();
                }
            }
        }
    }

    boolean hasItem(GenericContainerScreenHandler container) {
        for (int i = 0; i < (container.getInventory().size() == 90 ? 54 : 27); ++i) {
            if (container.getSlot(i).hasStack()) {
                return false;
            }
        }
        return true;
    }
}