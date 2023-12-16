package wtf.expensive.modules.impl.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.game.EventMouseTick;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.ClientUtil;

/**
 * @author dedinside
 * @since 09.06.2023
 */
@FunctionAnnotation(name = "MiddleClickFriend", type = Type.Util)
public class MiddleClickFriendFunction extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMouseTick e) {
            handleMouseTickEvent(e);
        }
    }

    /**
     * Обрабатывает событие нажатия кнопки мыши.
     *
     * @param event событие нажатия кнопки мыши
     */
    private void handleMouseTickEvent(EventMouseTick event) {
        if (event.getButton() == 2 && mc.pointedEntity instanceof LivingEntity) {
            String entityName = mc.pointedEntity.getName().getString();

            if (Managment.FRIEND_MANAGER.isFriend(entityName)) {
                Managment.FRIEND_MANAGER.removeFriend(entityName);
                displayRemoveFriendMessage(entityName);
            } else {
                Managment.FRIEND_MANAGER.addFriend(entityName);
                displayAddFriendMessage(entityName);
            }
        }
    }

    /**
     * Отображает сообщение о удалении друга.
     *
     * @param friendName имя друга
     */
    private void displayRemoveFriendMessage(String friendName) {
        ClientUtil.sendMesage(TextFormatting.RED + "Удалил " + TextFormatting.RESET + friendName + " из друзей!");
    }

    /**
     * Отображает сообщение о добавлении друга.
     *
     * @param friendName имя друга
     */
    private void displayAddFriendMessage(String friendName) {
        ClientUtil.sendMesage(TextFormatting.GREEN + "Добавил " + TextFormatting.RESET + friendName + " в друзья!");
    }
}
