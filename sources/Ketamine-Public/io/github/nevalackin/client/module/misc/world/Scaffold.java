package io.github.nevalackin.client.module.misc.world;

import com.google.common.collect.Lists;
import io.github.nevalackin.client.event.player.*;
import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.property.MultiSelectionEnumProperty;
import io.github.nevalackin.client.util.movement.JumpUtil;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.player.WindowClickRequest;
import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class Scaffold extends Module {
    private static final EnumFacing[] FACINGS = new EnumFacing[]{
        EnumFacing.EAST,
        EnumFacing.WEST,
        EnumFacing.SOUTH,
        EnumFacing.NORTH};

    // Tower
    private final BooleanProperty towerProperty = new BooleanProperty("Tower", true);
    private final MultiSelectionEnumProperty<TowerFeature> towerFeaturesProperty = new MultiSelectionEnumProperty<>("Tower Features",
                                                                                                                    Lists.newArrayList(TowerFeature.AUTO_CENTER, TowerFeature.WATCHDOG),
                                                                                                                    TowerFeature.values());
    // Draw
    private final MultiSelectionEnumProperty<DrawOption> drawOptionsProperty = new MultiSelectionEnumProperty<>("Draw Options",
                                                                                                                Lists.newArrayList(DrawOption.PLACEMENT),
                                                                                                                DrawOption.values());
    // Placement
    private final BooleanProperty spoofHeldItemProperty = new BooleanProperty("Spoof Held Item", true);
    private final DoubleProperty placeDelayProperty = new DoubleProperty("Place Delay", 0, 0, 10, 1);
    private final EnumProperty<Swing> swingProperty = new EnumProperty<>("Swing", Swing.SILENT);
    private final BooleanProperty rayTraceCheckProperty = new BooleanProperty("Ray Trace Check", false);

    // Movement
    private final BooleanProperty safeWalkProperty = new BooleanProperty("Safe Walk", false);
    private final BooleanProperty keepPosProperty = new BooleanProperty("No Y Gain", true);
    private final BooleanProperty noSprintProperty = new BooleanProperty("No Sprint", true);

    // For drawing only...
    private final List<Vec3> breadcrumbs = new ArrayList<>();
    private double fadeInOutProgress;
    private int totalBlockCount;
    // Counters
    private int ticksSinceWindowClick;
    private int ticksSincePlace;
    // Block data
    private BlockData data;
    private BlockData lastPlacement;
    private float[] angles;
    // Tower
    private boolean towering;
    private int placedBlocks;
    // Other...
    private int bestBlockStack;
    private double startPosY;
    private WindowClickRequest lastRequest;
    private float randomSmoothingFactor;

    public Scaffold() {
        super("Scaffold", Category.MISC, Category.SubCategory.MISC_WORLD);

        this.placeDelayProperty.addValueAlias(0, "None");

        this.register(this.spoofHeldItemProperty, this.placeDelayProperty, this.swingProperty, this.rayTraceCheckProperty,
                      this.towerProperty, this.towerFeaturesProperty,
                      this.safeWalkProperty, this.noSprintProperty, this.keepPosProperty,
                      this.drawOptionsProperty);
    }

    @EventLink
    private final Listener<GetClientSideCurrentSlotEvent> onGetCurrentItem = event -> {
        if (this.spoofHeldItemProperty.getValue() && this.bestBlockStack != -1 && this.bestBlockStack >= 36)
            event.setCurrentItem(this.bestBlockStack - InventoryUtil.ONLY_HOT_BAR_BEGIN);
    };

    @EventLink
    private final Listener<SafeWalkEvent> onSafeWalkEvent = event -> {
        if (this.safeWalkProperty.getValue()) {
            event.setCancelled();
        }
    };

    @EventLink
    private final Listener<WindowClickEvent> onWindowClick = event -> {
        this.ticksSinceWindowClick = 0;
    };

    @EventLink
    private final Listener<BlockPlaceEvent> onBlockPlace = event -> {
        this.ticksSincePlace = 0;
    };

    @EventLink(0) // Call this listener after Sprint listener
    private final Listener<SendSprintStateEvent> onSprintEvent = event -> {
        if (event.isSprintState() && this.noSprintProperty.getValue()) {
            this.mc.thePlayer.setSprinting(false);
            event.setSprintState(false);
        }
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        final ScaledResolution sr = event.getScaledResolution();
        final float mx = sr.getScaledWidth() / 2.0f;
        final float my = sr.getScaledHeight() / 2.0f;

        final EntityPlayerSP player = this.mc.thePlayer;

        final double minFadeInProgress = 0.7;

        // Block counter
        if (this.bestBlockStack != -1) {
            // Get the "best" block itemStack from the inventory (computed every tick)
            final ItemStack stack = player.inventoryContainer.getSlot(this.bestBlockStack).getStack();
            // Check the stack in slot has not changed since last update
            if (stack != null) {
                if (this.fadeInOutProgress < 1.0)
                    this.fadeInOutProgress += 1.0 / Minecraft.getDebugFPS() * 2;

                final double width = 60;
                final double height = 20;

                final double left = mx - width / 2.0;
                final double top = my + 20 + 10; // middle + arrow spacing + size

                // Background
                BlurUtil.blurArea(left, top, width, height);
                DrawUtil.glDrawFilledQuad(left, top, width, height, 0x80000000);

                // Line
                DrawUtil.glDrawGradientLine(left, top, mx + width / 2.0, top, 1.f, ColourUtil.getClientColour());

                final String blockCount = String.format("%s blocks", this.totalBlockCount);

                final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

                final int itemStackSize = 16;

                final int textWidth = itemStackSize + 2 + (int) Math.ceil(fontRenderer.getWidth(blockCount));

                final int iconRenderPosX = (int) (left + width / 2 - textWidth / 2);

                final int iconRenderPosY = (int) (top + (height - itemStackSize) / 2);

                // Setup for item render with proper lighting
                final boolean restore = DrawUtil.glEnableBlend();
                GlStateManager.enableRescaleNormal();
                RenderHelper.enableGUIStandardItemLighting();

                // Draw block icon
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, iconRenderPosX, iconRenderPosY);

                // Restore after item render
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                glEnable(GL_ALPHA_TEST);
                DrawUtil.glRestoreBlend(restore);

                fontRenderer.draw(blockCount, iconRenderPosX + itemStackSize + 2,
                                  top + height / 2 - fontRenderer.getHeight(blockCount) / 2,
                                  0xFFFFFFFF);
            }
        }

        // Direction arrow
        {
            final float partialTicks = event.getPartialTicks();

            final float client = DrawUtil.interpolate(player.prevRotationYaw, player.rotationYaw, partialTicks);
            final float server = DrawUtil.interpolate(player.getLastTickYaw(), player.getYaw(), partialTicks);

            final float rotation = server - client;

            glDisable(GL_TEXTURE_2D);
            final boolean restore = DrawUtil.glEnableBlend();

            glEnable(GL_POLYGON_SMOOTH);
            glEnable(GL_LINE_SMOOTH);

            glLineWidth(1);

            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

            glPushMatrix();
            glTranslatef(mx, my, 0);

            glRotatef(rotation, 0, 0, 1);
            glTranslatef(0, -20, 0);

            // Draw Outline

            DrawUtil.glColour(0xFFFFFFFF);

            glBegin(GL_LINE_LOOP);
            {
                addTriangleVertices(10);
            }
            glEnd();

            // Draw Arrow

            glEnable(GL_POLYGON_SMOOTH);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

            DrawUtil.glColour(0x80FFFFFF);

            glBegin(GL_TRIANGLES);
            {
                addTriangleVertices(10);
            }
            glEnd();

            glPopMatrix();

            glDisable(GL_POLYGON_SMOOTH);
            glDisable(GL_LINE_SMOOTH);

            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);

            DrawUtil.glRestoreBlend(restore);
            glEnable(GL_TEXTURE_2D);
        }
    };

    private static void addTriangleVertices(final double size) {
        glVertex2d(0, -size / 2);
        glVertex2d(-size / 2, size / 2);
        glVertex2d(size / 2, size / 2);
    }

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enabling blending
        glEnable(GL_BLEND);
        // Disable alpha testing
        glDisable(GL_ALPHA_TEST);
        // Enable vertex colour blending
        glShadeModel(GL_SMOOTH);
        // Disable culling
        glDisable(GL_CULL_FACE);

        final float partialTicks = event.getPartialTicks();

        final double x = DrawUtil.interpolate(this.mc.thePlayer.prevPosX, this.mc.thePlayer.posX, partialTicks);
        final double y = DrawUtil.interpolate(this.mc.thePlayer.prevPosY, this.mc.thePlayer.posY, partialTicks);
        final double z = DrawUtil.interpolate(this.mc.thePlayer.prevPosZ, this.mc.thePlayer.posZ, partialTicks);

        final int colour = ColourUtil.getClientColour();
        final int clear = ColourUtil.removeAlphaComponent(colour);

        glTranslated(x, y + 0.01, z);

        glBegin(GL_TRIANGLE_FAN);
        {
            DrawUtil.glColour(colour);
            glVertex3f(0.f, 0.f, 0.f);

            DrawUtil.glColour(clear);

            final float radius = 0.6f;
            final int points = 40;

            final double pix2 = Math.PI * 2.0;

            for (int i = 0; i <= points; i++) {
                final float px = radius * (float) Math.cos(i * pix2 / points);
                final float pz = radius * (float) Math.sin(i * pix2 / points);

                glVertex3f(px, 0.f, pz);
            }
        }
        glEnd();

        glTranslated(-x, -y, -z);

        // Enable culling
        glEnable(GL_CULL_FACE);
        // Disable vertex colour blending
        glShadeModel(GL_FLAT);
        // Make line visible through walls
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);

        if (this.drawOptionsProperty.hasSelections()) {
            // Enable line anti-aliasing
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            // Set line size
            glLineWidth(1.0F);

            if (this.drawOptionsProperty.isSelected(DrawOption.PLACEMENT) && this.lastPlacement != null) {
                final BlockPos placePos = this.lastPlacement.pos.offset(this.lastPlacement.face);

                DrawUtil.glColour(ColourUtil.overwriteAlphaComponent(colour, 0x50));
                DrawUtil.glDrawBoundingBox(this.mc.theWorld.getBlockState(placePos).getBlock()
                                               .getSelectedBoundingBox(this.mc.theWorld, placePos),
                                           1, true);
            }

            if (this.drawOptionsProperty.isSelected(DrawOption.BREADCRUMBS)) {
                // Set point size
                glPointSize(5.0F);
                // Enable point anti-aliasing
                glEnable(GL_POINT_SMOOTH);
                glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
                // Set color
                DrawUtil.glColour(ColourUtil.getSecondaryColour());

                Vec3 lastCrumb = null;

                // Draw line
                glBegin(GL_LINE_STRIP);
                {
                    for (final Vec3 breadcrumb : this.breadcrumbs) {
                        // If dist from last crumb >sqrt(3) start a new line
                        if (lastCrumb != null && lastCrumb.distanceTo(breadcrumb) > Math.sqrt(3)) {
                            glEnd();
                            glBegin(GL_LINE_STRIP);
                        }
                        // Specify line vertex (bend) at breadcrumb pos
                        glVertex3d(breadcrumb.xCoord, breadcrumb.yCoord, breadcrumb.zCoord);

                        lastCrumb = breadcrumb;
                    }
                }
                glEnd();

                // Set color
                DrawUtil.glColour(colour);

                // Draw points
                glBegin(GL_POINTS);
                {
                    for (final Vec3 breadcrumb : this.breadcrumbs) {
                        // Specify point pos at breadcrumb pos
                        glVertex3d(breadcrumb.xCoord, breadcrumb.yCoord, breadcrumb.zCoord);
                    }
                }
                glEnd();

                // Disable point anti-aliasing
                glDisable(GL_POINT_SMOOTH);
                glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
            }

            // Disable line anti-aliasing
            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }

        // Enable alpha testing
        glEnable(GL_ALPHA_TEST);
        // Enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Enable depth
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        // Disable blending
        glDisable(GL_BLEND);
    };


    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            // Increment tick counters
            this.ticksSinceWindowClick++;
            this.ticksSincePlace++;

            // Invalidate old data
            this.data = null;

            // Update towering state
            this.towering = this.towerProperty.getValue() && this.mc.gameSettings.keyBindJump.isKeyDown();

            // Look for best block stack in hot bar
            this.bestBlockStack = this.getBestBlockStack(InventoryUtil.ONLY_HOT_BAR_BEGIN, InventoryUtil.END);

            this.calculateTotalBlockCount();
            this.moveBlocksIntoHotBar();

            // If best block stack is in hot bar
            if (this.bestBlockStack >= InventoryUtil.ONLY_HOT_BAR_BEGIN) {
                final BlockPos blockUnder = this.getBlockUnder();
                this.data = this.getBlockData(blockUnder);
                if (this.data == null) this.data = this.getBlockData(blockUnder.offset(EnumFacing.DOWN));

                this.randomSmoothingFactor += RandomUtils.nextFloat(0.f, 0.2f) - 0.1f;
                this.randomSmoothingFactor = Math.min(Math.max(0.f, this.randomSmoothingFactor), 1.f);

                if (this.data != null) {
                    // If ray trace fails hit vec will be null
                    if (this.validateReplaceable(this.data) && this.data.hitVec != null) {
                        // Calculate rotations to hit vec
                        this.angles = RotationUtil.getRotations(new float[]{event.getLastTickYaw(), event.getLastTickPitch()},
                                                                12.f + this.randomSmoothingFactor * 2.f,
                                                                RotationUtil.getHitOrigin(this.mc.thePlayer),
                                                                this.data.hitVec);

                        if (this.towering) {
                            this.mc.thePlayer.motionX = 0;
                            this.mc.thePlayer.motionZ = 0;

                            final double min = 9.0E-4D;

                            if (this.mc.thePlayer.ticksExisted % 2 == 0)
                                event.setPosX(event.getPosX() + min);
                        }
                    } else {
                        this.data = null;
                    }
                }

                // If using no sprint & on ground
                if (this.noSprintProperty.getValue() && this.mc.thePlayer.onGround) {
                    // And has speed effect...
                    final PotionEffect speed = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed);
                    final int moveSpeedAmp = speed == null ? 0 : speed.getAmplifier() + 1;
                    if (moveSpeedAmp > 0) {
                        final double multiplier = 1.0 + 0.2 * moveSpeedAmp + 0.1;
                        // Reduce motionX/Z based on speed amplifier
                        this.mc.thePlayer.motionX /= multiplier;
                        this.mc.thePlayer.motionZ /= multiplier;
                    }
                }

                // If has not set angles or has not yet placed a block
                if (this.angles == null || this.lastPlacement == null) {
                    // Get the last rotations (EntityPlayerSP#rotationYaw/rotationPitch)
                    final float[] lastAngles = this.angles != null ? this.angles : new float[]{event.getYaw(), event.getPitch()};
                    // Get the opposite direct that you are moving
                    final float moveDir = MovementUtil.getMovementDirection(this.mc.thePlayer.moveForward,
                                                                            this.mc.thePlayer.moveStrafing,
                                                                            this.mc.thePlayer.rotationYaw);
                    // Desired rotations
                    final float[] dstRotations = new float[]{moveDir + 180.f, 75.f};
                    // Smooth to opposite
                    RotationUtil.applySmoothing(lastAngles, 12.f + this.randomSmoothingFactor * 2.f,
                                                dstRotations);
                    // Apply GCD fix (just for fun)
                    RotationUtil.applyGCD(dstRotations, lastAngles);
                    this.angles = dstRotations;
                }

                // Set rotations to persistent rotations
                event.setYaw(this.angles[0]);
                event.setPitch(this.angles[1]);
            }
        } else {
            this.doPlace(event);
        }
    };

    private void doPlace(final UpdatePositionEvent event) {
        if (this.bestBlockStack < 36 || this.data == null || this.ticksSincePlace <= this.placeDelayProperty.getValue())
            return;

        final Vec3 hitVec;

        if (this.rayTraceCheckProperty.getValue()) {
            // Perform ray trace with current angle stepped rotations
            final MovingObjectPosition rayTraceResult = RotationUtil.rayTraceBlocks(this.mc,
                                                                                    event.isPre() ? event.getLastTickYaw() : event.getYaw(),
                                                                                    event.isPre() ? event.getLastTickPitch() : event.getPitch());
            // If nothing is hit return
            if (rayTraceResult == null) return;
            // If did not hit block return
            if (rayTraceResult.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
            // If side hit does not match block data return
            if (rayTraceResult.sideHit != this.data.face) return;
            // If block pos does not match block data return
            final BlockPos dstPos = this.data.pos;
            final BlockPos rayDstPos = rayTraceResult.getBlockPos();
            if (rayDstPos.getX() != dstPos.getX() ||
                rayDstPos.getY() != dstPos.getY() ||
                rayDstPos.getZ() != dstPos.getZ()) return;

            hitVec = rayTraceResult.hitVec;
        } else {
            hitVec = this.data.hitVec;
        }

        final ItemStack heldItem;

        if (this.spoofHeldItemProperty.getValue()) {
            heldItem = this.mc.thePlayer.inventoryContainer.getSlot(this.bestBlockStack).getStack();
        } else {
            // Switch item client side
            this.mc.thePlayer.inventory.currentItem = this.bestBlockStack - InventoryUtil.ONLY_HOT_BAR_BEGIN;
            heldItem = this.mc.thePlayer.getCurrentEquippedItem();
        }

        if (heldItem == null) return;

        // Attempt place using ray trace hit vec
        if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, heldItem,
                                                        this.data.pos, this.data.face, hitVec)) {
            this.lastPlacement = this.data;
            this.placedBlocks++;

            // Save hit vec for bread crumbs
            if (this.drawOptionsProperty.isSelected(DrawOption.BREADCRUMBS)) {
                this.breadcrumbs.add(hitVec);
            }

            if (this.towering && MovementUtil.isOnGround(this.mc.theWorld, this.mc.thePlayer, 0.0626)) {

                // TODO :: Watchdog baipai impl (move on x/z)

                if (this.towerFeaturesProperty.isSelected(TowerFeature.AUTO_CENTER)) {
                    final double xDif = this.mc.thePlayer.posX - hitVec.xCoord;
                    final double zDif = this.mc.thePlayer.posZ - hitVec.zCoord;

                    if (Math.sqrt(xDif * xDif + zDif * zDif) < 0.2873 * 2.149) {
                        // Teleport to centre of block
                        this.mc.thePlayer.setPosition(hitVec.xCoord, this.mc.thePlayer.posY, hitVec.zCoord);
                    }
                }

                this.mc.thePlayer.motionY = JumpUtil.getJumpHeight(this.mc.thePlayer) - 0.000454352838557992;
            }

            switch (this.swingProperty.getValue()) {
                case CLIENT:
                    this.mc.thePlayer.swingItem();
                    break;
                case SILENT:
                    this.mc.thePlayer.sendQueue.sendPacket(new C0APacketAnimation());
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
        this.lastPlacement = null;
        this.towering = false;
        this.placedBlocks = 0;

        this.randomSmoothingFactor = 0.5f;

        if (this.mc.thePlayer != null) this.startPosY = this.mc.thePlayer.posY;
    }

    @Override
    public void onDisable() {
        this.angles = null;
        this.breadcrumbs.clear();
    }

    private BlockData getBlockData(final BlockPos pos) {
        final EnumFacing[] facings = FACINGS;

        // 1 of the 4 directions around player
        for (EnumFacing facing : facings) {
            final BlockPos blockPos = pos.add(facing.getOpposite().getDirectionVec());
            if (InventoryUtil.validateBlock(this.mc.theWorld.getBlockState(blockPos).getBlock(), InventoryUtil.BlockAction.PLACE_ON)) {
                final BlockData data = new BlockData(blockPos, facing);
                if (this.validateBlockRange(data))
                    return data;
            }
        }

        // 2 Blocks Under e.g. When jumping
        final BlockPos posBelow = pos.add(0, -1, 0);
        if (InventoryUtil.validateBlock(this.mc.theWorld.getBlockState(posBelow).getBlock(), InventoryUtil.BlockAction.PLACE_ON)) {
            final BlockData data = new BlockData(posBelow, EnumFacing.UP);
            if (this.validateBlockRange(data))
                return data;
        }

        // 2 Block extension & diagonal
        for (EnumFacing facing : facings) {
            final BlockPos blockPos = pos.add(facing.getOpposite().getDirectionVec());
            for (EnumFacing facing1 : facings) {
                final BlockPos blockPos1 = blockPos.add(facing1.getOpposite().getDirectionVec());
                if (InventoryUtil.validateBlock(this.mc.theWorld.getBlockState(blockPos1).getBlock(), InventoryUtil.BlockAction.PLACE_ON)) {
                    final BlockData data = new BlockData(blockPos1, facing1);
                    if (this.validateBlockRange(data))
                        return data;
                }
            }
        }

        return null;

    }

    private boolean validateBlockRange(final BlockData data) {
        final Vec3 pos = data.hitVec;

        if (pos == null)
            return false;

        final EntityPlayerSP player = this.mc.thePlayer;

        final double x = (pos.xCoord - player.posX);
        final double y = (pos.yCoord - (player.posY + player.getEyeHeight()));
        final double z = (pos.zCoord - player.posZ);

        final float reach = this.mc.playerController.getBlockReachDistance();

        return Math.sqrt(x * x + y * y + z * z) <= reach;
    }

    private boolean validateReplaceable(final BlockData data) {
        final BlockPos pos = data.pos.offset(data.face);
        return this.mc.theWorld.getBlockState(pos)
            .getBlock()
            .isReplaceable(this.mc.theWorld, pos);
    }

    private BlockPos getBlockUnder() {
        if (this.keepPosProperty.getValue() && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            return new BlockPos(this.mc.thePlayer.posX, Math.min(this.startPosY, this.mc.thePlayer.posY) - 1, this.mc.thePlayer.posZ);
        } else {
            this.startPosY = this.mc.thePlayer.posY;

            return new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1, this.mc.thePlayer.posZ);
        }
    }

    private void moveBlocksIntoHotBar() {
        // If no blocks in hot bar
        if (this.ticksSinceWindowClick > 4) {
            // Look for best block stack in inventory
            final int bestStackInInv = this.getBestBlockStack(InventoryUtil.EXCLUDE_ARMOR_BEGIN, InventoryUtil.ONLY_HOT_BAR_BEGIN);
            // If you have no blocks return
            if (bestStackInInv == -1) return;

            boolean foundEmptySlot = false;

            for (int i = InventoryUtil.END - 1; i >= InventoryUtil.ONLY_HOT_BAR_BEGIN; i--) {
                final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (stack == null) {
                    if (this.lastRequest == null || this.lastRequest.isCompleted()) {
                        final int slotID = i;
                        InventoryUtil.queueClickRequest(this.lastRequest = new WindowClickRequest() {
                            @Override
                            public void performRequest() {
                                // Move blocks from inventory into free slot
                                InventoryUtil.windowClick(mc, bestStackInInv,
                                                          slotID - InventoryUtil.ONLY_HOT_BAR_BEGIN,
                                                          InventoryUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                            }
                        });
                    }

                    foundEmptySlot = true;
                }
            }

            if (!foundEmptySlot) {
                if (this.lastRequest == null || this.lastRequest.isCompleted()) {
                    InventoryUtil.queueClickRequest(this.lastRequest = new WindowClickRequest() {
                        @Override
                        public void performRequest() {
                            final int overrideSlot = 8;
                            // Swap with item in last slot of hot bar
                            InventoryUtil.windowClick(mc, bestStackInInv, overrideSlot,
                                                      InventoryUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                        }
                    });
                }
            }
        }
    }

    private int getBestBlockStack(final int start, final int end) {
        int bestSlot = -1, bestSlotStackSize = 0;

        for (int i = start; i < end; i++) {
            final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null &&
                stack.stackSize > bestSlotStackSize &&
                stack.getItem() instanceof ItemBlock &&
                InventoryUtil.isStackValidToPlace(stack)) {

                bestSlot = i;
                bestSlotStackSize = stack.stackSize;
            }
        }

        return bestSlot;
    }

    private void calculateTotalBlockCount() {
        this.totalBlockCount = 0;

        for (int i = InventoryUtil.EXCLUDE_ARMOR_BEGIN; i < InventoryUtil.END; i++) {
            final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null &&
                stack.stackSize >= 1 &&
                stack.getItem() instanceof ItemBlock &&
                InventoryUtil.isStackValidToPlace(stack)) {

                this.totalBlockCount += stack.stackSize;
            }
        }
    }

    private static class BlockData {

        private final BlockPos pos;
        private final EnumFacing face;
        private final Vec3 hitVec;

        public BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
            this.hitVec = this.calculateBlockData();
        }

        private Vec3 calculateBlockData() {
            final Vec3i directionVec = this.face.getDirectionVec();
            final Minecraft mc = Minecraft.getMinecraft();

            double x;
            double z;

            switch (this.face.getAxis()) {
                case Z:
                    final double absX = Math.abs(mc.thePlayer.posX);
                    double xOffset = absX - (int) absX;

                    if (mc.thePlayer.posX < 0) {
                        xOffset = 1.0F - xOffset;
                    }

                    x = directionVec.getX() * xOffset;
                    z = directionVec.getZ() * xOffset;
                    break;
                case X:
                    final double absZ = Math.abs(mc.thePlayer.posZ);
                    double zOffset = absZ - (int) absZ;

                    if (mc.thePlayer.posZ < 0) {
                        zOffset = 1.0F - zOffset;
                    }

                    x = directionVec.getX() * zOffset;
                    z = directionVec.getZ() * zOffset;
                    break;
                default:
                    x = 0.25;
                    z = 0.25;
                    break;
            }

            if (this.face.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) {
                x = -x;
                z = -z;
            }

            final Vec3 hitVec = new Vec3(this.pos).addVector(x + z, directionVec.getY() * 0.5, x + z);

            final Vec3 src = mc.thePlayer.getPositionEyes(1.0F);
            final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(src,
                                                                        hitVec,
                                                                        false,
                                                                        false,
                                                                        true);

            if (obj == null || obj.hitVec == null || obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
                return null;

            switch (this.face.getAxis()) {
                case Z:
                    obj.hitVec = new Vec3(obj.hitVec.xCoord, obj.hitVec.yCoord, Math.round(obj.hitVec.zCoord));
                    break;
                case X:
                    obj.hitVec = new Vec3(Math.round(obj.hitVec.xCoord), obj.hitVec.yCoord, obj.hitVec.zCoord);
                    break;
            }

            if (this.face != EnumFacing.DOWN && this.face != EnumFacing.UP) {
                final IBlockState blockState = mc.theWorld.getBlockState(obj.getBlockPos());
                final Block blockAtPos = blockState.getBlock();

                double blockFaceOffset;

                blockFaceOffset = RandomUtils.nextDouble(0.1, 0.3);

                if (blockAtPos instanceof BlockSlab && !((BlockSlab) blockAtPos).isDouble()) {
                    final BlockSlab.EnumBlockHalf half = blockState.getValue(BlockSlab.HALF);

                    if (half != BlockSlab.EnumBlockHalf.TOP) {
                        blockFaceOffset += 0.5;
                    }
                }

                obj.hitVec = obj.hitVec.addVector(0.0D, -blockFaceOffset, 0.0D);
            }

            return obj.hitVec;
        }
    }

    private enum Swing {
        CLIENT("Client"),
        SILENT("Silent"),
        NO_SWING("No Swing");

        private final String name;

        Swing(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum DrawOption {
        BREADCRUMBS("Breadcrumbs"),
        PLACEMENT("Draw Placement");

        private final String name;

        DrawOption(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum TowerFeature {
        AUTO_CENTER("Auto Center"),
        WATCHDOG("Watchdog");

        private final String name;

        TowerFeature(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
