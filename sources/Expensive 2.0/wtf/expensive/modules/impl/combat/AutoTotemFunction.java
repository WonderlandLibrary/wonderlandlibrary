package wtf.expensive.modules.impl.combat;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.world.InventoryUtil;
import wtf.expensive.util.world.WorldUtil;

/**
 * @author dedinside
 * @since 05.06.2023
 */
@FunctionAnnotation(name = "AutoTotem", type = Type.Combat)
public class AutoTotemFunction extends Function {

    private final ModeSetting autototemMode = new ModeSetting("ћод", "RW", "RW", "Matrix");

    private final SliderSetting health = new SliderSetting("«доровье", 3.5f, 1.f, 20.f, 0.05f);
    private final BooleanOption swapBack = new BooleanOption("¬озвращать предмет", true);
    private final BooleanOption noBallSwitch = new BooleanOption("Ќе брать если шар в руке", false);
    private final MultiBoxSetting mode = new MultiBoxSetting("—рабатывать",
            new BooleanOption("«олотые сердца", true),
            new BooleanOption(" ристаллы", true),
            new BooleanOption("ќбсидиан", false),
            new BooleanOption("якорь", false),
            new BooleanOption("ѕадение", true));

    private final SliderSetting radiusExplosion = new SliderSetting("ƒистанци€ до кристала", 6, 1, 8, 1).setVisible(() -> mode.get(1));
    private final SliderSetting radiusObs = new SliderSetting("ƒистанци€ до обсидиана", 6, 1, 8, 1).setVisible(() -> mode.get(2));
    private final SliderSetting radiusAnch = new SliderSetting("ƒистанци€ до €кор€", 6, 1, 8, 1).setVisible(() -> mode.get(2));

    int oldItem = -1;

    public AutoTotemFunction() {
        addSettings(autototemMode, mode, health, swapBack, noBallSwitch);
    }

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            handleEventUpdate((EventUpdate) event);
        }
    }

    /**
     * ќбработка событи€ обновлени€.
     *
     * @param event событие обновлени€.
     */
    private void handleEventUpdate(EventUpdate event) {
        // ѕолучаем слот тотема
        final int slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);

        // ≈сли лева€ рука не пуста€
        boolean handNotNull = !(mc.player.getHeldItemOffhand().getItem() instanceof AirItem);

        // ѕровер€ем, находитс€ ли тотем в руке игрока (в любой из двух рук)
        final boolean totemInHand = mc.player.getHeldItemOffhand()
                .getItem() == Items.TOTEM_OF_UNDYING
                || mc.player.getHeldItemMainhand()
                .getItem() == Items.TOTEM_OF_UNDYING;

        if (condition()) {
            // ≈сли выполн€етс€ условие
            // и есть свободный слот дл€ тотема
            // и тотем не находитс€ в руке, то перемещаем его в руку
            if (slot >= 0 && !totemInHand) {
                InventoryUtil.moveItem(slot, 45, handNotNull);
                if (handNotNull && oldItem == -1) {
                    oldItem = slot;
                }
            }
        } else if (oldItem != -1 && swapBack.get()) {
            // ≈сли условие не выполн€етс€,
            // но был сохранен предыдущий слот тотема и активирован флаг swapBack,
            // то возвращаем тотем на предыдущее место
            InventoryUtil.moveItem(oldItem, 45, handNotNull);
            oldItem = -1;
        }
    }


    /**
     * ѕроверка всех условий
     */
    private boolean condition() {
        // –ассчитываем количество поглощенного урона от эффекта поглощени€
        final float absorption = this.mode.get(0) && mc.player
                .isPotionActive(Effects.ABSORPTION)
                ? mc.player.getAbsorptionAmount()
                : 0.0f;

        // ѕровер€ем услови€, при которых нужно использовать тотем
        if (mc.player.getHealth() + absorption <= this.health.getValue().floatValue())
            return true;

        if (!this.isBall()) {
            if (this.checkCrystal())
                return true;

            if (this.checkObsidian())
                return true;

            if (this.checkAnchor())
                return true;
        }

        return this.checkFall();
    }

    /**
     * ѕроверка услови€ дл€ использовани€ тотема при падении.
     *
     * @return true, если нужно использовать тотем при падении, иначе false.
     */
    private boolean checkFall() {
        if (!this.mode.get(4)) {
            return false;
        }

        if (mc.player.isElytraFlying()) {
            return false;
        }

        return mc.player.fallDistance > 10.0f;
    }

    /**
     * ѕроверка, если у игрока шар в левой руке.
     *
     * @return true, если у игрока шар в левой руке, иначе false.
     */
    private boolean isBall() {
        if (this.mode.get(3) && mc.player
                .fallDistance > 5.0f)
            return false;

        return this.noBallSwitch.get() && mc.player.getHeldItemOffhand()
                .getItem() instanceof SkullItem;
    }

    /**
     * ѕроверка услови€ дл€ использовани€ тотема при наличии обсидиановых блоков в радиусе.
     *
     * @return true, если нужно использовать тотем при наличии обсидиановых блоков в радиусе, иначе false.
     */
    private boolean checkObsidian() {
        if (!mode.get(2))
            return false;

        return WorldUtil.TotemUtil
                .getBlock(radiusObs.getValue().floatValue(), Blocks.OBSIDIAN) != null;
    }

    /**
     * ѕроверка услови€ дл€ использовани€ тотема при наличии €корей возрождени€ в радиусе.
     *
     * @return true, если нужно использовать тотем при наличии €корей возрождени€ в радиусе, иначе false.
     */
    private boolean checkAnchor() {
        if (!mode.get(3))
            return false;

        return WorldUtil.TotemUtil
                .getBlock(radiusAnch.getValue().floatValue(), Blocks.RESPAWN_ANCHOR) != null;
    }

    /**
     * ѕроверка услови€ дл€ использовани€ тотема при наличии кристаллов или TNT в радиусе.
     *
     * @return true, если нужно использовать тотем при наличии кристаллов или TNT в радиусе, иначе false.
     */
    private boolean checkCrystal() {
        if (!mode.get(1))
            return false;

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof EnderCrystalEntity
                    && mc.player.getDistance(entity) <= radiusExplosion.getValue().floatValue())
                return true;

            if ((entity instanceof TNTEntity || entity instanceof TNTMinecartEntity)
                    && mc.player.getDistance(entity) <= radiusExplosion.getValue().floatValue())
                return true;
        }
        return false;
    }


    /**
     * —брос состо€ни€ переменных.
     */
    private void reset() {
        this.oldItem = -1;
    }


    @Override
    protected void onEnable() {
        reset();
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        reset();
        super.onDisable();
    }
}
