package me.nyan.flush.module.impl.world;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.*;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    private final Timer timer = new Timer();
    private final Timer boostTimer = new Timer();
    private boolean shouldBoost;
    private int blockCount = 0;
    private Item currentBlock;
    private BlockData data;
    private double startY;
    private int startSlot;
    private int packetSlot;
    private float[] lastRotations = new float[2];
    private final BlockPos[] blockPositions = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
    private final EnumFacing[] facings = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH};

    private final List<Block> validPlacingBlocks = Arrays.asList(Blocks.air, Blocks.lava, Blocks.flowing_lava, Blocks.water, Blocks.flowing_water);
    private final List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table,
            Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava,
            Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
            Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate,
            Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom,
            Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus,
            Blocks.ladder, Blocks.web, Blocks.tripwire_hook, Blocks.tnt);

    private final ModeSetting itemSpoof = new ModeSetting("Item Spoof", this, "None", "None", "Packet", "Swap"),
            boostMode = new ModeSetting("Boost Mode", this, "None", "None", "Jump", "Verus", "Redesky", "Vanilla");
    private final BooleanSetting rotations = new BooleanSetting("Rotations", this, true),
            tower = new BooleanSetting("Tower", this, true),
            safewalk = new BooleanSetting("Safewalk", this, true),
            keepRotations = new BooleanSetting("Keep Rotations", this, false,
                    rotations::getValue),
            swingItem = new BooleanSetting("Swing Item", this, false),
            blockEsp = new BooleanSetting("Block ESP", this, false);
    private final NumberSetting boostSpeed = new NumberSetting("Boost Speed", this, 0.3, 0.2, 1.4, 0.1,
            () -> boostMode.is("vanilla")),
            delay = new NumberSetting("Delay", this, 0, 0, 400);
    public final BooleanSetting sprint = new BooleanSetting("Sprint", this, true);

    public Scaffold() {
        super("Scaffold", Category.WORLD);
    }

    @Override
    public void onEnable() {
        startY = (int) mc.thePlayer.posY - 1;
        shouldBoost = true;
        boostTimer.reset();
        timer.reset();
        startSlot = mc.thePlayer.inventory.currentItem;
        lastRotations = new float[] {Integer.MAX_VALUE, 0};
        packetSlot = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (itemSpoof.is("swap")) {
            mc.thePlayer.inventory.currentItem = startSlot;
        }
        mc.timer.timerSpeed = 1;

        if (itemSpoof.is("packet") && packetSlot != mc.thePlayer.inventory.currentItem) {
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        boolean jump = boostMode.is("jump") || boostMode.is("verus");
        if (data != null) {
            if (boostMode.is("verus")) {
                if (MovementUtils.isMoving()) {
                    double speed = -0.09;
                    if (!mc.thePlayer.isSprinting()) {
                        speed -= 0.03;
                    }
                    if (mc.thePlayer.moveStrafing != 0.0F) {
                        speed -= 0.01;
                    }
                    MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() + speed);
                } else {
                    MovementUtils.stopMotion();
                }
            }
        }
        data = null;

        int slot = -1;
        blockCount = 0;
        currentBlock = null;

        for (double posY = mc.thePlayer.posY - 1; posY > mc.thePlayer.posY - 4; posY--) {
            BlockData newData = getBlockData(new BlockPos(mc.thePlayer.posX, posY, mc.thePlayer.posZ),
                    new BlockPos(0, jump && !mc.thePlayer.movementInput.jump ? mc.thePlayer.posY - startY - 1 : -1, 0));

            if (newData != null) {
                data = newData;
                break;
            }
        }

        for (int i = 8; i >= 0; i--) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.stackSize <= 0 || !isItemValid(itemStack.getItem())) {
                continue;
            }

            blockCount += itemStack.stackSize;
            currentBlock = itemStack.getItem();
            slot = i;
        }

        BlockPos pos = data.getPos();
        Block block = pos.offset(data.getFacing()).getBlock();
        Vec3 vec = getVec3(data);

        if (slot == -1) {
            return;
        }

        if (rotations.getValue() && keepRotations.getValue() && lastRotations[0] != Integer.MAX_VALUE) {
            e.setYaw(lastRotations[0]);
            e.setPitch(lastRotations[1]);
        }

        if (!validPlacingBlocks.contains(block) || !block.isReplaceable(mc.theWorld, pos)) {
            return;
        }

        lastRotations = CombatUtils.getRotations(vec);

        switch (boostMode.getValue().toUpperCase()) {
            case "VANILLA":
                if (MovementUtils.isMoving() && mc.thePlayer.onGround) {
                    MovementUtils.setSpeed(boostSpeed.getValue() / 2);
                }
                break;

            case "REDESKY":
                if (MovementUtils.isMoving() && mc.thePlayer.onGround && !boostTimer.hasTimeElapsed(800, false) && shouldBoost
                        && !mc.gameSettings.keyBindJump.isKeyDown())
                    mc.timer.timerSpeed = 2.2f;
                else
                    mc.timer.timerSpeed = 1;
                break;

            case "JUMP":
                if (MovementUtils.isMoving()) {
                    if (mc.thePlayer.onGround && mc.thePlayer.jumpTicks == 0) {
                        mc.thePlayer.jump();
                        mc.thePlayer.jumpTicks = 10;
                    }
                }
                break;
        }

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            if (!MovementUtils.isMoving() && tower.getValue()) {
                mc.thePlayer.motionY = 0.4198;
                MovementUtils.stopMotion();
            } else if (mc.thePlayer.onGround)
                mc.thePlayer.motionY = 0.42;

            if (mc.thePlayer.onGround) {
                startY++;
            }
        }

        switch (e.getState()) {
            case PRE:
                if (rotations.getValue()) {
                    e.setYaw(lastRotations[0]);
                    e.setPitch(lastRotations[1]);
                }
                break;

            case POST:
                if (!timer.hasTimeElapsed(delay.getValueInt(), false)) {
                    return;
                }

                int lastSlot = mc.thePlayer.inventory.currentItem;

                if (mc.thePlayer.inventory.currentItem != slot) {
                    mc.thePlayer.inventory.currentItem = slot;
                }

                if (packetSlot != slot) {
                    mc.getNetHandler().addToSendQueueNoEvent(new C09PacketHeldItemChange(slot));
                    packetSlot = slot;
                }

                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, data.getFacing(), vec)) {
                    if (swingItem.getValue()) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.getNetHandler().addToSendQueueNoEvent(new C0APacketAnimation());
                    }
                    timer.reset();
                }

                if (!itemSpoof.is("swap")) {
                    mc.thePlayer.inventory.currentItem = lastSlot;
                }
                break;
        }
    }

    @SubscribeEvent
    public void onSafewalk(EventSafewalk e) {
        if (safewalk.getValue()) {
            e.cancel();
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (!blockEsp.getValue() || blockCount < 1 || data == null) {
            return;
        }

        GL11.glBlendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        GL11.glLineWidth(2);

        BlockPos blockPos = new BlockPos(mc.thePlayer.posX - (MovementUtils.isMoving() ? MathHelper.sin(MovementUtils.getDirection()) : 0),
                boostMode.is("jump") ? startY + (mc.thePlayer.posY - startY - 1) : mc.thePlayer.posY - 1,
                mc.thePlayer.posZ + (MovementUtils.isMoving() ? MathHelper.cos(MovementUtils.getDirection()) : 0));

        RenderUtils.drawBlockBox(blockPos, e.getPartialTicks(), 0x5000FF00);

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (blockCount < 1 || data == null) {
            return;
        }

        if (boostMode.is("redesky") && e.getPacket() instanceof S08PacketPlayerPosLook) {
            shouldBoost = false;
        }

        if (itemSpoof.is("packet") && e.getPacket() instanceof C09PacketHeldItemChange) {
            e.cancel();
            //((C09PacketHeldItemChange) e.getPacket()).setSlotId(slot);
        }
    }

    @SubscribeEvent
    public void onRender2D(Event2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        String text = (blockCount != 0 ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + (blockCount + (EnumChatFormatting.RESET + " block" +
                (blockCount != 1 ? "s" : "")));

        if (currentBlock != null) {
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(currentBlock), sr.getScaledWidth() / 2 - 20, sr.getScaledHeight() / 2 + 30);
            RenderHelper.disableStandardItemLighting();
        }

        Flush.getFont("GoogleSansDisplay", 18).drawString(text, sr.getScaledWidth() / 2f + (currentBlock != null ? -4 :
                -Flush.getFont("GoogleSansDisplay", 18).getStringWidth(text) / 2F), sr.getScaledHeight() / 2f + 30, -1);
    }

    @Override
    public String getSuffix() {
        return String.valueOf(blockCount);
    }

    private Vec3 getVec3(BlockData data) {
        return new Vec3(data.getPos().add(0.5F + data.getFacing().getFrontOffsetX() / 2F, 1F + data.getFacing().getFrontOffsetY() / 2F +
        (data.getFacing() != EnumFacing.UP && data.getFacing() != EnumFacing.DOWN ? 0.5F : 0), 0.5F + data.getFacing().getFrontOffsetZ() / 2F));
    }

    private BlockData getBlockData(final BlockPos pos, final BlockPos posBelow) {
        if (!validPlacingBlocks.contains(pos.add(posBelow).getBlock())) {
            return new BlockData(pos.add(posBelow), EnumFacing.UP);
        }

        for (int i = 0; i < blockPositions.length; i++) {
            BlockPos blockPos = pos.add(blockPositions[i]);

            if (!validPlacingBlocks.contains(blockPos.getBlock()))
                return new BlockData(blockPos, facings[i]);

            for (int j = 0; j < blockPositions.length; j++) {
                BlockPos blockPos1 = pos.add(blockPositions[j]);
                BlockPos blockPos2 = blockPos.add(0, blockPositions[j].getY(), blockPositions[j].getZ());

                if (!validPlacingBlocks.contains(blockPos1.getBlock()))
                    return new BlockData(blockPos1, facings[j]);

                if (!validPlacingBlocks.contains(blockPos2.getBlock()))
                    return new BlockData(blockPos2, facings[j]);
            }
        }

        return null;
    }

    private boolean isItemValid(Item item) {
        return item instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) item).getBlock());
    }

    private static class BlockData {
        private BlockPos pos;
        private EnumFacing facing;

        private BlockData(BlockPos pos, EnumFacing facing) {
            this.pos = pos;
            this.facing = facing;
        }

        public BlockPos getPos() {
            return pos;
        }

        public EnumFacing getFacing() {
            return facing;
        }

        public void setPos(BlockPos pos) {
            this.pos = pos;
        }

        public void setFacing(EnumFacing facing) {
            this.facing = facing;
        }
    }
}