package wtf.evolution.module.impl.Player;

import ru.salam4ik.bot.bot.Bot;
import wtf.evolution.command.impl.BotCommand;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "BotSpammer", type = Category.Player)
public class BotSpammer extends Module {

    public SliderSetting delay = new SliderSetting("Delay", 5000F, 0F, 30000F, 500F).call(this);

    public Counter c = new Counter();

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (c.hasReached(delay.get())) {
            for (Bot bot : Bot.bots) {
               bot.getBot().sendChatMessage(BotCommand.text);
            }
            c.reset();
        }
    }

}
