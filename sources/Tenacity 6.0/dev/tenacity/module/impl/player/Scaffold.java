// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.event.impl.player.SafeWalkEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.animations.Direction;
import net.minecraft.client.gui.IFontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import dev.tenacity.utils.render.ColorUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.event.impl.player.BlockPlaceableEvent;
import net.minecraft.network.play.client.C0APacketAnimation;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.entity.Entity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import dev.tenacity.utils.player.RotationUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.module.Category;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.util.MouseFilter;
import dev.tenacity.utils.player.ScaffoldUtils;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class Scaffold extends Module
{
    private final ModeSetting countMode;
    private final ModeSetting rotationMode;
    private final ModeSetting placeType;
    public static ModeSetting keepYMode;
    public static ModeSetting sprintMode;
    public static ModeSetting towerMode;
    public static ModeSetting swingMode;
    public static NumberSetting delay;
    private final NumberSetting timer;
    public static final BooleanSetting auto3rdPerson;
    public static final BooleanSetting speedSlowdown;
    public static final NumberSetting speedSlowdownAmount;
    public static final BooleanSetting itemSpoof;
    public static final BooleanSetting downwards;
    public static final BooleanSetting safeWalk;
    private final BooleanSetting sneak;
    public static final BooleanSetting tower;
    private final NumberSetting towerTimer;
    private final BooleanSetting swing;
    private final BooleanSetting autoJump;
    private final BooleanSetting hideJump;
    private final BooleanSetting baseSpeed;
    private ScaffoldUtils.BlockCache blockCache;
    private ScaffoldUtils.BlockCache lastBlockCache;
    private float y;
    private float speed;
    private final MouseFilter pitchMouseFilter;
    private final TimerUtil delayTimer;
    private final TimerUtil timerUtil;
    public static double keepYCoord;
    private boolean shouldSendPacket;
    private boolean shouldTower;
    private boolean firstJump;
    private boolean pre;
    private int jumpTimer;
    private int slot;
    private int prevSlot;
    private float[] cachedRots;
    private final Animation anim;
    
    public Scaffold() {
        super("Scaffold", "Scaffold", Category.PLAYER, "Automatically places blocks under you");
        this.countMode = new ModeSetting("Block Counter", "Tenacity", new String[] { "None", "Tenacity", "Rise 6", "Basic", "Polar" });
        this.rotationMode = new ModeSetting("Rotation Mode", "Watchdog", new String[] { "Off", "Watchdog", "NCP", "Back", "45", "Enum", "Down", "0" });
        this.placeType = new ModeSetting("Place Type", "Post", new String[] { "Pre", "Post", "Legit", "Dynamic" });
        this.timer = new NumberSetting("Timer", 1.0, 5.0, 0.1, 0.1);
        this.sneak = new BooleanSetting("Sneak", false);
        this.towerTimer = new NumberSetting("Tower Timer Boost", 1.2, 5.0, 0.1, 0.1);
        this.swing = new BooleanSetting("Swing", true);
        this.autoJump = new BooleanSetting("Auto Jump", false);
        this.hideJump = new BooleanSetting("Hide Jump", false);
        this.baseSpeed = new BooleanSetting("Base Speed", false);
        this.pitchMouseFilter = new MouseFilter();
        this.delayTimer = new TimerUtil();
        this.timerUtil = new TimerUtil();
        this.cachedRots = new float[2];
        this.anim = new DecelerateAnimation(250, 1.0);
        this.addSettings(this.countMode, this.rotationMode, this.placeType, Scaffold.keepYMode, Scaffold.sprintMode, Scaffold.towerMode, Scaffold.swingMode, Scaffold.delay, this.timer, Scaffold.auto3rdPerson, Scaffold.speedSlowdown, Scaffold.speedSlowdownAmount, Scaffold.itemSpoof, Scaffold.downwards, Scaffold.safeWalk, this.sneak, Scaffold.tower, this.towerTimer, this.swing, this.autoJump, this.hideJump, this.baseSpeed);
        Scaffold.towerMode.addParent(Scaffold.tower, ParentAttribute.BOOLEAN_CONDITION);
        Scaffold.swingMode.addParent(this.swing, ParentAttribute.BOOLEAN_CONDITION);
        this.towerTimer.addParent(Scaffold.tower, ParentAttribute.BOOLEAN_CONDITION);
        this.hideJump.addParent(this.autoJump, ParentAttribute.BOOLEAN_CONDITION);
        Scaffold.speedSlowdownAmount.addParent(Scaffold.speedSlowdown, ParentAttribute.BOOLEAN_CONDITION);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
            Scaffold.mc.timer.timerSpeed = this.timer.getValue().floatValue();
        }
        else {
            Scaffold.mc.timer.timerSpeed = (Scaffold.tower.isEnabled() ? this.towerTimer.getValue().floatValue() : 1.0f);
        }
        if (e.isPre()) {
            if (this.baseSpeed.isEnabled()) {
                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 0.7);
            }
            if (this.autoJump.isEnabled() && Scaffold.mc.thePlayer.onGround && MovementUtils.isMoving() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                Scaffold.mc.thePlayer.jump();
            }
            if (Scaffold.sprintMode.is("Watchdog") && Scaffold.mc.thePlayer.onGround && MovementUtils.isMoving() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !isDownwards() && Scaffold.mc.thePlayer.isSprinting()) {
                final double[] offset = MathUtils.yawPos(Scaffold.mc.thePlayer.getDirection(), MovementUtils.getSpeed() / 2.0f);
                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.mc.thePlayer.posX - offset[0], Scaffold.mc.thePlayer.posY, Scaffold.mc.thePlayer.posZ - offset[1], true));
            }
            if (!this.rotationMode.is("Off")) {
                float[] rotations = { 0.0f, 0.0f };
                final String mode = this.rotationMode.getMode();
                switch (mode) {
                    case "Watchdog": {
                        rotations = new float[] { MovementUtils.getMoveYaw(e.getYaw()) - 180.0f, this.y };
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    }
                    case "NCP": {
                        final float prevYaw = this.cachedRots[0];
                        final ScaffoldUtils.BlockCache blockInfo = ScaffoldUtils.getBlockInfo();
                        this.blockCache = blockInfo;
                        if (blockInfo == null) {
                            this.blockCache = this.lastBlockCache;
                        }
                        if (this.blockCache != null && (Scaffold.mc.thePlayer.ticksExisted % 3 == 0 || Scaffold.mc.theWorld.getBlockState(new BlockPos(e.getX(), ScaffoldUtils.getYLevel(), e.getZ())).getBlock() == Blocks.air)) {
                            this.cachedRots = RotationUtils.getRotations(this.blockCache.getPosition(), this.blockCache.getFacing());
                        }
                        if ((Scaffold.mc.thePlayer.onGround || (MovementUtils.isMoving() && Scaffold.tower.isEnabled() && Scaffold.mc.gameSettings.keyBindJump.isKeyDown())) && Math.abs(this.cachedRots[0] - prevYaw) >= 90.0f) {
                            this.cachedRots[0] = MovementUtils.getMoveYaw(e.getYaw()) - 180.0f;
                        }
                        rotations = this.cachedRots;
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    }
                    case "Back": {
                        rotations = new float[] { MovementUtils.getMoveYaw(e.getYaw()) - 180.0f, 77.0f };
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    }
                    case "Down": {
                        e.setPitch(90.0f);
                        break;
                    }
                    case "45": {
                        float val;
                        if (MovementUtils.isMoving()) {
                            final float f = MovementUtils.getMoveYaw(e.getYaw()) - 180.0f;
                            final float[] numbers = { -135.0f, -90.0f, -45.0f, 0.0f, 45.0f, 90.0f, 135.0f, 180.0f };
                            float lastDiff = 999.0f;
                            val = f;
                            for (final float v : numbers) {
                                final float diff = Math.abs(v - f);
                                if (diff < lastDiff) {
                                    lastDiff = diff;
                                    val = v;
                                }
                            }
                        }
                        else {
                            val = rotations[0];
                        }
                        rotations = new float[] { (val + MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.prevRotationYawHead)) / 2.0f, (77.0f + MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.prevRotationPitchHead)) / 2.0f };
                        e.setRotations(rotations[0], rotations[1]);
                        break;
                    }
                    case "Enum": {
                        if (this.lastBlockCache != null) {
                            final float yaw = RotationUtils.getEnumRotations(this.lastBlockCache.getFacing());
                            e.setRotations(yaw, 77.0f);
                            break;
                        }
                        e.setRotations(Scaffold.mc.thePlayer.rotationYaw + 180.0f, 77.0f);
                        break;
                    }
                    case "0": {
                        e.setRotations(0.0f, 0.0f);
                        break;
                    }
                }
                RotationUtils.setVisualRotations(e);
            }
            if (Scaffold.speedSlowdown.isEnabled() && Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && Scaffold.mc.thePlayer.onGround) {
                MovementUtils.setSpeed(Scaffold.speedSlowdownAmount.getValue());
            }
            if (this.sneak.isEnabled()) {
                KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
            if (Scaffold.mc.thePlayer.onGround) {
                Scaffold.keepYCoord = Math.floor(Scaffold.mc.thePlayer.posY - 1.0);
            }
            if (Scaffold.tower.isEnabled() && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                final double centerX = Math.floor(e.getX()) + 0.5;
                final double centerZ = Math.floor(e.getZ()) + 0.5;
                final String mode2 = Scaffold.towerMode.getMode();
                switch (mode2) {
                    case "Vanilla": {
                        Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                        break;
                    }
                    case "Verus": {
                        if (Scaffold.mc.thePlayer.ticksExisted % 2 == 0) {
                            Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                            break;
                        }
                        break;
                    }
                    case "Watchdog": {
                        if (!MovementUtils.isMoving() && Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer).down()).getBlock() != Blocks.air && this.lastBlockCache != null) {
                            if (Scaffold.mc.thePlayer.ticksExisted % 6 == 0) {
                                e.setX(centerX + 0.1);
                                e.setZ(centerZ + 0.1);
                            }
                            else {
                                e.setX(centerX - 0.1);
                                e.setZ(centerZ - 0.1);
                            }
                            MovementUtils.setSpeed(0.0);
                        }
                        Scaffold.mc.thePlayer.motionY = 0.3;
                        e.setOnGround(true);
                        break;
                    }
                    case "NCP": {
                        if (MovementUtils.isMoving() && MovementUtils.getSpeed() >= 0.16) {
                            break;
                        }
                        if (Scaffold.mc.thePlayer.onGround) {
                            Scaffold.mc.thePlayer.motionY = 0.42;
                            break;
                        }
                        if (Scaffold.mc.thePlayer.motionY < 0.23) {
                            Scaffold.mc.thePlayer.setPosition(Scaffold.mc.thePlayer.posX, (int)Scaffold.mc.thePlayer.posY, Scaffold.mc.thePlayer.posZ);
                            Scaffold.mc.thePlayer.motionY = 0.42;
                            break;
                        }
                        break;
                    }
                }
            }
            this.blockCache = ScaffoldUtils.getBlockInfo();
            if (this.blockCache == null) {
                return;
            }
            this.lastBlockCache = ScaffoldUtils.getBlockInfo();
            if (Scaffold.mc.thePlayer.ticksExisted % 4 == 0) {
                this.pre = true;
            }
            if ((this.placeType.is("Pre") || (this.placeType.is("Dynamic") && this.pre)) && this.place()) {
                this.pre = false;
            }
        }
        else {
            if (!Scaffold.itemSpoof.isEnabled()) {
                Scaffold.mc.thePlayer.inventory.currentItem = this.slot;
            }
            if (this.placeType.is("Post") || (this.placeType.is("Dynamic") && !this.pre)) {
                this.place();
            }
            this.pre = false;
        }
    }
    
    private boolean place() {
        final int slot = ScaffoldUtils.getBlockSlot();
        if (this.blockCache == null || this.lastBlockCache == null || slot == -1) {
            return false;
        }
        if (this.slot != slot) {
            this.slot = slot;
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(this.slot));
        }
        boolean placed = false;
        if (this.delayTimer.hasTimeElapsed(Scaffold.delay.getValue() * 1000.0)) {
            this.firstJump = false;
            if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.inventory.getStackInSlot(this.slot), this.lastBlockCache.getPosition(), this.lastBlockCache.getFacing(), ScaffoldUtils.getHypixelVec3(this.lastBlockCache))) {
                placed = true;
                this.y = MathUtils.getRandomInRange(79.5f, 83.5f);
                if (this.swing.isEnabled()) {
                    if (Scaffold.swingMode.is("Client")) {
                        Scaffold.mc.thePlayer.swingItem();
                    }
                    else {
                        PacketUtils.sendPacket(new C0APacketAnimation());
                    }
                }
            }
            this.delayTimer.reset();
            this.blockCache = null;
        }
        return placed;
    }
    
    @Override
    public void onBlockPlaceable(final BlockPlaceableEvent event) {
        if (this.placeType.is("Legit")) {
            this.place();
        }
    }
    
    @Override
    public void onTickEvent(final TickEvent event) {
        if (Scaffold.mc.thePlayer == null) {
            return;
        }
        if (this.hideJump.isEnabled() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && MovementUtils.isMoving() && !Scaffold.mc.thePlayer.onGround && this.autoJump.isEnabled()) {
            final EntityPlayerSP thePlayer = Scaffold.mc.thePlayer;
            thePlayer.posY -= Scaffold.mc.thePlayer.posY - Scaffold.mc.thePlayer.lastTickPosY;
            final EntityPlayerSP thePlayer2 = Scaffold.mc.thePlayer;
            thePlayer2.lastTickPosY -= Scaffold.mc.thePlayer.posY - Scaffold.mc.thePlayer.lastTickPosY;
            final EntityPlayerSP thePlayer3 = Scaffold.mc.thePlayer;
            final EntityPlayerSP thePlayer4 = Scaffold.mc.thePlayer;
            final float n = 0.1f;
            thePlayer4.cameraPitch = n;
            thePlayer3.cameraYaw = n;
        }
        if (Scaffold.downwards.isEnabled()) {
            KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode(), false);
            Scaffold.mc.thePlayer.movementInput.sneak = false;
        }
    }
    
    @Override
    public void onDisable() {
        if (Scaffold.mc.thePlayer != null) {
            if (!Scaffold.itemSpoof.isEnabled()) {
                Scaffold.mc.thePlayer.inventory.currentItem = this.prevSlot;
            }
            if (this.slot != Scaffold.mc.thePlayer.inventory.currentItem && Scaffold.itemSpoof.isEnabled()) {
                PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(Scaffold.mc.thePlayer.inventory.currentItem));
            }
            if (Scaffold.auto3rdPerson.isEnabled()) {
                Scaffold.mc.gameSettings.thirdPersonView = 0;
            }
            if (Scaffold.mc.thePlayer.isSneaking() && this.sneak.isEnabled()) {
                KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindSneak));
            }
        }
        Scaffold.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        this.lastBlockCache = null;
        if (Scaffold.mc.thePlayer != null) {
            this.prevSlot = Scaffold.mc.thePlayer.inventory.currentItem;
            this.slot = Scaffold.mc.thePlayer.inventory.currentItem;
            if (Scaffold.mc.thePlayer.isSprinting() && Scaffold.sprintMode.is("Cancel")) {
                PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            if (Scaffold.auto3rdPerson.isEnabled()) {
                Scaffold.mc.gameSettings.thirdPersonView = 1;
            }
        }
        this.firstJump = true;
        this.speed = 1.1f;
        this.timerUtil.reset();
        this.jumpTimer = 0;
        this.y = 80.0f;
        super.onEnable();
    }
    
    public void renderCounterBlur() {
        if (!this.enabled && this.anim.isDone()) {
            return;
        }
        final int slot = ScaffoldUtils.getBlockSlot();
        final ItemStack heldItem = (slot == -1) ? null : Scaffold.mc.thePlayer.inventory.mainInventory[slot];
        final int count = (slot == -1) ? 0 : ScaffoldUtils.getBlockCount();
        final String countStr = String.valueOf(count);
        final IFontRenderer fr = Scaffold.mc.fontRendererObj;
        final ScaledResolution sr = new ScaledResolution(Scaffold.mc);
        final String str = countStr + " block" + ((count != 1) ? "s" : "");
        final float output = this.anim.getOutput().floatValue();
        final String mode = this.countMode.getMode();
        switch (mode) {
            case "Tenacity": {
                final float blockWH = (heldItem != null) ? 15.0f : -2.0f;
                final int spacing = 3;
                final String text = "§l" + countStr + "§r block" + ((count != 1) ? "s" : "");
                final float textWidth = Scaffold.tenacityFont18.getStringWidth(text);
                final float totalWidth = (textWidth + blockWH + spacing + 6.0f) * output;
                final float x = sr.getScaledWidth() / 2.0f - totalWidth / 2.0f;
                final float y = sr.getScaledHeight() - (sr.getScaledHeight() / 2.0f - 20.0f);
                final float height = 20.0f;
                RenderUtil.scissorStart(x - 1.5, y - 1.5, totalWidth + 3.0f, height + 3.0f);
                RoundedUtil.drawRound(x, y, totalWidth, height, 5.0f, Color.BLACK);
                RenderUtil.scissorEnd();
                break;
            }
            case "Basic": {
                final float x = sr.getScaledWidth() / 2.0f - fr.getStringWidth(str) / 2.0f + 1.0f;
                final float y = sr.getScaledHeight() / 2.0f + 10.0f;
                RenderUtil.scaleStart(sr.getScaledWidth() / 2.0f, y + fr.FONT_HEIGHT / 2.0f, output);
                fr.drawStringWithShadow(str, x, y, 0);
                RenderUtil.scaleEnd();
                break;
            }
            case "Polar": {
                final float x = sr.getScaledWidth() / 2.0f - fr.getStringWidth(countStr) / 2.0f + ((heldItem != null) ? 6 : 1);
                final float y = sr.getScaledHeight() / 2.0f + 10.0f;
                GlStateManager.pushMatrix();
                RenderUtil.fixBlendIssues();
                GL11.glTranslatef(x + (float)((heldItem == null) ? 1 : 0), y, 1.0f);
                GL11.glScaled((double)this.anim.getOutput().floatValue(), (double)this.anim.getOutput().floatValue(), 1.0);
                GL11.glTranslatef(-x - (float)((heldItem == null) ? 1 : 0), -y, 1.0f);
                fr.drawOutlinedString(countStr, x, y, ColorUtil.applyOpacity(0, output), true);
                if (heldItem != null) {
                    final double scale = 0.7;
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.scale(scale, scale, scale);
                    RenderHelper.enableGUIStandardItemLighting();
                    Scaffold.mc.getRenderItem().renderItemAndEffectIntoGUI(heldItem, (int)((sr.getScaledWidth() / 2.0f - fr.getStringWidth(countStr) / 2.0f - 7.0f) / scale), (int)((sr.getScaledHeight() / 2.0f + 8.5f) / scale));
                    RenderHelper.disableStandardItemLighting();
                }
                GlStateManager.popMatrix();
                break;
            }
        }
    }
    
    public void renderCounter() {
        this.anim.setDirection(this.enabled ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.enabled && this.anim.isDone()) {
            return;
        }
        final int slot = ScaffoldUtils.getBlockSlot();
        final ItemStack heldItem = (slot == -1) ? null : Scaffold.mc.thePlayer.inventory.mainInventory[slot];
        final int count = (slot == -1) ? 0 : ScaffoldUtils.getBlockCount();
        final String countStr = String.valueOf(count);
        final IFontRenderer fr = Scaffold.mc.fontRendererObj;
        final ScaledResolution sr = new ScaledResolution(Scaffold.mc);
        final String str = countStr + " block" + ((count != 1) ? "s" : "");
        final float output = this.anim.getOutput().floatValue();
        final String mode = this.countMode.getMode();
        switch (mode) {
            case "Tenacity": {
                final float blockWH = (heldItem != null) ? 15.0f : -2.0f;
                final int spacing = 3;
                final String text = "§l" + countStr + "§r block" + ((count != 1) ? "s" : "");
                final float textWidth = Scaffold.tenacityFont18.getStringWidth(text);
                final float totalWidth = (textWidth + blockWH + spacing + 6.0f) * output;
                final float x = sr.getScaledWidth() / 2.0f - totalWidth / 2.0f;
                final float y = sr.getScaledHeight() - (sr.getScaledHeight() / 2.0f - 20.0f);
                final float height = 20.0f;
                RenderUtil.scissorStart(x - 1.5, y - 1.5, totalWidth + 3.0f, height + 3.0f);
                RoundedUtil.drawRound(x, y, totalWidth, height, 5.0f, ColorUtil.tripleColor(20, 0.45f));
                Scaffold.tenacityFont18.drawString(text, x + 3.0f + blockWH + spacing, y + Scaffold.tenacityFont18.getMiddleOfBox(height) + 0.5f, -1);
                if (heldItem != null) {
                    RenderHelper.enableGUIStandardItemLighting();
                    Scaffold.mc.getRenderItem().renderItemAndEffectIntoGUI(heldItem, (int)x + 3, (int)(y + 10.0f - blockWH / 2.0f));
                    RenderHelper.disableStandardItemLighting();
                }
                RenderUtil.scissorEnd();
                break;
            }
            case "Rise 6": {
                final float riseBlockWH = (heldItem != null) ? 15.0f : -2.0f;
                final int riseSpacing = 3;
                final String riseText = "§rAmount:§l " + countStr;
                final float riseTextWidth = Scaffold.tenacityFont18.getStringWidth(riseText);
                final float riseTotalWidth = (riseTextWidth + riseBlockWH + riseSpacing + 6.0f) * output;
                final float x = sr.getScaledWidth() / 2.0f - riseTotalWidth / 2.0f;
                final float y = sr.getScaledHeight() - (sr.getScaledHeight() / 2.0f - 20.0f);
                final float riseHeight = 20.0f;
                RenderUtil.scissorStart(x - 1.5, y - 1.5, riseTotalWidth + 3.0f, riseHeight + 3.0f);
                RoundedUtil.drawRound(x, y, riseTotalWidth, riseHeight, 5.0f, ColorUtil.tripleColor(20, 0.45f));
                Scaffold.tenacityFont18.drawString(riseText, x + 3.0f + riseBlockWH + riseSpacing, y + Scaffold.tenacityFont18.getMiddleOfBox(riseHeight) + 0.5f, ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), 255.0f).getRGB());
                if (heldItem != null) {
                    RenderHelper.enableGUIStandardItemLighting();
                    Scaffold.mc.getRenderItem().renderItemAndEffectIntoGUI(heldItem, (int)x + 3, (int)(y + 10.0f - riseBlockWH / 2.0f));
                    RenderHelper.disableStandardItemLighting();
                }
                RenderUtil.scissorEnd();
                break;
            }
            case "Basic": {
                final float x = sr.getScaledWidth() / 2.0f - fr.getStringWidth(str) / 2.0f + 1.0f;
                final float y = sr.getScaledHeight() / 2.0f + 10.0f;
                RenderUtil.scaleStart(sr.getScaledWidth() / 2.0f, y + fr.FONT_HEIGHT / 2.0f, output);
                fr.drawStringWithShadow(str, x, y, -1);
                RenderUtil.scaleEnd();
                break;
            }
            case "Polar": {
                final int color = (count < 24) ? -43691 : ((count < 128) ? -171 : -11141291);
                final float x = sr.getScaledWidth() / 2.0f - fr.getStringWidth(countStr) / 2.0f + ((heldItem != null) ? 6 : 1);
                final float y = sr.getScaledHeight() / 2.0f + 10.0f;
                GlStateManager.pushMatrix();
                RenderUtil.fixBlendIssues();
                GL11.glTranslatef(x + (float)((heldItem == null) ? 1 : 0), y, 1.0f);
                GL11.glScaled((double)this.anim.getOutput().floatValue(), (double)this.anim.getOutput().floatValue(), 1.0);
                GL11.glTranslatef(-x - (float)((heldItem == null) ? 1 : 0), -y, 1.0f);
                fr.drawOutlinedString(countStr, x, y, ColorUtil.applyOpacity(color, output), true);
                if (heldItem != null) {
                    final double scale = 0.7;
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.scale(scale, scale, scale);
                    RenderHelper.enableGUIStandardItemLighting();
                    Scaffold.mc.getRenderItem().renderItemAndEffectIntoGUI(heldItem, (int)((sr.getScaledWidth() / 2.0f - fr.getStringWidth(countStr) / 2.0f - 7.0f) / scale), (int)((sr.getScaledHeight() / 2.0f + 8.5f) / scale));
                    RenderHelper.disableStandardItemLighting();
                }
                GlStateManager.popMatrix();
                break;
            }
        }
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent e) {
        if (e.getPacket() instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)e.getPacket()).getAction() == C0BPacketEntityAction.Action.START_SPRINTING && Scaffold.sprintMode.is("Cancel")) {
            e.cancel();
        }
        if (e.getPacket() instanceof C09PacketHeldItemChange && Scaffold.itemSpoof.isEnabled()) {
            e.cancel();
        }
    }
    
    @Override
    public void onSafeWalkEvent(final SafeWalkEvent event) {
        if ((Scaffold.safeWalk.isEnabled() && !isDownwards()) || ScaffoldUtils.getBlockCount() == 0) {
            event.setSafe(true);
        }
    }
    
    public static boolean isDownwards() {
        return Scaffold.downwards.isEnabled() && GameSettings.isKeyDown(Scaffold.mc.gameSettings.keyBindSneak);
    }
    
    static {
        Scaffold.keepYMode = new ModeSetting("Keep Y", "Always", new String[] { "Always", "Speed Only", "Off" });
        Scaffold.sprintMode = new ModeSetting("Sprint", "Vanilla", new String[] { "Vanilla", "Watchdog", "Cancel", "Off" });
        Scaffold.towerMode = new ModeSetting("Tower Mode", "Watchdog", new String[] { "Vanilla", "NCP", "Watchdog", "Verus" });
        Scaffold.swingMode = new ModeSetting("Swing Mode", "Client", new String[] { "Client", "Silent" });
        Scaffold.delay = new NumberSetting("Delay", 0.0, 2.0, 0.0, 0.05);
        auto3rdPerson = new BooleanSetting("Auto 3rd Person", false);
        speedSlowdown = new BooleanSetting("Speed Slowdown", true);
        speedSlowdownAmount = new NumberSetting("Slowdown Amount", 0.1, 0.2, 0.01, 0.01);
        itemSpoof = new BooleanSetting("Item Spoof", false);
        downwards = new BooleanSetting("Downwards", false);
        safeWalk = new BooleanSetting("SafeWalk", false);
        tower = new BooleanSetting("Tower", false);
    }
}
