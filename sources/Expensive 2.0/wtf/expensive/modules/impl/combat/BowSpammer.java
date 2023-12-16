package wtf.expensive.modules.impl.combat;

import net.minecraft.item.BowItem;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

/**
 * @author dedinside
 * @since 29.06.2023
 */
@FunctionAnnotation(name = "BowSpammer", type = Type.Combat)
public class BowSpammer extends Function {


    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate eventUpdate) {
            handleUpdateEvent(eventUpdate);
        }
    }

    /**
     * Обрабатывает событие обновления
     *
     * @param eventUpdate обработчик обновления
     */
    private void handleUpdateEvent(EventUpdate eventUpdate) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof BowItem && mc.player.isHandActive()
                && mc.player.getItemInUseMaxCount() >= 1.5f) {
            mc.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM,
                    new BlockPos(0, 0, 0), mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            mc.player.stopActiveHand();
        }
    }
}
