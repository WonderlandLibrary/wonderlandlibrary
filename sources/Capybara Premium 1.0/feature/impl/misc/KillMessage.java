package fun.expensive.client.feature.impl.misc;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.utils.math.TimerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class KillMessage extends Feature {
    public TimerHelper timerHelper = new TimerHelper();

    public KillMessage() {
        super("KillMessage", "При килле пишет что-то в чат", FeatureCategory.Misc);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if ((KillAura.target.getHealth() <= 0.0f || KillAura.target.isDead) && KillAura.target instanceof EntityPlayer) {
            String[] messages = new String[]{"не позорься , купи рич премиум", "че как нищий, рич премиум вгет", "гетай рич премиум пока я твоей матери колени не пробил", "Capybara Premiumеmium вгет быстрее, вк группу сам знаешь", "был бы у тебя рич, ты бы щас не умер", "ахахахах, опозорился нищий, покупай рич премиум", "мамку твою выебал, купи рич уже", "твой софт залупа ебанная, купи рич", "чел... ты ричу слился, тебе не стыдно? покупай его))", "хочешь также? купи рич"};
            String finalText = messages[new Random().nextInt(messages.length)];
            if (timerHelper.hasReached(200)) {
                mc.player.sendChatMessage(KillAura.target.getName() + "," + " " + finalText);
                timerHelper.reset();
            }
        }
    }
}
