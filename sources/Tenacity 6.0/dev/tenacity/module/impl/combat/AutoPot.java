// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockGlass;
import dev.tenacity.utils.player.MovementUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class AutoPot extends Module
{
    private final NumberSetting delay;
    private final NumberSetting minHealHP;
    private final BooleanSetting splashFrogPots;
    private final TimerUtil timerUtil;
    public static boolean isPotting;
    private float prevPitch;
    
    public AutoPot() {
        super("AutoPot", "Auto Pot", Category.COMBAT, "auto splashes potions");
        this.delay = new NumberSetting("Delay", 750.0, 2000.0, 0.0, 50.0);
        this.minHealHP = new NumberSetting("Heal HP", 12.0, 20.0, 1.0, 0.5);
        this.splashFrogPots = new BooleanSetting("Frog potions", false);
        this.timerUtil = new TimerUtil();
        this.addSettings(this.delay, this.minHealHP, this.splashFrogPots);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (Tenacity.INSTANCE.isEnabled(Scaffold.class) || AutoPot.mc.currentScreen instanceof GuiChest) {
            return;
        }
        final int prevSlot = AutoPot.mc.thePlayer.inventory.currentItem;
        if (e.isPre()) {
            if (MovementUtils.isOnGround(1.0E-5) && !(AutoPot.mc.theWorld.getBlockState(new BlockPos(e.getX(), e.getY() - 1.0, e.getZ())).getBlock() instanceof BlockGlass) && (!AutoPot.mc.thePlayer.isPotionActive(Potion.moveSpeed) || AutoPot.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getDuration() < 30) && this.timerUtil.hasTimeElapsed(this.delay.getValue().longValue()) && !AutoPot.mc.thePlayer.isUsingItem()) {
                if (this.isSpeedPotsInHotbar()) {
                    for (int i = 36; i < 45; ++i) {
                        if (this.isSpeedPot(AutoPot.mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
                            AutoPot.isPotting = true;
                            this.prevPitch = AutoPot.mc.thePlayer.rotationPitch;
                            this.throwPot(prevSlot, i);
                            e.setPitch(-90.0f + ThreadLocalRandom.current().nextFloat());
                            break;
                        }
                    }
                    this.timerUtil.reset();
                    AutoPot.isPotting = false;
                }
                else {
                    this.moveSpeedPots();
                }
            }
            if (!AutoPot.mc.thePlayer.isPotionActive(Potion.regeneration) && AutoPot.mc.thePlayer.getHealth() <= this.minHealHP.getValue() && this.timerUtil.hasTimeElapsed(this.delay.getValue().longValue())) {
                if (this.isRegenPotsInHotbar()) {
                    for (int i = 36; i < 45; ++i) {
                        if (this.isRegenPot(AutoPot.mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
                            AutoPot.isPotting = true;
                            this.prevPitch = AutoPot.mc.thePlayer.rotationPitch;
                            this.throwPot(prevSlot, i);
                            e.setPitch(-90.0f + ThreadLocalRandom.current().nextFloat());
                            break;
                        }
                    }
                    this.timerUtil.reset();
                    AutoPot.isPotting = false;
                }
                else {
                    this.moveRegenPots();
                }
            }
            if (AutoPot.mc.thePlayer.getHealth() <= this.minHealHP.getValue() && this.timerUtil.hasTimeElapsed(this.delay.getValue().longValue())) {
                if (this.isHealthPotsInHotbar()) {
                    for (int i = 36; i < 45; ++i) {
                        if (this.isHealthPot(AutoPot.mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
                            AutoPot.isPotting = true;
                            this.prevPitch = AutoPot.mc.thePlayer.rotationPitch;
                            this.throwPot(prevSlot, i);
                            e.setPitch(-90.0f + ThreadLocalRandom.current().nextFloat());
                            break;
                        }
                    }
                    this.timerUtil.reset();
                    AutoPot.isPotting = false;
                }
                else {
                    this.moveHealthPots();
                }
            }
        }
        else if (e.isPost()) {
            AutoPot.isPotting = false;
        }
    }
    
    private void throwPot(final int prevSlot, final int index) {
        final double x = AutoPot.mc.thePlayer.posX;
        final double y = AutoPot.mc.thePlayer.posY;
        final double z = AutoPot.mc.thePlayer.posZ;
        final float yaw = AutoPot.mc.thePlayer.rotationYaw;
        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, 88.8f + ThreadLocalRandom.current().nextFloat(), AutoPot.mc.thePlayer.onGround));
        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(index - 36));
        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(BlockPos.NEGATIVE, 255, AutoPot.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
        PacketUtils.sendPacket(new C09PacketHeldItemChange(prevSlot));
        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw, this.prevPitch, AutoPot.mc.thePlayer.onGround));
    }
    
    private boolean isSpeedPotsInHotbar() {
        for (int index = 36; index < 45; ++index) {
            if (this.isSpeedPot(AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isHealthPotsInHotbar() {
        for (int index = 36; index < 45; ++index) {
            if (this.isHealthPot(AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isRegenPotsInHotbar() {
        for (int index = 36; index < 45; ++index) {
            if (this.isRegenPot(AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack())) {
                return true;
            }
        }
        return false;
    }
    
    private int getPotionCount() {
        int count = 0;
        for (int index = 0; index < 45; ++index) {
            final ItemStack stack = AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (this.isHealthPot(stack) || this.isHealthPot(stack) || this.isRegenPot(stack)) {
                ++count;
            }
        }
        return count;
    }
    
    private void moveSpeedPots() {
        if (AutoPot.mc.currentScreen instanceof GuiChest) {
            return;
        }
        for (int index = 9; index < 36; ++index) {
            final ItemStack stack = AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null) {
                if (this.splashFrogPots.isEnabled() || !stack.getDisplayName().contains("Frog")) {
                    if (this.isSpeedPot(stack)) {
                        AutoPot.mc.playerController.windowClick(0, index, 6, 2, AutoPot.mc.thePlayer);
                        break;
                    }
                }
            }
        }
    }
    
    private void moveHealthPots() {
        if (AutoPot.mc.currentScreen instanceof GuiChest) {
            return;
        }
        for (int index = 9; index < 36; ++index) {
            final ItemStack stack = AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (this.isHealthPot(stack)) {
                AutoPot.mc.playerController.windowClick(0, index, 6, 2, AutoPot.mc.thePlayer);
                break;
            }
        }
    }
    
    private void moveRegenPots() {
        if (AutoPot.mc.currentScreen instanceof GuiChest) {
            return;
        }
        for (int index = 9; index < 36; ++index) {
            final ItemStack stack = AutoPot.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (this.isRegenPot(stack)) {
                AutoPot.mc.playerController.windowClick(0, index, 6, 2, AutoPot.mc.thePlayer);
                break;
            }
        }
    }
    
    private boolean isSpeedPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            if (!this.splashFrogPots.isEnabled() && stack.getDisplayName().contains("Frog")) {
                return false;
            }
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final PotionEffect e : ((ItemPotion)stack.getItem()).getEffects(stack)) {
                    if (e.getPotionID() == Potion.moveSpeed.id && e.getPotionID() != Potion.jump.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isHealthPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getItemDamage())) {
            for (final PotionEffect e : ((ItemPotion)stack.getItem()).getEffects(stack)) {
                if (e.getPotionID() == Potion.heal.id) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isRegenPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getItemDamage())) {
            for (final PotionEffect e : ((ItemPotion)stack.getItem()).getEffects(stack)) {
                if (e.getPotionID() == Potion.regeneration.id) {
                    return true;
                }
            }
        }
        return false;
    }
}
