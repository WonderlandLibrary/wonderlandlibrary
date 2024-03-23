package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import dev.zprestige.prestige.client.util.impl.Rotation;
import dev.zprestige.prestige.client.util.impl.RotationUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class AimAssist extends Module {

    public MultipleSetting targets;
    public ModeSetting mode;
    public DragSetting visibleTime;
    public FloatSetting smoothing;
    public FloatSetting fov;
    public FloatSetting range;
    public FloatSetting random;
    public ModeSetting hitbox;
    public BooleanSetting weaponOnly;
    public BooleanSetting oneTarget;
    public TimerUtil randomTimer;
    public TimerUtil visibleTimer;
    public float randomValue;
    public Entity target;

    public AimAssist() {
        super("Aim Assist", Category.Combat, "Aims at targets");
        targets = setting("Targets", new String[]{"Players", "Crystals"}, new Boolean[]{true, true}).description("What entities to aim at");
        mode = setting("Mode", "Both", new String[]{"Both", "Horizontal", "Vertical"}).description("What axis to aim at");
        visibleTime = setting("Visible Time", 100, 150, 0, 500).description("How long the enemy has to be visible in order for aim assist to start");
        smoothing = setting("Smoothing", 0.5f, 0.1f, 1).description("The speed of the aim assist");
        fov = setting("Fov", 50, 0.1f, 180).description("The fov the target has to be in for aim assist to activate");
        range = setting("Range", 5, 0.1f, 10).description("The range the target has to be in for aim assist to activate");
        random = setting("Random", 0.1f, 0, 10).description("Randomness of the mouse movement");
        hitbox = setting("Hitbox", "Eye", new String[]{"Eye", "Center", "Bottom"}).description("Hitbox that will be aimed at");
        weaponOnly = setting("Weapon Only", false).description("Only use Aim Assist when holding weapons");
        oneTarget = setting("Sticky Targeting", true).description("Holds onto 1 target until it is no longer valid");
        randomTimer = new TimerUtil();
        visibleTimer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused()) {
            return;
        }
        if (getMc().crosshairTarget != null) {
            HitResult hitResult = getMc().crosshairTarget;
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                return;
            }
        }
        if (weaponOnly.getObject()) {
            if (!(getMc().player.getMainHandStack().getItem() instanceof SwordItem) && !(getMc().player.getMainHandStack().getItem() instanceof AxeItem)) {
                return;
            }
        }
        Rotation rotation = new Rotation(getMc().player.getYaw(), getMc().player.getPitch());
        if (target != null) {
            Vec3d vec3d = target.getPos();
            ClientPlayerEntity clientPlayerEntity3 = getMc().player;
            if (Math.sqrt(clientPlayerEntity3.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z)) > range.getObject()) {
                target = null;
            }
            if (!RotationUtil.INSTANCE.inRange(rotation, RotationUtil.INSTANCE.getNeededRotations((float) vec3d.x, (float) (vec3d.y - getHeight(target.getHeight())), (float) vec3d.z), fov.getObject())) {
                target = null;
            }
        }
        if (!oneTarget.getObject() || target == null) {
            target = getTarget(rotation);
        }
        if (target == null) {
            visibleTime.setValue();
            visibleTimer.reset();
            return;
        }
        Vec3d vec3d = target.getEyePos();
        double d = vec3d.y - getHeight(target.getHeight());
        BlockHitResult raycast = getMc().world.raycast(new RaycastContext(getMc().player.getCameraPosVec(getMc().getTickDelta()), new Vec3d(vec3d.x, d, vec3d.z), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, getMc().player));
        if (raycast.getType() == HitResult.Type.BLOCK) {
            visibleTime.setValue();
            visibleTimer.reset();
            return;
        }
        if (!randomTimer.delay(visibleTime)) {
            return;
        }
        Rotation rotation3 = RotationUtil.INSTANCE.getNeededRotations((float)vec3d.x, (float)d, (float)vec3d.z);
        if (randomTimer.delay(1000 * smoothing.getObject())) {
            randomValue = random.getObject() > 0 ? -(random.getObject() / 2) + RandomUtil.INSTANCE.getRandom().nextFloat() * random.getObject() : 0;
            randomTimer.reset();
        }
        switch (mode.getObject()) {
            case "Vertical" -> RotationUtil.INSTANCE.setPitch(rotation3, smoothing.getObject(), randomValue, 2);
            case "Horizontal" -> {}
            case "Both" -> RotationUtil.INSTANCE.setRotation(rotation3, smoothing.getObject(), randomValue, 2);
        }
    }

    private Entity getTarget(Rotation rotation) {
        Entity entity = null;
        double d = Double.MAX_VALUE;
        for (Entity entity2 : getMc().world.getEntities()) {
            if (isEntityValid(entity2) && Prestige.Companion.getAntiBotManager().isNotBot(entity2) && (entity2 instanceof PlayerEntity player && !Prestige.Companion.getSocialsManager().isFriend(player.getEntityName()))) {
                Vec3d vec3d = entity2.getEyePos();
                double d2 = vec3d.y - getHeight(entity2.getHeight());
                double d3 = getMc().player.squaredDistanceTo(vec3d.x, d2, vec3d.z);
                if (d3 < d && Math.sqrt(getMc().player.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z)) <= range.getObject() && RotationUtil.INSTANCE.inRange(rotation, RotationUtil.INSTANCE.getNeededRotations((float) vec3d.x, (float) d2, (float) vec3d.z), fov.getObject())) {
                    entity = entity2;
                    d = d3;
                }
            }
        }
        return entity;
    }

    boolean isEntityValid(Entity entity) {
        if (!(entity instanceof EndCrystalEntity) && !(entity instanceof PlayerEntity)) {
            return false;
        }
        if (entity == getMc().player || entity instanceof PlayerEntity && (!targets.getValue("Players") || !Prestige.Companion.getAntiBotManager().isNotBot(entity))) {
            return false;
        }
        return !(entity instanceof EndCrystalEntity) || targets.getValue("Crystals");
    }

    private float getHeight(float f) {
        return hitbox.getObject().equals("Eye") ? 0 : (hitbox.getObject().equals("Center") ? f / 2 : f);
    }

    @Override
    public String method224() {
        String string;
        if (target == null) {
            string = "";
        } else if (target instanceof PlayerEntity) {
            string = target.getEntityName();
        } else {
            string = "Crystal";
        }
        return string;
    }
}
