package dev.tenacity.module.impl.combat;

import dev.tenacity.Tenacity;
import dev.tenacity.commands.impl.FriendCommand;
import dev.tenacity.event.impl.player.*;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.impl.movement.Scaffold;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.module.settings.impl.*;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.player.InventoryUtils;
import dev.tenacity.utils.player.RotationUtils;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.server.PacketUtils;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.viamcp.utils.AttackOrder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class KillAura extends Module {

    public static boolean attacking;
    public static boolean blocking;
    public static boolean wasBlocking;
    private float yaw = 0;
    private int cps;
    public static EntityLivingBase target;
    public static final List<EntityLivingBase> targets = new ArrayList<>();
    private final TimerUtil attackTimer = new TimerUtil();
    private final TimerUtil switchTimer = new TimerUtil();

    private final MultipleBoolSetting targetsSetting = new MultipleBoolSetting("Targets",
            new BooleanSetting("Players", true),
            new BooleanSetting("Animals", false),
            new BooleanSetting("Mobs", false),
            new BooleanSetting("Invisibles", false));

    private final ModeSetting mode = new ModeSetting("Mode", "Single", "Single", "Multi");

    private final NumberSetting switchDelay = new NumberSetting("Switch Delay", 50, 500, 1, 1);
    private final NumberSetting maxTargetAmount = new NumberSetting("Max Target Amount", 3, 50, 2, 1);

    private final NumberSetting minCPS = new NumberSetting("Min CPS", 10, 20, 1, 1);
    private final NumberSetting maxCPS = new NumberSetting("Max CPS", 10, 20, 1, 1);
    private final NumberSetting reach = new NumberSetting("Reach", 4, 6, 3, 0.1);

    private final BooleanSetting autoblock = new BooleanSetting("Autoblock", false);

    private final ModeSetting autoblockMode = new ModeSetting("Autoblock Mode", "Watchdog", "Fake", "Verus", "Watchdog");

    private final BooleanSetting rotations = new BooleanSetting("Rotations", true);
    private final ModeSetting rotationMode = new ModeSetting("Rotation Mode", "Vanilla", "Vanilla", "Smooth");

    private final ModeSetting sortMode = new ModeSetting("Sort Mode", "Range", "Range", "Hurt Time", "Health", "Armor");

    private final MultipleBoolSetting addons = new MultipleBoolSetting("Addons",
            new BooleanSetting("Keep Sprint", true),
            new BooleanSetting("Through Walls", true),
            new BooleanSetting("Allow Scaffold", false),
            new BooleanSetting("Movement Fix", false),
            new BooleanSetting("Ray Cast", false));

    private final MultipleBoolSetting auraESP = new MultipleBoolSetting("Target ESP",
            new BooleanSetting("Circle", true),
            new BooleanSetting("Tracer", false),
            new BooleanSetting("Box", false),
            new BooleanSetting("Custom Color", false));
    private final ColorSetting customColor = new ColorSetting("Custom Color", Color.WHITE);
    private EntityLivingBase auraESPTarget;

    public KillAura() {
        super("KillAura", Category.COMBAT, "Automatically attacks players");
        autoblockMode.addParent(autoblock, a -> autoblock.isEnabled());
        rotationMode.addParent(rotations, r -> rotations.isEnabled());
        switchDelay.addParent(mode, m -> mode.is("Switch"));
        maxTargetAmount.addParent(mode, m -> mode.is("Multi"));
        customColor.addParent(auraESP, r -> r.isEnabled("Custom Color"));
        this.addSettings(targetsSetting, mode, maxTargetAmount, switchDelay, minCPS, maxCPS, reach, autoblock, autoblockMode,
                rotations, rotationMode, sortMode, addons, auraESP, customColor);
    }

    @Override
    public void onDisable() {
        target = null;
        targets.clear();
        blocking = false;
        attacking = false;
        if(wasBlocking) {
            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        wasBlocking = false;
        super.onDisable();
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        this.setSuffix(mode.getMode());

        if(minCPS.getValue() > maxCPS.getValue()) {
            minCPS.setValue(minCPS.getValue() - 1);
        }

        // Gets all entities in specified range, sorts them using your specified sort mode, and adds them to target list
        sortTargets();

        if (event.isPre()) {
            attacking = !targets.isEmpty() && (addons.getSetting("Allow Scaffold").isEnabled() || !Tenacity.INSTANCE.isEnabled(Scaffold.class));
            blocking = autoblock.isEnabled() && attacking && InventoryUtils.isHoldingSword();
            if (attacking) {
                target = targets.get(0);

                if (rotations.isEnabled()) {
                    float[] rotations = new float[]{0, 0};
                    switch (rotationMode.getMode()) {
                        case "Vanilla":
                            rotations = RotationUtils.getRotationsNeeded(target);
                            break;
                        case "Smooth":
                            rotations = RotationUtils.getSmoothRotations(target);
                            break;
                    }
                    yaw = event.getYaw();
                    event.setRotations(rotations[0], rotations[1]);
                    RotationUtils.setVisualRotations(event.getYaw(), event.getPitch());
                }

                if(addons.getSetting("Ray Cast").isEnabled() && !RotationUtils.isMouseOver(event.getYaw(), event.getPitch(), target, reach.getValue().floatValue()))
                    return;

                if (attackTimer.hasTimeElapsed(cps, true)) {
                    final int maxValue = (int) ((minCPS.getMaxValue() - maxCPS.getValue()) * 20);
                    final int minValue = (int) ((minCPS.getMaxValue() - minCPS.getValue()) * 20);
                    cps = MathUtils.getRandomInRange(minValue, maxValue);
                    if(mode.is("Multi")) {
                        for(EntityLivingBase entityLivingBase : targets) {
                            AttackEvent attackEvent = new AttackEvent(entityLivingBase);
                            Tenacity.INSTANCE.getEventProtocol().handleEvent(attackEvent);

                            if (!attackEvent.isCancelled()) {
                                AttackOrder.sendFixedAttack(mc.thePlayer, entityLivingBase);
                            }
                        }
                    } else {
                        AttackEvent attackEvent = new AttackEvent(target);
                        Tenacity.INSTANCE.getEventProtocol().handleEvent(attackEvent);

                        if (!attackEvent.isCancelled()) {
                            AttackOrder.sendFixedAttack(mc.thePlayer, target);
                        }
                    }
                }

            } else {
                target = null;
                switchTimer.reset();
            }
        }

        if (blocking) {
            switch (autoblockMode.getMode()) {
                case "Watchdog":
                    if (event.isPre() && wasBlocking && mc.thePlayer.ticksExisted % 4 == 0) {
                        PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        wasBlocking = false;
                    }

                    if (event.isPost() && !wasBlocking) {
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 255, 255, 255));
                        wasBlocking = true;
                    }
                    break;
                case "Verus":
                    if (event.isPre()) {
                        if (wasBlocking) {
                            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.
                                    Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                        wasBlocking = true;
                    }
                    break;
                case "Fake":
                    break;
            }
        } else if (wasBlocking && autoblockMode.is("Watchdog") && event.isPre()) {
            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.
                    Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            wasBlocking = false;
        }
    }

    private void sortTargets() {
        targets.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue() && isValid(entity) && mc.thePlayer != entityLivingBase && !FriendCommand.isFriend(entityLivingBase.getName())) {
                    targets.add(entityLivingBase);
                }
            }
        }
        switch (sortMode.getMode()) {
            case "Range":
                targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
                break;
            case "Hurt Time":
                targets.sort(Comparator.comparingInt(EntityLivingBase::getHurtTime));
                break;
            case "Health":
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case "Armor":
                targets.sort(Comparator.comparingInt(EntityLivingBase::getTotalArmorValue));
                break;
        }
    }

    public boolean isValid(Entity entity) {
        if (entity instanceof EntityPlayer && targetsSetting.getSetting("Players").isEnabled() && !entity.isInvisible() && mc.thePlayer.canEntityBeSeen(entity))
            return true;

        if (entity instanceof EntityPlayer && targetsSetting.getSetting("Invisibles").isEnabled() && entity.isInvisible())
            return true;

        if(entity instanceof EntityPlayer && addons.getSetting("Through Walls").isEnabled() && !mc.thePlayer.canEntityBeSeen(entity))
            return true;

        if (entity instanceof EntityAnimal && targetsSetting.getSetting("Animals").isEnabled())
            return true;

        if (entity instanceof EntityMob && targetsSetting.getSetting("Mobs").isEnabled())
            return true;

        if (entity.isInvisible() && targetsSetting.getSetting("Invisibles").isEnabled())
            return true;

        return false;
    }

    @Override
    public void onPlayerMoveUpdateEvent(PlayerMoveUpdateEvent event) {
        if(addons.getSetting("Movement Fix").isEnabled() && target != null){
            event.setYaw(yaw);
        }
    }

    @Override
    public void onJumpFixEvent(JumpFixEvent event) {
        if(addons.getSetting("Movement Fix").isEnabled() && target != null){
            event.setYaw(yaw);
        }
    }

    @Override
    public void onKeepSprintEvent(KeepSprintEvent event) {
        if(addons.getSetting("Keep Sprint").isEnabled()) {
            event.cancel();
        }
    }

    private final Animation auraESPAnim = new DecelerateAnimation(300, 1);

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        auraESPAnim.setDirection(target != null ? Direction.FORWARDS : Direction.BACKWARDS);
        if(target != null) {
            auraESPTarget = target;
        }

        if(auraESPAnim.finished(Direction.BACKWARDS)) {
            auraESPTarget = null;
        }

        Color color = HUDMod.getClientColors().getFirst();

        if(auraESP.isEnabled("Custom Color")){
            color = customColor.getColor();
        }


        if (auraESPTarget != null) {
            if (auraESP.getSetting("Box").isEnabled()) {
                RenderUtil.renderBoundingBox(auraESPTarget, color, auraESPAnim.getOutput().floatValue());
            }
            if (auraESP.getSetting("Circle").isEnabled()) {
                RenderUtil.drawCircle(auraESPTarget, event.getTicks(), .75f, color.getRGB(), auraESPAnim.getOutput().floatValue());
            }

            if (auraESP.getSetting("Tracer").isEnabled()) {
                RenderUtil.drawTracerLine(auraESPTarget, 4f, Color.BLACK, auraESPAnim.getOutput().floatValue());
                RenderUtil.drawTracerLine(auraESPTarget, 2.5f, color, auraESPAnim.getOutput().floatValue());
            }
        }
    }
}
