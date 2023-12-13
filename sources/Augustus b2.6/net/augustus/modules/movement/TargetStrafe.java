// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.modules.misc.MidClick;
import net.augustus.modules.combat.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.augustus.events.EventJump;
import net.augustus.utils.BlockUtil;
import net.augustus.events.EventMove;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.block.Block;
import net.augustus.Augustus;
import net.augustus.utils.MoveUtil;
import net.minecraft.entity.Entity;
import net.augustus.utils.RotationUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import java.util.Comparator;
import java.util.function.Predicate;
import net.augustus.events.EventSilentMove;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.entity.EntityLivingBase;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class TargetStrafe extends Module
{
    private final DoubleValue radius;
    private final BooleanValue killAuraTarget;
    private final BooleanValue autoStrafe;
    private final TimeHelper timeHelper;
    public float moveYaw;
    public EntityLivingBase target;
    private Object[] listOfTargets;
    private boolean leftRight;
    
    public TargetStrafe() {
        super("TargetStrafe", new Color(52, 0, 194), Categorys.MOVEMENT);
        this.radius = new DoubleValue(1, "Radius", this, 2.5, 0.1, 6.0, 1);
        this.killAuraTarget = new BooleanValue(2, "KillAuraTarget", this, true);
        this.autoStrafe = new BooleanValue(3, "AutoStrafe", this, true);
        this.timeHelper = new TimeHelper();
        this.target = null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        this.target = null;
        if (this.killAuraTarget.getBoolean()) {
            if (TargetStrafe.mm.killAura.isToggled()) {
                this.target = TargetStrafe.mm.killAura.target;
            }
        }
        else {
            this.listOfTargets = TargetStrafe.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entity -> TargetStrafe.mc.thePlayer.getDistanceToEntity(entity))).toArray();
            if (this.listOfTargets.length > 0) {
                this.target = (EntityLivingBase)this.listOfTargets[0];
            }
        }
        if (this.target != null && !TargetStrafe.mc.thePlayer.isOnLadder()) {
            if (TargetStrafe.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                this.leftRight = false;
            }
            else if (TargetStrafe.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                this.leftRight = true;
            }
            final Block block = TargetStrafe.mc.theWorld.getBlockState(new BlockPos(TargetStrafe.mc.thePlayer.posX, TargetStrafe.mc.thePlayer.posY - 2.0, TargetStrafe.mc.thePlayer.posZ)).getBlock();
            if (TargetStrafe.mc.thePlayer.isCollidedHorizontally) {
                this.leftRight = !this.leftRight;
            }
            if (block instanceof BlockAir && !TargetStrafe.mc.thePlayer.onGround && this.timeHelper.reached(300L)) {
                this.leftRight = !this.leftRight;
                this.timeHelper.reset();
            }
            final double radius = this.radius.getValue();
            final RotationUtil rotationUtil = new RotationUtil();
            double yaw = rotationUtil.middleRotation(this.target, TargetStrafe.mc.thePlayer.rotationYaw, TargetStrafe.mc.thePlayer.rotationPitch, false)[0];
            yaw += (this.leftRight ? -1 : 1) * 45;
            final double direction = Math.toRadians(yaw);
            final double posX = this.target.posX + Math.sin(direction) * radius;
            final double posZ = this.target.posZ - Math.cos(direction) * radius;
            this.moveYaw = rotationUtil.positionRotation(posX, this.target.posY, posZ, TargetStrafe.mc.thePlayer.rotationYaw, TargetStrafe.mc.thePlayer.rotationPitch, false)[0];
            if (this.autoStrafe.getBoolean()) {
                MoveUtil.strafe();
            }
            if (this.target != null) {}
            return;
        }
        this.moveYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (this.target != null && !BlockUtil.isScaffoldToggled()) {
            eventMove.setYaw(this.moveYaw);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (this.target != null && !BlockUtil.isScaffoldToggled()) {
            eventJump.setYaw(this.moveYaw);
        }
    }
    
    private boolean canAttacked(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (!TargetStrafe.mc.thePlayer.canEntityBeSeen(entity)) {
                return false;
            }
            if (entity.isInvisible() || ((EntityLivingBase)entity).deathTime > 1) {
                return false;
            }
            if (entity.ticksExisted < 1) {
                return false;
            }
            if (entity instanceof EntityPlayer && TargetStrafe.mm.teams.isToggled() && TargetStrafe.mm.teams.getTeammates().contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && (entity.getName().equals("§aShop") || entity.getName().equals("SHOP") || entity.getName().equals("UPGRADES"))) {
                return false;
            }
            if (entity.isDead) {
                return false;
            }
            if (entity instanceof EntityPlayer && TargetStrafe.mm.antiBot.isToggled() && AntiBot.bots.contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && !TargetStrafe.mm.midClick.noFiends && MidClick.friends.contains(entity.getName())) {
                return false;
            }
        }
        return entity instanceof EntityLivingBase && entity != TargetStrafe.mc.thePlayer && TargetStrafe.mc.thePlayer.getDistanceToEntity(entity) < 7.0f;
    }
}
