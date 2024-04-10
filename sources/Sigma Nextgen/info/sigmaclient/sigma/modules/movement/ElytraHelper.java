package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;

import static net.minecraft.network.play.client.CEntityActionPacket.Action.START_FALL_FLYING;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ElytraHelper extends Module {
    public BooleanValue jumped = new BooleanValue("Jump", true);
    public BooleanValue stop = new BooleanValue("Stop", false);
    public BooleanValue aw = new BooleanValue("Always", false);
    public ElytraHelper() {
        super("ElytraHelper", Category.Movement, "Better elytra helper");
     registerValue(jumped);
     registerValue(stop);
     registerValue(aw);
    }
    boolean jump = false, reset = false;
    @Override
    public void onEnable() {
        jump = false;
        super.onEnable();
    }
    public void reset(){
        if(reset){
            mc.gameSettings.keyBindJump.pressed = false;
            reset = false;
        }
    }
    @Override
    public void onDisable() {
        reset();
        if(stop.isEnable())
            mc.player.setFlag(7, false);
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.player.onGround && !jump && mc.player.fallDistance == 0 && jumped.isEnable()){
                mc.player.jump();
                jump = true;
            }
            if(mc.player.fallDistance > 0.08 || aw.isEnable()){
                ItemStack itemstack = mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                if(itemstack.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemstack)) {
                    mc.gameSettings.keyBindJump.pressed = mc.player.ticksExisted % 2 == 0;
                    reset = true;
                }else{
                    reset();
                }
            }else{
                reset();
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        super.onMoveEvent(event);
    }
}
