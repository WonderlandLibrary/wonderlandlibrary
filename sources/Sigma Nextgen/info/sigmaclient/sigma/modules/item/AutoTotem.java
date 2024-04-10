//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import top.fl0wowp4rty.phantomshield.annotations.Native;
import net.minecraft.item.ItemStack;


public class AutoTotem extends Module {
    private final NumberValue delay = new NumberValue("Delay", 0, 0, 20, NumberValue.NUMBER_TYPE.INT);
    private final NumberValue health = new NumberValue("Health", 5, 0, 20, NumberValue.NUMBER_TYPE.INT);

    public AutoTotem() {
        super("AutoTotem", Category.Item, "Auto use totem");
     registerValue(delay);
     registerValue(health);
    }
    private int nextTickSlot;
    private int totems;
    private int timer;
    private boolean wasTotemInOffhand;
    public static boolean movingTotem = false;

    @Override
    public void onDisable() {
        movingTotem = false;
        super.onDisable();
    }

    @Override
    public void onEnable()
    {
        nextTickSlot = -1;
        totems = 0;
        timer = 0;
        wasTotemInOffhand = false;
    }

    @Override
    public void onUpdateEvent(UpdateEvent e)
    {
        if(e.isPost()) return;
        movingTotem = false;
        if(mc.player.getHealth() > health.getValue().floatValue()) return;
        movingTotem = true;
        finishMovingTotem();

        PlayerInventory inventory = mc.player.inventory;
        int nextTotemSlot = searchForTotems(inventory);

        ItemStack offhandStack = inventory.getStackInSlot(40);
        if(isTotem(offhandStack))
        {
            totems++;
            wasTotemInOffhand = true;
            return;
        }

        if(wasTotemInOffhand)
        {
            timer = delay.getValue().intValue();
            wasTotemInOffhand = false;
        }
        if(nextTotemSlot == -1)
            return;

        if(timer > 0)
        {
            timer--;
            return;
        }

        moveTotem(nextTotemSlot, offhandStack);
    }

    private void moveTotem(int nextTotemSlot, ItemStack offhandStack)
    {
        boolean offhandEmpty = offhandStack == ItemStack.EMPTY;
        mc.playerController.windowClickFixed(
                mc.player.container.windowId,
                nextTotemSlot,0, ClickType.PICKUP,
                mc.player,
                false);
        mc.playerController.windowClickFixed(
                mc.player.container.windowId,
                45,0, ClickType.PICKUP,
                mc.player,
                false);
        mc.playerController.windowClickFixed(
                mc.player.container.windowId,
                nextTotemSlot,0, ClickType.PICKUP,
                mc.player,
                false);
        if(!offhandEmpty)
            nextTickSlot = nextTotemSlot;
    }

    private void finishMovingTotem()
    {
        if(nextTickSlot == -1)
            return;
        nextTickSlot = -1;
    }

    private int searchForTotems(PlayerInventory inventory)
    {
        totems = 0;
        int nextTotemSlot = -1;

        for(int slot = 0; slot <= 36; slot++)
        {
            if(!isTotem(inventory.getStackInSlot(slot)))
                continue;

            totems++;

            if(nextTotemSlot == -1)
                nextTotemSlot = slot < 9 ? slot + 36 : slot;
        }

        return nextTotemSlot;
    }

    private boolean isTotem(ItemStack stack)
    {
        return stack.getItem().getTranslationKey().contains("totem");
    }
}
