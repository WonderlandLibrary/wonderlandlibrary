package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPot extends Module {

    private static final String DELAY = "DELAY";
    private static final String HEALTH = "HEALTH";
    private final String PREDICT = "PREDICT";
    private String REGEN = "REGEN";
    private String OVERPOT = "OVERPOT";
    private Timer timer = new Timer();
    public static int haltTicks;
    public static boolean potting;
    private boolean send;

    public AutoPot(ModuleData data) {
        super(data);
        haltTicks = -1;
        settings.put(HEALTH, new Setting<>(HEALTH, 5, "Maximum health before healing.", 0.5, 0.5, 10));
        settings.put(DELAY, new Setting<>(DELAY, 350, "Delay before healing again.", 50, 100, 1000));
        settings.put(PREDICT, new Setting<>(PREDICT, false, "Predicts where to pot when moving."));
        settings.put(OVERPOT, new Setting<>(OVERPOT, true, "Pots earlier when armor is broken."));
        settings.put(REGEN, new Setting<>(REGEN, false, "Uses Regeneration pots."));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate e = (EventUpdate) event;
            float delay = ((Number) settings.get(DELAY).getValue()).floatValue();
            if (e.isPre()) {
                if (potting && haltTicks < 0) {
                    potting = false;
                }
                float health = ((Number) settings.get(HEALTH).getValue()).floatValue() * 2;
                if(mc.thePlayer.getEquipmentInSlot(4) == null && hasArmor(mc.thePlayer) && ((Boolean)settings.get(OVERPOT).getValue())) {
                    health += mc.thePlayer.getEquipmentInSlot(1) == null ? 6 : 3;
                }
                if (PlayerUtil.isMoving()) {
                    if (mc.thePlayer.getHealth() <= health && getPotionFromInv() != -1 && timer.delay(delay)) {
                        haltTicks = 6;
                        swap(getPotionFromInv(), 6);
                        e.setPitch(120);
                        if ((Boolean)settings.get(PREDICT).getValue())
                        {
                            double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 16.0D;
                            double movedPosY = mc.thePlayer.boundingBox.minY - 3.6D;
                            double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 16.0D;
                            float[] predRot = RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY);
                            e.setYaw(predRot[0]);
                            e.setPitch(predRot[1]);
                        }
                        potting = true;
                        timer.reset();
                    }
                } else if (mc.thePlayer.getHealth() <= health && getPotionFromInv() != -1 && timer.delay(delay) && haltTicks < 0 && mc.thePlayer.isCollidedVertically) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,mc.thePlayer.rotationYaw, -90, true));
                    swap(getPotionFromInv(), 6);
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    haltTicks = 5;
                    mc.thePlayer.jump();
                    potting = true;
                }
                haltTicks--;
            }
            if (e.isPost()) {
                if (potting) {
                    if(PlayerUtil.isMoving()) {
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        ChatUtil.printChat("OK");
                    }
                    timer.reset();
                    potting = false;

                }
            }
        }
/*        if (event instanceof EventPacket && (Boolean) settings.get(PSILENT).getValue()) {
            EventPacket ep = (EventPacket) event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer && !potting) {
                if (send) {
                    send = false;
                    return;
                }
            }
            if (potting && ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook && !send) {
                C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook) ep.getPacket();
                ep.setCancelled(true);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 120, true));
                send = true;
            }
        }*/
    }

    private boolean hasArmor(EntityPlayer player) {
        ItemStack boots = player.inventory.armorInventory[0];
        ItemStack pants = player.inventory.armorInventory[1];
        ItemStack chest = player.inventory.armorInventory[2];
        ItemStack head = player.inventory.armorInventory[3];
        return (boots != null) || (pants != null) || (chest != null) || (head != null);
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private int getPotionFromInv() {
        int pot = -1;
        for (int i = 0; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if ((item instanceof ItemPotion)) {
                    ItemPotion potion = (ItemPotion) item;
                    if (potion.getEffects(is) != null) {
                        for (Object o : potion.getEffects(is)) {
                            PotionEffect effect = (PotionEffect) o;
                            if ((effect.getPotionID() == Potion.heal.id || (effect.getPotionID() == Potion.regeneration.id && (Boolean)settings.get(REGEN).getValue() && !mc.thePlayer.isPotionActive(Potion.regeneration))) && (ItemPotion.isSplash(is.getItemDamage()))) {
                                pot = i;
                            }
                        }
                    }
                }
            }
        }
        return pot;
    }

}