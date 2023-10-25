package org.dreamcore.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.motion.EventJump;
import org.dreamcore.client.event.events.impl.motion.EventStrafe;
import org.dreamcore.client.event.events.impl.packet.EventAttackSilent;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventPostMotion;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.helpers.player.InventoryHelper;
import org.dreamcore.client.helpers.player.KillAuraHelper;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.world.EntityHelper;
import org.dreamcore.client.settings.impl.*;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class KillAura extends Feature {

    /* UTILS */

    public static TimerHelper oldTimerPvp = new TimerHelper();
    public static TimerHelper timer = new TimerHelper();
    public static BooleanSetting players;
    public static BooleanSetting animals;
    public static BooleanSetting team;
    public static BooleanSetting invis;
    public static BooleanSetting pets;
    public static BooleanSetting monsters;
    public static BooleanSetting armorStands;
    public static BooleanSetting walls;
    public static BooleanSetting bypass;
    public static ColorSetting targetHudColor;
    public static EntityLivingBase target;
    public static NumberSetting range;
    public static NumberSetting fov;
    public static BooleanSetting onlyCrit;
    public static NumberSetting minAps;
    public static NumberSetting maxAps;
    public static NumberSetting hitChance;
    public static BooleanSetting autoDisable;
    public static BooleanSetting nakedPlayer;
    public static BooleanSetting sprinting;
    public static BooleanSetting weaponOnly;
    public static BooleanSetting usingItemCheck;
    public static BooleanSetting shieldBreaker;
    public static BooleanSetting autoBlock;
    public static BooleanSetting clientLook = new BooleanSetting("Client Look", false, () -> true);
    public static BooleanSetting visualYaw;
    public static BooleanSetting visualPitch;
    public static BooleanSetting shieldFixer;
    public static BooleanSetting randomRotation = new BooleanSetting("Random Rotation", true, () -> true);
    public static NumberSetting randomYaw = new NumberSetting("Random Yaw", 2, 0, 4, 0.01F, () -> randomRotation.getBoolValue());
    public static NumberSetting randomPitch = new NumberSetting("Random Pitch", 2, 0, 4, 0.01F, () -> randomRotation.getBoolValue());
    public static BooleanSetting shieldBlockCheck;
    public static BooleanSetting attackInInvetory = new BooleanSetting("Attack In Inventory", true, () -> true);
    public static BooleanSetting autoShieldUnPress;
    public static NumberSetting breakerDelay;
    public static NumberSetting critFallDistance;
    public static NumberSetting pitchValue = new NumberSetting("Pitch Value", 0.16F, -4, 4, 0.01f, () -> true);
    public static NumberSetting maxSpeedRotation = new NumberSetting("Max Speed Rotation", 360, 0, 360, 1, () -> true);
    public static NumberSetting minSpeedRotation = new NumberSetting("Min Speed Rotation", 360, 0, 360, 1, () -> true);
    public static NumberSetting attackCoolDown;
    public static BooleanSetting targetHud;
    public static BooleanSetting targetEsp = new BooleanSetting("Target Esp", true, () -> true);
    public static ListSetting targetEspMode = new ListSetting("Target Esp Mode", "Default", () -> targetEsp.getBoolValue(), "Default", "Sims", "Jello", "Astolfo");
    public static ColorSetting targetEspColor = new ColorSetting("TargetEsp Color", Color.RED.getRGB(), () -> targetEsp.getBoolValue());
    public static NumberSetting points = new NumberSetting("Points", 32, 2, 64, 1, () -> targetEspMode.currentMode.equals("Astolfo"));
    public static NumberSetting circleRange = new NumberSetting("Circle Range", 3, 0.1F, 6, 0.01F, () -> targetEspMode.currentMode.equals("Astolfo"));
    public static ListSetting rotationMode;
    public static ListSetting targetHudMode;
    public static ListSetting rotationStrafeMode;
    public static ListSetting strafeMode;
    public static ListSetting sort;
    public static ListSetting swingMode = new ListSetting("Swing Mode", "Client", () -> true, "Client", "None", "Server");
    public static ListSetting clickMode;
    public static NumberSetting rotPredict = new NumberSetting("Rotation Predict", 0.05F, 0.0F, 10, 0.001F, () -> true);
    public static BooleanSetting raycast = new BooleanSetting("Ray-Cast", true, () -> true);

    /* SETTINGS */

    private final TimerHelper blockTimer = new TimerHelper();
    private final TimerHelper shieldBreakerTimer = new TimerHelper();
    private double circleAnim;

    private boolean isBlocking;
    private int changeSlotCounter;

    private double direction = 1, yPos, progress = 0;

    private float delta = 0;

    private long lastMS = System.currentTimeMillis();
    private long lastDeltaMS = 0L;

    public KillAura() {
        super("KillAura", "Автоматически бьет сущностей вокруг тебя", Type.Combat);

        rotationMode = new ListSetting("Aim Mode", "Packet", () -> true, "Packet", "None");

        targetHudMode = new ListSetting("TargetHud Mode", "Astolfo", () -> targetHud.getBoolValue(), "Astolfo", "Small", "Flux", "Novoline Old", "Novoline New");

        rotationStrafeMode = new ListSetting("Rotation Strafe Mode", "Default", () -> !strafeMode.currentMode.equals("None"), "Default", "Silent");

        strafeMode = new ListSetting("Strafe Mode", "None", () -> true, "None", "Always-F");
        sort = new ListSetting("Priority", "Distance", () -> true, "Distance", "Higher Armor", "Blocking Status", "Lowest Armor", "Health", "Angle", "HurtTime");
        clickMode = new ListSetting("PvP Mode", "1.9", () -> true, "1.9", "1.8");
        visualYaw = new BooleanSetting("Visual Yaw", "Отображение визуальной ротации", true, () -> rotationMode.currentMode.equals("Packet"));
        visualPitch = new BooleanSetting("Visual Pitch", "Отображение визуальной ротации", true, () -> rotationMode.currentMode.equals("Packet"));
        fov = new NumberSetting("FOV", "Позволяет редактировать радиус в котором вы можете ударить игрока", 180, 5, 180, 5, () -> true);
        attackCoolDown = new NumberSetting("Cool Down", "Редактирует скорость удара", 0.85F, 0.1F, 1F, 0.01F, () -> clickMode.currentMode.equals("1.9"));
        minAps = new NumberSetting("Min CPS", "Минимальное количество кликов в секунду", 12, 1, 20, 1, () -> clickMode.currentMode.equals("1.8"), NumberSetting.NumberType.APS);
        maxAps = new NumberSetting("Max CPS", "Максимальное количество кликов в секунду", 13, 1, 20, 1, () -> clickMode.currentMode.equals("1.8"), NumberSetting.NumberType.APS);
        hitChance = new NumberSetting("HitChance", "Шанс удара", 100, 1, 100, 5, () -> true);
        range = new NumberSetting("AttackRange", "Дистанция в которой вы можете ударить игрока", 3.6F, 3, 6, 0.1F, () -> true);
        players = new BooleanSetting("Attack Players", "Позволяет бить игроков", true, () -> true);
        armorStands = new BooleanSetting("Attack Armor Stands", "Позволяет бить армор-стенды", true, () -> true);
        monsters = new BooleanSetting("Attack Monsters", "Позволяет бить монстров", true, () -> true);
        pets = new BooleanSetting("Attack Pets", "Позволяет бить прирученных животных", true, () -> true);
        animals = new BooleanSetting("Attack Animals", "Позволяет бить безобидных мобов", false, () -> true);
        team = new BooleanSetting("Attack Teams", "Позволяет бить тимейтов на мини-играх", false, () -> true);
        invis = new BooleanSetting("Attack Invisible", "Позволяет бить невидемых существ", true, () -> true);
        nakedPlayer = new BooleanSetting("Ignore Naked Players", "Не бьет голых игроков", false, () -> true);
        walls = new BooleanSetting("Walls", "Позволяет бить сквозь стены", true, () -> true);
        bypass = new BooleanSetting("Hit Through Walls Bypass", "Обход ударов сквозь стену", true, () -> true);
        sprinting = new BooleanSetting("Stop Sprinting", "Автоматически выключает спринт", false, () -> true);
        weaponOnly = new BooleanSetting("Weapon Only", "Позволяет бить только с оружием в руках", false, () -> true);
        usingItemCheck = new BooleanSetting("Using Item Check", "Не бьет если вы используете меч, еду и т.д", false, () -> true);
        shieldBreaker = new BooleanSetting("Break Shield", "Автоматически ломает щит сопернику", false, () -> true);
        shieldFixer = new BooleanSetting("Shield Sync Fix", "Позволяет бить игроков через щит (обход)", false, () -> true);
        breakerDelay = new NumberSetting("Breaker Delay", "Регулировка ломания щита", 100, 0, 600, 10, () -> shieldBreaker.getBoolValue());
        autoShieldUnPress = new BooleanSetting("Auto Shield UnPress", "Автоматически отжимает щит если у соперника топор в руках", false, () -> true);
        shieldBlockCheck = new BooleanSetting("Shield Block Check", "Не бьет соперника если он прикрыт щитом", false, () -> true);
        autoBlock = new BooleanSetting("Auto Block", "Автоматически жмет пкм при ударе (нужно для 1.8 серверов)", false, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", "Автоматически выключает киллаура при смерти и т.д", false, () -> true);
        onlyCrit = new BooleanSetting("Only Critical", "Бьет в нужный момент для крита", false, () -> true);
        critFallDistance = new NumberSetting("Criticals Fall Distance", "Регулировка дистанции до земли для крита", 0.2F, 0.1F, 1F, 0.01f, () -> onlyCrit.getBoolValue());
        targetHud = new BooleanSetting("TargetHUD", "Отображает хп, еду, броню соперника на экране", true, () -> true);
        targetHudColor = new ColorSetting("TargetHUD Color", Color.PINK.getRGB(), () -> targetHud.getBoolValue() && (targetHudMode.currentMode.equals("Astolfo") || targetHudMode.currentMode.equals("Novoline Old") || targetHudMode.currentMode.equals("Novoline New")));

        addSettings(rotationMode, swingMode, clickMode, sort, attackCoolDown, minAps, maxAps, strafeMode, rotationStrafeMode, targetHud, targetHudMode, targetHudColor, targetEsp, targetEspMode, targetEspColor, circleRange, points, fov, range, hitChance,
                rotPredict, minSpeedRotation, maxSpeedRotation, pitchValue, randomRotation, randomYaw, randomPitch, visualYaw, visualPitch, clientLook, raycast, players, armorStands, monsters, animals, pets, invis, team, nakedPlayer, attackInInvetory, walls, bypass,
                sprinting, weaponOnly, usingItemCheck, autoShieldUnPress, shieldBlockCheck, shieldBreaker, breakerDelay,
                shieldFixer, autoBlock, autoDisable, onlyCrit, critFallDistance);
    }
}
    /*@EventTarget
    public void onEventPreMotion(EventPreMotion e) {
        if (mc.player.isEntityAlive()) {
            if (mc.currentScreen instanceof GuiGameOver && target.isDead) {
                this.toggle();
                NotificationManager.publicity(this.getLabel(), "Toggled off!", 2, NotificationType.INFO);
                return;
            }

            if (mc.player.ticksExisted <= 1) {
                this.toggle();
                NotificationManager.publicity(this.getLabel(), "Toggled off!", 2, NotificationType.INFO);
                return;
            }

            String mode = dreamcore.instance.getSettingByName("Rotation Mode").getValString();
            target = this.getClosest(dreamcore.instance.featureManager.getSettingByName("Range").getValDouble());
            this.setSuffix(mode + ", " + MathUtils.round(range.getValFloat(), 1));
            if (target == null) {
                return;
            }

            if (target.getHealth() > 0.0F) {
                float cdValue = Main.instance.setmgr.getSettingByName("OnlyCrits").getValue() ? 0.95F : 1.0F;
                float chance = Main.instance.setmgr.getSettingByName("HitChance").getValFloat();
                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue()) {
                    return;
                }

                if (mc.player.getCooledAttackStrength(0.0F) >= cdValue && RandomUtils.getRandomDouble(0.0D, 100.0D) <= (double)chance) {
                    double gt = Main.instance.setmgr.getSettingByName("OldPvpSystemDelay").getValDouble();
                    double gt1 = 100.0D;
                    if (!this.oldTimer.check((float)(gt + RandomUtils.getRandomDouble(0.0D, gt1))) && Main.instance.setmgr.getSettingByName("OldPvpSystem").getValue()) {
                        return;
                    }

                    if (!RotationSpoofer.isLookingAtEntity(target) && Main.instance.setmgr.getSettingByName("RayCast").getValue()) {
                        return;
                    }

                    if (!MovementUtil.isBlockAboveHead()) {
                        if (!(mc.player.fallDistance > Main.instance.setmgr.getSettingByName("Crits Fall Distance").getValFloat()) && !mc.player.isInLiquid2() && Main.instance.setmgr.getSettingByName("OnlyCrits").getValue()) {
                            return;
                        }
                    } else if (mc.player.fallDistance != 0.0F && !mc.player.isInLiquid2() && Main.instance.setmgr.getSettingByName("OnlyCrits").getValue()) {
                        return;
                    }

                    if (Main.instance.setmgr.getSettingByName("SnapHead").getValue() && mc.player.getCooledAttackStrength(0.0F) >= cdValue && RandomUtils.getRandomDouble(0.0D, 100.0D) <= (double)chance) {
                        float[] rots = RotationUtil.getFaceRotating(target);
                        e.setYaw(rots[0]);
                        e.setPitch(rots[1]);
                        mc.player.rotationYaw = rots[0];
                        mc.player.rotationPitch = rots[1];
                    }

                    mc.playerController.attackEntity(mc.player, target);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.oldTimer.reset();
                }
            }
        }

    }

    @EventTarget
    public void onRotations(EventPreMotionUpdate event) {
        if (target != null) {
            if (target.getHealth() > 0.0F) {
                String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
                float[] rots = RotationUtil.getFaceRotating(target);
                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && Main.instance.setmgr.getSettingByName("Weapon Only").getValue()) {
                    return;
                }

                if (mode.equalsIgnoreCase("Matrix")) {
                    event.setYaw(rots[0]);
                    mc.player.renderYawOffset = rots[0];
                    mc.player.rotationYawHead = rots[0];
                    if (Main.instance.setmgr.getSettingByName("RotationPitch").getValue()) {
                        mc.player.rotationPitchHead = rots[1];
                        event.setPitch(rots[1]);
                    }
                }

                if (mode.equalsIgnoreCase("Legit")) {
                    mc.player.rotationYaw = rots[0];
                    if (Main.instance.setmgr.getSettingByName("RotationPitch").getValue()) {
                        mc.player.rotationPitch = rots[1];
                    }
                }

                float[] rotationsInstant;
                if (mode.equalsIgnoreCase("BestPos")) {
                    rotationsInstant = RotationUtil.getRotationsNew(target, (double)range.getValFloat());
                    event.setYaw(rotationsInstant[0]);
                    event.setPitch(rotationsInstant[1]);
                }

                if (mode.equalsIgnoreCase("Predict")) {
                    rotationsInstant = RotationUtil.getRotationsPredictNew(target, true, 0.5D);
                    event.setYaw(rotationsInstant[0]);
                    event.setPitch(rotationsInstant[1]);
                }

                if (Main.instance.setmgr.getSettingByName("ShieldBreaker").getValue()) {
                    if (target == null) {
                        return;
                    }

                    if (target.isBlocking()) {
                        mc.player.inventory.currentItem = EntityUtil.getAxeAtHotbar();
                        EntityUtil.attackEntity(target, true, true);
                    } else if (target.getHeldItemOffhand().getItem() instanceof ItemShield) {
                        mc.player.inventory.currentItem = InventoryUtil.getSwordAtHotbar();
                    }
                }

                if (Main.instance.setmgr.getSettingByName("ShieldPresser").getValue()) {
                    if ((target == null || !((double)mc.player.getDistanceToEntity(target) <= Main.instance.setmgr.getSettingByName("Range").getValDouble())) && target.getHealth() < 0.0F) {
                        EntityUtil.blockByShield(false);
                        return;
                    }

                    if (target.getHealth() < 0.0F) {
                        EntityUtil.blockByShield(false);
                        return;
                    }

                    if ((double)mc.player.getCooledAttackStrength(0.0F) <= 0.1D) {
                        EntityUtil.blockByShield(true);
                    }

                    if ((double)mc.player.getCooledAttackStrength(0.0F) >= 0.75D) {
                        EntityUtil.blockByShield(false);
                    }
                }
            }

        }
    }

    private EntityLivingBase getClosest(double range) {
        this.targets.clear();
        double dist = range;
        EntityLivingBase target = null;
        Iterator var6 = mc.world.loadedEntityList.iterator();

        while(var6.hasNext()) {
            Object object = var6.next();
            Entity entity = (Entity)object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase)entity;
                if (this.canAttack(player)) {
                    double currentDist = (double)mc.player.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                        this.targets.add(player);
                    }
                }
            }
        }

        return target;
    }

    public static final void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static Color setAlpha(Color color, int alpha) {
        alpha = (int)MathUtils.clamp((float)alpha, 0.0F, 255.0F);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static final void color(double red, double green, double blue) {
        color(red, green, blue, 1.0D);
    }

    @EventTarget
    public void onRend(EventRender3D event) {
        if (target != null) {
            if (target.getHealth() > 0.0F && this.getState() && Main.instance.setmgr.getSettingByName("ESPHead").getValue()) {
                GL11.glPushMatrix();
                int color = target.hurtResistantTime > 15 ? ColorUtils.getColor(new Color(255, 70, 70, 80)) : ColorUtils.getColor(new Color(255, 255, 255, 80));
                double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                x -= 0.5D;
                z -= 0.5D;
                y += (double)target.getEyeHeight() + 0.35D - (target.isSneaking() ? 0.25D : 0.0D);
                double mid = 0.5D;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glTranslated(x + mid, y + mid, z + mid);
                GL11.glRotated((double)(-target.rotationYaw % 360.0F), 0.0D, 1.0D, 0.0D);
                GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                RenderUtil.glColor(color);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 0.05D, z + 1.0D));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }

        }
    }

    @EventTarget
    public void e(EventRender2D e) {
        String mode = Main.instance.setmgr.getSettingByName("TargetHud Mode").getValString();
        if (Main.instance.setmgr.getSettingByName("TargetHud").getValue() && target != null) {
            if (mode.equalsIgnoreCase("Flux")) {
                this.renderFluxTGHud();
            } else if (mode.equalsIgnoreCase("HVH")) {
                this.renderHVHTGHUD(e.getResolution());
            } else if (mode.equalsIgnoreCase("Astolfo")) {
                this.renderAstolfoTGHUD(e.getResolution());
            }
        }

    }

    private void renderAstolfoTGHUD(ScaledResolution resolution) {
        float scaledWidth = (float)resolution.getScaledWidth();
        float scaledHeight = (float)resolution.getScaledHeight();
        float x = scaledWidth / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat();
        float y = scaledHeight / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
        RenderUtil.color((new Color(0.1F, 0.1F, 0.1F, 0.9F)).getRGB());
        double hpPercentage = (double)(target.getHealth() / target.getMaxHealth());
        hpPercentage = MathHelper.clamp(hpPercentage, 0.0D, 1.0D);
        double hpWidth = 92.0D * hpPercentage;
        int healthColor = ColorUtils.getHealthColor(mc.player.getHealth(), mc.player.getMaxHealth()).getRGB();
        this.healthBarWidth = (double)AnimationUtil.calculateCompensation((float)hpWidth, (float)this.healthBarWidth, 5L, 0.3029999852180481D * Minecraft.frameTime);
        RenderUtil.drawRect2((double)x, (double)y, 155.0D, 60.0D, ColorUtils.getColor(23, 202));
        if (!target.getName().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 31.0F, y + 5.0F, -1);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1.0F);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GlStateManager.translate(-x, -y, 1.0F);
        mc.fontRendererObj.drawStringWithShadow(MathUtils.round(target.getHealth() / 2.0F, 1) + " ❤", x + 16.0F, y + 10.0F, (new Color(Main.getClientColor().getRGB())).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtil.drawEntityOnScreen((double)(x + 16.0F), (double)(y + 55.0F), 25.0D, target.rotationYaw, -target.rotationPitch, target);
        RenderUtil.drawRect2((double)(x + 30.0F), (double)(y + 46.0F), this.healthBarWidth + 26.0D, 10.0D, (new Color(Main.getClientColor().getRGB())).getRGB());
    }

    private void renderHVHTGHUD(ScaledResolution res) {
        GlStateManager.pushMatrix();
        float scaledWidth;
        float scaledHeight;
        float x;
        float y;
        float x;
        if (target.getHealth() > 0.0F && this.canAttack(target)) {
            scaledWidth = EntityUtil.getPing(mc.player);
            scaledHeight = EntityUtil.getPing(target);
            x = (float)res.getScaledWidth();
            y = (float)res.getScaledHeight();
            x = x / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat();
            float y = y / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
            if (mc.player != target) {
                double hpPercentage = (double)(mc.player.getHealth() / mc.player.getMaxHealth());
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0D, 1.0D);
                double hpWidth = 92.0D * hpPercentage;
                int healthColor = ColorUtils.getHealthColor(mc.player.getHealth(), mc.player.getMaxHealth()).getRGB();
                String healthStr = "HP: " + (float)((int)mc.player.getHealth()) / 2.0F;
                float health1 = mc.player.getHealth();
                double hpPercentage1 = (double)(mc.player.getHealth() / mc.player.getMaxHealth());
                hpPercentage1 = MathHelper.clamp(hpPercentage, 0.0D, 1.0D);
                double hpWidth1 = 92.0D * hpPercentage1;
                int healthColor1 = ColorUtils.getHealthColor(mc.player.getHealth(), mc.player.getMaxHealth()).getRGB();
                String healthStr1 = "HP: " + (float)((int)mc.player.getHealth()) / 2.0F;
                this.healthBarWidth = AnimationUtil.animate(hpWidth, this.healthBarWidth, 0.5029999852180481D);
                this.healthBarWidth1 = AnimationUtil.animate(hpWidth1, this.healthBarWidth1, 0.5029999852180481D);
                GL11.glEnable(3089);
                RenderUtil.prepareScissorBox(x - 55.0F, y - 150.0F, x + 243.0F, y + 100.0F);
                RenderUtil.drawRoundedRect((double)(x - 55.0F), (double)(y - 4.0F), (double)(x + 241.0F), (double)(y + 66.0F), ColorUtils.getTeamColor(mc.player), ColorUtils.getTeamColor(mc.player));
                RenderUtil.drawRect((double)(x - 54.0F), (double)(y - 3.0F), (double)(x + 240.0F), (double)(y + 65.0F), COLOR.getRGB());
                RenderUtil.drawRoundedRect((double)(x - 10.0F), (double)(y + 15.0F), (double)(x - 10.0F) + this.healthBarWidth1, (double)(y + 25.0F), healthColor1, healthColor1);
                mc.fontRenderer.drawStringWithShadow("(YOU)", x - 11.0F, y + 5.0F, -1);
                if (target instanceof EntityPlayer) {
                    mc.fontRenderer.drawStringWithShadow("VS", x + 88.0F, y + 33.0F, -1);
                    RenderUtil.drawArrow(x + 90.0F, y + 18.0F, -1);
                }

                ArrayList<ItemStack> list = new ArrayList();

                for(int i = 0; i < 5; ++i) {
                    ItemStack getEquipmentInSlot = mc.player.getEquipmentInSlot(i);
                    if (getEquipmentInSlot != null) {
                        list.add(getEquipmentInSlot);
                    }
                }

                float x2 = x / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat();
                float y2 = y / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
                int n10 = -(list.size() * 9);

                for(Iterator var25 = list.iterator(); var25.hasNext(); x2 += 16.0F) {
                    ItemStack itemStack = (ItemStack)var25.next();
                    RenderUtil.enableGUIStandardItemLighting();
                    RenderUtil.drawRoundedRect((int)x2 - 40, (int)y2 + 48, 14, 15, (new Color(45, 45, 45, 255)).getRGB());
                    RenderUtil.renderItem(itemStack, (int)x2 - 41, (int)y2 + 47);
                }

                List var5 = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(mc.player.connection.getPlayerInfoMap());

                for(Iterator var48 = var5.iterator(); var48.hasNext(); GL11.glDisable(3089)) {
                    Object aVar5 = var48.next();
                    NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                    if (mc.world.getPlayerEntityByUUID(var6.getGameProfile().getId()) == mc.player) {
                        Minecraft.getTextureManager().bindTexture(var6.getLocationSkin());
                        Gui.drawScaledCustomSizeModalRect((int)x - 50, (int)y, 8.0F, 8.0F, 8, 8, 36, 36, 64.0F, 64.0F);
                        if (((EntityPlayer)target).isWearing(EnumPlayerModelParts.HAT)) {
                        }

                        GlStateManager.bindTexture(0);
                    }
                }
            }
        }

        scaledWidth = (float)res.getScaledWidth();
        scaledHeight = (float)res.getScaledHeight();
        x = scaledWidth / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat();
        y = scaledHeight / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
        if (target.getHealth() > 0.0F && this.canAttack(target)) {
            x = target.getHealth();
            double hpPercentage = (double)(x / target.getMaxHealth());
            hpPercentage = MathHelper.clamp(hpPercentage, 0.0D, 1.0D);
            double hpWidth = 92.0D * hpPercentage;
            int healthColor = ColorUtils.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB();
            String healthStr = "HP: " + (float)((int)target.getHealth()) / 2.0F;
            this.healthBarWidth = AnimationUtil.animate(hpWidth, this.healthBarWidth, 1.2029999852180482D);
            this.hudHeight = AnimationUtil.animate(40.0D, this.hudHeight, 0.10000000149011612D);
            GL11.glEnable(3089);
            RenderUtil.drawRoundedRect((double)(x + 105.0F), (double)(y + 15.0F), (double)(x + 105.0F) + this.healthBarWidth, (double)(y + 25.0F), healthColor, healthColor);
            mc.fontRenderer.drawStringWithShadow("(TARGET)", x + 156.0F, y + 5.0F, -1);
            ArrayList<ItemStack> list1 = new ArrayList();

            ItemStack itemStack;
            for(int i = 4; i < 5; ++i) {
                itemStack = ((EntityPlayer)target).getEquipmentInSlot(i);
                if (itemStack != null) {
                    list1.add(itemStack);
                }
            }

            Iterator var33 = list1.iterator();

            while(var33.hasNext()) {
                itemStack = (ItemStack)var33.next();
                RenderUtil.enableGUIStandardItemLighting();
                mc.getRenderItem().renderItemIntoGUI(itemStack, (int)x + 147, (int)y + 47);
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, (int)x + 147, (int)y + 47);
            }

            ArrayList<ItemStack> list = new ArrayList();

            for(int i = 0; i < 5; ++i) {
                ItemStack getEquipmentInSlot = ((EntityPlayer)target).getEquipmentInSlot(i);
                if (getEquipmentInSlot != null || Type.HAND != null) {
                    list.add(getEquipmentInSlot);
                }
            }

            float x2 = scaledWidth / 2.0F - Main.instance.setmgr.getSettingByName("TargetHudPositionX").getValFloat();
            float y2 = scaledHeight / 2.0F + Main.instance.setmgr.getSettingByName("TargetHudPositionY").getValFloat();
            int n10 = -(list.size() * 9);

            for(Iterator var18 = list.iterator(); var18.hasNext(); x2 += 16.0F) {
                ItemStack itemStack = (ItemStack)var18.next();
                RenderUtil.enableGUIStandardItemLighting();
                RenderUtil.drawRoundedRect((int)x2 + 147, (int)y2 + 48, 14, 15, (new Color(45, 45, 45, 255)).getRGB());
                RenderUtil.renderItem(itemStack, (int)x2 + 147, (int)y2 + 47);
            }

            List var5 = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(mc.player.connection.getPlayerInfoMap());

            for(Iterator var42 = var5.iterator(); var42.hasNext(); GL11.glDisable(3089)) {
                Object aVar5 = var42.next();
                NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                if (mc.world.getPlayerEntityByUUID(var6.getGameProfile().getId()) == target) {
                    Minecraft.getTextureManager().bindTexture(var6.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int)x + 200, (int)y, 8.0F, 8.0F, 8, 8, 36, 36, 64.0F, 64.0F);
                    if (((EntityPlayer)target).isWearing(EnumPlayerModelParts.HAT)) {
                    }

                    GlStateManager.bindTexture(0);
                }
            }
        }

        GlStateManager.popMatrix();
    }

    private void renderFluxTGHud() {
        ScaledResolution sr = new ScaledResolution(mc);
        if (target instanceof EntityPlayer && Main.instance.setmgr.getSettingByName("TargetHud").getValue()) {
            if (ClickGuiScreen.scaling <= 1.0D) {
                ClickGuiScreen.scaling += 0.003000000026077032D;
            }

            int width = 20 + Minecraft.getMinecraft().clickguismall.getStringWidth(target.getName());
            GL11.glPushMatrix();
            GL11.glTranslated((double)(width / 100), (double)(sr.getScaledHeight() / 100), 0.0D);
            GL11.glScalef((float)ClickGuiScreen.scaling, (float)ClickGuiScreen.scaling, 1.0F);
            GL11.glTranslated((double)(sr.getScaledWidth() / 2 + 10), (double)(sr.getScaledHeight() / 2), (double)(sr.getScaledWidth() / 2 + 10));
            RenderUtil.drawSmoothRect(-10.0F, 20.0F, (float)(2 + width), target.getTotalArmorValue() != 0 ? 56.0F : 50.0F, (new Color(35, 35, 40, 230)).getRGB());
            Minecraft.getMinecraft().clickguismall.drawString(target.getName(), 10.0F, 26.0F, 16777215);
            Minecraft.getMinecraft().clickguismall.drawStringWithShadow(MathUtils.round(target.getHealth() / 2.0F, 1) + " HP", 10.0F, 35.0F, 16777215);
            this.drawHead(((NetHandlerPlayClient)Objects.requireNonNull(mc.getConnection())).getPlayerInfo(target.getUniqueID()).getLocationSkin(), -8, 22);
            RenderUtil.drawSmoothRect(-8.0F, 44.0F, (float)width, 46.0F, (new Color(25, 25, 35, 255)).getRGB());
            this.easingHealth = (int)AnimationUtil.animation((float)this.easingHealth, target.getHealth() - (float)this.easingHealth, 1.0E-4F);
            this.easingHealth += (int)((double)(target.getHealth() - (float)this.easingHealth) / Math.pow(2.0D, 7.0D));
            if (this.easingHealth < 0 || (float)this.easingHealth > target.getMaxHealth()) {
                this.easingHealth = (int)target.getHealth();
            }

            if ((float)this.easingHealth > target.getHealth()) {
                RenderUtil.drawSmoothRect(-8.0F, 66.0F, (float)this.easingHealth / target.getMaxHealth() * (float)width, 58.0F, (new Color(231, 182, 0, 255)).getRGB());
            }

            if ((float)this.easingHealth < target.getHealth()) {
                RenderUtil.drawRect((double)((float)this.easingHealth / target.getMaxHealth() * (float)width), 56.0D, (double)((float)this.easingHealth / target.getMaxHealth() * (float)width), 58.0D, (new Color(231, 182, 0, 255)).getRGB());
            }

            RenderUtil.drawSmoothRect(-8.0F, 44.0F, target.getHealth() / target.getMaxHealth() * (float)width, 46.0F, ColorUtils.getHealthColor(target).getRGB());
            if (target.getTotalArmorValue() != 0) {
                RenderUtil.drawSmoothRect(-8.0F, 50.0F, (float)width, 52.0F, (new Color(25, 25, 35, 255)).getRGB());
                RenderUtil.drawSmoothRect(-8.0F, 50.0F, (float)(target.getTotalArmorValue() / 20 * width), 52.0F, (new Color(77, 128, 255, 255)).getRGB());
            }

            GL11.glPopMatrix();
        }

    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft var10000 = mc;
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8.0F, 8.0F, 8, 8, 16, 16, 64.0F, 64.0F);
    }

    private boolean canAttack(EntityLivingBase player) {
        Iterator var2 = Main.instance.friendManager.getFriends().iterator();

        while(var2.hasNext()) {
            Friend friend = (Friend)var2.next();
            if (player.getName().equals(friend.getName())) {
                return false;
            }
        }

        if (Main.instance.moduleManager.getModuleByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && Main.instance.setmgr.getSettingByName("Hit Before").getValue()) {
            return false;
        } else if (Main.instance.moduleManager.getModuleByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && AntiBot.rad.getValue()) {
            return false;
        } else if (Main.instance.setmgr.getSettingByName("IgnoreNakedPlayers").getValue() && !EntityUtil.isArmorPlayer(player)) {
            return false;
        } else {
            if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
                if (player instanceof EntityPlayer && !Main.instance.setmgr.getSettingByName("Players").getValue()) {
                    return false;
                }

                if (player instanceof EntityAnimal && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
                    return false;
                }

                if (player instanceof EntityMob && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
                    return false;
                }

                if (player instanceof EntityVillager && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
                    return false;
                }
            }

            if (player.isInvisible() && !Main.instance.setmgr.getSettingByName("Invisible").getValue()) {
                return false;
            } else if (!RotationUtil.canSeeEntityAtFov(player, (float)Main.instance.setmgr.getSettingByName("FOV").getValDouble()) && !canSeeEntityAtFov(player, (float)Main.instance.setmgr.getSettingByName("FOV").getValDouble())) {
                return false;
            } else if (!range(player, (double)range.getValFloat())) {
                return false;
            } else if (!player.canEntityBeSeen(mc.player)) {
                return Main.instance.setmgr.getSettingByName("Walls").getValue();
            } else {
                return player != mc.player;
            }
        }
    }

    private static boolean range(EntityLivingBase entity, double range) {
        return (double)mc.player.getDistanceToEntity(entity) <= range;
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - Minecraft.getMinecraft().player.posX;
        double diffZ = entityLiving.posZ - Minecraft.getMinecraft().player.posZ;
        float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
        double difference = angleDifference((double)newYaw, (double)Minecraft.getMinecraft().player.rotationYaw);
        return difference <= (double)scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0D);
        if (yaw360 > 180.0F) {
            yaw360 = 360.0F - yaw360;
        }

        return (double)yaw360;
    }

    public boolean isPlayerValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            Collection<NetworkPlayerInfo> playerInfo = mc.player.connection.getPlayerInfoMap();
            Iterator var3 = playerInfo.iterator();

            while(var3.hasNext()) {
                NetworkPlayerInfo info = (NetworkPlayerInfo)var3.next();
                if (info.getGameProfile().getName().matches(entity.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    @EventTarget
    public void onMoveFlyingEvent(MoveEvent e) {
        String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
        if (target != null) {
            if (!this.getState()) {
                return;
            }

            if (mc.gameSettings.keyBindBack.isKeyDown()) {
                return;
            }

            if (mode.equalsIgnoreCase("Legit")) {
                return;
            }

            if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.player.onGround && Main.instance.setmgr.getSettingByName("RotationStrafe").getValue()) {
                if (Main.instance.moduleManager.getModuleByClass(TargetStrafe.class).getState() && target != null && mc.player.getDistanceToEntity(target) <= TargetStrafe.range.getValFloat() && Main.instance.setmgr.getSettingByName("AutoJump").getValue()) {
                    return;
                }

                MovementUtil.setSpeedAt(e, (float)((double)Main.fakeYaw + MovementUtil.getPressedMoveDir() - (double)mc.player.rotationYaw), MovementUtil.getMoveSpeed(e));
            }
        }

    }
}


    public static double easeInOutQuart(double x) {
        return (x < 0.5) ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2;
    }
}*/
