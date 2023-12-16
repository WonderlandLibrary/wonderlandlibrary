package wtf.expensive.modules.impl.movement;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.IPacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.movement.MoveUtil;
import wtf.expensive.util.world.InventoryUtil;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "Flight", type = Type.Movement)
public class FlightFunction extends Function {
    private final ModeSetting flMode = new ModeSetting("Flight Mode",
            "Motion",
            "Motion", "Glide", "Трезубец");

    private final SliderSetting motion
            = new SliderSetting("Speed XZ",
            1F,
            0F,
            8F,
            0.1F).setVisible(() -> !flMode.is("Трезубец"));

    private final SliderSetting motionY
            = new SliderSetting("Speed Y",
            1F,
            0F,
            8F,
            0.1F).setVisible(() -> !flMode.is("Трезубец"));

    private final BooleanOption setPitch = new BooleanOption("Поворачивать голову", false).setVisible(() -> flMode.is("Трезубец"));
    private int originalSlot = -1;
    public long lastUseTridantTime = 0;

    public CopyOnWriteArrayList<IPacket<?>> packets = new CopyOnWriteArrayList<>();

    public FlightFunction() {
        addSettings(flMode, motion, motionY, setPitch);
    }

    @Override
    public void onEvent(final Event event) {

        if (event instanceof EventUpdate) {

            handleFlyMode();
        }
    }

    /**
     * Обрабатывает выбранный режим полета.
     */
    private void handleFlyMode() {
        switch (flMode.get()) {
            case "Motion" -> handleMotionFly();
            case "Glide" -> handleGlideFly();
            case "Трезубец" -> handleTridentFly();
        }
    }


    private void handleTridentFly() {
        final int slot = InventoryUtil.getTrident();
        if ((mc.player.isInWater() || mc.world.getRainStrength(1) == 1)
                && EnchantmentHelper.getRiptideModifier(mc.player.getHeldItemMainhand()) > 0) {
            if (slot != -1) {
                originalSlot = mc.player.inventory.currentItem;
                if (mc.gameSettings.keyBindUseItem.pressed && setPitch.get()) {
                    mc.player.rotationPitch = -90;
                }
                mc.gameSettings.keyBindUseItem.setPressed(mc.player.ticksExisted % 20 < 15);
            }
        }
    }

    /**
     * Обрабатывает режим полета "Motion".
     */
    private void handleMotionFly() {
        final float motionY = this.motionY.getValue().floatValue();
        final float speed = this.motion.getValue().floatValue();

        mc.player.motion.y = 0;

        if (mc.gameSettings.keyBindJump.pressed) {
            mc.player.motion.y = motionY;
        } else if (mc.player.isSneaking()) {
            mc.player.motion.y = -motionY;
        }

        MoveUtil.setMotion(speed);
    }

    /**
     * Обрабатывает режим полета "Glide".
     */
    private void handleGlideFly() {
        if (mc.player.isOnGround()) {
            mc.player.motion.y = 0.42;  // Устанавливаем вертикальную скорость при нахождении на земле
        } else {
            mc.player.setVelocity(0, -0.003, 0);  // Устанавливаем вертикальную скорость при полете
            MoveUtil.setMotion(motion.getValue().floatValue());  // Устанавливаем горизонтальную скорость движения
        }
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;

        if (flMode.is("Трезубец")) {
            if (originalSlot != -1) {
                mc.player.inventory.currentItem = originalSlot;
                originalSlot = -1;
            }
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.gameSettings.keyBindUseItem.setPressed(false);
            }
        }
        super.onDisable();
    }
}
