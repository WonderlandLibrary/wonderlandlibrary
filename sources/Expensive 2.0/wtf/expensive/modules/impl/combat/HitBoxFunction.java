package wtf.expensive.modules.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.SliderSetting;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "HitBox", type = Type.Combat)
public class HitBoxFunction extends Function {

    public final SliderSetting size = new SliderSetting("Размер", 0.2f, 0.f, 3.f, 0.05f);
    public final BooleanOption invisible = new BooleanOption("Невидимые", false);

    public HitBoxFunction() {
        addSettings(size, invisible);
    }

    @Override
    public void onEvent(final Event event) {
        handleEvent(event);
    }

    /**
     * Обрабатываем событие.
     */
    private void handleEvent(Event event) {
        // Проверка, является ли событие типом EventRender и включено ли 3D-отображение
        if (!(event instanceof EventRender && ((EventRender) event).isRender3D()))
            return;

        // Проверка, включен ли режим невидимости
        if (invisible.get())
            return;

        // Выполнение корректировки границ хитбокса для игроков
        adjustBoundingBoxesForPlayers();
    }

    /**
     * Настраиваем хитбокс игрока под кастомный размер.
     */
    private void adjustBoundingBoxesForPlayers() {
        // Перебор всех игроков в мире
        for (PlayerEntity player : mc.world.getPlayers()) {
            // Проверка, нужно ли пропустить данного игрока при корректировке хитбокса
            if (shouldSkipPlayer(player))
                continue;

            // Вычисление множителя размера и установка нового хитбокса для игрока
            float sizeMultiplier = this.size.getValue().floatValue() * 2.5F;
            setBoundingBox(player, sizeMultiplier);
        }
    }

    /**
     * Проверка на валидного игрока
     */
    private boolean shouldSkipPlayer(PlayerEntity player) {
        // Проверка, нужно ли пропустить данного игрока при корректировке хитбокса
        // Игрок пропускается, если это текущий игрок (mc.player) или если игрок мертв
        return player == mc.player || !player.isAlive();
    }

    /**
     * Устанавливаем новый размер для хитбокса
     */
    private void setBoundingBox(Entity entity, float size) {
        // Вычисление нового хитбокса для сущности и установка ее
        AxisAlignedBB newBoundingBox = calculateBoundingBox(entity, size);
        entity.setBoundingBox(newBoundingBox);
    }

    /**
     * Вычисление координат минимальной и максимальной точек хитбокса для сущности и создание
     * и возвращение нового хитбокса сущности
     */
    private AxisAlignedBB calculateBoundingBox(Entity entity, float size) {
        // Вычисление координат минимальной и максимальной точек хитбокса для сущности
        double minX = entity.getPosX() - size;
        double minY = entity.getBoundingBox().minY;
        double minZ = entity.getPosZ() - size;
        double maxX = entity.getPosX() + size;
        double maxY = entity.getBoundingBox().maxY;
        double maxZ = entity.getPosZ() + size;

        // Создание и возвращение нового хитбокса сущности
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}

