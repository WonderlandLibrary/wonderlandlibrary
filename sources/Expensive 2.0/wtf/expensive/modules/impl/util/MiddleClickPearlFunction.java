package wtf.expensive.modules.impl.util;

import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.game.EventMouseTick;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AuraFunction;
import wtf.expensive.modules.impl.player.GappleCooldownFunction;
import wtf.expensive.util.world.InventoryUtil;

/**
 * @author dedinside
 * @since 07.06.2023
 */
@FunctionAnnotation(name = "MiddleClickPearl", type = Type.Util)
public class MiddleClickPearlFunction extends Function {

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMouseTick mouseTick) {
            if (mouseTick.getButton() == 2) {
                handleMouseTickEvent();
            }
        }
    }

    /**
     * Обрабатывает событие EventMouseTick при нажатии на правую кнопку мыши (кнопка с кодом 2).
     */
    private void handleMouseTickEvent() {
        if (!mc.player.getCooldownTracker().hasCooldown(Items.ENDER_PEARL) && InventoryUtil.getPearls() >= 0) {
            sendHeldItemChangePacket(InventoryUtil.getPearls());

            sendPlayerRotationPacket(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.isOnGround());
            useItem(Hand.MAIN_HAND);

            sendHeldItemChangePacket(mc.player.inventory.currentItem);
        }
    }

    /**
     * Отправляет пакет смены активного предмета.
     *
     * @param itemSlot Слот предмета
     */
    private void sendHeldItemChangePacket(int itemSlot) {
        mc.player.connection.sendPacket(new CHeldItemChangePacket(itemSlot));
        GappleCooldownFunction cooldown = Managment.FUNCTION_MANAGER.gappleCooldownFunction;
        GappleCooldownFunction.ItemEnum itemEnum = GappleCooldownFunction.ItemEnum.getItemEnum(Items.ENDER_PEARL);

        if (cooldown.state && itemEnum != null && cooldown.isCurrentItem(itemEnum)) {
            cooldown.lastUseItemTime.put(itemEnum.getItem(), System.currentTimeMillis());
        }
    }

    /**
     * Отправляет пакет поворота игрока, дабы кинуть перл не в сторону ротации киллауры.
     *
     * @param yaw      значение поворота по горизонтали
     * @param pitch    значение поворота по вертикали
     * @param onGround значения находится ли игрок на земле
     */
    private void sendPlayerRotationPacket(float yaw, float pitch, boolean onGround) {
        if (Managment.FUNCTION_MANAGER.auraFunction.target != null) {
            mc.player.connection.sendPacket(new CPlayerPacket.RotationPacket(yaw, pitch, onGround));
        }
    }

    /**
     * Отправляет пакет использования предмета в главной руке.
     *
     * @param hand Активная рука
     */
    private void useItem(Hand hand) {
        mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(hand));
        mc.player.swingArm(hand);
    }
}
