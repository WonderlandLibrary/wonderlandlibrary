package de.tired.base.module.implementation.combat;

import de.tired.base.event.events.*;
import de.tired.util.animation.Animation;
import de.tired.util.animation.ColorAnimation;
import de.tired.util.animation.Easings;
import de.tired.base.annotations.ModuleAnnotation;
import de.tired.util.combat.RayCastUtil;
import de.tired.util.math.MathUtil;
import de.tired.util.math.TimerUtil;
import de.tired.util.player.StrafeUtil;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.util.hook.Rotations;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.combat.rotation.NewRotationUtil;
import de.tired.util.combat.rotation.Rotation;
import de.tired.util.combat.rotation.VectorRotation;
import de.tired.util.render.ColorUtil;
import de.tired.base.event.EventTarget;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import de.tired.util.render.StencilUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleAnnotation(name = "KillAura", key = Keyboard.KEY_NONE, category = ModuleCategory.COMBAT, clickG = "Attack players around you!")
public class KillAura extends Module {

    private int targetIndex;

    public Entity target;

    private final List<EntityLivingBase> targets = new ArrayList<>();

    private Animation healthAnimation = new Animation();

    private ColorAnimation colorAnimation = new ColorAnimation();

    private float[] randomRotationsNoValue = new float[2], lastSavedRandom = new float[2];

    public float[] serverRotations = new float[2];

    private Rotation vectorRotation;

    private final TimerUtil switchTimer = new TimerUtil(), delayTimer = new TimerUtil();

    public float[] animations = new float[3];

    private final ModeSetting killAuraMode = new ModeSetting("KillAuraMode", this, new String[]{"Single", "Switch"});

    public final ModeSetting attackMode = new ModeSetting("attackMode", this, new String[]{"RayCast", "ClickMouse", "SimplePacket"});

    private final ModeSetting sortMode = new ModeSetting("sortMode", this, new String[]{"Health", "Distance"});

    private final NumberSetting cps = new NumberSetting("CPS", this, 12, 1, 20, 1);

    public BooleanSetting randomCps = new BooleanSetting("randomCps", this, true);

    private final NumberSetting switchDelay = new NumberSetting("SwitchDelay", this, 60, 10, 1000, 1, () -> killAuraMode.getValue().equalsIgnoreCase("Switch"));

    public NumberSetting range = new NumberSetting("Range", this, 3, .1, 6, .1);
    public BooleanSetting rotations = new BooleanSetting("Rotations", this, true);

    public BooleanSetting smooth = new BooleanSetting("smooth", this, true);
    public BooleanSetting players = new BooleanSetting("Players", this, true);

    public BooleanSetting mobs = new BooleanSetting("Mobs", this, true);
    public BooleanSetting moreParticles = new BooleanSetting("moreParticles", this, true);

    public BooleanSetting targetHUD = new BooleanSetting("TargetHUD", this, true);

    public BooleanSetting walls = new BooleanSetting("walls", this, true);

    public BooleanSetting teams = new BooleanSetting("walls", this, true);

    public BooleanSetting animals = new BooleanSetting("animals", this, true);

    public BooleanSetting vecRangeCalc = new BooleanSetting("vecRangeCalc", this, true);

    public BooleanSetting noSprint = new BooleanSetting("noSprint", this, true);

    public BooleanSetting autoBlock = new BooleanSetting("autoBlock", this, true);

    public ModeSetting autoBlockMode = new ModeSetting("AutoBlockMode", this, new String[]{"RightClick", "BlockPlacement", "Legit", "Fake"}, () -> autoBlock.getValue());

    public BooleanSetting smartBlock = new BooleanSetting("smartBlock", this, true, () -> autoBlock.getValue());

    public BooleanSetting movementAdjustment = new BooleanSetting("movementAdjustment", this, true);

    public BooleanSetting jumpMovementAdjustment = new BooleanSetting("jumpMovementAdjustment", this, true, () -> movementAdjustment.getValue());

    public BooleanSetting silentMovementAdjustment = new BooleanSetting("SilentMovementAdjustment", this, true, () -> movementAdjustment.getValue());

    public BooleanSetting necessaryRotation = new BooleanSetting("necessaryRotation", this, true, () -> rotations.getValue());
    public BooleanSetting PointOfViewRotation = new BooleanSetting("PointOfViewRotation", this, true, () -> rotations.getValue());

    public BooleanSetting GCDFix = new BooleanSetting("GCDFix", this, true, () -> rotations.getValue());

    public BooleanSetting throughWallsRotation = new BooleanSetting("throughWallsRotation", this, true, () -> rotations.getValue());

    public BooleanSetting blockRaycast = new BooleanSetting("blockRaycast", this, true, () -> rotations.getValue());

    public BooleanSetting outrangeRotation = new BooleanSetting("outrangeRotation", this, true, () -> rotations.getValue());

    public ModeSetting heuristicsMode = new ModeSetting("heuristic Type", this, new String[]{"SpeedCalc", "SinCos"}, () -> targetHUD.getValue());

    public ModeSetting centerMode = new ModeSetting("centerMode", this, new String[]{"CenterLine", "CenterSimple"}, () -> rotations.getValue() && !outrangeRotation.getValue() && !heuristicsMode.getValue().equalsIgnoreCase("SinCos"));

    public ModeSetting targetHudType = new ModeSetting("targetHud Type", this, new String[]{"Simple", "Glow", "Clean"}, () -> targetHUD.getValue());

    public BooleanSetting predictRotation = new BooleanSetting("predictRotation", this, true, () -> rotations.getValue());

    public BooleanSetting randomRotations = new BooleanSetting("randomRotations", this, true, () -> rotations.getValue());

    public final BooleanSetting GCDA3 = new BooleanSetting("GCDA3", this, true, () -> rotations.getValue() && GCDFix.getValue());
    public final BooleanSetting golems = new BooleanSetting("golems", this, true);

    private NumberSetting randomSpeed = new NumberSetting("RandomSpeed", this, 3, 1, 10, 1, () -> randomRotations.getValue());

    public NumberSetting smoothing = new NumberSetting("smoothing", this, 30, 1, 100, .1, () -> rotations.getValue() && smooth.getValue());

    public BooleanSetting villager = new BooleanSetting("villager", this, true);


    public KillAura() {
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
    }

    private void executeAutoBlock() {
        if (target == null) return;
        if (autoBlock.getValue()) {
            switch (autoBlockMode.getValue()) {
                case "RightClick":
                    if (MC.thePlayer.ticksExisted % 2 == 0)
                        MC.rightClickMouse();
                    break;
                case "BlockPlacement": {
                    MC.thePlayer.itemInUseCount = MC.thePlayer.getHeldItem().getMaxItemUseDuration();
                    sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, MC.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    break;
                }
                case "Legit": {
                    if (MC.thePlayer.ticksExisted % 2 == 0) {
                        MC.rightClickMouse();
                        MC.thePlayer.itemInUseCount = MC.thePlayer.getHeldItem().getMaxItemUseDuration();
                        sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, MC.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                        break;
                    }
                }
            }
        }
    }

    @EventTarget
    public void onAttack(AttackingEvent e) {
        if (!allowClick()) return;
        switch (attackMode.getValue()) {
            case "RayCast": {
                if (RayCastUtil.raycastEntity(range.getValue(), serverRotations) != null) {
                    MC.thePlayer.swingItem();
                    MC.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }
                break;
            }
            case "ClickMouse": {
                if (MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && MC.objectMouseOver.entityHit != null)
                    MC.clickMouse();
            }
            break;
            case "SimplePacket": {
                MC.thePlayer.swingItem();
                MC.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
            }
            break;
        }
        if ((smartBlock.getValue() && shouldSmartBlock(target)) || !smartBlock.getValue())
            executeAutoBlock();
        delayTimer.doReset();

        if (target == null) return;

        System.out.println(getRange(target));

    }

    private boolean shouldSmartBlock(Entity target) {
        if (MC.thePlayer.getHealth() <= 10.0F) {
            return true;
        } else if (!MC.thePlayer.onGround && MC.thePlayer.hurtTime > 0) {
            return true;
        } else if (!(Math.abs(target.lastTickPosX - target.posX) > 3.5) && !(Math.abs(target.lastTickPosZ - target.posZ) > 3.5)) {
            double tarMotionX = target.posX - target.prevPosX;
            double tarMotionY = target.posY - target.prevPosY;
            double tarMotionZ = target.posZ - target.prevPosZ;
            return tarMotionX <= 0.2 && tarMotionZ <= 0.2 && tarMotionY > 1.0;
        } else {
            return false;
        }
    }

    private boolean allowClick() {

        double random = MathUtil.getRandomSin(1.0, this.cps.getValueFloat(), 500.0);
        if (randomCps.getValue())
            return delayTimer.reachedTime((long) (1000.0 / random)) && target != null;
        else {
            return delayTimer.reachedTime((long) (1000.0 / this.cps.getValueInt())) && target != null;
        }
    }

    @EventTarget
    public void onRotation(RotationEvent e) {

        if (!rotations.getValue()) return;
        final VectorRotation mainRotation = NewRotationUtil.rotation(target, throughWallsRotation.getValue(), range.getValue(), blockRaycast.getValue(), !outrangeRotation.getValue(), predictRotation.getValue(), centerMode.getValue(), heuristicsMode.getValue().equalsIgnoreCase("SinCos"));
        float[] mainRotations;

        if (mainRotation == null) {
            this.vectorRotation = new VectorRotation(new Rotation(Rotations.instance.yaw, Rotations.instance.pitch), null);
            return;
        }

        if (vectorRotation == null)
            this.vectorRotation = new VectorRotation(new Rotation(Rotations.instance.yaw, Rotations.instance.pitch), null);

        if (smooth.getValue())
            mainRotations = new float[]{NewRotationUtil.smoothRotation(vectorRotation, mainRotation, smoothing.getValueFloat()).getYaw() + randomRotationsNoValue[0], NewRotationUtil.smoothRotation(vectorRotation, mainRotation, smoothing.getValueFloat()).getPitch() + randomRotationsNoValue[1]};
        else
            mainRotations = new float[]{mainRotation.getYaw() + randomRotationsNoValue[0], mainRotation.getPitch() + randomRotationsNoValue[1]};

        float[] rotations;

        float yawRate = (float) MathUtil.getRandomSin(10, 70, 50.0);
        if (!randomRotations.getValue()) {
            yawRate = 1000.0F;
        }

        if (target != null) {
            mainRotations[0] = MathUtil.limitAngleChange(vectorRotation.getYaw(), mainRotation.getYaw(), yawRate);
            mainRotations[1] = MathUtil.limitAngleChange(vectorRotation.getPitch(), mainRotation.getPitch(), yawRate);
        }

        if (GCDFix.getValue())
            rotations = applyMouseSensitivity(mainRotations[0], mainRotations[1], GCDA3.getValue());
        else
            rotations = new float[]{mainRotations[0], mainRotations[1]};

        this.serverRotations = rotations;


        boolean necessary = (MC.objectMouseOver == null || MC.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY);

        if (!necessary && necessaryRotation.getValue()) {
            rotations[0] = Rotations.instance.yaw;
            rotations[1] = Rotations.instance.pitch;
        }
        rotations[1] = MathHelper.clamp_float(rotations[1], -90, 90);


        e.setYaw(rotations[0]);
        e.setPitch(rotations[1]);


        MC.thePlayer.renderYawOffset = rotations[0];

        if (PointOfViewRotation.getValue()) {
            MC.thePlayer.rotationPitch = rotations[1];
            MC.thePlayer.rotationYaw = rotations[0];
        }

        this.serverRotations = rotations;
        this.vectorRotation = new Rotation(mainRotations[0], mainRotations[1]);

    }

    public float[] applyMouseSensitivity(float yaw, float pitch, boolean a3) {
        float sensitivity = MC.gameSettings.mouseSensitivity;
        sensitivity = Math.max(0.001F, sensitivity);
        int deltaYaw = (int) ((yaw - Rotations.instance.yaw) / ((sensitivity * (sensitivity >= 0.5 ? sensitivity : 1) / 2)));
        int deltaPitch = (int) ((pitch - Rotations.instance.pitch) / ((sensitivity * (sensitivity >= 0.5 ? sensitivity : 1) / 2))) * -1;

        if (a3) {
            deltaYaw -= deltaYaw % 0.5 + 0.25;
            deltaPitch -= deltaPitch % 0.5 + 0.25;
        }

        float f = sensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8F;
        float f2 = (float) deltaYaw * f1;
        float f3 = (float) deltaPitch * f1;

        float endYaw = (float) ((double) Rotations.instance.yaw + (double) f2 * 0.15);
        float endPitch = (float) ((double) Rotations.instance.pitch - (double) f3 * 0.15);

        return new float[]{endYaw, endPitch};
    }


    @EventTarget
    public void onUpdate(UpdateEvent e) {
        {
            collectTargets();
            sortTargets();

            if (switchTimer.reachedTime(switchDelay.getValueInt()) && killAuraMode.getValue().equalsIgnoreCase("Switch")) {
                targetIndex++;
                switchTimer.doReset();
            }
            if (targetIndex >= targets.size())
                targetIndex = 0;

            target = !targets.isEmpty() ? targets.get(targetIndex) : null;
        }
        if (noSprint.getValue() && target != null)
            MC.thePlayer.setSprinting(false);

        this.lastSavedRandom = this.randomRotationsNoValue;

        if (!meetsRequirements((EntityLivingBase) target))
            target = null;
        if (moreParticles.getValue() && target != null) {
            MC.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
            MC.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
        }

    }

    @EventTarget
    public void onTick(EventTick eventTick) {
        if (randomRotations.getValue() && heuristicsMode.getValue().equalsIgnoreCase("SpeedCalc")) {
            final double deltaX = Math.abs(target.posX - target.prevPosX);
            final double deltaZ = Math.abs(target.posZ - target.prevPosZ);

            final double hypo = Math.hypot(deltaX, deltaZ);

            /**
             * The faster the opponent and the player, the more inaccurate we hit
             */

            final double pDeltaX = Math.abs(MC.thePlayer.posX - MC.thePlayer.prevPosX);
            final double pDeltaZ = Math.abs(MC.thePlayer.posZ - MC.thePlayer.prevPosZ);

            final double pHypo = Math.hypot(pDeltaX, pDeltaZ);

            final double sum = ((hypo % pHypo) * 4) * randomSpeed.getValueInt();

            double sumClamp = MathHelper.clamp_double(sum, 0, 15);

            if ((randomRotationsNoValue[0] == 0.0 || randomRotationsNoValue[1] == 0.0) && pHypo != 0.0) {
                sumClamp = ((pHypo) * 4) * randomSpeed.getValueInt();
            }

            this.randomRotationsNoValue[0] = (float) sumClamp + lastSavedRandom[0] * 2;

            //Lower difference for the pitch

            this.randomRotationsNoValue[1] = (float) ((float) sumClamp * .9) + lastSavedRandom[1] * 2;

            if (Double.isNaN(randomRotationsNoValue[0]))
                randomRotationsNoValue[0] = 0;

            if (Double.isNaN(randomRotationsNoValue[1]))
                randomRotationsNoValue[1] = 0;
        }

    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if (target == null) return;

        renderTargetHUD();

    }

    @EventTarget
    public void onMoveFlying(MoveFlyingEvent e) {
        if (target == null) return;

        if (movementAdjustment.getValue()) {
            if (!silentMovementAdjustment.getValue())
                e.setYaw(serverRotations[0]);
        }

    }

    @EventTarget
    public void onSilent(EventStrafe e) {
        if (target == null) return;

        if (movementAdjustment.getValue()) {
            if (silentMovementAdjustment.getValue())
                StrafeUtil.customSilentMoveFlying(e, serverRotations[0]);
        }

    }


    @EventTarget
    public void jump(JumpEvent event) {
        if (target == null) return;

        if (movementAdjustment.getValue()) {
            if (jumpMovementAdjustment.getValue())
                event.setYaw(serverRotations[0]);
        }
    }


    @EventTarget
    public void getLook(EventLook event) {
        event.setRotations(serverRotations);
    }

    @EventTarget
    public void onRender(Render3DEvent e) {
    }


    @Override
    public void onState() {
        this.targets.clear();
        this.delayTimer.doReset();
        MC.thePlayer.itemInUseCount = 0;
    }

    @Override
    public void onUndo() {
        MC.thePlayer.itemInUseCount = 0;
        this.delayTimer.doReset();
        this.targets.clear();
    }

    private void sortTargets() {
        switch (sortMode.getValue()) {
            case "Health": {
                this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case "Distance": {
                this.targets.sort(Comparator.comparingDouble(KillAura::getDistanceToEntity));
                break;
            }
        }
    }

    private void collectTargets() {
        targets.clear();

        for (final Entity e : MC.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) e;
                if (meetsRequirements(entityLivingBase))
                    targets.add(entityLivingBase);
            }
        }

    }

    public double getRangeReal(final Entity entity) {
        return vecRangeCalc.getValue() ? getRange(entity) : entity.getDistanceToEntity(MC.thePlayer);
    }

    private boolean meetsRequirements(final EntityLivingBase entityLivingBase) {
        if (entityLivingBase == MC.thePlayer || entityLivingBase.isDead || (getRangeReal(entityLivingBase) >= (range.getValue())) || entityLivingBase.getHealth() == 0 || entityLivingBase.isInvisible() || entityLivingBase instanceof EntityArmorStand)
            return false;

        if (!entityLivingBase.canEntityBeSeen(MC.thePlayer) && !walls.getValue())
            return false;

        if (entityLivingBase instanceof EntityPlayer)
            return true;

        if (isOnSameTeam(entityLivingBase) && teams.getValue())
            return false;

        if ((entityLivingBase instanceof EntityMob || entityLivingBase instanceof EntityAmbientCreature || entityLivingBase instanceof EntityWaterMob) && !mobs.getValue())
            return false;

        if (entityLivingBase instanceof EntityAnimal && !animals.getValue())
            return false;

        if (entityLivingBase instanceof EntityGolem && !golems.getValue())
            return false;

        return !(entityLivingBase instanceof EntityVillager) || villager.getValue();

    }

    public static boolean isOnSameTeam(EntityLivingBase entity) {
        if (entity.getTeam() != null && MC.thePlayer.getTeam() != null) {
            char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            char c2 = MC.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }

    private void renderTargetHUD() {
        if (!targetHUD.getValue()) return;
        if (!(target instanceof EntityPlayer)) return;
        final EntityPlayer target = (EntityPlayer) this.target;
        final int targetHUDWidth = 130, targetHUDHeight = 50;

        final ScaledResolution scaledresolution = new ScaledResolution(this.MC);
        final int widthS = scaledresolution.getScaledWidth();
        final int heightS = scaledresolution.getScaledHeight();

        final int targetHUDPosX = (widthS / 2 - targetHUDWidth - 10), targetHUDPosY = (heightS / 2 - targetHUDHeight - 10);

        final float healthValue = target.getHealth() / target.getMaxHealth();
        final Color healthColor = healthValue > .75 ? new Color(66, 246, 123) : healthValue > .5 ? new Color(228, 255, 105) : healthValue > .35 ? new Color(236, 100, 64) : new Color(255, 65, 68);

        final float desiredWidth = (targetHUDWidth / target.getMaxHealth()) * Math.min(target.getHealth(), target.getMaxHealth());

        {
            colorAnimation.update();
            colorAnimation.animate(healthColor, .2);

            healthAnimation.update();
            healthAnimation.animate(desiredWidth, .1, Easings.BOUNCE_OUT);
        }

        final Color color = ColorUtil.interpolateColorsBackAndForth(15, 0, colorAnimation.getColor(), colorAnimation.getColor().darker(), false);

        switch (targetHudType.getValue()) {
            case "Simple": {
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX, targetHUDPosY, targetHUDWidth, targetHUDHeight, 0, new Color(20, 20, 20));

                RenderUtil.instance.drawGradientLR(targetHUDPosX + 3, targetHUDPosY + targetHUDHeight - 12, targetHUDWidth - 6, 8, 1, new Color(30, 30, 30), new Color(40, 40, 40));

                RenderUtil.instance.drawGradientLR(targetHUDPosX + 3, targetHUDPosY + targetHUDHeight - 12, healthAnimation.getValue() - 6, 8, 1, color.darker(), color.brighter());

                drawPlayerHead(target, targetHUDPosX + 3, targetHUDPosY + (targetHUDHeight / 2f - 19), 26, 26);

                FontManager.raleWay15.drawString("" + Math.round(target.getHealth()), targetHUDPosX + 5, targetHUDPosY + targetHUDHeight - 8.5f, -1);

                FontManager.futuraSmall.drawString("/" + Math.round(target.getMaxHealth()), targetHUDPosX + 6 + FontManager.raleWay15.getStringWidth("" + Math.round(target.getHealth())), targetHUDPosY + targetHUDHeight - 8.5f, -1);

                StencilUtil.initStencilToWrite();
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX, targetHUDPosY, targetHUDWidth - 10, targetHUDHeight, 1, Color.WHITE);
                StencilUtil.readStencilBuffer(1);

                FontManager.raleWay20.drawString(target.getName(), targetHUDPosX + 33, targetHUDPosY + 10, -1);

                StencilUtil.uninitStencilBuffer();

                StencilUtil.initStencilToWrite();
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX + 33, targetHUDPosY + 21, 70, 10, 1, Color.WHITE);
                StencilUtil.readStencilBuffer(1);

                if (target.getCurrentArmor(0) != null && target.getCurrentArmor(0).getItem() instanceof ItemArmor)
                    renderItem(target.getCurrentArmor(0), targetHUDPosX + 40, targetHUDPosY + 39, 0);

                if (target.getCurrentArmor(1) != null && target.getCurrentArmor(1).getItem() instanceof ItemArmor)
                    renderItem(target.getCurrentArmor(1), targetHUDPosX + 55, targetHUDPosY + 39, 0);

                if (target.getCurrentArmor(2) != null && target.getCurrentArmor(2).getItem() instanceof ItemArmor)
                    renderItem(target.getCurrentArmor(2), targetHUDPosX + 70, targetHUDPosY + 39, 0);

                if (target.getCurrentArmor(3) != null && target.getCurrentArmor(3).getItem() instanceof ItemArmor)
                    renderItem(target.getCurrentArmor(3), targetHUDPosX + 85, targetHUDPosY + 39, 0);


                StencilUtil.uninitStencilBuffer();
                break;
            }
            case "Glow": {
                Color firstColor = ColorUtil.interpolateColorsBackAndForth(3, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
                Color secondColor = ColorUtil.interpolateColorsBackAndForth(3, 90, new Color(84, 51, 158), new Color(104, 127, 203).darker(), false);

                RenderUtil.instance.doRenderShadow(firstColor, targetHUDPosX - 5, targetHUDPosY - 5, targetHUDWidth + 12, targetHUDHeight + 12, 43);
                ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(targetHUDPosX, targetHUDPosY, targetHUDWidth, targetHUDHeight, 3, firstColor, secondColor);

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX + 3, targetHUDPosY + targetHUDHeight - 21, targetHUDWidth - 6, 4, 2, new Color(50, 50, 50));

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX + 3, targetHUDPosY + targetHUDHeight - 21, healthAnimation.getValue() - 6, 4, 2, Color.WHITE);

                FontManager.raleWay15.drawString("" + Math.round(target.getHealth()) + "h " + Math.round(getDistanceToEntity(target)) + "m", calculateMiddle("" + Math.round(target.getHealth()) + "h " + Math.round(getDistanceToEntity(target)) + "m", FontManager.raleWay15, targetHUDPosX, targetHUDWidth), targetHUDPosY + targetHUDHeight - 7, -1);

                drawPlayerHead(target, targetHUDPosX + 4, targetHUDPosY + 7, 15, 15);

                FontManager.raleWay20.drawString(target.getName(), targetHUDPosX + 23, targetHUDPosY + 12, -1);


                break;
            }
            case "Clean": {
                RenderUtil.instance.doRenderShadow(Color.BLACK, targetHUDPosX, targetHUDPosY, targetHUDWidth, targetHUDHeight, 12);
                RenderUtil.instance.drawRect2(targetHUDPosX, targetHUDPosY, targetHUDWidth, targetHUDHeight, new Color(30, 30, 30).getRGB());

                RenderUtil.instance.doRenderShadow(Color.BLACK, targetHUDPosX + 4, targetHUDPosY + 7, 19, 19, 52);

                ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(targetHUDPosX, targetHUDPosY, targetHUDWidth, targetHUDHeight, 3, new Color(38, 111, 250), new Color(38, 111, 250).darker());

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX + 3, targetHUDPosY + targetHUDHeight - 11, targetHUDWidth - 6, 4, 2, new Color(30, 30, 30, 122));

                ShaderManager.shaderBy(RoundedRectShader.class).drawRound(targetHUDPosX + 3, targetHUDPosY + targetHUDHeight - 11, healthAnimation.getValue() - 6, 4, 2, Color.WHITE);

                final int percent = (int) (target.getHealth() * 100 / target.getMaxHealth());

                drawPlayerHead(target, targetHUDPosX + 4, targetHUDPosY + 8, 20, 20);

                FontManager.raleWay20.drawString(target.getName(), targetHUDPosX + 26, targetHUDPosY + 16, -1);

                RenderUtil.instance.doRenderShadow(Color.BLACK, targetHUDPosX + healthAnimation.getValue() - (FontManager.raleWay15.getStringWidth(percent + "%") + 8), targetHUDPosY + targetHUDHeight - 11, (FontManager.raleWay15.getStringWidth(percent + "%") + 6), 6, 12);
                FontManager.raleWay15.drawString(percent + "%", targetHUDPosX + healthAnimation.getValue() - (FontManager.raleWay15.getStringWidth(percent + "%") + 6), targetHUDPosY + targetHUDHeight - 9, -1);

                StencilUtil.uninitStencilBuffer();

            }
        }


    }

    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

    public double getRange(Entity e) {

        final Vec3 targetVector = e.getPositionVector();
        final Vec3 myVector = MC.thePlayer.getPositionVector();

        if (targetVector != null) {
            final AxisAlignedBB box = e.getEntityBoundingBox().grow(e.getCollisionBorderSize());

            if (box.isVecInside(targetVector)) {
                final Vec3 targetVec = new Vec3((box.minX + (box.maxX - box.minX)), (box.minY + (box.maxY - box.minY)), (box.minZ + (box.maxZ - box.minZ)));
                final double distance = targetVec.distanceTo(myVector);
                return distance * .86; //needed to do some correction to pass it to range slider
            }
        }
        return 0;
    }


    public void drawPlayerHead(EntityPlayer player, double x, double y, int width, int height) {

        AbstractClientPlayer clientPlayer = (AbstractClientPlayer) player;
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        MC.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
    }


    public static float getDistanceToEntity(EntityLivingBase entityLivingBase) {
        return MC.thePlayer.getDistanceToEntity(entityLivingBase);
    }

    public static KillAura getInstance() {
        return ModuleManager.getInstance(KillAura.class);
    }

    private void renderItem(ItemStack item, int xPos, int yPos, int zPos) {
        GL11.glPushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        IBakedModel ibakedmodel = MC.getRenderItem().getItemModelMesher().getItemModel(item);
        MC.getRenderItem().textureManager.bindTexture(TextureMap.locationBlocksTexture);
        final float scale = 16.0F;
        GlStateManager.scale(16.0F, 16.0F, 0.0F);
        GL11.glTranslated((double) (((float) xPos - 7.85F) / 16.0F), (double) ((float) (-5 + yPos) / 16.0F), (double) ((float) zPos / 16.0F));
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.disableLighting();
        ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
        if (ibakedmodel.isBuiltInRenderer()) {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityItemStackRenderer.instance.renderByItem(item);
        } else {
            MC.getRenderItem().renderModel(ibakedmodel, -1, item);
        }
        GlStateManager.enableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }

}
