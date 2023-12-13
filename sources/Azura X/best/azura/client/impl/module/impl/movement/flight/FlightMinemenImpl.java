package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.combat.KillAura;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.world.WorldSettings;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

public class FlightMinemenImpl implements ModeImpl<Flight> {

    private int ticks, flyState1;
    private boolean boosted = false;
    private boolean tp;
    private boolean hasBow;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Minemen";
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            if (((EventMotion) event).isPre()) {
                if (!boosted && ticks == 0) {
                    int bowSlot = -1;
                    for (int i = 0; i <= 8; i++) {
                        ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
                        if (s == null) continue;
                        if (s.getItem() instanceof ItemBow) bowSlot = i;
                    }
                    boolean canShoot = false;
                    for (int i = 0; i < 45; i++)
                        if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            Item item = stack.getItem();
                            if (item == Items.arrow) canShoot = true;
                        }
                    if (bowSlot == -1 || !canShoot) return;
                    ((EventMotion) event).pitch = -90;
                    if (flyState1 == 0) {
                        mc.thePlayer.inventory.currentItem = bowSlot;
                        mc.gameSettings.keyBindUse.pressed = true;
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(bowSlot)));
                    }
                    if (flyState1 > 2) {
                        mc.gameSettings.keyBindUse.pressed = false;
                        boosted = true;
                    }
                    flyState1++;
                }
            }
        }
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            if (ticks == 0) {
                MovementUtil.setSpeed(0, e);
                e.setY(0);
                mc.thePlayer.motionX = e.getX();
                mc.thePlayer.motionZ = e.getZ();
                mc.thePlayer.motionY = e.getY();
            }
        }
        if (event instanceof EventUpdate) {
            if (mc.thePlayer.isBurning()) {
                mc.timer.timerSpeed = 1.0f;
                getParent().setEnabled(false);
                return;
            }
            switch (ticks) {
                case 0:
                    ((KillAura)Client.INSTANCE.getModuleManager().getModule(KillAura.class)).targets.clear();
                    mc.timer.timerSpeed = 0.5f;
                    if (mc.thePlayer.hurtTime == 0) {
                        int bowSlot = -1;
                        for (int i = 0; i <= 8; i++) {
                            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
                            if (s == null) continue;
                            if (s.getItem() instanceof ItemBow) bowSlot = i;
                        }
                        boolean canShoot = false;
                        if (bowSlot != -1) {
                            if (!mc.playerController.getCurrentGameType().equals(WorldSettings.GameType.SURVIVAL))
                                canShoot = true;
                            for (int i = 0; i < 45; i++) {
                                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                                    ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                                    Item item = stack.getItem();
                                    if (item == Items.arrow) canShoot = true;
                                }
                            }
                        }
                        hasBow = (bowSlot != -1 && canShoot);
                        return;
                    }
                    if (hasBow && tp)
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    ticks++;
                    break;
                case 1:
                    mc.thePlayer.jump();
                    mc.thePlayer.setSpeed(9.0);
                    ticks++;
                    break;
                case 2:
                    if (mc.thePlayer.hurtTime == 0 && mc.thePlayer.motionY < -0.4) {
                        ticks = flyState1 = 0;
                        boosted = tp = false;
                    } else {
                        mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
        ticks = flyState1 = 0;
        boosted = tp = hasBow = false;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.setSpeed(0);
    }
}