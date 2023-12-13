package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.TimerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.Minecraft;

@Module.Mod(displayName = "AutoSword")
public class AutoSword extends Module
{
    @Op(name = "Slot", min = 0.0, max = 8.0, increment = 1.0)
    public double slot;
    @Op(name = "Delay", min = 1.0, max = 2000.0, increment = 1.0)
    public double delay;
    private TimerUtil timer;
    public static Minecraft mc;
    
    static {
        AutoSword.mc = Minecraft.getMinecraft();
    }
    
    public AutoSword() {
        super();
        this.timer = new TimerUtil();
        this.slot = 0.0;
        this.delay = 100.0;
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE || !this.timer.reach((long)this.delay) || (AutoSword.mc.currentScreen != null && !(AutoSword.mc.currentScreen instanceof GuiInventory))) {
            return;
        }
        int best = -1;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            if (AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    final float swordD = this.getItemDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = i;
                    }
                }
            }
        }
        final ItemStack current = AutoSword.mc.thePlayer.inventoryContainer.getSlot((int)(36.0 + this.slot)).getStack();
        if (best != -1 && (current == null || !(current.getItem() instanceof ItemSword) || swordDamage > this.getItemDamage(current))) {
            AutoSword.mc.playerController.windowClick(AutoSword.mc.thePlayer.inventoryContainer.windowId, best, (int)this.slot, 2, AutoSword.mc.thePlayer);
            this.timer.reset();
        }
    }
    
    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword)itemStack.getItem()).func_150931_i();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
}
