package today.flux.module.implement.Ghost;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.TimeHelper;

import java.util.Iterator;
import java.util.Map;

public class Refill extends Module {

    TimeHelper time = new TimeHelper();
    Item value;

    private Boolean switchBack = false;
    private Boolean useRod = false;

    public static FloatValue delay = new FloatValue("Refill", "Delay", 100f, 50f, 1000f, 50f);
    public static BooleanValue Soup = new BooleanValue("Refill", "Soup", false);
    public static BooleanValue Pot = new BooleanValue("Refill", "Pot", false);
    public static BooleanValue onInv = new BooleanValue("Refill", "On Inv", false);


    public Refill() {
        super("Refill", Category.Ghost, false);
    }


    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
        if (Soup.getValue()) {
            this.value = Items.mushroom_stew;
        } else if (Pot.getValue()) {
            ItemPotion itempotion = Items.potionitem;
            this.value = ItemPotion.getItemById(373);
        }

        this.refill();
    }

    private void refill() {
        if (!onInv.getValue() || mc.currentScreen instanceof GuiInventory) {
            if (!isHotbarFull() && this.time.isDelayComplete(delay.getValue())) {
                refill(this.value);
                this.time.reset();
            }
        }
    }


    public static boolean isHotbarFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemstack == null) {
                return false;
            }
        }

        return true;
    }


    public static void refill(Item value) {
        for (int i = 9; i < 37; ++i) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemstack != null && itemstack.getItem() == value) {
                mc.playerController.windowClick(0, i, 0, 1, mc.thePlayer);
                break;
            }
        }
    }


}
