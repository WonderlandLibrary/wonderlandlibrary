package summer.cheat.cheats.player;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MoveUtils;
import summer.base.utilities.ScaffoldUtils;
import summer.base.utilities.TimerUtils;
import summer.cheat.cheats.movement.Speed;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.cheat.guiutil.Setting;

public class Scaffold extends Cheats {
    public static Setting swing;
  //  public static Setting downWards;
    public static Setting tower;
    public static Setting extend;
    int lastItem = -1;
    boolean down = false;
    TimerUtils downwardsTimer, towerTimer, slowDownTimer, timer;
    public static BlockCache blockCache, lastBlockCache;
    static Minecraft mc = Minecraft.getMinecraft();
    private ArrayList<Packet> packets = new ArrayList<Packet>();
    private double yPos;

    public Scaffold() {
        super("Scaffold", "Places blocks under the player", Selection.PLAYER, false);

        Summer.INSTANCE.settingsManager.Property(swing = new Setting("Swing", this, false));
      //  Summer.INSTANCE.settingsManager.Property(downWards = new Setting("Down", this, true));
        Summer.INSTANCE.settingsManager.Property(tower = new Setting("Tower", this, true));
//        Summer.instance.sm.Property(extend = new Setting("Extend", this, 0.1, 0.1, 6, false));

        this.downwardsTimer = new TimerUtils();
        this.towerTimer = new TimerUtils();
        this.slowDownTimer = new TimerUtils();
        this.timer = new TimerUtils();
    }

    @Override
    public void onEnable() {

        super.onEnable();
        this.downwardsTimer.reset();
        this.towerTimer.reset();
        this.slowDownTimer.reset();
        this.timer.reset();
        down = false;
        this.packets.clear();
    }

    @Override
    public void onDisable() {

        super.onDisable();
        Timer.timerSpeed = 1f;
        this.downwardsTimer.reset();
        this.towerTimer.reset();
        this.slowDownTimer.reset();
        this.timer.reset();
        blockCache = null;
        lastBlockCache = null;
        down = false;

    }

    private boolean placeBlock(final BlockPos pos, final EnumFacing facing) {
        final Vec3 eyesPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight(),
                Minecraft.thePlayer.posZ);

        if (mc.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), pos, facing,
                new Vec3(blockCache.position).addVector(0.5, 0.5, 0.5)
                        .add(new Vec3(blockCache.facing.getDirectionVec()).scale(0.5)))) {
            if (swing.getValBoolean()) {
                Minecraft.thePlayer.swingItem();
            } else {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            return true;
        }
        return false;
    }

    private BlockCache grab() {
        final EnumFacing[] invert = {EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
                EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(0, 0, 0);
        if (Minecraft.thePlayer.isMoving() && !mc.gameSettings.keyBindJump.getIsKeyPressed()) {
            for (double n2 = 0.08D, n3 = 0.0; n3 <= n2; n3 += n2 / (Math.floor(n2) + 1.0)) {
                final BlockPos blockPos2 = new BlockPos(
                        Minecraft.thePlayer.posX - MathHelper.sin(ScaffoldUtils.clampRotation()) * n3,
                        Minecraft.thePlayer.posY - 1.0,
                        Minecraft.thePlayer.posZ + MathHelper.cos(ScaffoldUtils.clampRotation()) * n3);
                final IBlockState blockState = Minecraft.theWorld.getBlockState(blockPos2);
                if (blockState != null && blockState.getBlock() == Blocks.air) {
                    position = blockPos2;
                    break;
                }
            }
            // position = new BlockPos(new
            // BlockPos(this.mc.thePlayer.getPositionVector().xCoord,
            // this.mc.thePlayer.getPositionVector().yCoord,
            // this.mc.thePlayer.getPositionVector().zCoord)).offset(EnumFacing.DOWN);
        } else {
            position = new BlockPos(new BlockPos(Minecraft.thePlayer.getPositionVector().xCoord,
                    Minecraft.thePlayer.getPositionVector().yCoord, Minecraft.thePlayer.getPositionVector().zCoord))
                    .offset(EnumFacing.DOWN);
        }

        if (!(Minecraft.theWorld.getBlockState(position).getBlock() instanceof BlockAir)
                && !(Minecraft.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
            return null;
        }
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing facing = values[i];
            final BlockPos offset = position.offset(facing);
            final IBlockState blockState = Minecraft.theWorld.getBlockState(offset);
            if (!(Minecraft.theWorld.getBlockState(offset).getBlock() instanceof BlockAir)
                    && !(Minecraft.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
                return new BlockCache(offset, invert[facing.ordinal()], (BlockCache) null);
            }
        }
        final BlockPos[] offsets = {new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
                new BlockPos(0, 0, 1)};
        BlockPos[] array;
        for (int length2 = (array = offsets).length, j = 0; j < length2; ++j) {
            final BlockPos offset2 = array[j];
            final BlockPos offsetPos = position.add(offset2.getX(), 0, offset2.getZ());
            final IBlockState blockState2 = Minecraft.theWorld.getBlockState(offsetPos);
            if (Minecraft.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                EnumFacing[] values2;
                for (int length3 = (values2 = EnumFacing.values()).length, k = 0; k < length3; ++k) {
                    final EnumFacing facing2 = values2[k];
                    final BlockPos offset3 = offsetPos.offset(facing2);
                    final IBlockState blockState3 = Minecraft.theWorld.getBlockState(offset3);
                    if (!(Minecraft.theWorld.getBlockState(offset3).getBlock() instanceof BlockAir)) {
                        return new BlockCache(offset3, invert[facing2.ordinal()], (BlockCache) null);
                    }
                }
            }

        }
        return null;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        event.setPitch(90.0f);

        if (lastBlockCache != null) {
            float[] rotations = ScaffoldUtils.getRotations(lastBlockCache.position, lastBlockCache.facing);
            event.setYaw(rotations[0]);
        }

        if (event.isPre()) {
            if (mc.gameSettings.keyBindSneak.isKeyDown() && !down) {
                Minecraft.thePlayer.setSneaking(false);
                mc.gameSettings.keyBindSneak.pressed = false;
                down = true;
            }

            if (mc.gameSettings.keyBindSneak.isKeyDown() && down) {
                Minecraft.thePlayer.setSneaking(false);
                mc.gameSettings.keyBindSneak.pressed = false;
                down = false;
            }

            if (ScaffoldUtils.grabBlockSlot() == -1) {
                return;
            }
            blockCache = this.grab();
            if (blockCache != null) {
                lastBlockCache = this.grab();
            }
            if (blockCache == null) {
                return;
            }
        } else {
            if (blockCache == null)
                return;

            if (mc.gameSettings.keyBindJump.isKeyDown() && !Minecraft.thePlayer.isMoving()
                    && tower.getValBoolean()) {
                if (MoveUtils.getJumpEffect() == 0) {
                    Minecraft.thePlayer.motionY = 0;
                }

                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
                Minecraft.thePlayer.isJumping = false;
                Minecraft.thePlayer.setJumping(false);

                if (towerTimer.hasReached(100)) {
                    if (MoveUtils.getJumpEffect() == 0) {
                        Minecraft.thePlayer.jump();

                        if (slowDownTimer.delay(1500)) {
                            Minecraft.thePlayer.motionY = -0.28;
                            slowDownTimer.reset();
                        }
                    }
                    towerTimer.reset();
                }
            } else {
                towerTimer.reset();
            }

            final int currentSlot = Minecraft.thePlayer.inventory.currentItem;
            final int slot = ScaffoldUtils.grabBlockSlot();
            int time = 30;
            if (CheatManager.getInstance(Speed.class).isToggled())
                time = 10;

            if (MoveUtils.getSpeedEffect() > 0) {
                time = time / (MoveUtils.getSpeedEffect() * 8);
            }

            if (timer.hasReached(time)) {
                Minecraft.thePlayer.inventory.currentItem = slot;
                if (this.placeBlock(blockCache.position, blockCache.facing)) {
                    boolean exists = false;
                    for (int i = 0; i < 9; i++) {
                        if (Minecraft.thePlayer.inventory.mainInventory[i] == Minecraft.thePlayer.inventory.mainInventory[currentSlot]) {
                            exists = true;

                        }
                    }
                    if (exists) {
                        Minecraft.thePlayer.inventory.currentItem = currentSlot;
                        mc.playerController.updateController();
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
                    }

                    blockCache = null;
                }
                timer.reset();
            }
        }
    }

    private class BlockCache {
        private BlockPos position;
        private EnumFacing facing;

        private BlockCache(final BlockPos position, final EnumFacing facing, BlockCache blockCache) {
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

    @EventTarget
    public void onRender(EventRender2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        int blockCount = getBlockCount();
        Color color = new Color(0, 255, 0);
        if (this.getBlockCount() > 128) {
            color = new Color(0, 255, 0);
        }
        if (this.getBlockCount() < 128) {
            color = new Color(255, 255, 0);
        }
        if (this.getBlockCount() < 64) {
            color = new Color(255, 0, 0);
        }
        Minecraft.fontRendererObj.drawStringWithShadow(blockCount + "", (sr.getScaledWidth() / 2 - -10) - Minecraft.fontRendererObj.getStringWidth(blockCount + "") / 2, (sr.getScaledHeight() / 2 + 30) + -21, color.getRGB());
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && canIItemBePlaced(item)) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private boolean canIItemBePlaced(Item item) {
        if (Item.getIdFromItem(item) == 116)
            return false;
        if (Item.getIdFromItem(item) == 30)
            return false;
        if (Item.getIdFromItem(item) == 31)
            return false;
        if (Item.getIdFromItem(item) == 175)
            return false;
        if (Item.getIdFromItem(item) == 28)
            return false;
        if (Item.getIdFromItem(item) == 27)
            return false;
        if (Item.getIdFromItem(item) == 66)
            return false;
        if (Item.getIdFromItem(item) == 157)
            return false;
        if (Item.getIdFromItem(item) == 31)
            return false;
        if (Item.getIdFromItem(item) == 6)
            return false;
        if (Item.getIdFromItem(item) == 31)
            return false;
        if (Item.getIdFromItem(item) == 32)
            return false;
        if (Item.getIdFromItem(item) == 140)
            return false;
        if (Item.getIdFromItem(item) == 390)
            return false;
        if (Item.getIdFromItem(item) == 37)
            return false;
        if (Item.getIdFromItem(item) == 38)
            return false;
        if (Item.getIdFromItem(item) == 39)
            return false;
        if (Item.getIdFromItem(item) == 40)
            return false;
        if (Item.getIdFromItem(item) == 69)
            return false;
        if (Item.getIdFromItem(item) == 50)
            return false;
        if (Item.getIdFromItem(item) == 75)
            return false;
        if (Item.getIdFromItem(item) == 76)
            return false;
        if (Item.getIdFromItem(item) == 54)
            return false;
        if (Item.getIdFromItem(item) == 130)
            return false;
        if (Item.getIdFromItem(item) == 146)
            return false;
        if (Item.getIdFromItem(item) == 342)
            return false;
        if (Item.getIdFromItem(item) == 12)
            return false;
        if (Item.getIdFromItem(item) == 77)
            return false;
        if (Item.getIdFromItem(item) == 143)
            return false;
        return true;
    }

}
