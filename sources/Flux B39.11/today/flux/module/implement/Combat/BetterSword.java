package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.utility.DelayTimer;
import today.flux.utility.InvUtils;
import today.flux.module.value.BooleanValue;

/**
 * Created by John on 2017/05/10.
 */
public class BetterSword extends Module {
    private DelayTimer timer = new DelayTimer();
    public static BooleanValue AutoSet = new BooleanValue("BetterSword",  "Auto Set", false);

    public BetterSword(){
        super("BetterSword", Category.Combat, false);
    }

    @EventTarget
    public void onTick(TickEvent event){
        if (KillAura.target != null || !ModuleManager.killAuraMod.disableHelper.isDelayComplete(200))
            return;

        if (this.mc.currentScreen instanceof GuiContainer)
            return;

        if(!this.timer.hasPassed(200))
            return;

        if(AutoSet.getValue()){
            int slot = this.getBestSword(this.getScoreForSword(InvUtils.getItemBySlot(0)));

            if (slot == -1)
                return;

            this.swap(slot, 0);
        }
        else {
            if (!(InvUtils.getCurrentItem().getItem() instanceof ItemSword))
                return;

            int slot = this.getBestSword(this.getScoreForSword(InvUtils.getCurrentItem()));

            if (slot == -1)
                return;

            this.swap(slot, this.mc.thePlayer.inventory.currentItem);
        }

        this.timer.reset();
    }

    public int getBestSword(double minimum) {
        for (int i = 0; i < 36; ++i) {
            if (this.mc.thePlayer.inventory.currentItem == i)
                continue;

            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];

            if (itemStack == null)
                continue;

            if (!(itemStack.getItem() instanceof ItemSword))
                continue;

            if (minimum >= this.getScoreForSword(itemStack))
                continue;

            return i;
        }

        return -1;
    }

    public double getScoreForSword(final ItemStack itemStack){
        if(!(itemStack.getItem() instanceof ItemSword))
            return 0;

        ItemSword itemSword = (ItemSword) itemStack.getItem();

        double result = 1.0;

        result += itemSword.getDamageVsEntity();

        result += 1.25 * EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
        result += 0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);

        return result;
    }

    public void swap(int from, int to) {
        if(from <= 8){
            from = 36 + from;
        }

        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, from, to, 2, this.mc.thePlayer);
    }
}
