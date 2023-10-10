package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MovingObjectPosition;
import tech.atani.client.listener.event.minecraft.input.ClickingEvent;
import tech.atani.client.listener.event.minecraft.player.rotation.RotationEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;

@Native
@ModuleData(name = "Extinguish", description = "Automatically extinguishes fire under you", category = Category.COMBAT)
public class Extinguish extends Module {

    public boolean changed;
    public int slotId;

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        if(Methods.mc.thePlayer.isBurning()) {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {

                ItemStack itemStack = Methods.mc.thePlayer.inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem().getIdFromItem(itemStack.getItem()) == 326) {
                    if(!changed)
                        slotId = Methods.mc.thePlayer.inventory.currentItem;
                    Methods.mc.thePlayer.inventory.currentItem = i;
                    changed = true;
                }
            }
        }
    }

    @Listen
    public void onRotate(RotationEvent rotationEvent) {
        if (Methods.mc.thePlayer.isBurning()) {
            if(Methods.mc.thePlayer.getHeldItem() == null) {
                return;
            }
            if (Methods.mc.thePlayer.getHeldItem().getItem().getIdFromItem(Methods.mc.thePlayer.getHeldItem().getItem()) == 326) {
                rotationEvent.setPitch(90);
            }
        }
    }

    @Listen
    public void onClicking(ClickingEvent clickingEvent) {
        if (Methods.mc.thePlayer.isBurning()) {
            if(Methods.mc.thePlayer.getHeldItem() == null) {
                return;
            }
            if (Methods.mc.thePlayer.getHeldItem() != null && Methods.mc.thePlayer.getHeldItem().getItem().getIdFromItem(Methods.mc.thePlayer.getHeldItem().getItem()) == 326 && !Methods.mc.thePlayer.isInWater() && !Methods.mc.thePlayer.isInLava()) {
                if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
                    return;

                MovingObjectPosition objectOver = Methods.mc.objectMouseOver;
                BlockPos blockpos = Methods.mc.objectMouseOver.getBlockPos();
                ItemStack itemstack = Methods.mc.thePlayer.inventory.getCurrentItem();

                if (objectOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || Methods.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
                    return;
                }


                Methods.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Methods.mc.thePlayer.inventory.getCurrentItem()));

                //Reset to current hand.
                Methods.mc.thePlayer.inventory.currentItem = slotId;
                Methods.mc.playerController.updateController();

                if (itemstack != null && itemstack.stackSize == 0) {
                    Methods.mc.thePlayer.inventory.mainInventory[Methods.mc.thePlayer.inventory.currentItem] = null;
                }

                Methods.mc.sendClickBlockToController(Methods.mc.currentScreen == null && Methods.mc.gameSettings.keyBindAttack.isKeyDown() && Methods.mc.inGameHasFocus);
            }
        }
        if(changed) {
            Methods.mc.thePlayer.inventory.currentItem = slotId;
            changed = false;
        }
    }

    @Override
    public void onEnable() {
        changed = false;
    }

    @Override
    public void onDisable() {

    }
}
