// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.ObsidianPlaceEvent;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Auto Explosion", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd, \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class AutoExplosion extends Module
{
    public static TimerUtility timerHelper;
    private int counter;
    
    @EventTarget
    public void onObs(final ObsidianPlaceEvent e) {
        final Minecraft mc = AutoExplosion.mc;
        final int oldSlot = Minecraft.player.inventory.currentItem;
        final BlockPos pos = e.getPos();
        final int crystal = this.getSlotIDFromItem(Items.END_CRYSTAL);
        if (crystal >= 0) {
            ++this.counter;
            final Minecraft mc2 = AutoExplosion.mc;
            Minecraft.player.inventory.currentItem = this.getSlotWithCrystal();
            final PlayerControllerMP playerController = AutoExplosion.mc.playerController;
            final Minecraft mc3 = AutoExplosion.mc;
            playerController.processRightClickBlock(Minecraft.player, AutoExplosion.mc.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
            final Minecraft mc4 = AutoExplosion.mc;
            Minecraft.player.swingArm(EnumHand.MAIN_HAND);
            final Minecraft mc5 = AutoExplosion.mc;
            Minecraft.player.inventory.currentItem = oldSlot;
        }
    }
    
    @EventTarget
    public void update(final EventUpdate e) {
        for (final Entity entity : AutoExplosion.mc.world.loadedEntityList) {
            if (this.counter > 0) {
                this.attackEntity(entity);
            }
            if (!entity.isEntityAlive()) {
                this.counter = 0;
            }
        }
    }
    
    public void attackEntity(final Entity base) {
        if (base instanceof EntityEnderCrystal) {
            if (!base.isDead) {
                final Minecraft mc = AutoExplosion.mc;
                if (Minecraft.player.getDistance(base) <= 6.0f) {
                    if (AutoExplosion.timerHelper.hasTimeElapsed(200L)) {
                        final PlayerControllerMP playerController = AutoExplosion.mc.playerController;
                        final Minecraft mc2 = AutoExplosion.mc;
                        playerController.attackEntity(Minecraft.player, base);
                        final Minecraft mc3 = AutoExplosion.mc;
                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                        AutoExplosion.timerHelper.reset();
                    }
                }
            }
        }
    }
    
    public int getSlotIDFromItem(final Item item) {
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            final Minecraft mc = AutoExplosion.mc;
            final ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }
    
    private int getSlotWithCrystal() {
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc = AutoExplosion.mc;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemEndCrystal) {
                return i;
            }
        }
        return -1;
    }
    
    static {
        AutoExplosion.timerHelper = new TimerUtility();
    }
}
