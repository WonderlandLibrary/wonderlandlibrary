package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.module.impl.combat.Velocity;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class LongJumpZoneCraftImpl implements ModeImpl<LongJump> {
    private double speed;
    private int tick, stage, ticks, slot, prevSlot;

    private boolean hasBow() {
        final boolean base = (slot = getSlot()) != -1;
        if (mc.thePlayer.inventory.getStackInSlot(slot).getItem() == Items.bow && mc.playerController.gameIsSurvivalOrAdventure()) {
            for (int i = 0; i < 45; i++) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null) continue;
                if (stack.getItem() == Items.arrow) return base;
            }
            getParent().setEnabled(false);
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Long Jump", "No arrows found in hotbar!", 4000, Type.WARNING));
            return false;
        }
        return base;
    }

    private int getSlot() {
        if (slot != -1) return slot;
        for (int i = 0; i < 9; i++) {
            final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) continue;
            if (stack.getItem() != Items.bow && stack.getItem() != Items.fishing_rod) continue;
            return i;
        }
        getParent().setEnabled(false);
        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Long Jump", "No bow found in hotbar!", 4000, Type.WARNING));
        return -1;
    }

    @Override
    public LongJump getParent() {
        return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
    }

    @Override
    public String getName() {
        return "ZoneCraft";
    }

    private final float[] LONG_JUMP_Y_MOTIONS = {
            0.44f, 0.44f, 0.43f, 0.41f, 0.4f, 0.38f, 0.36f, 0.34f,
            0.32f, 0.28f, 0.18f, 0.13f, 0.09f,
            -0.03f, -0.07f, -0.11f, -0.15f, -0.23f
    };

    @Override
    public void onEnable() {
        speed = 0;
        tick = ticks = stage = 0;
        slot = -1;
        prevSlot = -1;
        Client.INSTANCE.getModuleManager().getModule("Velocity").setEnabled(false);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        Client.INSTANCE.getModuleManager().getModule("Velocity").setEnabled(true);
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (!e.isPre()) return;
            //random to bypass balance thingy in watchdog2
            if (stage == 0) e.pitch = MathUtil.getRandom_float(-90, -89.9f);
            if (prevSlot != -1) mc.thePlayer.inventory.currentItem = prevSlot;
            mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
            mc.thePlayer.lastTickPosY = mc.thePlayer.posY;
        }
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            switch (stage) {
                case 0:
                    if (!hasBow() && ticks == 0) return;
                    final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
                    if (ticks == 0) {
                        prevSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(slot));
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C08PacketPlayerBlockPlacement(stack));
                    }
                    if (ticks == (stack.getItem() == Items.bow ? 3 : 1)) {
                        prevSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        if (stack.getItem() == Items.bow) mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    }
                    if (mc.thePlayer.hurtTime > 0) {
                        if (stack.getItem() != Items.bow) mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        mc.thePlayer.inventory.currentItem = prevSlot;
                        prevSlot = -1;
                        stage++;
                    }
                    ticks++;
                    e.setX(0);
                    e.setZ(0);
                    break;
                case 1:
                    mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() * 4);
                    e.setY(mc.thePlayer.motionY = 1.3f);
                    mc.timer.timerSpeed = 0.15F;
                    stage++;
                    break;
                case 2:
                    mc.timer.timerSpeed = 1.5F;
                    /*if (tick < LONG_JUMP_Y_MOTIONS.length)
                        e.setY(mc.thePlayer.motionY = LONG_JUMP_Y_MOTIONS[tick++]);*/
                    e.setY(mc.thePlayer.motionY += MathUtil.getRandom_double(2.0E-8D, 2.0E-4D));
                    if (mc.thePlayer.onGround) getParent().setEnabled(false);
                    break;
            }
        }
    }
}