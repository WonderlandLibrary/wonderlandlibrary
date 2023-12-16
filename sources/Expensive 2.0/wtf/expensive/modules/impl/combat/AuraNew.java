package wtf.expensive.modules.impl.combat;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.*;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.math.AuraUtil;
import wtf.expensive.util.math.GCDUtil;
import wtf.expensive.util.math.RayTraceUtil;
import wtf.expensive.util.movement.MoveUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.world.InventoryUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.*;
import static net.minecraft.util.math.MathHelper.clamp;
import static net.minecraft.util.math.MathHelper.wrapDegrees;
import static wtf.expensive.util.math.GCDUtil.getSensitivity;
import static wtf.expensive.util.math.MathUtil.calculateDelta;

@FunctionAnnotation(name = "Aura", type = Type.Combat)
public class AuraNew extends Function {
    private final ModeSetting mode = new ModeSetting("Режим ротации",
            "Снапы",
            "Обычная", "Снапы"
    );
    private final SliderSetting range = new SliderSetting("Дистанция", 3, 3, 6, 0.05f);

    private final SliderSetting preRange = new SliderSetting("Дистанция ротации", 0.5f, 0.0f, 3.0f, 0.05f)
            .setVisible(() -> mode.is("Обычная"));


    private final MultiBoxSetting targets = new MultiBoxSetting("Цели",
            new BooleanOption("Игроки", true),
            new BooleanOption("Животные", false),
            new BooleanOption("Монстры", false)
    );

    private final MultiBoxSetting ignore = new MultiBoxSetting("Игнорировать",
            new BooleanOption("Друзей", true),
            new BooleanOption("Невидимых", false),
            new BooleanOption("Голых", false),
            new BooleanOption("Ботов", true)
    );

    private final ModeSetting sort = new ModeSetting("Сортировать",
            "По дистанции",
            "По дистанции", "По здоровью", "По полю зрения"
    );

    public final MultiBoxSetting settings = new MultiBoxSetting("Настройки",
            new BooleanOption("Только критами", true),
            new BooleanOption("Коррекция движения", true),
            new BooleanOption("Не бить при использовании", false),
            new BooleanOption("Отжимать щит", true),
            new BooleanOption("Ломать щит", true)
    );

    private final BooleanOption space = new BooleanOption("Криты только с пробелом", false).setVisible(() -> settings.get(0));
    private final BooleanOption silent = new BooleanOption("Сайлент коррекция", true).setVisible(() -> settings.get(1));

    public AuraNew() {
        addSettings(range, preRange, mode, targets, ignore, sort, settings, space, silent);
    }

    private long cpsLimit = 0;

    @Getter
    LivingEntity target;
    @Getter
    float rotYaw, rotPitch;

    int ticks;

    @Override
    public void onEvent(Event event) {
        if (mc.player == null || mc.world == null)
            return;

        if (event instanceof EventInteractEntity entity) {
            if (target != null) entity.setCancel(true);
        }

        if (event instanceof EventInput e) {
            if (settings.get(1) && silent.get()) {
                MoveUtil.fixMovement(e, rotYaw);
            }
        }

        if (event instanceof EventUpdate) {
            target = findTarget();

            if (target == null) {
                reset();
                return;
            }

            switch (mode.getIndex()) {
                case 0 -> {
                    updateRotation(0.5f);

                    if (canAttack() && RayTraceUtil.getMouseOver(target, rotYaw, rotPitch, range.getValue().floatValue()) == target)
                        updateAttack(target);
                }
                case 1 -> {
                    if (canAttack()) {
                        updateAttack(target);
                        ticks = 3;
                    }

                    if (ticks > 0) {
                        updateRotation(1);
                        ticks--;
                    } else {
                        rotYaw = mc.player.rotationYaw;
                        rotPitch = mc.player.rotationPitch;
                    }
                }
            }
        }

        if (event instanceof EventMotion e) {
            if (target == null) return;

            e.setYaw(rotYaw);
            e.setPitch(rotPitch);

            updateClientRotation(rotYaw, rotPitch);
        }

        if (event instanceof EventWorldChange) {
            reset();
        }

        if (event instanceof EventRender e) {
            if (e.isRender3D() && target != null) {
                //drawCircle(target, e);
            }
        }
    }

    private void updateRotation(float speed) {
        if (isInHitBox(mc.player, target)) return;

        Vector3d vec3d = getVector3d(mc.player, target);

        float rawYaw = (float) wrapDegrees(toDegrees(atan2(vec3d.z, vec3d.x)) - 90);
        float rawPitch = (float) wrapDegrees(toDegrees(-atan2(vec3d.y, hypot(vec3d.x, vec3d.z))));

        float yawDelta = wrapDegrees(rawYaw - rotYaw);
        float pitchDelta = wrapDegrees(rawPitch - rotPitch);

        if (abs(yawDelta) > 180) yawDelta -= signum(yawDelta) * 360;

        float additionYaw = clamp(yawDelta, -180 * speed, 180 * speed);
        float additionPitch = clamp(pitchDelta, -90 * speed, 90 * speed);

        float yaw = rotYaw + additionYaw + ThreadLocalRandom.current().nextFloat(-1f, 1);
        float pitch = rotPitch + additionPitch + ThreadLocalRandom.current().nextFloat(-1, 1);

        rotYaw = getSensitivity(yaw);
        rotPitch = getSensitivity(clamp(pitch, -89.0F, 89.0F));
    }

    private void updateAttack(LivingEntity target) {
        if (mc.player.isHandActive()) {
            if (settings.get(3) && mc.player.isActiveItemStackBlocking()) {
                mc.playerController.onStoppedUsingItem(mc.player);
            } else if (settings.get(2)) return;
        }

        boolean sprint = false;

        if (settings.get(0) && CEntityActionPacket.lastUpdatedSprint) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
            if (!mc.player.isInWater())
                mc.player.setSprinting(false);
            sprint = true;
        }

        cpsLimit = System.currentTimeMillis() + 550;

        attackEntity(target);

        if (settings.get(4) && target.isBlocking()) {
            int slot = InventoryUtil.getAxe(true);
            if (slot == -1) return;

            mc.player.connection.sendPacket(new CHeldItemChangePacket(slot));
            attackEntity(target);
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));

            ClientUtil.sendMesage("БРЯК: " + mc.player.ticksExisted);
        }

        if (sprint) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
        }
    }

    private boolean canAttack() {
        if (cpsLimit >= System.currentTimeMillis() ||  Managment.FUNCTION_MANAGER.autoPotionFunction.isActivePotion
                || getDistance(target) >= range.getValue().floatValue()
                || mc.player.getCooledAttackStrength(1) <= 0.93F
        ) return false;

        if (mc.player.isInWater() && mc.player.areEyesInFluid(FluidTags.WATER) || mc.player.isInLava()
                || mc.player.isOnLadder() || mc.player.isRidingHorse()
                || mc.player.abilities.isFlying || mc.player.isElytraFlying() || mc.player.isPotionActive(Effects.LEVITATION) || mc.player.isPotionActive(Effects.SLOW_FALLING) || !settings.get(0)
        ) return true;

        return (space.get() && !mc.player.movementInput.jump) || mc.player.fallDistance > 0 && !mc.player.isOnGround();
    }

    private LivingEntity findTarget() {
        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity living && isValid(living) && getDistance(living) <= range.getValue().floatValue()
                    + (mode.is("Обычная") ? preRange.getValue().floatValue() : 0.0f)) {
                if (ignore.get(0) && Managment.FRIEND_MANAGER.isFriend(living.getName().getString())) continue;
                if (ignore.get(1) && living.isInvisible()) continue;
                if (living.getHealth() < 0.01) continue;
                if (ignore.get(2) && living instanceof PlayerEntity && living.getTotalArmorValue() == 0) continue;
                if (ignore.get(3) && living instanceof PlayerEntity && !living.getUniqueID().equals(PlayerEntity.getOfflineUUID(living.getName().getString())))
                    continue;


                targets.add(living);
            }
        }

        if (targets.isEmpty()) {
            return null;
        }

        if (targets.size() == 1) {
            return targets.get(0);
        }
        return Collections.min(targets, switch (sort.getIndex()) {
            case 0 -> Comparator.comparingDouble(this::getDistance);
            case 1 -> Comparator.comparingDouble(LivingEntity::getHealth);
            case 2 -> Comparator.comparingDouble(this::getDegree);
            default -> null;
        });
    }

    private boolean isValid(LivingEntity entity) {
        if (entity instanceof ArmorStandEntity || entity == mc.player) return false;

        if (entity instanceof PlayerEntity) return targets.get(0);
        if (entity instanceof AnimalEntity || entity instanceof VillagerEntity || entity instanceof WaterMobEntity)
            return targets.get(1);
        if (entity instanceof MonsterEntity || entity instanceof SlimeEntity) return targets.get(2);

        return true;
    }

    private void updateClientRotation(float yaw, float pitch) {
        mc.player.rotationYawHead = yaw;
        mc.player.renderYawOffset = yaw;
        mc.player.rotationPitchHead = pitch;
    }

    private void reset() {
        rotYaw = mc.player.rotationYaw;
        rotPitch = mc.player.rotationPitch;

        target = null;
    }

    private void attackEntity(LivingEntity entity) {
        mc.playerController.attackEntity(mc.player, entity);
        mc.player.swingArm(Hand.MAIN_HAND);
    }

    private boolean isInHitBox(LivingEntity me, LivingEntity to) {
        double wHalf = to.getWidth() / 2;

        double yExpand = clamp(me.getPosYEye() - to.getPosY(), 0, to.getHeight());

        double xExpand = clamp(mc.player.getPosX() - to.getPosX(), -wHalf, wHalf);
        double zExpand = clamp(mc.player.getPosZ() - to.getPosZ(), -wHalf, wHalf);

        return new Vector3d(
                to.getPosX() - me.getPosX() + xExpand,
                to.getPosY() - me.getPosYEye() + yExpand,
                to.getPosZ() - me.getPosZ() + zExpand
        ).length() == 0;
    }

    private Vector3d getVector3d(LivingEntity me, LivingEntity to) {

        // САМЫЙ ПРОСТОЙ И ПРАВИЛЬНЫЙ РАСЧЕТ ОТ ЛИПЫ //

        double wHalf = to.getWidth() / 2;

        double yExpand = clamp(me.getPosYEye() - to.getPosY(), 0, to.getHeight() * (mc.player.getDistance(to) / (range.getValue().floatValue())));

        double xExpand = clamp(mc.player.getPosX() - to.getPosX(), -wHalf, wHalf);
        double zExpand = clamp(mc.player.getPosZ() - to.getPosZ(), -wHalf, wHalf);

        return new Vector3d(
                to.getPosX() - me.getPosX() + xExpand,
                to.getPosY() - me.getPosYEye() + yExpand,
                to.getPosZ() - me.getPosZ() + zExpand
        );

        // КАЛЬКУЛЯТОР ОТ САЛАТИКА //

//        double yOffset = MathHelper.clamp(
//                to.getEyeHeight() * (mc.player.getDistance(to) / (range.getValue().floatValue() + to.getWidth())),
//                mc.player.getEyeHeight() / 2,
//                mc.player.getEyeHeight()
//        );
//
//        double wHalf = to.getWidth() / 2;
//        Vector3d original = to.getPositionVec().add(new Vector3d(0, yOffset, 0));
//        boolean ignore = !mc.player.canEntityBeSeen(to);
//        if (!isHitBoxVisible(original)) {
//            original = AuraUtil.getBestVec3d(mc.player.getEyePosition(1), to.getBoundingBox());
//        }
//        boolean fix = false;
//        if (!ignore || mc.player.canEntityBeSeen(to) || original == null) {
//            original = to.getPositionVec();
//            fix = true;
//        }
//
//        double yExpand = clamp(me.getPosYEye() - (original.y + ((ignore && !fix) ? mc.player.getEyeHeight() : 0)), 0, to.getHeight());
//        double xExpand = clamp(mc.player.getPosX() - original.x, -wHalf, wHalf);
//        double zExpand = clamp(mc.player.getPosZ() - original.z, -wHalf, wHalf);
//
//        return new Vector3d(
//                original.x - me.getPosX() + xExpand,
//                original.y - me.getPosYEye() + yExpand,
//                original.z - me.getPosZ() + zExpand
//        );
    }

    private double getDistance(LivingEntity entity) {
        return getVector3d(mc.player, entity).length();
    }

    private double getDegree(LivingEntity entity) {
        Vector3d vec = getVector3d(mc.player, entity);

        double yaw = wrapDegrees(toDegrees(atan2(vec.z, vec.x)) - 90);
        double delta = wrapDegrees(yaw - mc.player.rotationYaw);

        if (abs(delta) > 180) delta -= signum(delta) * 360;

        return abs(delta);
    }

    private boolean isLookingAtMe(LivingEntity target) {
        double x = target.getPosX() - mc.player.getPosX();
        double z = target.getPosZ() - mc.player.getPosZ();

        float entityYaw = wrapDegrees(target.rotationYaw);
        double yaw = toDegrees(atan2(z, x)) + 90.0;

        return abs(wrapDegrees(yaw - entityYaw)) <= 90;
    }

    private void drawCircle(LivingEntity target, EventRender e) {
        EntityRendererManager rm = mc.getRenderManager();

        double x = target.lastTickPosX + (target.getPosX() - target.lastTickPosX) * e.partialTicks - rm.info.getProjectedView().getX();
        double y = target.lastTickPosY + (target.getPosY() - target.lastTickPosY) * e.partialTicks - rm.info.getProjectedView().getY();
        double z = target.lastTickPosZ + (target.getPosZ() - target.lastTickPosZ) * e.partialTicks - rm.info.getProjectedView().getZ();

        float height = target.getHeight();

        double duration = 2000;
        double elapsed = (System.currentTimeMillis() % duration);

        boolean side = elapsed > (duration / 2);
        double progress = elapsed / (duration / 2);

        if (side) progress -= 1;
        else progress = 1 - progress;

        progress = (progress < 0.5) ? 2 * progress * progress : 1 - pow((-2 * progress + 2), 2) / 2;

        double eased = (height / 2) * ((progress > 0.5) ? 1 - progress : progress) * ((side) ? -1 : 1);

        RenderSystem.pushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        RenderSystem.disableCull();

        RenderSystem.lineWidth(1.5f);
        RenderSystem.color4f(-1f, -1f, -1f, -1f);

        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);

        float[] colors = null;

        for (int i = 0; i <= 360; i++) {
            colors = RenderUtil.IntColor.rgb(Managment.STYLE_MANAGER.getCurrentStyle().getColor(i));

            buffer.pos(x + cos(toRadians(i)) * target.getWidth() * 0.8, y + (height * progress), z + sin(toRadians(i)) * target.getWidth() * 0.8)
                    .color(colors[0], colors[1], colors[2], 0.5F).endVertex();
            buffer.pos(x + cos(toRadians(i)) * target.getWidth() * 0.8, y + (height * progress) + eased, z + sin(toRadians(i)) * target.getWidth() * 0.8)
                    .color(colors[0], colors[1], colors[2], 0F).endVertex();
        }

        buffer.finishDrawing();
        WorldVertexBufferUploader.draw(buffer);
        RenderSystem.color4f(-1f, -1f, -1f, -1f);

        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i <= 360; i++) {
            buffer.pos(x + cos(toRadians(i)) * target.getWidth() * 0.8, y + (height * progress), z + sin(toRadians(i)) * target.getWidth() * 0.8)
                    .color(colors[0], colors[1], colors[2], 0.5F).endVertex();
        }

        buffer.finishDrawing();
        WorldVertexBufferUploader.draw(buffer);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enableAlphaTest();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.popMatrix();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        reset();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        reset();
    }
}
