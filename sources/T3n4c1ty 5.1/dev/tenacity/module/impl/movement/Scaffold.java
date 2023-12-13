package dev.tenacity.module.impl.movement;

import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.player.BlockPlaceableEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.SafeWalkEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.utils.player.RotationUtils;
import dev.tenacity.utils.player.ScaffoldUtils;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.server.PacketUtils;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.client.gui.IFontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Scaffold extends Module {

    private final ModeSetting countMode = new ModeSetting("Block Counter", "Tenacity", "None", "Tenacity", "Basic", "Polar");
    private final BooleanSetting rotations = new BooleanSetting("Rotations", true);
    private final ModeSetting rotationMode = new ModeSetting("Rotation Mode", "Watchdog", "Watchdog", "NCP", "Back", "45", "Enum", "Down", "0");
    private final ModeSetting placeType = new ModeSetting("Place Type", "Post", "Pre", "Post", "Legit", "Dynamic");
    public static ModeSetting keepYMode = new ModeSetting("Keep Y Mode", "Always", "Always", "Speed toggled");
    public static ModeSetting sprintMode = new ModeSetting("Sprint Mode", "Vanilla", "Vanilla", "Watchdog", "Cancel");
    public static ModeSetting towerMode = new ModeSetting("Tower Mode", "Watchdog", "Vanilla", "NCP", "Watchdog", "Verus");
    public static ModeSetting swingMode = new ModeSetting("Swing Mode", "Client", "Client", "Silent");
    public static NumberSetting delay = new NumberSetting("Delay", 0, 2, 0, 0.05);
    //public static NumberSetting extend = new NumberSetting("Extend", 0, 6, 0, 0.05);
    private final NumberSetting timer = new NumberSetting("Timer", 1, 5, 0.1, 0.1);
    public static final BooleanSetting auto3rdPerson = new BooleanSetting("Auto 3rd Person", false);
    public static final BooleanSetting speedSlowdown = new BooleanSetting("Speed Slowdown", true);
    public static final NumberSetting speedSlowdownAmount = new NumberSetting("Slowdown Amount", 0.1, 0.2, 0.01, 0.01);
    public static final BooleanSetting itemSpoof = new BooleanSetting("Item Spoof", false);
    public static final BooleanSetting downwards = new BooleanSetting("Downwards", false);
    public static final BooleanSetting safewalk = new BooleanSetting("Safewalk", false);
    public static final BooleanSetting sprint = new BooleanSetting("Sprint", false);
    private final BooleanSetting sneak = new BooleanSetting("Sneak", false);
    public static final BooleanSetting tower = new BooleanSetting("Tower", false);
    private final NumberSetting towerTimer = new NumberSetting("Tower Timer Boost", 1.2, 5, 0.1, 0.1);
    private final BooleanSetting swing = new BooleanSetting("Swing", true);
    private final BooleanSetting autoJump = new BooleanSetting("Auto Jump", false);
    private final BooleanSetting hideJump = new BooleanSetting("Hide Jump", false);
    private final BooleanSetting baseSpeed = new BooleanSetting("Base Speed", false);
    public static BooleanSetting keepY = new BooleanSetting("Keep Y", false);
    private ScaffoldUtils.BlockCache blockCache, lastBlockCache;
    private float y;
    private float speed;
    private final MouseFilter pitchMouseFilter = new MouseFilter();
    private final TimerUtil delayTimer = new TimerUtil();
    private final TimerUtil timerUtil = new TimerUtil();
    public static double keepYCoord;
    private boolean shouldSendPacket;
    private boolean shouldTower;
    private boolean firstJump;
    private boolean pre;
    private int jumpTimer;
    private int slot;
    private int prevSlot;
    private float[] cachedRots = new float[2];

    private final Animation anim = new DecelerateAnimation(250, 1);

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT, "Automatically places blocks under you");
        this.addSettings(countMode, rotations, rotationMode, placeType, keepYMode, sprintMode, towerMode, swingMode, delay, timer,
                auto3rdPerson, speedSlowdown, speedSlowdownAmount, itemSpoof, downwards, safewalk, sprint, sneak, tower, towerTimer,
                swing, autoJump, hideJump, baseSpeed, keepY);
        rotationMode.addParent(rotations, ParentAttribute.BOOLEAN_CONDITION);
        sprintMode.addParent(sprint, ParentAttribute.BOOLEAN_CONDITION);
        towerMode.addParent(tower, ParentAttribute.BOOLEAN_CONDITION);
        swingMode.addParent(swing, ParentAttribute.BOOLEAN_CONDITION);
        towerTimer.addParent(tower, ParentAttribute.BOOLEAN_CONDITION);
        keepYMode.addParent(keepY, ParentAttribute.BOOLEAN_CONDITION);
        hideJump.addParent(autoJump, ParentAttribute.BOOLEAN_CONDITION);
        speedSlowdownAmount.addParent(speedSlowdown, ParentAttribute.BOOLEAN_CONDITION);
    }

    @Override
    public void onMotionEvent(MotionEvent e) {
        // Timer Stuff
        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.timer.timerSpeed = timer.getValue().floatValue();
        } else {
            mc.timer.timerSpeed = tower.isEnabled() ? towerTimer.getValue().floatValue() : 1;
        }

        if (e.isPre()) {
            // Auto Jump
            if (baseSpeed.isEnabled()) {
                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 0.7);
            }
            if (autoJump.isEnabled() && mc.thePlayer.onGround && MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.jump();
            }

            if (sprint.isEnabled() && sprintMode.is("Watchdog") && mc.thePlayer.onGround && MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown() && !isDownwards() && mc.thePlayer.isSprinting()) {
                final double[] offset = MathUtils.yawPos(mc.thePlayer.getDirection(), MovementUtils.getSpeed() / 2);
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - offset[0], mc.thePlayer.posY, mc.thePlayer.posZ - offset[1], true));
            }

            // Rotations
            if (rotations.isEnabled()) {
                float[] rotations = new float[]{0, 0};
                switch (rotationMode.getMode()) {
                    case "Watchdog":
                        rotations = new float[]{MovementUtils.getMoveYaw(e.getYaw()) - 180, y};
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    case "NCP":
                        float prevYaw = cachedRots[0];
                        if ((blockCache = ScaffoldUtils.getBlockInfo()) == null) {
                            blockCache = lastBlockCache;
                        }
                        if (blockCache != null && (mc.thePlayer.ticksExisted % 3 == 0
                                || mc.theWorld.getBlockState(new BlockPos(e.getX(), ScaffoldUtils.getYLevel(), e.getZ())).getBlock() == Blocks.air)) {
                            cachedRots = RotationUtils.getRotations(blockCache.getPosition(), blockCache.getFacing());
                        }
                        if ((mc.thePlayer.onGround || (MovementUtils.isMoving() && tower.isEnabled() && mc.gameSettings.keyBindJump.isKeyDown())) && Math.abs(cachedRots[0] - prevYaw) >= 90) {
                            cachedRots[0] = MovementUtils.getMoveYaw(e.getYaw()) - 180;
                        }
                        rotations = cachedRots;
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    case "Back":
                        rotations = new float[]{MovementUtils.getMoveYaw(e.getYaw()) - 180, 77};
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    case "Down":
                        e.setPitch(90);
                        break;
                    case "45":
                        float val;
                        if (MovementUtils.isMoving()) {
                            float f = MovementUtils.getMoveYaw(e.getYaw()) - 180;
                            float[] numbers = new float[]{-135, -90, -45, 0, 45, 90, 135, 180};
                            float lastDiff = 999;
                            val = f;
                            for (float v : numbers) {
                                float diff = Math.abs(v - f);
                                if (diff < lastDiff) {
                                    lastDiff = diff;
                                    val = v;
                                }
                            }
                        } else {
                            val = rotations[0];
                        }
                        rotations = new float[]{
                                (val + MathHelper.wrapAngleTo180_float(mc.thePlayer.prevRotationYawHead)) / 2.0F,
                                (77 + MathHelper.wrapAngleTo180_float(mc.thePlayer.prevRotationPitchHead)) / 2.0F};
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    case "Enum":
                        if (lastBlockCache != null) {
                            float yaw = RotationUtils.getEnumRotations(lastBlockCache.getFacing());
                            e.setRotations(yaw, 77);
                        } else {
                            e.setRotations(mc.thePlayer.rotationYaw + 180, 77);
                        }
                        break;
                    case "0":
                        e.setRotations(0, 0);
                        break;
                }
                RotationUtils.setVisualRotations(e);
            }

            // Speed 2 Slowdown
            if (speedSlowdown.isEnabled() && mc.thePlayer.isPotionActive(Potion.moveSpeed) && !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround) {
                MovementUtils.setSpeed(speedSlowdownAmount.getValue());
            }

            if (sneak.isEnabled()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);

            // Save ground Y level for keep Y
            if (mc.thePlayer.onGround) {
                keepYCoord = Math.floor(mc.thePlayer.posY - 1.0);
            }

            if (tower.isEnabled() && mc.gameSettings.keyBindJump.isKeyDown()) {
                double centerX = Math.floor(e.getX()) + 0.5, centerZ = Math.floor(e.getZ()) + 0.5;
                switch (towerMode.getMode()) {
                    case "Vanilla":
                        mc.thePlayer.motionY = 0.42f;
                        break;
                    case "Verus":
                        if (mc.thePlayer.ticksExisted % 2 == 0)
                            mc.thePlayer.motionY = 0.42f;
                        break;
                    case "Watchdog":
                        if (!MovementUtils.isMoving() && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).down()).getBlock() != Blocks.air && lastBlockCache != null) {
                            if (mc.thePlayer.ticksExisted % 6 == 0) {
                                e.setX(centerX + 0.1);
                                e.setZ(centerZ + 0.1);
                            } else {
                                e.setX(centerX - 0.1);
                                e.setZ(centerZ - 0.1);
                            }
                            MovementUtils.setSpeed(0);
                        }

                        mc.thePlayer.motionY = 0.3;
                        e.setOnGround(true);
                        break;
                    case "NCP":
                        if (!MovementUtils.isMoving() || MovementUtils.getSpeed() < 0.16) {
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.motionY = 0.42;
                            } else if (mc.thePlayer.motionY < 0.23) {
                                mc.thePlayer.setPosition(mc.thePlayer.posX, (int) mc.thePlayer.posY, mc.thePlayer.posZ);
                                mc.thePlayer.motionY = 0.42;
                            }
                        }
                        break;
                }
            }

            // Setting Block Cache
            blockCache = ScaffoldUtils.getBlockInfo();
            if (blockCache != null) {
                lastBlockCache = ScaffoldUtils.getBlockInfo();
            } else {
                return;
            }

            if (mc.thePlayer.ticksExisted % 4 == 0) {
                pre = true;
            }

            // Placing Blocks (Pre)
            if (placeType.is("Pre") || (placeType.is("Dynamic") && pre)) {
                if (place()) {
                    pre = false;
                }
            }
        } else {
            // Setting Item Slot
            if (!itemSpoof.isEnabled()) {
                mc.thePlayer.inventory.currentItem = slot;
            }

            // Placing Blocks (Post)
            if (placeType.is("Post") || (placeType.is("Dynamic") && !pre)) {
                place();
            }

            pre = false;
        }
    }

    private boolean place() {
        int slot = ScaffoldUtils.getBlockSlot();
        if (blockCache == null || lastBlockCache == null || slot == -1) return false;

        if (this.slot != slot) {
            this.slot = slot;
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(this.slot));
        }

        boolean placed = false;
        if (delayTimer.hasTimeElapsed(delay.getValue() * 1000)) {
            firstJump = false;
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                    mc.thePlayer.inventory.getStackInSlot(this.slot),
                    lastBlockCache.getPosition(), lastBlockCache.getFacing(),
                    ScaffoldUtils.getHypixelVec3(lastBlockCache))) {
                placed = true;
                y = MathUtils.getRandomInRange(79.5f, 83.5f);
                if (swing.isEnabled()) {
                    if (swingMode.is("Client")) {
                        mc.thePlayer.swingItem();
                    } else {
                        PacketUtils.sendPacket(new C0APacketAnimation());
                    }
                }
            }
            delayTimer.reset();
            blockCache = null;
        }
        return placed;
    }

    @Override
    public void onBlockPlaceable(BlockPlaceableEvent event) {
        if (placeType.is("Legit")) {
            place();
        }
    }

    @Override
    public void onTickEvent(TickEvent event) {
        if (mc.thePlayer == null) return;
        if (hideJump.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown() && MovementUtils.isMoving() && !mc.thePlayer.onGround && autoJump.isEnabled()) {
            mc.thePlayer.posY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
            mc.thePlayer.lastTickPosY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
            mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0.1F;
        }
        if (downwards.isEnabled()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            mc.thePlayer.movementInput.sneak = false;
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            if (!itemSpoof.isEnabled()) mc.thePlayer.inventory.currentItem = prevSlot;
            if (slot != mc.thePlayer.inventory.currentItem && itemSpoof.isEnabled())
                PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

            if (auto3rdPerson.isEnabled()) {
                mc.gameSettings.thirdPersonView = 0;
            }
            if (mc.thePlayer.isSneaking() && sneak.isEnabled())
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindSneak));
        }
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        lastBlockCache = null;
        if (mc.thePlayer != null) {
            prevSlot = mc.thePlayer.inventory.currentItem;
            slot = mc.thePlayer.inventory.currentItem;
            if (mc.thePlayer.isSprinting() && sprint.isEnabled() && sprintMode.is("Cancel")) {
                PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            if (auto3rdPerson.isEnabled()) {
                mc.gameSettings.thirdPersonView = 1;
            }
        }
        firstJump = true;
        speed = 1.1f;
        timerUtil.reset();
        jumpTimer = 0;
        y = 80;
        super.onEnable();
    }

    public void renderCounterBlur() {
        if (!enabled && anim.isDone()) return;
        int slot = ScaffoldUtils.getBlockSlot();
        ItemStack heldItem = slot == -1 ? null : mc.thePlayer.inventory.mainInventory[slot];
        int count = slot == -1 ? 0 : ScaffoldUtils.getBlockCount();
        String countStr = String.valueOf(count);
        IFontRenderer fr = mc.fontRendererObj;
        ScaledResolution sr = new ScaledResolution(mc);
        int color;
        float x, y;
        String str = countStr + " block" + (count != 1 ? "s" : "");
        float output = anim.getOutput().floatValue();
        switch (countMode.getMode()) {
            case "Tenacity":
                float blockWH = heldItem != null ? 15 : -2;
                int spacing = 3;
                String text = "§l" + countStr + "§r block" + (count != 1 ? "s" : "");
                float textWidth = tenacityFont18.getStringWidth(text);

                float totalWidth = ((textWidth + blockWH + spacing) + 6) * output;
                x = sr.getScaledWidth() / 2f - (totalWidth / 2f);
                y = sr.getScaledHeight() - (sr.getScaledHeight() / 2f - 20);
                float height = 20;
                RenderUtil.scissorStart(x - 1.5, y - 1.5, totalWidth + 3, height + 3);

                RoundedUtil.drawRound(x, y, totalWidth, height, 5, Color.BLACK);
                RenderUtil.scissorEnd();
                break;
            case "Basic":
                x = sr.getScaledWidth() / 2F - fr.getStringWidth(str) / 2F + 1;
                y = sr.getScaledHeight() / 2F + 10;
                RenderUtil.scaleStart(sr.getScaledWidth() / 2.0F, y + fr.FONT_HEIGHT / 2.0F, output);
                fr.drawStringWithShadow(str, x, y, 0x000000);
                RenderUtil.scaleEnd();
                break;
            case "Polar":
                x = sr.getScaledWidth() / 2F - fr.getStringWidth(countStr) / 2F + (heldItem != null ? 6 : 1);
                y = sr.getScaledHeight() / 2F + 10;

                GlStateManager.pushMatrix();
                RenderUtil.fixBlendIssues();
                GL11.glTranslatef(x + (heldItem == null ? 1 : 0), y, 1);
                GL11.glScaled(anim.getOutput().floatValue(), anim.getOutput().floatValue(), 1);
                GL11.glTranslatef(-x - (heldItem == null ? 1 : 0), -y, 1);

                fr.drawOutlinedString(countStr, x, y, ColorUtil.applyOpacity(0x000000, output), true);

                if (heldItem != null) {
                    double scale = 0.7;
                    GlStateManager.color(1, 1, 1, 1);
                    GlStateManager.scale(scale, scale, scale);
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                            heldItem,
                            (int) ((sr.getScaledWidth() / 2F - fr.getStringWidth(countStr) / 2F - 7) / scale),
                            (int) ((sr.getScaledHeight() / 2F + 8.5F) / scale)
                    );
                    RenderHelper.disableStandardItemLighting();
                }
                GlStateManager.popMatrix();
                break;
        }
    }

    public void renderCounter() {
        anim.setDirection(enabled ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!enabled && anim.isDone()) return;
        int slot = ScaffoldUtils.getBlockSlot();
        ItemStack heldItem = slot == -1 ? null : mc.thePlayer.inventory.mainInventory[slot];
        int count = slot == -1 ? 0 : ScaffoldUtils.getBlockCount();
        String countStr = String.valueOf(count);
        IFontRenderer fr = mc.fontRendererObj;
        ScaledResolution sr = new ScaledResolution(mc);
        int color;
        float x, y;
        String str = countStr + " block" + (count != 1 ? "s" : "");
        float output = anim.getOutput().floatValue();
        switch (countMode.getMode()) {
            case "Tenacity":
                float blockWH = heldItem != null ? 15 : -2;
                int spacing = 3;
                String text = "§l" + countStr + "§r block" + (count != 1 ? "s" : "");
                float textWidth = tenacityFont18.getStringWidth(text);

                float totalWidth = ((textWidth + blockWH + spacing) + 6) * output;
                x = sr.getScaledWidth() / 2f - (totalWidth / 2f);
                y = sr.getScaledHeight() - (sr.getScaledHeight() / 2f - 20);
                float height = 20;
                RenderUtil.scissorStart(x - 1.5, y - 1.5, totalWidth + 3, height + 3);

                RoundedUtil.drawRound(x, y, totalWidth, height, 5, ColorUtil.tripleColor(20, .45f));

                tenacityFont18.drawString(text, x + 3 + blockWH + spacing, y + tenacityFont18.getMiddleOfBox(height) + .5f, -1);

                if (heldItem != null) {
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(heldItem, (int) x + 3, (int) (y + 10 - (blockWH / 2)));
                    RenderHelper.disableStandardItemLighting();
                }
                RenderUtil.scissorEnd();
                break;
            case "Basic":
                x = sr.getScaledWidth() / 2F - fr.getStringWidth(str) / 2F + 1;
                y = sr.getScaledHeight() / 2F + 10;
                RenderUtil.scaleStart(sr.getScaledWidth() / 2.0F, y + fr.FONT_HEIGHT / 2.0F, output);
                fr.drawStringWithShadow(str, x, y, -1);
                RenderUtil.scaleEnd();
                break;
            case "Polar":
                color = count < 24 ? 0xFFFF5555 : count < 128 ? 0xFFFFFF55 : 0xFF55FF55;
                x = sr.getScaledWidth() / 2F - fr.getStringWidth(countStr) / 2F + (heldItem != null ? 6 : 1);
                y = sr.getScaledHeight() / 2F + 10;

                GlStateManager.pushMatrix();
                RenderUtil.fixBlendIssues();
                GL11.glTranslatef(x + (heldItem == null ? 1 : 0), y, 1);
                GL11.glScaled(anim.getOutput().floatValue(), anim.getOutput().floatValue(), 1);
                GL11.glTranslatef(-x - (heldItem == null ? 1 : 0), -y, 1);

                fr.drawOutlinedString(countStr, x, y, ColorUtil.applyOpacity(color, output), true);

                if (heldItem != null) {
                    double scale = 0.7;
                    GlStateManager.color(1, 1, 1, 1);
                    GlStateManager.scale(scale, scale, scale);
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                            heldItem,
                            (int) ((sr.getScaledWidth() / 2F - fr.getStringWidth(countStr) / 2F - 7) / scale),
                            (int) ((sr.getScaledHeight() / 2F + 8.5F) / scale)
                    );
                    RenderHelper.disableStandardItemLighting();
                }
                GlStateManager.popMatrix();
                break;
        }
    }

    @Override
    public void onPacketSendEvent(PacketSendEvent e) {
        if (e.getPacket() instanceof C0BPacketEntityAction
                && ((C0BPacketEntityAction) e.getPacket()).getAction() == C0BPacketEntityAction.Action.START_SPRINTING
                && sprint.isEnabled() && sprintMode.is("Cancel")) {
            e.cancel();
        }
        if (e.getPacket() instanceof C09PacketHeldItemChange && itemSpoof.isEnabled()) {
            e.cancel();
        }
    }

    @Override
    public void onSafeWalkEvent(SafeWalkEvent event) {
        if ((safewalk.isEnabled() && !isDownwards()) || ScaffoldUtils.getBlockCount() == 0) {
            event.setSafe(true);
        }
    }

    public static boolean isDownwards() {
        return downwards.isEnabled() && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak);
    }

}
