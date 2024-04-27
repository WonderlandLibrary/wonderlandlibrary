package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.MotionEvent;
import me.aquavit.liquidsense.event.events.SlowDownEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.Random;

@ModuleInfo(name = "NoSlowDown", description = "Cancels slowness effects caused by soulsand and using items.",
        category = ModuleCategory.MOVEMENT)
public class NoSlow extends Module {
    private final FloatValue blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla","Packet", "AAC", "OldAAC", "OldPacket"}, "Packet");
    public static final BoolValue soulsandValue = new BoolValue("Soulsand", true);

    Aura aura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);
    MSTimer timer = new MSTimer();

    @EventTarget
    public void onMotion(MotionEvent event) {
        boolean blockState = isBlocking();
        ItemStack heldItem = mc.thePlayer.getHeldItem();

        if (heldItem == null || !(heldItem.getItem() instanceof ItemSword) || (!modeValue.get().equalsIgnoreCase("AAC") && !MovementUtils.isMoving())){
            return;
        }

        if (!mc.thePlayer.isBlocking() && !aura.getBlockingStatus()) {
            return;
        }

        switch (modeValue.get()){
            case "Packet":{
                switch (event.getEventState()){
                    case PRE:{
                        C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-.8,-.8,-.8), EnumFacing.DOWN);
                        mc.getNetHandler().addToSendQueue(digging);
                        break;
                    }
                    case POST:{
                        C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(new BlockPos(-.8,-.8,-.8), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f);
                        mc.getNetHandler().addToSendQueue(blockPlace);
                    }
                }
                break;
            }
            case "AAC":{
                if ((mc.thePlayer.ticksExisted % 2 == 0 || !(blockState)) && (aura.getTarget() != null || mc.thePlayer.isBlocking())){
                    switch (event.getEventState()){
                        case PRE:{
                            C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN);
                            mc.getNetHandler().addToSendQueue(digging);
                            break;
                        }
                        case POST:{
                            C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
                            mc.getNetHandler().addToSendQueue(blockPlace);
                        }
                    }
                }
                break;
            }
            case "OldAAC":{
                if (timer.hasTimePassed(80)){
                    switch (event.getEventState()){
                        case PRE:{
                            C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN);
                            mc.getNetHandler().addToSendQueue(digging);
                            break;
                        }
                        case POST:{
                            C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
                            mc.getNetHandler().addToSendQueue(blockPlace);
                        }
                    }
                }
                break;
            }
            case "OldPacket":{
                if (aura.getTarget() == null){
                    switch (event.getEventState()){
                        case PRE:{
                            C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
                            mc.getNetHandler().addToSendQueue(digging);
                            break;
                        }
                        case POST:{
                            C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
                            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(getBlockpos(), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
                        }
                    }
                }
                break;
            }

        }
    }

    @EventTarget
    public void onSlowDown(SlowDownEvent event) {
        Item heldItem = mc.thePlayer.getHeldItem().getItem();

        event.setForward(getMultiplier(heldItem, true));
        event.setStrafe(getMultiplier(heldItem, false));
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

    private float getMultiplier(Item item, boolean isForward) {
        return item instanceof ItemFood || item instanceof ItemPotion || item instanceof ItemBucketMilk ?
                (isForward ? consumeForwardMultiplier.get() : consumeStrafeMultiplier.get())
                : (item instanceof ItemSword ? (isForward ? blockForwardMultiplier.get() : blockStrafeMultiplier.get())
                : (item instanceof ItemBow ? (isForward ? bowForwardMultiplier.get() : bowStrafeMultiplier.get()) : 0.2f));
    }

    private BlockPos getBlockpos() {
        Random random = new Random();
        double dx = MathHelper.floor_double(random.nextDouble() / 1000 + 2820);
        double jy = MathHelper.floor_double(random.nextDouble() / 100 * 0.20000000298023224);
        double kz = MathHelper.floor_double(random.nextDouble() / 1000 + 2820);
        return new BlockPos(dx, -jy % 255, kz);
    }

    private boolean isBlocking() {
        return mc.thePlayer.isBlocking() || aura.getBlockingStatus();
    }
}
