package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.time.StopWatch;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.CollideEvent;
import wtf.diablo.events.impl.MoveEvent;
import wtf.diablo.events.impl.OverlayEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.math.MathUtil;
import wtf.diablo.utils.math.Stopwatch;
import wtf.diablo.utils.packet.PacketUtil;
import wtf.diablo.utils.player.MoveUtil;
import wtf.diablo.utils.world.EntityUtil;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Scaffold extends Module {
    public BooleanSetting sprint = new BooleanSetting("Sprint", true);
    public BooleanSetting jumpset = new BooleanSetting("Jump", false);
    public BooleanSetting towerset = new BooleanSetting("Tower", false);

    public BooleanSetting collide = new BooleanSetting("Collide", false);

    public NumberSetting timer = new NumberSetting("Timer", 1, 0.1, 1, 2);

    private final List<Block> invalidBlocks;

    private Stopwatch ttimer = new Stopwatch();
    private Stopwatch tttimer = new Stopwatch();

    private boolean check1;
    private int lastSlot;

    double keepY;

    public Scaffold() {
        super("Scaffold", "Place blocks at your feet", Category.PLAYER, ServerType.All);
        this.addSettings(sprint, timer, jumpset, towerset, collide);
        this.invalidBlocks = Arrays.asList(Blocks.redstone_wire, Blocks.tallgrass, Blocks.redstone_torch, Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web, Blocks.gravel, Blocks.tnt);
    }

    @Override
    public void onEnable() {
        lastSlot = mc.thePlayer.inventory.currentItem;
        keepY = mc.thePlayer.posY - 1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.inventory.currentItem = lastSlot;
        super.onDisable();
        check1 = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if (mc.thePlayer.onGround && jumpset.getValue() && MoveUtil.isMoving()) {
            mc.thePlayer.motionY = 0.32;
        }

        mc.timer.timerSpeed = timer.getFloatValue();
        WorldClient world = mc.theWorld;
        EntityPlayerSP player = mc.thePlayer;
        BlockData data = getBlockData(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (mc.gameSettings.keyBindSneak.pressed ? 2 : 1), mc.thePlayer.posZ));
        Vec3 hitVec = this.getVec3(data);

        float[] rotations = getBlockRotations(data.pos, data.face);

        int slot = -1;
        int blockCount = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = player.inventory.getStackInSlot(i);
            if (itemStack != null) {
                final int stackSize = itemStack.stackSize;
                if (this.isValidItem(itemStack.getItem()) && stackSize > blockCount) {
                    blockCount = stackSize;
                    slot = i;
                }
            }
        }

        EntityUtil.setRotations(e, (float) MathUtil.round(rotations[0], 39), rotations[1]);
        mc.thePlayer.setSprinting(sprint.getValue());
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            MoveUtil.setMotion(0.095f);
        }


        if (slot != -1) {
            if (e.getType() == EventType.Post) {
                player.inventory.currentItem = slot;
                if (mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), data.pos, data.face, hitVec)) {
                    player.swingItem();
                }
            }

            if (MoveUtil.isMoving() && this.sprint.getValue()) {
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }

    @Subscribe
    public void onHud(OverlayEvent e) {
        ScaledResolution scaledResolution = e.getScaledResolution();

        int blockCount = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                final int stackSize = itemStack.stackSize;
                if (this.isValidItem(itemStack.getItem())) {
                    blockCount += stackSize;
                }
            }
        }

        mc.fontRendererObj.drawStringWithShadow("Blocks: " + String.valueOf(blockCount), scaledResolution.getScaledWidth() / 2 + 4, scaledResolution.getScaledHeight() / 2 + 14, -1);
    }

    private float[] getBlockRotations(BlockPos blockPos, EnumFacing enumFacing) {
        if (blockPos == null && enumFacing == null) {
            return null;
        } else {
            Vec3 positionEyes = mc.thePlayer.getPositionEyes(2.0F);
            Vec3 add = (new Vec3((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D));
            double n = add.xCoord - positionEyes.xCoord;
            double n2 = add.yCoord - positionEyes.yCoord;
            double n3 = add.zCoord - positionEyes.zCoord;
            return new float[]{(float) (Math.atan2(n3, n) * 180.0D / 3.141592653589793D - 90.0D), -((float) (Math.atan2(n2, (float) Math.hypot(n, n3)) * 180.0D / 3.141592653589793D))};
        }
    }

    @Subscribe
    public void onCollide(CollideEvent e) {
        final double x = e.getX();
        final double y = e.getY();
        final double z = e.getZ();

        if (collide.getValue()) {
            if (!mc.gameSettings.keyBindJump.pressed) {
                e.setBoundingBox(AxisAlignedBB.fromBounds(15.0, 1.0, 15.0, -15.0, -1.0, -15.0).offset(x, y, z));
            }
        }
    }

    @Subscribe
    public void onMove(MoveEvent e) {
        /*
        if (towerset.getValue() && !MoveUtil.isMoving() && mc.gameSettings.keyBindJump.pressed) {
            e.setX(0);
            e.setZ(0);
            e.setY(0.42F);
        }

         */
    }

    private Vec3 getVec3(final BlockData data) {
        final BlockPos pos = data.pos;
        final EnumFacing face = data.face;
        double x = pos.getX() + 0.5f;
        double y = pos.getY() + 0.5f;
        double z = pos.getZ() + 0.5f;
        /*
        if(!mc.getCurrentServerData().serverName.contains("hypixel.net")){

        }

         */
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += this.randomNumber(0.3, -0.3);
            z += this.randomNumber(0.3, -0.3);
        } else {
            y += this.randomNumber(0.49, 0.5);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += this.randomNumber(0.3, -0.3);
        }

        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += this.randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    private BlockData getBlockData(final BlockPos pos) {
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos5.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos5.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos5.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos5.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos5.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos6.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos6.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos6.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos6.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos6.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState((pos7.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos7.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos7.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos7.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos7.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos8.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos8.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos8.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos8.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos8.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos9.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos9.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos9.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos9.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(mc.theWorld.getBlockState(pos9.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }

    private boolean isValidItem(final Item item) {
        if (item instanceof ItemBlock) {
            final ItemBlock iBlock = (ItemBlock) item;
            final Block block = iBlock.getBlock();
            return !this.invalidBlocks.contains(block);
        }
        return false;
    }

    private static class BlockData {
        public final BlockPos pos;
        public final EnumFacing face;

        private BlockData(final BlockPos pos, final EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }
}
