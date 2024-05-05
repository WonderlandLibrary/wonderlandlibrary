/*
 * Decompiled with CFR 0.150.
 */
package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.ObsidianPlaceEvent;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.GCDFix;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.RotationHelper;
import ru.smertnix.celestial.utils.math.TimerHelper;

public class CrystalAura extends Feature {

    public static TimerHelper timerHelper = new TimerHelper();
    private int counter;
    public CrystalAura() {
        super("AutoExplosion", "Автоматически ставит кристалл и взрывает его", FeatureCategory.Combat);
        addSettings();
    }
    @EventTarget
    public void onObs(ObsidianPlaceEvent e) {
        final int oldSlot = mc.player.inventory.currentItem;
        final BlockPos pos = e.getPos();
        int crystal = getSlotIDFromItem(Items.END_CRYSTAL);
        if (crystal >= 0) {
            counter++;
            mc.player.inventory.currentItem = getSlotWithCrystal();
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.inventory.currentItem = oldSlot;
        }
    }
    @EventTarget
    public void update (EventUpdate e) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (counter > 0) attackEntity(entity);
            if (!entity.isEntityAlive()) counter = 0;
        }
    }
    public void attackEntity(Entity base) {
        if (base instanceof EntityEnderCrystal) {
            if (base.isDead || mc.player.getDistance(base) > 6) {
                return;
            }
            if (timerHelper.hasReached(200)) {
                mc.playerController.attackEntity(mc.player, base);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                timerHelper.reset();
            }
        }
    }
    public int getSlotIDFromItem(Item item) {
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

    private int getSlotWithCrystal() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEndCrystal) {
                return i;
            }
        }
        return -1;
    }
}
