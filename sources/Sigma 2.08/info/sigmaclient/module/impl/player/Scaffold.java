package info.sigmaclient.module.impl.player;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.render.Colors;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {

    public static List<Block> getBlacklistedBlocks() {
        return blacklistedBlocks;
    }

    private static List<Block> blacklistedBlocks;
    private BlockData blockBelowData;
    private Timer timer = new Timer();
    private Timer towerTimer = new Timer();
    private String TOWER = "TOWER";
    private String MODE = "MODE";

    public Scaffold(ModuleData data) {
        super(data);
        settings.put(TOWER, new Setting<>(TOWER, true, "Helps you build up faster."));
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Normal", new String[] {"Normal", "Watchdog"}), "Scaffold method."));
        blacklistedBlocks = Arrays.asList(
                Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
                Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
                Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
                Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
                Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
                Blocks.stone_button, Blocks.wooden_button, Blocks.lever);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        //Resets timer so player can tower up instantly if needed.
        towerTimer.reset();
    }

    public void onDisable() {
        if(mc.thePlayer.isSwingInProgress) {
            mc.thePlayer.swingProgress = 0;
            mc.thePlayer.swingProgressInt = 0;
            mc.thePlayer.isSwingInProgress = false;
        }
    }

    int slot;

    @Override
    @RegisterEvent(events = {EventPacket.class, EventUpdate.class, EventRenderGui.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderGui) {
            //Renders block count. TODO: Make text clearer.
            ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int color = Colors.getColor(255, 0, 0);
            if (getBlockCount() >= 64 && 128 > getBlockCount()) {
                color = Colors.getColor(255, 255, 0);
            } else if (getBlockCount() >= 128) {
                color = Colors.getColor(0, 255, 0);
            }
            Client.fm.getFont("SFB 7").drawStringWithShadow(getBlockCount() + "", res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(getBlockCount() + "") / 2, res.getScaledHeight() / 2 - 25, color);
        }
        if (event instanceof EventUpdate) {
            String currentMode = ((Options)settings.get(MODE).getValue()).getSelected();
            setSuffix(currentMode);
            EventUpdate em = (EventUpdate) event;
            if(currentMode.equalsIgnoreCase("Watchdog")) {
                if (em.isPre()) {
                    int tempSlot = getBlockSlot();
                    blockBelowData = null;
                    slot = -1;
                    if (!mc.thePlayer.isSneaking() && tempSlot != -1) {
                        double x = mc.thePlayer.posX, y = mc.thePlayer.posY - 1, z = mc.thePlayer.posZ;
                        BlockPos blockBelow1 = new BlockPos(x, y, z);
                        blockBelowData = getBlockData(blockBelow1);
                        slot = tempSlot;
                        if (blockBelowData != null) {
                            float[] values = getRotationsBlock(blockBelowData.position, blockBelowData.face);
                            em.setYaw(values[0]);
                            em.setPitch(values[1]);
                        }
                    }
                } else if (em.isPost()) {
                    if (blockBelowData == null) {
                        towerTimer.reset();
                        return;
                    }
                    if (slot == -1) return;
                    if (!mc.thePlayer.worldObj.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX, -1, mc.thePlayer.motionZ)).isEmpty())
                        return;
                    boolean dohax = mc.thePlayer.inventory.currentItem != slot;
                    if (dohax) mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventoryContainer.getSlot(36 + slot).getStack(), blockBelowData.position, blockBelowData.face, new Vec3(0, 0, 0))) {
                        mc.thePlayer.swingItem();
                    }
                    if (dohax)
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    if (mc.gameSettings.keyBindJump.getIsKeyPressed() && (Boolean) settings.get(TOWER).getValue()) {
                        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                        mc.thePlayer.motionY = 0.42;
                        if (towerTimer.delay(1500)) {
                            mc.thePlayer.motionY = -0.28;
                            towerTimer.reset();
                        }
                    } else {
                        towerTimer.reset();
                    }
                }
            } else {
                if (em.isPre()) {
                    setSuffix(Integer.toString(getBlockCount()));
                    blockBelowData = null;

                    BlockPos blockBelow;

                    //Get block based off of movement

                    double x = mc.thePlayer.posX;
                    double y = mc.thePlayer.posY - 1.0;
                    double z = mc.thePlayer.posZ;
                    double forward = mc.thePlayer.movementInput.moveForward;
                    double strafe = mc.thePlayer.movementInput.moveStrafe;
                    float yaw = mc.thePlayer.rotationYaw;
                    x += (forward * 0.45 * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(yaw + 90.0f)));
                    z += (forward * 0.45 * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(yaw + 90.0f)));

                    //Checks if the block below is a valid block + timer delay

                    if (!mc.thePlayer.isSneaking() && (mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.air ||
                            mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.snow_layer ||
                            mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.tallgrass) && timer.delay(100)) {
                        timer.reset();
                        //Grab the block data for the block below
                        blockBelowData = getBlockData(blockBelow);
                        if (blockBelowData != null) {
                            //Face in the center of the block
                            float[] rotations = getRotationsBlock(blockBelowData.position, blockBelowData.face);
                            em.setYaw(rotations[0]);
                            em.setPitch(rotations[1]);
                        }
                    }

                } else if (em.isPost()) {

                    if (!mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                        towerTimer.reset();
                    }

                    //If the player can tower
                    if (blockBelowData != null && (Boolean) settings.get(TOWER).getValue() && getBlockCount() > 0 && mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                        //Reduces player's motion and moves the player upwards.
                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionZ = 0;
                        mc.thePlayer.motionY = 0.42;

                        //After about a second and a half, NCP will want to flag you. This will prevent that.
                        if (towerTimer.delay(1500)) {
                            mc.thePlayer.motionY = -0.28;
                            towerTimer.reset();
                        }
                    }

                    //Loop through the items and find the block to place.
                    for (int i = 36; i < 45; i++) {
                        if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            Item item = is.getItem();
                            if (item instanceof ItemBlock && !blacklistedBlocks.contains(((ItemBlock) item).getBlock()) && !((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest") && blockBelowData != null) {
                                mc.rightClickDelayTimer = 2;
                                int currentItem = mc.thePlayer.inventory.currentItem;

                                //Swap to block.
                                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
                                mc.thePlayer.inventory.currentItem = i - 36;
                                mc.playerController.updateController();

                                //Caused a null pointer for some reason, will look into soon.
                                try {
                                    if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockBelowData.position, blockBelowData.face, new Vec3(blockBelowData.position.getX(), blockBelowData.position.getY(), blockBelowData.position.getZ()))) {
                                        mc.thePlayer.swingItem();
                                    }
                                } catch (Exception ignored) {

                                }

                                //Reset to current hand.
                                mc.thePlayer.inventory.currentItem = currentItem;
                                mc.playerController.updateController();
                                return;
                            }
                        }
                    }
                }
            }
            if (invCheck()) {
                for (int i = 9; i < 36; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                        if (item instanceof ItemBlock && !blacklistedBlocks.contains(((ItemBlock) item).getBlock()) && !((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest")) {
                            swap(i, 7);
                            break;
                        }
                    }
                }
            }
        }
    }



    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private boolean invCheck() {
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemBlock && !blacklistedBlocks.contains(((ItemBlock) item).getBlock())) {
                    return false;
                }
            }
        }
        return true;
    }

    private BlockData getBlockData(BlockPos pos) {
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }

    public static float[] getRotationsBlock(BlockPos pos, EnumFacing facing) {
        double d0 = pos.getX() - mc.thePlayer.posX;
        double d1 = pos.getY() - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double d2 = pos.getZ() - mc.thePlayer.posZ;
        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
        return new float[]{f, f1};
    }

    private int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                if (blacklistedBlocks.stream().anyMatch(e -> e.equals(((ItemBlock) itemStack.getItem()).getBlock()))) {
                    continue;
                }
                return i - 36;
            }
        }
        return -1;
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !blacklistedBlocks.contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private class BlockData {

        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }

    }

}
