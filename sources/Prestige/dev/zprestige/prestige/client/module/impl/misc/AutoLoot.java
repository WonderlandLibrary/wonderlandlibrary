package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BindSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;

import java.util.List;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class AutoLoot extends Module {

    public DragSetting delay;
    public IntSetting minTotems;
    public IntSetting minPearls;
    public BindSetting keyBind;
    public TimerUtil timer;

    public AutoLoot() {
        super("AutoLoot", Category.Misc, "Helps with looting items from the ground");
        delay = setting("Delay", 30, 50, 0, 300).description("Delay between each action");
        minTotems = setting("Min Totems", 0, 1, 20).description("Minimum amount of totems");
        minPearls = setting("Min Pearls", 5, 1, 10).description("Minimum amount of pearls (stacks)");
        keyBind = setting("Key", -1);
        timer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (getMc().currentScreen instanceof InventoryScreen) {
            if (!timer.delay(delay)) {
                return;
            }
            if (keyBind.getObject() == -1) {
                return;
            }
            if (GLFW.glfwGetKey(getMc().getWindow().getHandle(), keyBind.getObject()) == 0) {
                return;
            }
            List<Integer> list = InventoryUtil.INSTANCE.findItemSlot(Items.TOTEM_OF_UNDYING, true);
            if (!list.isEmpty() && list.size() > minTotems.getObject()) {
                InventoryUtil.INSTANCE.performSlotAction(list.get(0) < 9 ? list.get(0) + 36 : list.get(0), 1, SlotActionType.THROW);
                delay.setValue();
                timer.reset();
            }
            List<Integer> list2 = InventoryUtil.INSTANCE.findItemSlot( Items.ENDER_PEARL, true);
            if (!list2.isEmpty() && list2.size() > minPearls.getObject()) {
                InventoryUtil.INSTANCE.performSlotAction(list2.get(0) < 9 ? list2.get(0) + 36 : list2.get(0), 1, SlotActionType.THROW);
                delay.setValue();
                timer.reset();
            }
        }
    }
}