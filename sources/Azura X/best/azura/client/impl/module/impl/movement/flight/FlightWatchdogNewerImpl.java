package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

import java.util.Collections;
import java.util.List;

public class FlightWatchdogNewerImpl implements ModeImpl<Flight> {
    private double startPosY, distFlown, speed;
    private int stage;
    private boolean setback, fly;

    private final BooleanValue fast = new BooleanValue("Fast", "Fast halal zoom", false);

    @Override
    public List<Value<?>> getValues() {
        return Collections.singletonList(fast);
    }

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Watchdog New";
    }

    @Override
    public void onEnable() {
        distFlown = 0.0;
        startPosY = mc.thePlayer.posY;
        stage = 0;
        speed = 0.0;
        fly = false;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        mc.timer.silentTimer = false;
        setback = false;
    }

    @EventHandler
    private void onEvent(final Event event) {
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            MovementUtil.setSpeed(Math.max(MovementUtil.getBaseSpeed(), speed -= 0.01), e);
            if (!fly || !mc.thePlayer.isMoving()) MovementUtil.setSpeed(0, e);
        }
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                e.yaw = (float) Math.toDegrees(mc.thePlayer.getDirection());
                if (mc.thePlayer.fallDistance > 0)
                    fly = true;
                if (!fly) {
                    e.pitch = 90;
                    if (mc.thePlayer.onGround) mc.thePlayer.jump();
                    if (mc.thePlayer.posY > startPosY + 1) {
                        int slot = -1;
                        for (int i = 0; i < 9; i++) {
                            final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                            if (stack != null && stack.getItem() instanceof ItemBlock) {
                                if (((ItemBlock) stack.getItem()).getBlock() != Blocks.slime_block) continue;
                                slot = i;
                                break;
                            }
                        }
                        if (slot == -1) return;
                        if (mc.thePlayer.inventory.currentItem != slot)
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down().down(), EnumFacing.UP.getIndex(), null, 0, (float) Math.random() / 5f, 0));
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        if (mc.thePlayer.inventory.currentItem != slot)
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        if (ServerUtil.isHypixel())
                            speed = MovementUtil.getBaseSpeed() * 1.7;
                    }
                } else {
                    mc.thePlayer.motionY = 0;
                }
            }
            if (e.isPost()) {
                final double distX = mc.thePlayer.posX - mc.thePlayer.lastTickPosX, distZ = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
                distFlown += Math.sqrt(distX * distX + distZ * distZ);
            }
        }
        if (event instanceof EventRender2D) {
            final ScaledResolution sr = new ScaledResolution(mc);
            mc.fontRendererObj.drawStringWithShadow("Distance flown: " + (Math.round(distFlown * 100.0) / 100.0), sr.getScaledWidth() / 2.0 -
                    mc.fontRendererObj.getStringWidth("Distance flown: " + (Math.round(distFlown * 100.0) / 100.0)) / 2.0, sr.getScaledHeight() / 2.0 + 48, -1);
        }
    }
}