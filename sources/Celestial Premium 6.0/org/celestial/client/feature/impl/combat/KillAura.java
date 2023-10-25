/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventJump;
import org.celestial.client.event.events.impl.motion.EventStrafe;
import org.celestial.client.event.events.impl.packet.EventAttackSilent;
import org.celestial.client.event.events.impl.player.EventPostMotion;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.Criticals;
import org.celestial.client.feature.impl.combat.TargetStrafe;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.feature.impl.movement.Speed;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.misc.TpsHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.player.KillAuraHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.components.draggable.DraggableModule;
import org.celestial.client.ui.components.draggable.impl.TargetHUDComponent;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class KillAura
extends Feature {
    public static TimerHelper oldTimerPvp = new TimerHelper();
    public static TimerHelper timer = new TimerHelper();
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public ListSetting simsMarkMode = new ListSetting("Sims Mark Mode", "Client", () -> simsMark.getCurrentValue(), "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting simsMarkColor = new ColorSetting("Sims Mark Color", Color.WHITE.getRGB(), () -> simsMark.getCurrentValue() && this.simsMarkMode.currentMode.equals("Custom"));
    public ListSetting jelloMode = new ListSetting("Jello Mode", "Client", () -> circle.getCurrentValue() && KillAura.circleMode.currentMode.equals("Jello"), "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting jelloColor = new ColorSetting("Jello Color", Color.WHITE.getRGB(), () -> circle.getCurrentValue() && this.jelloMode.currentMode.equals("Custom"));
    public static BooleanSetting team;
    public static BooleanSetting invis;
    public static BooleanSetting armorStands;
    public static BooleanSetting walls;
    public static BooleanSetting wallsBypass;
    public static ColorSetting targetHudColor;
    public static EntityLivingBase target;
    public static NumberSetting range;
    public static NumberSetting fov;
    public static BooleanSetting onlyCrit;
    public static BooleanSetting spaceOnly;
    public static BooleanSetting adaptiveCrits;
    public static NumberSetting adaptiveCritsHealth;
    public static BooleanSetting pullDown;
    public static NumberSetting pullStength;
    public static NumberSetting oldPvpSystemDelayMin;
    public static NumberSetting oldPvpSystemDelayMax;
    public static NumberSetting rotSpeed;
    public static NumberSetting rotYawRandom;
    public static NumberSetting rotPitchRandom;
    public static NumberSetting rotPitchDown;
    public static NumberSetting preAimRange;
    public static NumberSetting hitChance;
    public static BooleanSetting autoDisable;
    public static BooleanSetting nakedPlayer;
    public static BooleanSetting rayTrace;
    public static NumberSetting rayTraceBox;
    public static BooleanSetting sprinting;
    public static BooleanSetting autoWeapon;
    public static BooleanSetting weaponOnly;
    public static BooleanSetting autoJump;
    public static BooleanSetting usingItemCheck;
    public static BooleanSetting shieldBreaker;
    public static NumberSetting shieldBlockTicks;
    public static BooleanSetting autoBlock;
    public static NumberSetting sendDelay;
    public static NumberSetting outDelay;
    public static BooleanSetting visualYaw;
    public static BooleanSetting visualPitch;
    public static BooleanSetting shieldFixer;
    public static BooleanSetting shieldBlockCheck;
    public static BooleanSetting autoShieldUnPress;
    public static BooleanSetting shieldDesync;
    public static NumberSetting fixerDelay;
    public static NumberSetting breakerDelay;
    public static NumberSetting critFallDistance;
    public static NumberSetting attackCoolDown;
    public static BooleanSetting tpsSync;
    public static BooleanSetting targetHud;
    public static BooleanSetting circle;
    public static NumberSetting segments;
    public static NumberSetting circleRange;
    public static BooleanSetting simsMark;
    public static ListSetting rotationMode;
    public static ListSetting targetHudMode;
    public static ListSetting circleMode;
    public static ListSetting rotationStrafeMode;
    public static ListSetting strafeMode;
    public static ListSetting sort;
    public static ListSetting clickMode;
    public static NumberSetting rotPredict;
    public static BooleanSetting setRotations;
    public static NumberSetting breakRadius;
    public static NumberSetting circleSpeed;
    public static BooleanSetting strafing;
    private final TimerHelper blockTimer = new TimerHelper();
    private final TimerHelper blockTimer1 = new TimerHelper();
    private final TimerHelper shieldFixerTimer = new TimerHelper();
    private final TimerHelper shieldBreakerTimer = new TimerHelper();
    public static boolean isAttacking;
    private boolean isBlocking;
    private double circleAnim;
    private double circleValue;
    private boolean canDown;
    private int changeSlotCounter;
    public static int critCounter;
    public static int shieldHitCounter;
    public static boolean canDo;
    public static boolean isBreaked;
    public static int shieldTicks;

    public KillAura() {
        super("KillAura", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u044c\u0435\u0442 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439 \u0432\u043e\u043a\u0440\u0443\u0433 \u0442\u0435\u0431\u044f", Type.Combat);
        rotationMode = new ListSetting("Rotation Mode", "Packet", () -> true, "Packet", "Client", "AAC", "Snap", "Sunrise", "ReallyWorld", "None");
        targetHudMode = new ListSetting("TargetHud Mode", "Glow", () -> targetHud.getCurrentValue(), "Glow", "Red-Blue", "Moon Dev", "Astolfo", "Skeet", "Flux", "Minecraft", "Novoline Old", "Novoline New", "Dev");
        rotationStrafeMode = new ListSetting("Rotation Strafe Mode", "Default", () -> !KillAura.strafeMode.currentMode.equals("None"), "Default", "Silent");
        strafeMode = new ListSetting("Strafe Mode", "None", () -> true, "None", "Always", "No Air");
        sort = new ListSetting("TargetSort Mode", "Health", () -> true, "Distance", "Higher Armor", "Blocking Status", "Lowest Armor", "Health", "Angle", "HurtTime");
        clickMode = new ListSetting("Click Mode", "1.9", () -> true, "1.9", "1.8");
        visualYaw = new BooleanSetting("Visual Yaw", "\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0432\u0438\u0437\u0443\u0430\u043b\u044c\u043d\u043e\u0439 \u0440\u043e\u0442\u0430\u0446\u0438\u0438", true, () -> true);
        visualPitch = new BooleanSetting("Visual Pitch", "\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0432\u0438\u0437\u0443\u0430\u043b\u044c\u043d\u043e\u0439 \u0440\u043e\u0442\u0430\u0446\u0438\u0438", true, () -> true);
        fov = new NumberSetting("FOV", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0440\u0430\u0434\u0438\u0443\u0441 \u0432 \u043a\u043e\u0442\u043e\u0440\u043e\u043c \u0432\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0443\u0434\u0430\u0440\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430", 180.0f, 5.0f, 180.0f, 5.0f, () -> true);
        attackCoolDown = new NumberSetting("Attack Cooldown", "\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u0443\u0435\u0442 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0443\u0434\u0430\u0440\u0430", 0.9f, 0.1f, 1.0f, 0.01f, () -> KillAura.clickMode.currentMode.equals("1.9"));
        tpsSync = new BooleanSetting("TPS Sync", "\u0421\u0438\u043d\u0445\u0440\u043e\u043d\u0438\u0437\u0438\u0440\u0443\u0435\u0442 \u0430\u0442\u0430\u043a\u0443 \u0441 TPS'\u043e\u043c \u0441\u0435\u0440\u0432\u0435\u0440\u0430", false, () -> true);
        oldPvpSystemDelayMin = new NumberSetting("Min APS", "\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043a\u043b\u0438\u043a\u043e\u0432 \u0432 \u0441\u0435\u043a\u0443\u043d\u0434\u0443", 12.0f, 1.0f, 20.0f, 1.0f, () -> KillAura.clickMode.currentMode.equals("1.8"));
        oldPvpSystemDelayMax = new NumberSetting("Max APS", "\u041c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043a\u043b\u0438\u043a\u043e\u0432 \u0432 \u0441\u0435\u043a\u0443\u043d\u0434\u0443", 13.0f, 1.0f, 20.0f, 1.0f, () -> KillAura.clickMode.currentMode.equals("1.8"));
        hitChance = new NumberSetting("HitChance", "\u0428\u0430\u043d\u0441 \u0443\u0434\u0430\u0440\u0430", 100.0f, 1.0f, 100.0f, 5.0f, () -> true);
        range = new NumberSetting("AttackRange", "\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u0432 \u043a\u043e\u0442\u043e\u0440\u043e\u0439 \u0432\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0443\u0434\u0430\u0440\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430", 3.6f, 3.0f, 7.0f, 0.01f, () -> true);
        rotPredict = new NumberSetting("Rotation Predict", "\u041f\u0440\u0435\u0434\u0438\u043a\u0442 \u0440\u043e\u0442\u0430\u0446\u0438\u0438", 0.0f, 0.0f, 5.0f, 0.1f, () -> !KillAura.rotationMode.currentMode.equals("None") && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        rotSpeed = new NumberSetting("Rotation Speed", "\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0440\u043e\u0442\u0430\u0446\u0438\u0438", 5.0f, 0.5f, 5.0f, 0.1f, () -> !KillAura.rotationMode.currentMode.equals("None") && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        rotYawRandom = new NumberSetting("Rotation Yaw Random", "\u0420\u0435\u043b\u0443\u043b\u0438\u0433\u0440\u0443\u0435\u0442 \u0440\u0430\u043d\u0434\u043e\u043c \u0440\u043e\u0442\u0430\u0446\u0438\u0438", 2.0f, 0.0f, 5.0f, 0.1f, () -> !KillAura.rotationMode.currentMode.equals("None") && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        rotPitchRandom = new NumberSetting("Rotation Pitch Random", "\u0420\u0435\u043b\u0443\u043b\u0438\u0433\u0440\u0443\u0435\u0442 \u0440\u0430\u043d\u0434\u043e\u043c \u0440\u043e\u0442\u0430\u0446\u0438\u0438", 2.0f, 0.0f, 5.0f, 0.1f, () -> !KillAura.rotationMode.currentMode.equals("None") && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        rotPitchDown = new NumberSetting("Rotation Pitch Down", "\u041a\u0430\u043a \u043d\u0438\u0437\u043a\u043e \u0432\u044b \u0431\u0443\u0434\u0435\u0442\u0435 \u0441\u043c\u043e\u0442\u0440\u0435\u0442\u044c", 0.2f, 0.0f, 1.0f, 0.01f, () -> !KillAura.rotationMode.currentMode.equals("None") && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        preAimRange = new NumberSetting("Pre Aim Range", "\u0418\u0433\u0440\u043e\u043a \u0431\u0443\u0434\u0435\u0442 \u043d\u0430\u0432\u043e\u0434\u0438\u0442\u044c\u0441\u044f \u0434\u043e \u0430\u0442\u0430\u043a\u0438", 0.0f, 0.0f, 4.0f, 0.1f, () -> !KillAura.rotationMode.currentMode.equals("None"));
        players = new BooleanSetting("Players", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432", true, () -> true);
        armorStands = new BooleanSetting("Armor Stands", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0430\u0440\u043c\u043e\u0440-\u0441\u0442\u0435\u043d\u0434\u044b", false, () -> true);
        mobs = new BooleanSetting("Mobs", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u043c\u043e\u0431\u043e\u0432", false, () -> true);
        team = new BooleanSetting("Team Check", "\u041d\u0435 \u0431\u044c\u0435\u0442 \u0442\u0438\u043c\u0435\u0439\u0442\u043e\u0432 \u043d\u0430 \u043c\u0438\u043d\u0438-\u0438\u0433\u0440\u0430\u0445", false, () -> true);
        invis = new BooleanSetting("Invisible", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u043d\u0435\u0432\u0438\u0434\u0435\u043c\u044b\u0445 \u0441\u0443\u0449\u0435\u0441\u0442\u0432", true, () -> true);
        nakedPlayer = new BooleanSetting("Ignore Naked Players", "\u041d\u0435 \u0431\u044c\u0435\u0442 \u0433\u043e\u043b\u044b\u0445 \u0438\u0433\u0440\u043e\u043a\u043e\u0432", false, () -> true);
        walls = new BooleanSetting("Walls", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", true, () -> true);
        wallsBypass = new BooleanSetting("Walls Bypass", "\u041e\u0431\u0445\u043e\u0434 \u0443\u0434\u0430\u0440\u043e\u0432 \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u0443", false, () -> true);
        rayTrace = new BooleanSetting("Ray-Trace", "\u041f\u0440\u043e\u0432\u0435\u0440\u044f\u0435\u0442 \u0447\u0442\u043e \u0431\u044b \u0432\u0430\u0448\u0430 \u0440\u043e\u0442\u0430\u0446\u0438\u044f \u0441\u043c\u043e\u0442\u0440\u0435\u043b\u0430 \u043d\u0430 \u0445\u0438\u0442\u0431\u043e\u043a\u0441 \u0438\u0433\u0440\u043e\u043a\u0430", true, () -> !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        rayTraceBox = new NumberSetting("Ray-Trace Box", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0440\u0430\u0434\u0438\u0443\u0441 \u0440\u0435\u0439-\u0442\u0440\u0435\u0439\u0441\u0430", 0.06f, -0.5f, 0.5f, 0.01f, () -> rayTrace.getCurrentValue() && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        sprinting = new BooleanSetting("Stop Sprinting", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u044b\u043a\u043b\u044e\u0447\u0430\u0435\u0442 \u0441\u043f\u0440\u0438\u043d\u0442", false, () -> true);
        autoJump = new BooleanSetting("Auto Jump", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u043f\u0440\u044b\u0436\u043e\u043a", false, () -> true);
        strafing = new BooleanSetting("Strafing", "\u0412\u044b \u0441\u043c\u043e\u0436\u0435\u0442\u0435 \u0441\u0442\u0440\u0435\u0439\u0444\u0438\u0442\u044c", false, () -> true);
        autoWeapon = new BooleanSetting("Auto Weapon", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0435\u0442 \u0432 \u0440\u0443\u043a\u0443 \u043c\u0435\u0447 \u0438\u043b\u0438 \u0434\u0440\u0443\u0433\u043e\u0435 \u043e\u0440\u0443\u0436\u0438\u0435", false, () -> true);
        weaponOnly = new BooleanSetting("Weapon Only", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0442\u043e\u043b\u044c\u043a\u043e \u0441 \u043e\u0440\u0443\u0436\u0438\u0435\u043c \u0432 \u0440\u0443\u043a\u0430\u0445", false, () -> true);
        usingItemCheck = new BooleanSetting("Using Item Check", "\u041d\u0435 \u0431\u044c\u0435\u0442 \u0435\u0441\u043b\u0438 \u0432\u044b \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0435\u0442\u0435 \u043c\u0435\u0447, \u0435\u0434\u0443 \u0438 \u0442.\u0434", false, () -> true);
        shieldBreaker = new BooleanSetting("Shield Breaker", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043b\u043e\u043c\u0430\u0435\u0442 \u0449\u0438\u0442 \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0443", false, () -> true);
        shieldBlockTicks = new NumberSetting("Shield Block Ticks", "\u041f\u0440\u043e\u0432\u0435\u0440\u044f\u0435\u0442 \u043a\u0430\u043a \u0434\u043e\u043b\u0433\u043e \u0438\u0433\u0440\u043e\u043a \u0431\u0443\u0434\u0435\u0442 \u0434\u0435\u0440\u0436\u0430\u0442\u044c \u0449\u0438\u0442 \u043f\u0440\u0435\u0436\u0434\u0435 \u0447\u0435\u043c \u0441\u043b\u043e\u043c\u0430\u0442\u044c \u0435\u0433\u043e", 2.0f, 1.0f, 5.0f, 1.0f, () -> shieldBreaker.getCurrentValue());
        shieldFixer = new BooleanSetting("Shield Fixer", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0447\u0435\u0440\u0435\u0437 \u0449\u0438\u0442 (\u043e\u0431\u0445\u043e\u0434)", false, () -> true);
        fixerDelay = new NumberSetting("Fixer Delay", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u043a\u0430\u043a \u0434\u043e\u043b\u0433\u043e \u0449\u0438\u0442 \u0431\u0443\u0434\u0435\u0442 \u043e\u0442\u0436\u043c\u0438\u043c\u0430\u0442\u044c\u0441\u044f (\u0447\u0435\u043c \u0431\u043e\u043b\u044c\u0448\u0435, \u0442\u0435\u043c \u0449\u0438\u0442 \u0431\u0443\u0434\u0435\u0442 \u0434\u043e\u043b\u044c\u0448\u0435 \u043e\u0442\u0436\u0438\u043c\u0430\u0442\u044c\u0441\u044f)", 150.0f, 0.0f, 600.0f, 10.0f, () -> shieldFixer.getCurrentValue());
        setRotations = new BooleanSetting("Set Rotations", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043d\u0430\u0432\u043e\u0434\u0438\u0442 \u0432\u0430\u0441 \u043d\u0430 \u0446\u0435\u043b\u044c \u0434\u043b\u044f \u0442\u043e\u0433\u043e \u0447\u0442\u043e \u0431\u044b \u0431\u044b\u0441\u0442\u0440\u0435\u0435 \u0441\u043b\u043e\u043c\u0430\u0442\u044c \u0449\u0438\u0442", false, () -> shieldBreaker.getCurrentValue());
        breakRadius = new NumberSetting("Break Radius", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 FOV \u043f\u0440\u0438 \u043a\u043e\u0442\u043e\u0440\u043e\u043c \u0432\u044b \u0441\u043c\u043e\u0436\u0435\u0442\u0435 \u0441\u043b\u043e\u043c\u0430\u0442\u044c \u0449\u0438\u0442", 75.0f, 5.0f, 180.0f, 1.0f, () -> shieldBreaker.getCurrentValue());
        breakerDelay = new NumberSetting("Breaker Delay", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u043b\u043e\u043c\u0430\u043d\u0438\u044f \u0449\u0438\u0442\u0430", 50.0f, 0.0f, 1000.0f, 10.0f, () -> shieldBreaker.getCurrentValue());
        shieldDesync = new BooleanSetting("Shield Desync", "\u0414\u0435\u0441\u0438\u043d\u043a\u0430\u0435\u0442 \u0449\u0438\u043b\u0434\u0431\u0440\u0435\u0439\u043a\u0435\u0440\u044b \u0434\u0440\u0443\u0433\u0438\u0445 \u0447\u0438\u0442\u0435\u0440\u043e\u0432", false, () -> true);
        autoShieldUnPress = new BooleanSetting("Auto Shield UnPress", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043e\u0442\u0436\u043c\u043c\u0430\u0435\u0442 \u0449\u0438\u0442 \u0435\u0441\u043b\u0438 \u0443 \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0430 \u0442\u043e\u043f\u043e\u0440 \u0432 \u0440\u0443\u043a\u0430\u0445", false, () -> true);
        shieldBlockCheck = new BooleanSetting("Shield Block Check", "\u041d\u0435 \u0431\u044c\u0435\u0442 \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0430 \u0435\u0441\u043b\u0438 \u043e\u043d \u043f\u0440\u0438\u043a\u0440\u044b\u0442 \u0449\u0438\u0442\u043e\u043c", false, () -> true);
        autoBlock = new BooleanSetting("Auto Block", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0436\u043c\u0435\u0442 \u043f\u043a\u043c \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435 (\u043d\u0443\u0436\u043d\u043e \u0434\u043b\u044f 1.8 \u0441\u0435\u0440\u0432\u0435\u0440\u043e\u0432)", false, () -> true);
        sendDelay = new NumberSetting("Send Block Delay", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u0434\u0435\u043b\u044d\u044f \u0434\u043b\u044f \u0430\u0432\u0442\u043e\u0431\u043b\u043e\u043a\u0430", 100.0f, 0.0f, 300.0f, 10.0f, () -> autoBlock.getCurrentValue());
        outDelay = new NumberSetting("Out Block Delay", "\u041a\u0430\u043a \u0434\u043e\u043b\u0433\u043e \u0438\u0433\u0440\u043e\u043a \u0431\u0443\u0434\u0435\u0442 \u043e\u0442\u0436\u0438\u043c\u0430\u0442\u044c \u0430\u0432\u0442\u043e\u0431\u043b\u043e\u043a", 0.0f, 0.0f, 300.0f, 10.0f, () -> autoBlock.getCurrentValue());
        autoDisable = new BooleanSetting("Auto Disable", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u044b\u043a\u043b\u044e\u0447\u0430\u0435\u0442 \u043a\u0438\u043b\u043b\u0430\u0443\u0440\u0430 \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438 \u0438 \u0442.\u0434", false, () -> true);
        onlyCrit = new BooleanSetting("Only Crits", "\u0411\u044c\u0435\u0442 \u0432 \u043d\u0443\u0436\u043d\u044b\u0439 \u043c\u043e\u043c\u0435\u043d\u0442 \u0434\u043b\u044f \u043a\u0440\u0438\u0442\u0430", false, () -> true);
        spaceOnly = new BooleanSetting("Space Only", "Only Crits \u0431\u0443\u0434\u0443\u0442 \u0440\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u0435\u0441\u043b\u0438 \u0437\u0430\u0436\u0430\u0442 \u043f\u0440\u043e\u0431\u0435\u043b", false, () -> onlyCrit.getCurrentValue());
        adaptiveCrits = new BooleanSetting("Adaptive Crits", "\u0411\u044c\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u0430 \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0443\u0434\u0430\u0440\u043e\u043c \u0435\u0441\u043b\u0438 \u0443 \u043d\u0435\u0433\u043e \u043c\u0430\u043b\u043e \u0445\u043f \u0447\u0442\u043e \u0431\u044b \u0434\u043e\u0431\u0438\u0442\u044c", false, () -> onlyCrit.getCurrentValue());
        adaptiveCritsHealth = new NumberSetting("Adaptive Crits Health", "\u041a\u0430\u043a\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0445\u043f \u0431\u0443\u0434\u0435\u0442 \u0443 \u0438\u0433\u0440\u043e\u043a\u0430 \u0447\u0442\u043e \u0431\u044b \u043a\u0438\u043b\u043b\u0430\u0443\u0440\u0430 \u0431\u0438\u043b\u0430 \u043e\u0431\u044b\u0447\u043d\u044b\u043c \u0443\u0434\u0430\u0440\u043e\u043c (10 \u0445\u043f = 5 \u0441\u0435\u0440\u0434\u0435\u0446)", 5.0f, 0.5f, 10.0f, 0.5f, () -> onlyCrit.getCurrentValue() && adaptiveCrits.getCurrentValue());
        pullDown = new BooleanSetting("Pull Down", "\u0422\u044f\u043d\u0435\u0442 \u0432\u0430\u0441 \u0432\u043d\u0438\u0437, \u0442\u0435\u043c \u0441\u0430\u043c\u044b\u043c \u0432\u044b \u0431\u044b\u0441\u0442\u0440\u0435\u0435 \u043f\u0440\u044b\u0433\u0430\u0435\u0442\u0435, \u0431\u0443\u0434\u0435\u0442 \u043f\u043e\u043b\u0435\u0437\u043d\u043e \u0434\u043b\u044f Only Crits", false, () -> true);
        pullStength = new NumberSetting("Pull Strength", "\u041a\u0430\u043a \u0441\u0438\u043b\u044c\u043d\u043e \u0432\u0430\u0441 \u0431\u0443\u0434\u0435\u0442 \u0442\u044f\u043d\u0443\u0442\u044c \u0432\u043d\u0438\u0437", 0.005f, 0.001f, 0.1f, 0.001f, () -> pullDown.getCurrentValue());
        critFallDistance = new NumberSetting("Crits Fall Distance", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u0434\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u0438 \u0434\u043e \u0437\u0435\u043c\u043b\u0438 \u0434\u043b\u044f \u043a\u0440\u0438\u0442\u0430", 0.2f, 0.08f, 0.42f, 0.01f, () -> onlyCrit.getCurrentValue());
        targetHud = new BooleanSetting("TargetHUD", "\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0430\u0435\u0442 \u0445\u043f, \u0435\u0434\u0443, \u0431\u0440\u043e\u043d\u044e \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0430 \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435", true, () -> true);
        targetHudColor = new ColorSetting("TargetHUD Color", new Color(200, 78, 205, 25).brighter().getRGB(), () -> targetHud.getCurrentValue() && (KillAura.targetHudMode.currentMode.equals("Astolfo") || KillAura.targetHudMode.currentMode.equals("Glow") || KillAura.targetHudMode.currentMode.equals("Novoline Old") || KillAura.targetHudMode.currentMode.equals("Novoline New")));
        circle = new BooleanSetting("Circle around entity", "\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0430\u0435\u0442 \u043a\u0440\u0443\u0433 \u0432\u043e\u043a\u0440\u0443\u0433 \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0430", false, () -> true);
        circleMode = new ListSetting("Circle Mode", "Jello", () -> circle.getCurrentValue(), "Jello", "Astolfo");
        segments = new NumberSetting("Circle Segments", "\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 \u0443\u0433\u043b\u043e\u0432 \u0443 \u043a\u0440\u0443\u0433\u0430", 45.0f, 3.0f, 45.0f, 1.0f, () -> circle.getCurrentValue() && KillAura.circleMode.currentMode.equals("Astolfo"));
        circleRange = new NumberSetting("Circle Range", "\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 \u0434\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u0438 \u043a\u0440\u0443\u0433\u0430", 1.5f, 0.5f, 7.0f, 0.5f, () -> circle.getCurrentValue() && KillAura.circleMode.currentMode.equals("Astolfo"));
        circleSpeed = new NumberSetting("Circle Speed", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u0443\u0435\u0442 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c Jello \u043a\u0440\u0443\u0433\u0430", 0.01f, 0.001f, 0.05f, 0.001f, () -> circle.getCurrentValue() && KillAura.circleMode.currentMode.equals("Jello"));
        simsMark = new BooleanSetting("Sims Mark", "\u041e\u0442\u043e\u0431\u0440\u043e\u0436\u0430\u0435\u0442 \u0440\u043e\u043c\u0431 \u043d\u0430\u0434 \u0433\u043e\u043b\u043e\u0432\u043e\u0439 \u043f\u0440\u043e\u0442\u0438\u0432\u043d\u0438\u043a\u0430", false, () -> true);
        this.addSettings(rotationMode, clickMode, attackCoolDown, oldPvpSystemDelayMin, oldPvpSystemDelayMax, tpsSync, strafeMode, rotationStrafeMode, targetHud, targetHudMode, targetHudColor, fov, range, hitChance, rotPredict, rotSpeed, rotYawRandom, rotPitchRandom, rotPitchDown, preAimRange, sort, visualYaw, visualPitch, players, armorStands, mobs, invis, team, nakedPlayer, walls, wallsBypass, rayTrace, rayTraceBox, sprinting, autoJump, strafing, autoWeapon, weaponOnly, usingItemCheck, autoShieldUnPress, shieldBlockCheck, shieldBreaker, setRotations, shieldBlockTicks, breakRadius, breakerDelay, shieldFixer, fixerDelay, shieldDesync, autoBlock, sendDelay, outDelay, autoDisable, onlyCrit, spaceOnly, adaptiveCrits, critFallDistance, adaptiveCritsHealth, pullDown, pullStength, circle, circleMode, this.jelloMode, this.jelloColor, circleSpeed, circleRange, segments, simsMark, this.simsMarkMode, this.simsMarkColor);
    }

    public static boolean canApsAttack() {
        int apsMultiplier = 14 / MathematicHelper.middleRandomize((int)oldPvpSystemDelayMax.getCurrentValue(), (int)oldPvpSystemDelayMin.getCurrentValue());
        if (oldTimerPvp.hasReached(50 * apsMultiplier)) {
            oldTimerPvp.reset();
            return true;
        }
        return false;
    }

    private int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = KillAura.mc.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemAxe)) continue;
            return i;
        }
        return 1;
    }

    @Override
    public void onDisable() {
        target = null;
        this.circleAnim = 0.0;
        if (this.isBlocking) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.isBlocking = false;
        }
        critCounter = 0;
        isBreaked = false;
        shieldTicks = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        this.circleAnim = 0.0;
        super.onEnable();
    }

    @EventTarget
    public void onStrafeUpdate(EventUpdate event) {
        if (!strafing.getCurrentValue()) {
            return;
        }
        if (target == null) {
            return;
        }
        if (KillAura.mc.player.isInWater() || KillAura.mc.player.isInLava() || KillAura.mc.player.isRiding()) {
            return;
        }
        if (MovementHelper.isMoving()) {
            MovementHelper.setSpeed(MovementHelper.getSpeed() * 1.013f);
        }
    }

    @EventTarget
    public void onStrafe(EventStrafe eventStrafe) {
        String mode = strafeMode.getOptions();
        String rotStrafeMode = rotationStrafeMode.getOptions();
        if (target == null || mc.isSingleplayer()) {
            return;
        }
        if (!KillAura.target.isDead && (mode.equalsIgnoreCase("Always") || mode.equalsIgnoreCase("No Air"))) {
            if (mode.equalsIgnoreCase("No Air") && KillAura.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            if (rotStrafeMode.equalsIgnoreCase("Silent")) {
                eventStrafe.setCancelled(true);
                MovementHelper.calculateSilentMove(eventStrafe, RotationHelper.Rotation.packetYaw);
            } else if (rotStrafeMode.equalsIgnoreCase("Default")) {
                float yaw = RotationHelper.Rotation.packetYaw;
                float strafe = eventStrafe.getStrafe();
                float forward = eventStrafe.getForward();
                float friction = eventStrafe.getFriction();
                float f = strafe * strafe + forward * forward;
                if (f >= 1.0E-4f) {
                    if ((f = MathHelper.sqrt(f)) < 1.0f) {
                        f = 1.0f;
                    }
                    f = friction / f;
                    float yawSin = MathHelper.sin((float)((double)yaw * Math.PI / 180.0));
                    float yawCos = MathHelper.cos((float)((double)yaw * Math.PI / 180.0));
                    KillAura.mc.player.motionX += (double)((strafe *= f) * yawCos - (forward *= f) * yawSin);
                    KillAura.mc.player.motionZ += (double)(forward * yawCos + strafe * yawSin);
                }
                eventStrafe.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onJumpMotion(EventJump eventJump) {
        String mode = strafeMode.getOptions();
        if (target == null) {
            return;
        }
        if (!KillAura.target.isDead && (mode.equalsIgnoreCase("Always") || mode.equalsIgnoreCase("No Air"))) {
            if (mode.equalsIgnoreCase("No Air") && KillAura.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            eventJump.setYaw(RotationHelper.Rotation.packetYaw);
        }
    }

    @EventTarget
    public void onPreAttack(EventPreMotion preAttack) {
        if (Celestial.instance.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Old Matrix") && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && target != null && preAttack.isOnGround()) {
            preAttack.setOnGround(false);
        }
        this.doAuraPre(preAttack);
    }

    @EventTarget
    public void onRotations(EventPreMotion event) {
        String mode = rotationMode.getOptions();
        int speedMultiplier = 50;
        if (target == null) {
            return;
        }
        if (!KillAura.target.isDead) {
            if (!(KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && weaponOnly.getCurrentValue()) {
                return;
            }
            float[] rots = RotationHelper.getRotationsCustom(target, rotSpeed.getCurrentValue() * (float)speedMultiplier, !KillAura.rotationMode.currentMode.equals("Sunrise"));
            if (mode.equalsIgnoreCase("Packet")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            } else if (mode.equalsIgnoreCase("Sunrise")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            } else if (mode.equalsIgnoreCase("ReallyWorld")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            } else if (mode.equalsIgnoreCase("Client")) {
                KillAura.mc.player.rotationYaw = rots[0];
                KillAura.mc.player.rotationPitch = rots[1];
            }
            if (!(KillAura.rotationMode.currentMode.equals("AAC") || KillAura.rotationMode.currentMode.equals("None") || KillAura.rotationMode.currentMode.equals("Snap"))) {
                if (visualYaw.getCurrentValue()) {
                    KillAura.mc.player.renderYawOffset = rots[0];
                    KillAura.mc.player.rotationYawHead = rots[0];
                }
                if (visualPitch.getCurrentValue()) {
                    KillAura.mc.player.rotationPitchHead = rots[1];
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (autoDisable.getCurrentValue() && (KillAura.mc.currentScreen instanceof GuiGameOver && !KillAura.mc.player.isEntityAlive() || KillAura.mc.player.ticksExisted <= 1)) {
            EventManager.unregister(this);
            this.onDisable();
            if (this.getState()) {
                this.setState(false);
            }
            NotificationManager.publicity("AutoDisable", "KillAura was toggled off!", 4, NotificationType.INFO);
        }
        if (target == null) {
            return;
        }
        if (autoJump.getCurrentValue() && KillAura.mc.player.onGround && !KillAura.mc.player.isRiding() && !KillAura.mc.player.isOnLadder() && !KillAura.mc.player.isInLiquid() && !KillAura.mc.player.isInWeb) {
            KillAura.mc.gameSettings.keyBindJump.pressed = false;
            if (!Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() || !Celestial.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
                MovementHelper.strafePlayer();
            }
            KillAura.mc.player.jump();
        }
        if (autoShieldUnPress.getCurrentValue() && KillAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (KillAura.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    KillAura.mc.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                KillAura.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
        if (shieldDesync.getCurrentValue() && KillAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.NORTH));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        isAttacking = true;
        if (KillAura.mc.player.isBlocking() && this.shieldFixerTimer.hasReached(fixerDelay.getCurrentValue()) && KillAura.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield && shieldFixer.getCurrentValue()) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.UP));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
            this.shieldFixerTimer.reset();
        }
    }

    private void attackEntitySuccess(EntityLivingBase target) {
        if (target == null || KillAura.mc.player.getHealth() < 0.0f) {
            return;
        }
        if (KillAura.mc.player.getDistanceToEntity(target) > range.getCurrentValue()) {
            return;
        }
        if (!target.isDead) {
            switch (clickMode.getOptions()) {
                case "1.9": {
                    float attackDelay = tpsSync.getCurrentValue() ? attackCoolDown.getCurrentValue() * TpsHelper.getTickRate() / 20.0f : attackCoolDown.getCurrentValue();
                    if (!(KillAura.mc.player.getCooledAttackStrength(0.0f) >= attackDelay) || !((float)MathematicHelper.spikeRandomize(100, 0) < hitChance.getCurrentValue())) break;
                    if (Celestial.instance.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Sunrise Ground")) {
                        MovementHelper.setSpeed(MovementHelper.getSpeed());
                        if (critCounter == 3) {
                            critCounter = 0;
                        } else {
                            if (critCounter == 0 && KillAura.mc.player.onGround) {
                                KillAura.mc.player.moveEntity(MoverType.PLAYER, 0.0, 0.305999999359984, 0.0);
                            }
                            ++critCounter;
                            break;
                        }
                    }
                    KillAura.mc.playerController.attackEntity(KillAura.mc.player, target);
                    KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    break;
                }
                case "1.8": {
                    if (!KillAura.canApsAttack()) break;
                    if (this.isBlocking && autoBlock.getCurrentValue() && KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && this.blockTimer1.hasReached(sendDelay.getCurrentValue())) {
                        KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                        this.isBlocking = false;
                        this.blockTimer1.reset();
                    }
                    if (!((float)MathematicHelper.spikeRandomize(100, 0) < hitChance.getCurrentValue())) break;
                    KillAura.mc.playerController.attackEntity(KillAura.mc.player, target);
                    KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private void doAuraPre(EventPreMotion event) {
        boolean flag;
        if (KillAura.mc.player.getHealth() > 0.0f) {
            String mode = rotationMode.getOptions();
            this.setSuffix(mode);
            target = KillAuraHelper.getSortEntities();
            if (target == null) {
                return;
            }
            if (KillAura.mc.player.isUsingItem() && usingItemCheck.getCurrentValue()) {
                return;
            }
            if (!(KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && weaponOnly.getCurrentValue()) {
                return;
            }
            if (target.isActiveItemStackBlocking(5) && target.isBlocking() && target.isHandActive() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield) && shieldBlockCheck.getCurrentValue() && RotationHelper.isAimAtMe(target, 85.0f)) {
                return;
            }
        } else if (autoDisable.getCurrentValue()) {
            this.toggle();
            NotificationManager.publicity("KillAura", "KillAura was disabled because of Death!", 3, NotificationType.INFO);
        }
        if (autoWeapon.getCurrentValue()) {
            KillAura.mc.player.inventory.currentItem = EntityHelper.getBestWeapon();
            KillAura.mc.playerController.updateController();
        }
        if (pullDown.getCurrentValue() && !KillAura.mc.player.onGround) {
            KillAura.mc.player.motionY -= (double)pullStength.getCurrentValue();
        }
        float[] rots = RotationHelper.getRotationsCustom(target, Float.MAX_VALUE, !KillAura.rotationMode.currentMode.equals("Sunrise"));
        float yaw = KillAura.rotationMode.currentMode.equals("Packet") || KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") || KillAura.rotationMode.currentMode.equals("AAC") ? RotationHelper.Rotation.packetYaw : KillAura.mc.player.rotationYaw;
        float pitch = KillAura.rotationMode.currentMode.equals("Packet") || KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") || KillAura.rotationMode.currentMode.equals("AAC") ? RotationHelper.Rotation.packetPitch : KillAura.mc.player.rotationPitch;
        KillAura.mc.player.jumpTicks = 0;
        BlockPos blockPos = new BlockPos(KillAura.mc.player.posX, KillAura.mc.player.posY - 0.1, KillAura.mc.player.posZ);
        Block block = KillAura.mc.world.getBlockState(blockPos).getBlock();
        float f2 = KillAura.mc.player.getCooledAttackStrength(0.5f);
        boolean bl = flag = f2 > 0.9f;
        if (!flag && onlyCrit.getCurrentValue()) {
            return;
        }
        if (!(adaptiveCrits.getCurrentValue() && target.getHealth() / 2.0f <= adaptiveCritsHealth.getCurrentValue() || Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState() && Flight.flyMode.currentMode.equals("Sunrise Disabler") || !KillAura.mc.gameSettings.keyBindJump.isKeyDown() && spaceOnly.getCurrentValue())) {
            if (MovementHelper.airBlockAboveHead()) {
                if (!(KillAura.mc.player.fallDistance >= critFallDistance.getCurrentValue() || block instanceof BlockLiquid || !onlyCrit.getCurrentValue() || KillAura.mc.player.isRiding() || KillAura.mc.player.isOnLadder() || KillAura.mc.player.isInLiquid() || KillAura.mc.player.isInWeb)) {
                    KillAura.mc.player.connection.sendPacket(new CPacketEntityAction(KillAura.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    return;
                }
            } else if (!(!(KillAura.mc.player.fallDistance > 0.0f) || KillAura.mc.player.onGround || !onlyCrit.getCurrentValue() || KillAura.mc.player.isRiding() || KillAura.mc.player.isOnLadder() || KillAura.mc.player.isInLiquid() || KillAura.mc.player.isInWeb)) {
                KillAura.mc.player.connection.sendPacket(new CPacketEntityAction(KillAura.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                return;
            }
        }
        if (KillAura.rotationMode.currentMode.equalsIgnoreCase("AAC")) {
            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
            if (visualYaw.getCurrentValue()) {
                KillAura.mc.player.renderYawOffset = rots[0];
                KillAura.mc.player.rotationYawHead = rots[0];
            }
            if (visualPitch.getCurrentValue()) {
                KillAura.mc.player.rotationPitchHead = rots[1];
            }
            if (!RotationHelper.isLookingAtEntity(false, yaw, pitch, rayTraceBox.getCurrentValue(), rayTraceBox.getCurrentValue(), rayTraceBox.getCurrentValue(), target, range.getCurrentValue() + preAimRange.getCurrentValue())) {
                return;
            }
        }
        if (KillAura.rotationMode.currentMode.equalsIgnoreCase("Sunrise") || KillAura.rotationMode.currentMode.equalsIgnoreCase("ReallyWorld") ? !RotationHelper.isLookingAtEntity(false, yaw, pitch, 0.12f, 0.12f, 0.12f, target, range.getCurrentValue() + preAimRange.getCurrentValue()) : !KillAura.rotationMode.currentMode.equals("Snap") && !RotationHelper.isLookingAtEntity(false, yaw, pitch, rayTraceBox.getCurrentValue(), rayTraceBox.getCurrentValue(), rayTraceBox.getCurrentValue(), target, range.getCurrentValue() + preAimRange.getCurrentValue()) && rayTrace.getCurrentValue()) {
            return;
        }
        canDo = true;
        if (Celestial.instance.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Sunrise Air")) {
            return;
        }
        if (KillAura.rotationMode.currentMode.equals("Snap")) {
            float[] rots1 = RotationHelper.getRotationsCustom(target, rotSpeed.getCurrentValue() * 50.0f, true);
            if (KillAura.mc.player.getCooledAttackStrength(0.0f) >= attackCoolDown.getCurrentValue()) {
                KillAura.mc.player.rotationYaw = rots1[0];
                KillAura.mc.player.rotationPitch = rots1[1];
            }
        }
        this.attackEntitySuccess(EntityHelper.rayCast(target, range.getCurrentValue()));
    }

    @EventTarget
    public void onPostMotion(EventPostMotion event) {
        if (target == null) {
            return;
        }
        if (KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && !this.isBlocking && autoBlock.getCurrentValue() && this.blockTimer.hasReached(outDelay.getCurrentValue())) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            this.blockTimer.reset();
            this.isBlocking = true;
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (target == null) {
            return;
        }
        if (!InventoryHelper.doesHotbarHaveAxe()) {
            return;
        }
        if (autoWeapon.getCurrentValue()) {
            return;
        }
        if (shieldBreaker.getCurrentValue() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield)) {
            if (target.isBlocking() && target.isHandActive() && target.isActiveItemStackBlocking(shieldBlockTicks.getCurrentValueInt())) {
                float pitch;
                float yaw = KillAura.rotationMode.currentMode.equals("Packet") || KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") || KillAura.rotationMode.currentMode.equals("AAC") ? RotationHelper.Rotation.packetYaw : KillAura.mc.player.rotationYaw;
                float f = pitch = KillAura.rotationMode.currentMode.equals("Packet") || KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") || KillAura.rotationMode.currentMode.equals("AAC") ? RotationHelper.Rotation.packetPitch : KillAura.mc.player.rotationPitch;
                if (!RotationHelper.isLookingAtEntity(false, yaw, pitch, 0.06f, 0.06f, 0.06f, target, range.getCurrentValue() + preAimRange.getCurrentValue()) && !KillAura.rotationMode.currentMode.equals("None")) {
                    return;
                }
                if (RotationHelper.isAimAtMe(target, breakRadius.getCurrentValue())) {
                    if (setRotations.getCurrentValue()) {
                        float[] rots = RotationHelper.getRotationsCustom(target, Float.MAX_VALUE, !KillAura.rotationMode.currentMode.equals("Sunrise"));
                        KillAura.mc.player.rotationYaw = rots[0];
                        KillAura.mc.player.rotationPitch = rots[1];
                    }
                    if (KillAura.mc.player.inventory.currentItem != this.getAxe()) {
                        KillAura.mc.player.inventory.currentItem = this.getAxe();
                        KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
                    }
                    if (KillAura.mc.player.inventory.currentItem == this.getAxe()) {
                        if (this.shieldBreakerTimer.hasReached(breakerDelay.getCurrentValue())) {
                            isBreaked = true;
                            KillAura.mc.playerController.attackEntity(KillAura.mc.player, target);
                            KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                            KillAura.mc.player.resetCooldown();
                            this.shieldBreakerTimer.reset();
                        }
                        this.changeSlotCounter = -1;
                    } else {
                        this.changeSlotCounter = 0;
                    }
                }
            } else if (!(KillAura.mc.player.inventory.currentItem == InventoryHelper.getSwordAtHotbar() || this.changeSlotCounter != -1 || InventoryHelper.getSwordAtHotbar() == -1 || target.isBlocking() && target.isHandActive() && target.isActiveItemStackBlocking(2))) {
                KillAura.mc.player.inventory.currentItem = InventoryHelper.getSwordAtHotbar();
                KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
                this.changeSlotCounter = 0;
                NotificationManager.publicity("Shield-Breaker", "Successfully destroyed " + target.getName() + " shield", 2, NotificationType.SUCCESS);
                isBreaked = false;
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (!this.getState() && target != null) {
            return;
        }
        for (DraggableModule draggableModule : Celestial.instance.draggableManager.getMods()) {
            if (!this.getState() || !(draggableModule instanceof TargetHUDComponent)) continue;
            draggableModule.draw();
        }
    }

    @EventTarget
    public void onRenderCircle(EventRender3D event) {
        double z;
        double y;
        int color;
        int oneColor;
        if (target == null) {
            return;
        }
        if (circle.getCurrentValue() && KillAura.mc.player.getDistanceToEntity(target) <= range.getCurrentValue() + preAimRange.getCurrentValue()) {
            if (target != null && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.mc.player.getDistanceToEntity(target) <= range.getCurrentValue() + preAimRange.getCurrentValue()) {
                if (KillAura.circleMode.currentMode.equals("Astolfo")) {
                    if (!KillAura.target.isDead) {
                        this.circleAnim += (double)0.05f * Minecraft.frameTime / 10.0;
                        this.circleAnim = MathHelper.clamp(this.circleAnim, 0.0, (double)circleRange.getCurrentValue());
                        RenderHelper.drawCircle3D(target, this.circleAnim - 0.006, event.getPartialTicks(), (int)segments.getCurrentValue(), 6.0f, Color.BLACK.getRGB());
                        RenderHelper.drawCircle3D(target, this.circleAnim + 0.006, event.getPartialTicks(), (int)segments.getCurrentValue(), 6.0f, Color.BLACK.getRGB());
                        RenderHelper.drawCircle3D(target, this.circleAnim, event.getPartialTicks(), (int)segments.getCurrentValue(), 2.0f, -1);
                    } else {
                        this.circleAnim = 0.0;
                    }
                } else if (KillAura.circleMode.currentMode.equals("Jello") && !KillAura.target.isDead) {
                    double iCos;
                    double iSin;
                    int i;
                    oneColor = this.jelloColor.getColor();
                    color = 0;
                    switch (this.jelloMode.currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            color = oneColor;
                            break;
                        }
                        case "Astolfo": {
                            color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                        }
                    }
                    double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * (double)KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosX;
                    y = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * (double)KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosY;
                    z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * (double)KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosZ;
                    this.circleValue += (double)circleSpeed.getCurrentValue() * (Minecraft.frameTime * 0.1);
                    float targetHeight = (float)(0.5 * (1.0 + Math.sin(Math.PI * 2 * (this.circleValue * (double)0.3f))));
                    float size = KillAura.target.width + (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Chinchilla")) && target instanceof EntityPlayer && !CustomModel.onlyMe.getCurrentValue() ? 0.3f : 0.0f);
                    float endYValue = (float)(((Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus") && target instanceof EntityPlayer && !CustomModel.onlyMe.getCurrentValue() ? 1.3 : (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Red Panda")) && !CustomModel.onlyMe.getCurrentValue() ? 0.7 : (double)KillAura.target.height)) * 1.0 + 0.2) * (double)targetHeight);
                    if ((double)targetHeight > 0.99) {
                        this.canDown = true;
                    } else if ((double)targetHeight < 0.01) {
                        this.canDown = false;
                    }
                    GlStateManager.enableBlend();
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(2848);
                    GlStateManager.disableDepth();
                    GlStateManager.disableTexture2D();
                    GlStateManager.disableAlpha();
                    GL11.glLineWidth(2.0f);
                    GL11.glShadeModel(7425);
                    GL11.glDisable(2884);
                    GL11.glBegin(5);
                    float alpha = (this.canDown ? 255.0f * targetHeight : 255.0f * (1.0f - targetHeight)) / 255.0f;
                    float red = (float)(color >> 16 & 0xFF) / 255.0f;
                    float green = (float)(color >> 8 & 0xFF) / 255.0f;
                    float blue = (float)(color & 0xFF) / 255.0f;
                    for (i = 0; i < 2166; ++i) {
                        RenderHelper.color(red, green, blue, alpha);
                        iSin = Math.sin(Math.toRadians(i)) * (double)size;
                        iCos = Math.cos(Math.toRadians(i)) * (double)size;
                        GL11.glVertex3d(x + iCos, y + (double)endYValue, z - iSin);
                        RenderHelper.color(red, green, blue, 0.0f);
                        GL11.glVertex3d(x + iCos, y + (double)endYValue + (double)(this.canDown ? -0.5f * (1.0f - targetHeight) : 0.5f * targetHeight), z - iSin);
                    }
                    GL11.glEnd();
                    GL11.glBegin(2);
                    RenderHelper.color(color);
                    for (i = 0; i < 361; ++i) {
                        iSin = Math.sin(Math.toRadians(i)) * (double)size;
                        iCos = Math.cos(Math.toRadians(i)) * (double)size;
                        GL11.glVertex3d(x + iCos, y + (double)endYValue, z - iSin);
                    }
                    GL11.glEnd();
                    GlStateManager.enableAlpha();
                    GL11.glShadeModel(7424);
                    GL11.glDisable(2848);
                    GL11.glEnable(2884);
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableDepth();
                    GlStateManager.disableBlend();
                    GlStateManager.resetColor();
                }
            } else {
                this.circleAnim = 0.0;
            }
        }
        if (simsMark.getCurrentValue()) {
            oneColor = this.simsMarkColor.getColor();
            color = 0;
            switch (this.simsMarkMode.currentMode) {
                case "Client": {
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color = oneColor;
                    break;
                }
                case "Astolfo": {
                    color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                }
            }
            double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * (double)KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosX;
            y = KillAura.target.lastTickPosY - (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus") && target instanceof EntityPlayer && !CustomModel.onlyMe.getCurrentValue() ? 0.5 : (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && target instanceof EntityPlayer && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Red Panda")) && !CustomModel.onlyMe.getCurrentValue() ? (double)1.1f : (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Freddy Bear") && target instanceof EntityPlayer && !CustomModel.onlyMe.getCurrentValue() ? -0.2 : 0.0))) + (KillAura.target.posY - KillAura.target.lastTickPosY) * (double)KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosY;
            z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * (double)KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosZ;
            double d = (double)target.getEyeHeight() + 0.35;
            double d2 = target.isSneaking() ? 0.25 : 0.0;
            double middle = 0.6;
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glTranslated(x, (y += d - d2) + middle + 0.1, z);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            RenderHelper.color(color);
            RenderHelper.drawCone(0.2f, 0.3f, 60, false);
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glTranslated(x, y + middle - 0.5, z);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            RenderHelper.color(color);
            RenderHelper.drawCone(0.2f, -0.3f, 60, false);
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GlStateManager.popMatrix();
        }
    }
}

