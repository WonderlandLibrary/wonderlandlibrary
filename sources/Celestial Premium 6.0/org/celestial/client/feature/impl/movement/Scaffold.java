/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventSafeWalk;
import org.celestial.client.event.events.impl.motion.EventStrafe;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Scaffold
extends Feature {
    public static List<Block> invalidBlocks = Arrays.asList(Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA, Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_SLAB, Blocks.WOODEN_SLAB, Blocks.STONE_SLAB2, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.CACTUS, Blocks.LADDER, Blocks.WEB, Blocks.PUMPKIN);
    public static BlockData data;
    public static boolean isSneaking;
    public static BooleanSetting down;
    public static BooleanSetting cast;
    public static BooleanSetting sprintoff;
    private final TimerHelper time = new TimerHelper();
    private final BooleanSetting jump;
    private final BooleanSetting swing;
    private final NumberSetting delay;
    private final NumberSetting delayRandom;
    private final NumberSetting chance;
    private final NumberSetting speed;
    private final NumberSetting sneakOffset;
    private final NumberSetting placeOffset;
    private final NumberSetting motionOffset;
    private final BooleanSetting rotStrafe;
    private final BooleanSetting jartex;
    private final BooleanSetting autoMotionStop;
    private final BooleanSetting blocksafe;
    private final ListSetting blockRotation = new ListSetting("Rotation Mode", "Matrix", () -> true, "Matrix", "None");
    private final ListSetting sneakMode = new ListSetting("Sneak Mode", "Silent", () -> true, "Silent", "Client", "None");
    private final ListSetting towerMode = new ListSetting("Tower Mode", "Matrix", () -> true, "Matrix", "NCP", "Default");
    private boolean switchTimer;
    private int slot;

    public Scaffold() {
        super("Scaffold", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0441\u0442\u0430\u0432\u0438\u0442 \u043f\u043e\u0434 \u0432\u0430\u0441 \u0431\u043b\u043e\u043a\u0438", Type.Movement);
        this.chance = new NumberSetting("PlaceChance", 100.0f, 0.0f, 100.0f, 1.0f, () -> true);
        this.delay = new NumberSetting("PlaceDelay", 0.0f, 0.0f, 300.0f, 1.0f, () -> true);
        this.delayRandom = new NumberSetting("PlaceDelayRandom", 0.0f, 0.0f, 1000.0f, 1.0f, () -> true);
        this.speed = new NumberSetting("Scaffold Speed", 0.6f, 0.05f, 1.2f, 0.01f, () -> true);
        this.sneakOffset = new NumberSetting("Sneak Offset", 0.25f, -0.3f, 0.3f, 0.01f, () -> !this.sneakMode.currentMode.equals("None"));
        this.placeOffset = new NumberSetting("Place Offset", 0.15f, 0.01f, 0.3f, 0.01f, () -> true);
        this.motionOffset = new NumberSetting("MotionOffset", 0.17f, 0.0f, 1.0f, 0.01f, () -> true);
        this.autoMotionStop = new BooleanSetting("AutoMotionStop", true, () -> true);
        sprintoff = new BooleanSetting("SprintOFF", true, () -> true);
        this.blocksafe = new BooleanSetting("BlockSafe", true, () -> true);
        this.jump = new BooleanSetting("Jump", false, () -> true);
        down = new BooleanSetting("DownWard", false, () -> true);
        this.swing = new BooleanSetting("SwingHand", false, () -> true);
        this.rotStrafe = new BooleanSetting("Rotation Strafe", false, () -> true);
        this.jartex = new BooleanSetting("Jartex", false, () -> true);
        this.addSettings(this.blockRotation, this.sneakMode, this.towerMode, this.chance, this.delay, this.delayRandom, this.speed, this.placeOffset, this.sneakOffset, this.motionOffset, this.autoMotionStop, sprintoff, this.blocksafe, this.jump, down, this.swing, this.rotStrafe, this.jartex);
    }

    public static int searchBlock() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = Scaffold.mc.player.inventoryContainer.getSlot(i).getStack();
            if (!(itemStack.getItem() instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    private boolean canPlace() {
        BlockPos bp1 = new BlockPos(Scaffold.mc.player.posX - (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posZ - (double)this.placeOffset.getCurrentValue());
        BlockPos bp2 = new BlockPos(Scaffold.mc.player.posX - (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posZ + (double)this.placeOffset.getCurrentValue());
        BlockPos bp3 = new BlockPos(Scaffold.mc.player.posX + (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posZ + (double)this.placeOffset.getCurrentValue());
        BlockPos bp4 = new BlockPos(Scaffold.mc.player.posX + (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.placeOffset.getCurrentValue(), Scaffold.mc.player.posZ - (double)this.placeOffset.getCurrentValue());
        return Scaffold.mc.player.world.getBlockState(bp1).getBlock() == Blocks.AIR && Scaffold.mc.player.world.getBlockState(bp2).getBlock() == Blocks.AIR && Scaffold.mc.player.world.getBlockState(bp3).getBlock() == Blocks.AIR && Scaffold.mc.player.world.getBlockState(bp4).getBlock() == Blocks.AIR;
    }

    private boolean canSneak() {
        BlockPos bp1 = new BlockPos(Scaffold.mc.player.posX - (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posZ - (double)this.sneakOffset.getCurrentValue());
        BlockPos bp2 = new BlockPos(Scaffold.mc.player.posX - (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posZ + (double)this.sneakOffset.getCurrentValue());
        BlockPos bp3 = new BlockPos(Scaffold.mc.player.posX + (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posZ + (double)this.sneakOffset.getCurrentValue());
        BlockPos bp4 = new BlockPos(Scaffold.mc.player.posX + (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posY - (double)this.sneakOffset.getCurrentValue(), Scaffold.mc.player.posZ - (double)this.sneakOffset.getCurrentValue());
        return Scaffold.mc.player.world.getBlockState(bp1).getBlock() == Blocks.AIR && Scaffold.mc.player.world.getBlockState(bp2).getBlock() == Blocks.AIR && Scaffold.mc.player.world.getBlockState(bp3).getBlock() == Blocks.AIR && Scaffold.mc.player.world.getBlockState(bp4).getBlock() == Blocks.AIR;
    }

    @Override
    public void onEnable() {
        this.slot = Scaffold.mc.player.inventory.currentItem;
        if (this.autoMotionStop.getCurrentValue()) {
            Scaffold.mc.player.motionX *= -1.15;
            Scaffold.mc.player.motionZ *= -1.15;
        }
        data = null;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Scaffold.mc.player.inventory.currentItem = this.slot;
        Scaffold.mc.timer.timerSpeed = 1.0f;
        if (this.sneakMode.getOptions().equalsIgnoreCase("Silent")) {
            Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction(Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        super.onDisable();
    }

    @EventTarget
    public void onStrafeMotion(EventStrafe eventStrafe) {
        if (this.rotStrafe.getCurrentValue()) {
            if (mc.isSingleplayer()) {
                return;
            }
            eventStrafe.setCancelled(true);
            MovementHelper.calculateSilentMove(eventStrafe, RotationHelper.Rotation.packetYaw);
        }
    }

    @EventTarget
    public void onSafe(EventSafeWalk eventSafeWalk) {
        if (this.blocksafe.getCurrentValue() && !isSneaking) {
            eventSafeWalk.setCancelled(Scaffold.mc.player.onGround);
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion eventUpdate) {
        String tower = this.towerMode.getCurrentMode();
        this.setSuffix(this.blockRotation.getCurrentMode());
        if (this.jartex.getCurrentValue() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !Scaffold.mc.gameSettings.keyBindJump.isPressed()) {
            Scaffold.mc.player.motionY = 0.0;
            MovementHelper.setSpeed(MovementHelper.getSpeed());
        }
        if (tower.equalsIgnoreCase("Matrix")) {
            if (!MovementHelper.isMoving()) {
                if (Scaffold.mc.player.onGround && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Scaffold.mc.player.jump();
                }
                Scaffold.mc.player.motionY = Scaffold.mc.player.motionY > 0.0 && !Scaffold.mc.player.onGround ? (Scaffold.mc.player.motionY -= 0.00994) : (Scaffold.mc.player.motionY -= 0.00995);
            }
        } else if (tower.equalsIgnoreCase("NCP") && !MovementHelper.isMoving()) {
            if (Scaffold.mc.player.onGround && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                Scaffold.mc.player.jump();
            }
            float pos = -2.0f;
            if (Scaffold.mc.player.motionY < 0.1 && !(Scaffold.mc.world.getBlockState(new BlockPos(Scaffold.mc.player.posX, Scaffold.mc.player.posY, Scaffold.mc.player.posZ).add(0.0, pos, 0.0)).getBlock() instanceof BlockAir)) {
                Scaffold.mc.player.motionY -= 10.0;
            }
        }
        if (GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindSneak) && down.getCurrentValue()) {
            Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
            isSneaking = true;
        } else {
            isSneaking = false;
        }
        Scaffold.mc.player.motionX *= (double)this.speed.getCurrentValue();
        Scaffold.mc.player.motionZ *= (double)this.speed.getCurrentValue();
        if (InventoryHelper.doesHotbarHaveBlock()) {
            if (this.autoMotionStop.getCurrentValue() && Scaffold.mc.player.isSprinting() && Scaffold.mc.gameSettings.keyBindForward.isKeyDown() && sprintoff.getCurrentValue() && this.jump.getCurrentValue()) {
                Scaffold.mc.player.motionX *= -1.15;
                Scaffold.mc.player.motionZ *= -1.15;
            }
        } else if (!(Scaffold.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock) && Scaffold.searchBlock() != -1) {
            Scaffold.mc.playerController.windowClick(0, Scaffold.searchBlock(), 1, ClickType.QUICK_MOVE, Scaffold.mc.player);
        }
        for (double posY = Scaffold.mc.player.posY - 1.0; posY > 0.0; posY -= 1.0) {
            double yDif;
            BlockPos blockPos = isSneaking ? new BlockPos(Scaffold.mc.player).add(0.0, -1.0, 0.0).down() : new BlockPos(Scaffold.mc.player).add(0.0, -1.0, 0.0);
            BlockData newData = this.getBlockData2(blockPos);
            if (newData == null || !((yDif = Scaffold.mc.player.posY - posY) <= 7.0)) continue;
            data = newData;
        }
        if (sprintoff.getCurrentValue()) {
            Scaffold.mc.player.setSprinting(false);
        }
        if (data != null && this.slot != -1) {
            Vec3d hitVec = this.getVectorToRotate(data);
            if (this.blockRotation.getOptions().equalsIgnoreCase("Matrix")) {
                float[] rots = RotationHelper.getRotationVector(hitVec);
                eventUpdate.setYaw(rots[0]);
                eventUpdate.setPitch(rots[1]);
                Scaffold.mc.player.renderYawOffset = rots[0];
                Scaffold.mc.player.rotationYawHead = rots[0];
                Scaffold.mc.player.rotationPitchHead = rots[1];
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (InventoryHelper.doesHotbarHaveBlock() && data != null) {
            int slot = -1;
            int lastItem = Scaffold.mc.player.inventory.currentItem;
            BlockPos pos = Scaffold.data.pos;
            Vec3d hitVec = this.getVectorToPlace(data);
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = Scaffold.mc.player.inventory.getStackInSlot(i);
                if (!this.isValidItem(itemStack.getItem())) continue;
                slot = i;
            }
            if (slot != -1) {
                if (this.jump.getCurrentValue() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && Scaffold.mc.player.onGround) {
                    Scaffold.mc.player.jump();
                }
                String mode = this.sneakMode.getOptions();
                if (!this.jump.getCurrentValue() && MovementHelper.isMoving() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && InventoryHelper.doesHotbarHaveBlock()) {
                    if (this.canSneak()) {
                        if (mode.equalsIgnoreCase("Silent")) {
                            Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction(Scaffold.mc.player, CPacketEntityAction.Action.START_RIDING_JUMP));
                            Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction(Scaffold.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                        } else if (mode.equalsIgnoreCase("Client")) {
                            Scaffold.mc.gameSettings.keyBindSneak.setPressed(true);
                        }
                    } else if (mode.equalsIgnoreCase("Silent")) {
                        Scaffold.mc.player.connection.sendPacket(new CPacketEntityAction(Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    } else if (mode.equalsIgnoreCase("Client")) {
                        Scaffold.mc.gameSettings.keyBindSneak.setPressed(false);
                    }
                }
                if (this.canPlace() && EntityHelper.canBeClicked(pos)) {
                    if (this.time.hasReached(this.delay.getCurrentValue() + MathematicHelper.randomizeFloat(0.0f, this.delayRandom.getCurrentValue()))) {
                        if (MathematicHelper.randomizeFloat(0.0f, 100.0f) <= this.chance.getCurrentValue() && Scaffold.mc.player.inventory.currentItem != slot) {
                            Scaffold.mc.player.inventory.currentItem = slot;
                        }
                        Scaffold.mc.playerController.processRightClickBlock(Scaffold.mc.player, Scaffold.mc.world, pos, Scaffold.data.face, hitVec, EnumHand.MAIN_HAND);
                        if (this.swing.getCurrentValue()) {
                            Scaffold.mc.player.swingArm(EnumHand.MAIN_HAND);
                        } else {
                            Scaffold.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        }
                        Scaffold.mc.rightClickDelayTimer = 4;
                    }
                    if (Scaffold.mc.player.inventory.currentItem != lastItem) {
                        Scaffold.mc.player.inventory.currentItem = lastItem;
                    }
                    this.time.reset();
                }
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D render) {
        float width = render.getResolution().getScaledWidth();
        float height = render.getResolution().getScaledHeight();
        String blockString = this.getBlockCount() + " Blocks";
        GlStateManager.pushMatrix();
        GlStateManager.translate(10.0f, 5.0f, 0.0f);
        Scaffold.mc.fontRenderer.drawStringWithShadow(blockString, width / 2.0f + 50.0f - (float)Scaffold.mc.fontRendererObj.getStringWidth(blockString), height / 2.0f - 4.0f, -1);
        GlStateManager.popMatrix();
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!Scaffold.mc.player.inventoryContainer.getSlot(i).getHasStack() || !this.isValidItem(item = (is = Scaffold.mc.player.inventoryContainer.getSlot(i).getStack()).getItem())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private boolean isValidItem(Item item) {
        if (item instanceof ItemBlock) {
            ItemBlock iBlock = (ItemBlock)item;
            Block block = iBlock.getBlock();
            return !invalidBlocks.contains(block);
        }
        return false;
    }

    public BlockData getBlockData2(BlockPos pos) {
        BlockData blockData = null;
        for (int i = 0; blockData == null && i < 2; ++i) {
            if (!this.isBlockPosAir(pos.add(0, 0, 1))) {
                blockData = new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (!this.isBlockPosAir(pos.add(0, 0, -1))) {
                blockData = new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (!this.isBlockPosAir(pos.add(1, 0, 0))) {
                blockData = new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
                break;
            }
            if (!this.isBlockPosAir(pos.add(-1, 0, 0))) {
                blockData = new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
                break;
            }
            if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !this.isBlockPosAir(pos.add(0, -1, 0))) {
                blockData = new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
                break;
            }
            if (!this.isBlockPosAir(pos.add(0, 1, 0)) && isSneaking) {
                blockData = new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (!this.isBlockPosAir(pos.add(0, 1, 1)) && isSneaking) {
                blockData = new BlockData(pos.add(0, 1, 1), EnumFacing.DOWN);
                break;
            }
            if (!this.isBlockPosAir(pos.add(0, 1, -1)) && isSneaking) {
                blockData = new BlockData(pos.add(0, 1, -1), EnumFacing.DOWN);
                break;
            }
            if (!this.isBlockPosAir(pos.add(1, 1, 0)) && isSneaking) {
                blockData = new BlockData(pos.add(1, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (!this.isBlockPosAir(pos.add(-1, 1, 0)) && isSneaking) {
                blockData = new BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (!this.isBlockPosAir(pos.add(1, 0, 1))) {
                blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (!this.isBlockPosAir(pos.add(-1, 0, -1))) {
                blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (!this.isBlockPosAir(pos.add(1, 0, 1))) {
                blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.WEST);
                break;
            }
            if (!this.isBlockPosAir(pos.add(-1, 0, -1))) {
                blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.EAST);
                break;
            }
            if (!this.isBlockPosAir(pos.add(-1, 0, 1))) {
                blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (!this.isBlockPosAir(pos.add(1, 0, -1))) {
                blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (!this.isBlockPosAir(pos.add(1, 0, -1))) {
                blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.WEST);
                break;
            }
            if (!this.isBlockPosAir(pos.add(-1, 0, 1))) {
                blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.EAST);
                break;
            }
            pos = pos.down();
        }
        return blockData;
    }

    private Vec3d getVectorToPlace(BlockData data) {
        BlockPos pos = data.pos;
        EnumFacing face = data.face;
        double x = (double)pos.getX() + 0.5;
        double y = (double)pos.getY() + 0.5;
        double z = (double)pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += 0.3;
            z += 0.3;
        } else {
            y += 0.5;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += 0.15;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += 0.15;
        }
        return new Vec3d(x, y, z);
    }

    private Vec3d getVectorToRotate(BlockData data) {
        BlockPos pos = data.pos;
        EnumFacing face = data.face;
        double x = (double)pos.getX() + 0.5;
        double y = (double)pos.getY() + 0.5;
        double z = (double)pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += 0.4;
            z += 0.4;
        } else {
            y += 0.4;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += (double)this.motionOffset.getCurrentValue();
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += (double)this.motionOffset.getCurrentValue();
        }
        return new Vec3d(x, y, z);
    }

    public boolean isBlockPosAir(BlockPos blockPos) {
        return this.getBlockByPos(blockPos) == Blocks.AIR || this.getBlockByPos(blockPos) instanceof BlockLiquid;
    }

    public Block getBlockByPos(BlockPos blockPos) {
        return Scaffold.mc.world.getBlockState(blockPos).getBlock();
    }

    private static class BlockData {
        public BlockPos pos;
        public EnumFacing face;

        private BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }
}

