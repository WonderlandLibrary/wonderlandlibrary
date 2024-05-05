// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import Lavish.event.events.EventRenderGUI;
import net.minecraft.potion.Potion;
import Lavish.event.events.EventMotion;
import Lavish.event.events.EventUpdate;
import Lavish.utils.rotation.RotationUtils;
import Lavish.utils.render.RenderUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import Lavish.event.events.EventPacket;
import Lavish.event.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import Lavish.utils.misc.ScaffoldUtils;
import Lavish.utils.movement.MovementUtil;
import net.minecraft.network.play.client.C0APacketAnimation;
import Lavish.utils.math.MathUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import Lavish.utils.animations.Direction;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.utils.animations.impl.DecelerateAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class Scaffold extends Module
{
    public static boolean swing;
    public static boolean tower;
    int slot;
    int lastItem;
    int lastItemBeforeStart;
    boolean down;
    Timer downwardsTimer;
    Timer towerTimer;
    Timer slowDownTimer;
    Timer timer;
    public static BlockCache blockCache;
    public static BlockCache lastBlockCache;
    static Minecraft mc;
    private ArrayList<Packet> packets;
    private double yPos;
    Timer timerscaffold;
    public boolean placing;
    float[] rotations;
    public static float yaw;
    public static float pitch;
    public static int ypos;
    double[] coords;
    BlockPos blockPos2;
    int ticks;
    boolean done;
    protected float zLevel;
    DecelerateAnimation ani;
    
    static {
        Scaffold.tower = false;
        Scaffold.mc = Minecraft.getMinecraft();
        Scaffold.yaw = 999.0f;
        Scaffold.pitch = 999.0f;
    }
    
    public Scaffold() {
        super("Scaffold", 0, true, Category.Player, "Places blocks under you while you walk");
        this.lastItem = -1;
        this.lastItemBeforeStart = 0;
        this.down = false;
        this.packets = new ArrayList<Packet>();
        this.timerscaffold = new Timer();
        this.placing = false;
        this.ticks = 0;
        this.done = false;
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Hypixel");
        options.add("NCP");
        this.downwardsTimer = new Timer();
        this.towerTimer = new Timer();
        this.slowDownTimer = new Timer();
        this.timer = new Timer();
        Client.instance.setmgr.rSetting(new Setting("Scaffold Rotation", this, "Hypixel", options));
        Client.instance.setmgr.rSetting(new Setting("Tower", this, false));
        Client.instance.setmgr.rSetting(new Setting("Sprint", this, false));
        Client.instance.setmgr.rSetting(new Setting("Down", this, false));
        Client.instance.setmgr.rSetting(new Setting("Timer Abuse", this, false));
        Client.instance.setmgr.rSetting(new Setting("Expand Length", this, 1.0, 0.01, 10.0, false));
        Client.instance.setmgr.rSetting(new Setting("Timer", this, 1.0, 0.5, 5.0, false));
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        this.done = false;
        Scaffold.ypos = (int)Scaffold.mc.thePlayer.posY;
        Scaffold.yaw = 999.0f;
        Scaffold.pitch = 999.0f;
        this.down = false;
        Scaffold.tower = Client.instance.setmgr.getSettingByName("Tower").getValBoolean();
        Scaffold.mc.timer.timerSpeed = (float)Client.instance.setmgr.getSettingByName("Timer").getValDouble();
        this.downwardsTimer.reset();
        this.towerTimer.reset();
        this.slowDownTimer.reset();
        this.timer.reset();
        this.timerscaffold.reset();
        this.packets.clear();
        this.lastItemBeforeStart = Scaffold.mc.thePlayer.inventory.currentItem;
        (this.ani = new DecelerateAnimation(700, 100.0, Direction.BACKWARDS)).reset();
    }
    
    @Override
    public void onDisable() {
        this.ticks = 0;
        this.done = false;
        Scaffold.yaw = 999.0f;
        Scaffold.pitch = 999.0f;
        Scaffold.mc.timer.timerSpeed = 1.0f;
        Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
        this.downwardsTimer.reset();
        this.towerTimer.reset();
        this.slowDownTimer.reset();
        this.timer.reset();
        Scaffold.blockCache = null;
        Scaffold.lastBlockCache = null;
        this.down = false;
        Scaffold.mc.thePlayer.inventory.currentItem = this.lastItemBeforeStart;
    }
    
    private boolean placeBlock(final BlockPos pos, final EnumFacing facing) {
        final Vec3 eyesPos = new Vec3(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY + Scaffold.mc.thePlayer.getEyeHeight(), Scaffold.mc.thePlayer.posZ);
        Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getHeldItem(), Scaffold.lastBlockCache.position, Scaffold.lastBlockCache.facing, new Vec3(Scaffold.lastBlockCache.position.getX() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9, Scaffold.lastBlockCache.position.getY() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9, Scaffold.lastBlockCache.position.getZ() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9));
        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        Scaffold.mc.thePlayer.swingItem();
        return true;
    }
    
    private BlockCache grab() {
        final EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
        BlockPos position = new BlockPos(0, 0, 0);
        if (MovementUtil.isMoving() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
            for (double n2 = Client.instance.setmgr.getSettingByName("Expand Length").getValDouble(), n3 = 0.0; n3 <= n2; n3 += n2 / (Math.floor(n2) + 1.0)) {
                if (Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && Client.instance.setmgr.getSettingByName("Down").getValBoolean()) {
                    final BlockPos blockPos2 = new BlockPos(Scaffold.mc.thePlayer.posX - MathHelper.sin(ScaffoldUtils.clampRotation()) * n3, Scaffold.mc.thePlayer.posY - 1.5, Scaffold.mc.thePlayer.posZ + MathHelper.cos(ScaffoldUtils.clampRotation()) * n3);
                    final IBlockState blockState = Scaffold.mc.theWorld.getBlockState(blockPos2);
                    if (blockState != null && blockState.getBlock() == Blocks.air) {
                        position = blockPos2;
                        break;
                    }
                }
                if (!Scaffold.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (Client.instance.moduleManager.getModuleByName("Speed").isEnabled()) {
                        final BlockPos blockPos2 = new BlockPos(Scaffold.mc.thePlayer.posX - MathHelper.sin(ScaffoldUtils.clampRotation()) * n3, Scaffold.ypos - 1, Scaffold.mc.thePlayer.posZ + MathHelper.cos(ScaffoldUtils.clampRotation()) * n3);
                        final IBlockState blockState = Scaffold.mc.theWorld.getBlockState(blockPos2);
                        if (blockState != null && blockState.getBlock() == Blocks.air) {
                            position = blockPos2;
                            break;
                        }
                    }
                    else {
                        final BlockPos blockPos2 = new BlockPos(Scaffold.mc.thePlayer.posX - MathHelper.sin(ScaffoldUtils.clampRotation()) * n3, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ + MathHelper.cos(ScaffoldUtils.clampRotation()) * n3);
                        final IBlockState blockState = Scaffold.mc.theWorld.getBlockState(blockPos2);
                        if (blockState != null && blockState.getBlock() == Blocks.air) {
                            position = blockPos2;
                            break;
                        }
                    }
                }
            }
        }
        else if (Client.instance.moduleManager.getModuleByName("Speed").isEnabled()) {
            position = new BlockPos(new BlockPos(Scaffold.mc.thePlayer.getPositionVector().xCoord, Scaffold.ypos, Scaffold.mc.thePlayer.getPositionVector().zCoord)).offset(EnumFacing.DOWN);
        }
        else {
            position = new BlockPos(new BlockPos(Scaffold.mc.thePlayer.getPositionVector().xCoord, Scaffold.mc.thePlayer.getPositionVector().yCoord, Scaffold.mc.thePlayer.getPositionVector().zCoord)).offset(EnumFacing.DOWN);
        }
        if (!(Scaffold.mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir) && !(Scaffold.mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
            return null;
        }
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing facing = values[i];
            final BlockPos offset = position.offset(facing);
            final IBlockState blockState = Scaffold.mc.theWorld.getBlockState(offset);
            if (!(Scaffold.mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir) && !(Scaffold.mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
                return new BlockCache(offset, invert[facing.ordinal()], (BlockCache)null, (BlockCache)null);
            }
        }
        final BlockPos[] offsets = { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        BlockPos[] array;
        for (int length2 = (array = offsets).length, j = 0; j < length2; ++j) {
            final BlockPos offset2 = array[j];
            final BlockPos offsetPos = position.add(offset2.getX(), 0, offset2.getZ());
            final IBlockState blockState2 = Scaffold.mc.theWorld.getBlockState(offsetPos);
            if (Scaffold.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                EnumFacing[] values2;
                for (int length3 = (values2 = EnumFacing.values()).length, k = 0; k < length3; ++k) {
                    final EnumFacing facing2 = values2[k];
                    final BlockPos offset3 = offsetPos.offset(facing2);
                    final IBlockState blockState3 = Scaffold.mc.theWorld.getBlockState(offset3);
                    if (!(Scaffold.mc.theWorld.getBlockState(offset3).getBlock() instanceof BlockAir)) {
                        return new BlockCache(offset3, invert[facing2.ordinal()], (BlockCache)null, (BlockCache)null);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventPacket && ((EventPacket)event).getPacket() instanceof C03PacketPlayer && ((EventPacket)event).isSending()) {
            final Packet packet = ((EventPacket)event).getPacket();
            final C03PacketPlayer C03 = (C03PacketPlayer)packet;
            if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("Hypixel")) {
                Scaffold.mc.thePlayer.rotationYaw += 0.001f;
                Scaffold.mc.thePlayer.rotationPitch += 0.001f;
                RenderUtils.setCustomPitch(87.0f);
                C03.setYaw(Scaffold.mc.thePlayer.rotationYaw + 180.0f);
                C03.setPitch(87);
                Scaffold.yaw = Scaffold.mc.thePlayer.rotationYaw + 180.0f;
                Scaffold.pitch = 87.0f;
            }
            if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("NCP")) {
                if (Scaffold.lastBlockCache != null) {
                    final float[] rot = RotationUtils.aimAtLocation(Scaffold.lastBlockCache.position, Scaffold.lastBlockCache.facing);
                    RenderUtils.setCustomPitch(rot[1]);
                    Scaffold.mc.thePlayer.rotationYaw += 0.001f;
                    Scaffold.mc.thePlayer.rotationPitch += 0.001f;
                    C03.setYaw(rot[0]);
                    C03.setPitch((int)rot[1]);
                    Scaffold.yaw = rot[0];
                    Scaffold.pitch = rot[1];
                }
            }
            else {
                RenderUtils.setCustomPitch(Scaffold.mc.thePlayer.rotationPitch);
            }
        }
        if (event instanceof EventUpdate && !Scaffold.mc.gameSettings.keyBindForward.isKeyDown()) {
            Scaffold.mc.timer.timerSpeed = 1.0f;
        }
        if (event instanceof EventMotion) {
            if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("Hypixel")) {
                final int blockCount = this.getBlockCount();
                if (blockCount != 0 && Scaffold.yaw != 999.0f && Scaffold.pitch != 999.0f) {
                    Scaffold.mc.thePlayer.rotationYawHead = Scaffold.mc.thePlayer.rotationYaw + 180.0f;
                    Scaffold.mc.thePlayer.renderYawOffset = Scaffold.mc.thePlayer.rotationYaw + 180.0f;
                    Scaffold.mc.thePlayer.rotationYaw += 0.001f;
                    Scaffold.mc.thePlayer.rotationPitch += 0.001f;
                    RenderUtils.setCustomPitch(87.0f);
                    ((EventMotion)event).setYaw(Scaffold.mc.thePlayer.rotationYaw + 180.0f);
                    ((EventMotion)event).setPitch(87.0f);
                }
            }
            if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("NCP")) {
                final int blockCount = this.getBlockCount();
                if (blockCount != 0 && Scaffold.yaw != 999.0f && Scaffold.pitch != 999.0f) {
                    Scaffold.mc.thePlayer.rotationYawHead = Scaffold.yaw;
                    Scaffold.mc.thePlayer.renderYawOffset = Scaffold.yaw;
                    Scaffold.mc.thePlayer.rotationYaw += 0.001f;
                    Scaffold.mc.thePlayer.rotationPitch += 0.001f;
                    ((EventMotion)event).setYaw(Scaffold.yaw);
                    ((EventMotion)event).setPitch(Scaffold.pitch);
                }
            }
            if (Client.instance.setmgr.getSettingByName("Sprint").getValBoolean()) {
                if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("NCP") && Scaffold.mc.thePlayer.moveForward > 0.0f && !Scaffold.mc.thePlayer.isUsingItem() && !Scaffold.mc.thePlayer.isSneaking() && !Scaffold.mc.thePlayer.isCollidedHorizontally && Scaffold.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                    Scaffold.mc.thePlayer.setSprinting(true);
                }
                if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("Hypixel")) {
                    if (Scaffold.lastBlockCache == null) {
                        if (Scaffold.mc.thePlayer.moveForward > 0.0f && !Scaffold.mc.thePlayer.isUsingItem() && !Scaffold.mc.thePlayer.isSneaking() && !Scaffold.mc.thePlayer.isCollidedHorizontally && Scaffold.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                            Scaffold.mc.thePlayer.setSprinting(true);
                        }
                    }
                    else {
                        Scaffold.mc.thePlayer.setSprinting(false);
                    }
                }
            }
            else {
                Scaffold.mc.thePlayer.setSprinting(false);
            }
            if (event.isPre()) {
                if (Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && !this.down) {
                    Scaffold.mc.thePlayer.setSneaking(false);
                    this.down = true;
                }
                if (Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && this.down) {
                    Scaffold.mc.thePlayer.setSneaking(false);
                    this.down = false;
                }
                Scaffold.blockCache = this.grab();
                if (Scaffold.blockCache != null) {
                    Scaffold.lastBlockCache = this.grab();
                }
                if (Scaffold.blockCache == null) {
                    return;
                }
            }
            else {
                if (Scaffold.blockCache == null) {
                    return;
                }
                if (Scaffold.tower) {
                    if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("Hypixel")) {
                        final int blockCount = this.getBlockCount();
                        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !Scaffold.mc.thePlayer.isPotionActive(Potion.jump) && blockCount != 0) {
                            Scaffold.mc.thePlayer.motionY = 0.4000000059604645;
                            Scaffold.mc.timer.timerSpeed = 1.0f;
                            MovementUtil.setSpeed(0.17);
                        }
                    }
                    if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("NCP")) {
                        final int blockCount = this.getBlockCount();
                        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !Scaffold.mc.thePlayer.isPotionActive(Potion.jump) && !MovementUtil.isMoving() && blockCount != 0) {
                            Scaffold.mc.thePlayer.motionX = 0.0;
                            Scaffold.mc.thePlayer.motionZ = 0.0;
                            Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                        }
                        if (this.towerTimer.hasTimeElapsed(1500.0, true)) {
                            Scaffold.mc.thePlayer.motionY = -0.2800000011920929;
                            this.towerTimer.reset();
                        }
                    }
                }
                final int currentSlot = Scaffold.mc.thePlayer.inventory.currentItem;
                this.slot = ScaffoldUtils.grabBlockSlot();
                final int time = 1;
                final int blockCount2 = this.getBlockCount();
                if (blockCount2 != 0) {
                    Scaffold.mc.thePlayer.inventory.currentItem = ScaffoldUtils.grabBlockSlot();
                    if (this.placeBlock(Scaffold.blockCache.position, Scaffold.blockCache.facing)) {
                        if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("Hypixel")) {
                            if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && Client.instance.setmgr.getSettingByName("Sprint").getValBoolean()) {
                                if (Client.instance.setmgr.getSettingByName("Timer Abuse").getValBoolean()) {
                                    Scaffold.mc.timer.timerSpeed = (float)MathUtils.getRandomInRange(0.8, 2.0);
                                }
                                else {
                                    Scaffold.mc.timer.timerSpeed = 1.28f;
                                }
                            }
                            else {
                                Scaffold.mc.timer.timerSpeed = 1.0f;
                            }
                        }
                        if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("NCP")) {
                            Scaffold.mc.timer.timerSpeed = 1.0f;
                        }
                        Scaffold.blockCache = null;
                    }
                    if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("NCP")) {
                        final float[] rot2 = RotationUtils.aimAtLocation(Scaffold.lastBlockCache.position, Scaffold.lastBlockCache.facing);
                        RenderUtils.setCustomPitch(rot2[1]);
                        Scaffold.mc.thePlayer.rotationYaw += 0.001f;
                        Scaffold.mc.thePlayer.rotationPitch += 0.001f;
                        ((EventMotion)event).setYaw(rot2[0]);
                        ((EventMotion)event).setPitch(rot2[1]);
                        Scaffold.yaw = rot2[0];
                        Scaffold.pitch = rot2[1];
                    }
                    if (Client.instance.setmgr.getSettingByName("Scaffold Rotation").getValString().equalsIgnoreCase("Hypixel")) {
                        Scaffold.mc.thePlayer.rotationYaw += 0.001f;
                        Scaffold.mc.thePlayer.rotationPitch += 0.001f;
                        ((EventMotion)event).setYaw(Scaffold.mc.thePlayer.rotationYaw + 180.0f);
                        ((EventMotion)event).setPitch(87.0f);
                        Scaffold.yaw = Scaffold.mc.thePlayer.rotationYaw + 180.0f;
                        Scaffold.pitch = 87.0f;
                    }
                }
            }
        }
        final boolean b = event instanceof EventRenderGUI;
    }
    
    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && this.canIItemBePlaced(item)) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }
    
    private boolean canIItemBePlaced(final Item item) {
        return Item.getIdFromItem(item) != 116 && Item.getIdFromItem(item) != 30 && Item.getIdFromItem(item) != 31 && Item.getIdFromItem(item) != 175 && Item.getIdFromItem(item) != 28 && Item.getIdFromItem(item) != 27 && Item.getIdFromItem(item) != 66 && Item.getIdFromItem(item) != 157 && Item.getIdFromItem(item) != 31 && Item.getIdFromItem(item) != 6 && Item.getIdFromItem(item) != 31 && Item.getIdFromItem(item) != 32 && Item.getIdFromItem(item) != 140 && Item.getIdFromItem(item) != 390 && Item.getIdFromItem(item) != 37 && Item.getIdFromItem(item) != 38 && Item.getIdFromItem(item) != 39 && Item.getIdFromItem(item) != 40 && Item.getIdFromItem(item) != 69 && Item.getIdFromItem(item) != 50 && Item.getIdFromItem(item) != 75 && Item.getIdFromItem(item) != 76 && Item.getIdFromItem(item) != 54 && Item.getIdFromItem(item) != 130 && Item.getIdFromItem(item) != 146 && Item.getIdFromItem(item) != 342 && Item.getIdFromItem(item) != 12 && Item.getIdFromItem(item) != 77 && Item.getIdFromItem(item) != 143;
    }
    
    private class BlockCache
    {
        private BlockPos position;
        private EnumFacing facing;
        
        private BlockCache(final BlockPos position, final EnumFacing facing, final BlockCache blockCache) {
            this.position = position;
            this.facing = facing;
        }
        
        private BlockPos getPosition() {
            return this.position;
        }
        
        private EnumFacing getFacing() {
            return this.facing;
        }
    }
}
