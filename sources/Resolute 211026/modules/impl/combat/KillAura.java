// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemSword;
import vip.Resolute.util.world.RandomUtil;
import vip.Resolute.modules.impl.movement.Scaffold;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.world.World;
import net.minecraft.client.gui.FontRenderer;
import me.tojatta.api.utilities.angle.Angle;
import java.util.Iterator;
import java.util.List;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.util.render.Render2DUtils;
import vip.Resolute.util.render.Translate;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.inventory.GuiInventory;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import vip.Resolute.events.impl.EventRender2D;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.tojatta.api.utilities.vector.impl.Vector3;
import me.tojatta.api.utilities.angle.AngleUtility;
import vip.Resolute.util.player.RotationUtils;
import net.minecraft.entity.Entity;
import java.util.Comparator;
import vip.Resolute.Resolute;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventEntityDamage;
import net.minecraft.network.play.client.C0APacketAnimation;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import net.minecraft.network.Packet;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import java.util.HashMap;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import java.util.Map;
import org.lwjgl.util.vector.Vector2f;
import net.minecraft.entity.EntityLivingBase;
import vip.Resolute.util.misc.TimerUtil;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import vip.Resolute.modules.Module;

public class KillAura extends Module
{
    private static final C07PacketPlayerDigging PLAYER_DIGGING;
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT;
    public static int waitTicks;
    private double x;
    private double y;
    private float lastHealth;
    private final TimerUtil attackTimer;
    public static EntityLivingBase target;
    public static boolean blocking;
    double healthBarWidth;
    private boolean entityInBlockRange;
    private Vector2f lastAngles;
    private final Map<EntityLivingBase, Double> entityDamageMap;
    private final Map<EntityLivingBase, Integer> entityArmorCache;
    private boolean hasEnemyBeenHit;
    public ModeSetting auraModeProp;
    public ModeSetting rotationModeProp;
    public ModeSetting sortMethodProp;
    public ModeSetting attackMethodProp;
    public NumberSetting minApsProp;
    public NumberSetting maxApsProp;
    public NumberSetting rangeProp;
    public BooleanSetting randomize;
    public NumberSetting randFactor;
    public BooleanSetting customPitchProp;
    public NumberSetting customPitchValueProp;
    public BooleanSetting autoblockProp;
    public NumberSetting autoBlockRangeProp;
    public static ModeSetting autoblockModeProp;
    public NumberSetting maxAngleProp;
    public BooleanSetting lockViewProp;
    public BooleanSetting throughWallsProp;
    public BooleanSetting keepSprintProp;
    public BooleanSetting raytraceProp;
    public BooleanSetting packetUpdateProp;
    public BooleanSetting indicatorProp;
    public ModeSetting indMode;
    public BooleanSetting targetHudProp;
    public ModeSetting targetHudModeProp;
    public ColorSetting firstColorProp;
    public ColorSetting secondColorProp;
    
    public KillAura() {
        super("KillAura", 47, "Automatically attacks a target in range", Category.COMBAT);
        this.lastHealth = 0.0f;
        this.attackTimer = new TimerUtil();
        this.lastAngles = new Vector2f(0.0f, 0.0f);
        this.entityDamageMap = new HashMap<EntityLivingBase, Double>();
        this.entityArmorCache = new HashMap<EntityLivingBase, Integer>();
        this.hasEnemyBeenHit = false;
        this.auraModeProp = new ModeSetting("Aura Mode", "Single", new String[] { "Single", "HVH5" });
        this.rotationModeProp = new ModeSetting("Rotation Mode", "Normal", new String[] { "Normal", "NCP", "Stepped", "Smooth", "None" });
        this.sortMethodProp = new ModeSetting("Sorting Mode", "Distance", new String[] { "Hurt Time", "Distance", "Health", "Angle", "Combined" });
        this.attackMethodProp = new ModeSetting("Attack Mode", "PRE", new String[] { "PRE", "POST" });
        this.minApsProp = new NumberSetting("Min APS", 10.0, 1.0, 20.0, 1.0);
        this.maxApsProp = new NumberSetting("Max APS", 12.0, 1.0, 20.0, 1.0);
        this.rangeProp = new NumberSetting("Range", 4.0, 3.0, 6.0, 0.1);
        this.randomize = new BooleanSetting("Randomize", true);
        this.randFactor = new NumberSetting("RAND Factor", 2.0, this.randomize::isEnabled, 0.1, 30.0, 0.1);
        this.customPitchProp = new BooleanSetting("Custom Pitch", false);
        this.customPitchValueProp = new NumberSetting("Custom Pitch Value", 90.0, this.customPitchProp::isEnabled, -90.0, 90.0, 1.0);
        this.autoblockProp = new BooleanSetting("Autoblock", true);
        this.autoBlockRangeProp = new NumberSetting("Block Range", 4.0, 3.0, 6.0, 0.1);
        this.maxAngleProp = new NumberSetting("NCP Step", 45.0, () -> this.rotationModeProp.is("NCP"), 1.0, 180.0, 1.0);
        this.lockViewProp = new BooleanSetting("Lock View", false);
        this.throughWallsProp = new BooleanSetting("Through Walls", true);
        this.keepSprintProp = new BooleanSetting("Keep Sprint", true);
        this.raytraceProp = new BooleanSetting("Ray Trace", false);
        this.packetUpdateProp = new BooleanSetting("Packet Update", false);
        this.indicatorProp = new BooleanSetting("Indicator", true);
        this.indMode = new ModeSetting("Indicator Mode", "Box", this.indicatorProp::isEnabled, new String[] { "Platform", "Box", "Diamond" });
        this.targetHudProp = new BooleanSetting("TargetHUD", true);
        this.targetHudModeProp = new ModeSetting("TargetHUD Mode", "Radium New", new String[] { "Exhibition", "Radium", "Radium New" });
        this.firstColorProp = new ColorSetting("First Color", new Color(0, 255, 98), () -> (this.targetHudModeProp.is("Radium") || this.targetHudModeProp.is("Radium New")) && this.targetHudProp.isEnabled());
        this.secondColorProp = new ColorSetting("Second Color", new Color(136, 0, 255), () -> (this.targetHudModeProp.is("Radium") || this.targetHudModeProp.is("Radium New")) && this.targetHudProp.isEnabled());
        this.addSettings(this.auraModeProp, this.rotationModeProp, this.sortMethodProp, this.attackMethodProp, this.minApsProp, this.maxApsProp, this.rangeProp, this.randomize, this.randFactor, this.customPitchProp, this.customPitchValueProp, this.autoblockProp, this.autoBlockRangeProp, KillAura.autoblockModeProp, this.maxAngleProp, this.lockViewProp, this.throughWallsProp, this.keepSprintProp, this.raytraceProp, this.packetUpdateProp, this.indicatorProp, this.indMode, this.targetHudProp, this.targetHudModeProp, this.firstColorProp, this.secondColorProp);
    }
    
    @Override
    public void onEnable() {
        if (KillAura.mc.thePlayer != null) {
            this.lastAngles.x = KillAura.mc.thePlayer.rotationYaw;
        }
    }
    
    @Override
    public void onDisable() {
        if (KillAura.blocking) {
            KillAura.blocking = false;
            KillAura.mc.getNetHandler().getNetworkManager().sendPacket(KillAura.PLAYER_DIGGING);
        }
        if (KillAura.mc.thePlayer != null) {
            this.lastAngles.x = KillAura.mc.thePlayer.rotationYaw;
        }
        KillAura.target = null;
        this.entityInBlockRange = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.auraModeProp.getMode());
        if (e instanceof EventPacket && ((EventPacket)e).getPacket() instanceof C0APacketAnimation) {
            this.attackTimer.reset();
        }
        if (e instanceof EventEntityDamage) {
            this.entityDamageMap.put((EntityLivingBase)((EventEntityDamage)e).getEntity(), ((EventEntityDamage)e).getDamage());
        }
        if (e instanceof EventMotion) {
            final EventMotion event = (EventMotion)e;
            if (e.isPre()) {
                this.entityInBlockRange = false;
                EntityLivingBase optimalTarget = null;
                final List<EntityLivingBase> entities = Resolute.getLivingEntities(this::isValid);
                if (entities.size() > 1) {
                    final String mode = this.sortMethodProp.getMode();
                    switch (mode) {
                        case "Hurt Time": {
                            entities.sort(SortingMethod.HURT_TIME.getSorter());
                            break;
                        }
                        case "Distance": {
                            entities.sort(SortingMethod.DISTANCE.getSorter());
                            break;
                        }
                        case "Health": {
                            entities.sort(SortingMethod.HEALTH.getSorter());
                            break;
                        }
                        case "Angle": {
                            entities.sort(SortingMethod.ANGLE.getSorter());
                            break;
                        }
                        case "Combined": {
                            entities.sort(SortingMethod.COMBINED.getSorter());
                            break;
                        }
                    }
                }
                for (final EntityLivingBase entity : entities) {
                    final float dist = KillAura.mc.thePlayer.getDistanceToEntity(entity);
                    if (!this.entityInBlockRange && dist < this.autoBlockRangeProp.getValue()) {
                        this.entityInBlockRange = true;
                    }
                    if (dist < this.rangeProp.getValue()) {
                        optimalTarget = entity;
                        break;
                    }
                }
                KillAura.target = optimalTarget;
                if (KillAura.blocking) {
                    KillAura.blocking = false;
                    if (this.isHoldingSword()) {
                        this.unblock();
                    }
                }
                if (!this.isOccupied()) {
                    if (optimalTarget != null) {
                        final String mode2 = this.rotationModeProp.getMode();
                        int n5 = -1;
                        switch (mode2.hashCode()) {
                            case -1955878649: {
                                if (mode2.equals("Normal")) {
                                    n5 = 0;
                                    break;
                                }
                                break;
                            }
                            case 77115: {
                                if (mode2.equals("NCP")) {
                                    n5 = 1;
                                    break;
                                }
                                break;
                            }
                            case -228901213: {
                                if (mode2.equals("Stepped")) {
                                    n5 = 2;
                                    break;
                                }
                                break;
                            }
                            case -1814666802: {
                                if (mode2.equals("Smooth")) {
                                    n5 = 3;
                                    break;
                                }
                                break;
                            }
                            case 2433880: {
                                if (mode2.equals("None")) {
                                    n5 = 4;
                                    break;
                                }
                                break;
                            }
                        }
                        float yaw = 0.0f;
                        float pitch = 0.0f;
                        switch (n5) {
                            case 0: {
                                final float[] rotations = RotationUtils.getRotationsToEntity(optimalTarget);
                                yaw = rotations[0];
                                pitch = rotations[1];
                                break;
                            }
                            case 1: {
                                final float[] rotations = getRotations(optimalTarget, event.getPrevYaw(), event.getPrevPitch(), (float)this.maxAngleProp.getValue());
                                yaw = rotations[0];
                                pitch = rotations[1];
                                break;
                            }
                            case 2: {
                                final float[] rotations = getRotations(KillAura.target);
                                yaw = rotations[0];
                                pitch = rotations[1];
                                break;
                            }
                            case 3: {
                                final AngleUtility angleUtil = new AngleUtility(10.0f, 190.0f, 10.0f, 10.0f);
                                final Vector3<Double> enemyCoords = new Vector3<Double>(KillAura.target.posX, KillAura.target.posY, KillAura.target.posZ);
                                final Vector3<Double> myCoords = new Vector3<Double>(KillAura.mc.thePlayer.posX, KillAura.mc.thePlayer.posY, KillAura.mc.thePlayer.posZ);
                                final Angle dstAngle = AngleUtility.calculateAngle(enemyCoords, myCoords);
                                yaw = dstAngle.getYaw();
                                pitch = dstAngle.getPitch();
                                break;
                            }
                            case 4: {
                                yaw = KillAura.mc.thePlayer.rotationYaw;
                                pitch = KillAura.mc.thePlayer.rotationPitch;
                                break;
                            }
                            default: {
                                yaw = KillAura.mc.thePlayer.rotationYaw;
                                pitch = KillAura.mc.thePlayer.rotationPitch;
                                break;
                            }
                        }
                        if (this.randomize.isEnabled()) {
                            if (pitch > 0.0f) {
                                pitch += (float)(Math.random() * this.randFactor.getValue());
                            }
                            else {
                                pitch -= (float)(Math.random() * this.randFactor.getValue());
                            }
                            final double yawRandom = Math.random() * this.randFactor.getValue();
                            if (yawRandom > yawRandom / 2.0) {
                                yaw += (float)yawRandom;
                            }
                            else {
                                yaw -= (float)yawRandom;
                            }
                        }
                        if (pitch > 90.0f) {
                            pitch = 90.0f;
                        }
                        else if (pitch < -90.0f) {
                            pitch = -90.0f;
                        }
                        event.setYaw(yaw);
                        if (this.customPitchProp.isEnabled()) {
                            event.setPitch((float)this.customPitchValueProp.getValue());
                        }
                        else {
                            event.setPitch(pitch);
                        }
                        if (this.lockViewProp.isEnabled()) {
                            KillAura.mc.thePlayer.rotationYaw = yaw;
                            KillAura.mc.thePlayer.rotationPitch = pitch;
                        }
                        if (this.packetUpdateProp.isEnabled()) {
                            KillAura.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
                        }
                        if (this.attackMethodProp.is("PRE") && this.checkWaitTicks()) {
                            this.tryAttack(event);
                        }
                    }
                    else {
                        this.hasEnemyBeenHit = false;
                    }
                }
            }
            else if (!this.isOccupied()) {
                if (KillAura.target != null && this.attackMethodProp.is("POST") && this.checkWaitTicks()) {
                    this.tryAttack(event);
                }
                if (this.entityInBlockRange && this.autoblockProp.isEnabled() && this.isHoldingSword()) {
                    this.setItemInUse();
                    if (!KillAura.blocking) {
                        this.block();
                        KillAura.blocking = true;
                    }
                }
            }
        }
        if (e instanceof EventRender2D) {
            if (KillAura.target != null) {
                if (this.targetHudProp.isEnabled()) {
                    if (this.targetHudModeProp.is("Radium")) {
                        int fadeColor = 0;
                        if (KillAura.target instanceof EntityPlayer) {
                            final ScaledResolution lr = new ScaledResolution(KillAura.mc);
                            final FontRenderer fontRenderer = KillAura.mc.fontRendererObj;
                            final float sWidth = (float)lr.getScaledWidth();
                            final float sHeight = (float)lr.getScaledHeight();
                            final float middleX = sWidth / 2.0f;
                            final float middleY = sHeight / 2.0f;
                            final float top = middleY + 20.0f;
                            final float xOffset = 0.0f;
                            String name;
                            if (KillAura.target instanceof EntityPlayer) {
                                name = ((EntityPlayer)KillAura.target).getGameProfile().getName();
                            }
                            else {
                                name = KillAura.target.getDisplayName().getUnformattedText();
                            }
                            final float modelWidth = 30.0f;
                            final float nameYOffset = 4.0f;
                            final float nameHeight = (float)fontRenderer.FONT_HEIGHT;
                            final float width = Math.max(120.0f, modelWidth + 4.0f + fontRenderer.getStringWidth(name) + 2.0f);
                            final float height = 50.0f;
                            final float half = width / 2.0f;
                            final float left = middleX - half + xOffset;
                            final float right = middleX + half + xOffset;
                            final float bottom = top + height;
                            Gui.drawRect(left, top, right, bottom, Integer.MIN_VALUE);
                            GL11.glDisable(3553);
                            GL11.glLineWidth(0.5f);
                            GL11.glColor3f(0.0f, 0.0f, 0.0f);
                            GL11.glBegin(2);
                            GL11.glVertex2f(left, top);
                            GL11.glVertex2f(left, bottom);
                            GL11.glVertex2f(right, bottom);
                            GL11.glVertex2f(right, top);
                            GL11.glEnd();
                            GL11.glEnable(3553);
                            final float textLeft = left + modelWidth;
                            fontRenderer.drawStringWithShadow(name, textLeft, top + nameYOffset, -1);
                            final float healthTextY = top + nameHeight + nameYOffset;
                            final float health = KillAura.target.getHealth();
                            final String healthText = String.format("%.1f", health);
                            final float scale = 2.0f;
                            final float healthTextHeight = fontRenderer.FONT_HEIGHT * scale;
                            final float healthPercentage = health / KillAura.target.getMaxHealth();
                            fadeColor = RenderUtils.fadeBetween(this.firstColorProp.getColor(), this.secondColorProp.getColor(), System.currentTimeMillis() % 3000L / 1500.0f);
                            final float downScale = 1.0f / scale;
                            GL11.glScalef(scale, scale, 1.0f);
                            fontRenderer.drawStringWithShadow(healthText, textLeft / scale, healthTextY / scale + 2.0f, fadeColor);
                            GL11.glScalef(downScale, downScale, 1.0f);
                            final float healthBarY = healthTextY + healthTextHeight + 2.0f;
                            final float healthBarHeight = 8.0f;
                            final float healthBarRight = right - 4.0f;
                            final float dif = healthBarRight - textLeft;
                            Gui.drawRect(textLeft, healthBarY, healthBarRight, healthBarY + healthBarHeight, 1342177280);
                            KillAura.target.healthProgressX = (float)RenderUtils.progressiveAnimation(KillAura.target.healthProgressX, healthPercentage, 1.0);
                            final Double lastDamage = this.entityDamageMap.get(KillAura.target);
                            final float healthWidth = dif * KillAura.target.healthProgressX;
                            final float healthBarEnd = textLeft + healthWidth;
                            if (lastDamage != null && lastDamage > 0.0) {
                                final float damage = lastDamage.floatValue();
                                final float damageWidth = dif * (damage / KillAura.target.getMaxHealth());
                                Gui.drawRect(healthBarEnd, healthBarY, Math.min(healthBarEnd + damageWidth, healthBarRight), healthBarY + healthBarHeight, RenderUtils.darker(fadeColor, 0.49f));
                            }
                            Gui.drawRect(textLeft, healthBarY, healthBarEnd, healthBarY + healthBarHeight, fadeColor);
                            GL11.glColor3f(1.0f, 1.0f, 1.0f);
                            GuiInventory.drawEntityOnScreen((int)(left + modelWidth / 2.0f), (int)bottom - 2, 23, 0.0f, 0.0f, KillAura.target);
                        }
                    }
                    if (this.targetHudModeProp.is("Astolfo")) {
                        final ScaledResolution scaledResolution = new ScaledResolution(KillAura.mc);
                        if (KillAura.target instanceof EntityPlayer) {
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(this.x, this.y, 0.0);
                            final int n2 = scaledResolution.getScaledWidth() / 2 + 300;
                            final int n3 = scaledResolution.getScaledHeight() / 2 + 200;
                            RenderUtils.drawRoundedRect2(n2 - 70.0f, n3 + 35.0f, n2 + 88.0f, n3 + 89.0f, 6.0f, new Color(0, 0, 0, 190).getRGB());
                            final float health2 = KillAura.target.getHealth();
                            final float healthPercentage2 = health2 / KillAura.target.getMaxHealth();
                            float scaledWidth = 0.0f;
                            if (healthPercentage2 != this.lastHealth) {
                                final float scaledHeight = healthPercentage2 - this.lastHealth;
                                scaledWidth = this.lastHealth;
                                this.lastHealth += scaledHeight / 8.0f;
                            }
                            RenderUtils.drawRoundedRect2(n2 - 36.0f, n3 + 78.0f, n2 - 36.0f + 120.0, n3 + 85.0f, 6.0, RenderUtils.pulseBrightness(new Color(14, 60, 190), 2, 2).getRGB());
                            if (healthPercentage2 * 100.0f <= 75.0f) {
                                RenderUtils.drawRoundedRect2(n2 - 36.0f, n3 + 78.0f, n2 - 36.0f + 126.0f * scaledWidth, n3 + 85.0f, 6.0f, RenderUtils.pulseBrightness(new Color(13, 108, 244), 2, 2).getRGB());
                                RenderUtils.drawRoundedRect2(n2 - 36.0f, n3 + 78.0f, n2 - 36.0f + 120.0f * scaledWidth, n3 + 85.0f, 6.0f, RenderUtils.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
                            }
                            else {
                                RenderUtils.drawRoundedRect2(n2 - 36.0f, n3 + 78.0f, n2 - 36.0f + 120.0f * scaledWidth, n3 + 85.0f, 6.0f, RenderUtils.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
                            }
                            final int x1 = n2 - 50;
                            final int i = n3 + 32;
                            GL11.glPushMatrix();
                            GlStateManager.translate((float)x1, (float)i, 1.0f);
                            GL11.glScaled(1.1, 1.1, 1.1);
                            GlStateManager.translate((float)(-x1), (float)(-i), 1.0f);
                            KillAura.mc.fontRendererObj.drawStringWithShadow(((EntityPlayer)KillAura.target).getGameProfile().getName(), x1 + 13.5f, i + 7.5f, -1);
                            GL11.glPopMatrix();
                            final int x2 = n2 - 64;
                            final int yAdd = n3 + 40;
                            GL11.glPushMatrix();
                            GlStateManager.translate((float)x2, (float)yAdd, 1.0f);
                            GL11.glScalef(2.0f, 2.0f, 2.0f);
                            GlStateManager.translate((float)(-x2), (float)(-yAdd), 1.0f);
                            KillAura.mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", KillAura.target.getHealth() / 2.0f) + " \u2764", x2 + 13.5f, yAdd + 7.5f, RenderUtils.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
                            GL11.glPopMatrix();
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            drawEntityOnScreen(n2 - 53, n3 + 85, 24, 25.0f, 25.0f, KillAura.target);
                            GlStateManager.popMatrix();
                        }
                    }
                    if (this.targetHudModeProp.is("Radium New") && KillAura.target instanceof EntityPlayer) {
                        final ScaledResolution lr2 = new ScaledResolution(KillAura.mc);
                        final FontRenderer fontRenderer2 = KillAura.mc.fontRendererObj;
                        final int sWidth2 = lr2.getScaledWidth();
                        final int sHeight2 = lr2.getScaledHeight();
                        final int middleX2 = sWidth2 / 2;
                        final int middleY2 = sHeight2 / 2 + 10;
                        final String name2 = ((EntityPlayer)KillAura.target).getGameProfile().getName();
                        final int width2 = Math.max(100, 32 + (int)Math.ceil(fontRenderer2.getStringWidth(name2) / 2) + 4);
                        final int half2 = width2 / 2;
                        final int left2 = middleX2 - half2;
                        final int right2 = middleX2 + half2;
                        final int top2 = middleY2 + 20;
                        final int bottom2 = top2 + 32;
                        RenderUtils.enableBlending();
                        final AbstractClientPlayer clientPlayer = (AbstractClientPlayer)KillAura.target;
                        GL11.glEnable(3553);
                        KillAura.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f);
                        float maxHealth1 = 0.125f;
                        GL11.glBegin(7);
                        GL11.glTexCoord2f(0.125f, 0.125f);
                        GL11.glVertex2i(left2, top2);
                        GL11.glTexCoord2f(0.125f, 0.25f);
                        GL11.glVertex2i(left2, bottom2);
                        GL11.glTexCoord2f(0.25f, 0.25f);
                        GL11.glVertex2i(left2 + 32, bottom2);
                        GL11.glTexCoord2f(0.25f, 0.125f);
                        GL11.glVertex2i(left2 + 32, top2);
                        GL11.glEnd();
                        GL11.glDisable(3042);
                        final float health3 = KillAura.target.getHealth();
                        maxHealth1 = KillAura.target.getMaxHealth();
                        final float healthPercentage3 = health3 / maxHealth1;
                        int fadeColor2 = RenderUtils.fadeBetween(this.firstColorProp.getColor(), this.secondColorProp.getColor());
                        fadeColor2 = RenderUtils.fadeBetween(fadeColor2, RenderUtils.darker(fadeColor2));
                        final int alphaInt = alphaToInt(0.6f, 0);
                        final int textAlpha = alphaToInt(0.6f, 70);
                        fadeColor2 += textAlpha << 24;
                        final int backgroundColor = alphaInt << 24;
                        Gui.drawRect(left2 + 32, top2, right2, bottom2, backgroundColor);
                        final float infoLeft = (float)(left2 + 32 + 2);
                        final float infoTop = (float)(top2 + 2);
                        final float scale2 = 0.5f;
                        GL11.glScalef(0.5f, 0.5f, 1.0f);
                        float infoypos = infoTop / 0.5f;
                        fontRenderer2.drawStringWithShadow(name2, infoLeft / 0.5f, infoypos, 16777215 + (textAlpha << 24));
                        infoypos += fontRenderer2.FONT_HEIGHT;
                        final String healthText2 = String.format("§FHP: §R%.1f", health3 / 2.0);
                        fontRenderer2.drawStringWithShadow(healthText2, infoLeft / 0.5f, infoypos, fadeColor2);
                        infoypos += fontRenderer2.FONT_HEIGHT;
                        final EntityPlayer player = (EntityPlayer)KillAura.target;
                        final int targetArmor = this.getOrCacheArmor(player);
                        final int localArmor = this.getOrCacheArmor(KillAura.mc.thePlayer);
                        char prefix;
                        if (targetArmor > localArmor) {
                            prefix = '4';
                        }
                        else if (targetArmor < localArmor) {
                            prefix = 'A';
                        }
                        else {
                            prefix = 'F';
                        }
                        final String armorText = String.format("§FArmor: §R%s%% §F/ §%s%s%%", targetArmor, prefix, Math.abs(targetArmor - localArmor));
                        fontRenderer2.drawStringWithShadow(armorText, infoLeft / 0.5f, infoypos, 5308415 + (textAlpha << 24));
                        GL11.glScalef(2.0f, 2.0f, 1.0f);
                        KillAura.target.healthProgressX = (float)RenderUtils.linearAnimation(KillAura.target.healthProgressX, healthPercentage3, 0.02);
                        final float healthBarRight2 = (float)(right2 - 2);
                        final float xDif = healthBarRight2 - infoLeft;
                        final float healthBarThickness = 4.0f;
                        final float healthBarEnd2 = infoLeft + xDif * KillAura.target.healthProgressX;
                        final float healthBarBottom = (float)(bottom2 - 2);
                        final float healthBarTop = healthBarBottom - 4.0f;
                        Gui.drawRect(infoLeft, healthBarTop, healthBarRight2, healthBarBottom, backgroundColor);
                        if (this.entityDamageMap.containsKey(KillAura.target)) {
                            final double lastDamage2 = this.entityDamageMap.get(KillAura.target);
                            if (lastDamage2 > 0.0) {
                                final double damageAsHealthBarWidth = xDif * (lastDamage2 / maxHealth1);
                                Gui.drawRect(healthBarEnd2, healthBarTop, Math.min(healthBarEnd2 + damageAsHealthBarWidth, healthBarRight2), healthBarBottom, RenderUtils.darker(fadeColor2));
                            }
                        }
                        Gui.drawRect(infoLeft, healthBarTop, healthBarEnd2, healthBarBottom, fadeColor2);
                    }
                    if (this.targetHudModeProp.is("Exhibition") && KillAura.target instanceof EntityPlayer) {
                        final float startX = 20.0f;
                        final ScaledResolution sr = new ScaledResolution(KillAura.mc);
                        final float x3 = sr.getScaledWidth() / 2.0f + 30.0f;
                        final float y = sr.getScaledHeight() / 2.0f + 30.0f;
                        final float healthRender = KillAura.target.getHealth();
                        double hpPercentage = healthRender / KillAura.target.getMaxHealth();
                        hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                        final double hpWidth = 60.0 * hpPercentage;
                        final String healthStr = String.valueOf((int)KillAura.target.getHealth() / 1.0f);
                        int xAdd = 0;
                        final double multiplier = 0.85;
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(multiplier, multiplier, multiplier);
                        if (KillAura.target.getCurrentArmor(3) != null) {
                            KillAura.mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(3), (int)((sr.getScaledWidth() / 2 + startX + 33.0f + xAdd) / multiplier), (int)((sr.getScaledHeight() / 2 + 56) / multiplier));
                            xAdd += 15;
                        }
                        if (KillAura.target.getCurrentArmor(2) != null) {
                            KillAura.mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(2), (int)((sr.getScaledWidth() / 2 + startX + 33.0f + xAdd) / multiplier), (int)((sr.getScaledHeight() / 2 + 56) / multiplier));
                            xAdd += 15;
                        }
                        if (KillAura.target.getCurrentArmor(1) != null) {
                            KillAura.mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(1), (int)((sr.getScaledWidth() / 2 + startX + 33.0f + xAdd) / multiplier), (int)((sr.getScaledHeight() / 2 + 56) / multiplier));
                            xAdd += 15;
                        }
                        if (KillAura.target.getCurrentArmor(0) != null) {
                            KillAura.mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(0), (int)((sr.getScaledWidth() / 2 + startX + 33.0f + xAdd) / multiplier), (int)((sr.getScaledHeight() / 2 + 56) / multiplier));
                            xAdd += 15;
                        }
                        if (KillAura.target.getHeldItem() != null) {
                            KillAura.mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getHeldItem(), (int)((sr.getScaledWidth() / 2 + startX + 33.0f + xAdd) / multiplier), (int)((sr.getScaledHeight() / 2 + 56) / multiplier));
                        }
                        GlStateManager.popMatrix();
                        this.healthBarWidth = Translate.animate(hpWidth, this.healthBarWidth, 0.1);
                        Gui.drawGradientRect(x3 - 23.5, y - 3.5, x3 + 105.5f, y + 42.4f, new Color(10, 10, 10, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
                        Gui.drawGradientRect(x3 - 23.0f, y - 3.2, x3 + 104.8f, y + 41.8f, new Color(40, 40, 40, 255).getRGB(), new Color(40, 40, 40, 255).getRGB());
                        Gui.drawGradientRect(x3 - 21.4, y - 1.5, x3 + 103.5f, y + 40.5f, new Color(74, 74, 74, 255).getRGB(), new Color(74, 74, 74, 255).getRGB());
                        Gui.drawGradientRect(x3 - 21.0f, y - 1.0f, x3 + 103.0f, y + 40.0f, new Color(32, 32, 32, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
                        Gui.drawRect(x3 + 25.0f, y + 11.0f, x3 + 87.0f, y + 14.29f, new Color(105, 105, 105, 40).getRGB());
                        Gui.drawRect(x3 + 25.0f, y + 11.0f, x3 + 27.0f + this.healthBarWidth, y + 14.29f, RenderUtils.getColorFromPercentage(KillAura.target.getHealth(), KillAura.target.getMaxHealth()));
                        KillAura.mc.fontRendererObj.drawStringWithShadow(KillAura.target.getName(), x3 + 24.8f, y + 1.9f, new Color(255, 255, 255).getRGB());
                        KillAura.mc.fontRendererObj.drawStringWithShadow("l   l   l   l   l   l   ", x3 + 27.5f, y + 10.2f, new Color(50, 50, 50).getRGB());
                        KillAura.mc.fontRendererObj.drawStringWithShadow("HP:" + healthStr, x3 - 11.2f + 44.0f - KillAura.mc.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 17.0f, -1);
                        Render2DUtils.drawFace((int)(x3 - 20.0f), (int)y, 8.0f, 8.0f, 8, 8, 40, 40, 64.0f, 64.0f, (AbstractClientPlayer)KillAura.target);
                    }
                }
            }
            else {
                this.healthBarWidth = 92.0;
            }
        }
        if (e instanceof EventRender3D) {
            if (KillAura.target != null && this.indicatorProp.isEnabled() && this.indMode.is("Platform")) {
                RenderUtils.drawAuraMark(KillAura.target, (KillAura.target.hurtTime > 3) ? new Color(0, 255, 88, 75) : new Color(235, 40, 40, 75));
            }
            if (KillAura.target != null && this.indicatorProp.isEnabled() && this.indMode.is("Diamond")) {
                RenderUtils.drawExhi(KillAura.target, (EventRender3D)e);
            }
            if (KillAura.target != null && this.indicatorProp.isEnabled() && this.indMode.is("Box")) {
                RenderUtils.drawPlatform(KillAura.target, (KillAura.target.hurtTime > 3) ? new Color(0, 255, 88, 75) : new Color(235, 40, 40, 75));
            }
        }
    }
    
    public void setItemInUse() {
        if (KillAura.autoblockModeProp.is("NCP")) {
            KillAura.mc.thePlayer.setItemInUse(KillAura.mc.thePlayer.getCurrentEquippedItem(), KillAura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
        }
        if (KillAura.autoblockModeProp.is("Watchdog")) {
            KillAura.mc.thePlayer.setItemInUse(KillAura.mc.thePlayer.getHeldItem(), 8100);
        }
        if (KillAura.autoblockModeProp.is("Vanilla")) {
            KillAura.mc.playerController.sendUseItem(KillAura.mc.thePlayer, KillAura.mc.theWorld, KillAura.mc.thePlayer.getHeldItem());
        }
    }
    
    public void block() {
        if (KillAura.autoblockModeProp.is("NCP")) {
            KillAura.mc.getNetHandler().getNetworkManager().sendPacket(KillAura.BLOCK_PLACEMENT);
        }
        if (KillAura.autoblockModeProp.is("Watchdog")) {
            KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(KillAura.target, KillAura.target.getPositionVector()));
            KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(KillAura.target, C02PacketUseEntity.Action.INTERACT));
            KillAura.mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(KillAura.mc.thePlayer.inventory.getCurrentItem()));
        }
    }
    
    public void unblock() {
        if (KillAura.autoblockModeProp.is("NCP")) {
            KillAura.mc.getNetHandler().getNetworkManager().sendPacket(KillAura.PLAYER_DIGGING);
        }
        if (KillAura.autoblockModeProp.is("Watchdog")) {
            KillAura.mc.getNetHandler().getNetworkManager().sendPacket(KillAura.PLAYER_DIGGING);
        }
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        if (!entity.isEntityAlive()) {
            return false;
        }
        if (entity.isInvisible() && !SkeetUI.isInvisibles()) {
            return false;
        }
        if (!entity.canEntityBeSeen(KillAura.mc.thePlayer) && !this.throughWallsProp.isEnabled()) {
            return false;
        }
        if (entity instanceof EntityOtherPlayerMP) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (!SkeetUI.isPlayers()) {
                return false;
            }
            if (AntiBot.enabled && AntiBot.watchdogBots.contains(player)) {
                return false;
            }
            if (!SkeetUI.isTeams() && isTeamMate(player)) {
                return false;
            }
        }
        else if (entity instanceof EntityVillager) {
            if (!SkeetUI.isVillagers()) {
                return false;
            }
        }
        else if (entity instanceof EntityMob) {
            if (!SkeetUI.isMobs()) {
                return false;
            }
        }
        else {
            if (!(entity instanceof EntityAnimal)) {
                return false;
            }
            if (!SkeetUI.isAnimals()) {
                return false;
            }
        }
        return KillAura.mc.thePlayer.getDistanceToEntity(entity) < Math.max(this.autoBlockRangeProp.getValue(), this.rangeProp.getValue());
    }
    
    private static float[] getRotations(final Entity entity) {
        final EntityPlayerSP player = KillAura.mc.thePlayer;
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        double yDist = entity.posY - player.posY;
        final double dist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final boolean close = dist < 2.0 && Math.abs(yDist) < 2.0;
        float pitch;
        if (close && playerEyePos > entityBB.minY) {
            pitch = 60.0f;
        }
        else {
            yDist = ((playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0));
            pitch = (float)(-(StrictMath.atan2(yDist, dist) * 57.29577951308232));
        }
        float yaw = (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f;
        if (close) {
            final int inc = (dist < 1.0) ? 180 : 90;
            yaw = (float)(Math.round(yaw / inc) * inc);
        }
        return new float[] { yaw, pitch };
    }
    
    private static float[] getRotations(final Entity entity, final float prevYaw, final float prevPitch, final float aimSpeed) {
        final EntityPlayerSP player = KillAura.mc.thePlayer;
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final double yDist = (playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0);
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float yaw = interpolateRotation(prevYaw, (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f, aimSpeed);
        final float pitch = interpolateRotation(prevPitch, (float)(-(StrictMath.atan2(yDist, fDist) * 57.29577951308232)), aimSpeed);
        return new float[] { yaw, pitch };
    }
    
    private static float interpolateRotation(final float prev, final float now, final float maxTurn) {
        float var4 = MathHelper.wrapAngleTo180_float(now - prev);
        if (var4 > maxTurn) {
            var4 = maxTurn;
        }
        if (var4 < -maxTurn) {
            var4 = -maxTurn;
        }
        return prev + var4;
    }
    
    private boolean isInMenu() {
        final GuiScreen currentScreen = KillAura.mc.currentScreen;
        return currentScreen != null && !(currentScreen instanceof SkeetUI);
    }
    
    private boolean isOccupied() {
        return this.isInMenu() || Scaffold.enabled;
    }
    
    private boolean checkWaitTicks() {
        if (KillAura.waitTicks > 0) {
            --KillAura.waitTicks;
            return false;
        }
        return true;
    }
    
    private void tryAttack(final EventMotion event) {
        if (this.isUsingItem()) {
            return;
        }
        final double min = this.minApsProp.getValue();
        final double max = this.minApsProp.getValue();
        double cps;
        if (min == max) {
            cps = min;
        }
        else {
            cps = RandomUtil.getRandomInRange(this.minApsProp.getValue(), this.maxApsProp.getValue());
        }
        if (this.auraModeProp.is("Single") && this.attackTimer.hasElapsed(1000L / (long)cps) && (!this.raytraceProp.isEnabled() || isLookingAtEntity(event.getYaw(), event.getPitch(), KillAura.target, this.rangeProp.getValue(), this.raytraceProp.isEnabled()))) {
            KillAura.mc.thePlayer.swingItem();
            KillAura.mc.getNetHandler().sendPacketNoEvent(new C02PacketUseEntity(KillAura.target, C02PacketUseEntity.Action.ATTACK));
            if (!this.keepSprintProp.isEnabled() && KillAura.mc.thePlayer.isSprinting()) {
                final EntityPlayerSP thePlayer = KillAura.mc.thePlayer;
                thePlayer.motionX *= 0.6;
                final EntityPlayerSP thePlayer2 = KillAura.mc.thePlayer;
                thePlayer2.motionZ *= 0.6;
                KillAura.mc.thePlayer.setSprinting(false);
            }
        }
        if (this.auraModeProp.is("HVH5")) {
            if (KillAura.target.hurtTime > 1) {
                this.hasEnemyBeenHit = true;
            }
            else {
                this.hasEnemyBeenHit = false;
            }
            if (this.attackTimer.hasElapsed(480L) || (!this.hasEnemyBeenHit && this.attackTimer.hasElapsed(1000L / (long)cps) && (!this.raytraceProp.isEnabled() || isLookingAtEntity(event.getYaw(), event.getPitch(), KillAura.target, this.rangeProp.getValue(), this.raytraceProp.isEnabled())))) {
                KillAura.mc.thePlayer.swingItem();
                KillAura.mc.getNetHandler().sendPacketNoEvent(new C02PacketUseEntity(KillAura.target, C02PacketUseEntity.Action.ATTACK));
                if (!this.keepSprintProp.isEnabled() && KillAura.mc.thePlayer.isSprinting()) {
                    final EntityPlayerSP thePlayer3 = KillAura.mc.thePlayer;
                    thePlayer3.motionX *= 0.6;
                    final EntityPlayerSP thePlayer4 = KillAura.mc.thePlayer;
                    thePlayer4.motionZ *= 0.6;
                    KillAura.mc.thePlayer.setSprinting(false);
                }
            }
        }
    }
    
    private boolean isUsingItem() {
        return KillAura.mc.thePlayer.isUsingItem() && !this.isHoldingSword();
    }
    
    private boolean isHoldingSword() {
        return KillAura.mc.thePlayer.getCurrentEquippedItem() != null && KillAura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }
    
    private static boolean isLookingAtEntity(final float yaw, final float pitch, final Entity entity, final double range, final boolean rayTrace) {
        final EntityPlayer player = KillAura.mc.thePlayer;
        final Vec3 src = KillAura.mc.thePlayer.getPositionEyes(1.0f);
        final Vec3 rotationVec = Entity.getVectorForRotation(pitch, yaw);
        final Vec3 dest = src.addVector(rotationVec.xCoord * range, rotationVec.yCoord * range, rotationVec.zCoord * range);
        final MovingObjectPosition obj = KillAura.mc.theWorld.rayTraceBlocks(src, dest, false, false, true);
        if (obj == null) {
            return false;
        }
        if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (rayTrace) {
                return false;
            }
            if (player.getDistanceToEntity(entity) > 3.0) {
                return false;
            }
        }
        return entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612).calculateIntercept(src, dest) != null;
    }
    
    public static double getEffectiveHealth(final EntityLivingBase entity) {
        return entity.getHealth() * (20.0 / entity.getTotalArmorValue());
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float f = ent.renderYawOffset;
        final float f2 = ent.rotationYaw;
        final float f3 = ent.rotationPitch;
        final float f4 = ent.prevRotationYawHead;
        final float f5 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f2;
        ent.rotationPitch = f3;
        ent.prevRotationYawHead = f4;
        ent.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    private int getOrCacheArmor(final EntityPlayer player) {
        final Integer cachedTargetArmor = this.entityArmorCache.get(player);
        if (cachedTargetArmor == null) {
            final int targetArmor = (int)Math.ceil(getTotalArmorProtection(player) / 20.0 * 100.0);
            this.entityArmorCache.put(player, targetArmor);
            return targetArmor;
        }
        return cachedTargetArmor;
    }
    
    public static double getTotalArmorProtection(final EntityPlayer player) {
        double totalArmor = 0.0;
        for (int i = 0; i < 4; ++i) {
            final ItemStack armorStack = player.getCurrentArmor(i);
            if (armorStack != null && armorStack.getItem() instanceof ItemArmor) {
                totalArmor += getDamageReduction(armorStack);
            }
        }
        return totalArmor;
    }
    
    public static double getDamageReduction(final ItemStack stack) {
        double reduction = 0.0;
        final ItemArmor armor = (ItemArmor)stack.getItem();
        reduction += armor.damageReduceAmount;
        if (stack.isItemEnchanted()) {
            reduction += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.25;
        }
        return reduction;
    }
    
    private static int alphaToInt(final float alpha, final int offset) {
        return Math.min(255, (int)Math.ceil(alpha * 255.0f) + offset);
    }
    
    public static boolean isTeamMate(final EntityPlayer entity) {
        final String entName = entity.getDisplayName().getFormattedText();
        final String playerName = KillAura.mc.thePlayer.getDisplayName().getFormattedText();
        return entName.length() >= 2 && playerName.length() >= 2 && entName.startsWith("§") && playerName.startsWith("§") && entName.charAt(1) == playerName.charAt(1);
    }
    
    static {
        KillAura.autoblockModeProp = new ModeSetting("Autoblock Mode", "NCP", new String[] { "NCP", "Watchdog", "Vanilla", "Fake" });
        PLAYER_DIGGING = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
        BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    }
    
    private enum SortingMethod
    {
        DISTANCE((Comparator<EntityLivingBase>)new DistanceSorting()), 
        HEALTH((Comparator<EntityLivingBase>)new HealthSorting()), 
        HURT_TIME((Comparator<EntityLivingBase>)new HurtTimeSorting()), 
        ANGLE((Comparator<EntityLivingBase>)new AngleSorting()), 
        COMBINED((Comparator<EntityLivingBase>)new CombinedSorting());
        
        private final Comparator<EntityLivingBase> sorter;
        
        private SortingMethod(final Comparator<EntityLivingBase> sorter) {
            this.sorter = sorter;
        }
        
        public Comparator<EntityLivingBase> getSorter() {
            return this.sorter;
        }
    }
    
    private static class AngleSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            final float yaw = Module.mc.thePlayer.currentEvent.getYaw();
            return Double.compare(Math.abs(RotationUtils.getYawToEntity(o1) - yaw), Math.abs(RotationUtils.getYawToEntity(o2) - yaw));
        }
    }
    
    private static class CombinedSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            int t1 = 0;
            SortingMethod[] values;
            for (int length = (values = SortingMethod.values()).length, i = 0; i < length; ++i) {
                final SortingMethod sortingMethod = values[i];
                final Comparator<EntityLivingBase> sorter = sortingMethod.getSorter();
                if (sorter != this) {
                    t1 += sorter.compare(o1, o2);
                }
            }
            return t1;
        }
    }
    
    private static class DistanceSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Double.compare(o1.getDistanceToEntity(Module.mc.thePlayer), o2.getDistanceToEntity(Module.mc.thePlayer));
        }
    }
    
    private static class HealthSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Double.compare(KillAura.getEffectiveHealth(o1), KillAura.getEffectiveHealth(o2));
        }
    }
    
    private static class HurtTimeSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Integer.compare(20 - o2.hurtResistantTime, 20 - o1.hurtResistantTime);
        }
    }
}
