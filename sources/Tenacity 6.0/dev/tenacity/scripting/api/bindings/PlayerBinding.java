// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C18PacketSpectate;
import java.util.UUID;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import dev.tenacity.scripting.api.objects.ScriptPlayerCapabilites;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.entity.Entity;
import java.util.Arrays;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.item.ItemStack;
import dev.tenacity.utils.player.MovementUtils;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.utils.Utils;

@Exclude({ Strategy.NAME_REMAPPING })
public class PlayerBinding implements Utils
{
    public void respawn() {
        PlayerBinding.mc.thePlayer.respawnPlayer();
    }
    
    public void swingItem() {
        PlayerBinding.mc.thePlayer.swingItem();
    }
    
    public void setPitch(final double pitch) {
        PlayerBinding.mc.thePlayer.rotationPitch = (float)pitch;
    }
    
    public void setYaw(final double yaw) {
        PlayerBinding.mc.thePlayer.rotationYaw = (float)yaw;
    }
    
    public void setMotionX(final double x) {
        PlayerBinding.mc.thePlayer.motionZ = x;
    }
    
    public void setMotionY(final double y) {
        PlayerBinding.mc.thePlayer.motionY = y;
    }
    
    public void setMotionZ(final double z) {
        PlayerBinding.mc.thePlayer.motionZ = z;
    }
    
    public void setPosition(final double x, final double y, final double z) {
        PlayerBinding.mc.thePlayer.setPosition(x, y, z);
    }
    
    public void jump() {
        PlayerBinding.mc.thePlayer.jump();
    }
    
    public void setSpeed(final double speed) {
        MovementUtils.setSpeed(speed);
    }
    
    public void setSneaking(final boolean state) {
        PlayerBinding.mc.thePlayer.setSneaking(state);
    }
    
    public void setSprinting(final boolean state) {
        PlayerBinding.mc.thePlayer.setSprinting(state);
    }
    
    public void setFallDistance(final double fallDistance) {
        PlayerBinding.mc.thePlayer.setFallDistance((float)fallDistance);
    }
    
    public void leftClick() {
        PlayerBinding.mc.clickMouse();
    }
    
    public void rightClick() {
        PlayerBinding.mc.rightClickMouse();
    }
    
    public void setHeldItemSlot(final int slot) {
        PlayerBinding.mc.thePlayer.inventory.currentItem = slot;
    }
    
    public void sendMessage(final String msg) {
        PlayerBinding.mc.thePlayer.sendChatMessage(msg);
    }
    
    public boolean collidedHorizontally() {
        return PlayerBinding.mc.thePlayer.isCollidedHorizontally;
    }
    
    public boolean collidedVertically() {
        return PlayerBinding.mc.thePlayer.isCollidedVertically;
    }
    
    public boolean collided() {
        return PlayerBinding.mc.thePlayer.isCollided;
    }
    
    public boolean moving() {
        return MovementUtils.isMoving();
    }
    
    public boolean sneaking() {
        return PlayerBinding.mc.thePlayer.isSneaking();
    }
    
    public boolean sprinting() {
        return PlayerBinding.mc.thePlayer.isSprinting();
    }
    
    public boolean eating() {
        return PlayerBinding.mc.thePlayer.isEating();
    }
    
    public boolean onGround() {
        return PlayerBinding.mc.thePlayer.onGround;
    }
    
    public boolean airBorne() {
        return PlayerBinding.mc.thePlayer.isAirBorne;
    }
    
    public boolean onLadder() {
        return PlayerBinding.mc.thePlayer.isOnLadder();
    }
    
    public boolean inWater() {
        return PlayerBinding.mc.thePlayer.isInWater();
    }
    
    public boolean inLava() {
        return PlayerBinding.mc.thePlayer.isInLava();
    }
    
    public boolean inWeb() {
        return PlayerBinding.mc.thePlayer.isInWeb;
    }
    
    public boolean inPortal() {
        return PlayerBinding.mc.thePlayer.inPortal;
    }
    
    public boolean usingItem() {
        return PlayerBinding.mc.thePlayer.isUsingItem();
    }
    
    public boolean burning() {
        return PlayerBinding.mc.thePlayer.isBurning();
    }
    
    public boolean dead() {
        return PlayerBinding.mc.thePlayer.isDead;
    }
    
    public boolean isPotionActive(final int potionId) {
        return PlayerBinding.mc.thePlayer.isPotionActive(potionId);
    }
    
    public String name() {
        return PlayerBinding.mc.thePlayer.getName();
    }
    
    public int hurtTime() {
        return PlayerBinding.mc.thePlayer.hurtTime;
    }
    
    public int heldItemSlot() {
        return PlayerBinding.mc.thePlayer.inventory.currentItem;
    }
    
    public double ticksExisted() {
        return PlayerBinding.mc.thePlayer.ticksExisted;
    }
    
    public double fallDistance() {
        return PlayerBinding.mc.thePlayer.fallDistance;
    }
    
    public double health() {
        return PlayerBinding.mc.thePlayer.getHealth();
    }
    
    public double maxHealth() {
        return PlayerBinding.mc.thePlayer.getMaxHealth();
    }
    
    public double armorValue() {
        return PlayerBinding.mc.thePlayer.getTotalArmorValue();
    }
    
    public double hunger() {
        return PlayerBinding.mc.thePlayer.getFoodStats().getFoodLevel();
    }
    
    public double absorption() {
        return PlayerBinding.mc.thePlayer.getAbsorptionAmount();
    }
    
    public double pitch() {
        return PlayerBinding.mc.thePlayer.rotationPitch;
    }
    
    public double yaw() {
        return PlayerBinding.mc.thePlayer.rotationYaw;
    }
    
    public double x() {
        return PlayerBinding.mc.thePlayer.posX;
    }
    
    public double y() {
        return PlayerBinding.mc.thePlayer.posY;
    }
    
    public double z() {
        return PlayerBinding.mc.thePlayer.posZ;
    }
    
    public double prevX() {
        return PlayerBinding.mc.thePlayer.prevPosX;
    }
    
    public double prevY() {
        return PlayerBinding.mc.thePlayer.prevPosY;
    }
    
    public double prevZ() {
        return PlayerBinding.mc.thePlayer.prevPosZ;
    }
    
    public double motionX() {
        return PlayerBinding.mc.thePlayer.motionX;
    }
    
    public double motionY() {
        return PlayerBinding.mc.thePlayer.motionY;
    }
    
    public double motionZ() {
        return PlayerBinding.mc.thePlayer.motionZ;
    }
    
    public double speed() {
        return MovementUtils.getSpeed();
    }
    
    public double timerSpeed() {
        return MovementUtils.getSpeed();
    }
    
    public double getBaseMoveSpeed() {
        return MovementUtils.getBaseMoveSpeed();
    }
    
    public ItemStack getInventorySlot(final int slot) {
        return PlayerBinding.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
    }
    
    public String getServerIP() {
        return (PlayerBinding.mc.isSingleplayer() || PlayerBinding.mc.getCurrentServerData() == null) ? "singleplayer" : PlayerBinding.mc.getCurrentServerData().serverIP;
    }
    
    public void sendPacket(final int integer, final Object... args) {
        Packet<?> packet = null;
        switch (integer) {
            case 36: {
                packet = new C0APacketAnimation();
                break;
            }
            case 37: {
                final C0BPacketEntityAction.Action c0bAction = Arrays.stream(C0BPacketEntityAction.Action.values()).filter(action -> action.getId() == (int)args[0]).findFirst().orElse(null);
                packet = new C0BPacketEntityAction(PlayerBinding.mc.thePlayer, c0bAction);
                break;
            }
            case 38: {
                packet = new C0CPacketInput((float)args[0], (float)args[1], (boolean)args[2], (boolean)args[3]);
                break;
            }
            case 39: {
                packet = new C0DPacketCloseWindow((int)args[0]);
                break;
            }
            case 5: {
                packet = new C0FPacketConfirmTransaction((int)args[0], (short)args[1], (boolean)args[2]);
                break;
            }
            case 6: {
                packet = new C00PacketKeepAlive((int)args[0]);
                break;
            }
            case 8: {
                final C02PacketUseEntity.Action c02Action = Arrays.stream(C02PacketUseEntity.Action.values()).filter(action -> action.getId() == (int)args[1]).findFirst().orElse(null);
                packet = new C02PacketUseEntity((Entity)args[0], c02Action);
                break;
            }
            case 9: {
                packet = new C03PacketPlayer((boolean)args[0]);
                break;
            }
            case 10: {
                packet = new C03PacketPlayer.C04PacketPlayerPosition((double)args[0], (double)args[1], (double)args[2], (boolean)args[3]);
                break;
            }
            case 11: {
                final Double arg0 = (Double)args[0];
                final Double arg2 = (Double)args[1];
                packet = new C03PacketPlayer.C05PacketPlayerLook(arg0.floatValue(), arg2.floatValue(), (boolean)args[2]);
                break;
            }
            case 12: {
                final Double arg3 = (Double)args[3];
                final Double arg4 = (Double)args[4];
                packet = new C03PacketPlayer.C06PacketPlayerPosLook((double)args[0], (double)args[1], (double)args[2], arg3.floatValue(), arg4.floatValue(), (boolean)args[5]);
                break;
            }
            case 13: {
                final C07PacketPlayerDigging.Action c07Action = Arrays.stream(C07PacketPlayerDigging.Action.values()).filter(action -> action.getId() == (int)args[0]).findFirst().orElse(null);
                final EnumFacing facing2 = Arrays.stream(EnumFacing.values()).filter(facing1 -> facing1.getIndex() == (int)args[4]).findFirst().orElse(null);
                packet = new C07PacketPlayerDigging(c07Action, new BlockPos((double)args[1], (double)args[2], (double)args[3]), facing2);
                break;
            }
            case 14: {
                final Double arg5 = (Double)args[5];
                final Double arg6 = (Double)args[6];
                final Double arg7 = (Double)args[7];
                packet = new C08PacketPlayerBlockPlacement(new BlockPos((double)args[0], (double)args[1], (double)args[2]), (int)args[3], (ItemStack)args[4], arg5.floatValue(), arg6.floatValue(), arg7.floatValue());
                break;
            }
            case 15: {
                packet = new C09PacketHeldItemChange((int)args[0]);
                break;
            }
            case 19: {
                final ScriptPlayerCapabilites abilites = (ScriptPlayerCapabilites)args[0];
                packet = new C13PacketPlayerAbilities(abilites.getActualAbilites());
                break;
            }
            case 22: {
                final C16PacketClientStatus.EnumState state2 = Arrays.stream(C16PacketClientStatus.EnumState.values()).filter(state1 -> state1.getId() == (int)args[0]).findFirst().orElse(null);
                packet = new C16PacketClientStatus(state2);
                break;
            }
            case 24: {
                packet = new C18PacketSpectate((UUID)args[0]);
                break;
            }
        }
        PacketUtils.sendPacketNoEvent(packet);
    }
}
