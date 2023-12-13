package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import today.flux.event.PostUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.World.scaffold.*;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ModeValue;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    public static ModeValue blockCount = new ModeValue("Scaffold", "Block Counter", "New", "Simple", "New");
    public static BooleanValue safewalk = new BooleanValue("Scaffold", "SafeWalk", true);
    public static BooleanValue esp = new BooleanValue("Scaffold", "ESP", false);
    public static BooleanValue tower = new BooleanValue("Scaffold", "Tower", true);
    public static BooleanValue swing = new BooleanValue("Scaffold", "Swing", false);

    public static float curYaw = 0;

    public static List<Block> blacklist;

    public Scaffold() {
        super("Scaffold", Category.World, true, new Normal(), new AAC(), new Hypixel());
        blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.tnt, Blocks.sand, Blocks.enchanting_table, Blocks.beacon, Blocks.noteblock, Blocks.sand, Blocks.chest, Blocks.gravel, Blocks.ender_chest);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onPost(PostUpdateEvent e) {
        curYaw = e.yaw;
    }

    @EventTarget
    public void onRender(WorldRenderEvent event) {
        //render place block
        if (esp.getValueState()) {
            BlockPos pos = new BlockPos(mc.thePlayer).down();
            if (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) {
                pos = pos.north();
            } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) {
                pos = pos.south();
            } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) {
                pos = pos.west();
            } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST) {
                pos = pos.east();
            } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.UP) {
                pos = pos.up();
            } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.DOWN) {
                pos = pos.down();
            }
            double x = pos.getX() - mc.getRenderManager().renderPosX;
            double y = pos.getY() - mc.getRenderManager().renderPosY;
            double z = pos.getZ() - mc.getRenderManager().renderPosZ;
            if (mc.theWorld.getBlockState(pos).getBlock().isReplaceable(mc.theWorld, pos)) {
                RenderUtil.drawEntityESP(x, y, z, x + 1, y + 1, z + 1, 1, 1, 1, 0.2F);
            }
        }
    }

    //Auto
    @EventTarget
    public void onRespawn(RespawnEvent e) {
        this.disable();
        NotificationManager.show("Module", this.getName() + " Disabled (Auto)", Notification.Type.INFO);
    }

    public static boolean isScaffoldBlock(ItemStack itemStack) {
        if (itemStack == null)
            return false;

        if (itemStack.stackSize <= 0)
            return false;

        if (!(itemStack.getItem() instanceof ItemBlock))
            return false;

        ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

        // whitelist
        if (itemBlock.getBlock() == Blocks.glass)
            return true;

        // only fullblock
        if (!itemBlock.getBlock().isFullBlock())
            return false;

        return true;
    }

    public int getBlocksCount() {
        int result = 0;
        int i = 9;
        while (i < 45) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isScaffoldBlock(stack)) {
                result += stack.stackSize;
            }
            ++i;
        }
        return result;
    }


    // Minecraft Skid
    static IBlockState blockState(BlockPos pos) {
        //MCP
        return mc.theWorld.getBlockState(pos);
    }

    static Block getBlock(BlockPos pos) {
        //MCP
        return blockState(pos).getBlock();
    }

    //Recieve SideBlock Data
    public static BlockPos getSideBlock(BlockPos currentPos) {
        BlockPos pos = currentPos;
        if (getBlock(currentPos.add(0, -1, 0)) != Blocks.air && !(getBlock(currentPos.add(0, -1, 0)) instanceof BlockLiquid))
            return currentPos.add(0, -1, 0);

        double dist = 20;
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos newPos = currentPos.add(x, 0, z);
                    double newDist = MathHelper.sqrt_double(x * x + y * y + z * z);
                    if (getBlock(newPos) != Blocks.air && !(getBlock(newPos) instanceof BlockLiquid)
                            && getBlock(newPos).getMaterial().isSolid() && newDist <= dist) {
                        pos = currentPos.add(x, y, z);
                        dist = newDist;
                    }
                }
            }
        }
        return pos;
    }

    // get Side Hit Data
    public static EnumFacing getSideHit(BlockPos currentPos, BlockPos sideBlock) {
        int xDiff = sideBlock.getX() - currentPos.getX();
        int yDiff = sideBlock.getY() - currentPos.getY();
        int zDiff = sideBlock.getZ() - currentPos.getZ();
        return yDiff != 0 ? EnumFacing.UP : xDiff <= -1 ? EnumFacing.EAST : xDiff >= 1 ? EnumFacing.WEST : zDiff <= -1 ? EnumFacing.SOUTH : zDiff >= 1 ? EnumFacing.NORTH : EnumFacing.DOWN;
    }
}
