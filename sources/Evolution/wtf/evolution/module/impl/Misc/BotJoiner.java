package wtf.evolution.module.impl.Misc;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import ru.salam4ik.bot.bot.BotStarter;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "BotJoiner", type = Category.Misc)
public class BotJoiner extends Module {

    @EventTarget
    public void onUpdate(EventMotion e) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                Faker faker = new Faker();
                //BotStarter.run(faker.name().firstName().toLowerCase() + getRandomString(MathHelper.getRandomNumberBetween(2, 5)), true);
            }).start();
        }
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.random(length, "0123456789");
    }

}
