
package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoGapple extends Module {
    private final NumberValue health = new NumberValue("Health", 10, 0, 20, NumberValue.NUMBER_TYPE.INT);
    private final NumberValue delay = new NumberValue("Delay", 0, 0, 10, NumberValue.NUMBER_TYPE.FLOAT);
    private final BooleanValue offhand = new BooleanValue("Offhand", false);
    private final BooleanValue regen = new BooleanValue("Wait for regen", false);
    private final BooleanValue itemPicker = new BooleanValue("Illegal Item Picker", false);
    public AutoGapple() {
        super("AutoGapple", Category.Item, "Automatically eat golden apples (wait for totem)");
     registerValue(health);
//     registerValue(delay);
     registerValue(offhand);
     registerValue(regen);
     registerValue(itemPicker);
    }
    int oldSlot = -1, delayT = 0;
    boolean click = false;
    boolean press = false;
    @Override
    public void onEnable()
    {
        click = false;
    }
    public void no(){
        if(oldSlot != -1){
            mc.player.inventory.currentItem = oldSlot;
            oldSlot = -1;
        }
        if(press) {
            press = false;
            mc.gameSettings.keyBindUseItem.pressed = false;
        }
    }

    @Override
    public void onUpdateEvent(UpdateEvent e) {
        if(e.isPost()) return;
        if (mc.player.getHealth() > health.getValue().intValue()) {
            no();
            return;
        }
        if(offhand.isEnable()){
            if(AutoTotem.movingTotem) {
                no();
                return;
            }
        }
        if(itemPicker.isEnable()){
            if(searchForGapples(mc.player.inventory) == -1 && searchForGapplesFully(mc.player.inventory) != -1 && !isNeed(mc.player.getHeldItemOffhand())){
                mc.playerController.windowClickFixed(
                        mc.player.container.windowId,
                        searchForGapplesFully(mc.player.inventory),0, ClickType.PICKUP,
                        mc.player,
                        false);
                mc.playerController.windowClickFixed(
                        mc.player.container.windowId,
                        45,0, ClickType.PICKUP,
                        mc.player,
                        false);
                mc.playerController.windowClickFixed(
                        mc.player.container.windowId,
                        searchForGapplesFully(mc.player.inventory),0, ClickType.PICKUP,
                        mc.player,
                        false);
                no();
                return;
            }
        }
        if(regen.isEnable()){
            if(mc.player.isPotionActive(Effects.REGENERATION)) {
                no();
                return;
            }
        }
        if(!offhand.isEnable()) {
            PlayerInventory inventory = mc.player.inventory;
            int nextTotemSlot = searchForGapples(inventory);
            if (nextTotemSlot == -1) {
                no();
                return;
            }
            if (mc.player.inventory.currentItem != nextTotemSlot) {
                oldSlot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = nextTotemSlot;
            }
            press = true;
            mc.gameSettings.keyBindUseItem.pressed = true;
        }else{
            PlayerInventory inventory = mc.player.inventory;
            int nextGappleSlot = searchForGapples(inventory);
            if (searchForGapplesFully(inventory) == -1 && !isNeed(mc.player.getHeldItemOffhand())) {
                no();
                return;
            }
            if (delayT > 0) {
                delayT --;
            }
            boolean swap = mc.player.inventory.currentItem != nextGappleSlot;
            if (!isNeed(mc.player.getHeldItemOffhand()) && delayT == 0) {
                delayT = 5;
                oldSlot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = nextGappleSlot;
                if (swap)
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                if (!mc.player.isSpectator()) {
                    mc.getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                }
                mc.player.inventory.currentItem = oldSlot;
                if (swap)
                    mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                oldSlot = -1;
            }
            if(isNeed(mc.player.getHeldItemOffhand())) {
                press = true;
                mc.gameSettings.keyBindUseItem.pressed = true;
            }
        }
    }
    @Override
    public void onClickEvent(ClickEvent event) {
        if(click){
            click = false;
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
        }
    }

    private int searchForGapplesFully(PlayerInventory inventory)
    {
        for(int slot = 0; slot < 36; slot++)
        {
            if(!isNeed(inventory.getStackInSlot(slot)))
                continue;
            return slot;
        }
        return -1;
    }
    private int searchForGapples(PlayerInventory inventory)
    {
        for(int slot = 0; slot < 9; slot++)
        {
            if(!isNeed(inventory.getStackInSlot(slot)))
                continue;
            return slot;
        }
        return -1;
    }

    private boolean isNeed(ItemStack stack)
    {
        return stack.getItem().getFood() == Foods.GOLDEN_APPLE;
    }

}
