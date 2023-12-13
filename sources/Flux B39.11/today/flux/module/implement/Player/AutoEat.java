package today.flux.module.implement.Player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;

public class AutoEat extends Module {
    private int oldSlot;
    private int bestSlot;

    public static FloatValue FoodLevel = new FloatValue("AutoEat", "Food Level", 10f, 1f, 20f, 1f);
    public static BooleanValue Gapple = new BooleanValue("AutoEat", "Eat Gapple", false);
    public static BooleanValue RottenFlesh = new BooleanValue("AutoEat", "Eat RottenFlesh", false);

    public AutoEat() {
        super("AutoEat", Category.Player, false);
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
        if (event.isModified() && oldSlot == -1)
            return;

        if (this.oldSlot == -1) {
            if (mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.getFoodStats().getFoodLevel() >= FoodLevel.getValue()) {
                return;
            }

            float item = 0.0F;
            this.bestSlot = -1;

            for (int i = 0; i < 9; ++i) {
                ItemStack item1 = mc.thePlayer.inventory.getStackInSlot(i);
                if (item1 != null) {
                    float saturation = 0.0F;
                    if (item1.getItem() instanceof ItemFood && (Gapple.getValue() || item1.getItem() != Items.golden_apple) && (RottenFlesh.getValue() || item1.getItem() != Items.rotten_flesh)) {
                        saturation = ((ItemFood) item1.getItem()).getSaturationModifier(item1);
                }

                    if (saturation > item) {
                        item = saturation;
                        this.bestSlot = i;
                    }
                }
            }

            if (this.bestSlot == -1) {
                return;
            }

            this.oldSlot = mc.thePlayer.inventory.currentItem;
        } else {
            if (mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.getFoodStats().getFoodLevel() >= 20) {
                this.stop();
                return;
            }

            ItemStack var6 = mc.thePlayer.inventory.getStackInSlot(this.bestSlot);
            if (var6 == null || !(var6.getItem() instanceof ItemFood)) {
                this.stop();
                return;
            }

            mc.thePlayer.inventory.currentItem = this.bestSlot;
            mc.gameSettings.keyBindUseItem.setPressed(true);
        }

    }

    private void stop() {
    	mc.gameSettings.keyBindUseItem.setPressed(false);
    	mc.thePlayer.inventory.currentItem = this.oldSlot;
        this.oldSlot = -1;
    }
}
