package xyz.northclient.features.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.*;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.util.MoveUtil;
import xyz.northclient.util.Stopwatch;
import xyz.northclient.util.killaura.RotationsUtil;
import xyz.northclient.util.killaura.TargetingUtil;
import xyz.northclient.util.shader.RenderUtil;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@ModuleInfo(name = "KillAura", description = "Recoded Kill Aura", category = Category.COMBAT, keyCode = Keyboard.KEY_R)
public class KillAura extends AbstractModule {
    public DoubleValue autoBlockRange = new DoubleValue("Auto Block range", this)
            .setMin(1)
            .setMax(6)
            .setIncrement(0.1)
            .setDefault(3);
    public DoubleValue rotationRange = new DoubleValue("Rotation range", this)
            .setMin(1)
            .setMax(6)
            .setIncrement(0.1)
            .setDefault(3);

    public DoubleValue range = new DoubleValue("Range", this)
            .setMin(1)
            .setMax(6)
            .setIncrement(0.1)
            .setDefault(3);
    public DoubleValue minCps = new DoubleValue("Min CPS", this)
            .setDefault(5)
            .setMin(1)
            .setMax(30)
            .enableOnlyInt();
    public DoubleValue maxCps = new DoubleValue("Max CPS", this)
            .setDefault(10)
            .setMin(1)
            .setMax(30)
            .enableOnlyInt();
    public ModeValue sortMode = new ModeValue("Sort Mode", this)
            .add(new Watermark.StringMode("Distance", this))
            .add(new Watermark.StringMode("Hurt", this))
            .add(new Watermark.StringMode("Health", this))
            .setDefault("Distance");
    public ModeValue rotations = new ModeValue("Rotations", this)
            .add(new Watermark.StringMode("Normal", this))
            .add(new Watermark.StringMode("Disabled", this))
            .setDefault("Normal");
    public ModeValue autoblock = new ModeValue("Auto Block", this)
            .add(new Watermark.StringMode("Fake", this))
            .add(new Watermark.StringMode("Disabled", this))
            .setDefault("Fake");
    public DoubleValue missChance = new DoubleValue("Miss chance (%)", this)
            .setDefault(5)
            .setMin(0)
            .setMax(100)
            .enableOnlyInt();

    public BoolValue raytrace = new BoolValue("RayCast", this)
            .setDefault(true);
    public BoolValue correctMovement = new BoolValue("Correct Movement", this)
            .setDefault(true);
    public BoolValue keepSprint = new BoolValue("Keep Sprint", this)
            .setDefault(true);

    private RotationsUtil fixedRotations;
    public static EntityLivingBase target = null;
    public boolean shouldAttack;
    public Stopwatch stopwatch = new Stopwatch();
    public float cps;
    public boolean block;

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
        shouldAttack = false;
        block = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        target = null;
        block = false;
        fixedRotations = new RotationsUtil(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    @EventLink
    public void onMove(TickEvent event) {
        List<Entity> entities = TargetingUtil.collectTargets();
        target = TargetingUtil.getCloset(entities, 5);


        boolean miss = (new SecureRandom().nextFloat() * 100) > missChance.get().intValue();
        cps = (float) (minCps.get().intValue() + new Random().nextInt((maxCps.get().intValue() - minCps.get().intValue()) + 1) + Math.random() * 0.2f);

        if (target == null) {
            return;
        }

        if (!autoblock.is("Disabled")) {
            performAutoblock();
        }

        if (!rotations.is("Disabled")) {
            performRotations();
        }

        shouldAttack = true;

        if (raytrace.get()) {
            MovingObjectPosition movingObjectPosition = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.getPosition())
                    .add(new Vec3(0, mc.thePlayer.getEyeHeight(), 0)), new Vec3(target.getPosition()));

            MovingObjectPosition movingObjectPosition2 = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.getPosition())
                    .add(new Vec3(0, mc.thePlayer.getEyeHeight(), 0)), new Vec3(target.getPosition())
                    .add(new Vec3(0, target.getEyeHeight(), 0)));

            if (movingObjectPosition != null && movingObjectPosition2 != null) {
                shouldAttack = false;
            }
        }


        if (shouldAttack && this.stopwatch.elapsed((long) (1000 / cps)) && target.getDistanceToEntity(mc.thePlayer) <= range.get().doubleValue()) {
            attack(target);
            stopwatch.reset();
        }

        if (correctMovement.get().booleanValue()) {
            MoveUtil.correctMovement(fixedRotations);
        }
    }

    @EventLink
    public void onMotion(MotionEvent e) {
        if (e.isPre() && target != null) {
            e.setYaw(fixedRotations.getYaw());
            e.setPitch(fixedRotations.getPitch());
            mc.thePlayer.renderYawOffset = fixedRotations.getYaw();
            mc.thePlayer.rotationYawHead = fixedRotations.getYaw();
        }
    }

    @EventLink
    public void onStrafe(StrafeEvent e) {
        if (target != null && correctMovement.get().booleanValue()) {
            float[] moveFix = MoveUtil.getFixedMovement(fixedRotations, e.getForward() != 0 ? Math.abs(e.getForward()) : Math.abs(e.getStrafe()), 0);

            e.setYaw(fixedRotations.getYaw());
            e.setForward(moveFix[0]);
            e.setStrafe(moveFix[1]);
        }
    }

    @EventLink
    public void onJump(JumpEvent e) {
        if (target != null && correctMovement.get().booleanValue()) {
            e.setYaw(fixedRotations.getYaw());
        }
    }

    @EventLink
    public void on3D(Render3DEvent event) {
        if (target != null) {
            RenderUtil.drawCircle(target, event.getTicks(), .75f, ColorUtil.GetColor(0), 0.5f);
        }
    }

    public void attack(EntityLivingBase ent) {
        mc.thePlayer.swingItem();

        if (keepSprint.get().booleanValue()) {
            if (mc.thePlayer.onGround) {
                mc.playerController.attackEntity(mc.thePlayer, ent);
            } else {
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
            }
        } else {
            mc.playerController.attackEntity(mc.thePlayer, ent);
        }
    }

    public void performRotations() {
        if (target.getDistanceToEntity(mc.thePlayer) > rotationRange.get().intValue()) return;

        float yaw = fixedRotations.getYaw();
        float pitch = fixedRotations.getPitch();

        if (target != null) {
            float rots[] = RotationsUtil.getRotationsToEntity(target);

            double randomAmount = 3.5;

            yaw = (float) (rots[0] + Math.random() * randomAmount - randomAmount / 2);
            pitch = (float) (rots[1] + Math.random() * randomAmount - randomAmount / 2);

            yaw += (float) (Math.random() - 0.5f);
            pitch += (float) (Math.random() - 0.5f) * 2;
        }

        fixedRotations.updateRotations(yaw, pitch);
    }

    public void performAutoblock() {
        if (target != null) {
            if (target.getDistanceToEntity(mc.thePlayer) > autoBlockRange.get().intValue()) return;

            block = true;
        } else if (block) {
            block = false;
        }
    }
}
