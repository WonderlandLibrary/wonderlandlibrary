/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Timer;
import cow.milkgod.cheese.utils.Wrapper;
import java.io.PrintStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class AutoApple
extends Module {
    private final Property<Long> delay;
    private final Property<Float> health;
    private final Timer time;

    public AutoApple() {
        super("AutoApple", 0, Category.COMBAT, 3394662, true, "AutoApple", new String[]{"aap", "apple"});
        this.delay = new Property<Long>(this, "autoapple_delay", (long) 500);
        this.health = new Property<Float>(this, "autoapple_health", Float.valueOf(16.0f));
        this.time = new Timer();
    }

    private boolean canEat() {
        if (Wrapper.mc.thePlayer.onGround && Wrapper.mc.thePlayer.getHealth() <= ((Float)this.health.value).floatValue()) {
            return true;
        }
        return false;
    }

    private int findItem(int id2) {
        int index = 36;
        while (index < 45) {
            ItemStack item = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == id2) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Wrapper.mc.theWorld != null) {
            Logger.logChat("\u00a7cWARNING:\u00a7f This mod is patched by newer NoCheat versions.");
        }
    }

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (this.updateCounter() != 0 && this.canEat()) {
            int selectedSlot = -1;
            int original = Wrapper.mc.thePlayer.inventory.currentItem;
            if (this.findItem(322) != -1) {
                selectedSlot = this.findItem(322) - 36;
            }
            if (this.time.delay(((Long)this.delay.value).longValue()) && selectedSlot != -1) {
                System.out.println("applereached");
                Wrapper.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(selectedSlot));
                Wrapper.mc.playerController.updateController();
                Wrapper.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, Wrapper.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                Wrapper.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(selectedSlot));
                int x2 = 0;
                while (x2 < 32) {
                    Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(Wrapper.mc.thePlayer.onGround));
                    ++x2;
                }
                Wrapper.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                Wrapper.mc.thePlayer.stopUsingItem();
                Wrapper.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(original));
                this.time.reset();
            }
        }
    }

    private int updateCounter() {
        int counter = 0;
        int index = 36;
        while (index < 45) {
            ItemStack item = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == 322) {
                counter += item.stackSize;
            }
            ++index;
        }
        return counter;
    }
}

