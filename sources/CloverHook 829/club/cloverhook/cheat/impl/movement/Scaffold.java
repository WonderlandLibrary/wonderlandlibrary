package club.cloverhook.cheat.impl.movement;

import club.cloverhook.cheat.impl.visual.KillSults;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.cheat.impl.combat.aura.Aura;
import club.cloverhook.event.minecraft.PlayerJumpEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.BlockUtils;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * Made by Niada/JonathanH/Spec ------------------------------- ethereal.rip @
 * 9:53PM - 3/19/2019 -------------------------------
 **/
public class Scaffold extends Cheat {

    //StringsProperty mode = new StringsProperty("Mode", "The fucking mode retard", null, false, true, new String[]{"Normal", "Hypixel"}, new Boolean[]{true, false});
    BooleanProperty tower = new BooleanProperty("Tower", "Quickly places blocks under you when you are holding your jump key.", null, true);
    BooleanProperty swing = new BooleanProperty("Swing", "Swing when you place a block.", null, false);
    //BooleanProperty forceitem        = new BooleanProperty("Force Item","Forces you to hold the scaffold item", null, true);
    //BooleanProperty reverse 		 = new BooleanProperty("Reverse Rotations","Allows for sprint scaffolding, fucks with some scaffold checks", null, true);
    BooleanProperty delayedplacement = new BooleanProperty("Delay", "Delay placing blocks", null, true);
    DoubleProperty prop_delay = new DoubleProperty("Place Delay", "Delay between blockplaces", () -> delayedplacement.getValue(), 60, 2, 200, 1, null);
    int heldItem, warnthisnibba, places;
    float serverSidedPitch, serverSidedYaw, placedelay;//Will use place delay when I make aacap/reflex mode, but for now its fine

    private Stopwatch towerStopwatch;

    public List<Block> getBlacklistedBlocks() {
        return blacklistedBlocks;
    }

    BlockData blockdata;
    private Stopwatch timer;
    public float yaw = 999, pitch = 999;
    private List<Block> blacklistedBlocks;
    private List<Block> invalidBlocks = Arrays.asList(
            Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
            Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
            Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock,
            Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
            Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table,
            Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2);
    private long lastPlace;

    public Scaffold() {
        super("Scaffold", "Places blocks under you as you walk.", CheatCategory.MOVEMENT);
        registerProperties(tower, swing, delayedplacement, prop_delay);
        towerStopwatch = new Stopwatch();
        timer = new Stopwatch();
        /*
         * https://gitlab.com/Arithmo/Sigma/blob/master/info/sigmaclient/module/impl/player/Scaffold.java
         *
         * */
        blacklistedBlocks = Arrays.asList(
                Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
                Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
                Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
                Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
                Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
                Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily,
                Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace,
                Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
    }

    //We do not need to override the cheat class ondisabele/onenable for scaffold, made that mistake too many times
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        mc.gameSettings.keyBindSneak.pressed = false;
        getPlayer().movementInput.sneak = false;
        getPlayer().inventory.currentItem = heldItem;
    }


    public void onEnable() {
        mc.timer.timerSpeed = 1.0f;
        placedelay = 95;
        if (mc.thePlayer != null) {
            serverSidedYaw = getPlayer().rotationYaw;
            serverSidedPitch = getPlayer().rotationPitch;

            heldItem = getPlayer().inventory.currentItem;
            warnthisnibba = 0;
            lastPlace = 420;
        }
    }

    @Collect
    public void onPlayerJump(PlayerJumpEvent playerJumpEvent) {
        if(Aura.targetIndex != -1)
            return;
        if (mc.thePlayer.isMoving()) {
            if (mc.thePlayer.isSprinting()) {
                float f = mc.thePlayer.rotationYaw * 0.017453292F;
                mc.thePlayer.motionX -= (double) (MathHelper.sin(f) * 0.2F) / 1.5;
                mc.thePlayer.motionZ += (double) (MathHelper.cos(f) * 0.2F) / 1.5;
            }
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent em) {
        if(Aura.targetIndex != -1)
            return;
        //Skidded sigma scaffold - oh stop fucking complaining, Jello, Remix, Astolfo, Adjust, and all the fuckers comming after us did same

        double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;

        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock().getMaterial().isReplaceable()) {
            x = mc.thePlayer.posX;
            z = mc.thePlayer.posZ;
        }

        BlockPos underPos = new BlockPos(x, mc.thePlayer.posY - 1, z);
        BlockData data = getBlockData(underPos);

        if (em.isPre()) {
            if (getBlockSlot() != -1 && mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isMoving() && tower.getValue()) {
                mc.thePlayer.setSpeed(0);
                if (mc.thePlayer.onGround) {
                    if (isOnGround(0.76) && !isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                        mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
                    }
                    if (isOnGround(0.0001)) {
                        mc.thePlayer.motionY = 0.42f;
                        if (timer.hasPassed(1500)) {
                            mc.thePlayer.motionY = -0.28;
                            timer.reset();
                        }
                    } else if (mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001) {
                        mc.thePlayer.motionY = 0;
                    }
                } else {
                    if (mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                        mc.thePlayer.motionY = 0.41955;
                    }
                }
            }
        }

        if (mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
            if (em.isPre()) {
                float[] rot = getRotations(data.position, data.face);
                em.setYaw(rot[0]);
                em.setPitch(rot[1]);
                yaw = rot[0];
                pitch = rot[1];
                if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                    em.setOnGround(false);
                }
            } else if (getBlockSlot() != -1) {
                int slot = mc.thePlayer.inventory.currentItem;
                if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                    em.setOnGround(false);
                }
                mc.thePlayer.inventory.currentItem = getBlockSlot();
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, getVec3(data.position, data.face));
                if (swing.getValue())
                    mc.thePlayer.swingItem();
                else
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());

                mc.thePlayer.inventory.currentItem = slot;

            }
        } else {
            em.setYaw(yaw);
            em.setPitch(pitch);
        }
    }

    public BlockPos getSideBlock(BlockPos currentPos) {
        //my own code
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

    public EnumFacing getSideHit(BlockPos currentPos, BlockPos sideBlock) {
        //my own code
        int xDiff = sideBlock.getX() - currentPos.getX();
        int yDiff = sideBlock.getY() - currentPos.getY();
        int zDiff = sideBlock.getZ() - currentPos.getZ();
        return yDiff != 0 ? EnumFacing.UP : xDiff <= -1 ? EnumFacing.EAST : xDiff >= 1 ? EnumFacing.WEST : zDiff <= -1 ? EnumFacing.SOUTH : zDiff >= 1 ? EnumFacing.NORTH : EnumFacing.DOWN;
    }

    public Vec3 getVectorForRotation(float pitch1, float yaw1) {

        //MCP + My own code
        float yaw = -yaw1 + 180;
        float pitch = pitch1;
        double x = MathHelper.cos((float) Math.toRadians(yaw + 90D));
        double y = -MathHelper.sin((float) Math.toRadians(pitch));
        double yDecrement = MathHelper.cos((float) Math.toRadians(pitch));
        double z = -MathHelper.cos((float) Math.toRadians(yaw));
        return new Vec3(x * yDecrement, y, z * yDecrement);
    }

    public float[] getRotationToBlock(BlockPos pos, EnumFacing facing) {
        /*
         * https://gitlab.com/Arithmo/Sigma/blob/master/info/sigmaclient/module/impl/player/Scaffold.java
         *  +
         *  my own code
         * */
        double xDiff = pos.getX() + 0.5 - getPlayer().posX + (double) facing.getDirectionVec().getX() / 2;
        double zDiff = pos.getZ() + 0.5 - getPlayer().posZ + (double) facing.getDirectionVec().getZ() / 2;
        double yDiff = pos.getY() - getPlayer().getEntityBoundingBox().minY - 1;
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) -Math.toDegrees(Math.atan2(xDiff, zDiff));
        float pitch = (float) -Math.toDegrees(Math.atan(yDiff / distance));

        return new float[]{Math.abs(yaw - mc.thePlayer.rotationYaw) < .1 ? mc.thePlayer.rotationYaw : yaw, Math.abs(pitch - mc.thePlayer.rotationPitch) < .1 ? mc.thePlayer.rotationPitch : pitch};
    }

    public MovingObjectPosition getMovingObjectPosition(BlockPos blockpos, double reach, float pitch, float yaw) {
        //MCP
        AxisAlignedBB bb = new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1);
        Vec3 vectorEyes = new Vec3(getPlayer().posX, getPlayer().posY + (double) getPlayer().getEyeHeight(), getPlayer().posZ);
        Vec3 vectorRotation = getVectorForRotation(pitch, yaw - 180);
        Vec3 vectorReach = vectorEyes.addVector(vectorRotation.xCoord * reach, vectorRotation.yCoord * reach, vectorRotation.zCoord * reach);
        return bb.calculateIntercept(vectorEyes, vectorReach);
    }

    public Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        //MCP
        float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292F);
        float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292F);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }

    IBlockState blockState(BlockPos pos) {
        //MCP
        return mc.theWorld.getBlockState(pos);
    }

    Block getBlock(BlockPos pos) {
        //MCP
        return blockState(pos).getBlock();
    }

    Material getMaterial(BlockPos pos) {
        //MCP
        return getBlock(pos).getMaterial();
    }

    int getBlockSlot() {
        /*
         * https://gitlab.com/Arithmo/Sigma/blob/master/info/sigmaclient/module/impl/player/Scaffold.java
         *
         * */
        for (int i = 36; i < 45; i++) {
            if (getPlayer().inventoryContainer.getSlot(i).getStack() != null
                    && getPlayer().inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock
                    && !invalidBlocks.contains(((ItemBlock) getPlayer().inventoryContainer.getSlot(i).getStack().getItem()).getBlock())) {
                return i - 36;
            }
        }
        return -1;//Would return 0 but that is used number inside inventory
    }

    private BlockData getBlockData(BlockPos pos) {

        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(0, -1, 0).add(1, 0, 0);
        BlockPos pos2 = pos.add(0, -1, 0).add(0, 0, 1);
        BlockPos pos3 = pos.add(0, -1, 0).add(-1, 0, 0);
        BlockPos pos4 = pos.add(0, -1, 0).add(0, 0, -1);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;

        /*
         * https://gitlab.com/Arithmo/Sigma/blob/master/info/sigmaclient/module/impl/player/Scaffold.java
         *
         * */
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc.thePlayer.posX + (double) face.getFrontOffsetX() / 2;
        double z = block.getZ() + 0.5 - mc.thePlayer.posZ + (double) face.getFrontOffsetZ() / 2;
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (block.getY() + 0.5);
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180 / Math.PI) - 90;
        float pitch = (float) (Math.atan2(d1, d3) * 180 / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }

    public Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += (double) face.getFrontOffsetX() / 2;
        z += (double) face.getFrontOffsetZ() / 2;
        y += (double) face.getFrontOffsetY() / 2;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += Mafs.getRandomInRange(.25, -.25);
            z += Mafs.getRandomInRange(.25, -.25);
        } else {
            y += Mafs.getRandomInRange(.25, -.25);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += Mafs.getRandomInRange(.25, -.25);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += Mafs.getRandomInRange(.25, -.25);
        }
        return new Vec3(x, y, z);
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    public boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    private boolean isValid(Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        } else {
            ItemBlock iBlock = (ItemBlock) item;
            Block block = iBlock.getBlock();
            if (blacklistedBlocks.contains(block)) {
                return false;
            }
        }
        return true;
    }

    private class BlockData {
        /*
         * https://gitlab.com/Arithmo/Sigma/blob/master/info/sigmaclient/module/impl/player/Scaffold.java
         *
         * */
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}