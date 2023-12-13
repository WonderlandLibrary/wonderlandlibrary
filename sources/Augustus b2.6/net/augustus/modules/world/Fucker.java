// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.world;

import net.minecraft.network.ThreadQuickExitException;
import java.util.Comparator;
import java.util.HashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventJump;
import net.augustus.Augustus;
import net.augustus.events.EventMove;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.augustus.events.EventClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.utils.RayTraceUtil;
import net.augustus.utils.RandomUtil;
import net.augustus.utils.BlockUtil;
import net.augustus.events.EventEarlyTick;
import net.minecraft.client.settings.KeyBinding;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.network.INetHandler;
import net.minecraft.util.EnumFacing;
import net.augustus.utils.RotationUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class Fucker extends Module
{
    private final TimeHelper timeHelper;
    public StringValue block;
    public StringValue action;
    public BooleanValue troughWall;
    public BooleanValue instant;
    public BooleanValue moveFix;
    public BooleanValue myBed;
    public DoubleValue customID;
    public DoubleValue yawSpeed;
    public DoubleValue pitchSpeed;
    public DoubleValue delay;
    public DoubleValue range;
    public ArrayList<Packet> packets;
    public BlockPos b;
    private RotationUtil rotationUtil;
    private float[] rots;
    private float[] lastRots;
    private EnumFacing lastEnumFacing;
    private int slotID;
    private boolean breaking;
    private INetHandler netHandler;
    
    public Fucker() {
        super("Fucker", new Color(161, 17, 51), Categorys.WORLD);
        this.timeHelper = new TimeHelper();
        this.block = new StringValue(2, "Block", this, "Bed", new String[] { "Bed", "Cake", "Custom" });
        this.action = new StringValue(3, "Action", this, "Break", new String[] { "Break", "Click", "Use" });
        this.troughWall = new BooleanValue(1, "ThroughWall", this, true);
        this.instant = new BooleanValue(10, "Instant", this, true);
        this.moveFix = new BooleanValue(9, "MoveFix", this, true);
        this.myBed = new BooleanValue(11, "FriendMyBlock", this, true);
        this.customID = new DoubleValue(4, "CustomID", this, 26.0, 0.0, 400.0, 0);
        this.yawSpeed = new DoubleValue(5, "YawSpeed", this, 180.0, 0.0, 180.0, 0);
        this.pitchSpeed = new DoubleValue(6, "PitchSpeed", this, 180.0, 0.0, 180.0, 0);
        this.delay = new DoubleValue(7, "Delay", this, 500.0, 0.0, 1000.0, 0);
        this.range = new DoubleValue(8, "Range", this, 4.5, 0.0, 6.0, 1);
        this.packets = new ArrayList<Packet>();
        this.rotationUtil = new RotationUtil();
        this.rots = new float[2];
        this.lastRots = new float[2];
        this.breaking = false;
        this.netHandler = null;
        this.customID.setVisible(false);
    }
    
    @Override
    public void onEnable() {
        this.rotationUtil = new RotationUtil();
        if (Fucker.mc.thePlayer == null) {
            return;
        }
        this.breaking = false;
        this.rots[0] = Fucker.mc.thePlayer.rotationYaw;
        this.rots[1] = Fucker.mc.thePlayer.rotationPitch;
        this.lastRots[0] = Fucker.mc.thePlayer.prevRotationYaw;
        this.lastRots[1] = Fucker.mc.thePlayer.prevRotationPitch;
        this.slotID = Fucker.mc.thePlayer.inventory.currentItem + 36;
        this.b = null;
        this.lastEnumFacing = null;
    }
    
    @Override
    public void onDisable() {
        if (this.slotID != Fucker.mc.thePlayer.inventory.currentItem + 36) {
            this.slotID = Fucker.mc.thePlayer.inventory.currentItem + 36;
        }
        if (this.breaking) {
            KeyBinding.setKeyBindState(Fucker.mc.gameSettings.keyBindAttack.getKeyCode(), false);
            this.breaking = false;
        }
        this.resetPackets(this.netHandler);
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        this.setDisplayName(super.getName() + " §8" + this.block.getSelected());
        Vec3 bedVec = null;
        if ((!Fucker.mm.killAura.isToggled() || Fucker.mm.killAura.target == null) && !BlockUtil.isScaffoldToggled()) {
            bedVec = this.getBedPos();
            this.b = ((bedVec == null) ? null : new BlockPos(bedVec));
        }
        else {
            this.b = null;
        }
        if (this.b == null) {
            this.lastRots = this.rots;
            return;
        }
        final float yawSpeed = (float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f));
        final float pitchSpeed = (float)(this.pitchSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f));
        final Block block = Fucker.mc.theWorld.getBlockState(this.b).getBlock();
        final float[] floats = this.rotationUtil.scaffoldRots(this.b.getX() + block.getBlockBoundsMaxX() / 2.0, this.b.getY() + block.getBlockBoundsMaxY() / 2.0, this.b.getZ() + block.getBlockBoundsMaxZ() / 2.0, this.rots[0], this.rots[1], yawSpeed, pitchSpeed, false);
        final MovingObjectPosition objectPosition = RayTraceUtil.rayCast(1.0f, floats);
        if (objectPosition != null && objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !this.isValidBlock(objectPosition.getBlockPos(), this.getId())) {
            for (float yaw = this.rots[0] - Math.min(yawSpeed, 40.0f); yaw < this.rots[0] + Math.min(yawSpeed, 40.0f); yaw += 2.0f) {
                for (float pitch = MathHelper.clamp_float(this.rots[1] - Math.min(pitchSpeed, 30.0f), -90.0f, 90.0f); pitch < MathHelper.clamp_float(this.rots[1] + Math.min(pitchSpeed, 30.0f), -90.0f, 90.0f); pitch += 2.0f) {
                    final float[] sensedRots = RotationUtil.mouseSens(yaw, pitch, this.rots[0], this.rots[1]);
                    final MovingObjectPosition objectPosition2 = RayTraceUtil.rayCast(1.0f, sensedRots);
                    if (objectPosition2 != null && objectPosition2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.isValidBlock(objectPosition2.getBlockPos(), this.getId())) {
                        this.lastRots = this.rots;
                        this.rots = sensedRots;
                        this.setRotation();
                        return;
                    }
                }
            }
        }
        this.lastRots = this.rots;
        this.rots = floats;
        this.setRotation();
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if ((!Fucker.mm.killAura.isToggled() || Fucker.mm.killAura.target == null) && !BlockUtil.isScaffoldToggled()) {
            boolean bb = false;
            if (this.b != null) {
                this.breaking = false;
                if (this.troughWall.getBoolean()) {
                    eventClick.setCanceled(true);
                    if (Fucker.mc.thePlayer.isUsingItem()) {
                        Fucker.mc.thePlayer.stopUsingItem();
                        Fucker.mc.sendClickBlockToController(Fucker.mc.currentScreen == null && Fucker.mc.gameSettings.keyBindAttack.isKeyDown() && Fucker.mc.inGameHasFocus);
                        return;
                    }
                    this.slotID = Fucker.mc.thePlayer.inventory.currentItem + 36;
                    final MovingObjectPosition movingObjectPosition = RayTraceUtil.getHitVec(this.b, this.rots[0], this.rots[1], this.range.getValue());
                    if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        final String selected = this.action.getSelected();
                        switch (selected) {
                            case "Break": {
                                if (this.instant.getBoolean()) {
                                    this.breakInstant(this.b, movingObjectPosition.sideHit);
                                    break;
                                }
                                Fucker.mc.sendClickBlockToControllerCustom(Fucker.mc.currentScreen == null && Fucker.mc.inGameHasFocus, movingObjectPosition, this.b, this.slotID - 36);
                                this.lastEnumFacing = movingObjectPosition.sideHit;
                                break;
                            }
                            case "Use": {
                                if (this.timeHelper.reached((long)this.delay.getValue()) && Fucker.mc.playerController.onPlayerRightClick(Fucker.mc.thePlayer, Fucker.mc.theWorld, Fucker.mc.thePlayer.getHeldItem(), this.b, movingObjectPosition.sideHit, movingObjectPosition.hitVec)) {
                                    Fucker.mc.thePlayer.swingItem();
                                    this.timeHelper.reset();
                                    break;
                                }
                                break;
                            }
                            default: {
                                if (this.timeHelper.reached((long)this.delay.getValue())) {
                                    Fucker.mc.thePlayer.swingItem();
                                    Fucker.mc.playerController.clickBlock(this.b, movingObjectPosition.sideHit);
                                    this.timeHelper.reset();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    else if (this.lastEnumFacing != null) {
                        Fucker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.b, this.lastEnumFacing));
                    }
                }
                else {
                    final int bestItemID = this.getBestItem();
                    if (bestItemID != -1) {
                        eventClick.setSlot(bestItemID - 36);
                        this.slotID = bestItemID;
                    }
                    else {
                        this.slotID = Fucker.mc.thePlayer.inventory.currentItem + 36;
                    }
                    if (Fucker.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !Fucker.mc.objectMouseOver.getBlockPos().equals(this.b)) {
                        KeyBinding.setKeyBindState(Fucker.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                        this.breaking = true;
                    }
                    else {
                        final MovingObjectPosition movingObjectPosition2 = Fucker.mc.objectMouseOver;
                        if (movingObjectPosition2 != null) {
                            final String selected2 = this.action.getSelected();
                            switch (selected2) {
                                case "Break": {
                                    if (this.instant.getBoolean()) {
                                        this.breakInstant(this.b, movingObjectPosition2.sideHit);
                                        break;
                                    }
                                    bb = true;
                                    this.lastEnumFacing = movingObjectPosition2.sideHit;
                                    KeyBinding.setKeyBindState(Fucker.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                                    this.breaking = true;
                                    break;
                                }
                                case "Use": {
                                    if (this.timeHelper.reached((long)this.delay.getValue()) && Fucker.mc.playerController.onPlayerRightClick(Fucker.mc.thePlayer, Fucker.mc.theWorld, Fucker.mc.thePlayer.getHeldItem(), this.b, movingObjectPosition2.sideHit, movingObjectPosition2.hitVec)) {
                                        Fucker.mc.thePlayer.swingItem();
                                        this.timeHelper.reset();
                                        break;
                                    }
                                    break;
                                }
                                default: {
                                    if (this.timeHelper.reached((long)this.delay.getValue())) {
                                        Fucker.mc.thePlayer.swingItem();
                                        Fucker.mc.playerController.clickBlock(this.b, movingObjectPosition2.sideHit);
                                        this.timeHelper.reset();
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else {
                if (this.breaking) {
                    KeyBinding.setKeyBindState(Fucker.mc.gameSettings.keyBindAttack.getKeyCode(), false);
                    this.breaking = false;
                }
                this.slotID = Fucker.mc.thePlayer.inventory.currentItem + 36;
            }
        }
        else {
            this.slotID = Fucker.mc.thePlayer.inventory.currentItem + 36;
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (!this.moveFix.getBoolean() && this.b != null) {
            eventMove.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!this.moveFix.getBoolean() && this.b != null) {
            eventJump.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (this.moveFix.getBoolean() && this.b != null) {
            eventSilentMove.setSilent(true);
        }
    }
    
    private int getBestItem() {
        float maxStrength = 0.0f;
        int bestItem = -1;
        for (int i = 36; i < Fucker.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
            final ItemStack itemStack = Fucker.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemTool && Fucker.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final Block block = Fucker.mc.theWorld.getBlockState(Fucker.mc.objectMouseOver.getBlockPos()).getBlock();
                if (this.getToolSpeed(itemStack, block) > maxStrength) {
                    maxStrength = this.getToolSpeed(itemStack, block);
                    bestItem = i;
                }
            }
        }
        return bestItem;
    }
    
    private float getToolSpeed(final ItemStack itemStack, final Block block) {
        float damage = 0.0f;
        if (itemStack.getItem() instanceof ItemTool) {
            damage += itemStack.getItem().getStrVsBlock(itemStack, block) + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
            damage += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 11.0);
            damage += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemStack) / 11.0);
            damage += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) / 33.0);
        }
        return damage;
    }
    
    private void setRotation() {
        if (Fucker.mc.currentScreen != null) {
            return;
        }
        Fucker.mc.thePlayer.rotationYaw = this.rots[0];
        Fucker.mc.thePlayer.rotationPitch = this.rots[1];
        Fucker.mc.thePlayer.prevRotationYaw = this.lastRots[0];
        Fucker.mc.thePlayer.prevRotationPitch = this.lastRots[1];
    }
    
    private Vec3 getBedPos() {
        final BlockPos b = new BlockPos(Fucker.mc.thePlayer.posX, Fucker.mc.thePlayer.posY, Fucker.mc.thePlayer.posZ);
        final ArrayList<Vec3> positions = new ArrayList<Vec3>();
        final HashMap<Vec3, BlockPos> map = new HashMap<Vec3, BlockPos>();
        for (int d = (int)(this.range.getValue() + 1.0), x = b.getX() - d; x < b.getX() + d; ++x) {
            for (int y = b.getY() - d; y < b.getY() + d; ++y) {
                for (int z = b.getZ() - d; z < b.getZ() + d; ++z) {
                    if (this.isValidBlock(new BlockPos(x, y, z), this.getId())) {
                        final BlockPos blockPos = new BlockPos(x, y, z);
                        final Vec3 vec4 = new Vec3(x, y, z);
                        if (this.myBed.getBoolean() && Fucker.mc.thePlayer.getSpawnPos() != null) {
                            if (Fucker.mc.thePlayer.getSpawnPos().distanceTo(vec4) > 20.0) {
                                positions.add(vec4);
                                map.put(vec4, blockPos);
                            }
                        }
                        else {
                            positions.add(vec4);
                            map.put(vec4, blockPos);
                        }
                    }
                }
            }
        }
        positions.sort(Comparator.comparingDouble(vec3 -> Fucker.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
        if (!positions.isEmpty()) {
            return positions.get(0);
        }
        return null;
    }
    
    private int getId() {
        final String selected = this.block.getSelected();
        int id = 0;
        switch (selected) {
            case "Bed": {
                id = 26;
                break;
            }
            case "Cake": {
                id = 92;
                break;
            }
            default: {
                id = (int)this.customID.getValue();
                break;
            }
        }
        return id;
    }
    
    private boolean isValidBlock(final BlockPos blockPos, final int id) {
        if (blockPos != null) {
            final Block block = Fucker.mc.theWorld.getBlockState(blockPos).getBlock();
            return block.equals(Block.getBlockById(id));
        }
        return false;
    }
    
    private void breakInstant(final BlockPos blockPos, final EnumFacing enumFacing) {
        if (this.timeHelper.reached((long)this.delay.getValue())) {
            Fucker.mc.thePlayer.swingItem();
            Fucker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, enumFacing));
            Fucker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, enumFacing));
            this.timeHelper.reset();
        }
    }
    
    public int getSlotID() {
        return this.slotID - 36;
    }
    
    public void resetPackets(final INetHandler iNetHandler) {
        if (this.packets.size() > 0) {
            while (this.packets.size() != 0) {
                try {
                    this.packets.get(0).processPacket(iNetHandler);
                }
                catch (ThreadQuickExitException ex) {}
                this.packets.remove(this.packets.get(0));
            }
        }
    }
}
