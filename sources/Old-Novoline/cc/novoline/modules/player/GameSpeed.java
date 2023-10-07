package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;

public class GameSpeed extends AbstractModule {

    public GameSpeed(@NotNull ModuleManager novoline) {
        super(novoline, EnumModuleType.PLAYER, "GameSpeed", "Game Speed");
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        if (mc.player.isPotionActive(Potion.moveSpeed)) {
            mc.timer.timerSpeed = 1.3F + 0.2F * (mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        } else {
            mc.timer.timerSpeed = 1.3F;
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }
}
