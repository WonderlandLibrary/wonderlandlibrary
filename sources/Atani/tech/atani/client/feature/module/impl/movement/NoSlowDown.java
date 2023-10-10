package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.game.RunTickEvent;
import tech.atani.client.listener.event.minecraft.player.movement.NoSlowEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;

@Native
@ModuleData(name = "NoSlowDown", description = "Removes the blocking & eating slowdown", category = Category.MOVEMENT)
public class NoSlowDown extends Module {

    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[] {"Vanilla", "Switch", "Grim", "Old Intave", "Old NCP", "Matrix", "Old Hypixel", "MineMenClub"});

    private final MultiStringBoxValue items = new MultiStringBoxValue("Items", "Should the module disable slowdown with these items?", this, new String[] {"Sword"}, new String[] {"Sword", "Food", "Bow"});
    private final SliderValue<Float> swordForward = new SliderValue<Float>("Sword Forward", "How high should the sword forward multiplier be?", this, 1f, 0f, 1f, 2, new Supplier[]{() -> items.get("Sword")}),
            swordStrafe = new SliderValue<Float>("Sword Strafe", "How high should the sword strafe multiplier be?", this, 1f, 0f, 1f, 2, new Supplier[]{() -> items.get("Sword")}),
            foodForward = new SliderValue<Float>("Food Forward", "How high should the food forward multiplier be?", this, 1f, 0f, 1f, 2, new Supplier[]{() -> items.get("Food")}),
            foodStrafe = new SliderValue<Float>("Food Strafe", "How high should the food strafe multiplier be?", this, 1f, 0f, 1f, 2, new Supplier[]{() -> items.get("Food")}),
            bowForward = new SliderValue<Float>("Bow Forward", "How high should the bow forward multiplier be?", this, 1f, 0f, 1f, 2, new Supplier[]{() -> items.get("Bow")}),
            bowStrafe = new SliderValue<Float>("Bow Strafe", "How high should the bow strafe multiplier be?", this, 1f, 0f, 1f, 2, new Supplier[]{() -> items.get("Bow")});

    // Old Intave
    private final TimeHelper intaveTimer = new TimeHelper();

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    @Listen
    public void onNoSlowEvent(NoSlowEvent event) {
        ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !mc.thePlayer.isUsingItem() || !isMoving()) {
            return;
        }

        if (items.get("Sword") && currentItem.getItem() instanceof ItemSword) {
            event.setSprint(!mode.is("MineMenClub"));
            event.setForward(swordForward.getValue());
            event.setStrafe(swordStrafe.getValue());
        }

        if (items.get("Food") && currentItem.getItem() instanceof ItemFood) {
            event.setSprint(!mode.is("MineMenClub"));
            event.setForward(foodForward.getValue());
            event.setStrafe(foodStrafe.getValue());
        }

        if (items.get("Bow") && currentItem.getItem() instanceof ItemBow) {
            event.setSprint(!mode.is("MineMenClub"));
            event.setForward(bowForward.getValue());
            event.setStrafe(bowStrafe.getValue());
        }
    }

    @Listen
    public void onMotionEvent(UpdateMotionEvent event) {
        ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();
        if (currentItem == null || !mc.thePlayer.isUsingItem() || !isMoving()) {
            return;
        }

        switch (mode.getValue()) {
            case "Old Hypixel":
                if (event.getType() == UpdateMotionEvent.Type.POST) {
                    if(mc.thePlayer.isUsingItem() && currentItem.getItem() instanceof ItemSword) {
                        sendPacket(new C08PacketPlayerBlockPlacement(currentItem));
                    }
                }
                break;
            case "Matrix":
                if (event.getType() == UpdateMotionEvent.Type.MID) {
                    if(mc.thePlayer.isUsingItem() && currentItem.getItem() instanceof ItemSword) {
                        sendPacketUnlogged(new C0BPacketEntityAction());
                        mc.thePlayer.onGround = false;
                    }
                }
                break;
            case "Old NCP":
                if(mc.thePlayer.isUsingItem() && currentItem.getItem() instanceof ItemSword) {
                    if (event.getType() == UpdateMotionEvent.Type.MID) {
                        sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }

                    if (event.getType() == UpdateMotionEvent.Type.POST) {
                        sendPacket(new C08PacketPlayerBlockPlacement(currentItem));
                    }
                }
                break;
            case "Old Intave":
                if(mc.thePlayer.isUsingItem() && currentItem.getItem() instanceof ItemSword && intaveTimer.hasReached(150L)) {
                    if(event.getType() == UpdateMotionEvent.Type.MID) {
                        sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }

                    if(event.getType() == UpdateMotionEvent.Type.POST) {
                        sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        intaveTimer.reset();
                    }
                }
                break;
            case "Grim":
            case "Switch":
                if(event.getType() == UpdateMotionEvent.Type.MID) {
                    sendPacket(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
                    sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
                break;
        }
    }

    @Listen
    public void onTickEvent(RunTickEvent event) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        switch (mode.getValue()) {
            case "Grim":
                if(mc.thePlayer.isBlocking()) {
                    sendPacket(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                } else if (mc.thePlayer.isUsingItem()) {
                    sendPacket(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
                    sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
                break;
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
