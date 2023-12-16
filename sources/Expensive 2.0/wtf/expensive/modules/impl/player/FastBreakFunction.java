package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "FastBreak", type = Type.Player)
public class FastBreakFunction extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            // —брасываем задержку удара блока дл€ игрока
            mc.playerController.blockHitDelay = 0;

            // ѕровер€ем, превышает ли текущий урон блока значение 1.0F
            if (mc.playerController.curBlockDamageMP > 1.0F) {
                // ≈сли превышает, устанавливаем значение урона блока равным 1.0F
                mc.playerController.curBlockDamageMP = 1.0F;
            }
        }
    }
}
