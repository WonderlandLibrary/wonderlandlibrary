package wtf.expensive.modules.impl.combat;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventInteractEntity;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.player.AutoPotionFunction;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.math.AuraUtil;
import wtf.expensive.util.math.GCDUtil;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.math.RayTraceUtil;
import wtf.expensive.util.movement.MoveUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.world.InventoryUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.*;
import static wtf.expensive.util.math.MathUtil.calculateDelta;

/**
 * @author dedinside
 * @since 07.06.2023
 */

@SuppressWarnings("all")
@FunctionAnnotation(name = "Aura", type = Type.Combat)
public class AuraFunction extends Function {
    @Getter
    public LivingEntity target = null;

    public Vector2f rotate = new Vector2f(0, 0);

    private final ModeSetting rotationMode = new ModeSetting("Мод ротации", "Обычная", "Обычная", "Снапы");

    private final ModeSetting sortMode = new ModeSetting("Сортировать",
            "По всему",
            "По всему", "По здоровью", "По дистанции"
    );

    private final MultiBoxSetting targets = new MultiBoxSetting("Цели",
            new BooleanOption("Игроки", true),
            new BooleanOption("Друзья", false),
            new BooleanOption("Голые", true),
            new BooleanOption("Мобы", false)
    );

    private final SliderSetting distance = new SliderSetting("Дистанция аттаки", 3.0f, 2.0f, 5.0f, 0.05f);
    private final SliderSetting rotateDistance = new SliderSetting("Дистанция ротации", 1.5f, 0.0f, 3.0f, 0.05f).setVisible(() -> rotationMode.is("Обычная"));

    public final MultiBoxSetting settings = new MultiBoxSetting("Настройки",
            new BooleanOption("Только критами", true),
            new BooleanOption("Коррекция движения", true),
            new BooleanOption("Отжимать щит", true),
            new BooleanOption("Ломать щит", true),
            new BooleanOption("Таргет ЕСП", true)

    );
    private final BooleanOption onlySpaceCritical = new BooleanOption("Только с пробелом", false)
            .setVisible(() -> settings.get(0));
    private final BooleanOption silent = new BooleanOption("Сайлент коррекция", true).setVisible(() -> settings.get(1));

    int ticksUntilNextAttack;
    private boolean hasRotated;
    private long cpsLimit = 0;

    public AuraFunction() {
        this.addSettings(rotationMode,
                targets,
                sortMode,
                distance,
                rotateDistance,
                settings,
                onlySpaceCritical,
                silent);
    }

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventInteractEntity entity) {
            if (target != null)
                entity.setCancel(true);
        }
        if (event instanceof EventInput eventInput) {
            if (settings.get(1) && silent.get()) {
                MoveUtil.fixMovement(eventInput, Managment.FUNCTION_MANAGER.autoPotionFunction.isActivePotion ? Minecraft.getInstance().player.rotationYaw : rotate.x);
            }
        }
        if (event instanceof EventUpdate updateEvent) {
            if (!(target != null && isValidTarget(target))) {
                target = findTarget();
            }
            if (target == null) {
                cpsLimit = System.currentTimeMillis();
                rotate = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
                return;
            }

            attackAndRotateOnEntity(target);
        }
        if (event instanceof EventMotion motionEvent) {
            handleMotionEvent(motionEvent);
        }
        if (event instanceof EventRender e) {
            if (e.isRender3D() && target != null) {
                if (settings.get(4)) {
                    drawCircle(target, e);
                }
            }
        }
    }

    private double prevCircleStep, circleStep;

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


    public Vector2f clientRot = null;

    private void handleMotionEvent(EventMotion motionEvent) {
        if (target == null || Managment.FUNCTION_MANAGER.autoPotionFunction.isActivePotion)
            return;

        motionEvent.setYaw(rotate.x);
        motionEvent.setPitch(rotate.y);
        mc.player.rotationYawHead = rotate.x;
        mc.player.renderYawOffset = rotate.x;
        mc.player.rotationPitchHead = rotate.y;
    }


    private void attackAndRotateOnEntity(LivingEntity target) {
        hasRotated = false;
        switch (rotationMode.getIndex()) {
            case 0 -> {
                hasRotated = false;
                if (shouldAttack(target) && RayTraceUtil.getMouseOver(target, rotate.x, rotate.y, distance.getValue().floatValue()) == target
                        && !Managment.FUNCTION_MANAGER.autoPotionFunction.isActivePotion) {
                    attackTarget(target);
                }
                if (!hasRotated)
                    setRotation(target, false);
            }
            case 1 -> {
                if (shouldAttack(target) && !Managment.FUNCTION_MANAGER.autoPotionFunction.isActivePotion) {
                    attackTarget(target);
                    ticksUntilNextAttack = 2;
                }
                if (ticksUntilNextAttack > 0) {
                    setRotation(target, false);
                    ticksUntilNextAttack--;
                } else {
                    rotate.x = mc.player.rotationYaw;
                    rotate.y = mc.player.rotationPitch;
                }
            }
        }
    }

    private void attackTarget(final LivingEntity targetEntity) {
        if (settings.get(2) && mc.player.isBlocking()) {
            mc.playerController.onStoppedUsingItem(mc.player);
        }

        boolean sprint = false;
        if (CEntityActionPacket.lastUpdatedSprint && !mc.player.isInWater()) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
            sprint = true;
        }
        cpsLimit = System.currentTimeMillis() + 550;

        mc.playerController.attackEntity(mc.player, targetEntity);
        mc.player.swingArm(Hand.MAIN_HAND);


        if (settings.get(3)) {
            breakShieldAndSwapSlot();
        }

        if (sprint) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
        }
    }

    private void breakShieldAndSwapSlot() {
        LivingEntity targetEntity = target;
        if (targetEntity instanceof PlayerEntity player) {
            if (target.isActiveItemStackBlocking(2)
                    && !player.isSpectator()
                    && !player.isCreative()
                    && (target.getHeldItemOffhand().getItem() == Items.SHIELD
                    || target.getHeldItemMainhand().getItem() == Items.SHIELD)) {
                int slot = breakShield(player);
                if (slot > 8) {
                    mc.playerController.pickItem(slot);
                }
            }
        }
    }


    public int breakShield(LivingEntity target) {
        int hotBarSlot = InventoryUtil.getAxe(true);
        if (hotBarSlot != -1) {
            mc.player.connection.sendPacket(new CHeldItemChangePacket(hotBarSlot));
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
            return hotBarSlot;
        }
        int inventorySLot = InventoryUtil.getAxe(false);
        if (inventorySLot != -1) {
            mc.playerController.pickItem(inventorySLot);
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            return inventorySLot;
        }
        return -1;
    }

    private boolean shouldAttack(LivingEntity targetEntity) {
        return canAttack() && targetEntity != null && (cpsLimit <= System.currentTimeMillis());
    }

    private void setRotation(final LivingEntity base, final boolean attack) {
        this.hasRotated = true;

        Vector3d vec3d = AuraUtil.getVector(base);

        final double diffX = vec3d.x;
        final double diffY = vec3d.y;
        final double diffZ = vec3d.z;

        float[] rotations = new float[]{
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F,
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.hypot(diffX, diffZ))))
        };

        float deltaYaw = MathHelper.wrapDegrees(calculateDelta(rotations[0], this.rotate.x));
        float deltaPitch = calculateDelta(rotations[1], this.rotate.y);

        float limitedYaw = min(max(abs(deltaYaw), 1.0F), 180.0F);
        float limitedPitch = (float) min(max(abs(deltaPitch), 1.0F), 15.0F);

        float finalYaw = this.rotate.x + (deltaYaw > 0.0f ? limitedYaw : -limitedYaw) + ThreadLocalRandom.current().nextFloat(-1, 1);
        float finalPitch = MathHelper.clamp(this.rotate.y + (deltaPitch > 0.0f ? limitedPitch : -limitedPitch) + ThreadLocalRandom.current().nextFloat(-1, 1), -89.0f, 89.0f);

        float gcd = GCDUtil.getGCDValue();
        finalYaw = (float) ((double) finalYaw - (double) (finalYaw - this.rotate.x) % gcd);
        finalPitch = (float) ((double) finalPitch - (double) (finalPitch - rotate.y) % gcd);

        this.rotate.x = finalYaw;
        this.rotate.y = finalPitch;
    }

    public boolean canAttack() {
        final boolean onSpace = onlySpaceCritical.get()
                && mc.player.isOnGround()
                && !mc.gameSettings.keyBindJump.isKeyDown();

        final boolean reasonForAttack = mc.player.isPotionActive(Effects.BLINDNESS)
                || mc.player.isOnLadder()
                || mc.player.isInWater() && mc.player.areEyesInFluid(FluidTags.WATER)
                || mc.player.isRidingHorse()
                || mc.player.abilities.isFlying || mc.player.isElytraFlying();

        if (getDistance(target) >= distance.getValue().floatValue()
                || mc.player.getCooledAttackStrength(1.5F) < 0.92F) {
            return false;
        }
        if (Managment.FUNCTION_MANAGER.freeCam.player != null) return true;

        if (!reasonForAttack && settings.get(0)) {
            return onSpace || !mc.player.isOnGround() && mc.player.fallDistance > 0.0F;
        }
        return true;
    }

    private LivingEntity findTarget() {
        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity && isValidTarget((LivingEntity) entity)) {
                targets.add((LivingEntity) entity);
            }
        }

        if (targets.isEmpty()) {
            return null;
        }

        if (targets.size() > 1) {
            switch (sortMode.get()) {
                case "По всему" -> {
                    targets.sort(Comparator.comparingDouble(target -> {
                        if (target instanceof PlayerEntity player) {
                            return -this.getEntityArmor(player);
                        }
                        if (target instanceof LivingEntity livingEntity) {
                            return -livingEntity.getTotalArmorValue();
                        }
                        return 0.0;
                    }).thenComparing((o, o1) -> {
                        double health = getEntityHealth((LivingEntity) o);
                        double health1 = getEntityHealth((LivingEntity) o1);
                        return Double.compare(health, health1);
                    }).thenComparing((object, object2) -> {
                        double d2 = getDistance((LivingEntity) object);
                        double d3 = getDistance((LivingEntity) object2);
                        return Double.compare(d2, d3);
                    }));
                }
                case "По дистанции" -> {
                    targets.sort(Comparator.comparingDouble(Managment.FUNCTION_MANAGER.auraFunction::getDistance).thenComparingDouble(this::getEntityHealth));
                }
                case "По здоровью" -> {
                    targets.sort(Comparator.comparingDouble(this::getEntityHealth).thenComparingDouble(mc.player::getDistance));
                }
            }
        } else {
            cpsLimit = System.currentTimeMillis();
        }
        return targets.get(0);
    }

    private boolean isValidTarget(final LivingEntity base) {
        if (base.getShouldBeDead() || !base.isAlive() || base == mc.player) return false;

        if (base instanceof PlayerEntity) {
            String playerName = base.getName().getString();
            if (Managment.FRIEND_MANAGER.isFriend(playerName) && !targets.get(1)
                    || Managment.FUNCTION_MANAGER.freeCam.player != null && playerName.equals(Managment.FUNCTION_MANAGER
                    .freeCam.player.getName().getString())
                    || base.getTotalArmorValue() == 0 && (!targets.get(0) || !targets.get(2)))
                return false;
        }

        if ((base instanceof MobEntity || base instanceof AnimalEntity) && !targets.get(3)) return false;

        if (base instanceof ArmorStandEntity || base instanceof PlayerEntity && ((PlayerEntity) base).isBot)
            return false;

        return getDistance(base) <= distance.getValue().floatValue()
                + (rotationMode.is("Обычная") ? rotateDistance.getValue().floatValue() : 0.0f);
    }

    private double getDistance(LivingEntity entity) {
        return AuraUtil.getVector(entity).length();
    }

    public double getEntityArmor(PlayerEntity target) {
        double totalArmor = 0.0;

        for (ItemStack armorStack : target.inventory.armorInventory) {
            if (armorStack != null && armorStack.getItem() instanceof ArmorItem) {
                totalArmor += getProtectionLvl(armorStack);
            }
        }

        return totalArmor;
    }

    public double getEntityHealth(Entity ent) {
        if (ent instanceof PlayerEntity player) {
            double armorValue = getEntityArmor(player) / 20.0;
            return (player.getHealth() + player.getAbsorptionAmount()) * armorValue;
        } else if (ent instanceof LivingEntity livingEntity) {
            return livingEntity.getHealth() + livingEntity.getAbsorptionAmount();
        }
        return 0.0;
    }


    private double getProtectionLvl(ItemStack stack) {
        ArmorItem armor = (ArmorItem) stack.getItem();
        double damageReduce = armor.getDamageReduceAmount();
        if (stack.isEnchanted()) {
            damageReduce += (double) EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack) * 0.25;
        }
        return damageReduce;
    }

    @Override
    public void onDisable() {
        this.rotate = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
        target = null;
        cpsLimit = System.currentTimeMillis();
        super.onDisable();
    }
}