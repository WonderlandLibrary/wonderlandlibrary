// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.Xulu;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.EventSendPacket;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.util.LagCompensator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import com.elementars.eclient.friend.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Aura extends Module
{
    final Value<Boolean> players;
    final Value<Boolean> animals;
    final Value<Boolean> mobs;
    final Value<Double> range;
    final Value<Boolean> wait;
    final Value<Boolean> walls;
    final Value<Boolean> rotate;
    final Value<Boolean> sharpness;
    boolean isSpoofingAngles;
    double yaw;
    double pitch;
    
    public Aura() {
        super("Aura", "Hits people", 0, Category.COMBAT, true);
        this.players = this.register(new Value<Boolean>("Players", this, true));
        this.animals = this.register(new Value<Boolean>("Animals", this, false));
        this.mobs = this.register(new Value<Boolean>("Mobs", this, false));
        this.range = this.register(new Value<Double>("Range", this, 5.5, 1.0, 10.0));
        this.wait = this.register(new Value<Boolean>("Wait", this, true));
        this.walls = this.register(new Value<Boolean>("Walls", this, false));
        this.rotate = this.register(new Value<Boolean>("Rotate", this, true));
        this.sharpness = this.register(new Value<Boolean>("32k Switch", this, false));
        this.isSpoofingAngles = false;
    }
    
    @Override
    public void onUpdate() {
        if (Aura.mc.player.isDead) {
            return;
        }
        if (this.shouldPause()) {
            this.resetRotation();
            return;
        }
        final boolean shield = Aura.mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && Aura.mc.player.getActiveHand() == EnumHand.OFF_HAND;
        final boolean gap = Aura.mc.player.getHeldItemOffhand().getItem().equals(Items.GOLDEN_APPLE) && Aura.mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (Aura.mc.player.isHandActive() && !shield && !gap) {
            return;
        }
        if (this.wait.getValue()) {
            if (Aura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0f) {
                return;
            }
            if (Aura.mc.player.ticksExisted % 2 != 0) {
                return;
            }
        }
        for (final Entity target : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!EntityUtil.isLiving(target)) {
                continue;
            }
            if (target == Aura.mc.player) {
                continue;
            }
            if (Aura.mc.player.getDistance(target) > this.range.getValue()) {
                continue;
            }
            if (((EntityLivingBase)target).getHealth() <= 0.0f) {
                continue;
            }
            if (((EntityLivingBase)target).hurtTime != 0 && this.wait.getValue()) {
                continue;
            }
            if (!this.walls.getValue() && !Aura.mc.player.canEntityBeSeen(target) && !this.canEntityFeetBeSeen(target)) {
                continue;
            }
            if (this.players.getValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
                this.attack(target);
                return;
            }
            Label_0453: {
                if (EntityUtil.isPassive(target)) {
                    if (this.animals.getValue()) {
                        break Label_0453;
                    }
                    continue;
                }
                else {
                    if (EntityUtil.isMobAggressive(target) && this.mobs.getValue()) {
                        break Label_0453;
                    }
                    continue;
                }
                continue;
            }
            this.attack(target);
            return;
        }
        this.resetRotation();
    }
    
    private boolean checkSharpness(final ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        int i = 0;
        while (i < enchants.tagCount()) {
            final NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                final int lvl = enchant.getInteger("lvl");
                if (lvl >= 16) {
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private void attack(final Entity e) {
        if (this.sharpness.getValue() && !this.checkSharpness(Aura.mc.player.getHeldItemMainhand())) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Aura.mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY) {
                    if (this.checkSharpness(stack)) {
                        newSlot = i;
                        break;
                    }
                }
            }
            if (newSlot != -1) {
                Aura.mc.player.inventory.currentItem = newSlot;
            }
        }
        if (this.rotate.getValue()) {
            this.lookAtPacket(e.posX, e.posY, e.posZ, (EntityPlayer)Aura.mc.player);
        }
        Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.player, e);
        Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    private float getLagComp() {
        if (this.wait.getValue()) {
            return -(20.0f - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0f;
    }
    
    private boolean canEntityFeetBeSeen(final Entity entityIn) {
        return Aura.mc.world.rayTraceBlocks(new Vec3d(Aura.mc.player.posX, Aura.mc.player.posX + Aura.mc.player.getEyeHeight(), Aura.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = AutoCrystal.calculateLookAt(px, py, pz, me);
        this.setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private void setYawAndPitch(final float yaw1, final float pitch1) {
        this.yaw = yaw1;
        this.pitch = pitch1;
        this.isSpoofingAngles = true;
    }
    
    private void resetRotation() {
        if (this.isSpoofingAngles) {
            this.yaw = Aura.mc.player.rotationYaw;
            this.pitch = Aura.mc.player.rotationPitch;
            this.isSpoofingAngles = false;
        }
    }
    
    @Override
    public void onDisable() {
        this.resetRotation();
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        final Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && this.rotate.getValue() && this.isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)this.yaw;
            ((CPacketPlayer)packet).pitch = (float)this.pitch;
        }
    }
    
    private boolean shouldPause() {
        return (Xulu.MODULE_MANAGER.getModule(Surround.class).isToggled() && Surround.isExposed() && Xulu.MODULE_MANAGER.getModuleT(Surround.class).findObiInHotbar() != -1) || Xulu.MODULE_MANAGER.getModule(AutoTrap.class).isToggled() || Xulu.MODULE_MANAGER.getModule(HoleFill.class).isToggled() || (Xulu.MODULE_MANAGER.getModule(HoleBlocker.class).isToggled() && HoleBlocker.isExposed() && Xulu.MODULE_MANAGER.getModuleT(Surround.class).findObiInHotbar() != -1);
    }
}
