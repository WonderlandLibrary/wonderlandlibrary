package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.types.BooleanOption;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.Timer;
import net.minecraft.item.ItemPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
@Mod(displayName = "AutoPotion")
public class AutoPotion extends Module
{
    @Option.Op(min = 0.5, max = 10.0, increment = 0.5)
    public static double health;
    @Option.Op(min = 200.0, max = 1000.0, increment = 50.0)
    public static double delay;
    @Option.Op(min = 0.0, max = 9.0, increment = 1.0)
    private double slot;
    @Option.Op
    private boolean compatible;
    private int haltTicks;
    public static Timer timer;
    public static boolean potting;
    public static boolean potNextCompat;
    public static boolean swapTarget;
    private double x;
    private double y;
    private double z;
    
    static {
        AutoPotion.health = 4.5;
        AutoPotion.delay = 250.0;
        AutoPotion.timer = new Timer();
    }
    
    public AutoPotion() {
        this.slot = 6.0;
        this.compatible = true;
        this.haltTicks = -1;
    }
    
    @Override
    public void enable() {
        this.haltTicks = -1;
        super.enable();
    }
    
    @EventTarget(3)
    private void onUpdate(final UpdateEvent event) {
        switch (event.getState()) {
            case PRE: {
                boolean targets = false;
                final int nearbyEntities = 0;
                for (final Entity entity : ClientUtils.loadedEntityList()) {
                    }
                }
                boolean check = AutoPotion.timer.delay((float)AutoPotion.delay);
                if (getPotionFromInventory() != -1 && ClientUtils.player().getHealth() <= AutoPotion.health * 2.0 && check) {
                    if (ClientUtils.player().isCollidedVertically) {
                        AutoPotion.swapTarget = true;
                        AutoPotion.timer.reset();
                        ClientUtils.packet(new C03PacketPlayer.C06PacketPlayerPosLook(ClientUtils.x(), ClientUtils.y(), ClientUtils.z(), ClientUtils.yaw(), -90.0f, true));
                        this.swap(getPotionFromInventory(), (int)this.slot);
                        ClientUtils.packet(new C09PacketHeldItemChange((int)this.slot));
                        ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
                        ClientUtils.packet(new C09PacketHeldItemChange(ClientUtils.player().inventory.currentItem));
                        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 0.42, ClientUtils.z(), true));
                        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 0.75, ClientUtils.z(), true));
                        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.0, ClientUtils.z(), true));
                        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.16, ClientUtils.z(), true));
                        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.24, ClientUtils.z(), true));
                        this.x = ClientUtils.x();
                        this.y = ClientUtils.y() + 1.24;
                        this.z = ClientUtils.z();
                        this.haltTicks = 5;
                    }
                    else {
                        event.setAlwaysSend(true);
                        event.setPitch(90.0f);
                        AutoPotion.potting = true;
                        AutoPotion.swapTarget = true;
                        AutoPotion.timer.reset();
                    }
                }
                if (this.haltTicks >= 0) {
                    event.setCancelled(true);
                }
                if (this.haltTicks == 0) {
                    final EntityPlayerSP player = ClientUtils.player();
                    final EntityPlayerSP player2 = ClientUtils.player();
                    final double n = 0.0;
                    player2.motionZ = n;
                    player.motionX = n;
                    ClientUtils.player().setPositionAndUpdate(this.x, this.y, this.z);
                    ClientUtils.player().motionY = -0.08;
                }
                --this.haltTicks;
                break;
            }
        {
            {
                final int potSlot = getPotionFromInventory();
                if (AutoPotion.potting) {
                    this.swap(getPotionFromInventory(), (int)this.slot);
                    ClientUtils.packet(new C09PacketHeldItemChange((int)this.slot));
                    ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
                    ClientUtils.packet(new C09PacketHeldItemChange(ClientUtils.player().inventory.currentItem));
                    AutoPotion.timer.reset();
                    AutoPotion.potting = false;
                }
            }
        if (event.getState().equals(Event.State.PRE) && AutoPotion.potNextCompat) {
            AutoPotion.potNextCompat = false;
        }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, slot, hotbarNum, 2, ClientUtils.player());
    }
    
    public static int getPotionFromInventory() {
        int pot = -1;
        int counter = 0;
        for (int i = 1; i < 45; ++i) {
            if (ClientUtils.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = ClientUtils.player().inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (item instanceof ItemPotion) {
                    final ItemPotion potion = (ItemPotion)item;
                    if (potion.getEffects(is) != null) {
                        for (final Object o : potion.getEffects(is)) {
                            final PotionEffect effect = (PotionEffect)o;
                            if (effect.getPotionID() == Potion.heal.id && ItemPotion.isSplash(is.getItemDamage())) {
                                ++counter;
                                pot = i;
                            }
                        }
                    }
                }
            }
        }
        return pot;
    }
    
    @Override
    public void disable() {
        AutoPotion.potting = false;
        super.disable();
    }
}
