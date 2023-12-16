package wtf.expensive.modules.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventObsidianPlace;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.movement.MoveUtil;

import java.util.List;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "AutoExplosion", type = Type.Combat)
public class AutoExplosionFunction extends Function {
    private BlockPos position = null;
    private Entity crystalEntity = null;
    private int oldSlot = -1;
    private final TimerUtil timerUtil = new TimerUtil();

    private final BooleanOption saveSelf = new BooleanOption("Не взрывать себя", true);
    public final BooleanOption correction = new BooleanOption("Коррекция движения", true);

    public AutoExplosionFunction() {
        addSettings(saveSelf,correction);
    }

    public Vector2f server;

    public boolean check() {
        return server != null && correction.get() && crystalEntity != null && position != null && state;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventInput e) {
            if (check()) {
                MoveUtil.fixMovement(e, server.x);
            }
        }
        if (event instanceof EventObsidianPlace obsidianPlace) {
            handleObsidianPlace(obsidianPlace.getPos());
        } else if (event instanceof EventUpdate updateEvent) {
            handleUpdateEvent(updateEvent);
        } else if (event instanceof EventMotion motionEvent) {
            handleMotionEvent(motionEvent);
        }
    }

    /**
     * Обработчик события EventUpdate.
     * Проверяет наличие кристалла в указанной позиции и атакует его.
     */
    private void handleUpdateEvent(EventUpdate updateEvent) {
        if (position != null) {
            List<Entity> crystals = mc.world.getEntitiesWithinAABBExcludingEntity(null,
                            new AxisAlignedBB(position.getX(),
                                    position.getY(),
                                    position.getZ(),
                                    position.getX() + 1.0,
                                    position.getY() + 2.0,
                                    position.getZ() + 1.0))
                    .stream()
                    .filter(entity -> entity instanceof EnderCrystalEntity)
                    .toList();

            crystals.forEach(this::attackEntity);
        }

        if (crystalEntity != null && !crystalEntity.isAlive()) {
            crystalEntity = null;
            position = null;
            server = null;
        }
    }


    /**
     * Обработчик события EventObsidianPlace.
     * Размещает кристалл в указанной позиции (если обсидиан поставил mc.player).
     */
    private void handleObsidianPlace(BlockPos position) {
        final int crystalSlot = getSlotWithCrystal();

        this.oldSlot = mc.player.inventory.currentItem;


        if (crystalSlot == -1 || position == null) {
            return;
        }

        mc.player.inventory.currentItem = crystalSlot;

        BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(
                new Vector3d(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f),
                Direction.UP,
                position,
                false
        );

        if (mc.playerController.processRightClickBlock(mc.player, mc.world, Hand.MAIN_HAND, rayTraceResult)
                == ActionResultType.SUCCESS) {
            mc.player.swingArm(Hand.MAIN_HAND);
        }
        if (oldSlot != -1)
            mc.player.inventory.currentItem = this.oldSlot;
        this.oldSlot = -1;
        this.position = position;
    }

    /**
     * Обработчик события EventMotion.
     * Устанавливает повороты игрока в направлении кристалла.
     */
    private void handleMotionEvent(EventMotion motionEvent) {
        if (!isValid(crystalEntity)) {
            return;
        }

        server = MathUtil.rotationToEntity(crystalEntity);

        motionEvent.setYaw(server.x);
        motionEvent.setPitch(server.y);
        mc.player.rotationYawHead = server.x;
        mc.player.renderYawOffset = server.x;
        mc.player.rotationPitchHead = server.y;
    }

    /**
     * Аттакует сущность с задержкой 300 мс.
     *
     * @param base Эндер кристал
     */
    private void attackEntity(Entity base) {
        if (!isValid(base)
                || mc.player.getCooledAttackStrength(1) < 1) {
            return;
        }
        if (timerUtil.hasTimeElapsed(400)) {

            mc.playerController.attackEntity(mc.player, base);
            mc.player.swingArm(Hand.MAIN_HAND);
            timerUtil.reset();
        }
        crystalEntity = base;
    }

    /**
     * Получает слот с кристаллом в инвентаре игрока.
     *
     * @return Слот с кристаллом, или -1 если кристалл не найден.
     */
    private int getSlotWithCrystal() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Проверяет, находится ли игрок на корректном расстоянии от позиции кристалла.
     *
     * @return true, если игрок находится на корректном расстоянии, иначе false.
     */
    private boolean isCorrectDistance() {
        if (position == null) {
            return false;
        }
        return mc.player.getPositionVec().distanceTo(
                new Vector3d(position.getX(),
                        position.getY(),
                        position.getZ())) <= mc.playerController.getBlockReachDistance();
    }

    /**
     * Проверяет, валидна ли сущность
     *
     * @return true, если сущность валидна, иначе false.
     */
    private boolean isValid(Entity base) {
        if (base == null) {
            return false;
        }
        if (position == null) {
            return false;
        }
        if (saveSelf.get() && !(base.getPosition().getY() > mc.player.getPosition().getY())) {
            return false;
        }

        return isCorrectDistance();
    }

    @Override
    protected void onDisable() {
        server = null;
        position = null;
        crystalEntity = null;
        oldSlot = -1;
        super.onDisable();
    }
}