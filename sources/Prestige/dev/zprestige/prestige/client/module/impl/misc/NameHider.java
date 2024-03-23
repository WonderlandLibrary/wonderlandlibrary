package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.NameEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.setting.impl.StringSetting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;

public class NameHider extends Module {

    public ModeSetting mode;
    public StringSetting customName;
    public BooleanSetting randomize;
    public FloatSetting speed;
    public TimerUtil timer;
    public String username;

    public NameHider() {
        super("Name Hider", Category.Misc, "Attempts to replace your name in chat and F5");
        mode = setting("Mode", "Random", new String[]{"Random", "Custom"}).description("How to generate the name");
        customName = setting("Custom Name", "PrestigeClient", "Custom Name").invokeVisibility(arg_0 -> mode.getObject().equals("Custom")).description("Custom name to be hidden as");
        randomize = setting("Randomize", true).invokeVisibility(arg_0 -> mode.getObject().equals("Random")).description("Randomize your name every x milliseconds");
        speed = setting("Speed", 100f, 0f, 5000f).invokeVisibility(arg_0 -> randomize.getObject() && mode.getObject().equals("Random")).description("How often to randomize your name");
        timer = new TimerUtil();
        username = "";
    }

    @Override
    public void onEnable() {
        username = getUserName();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (randomize.getObject() && timer.delay(speed.getObject())) {
            username = getUserName();
            timer.reset();
        }
        if (mode.getObject().equals("Custom")) {
            username = getUserName();
        }
    }

    @EventListener
    public void event(NameEvent event) {
        event.setFakeName(getMc().getSession().getUsername());
        event.setName(username);
        event.setCancelled();
    }

    private String getUserName() {
        if (mode.getObject().equals("Custom")) {
            return customName.getObject();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= getMc().player.getDisplayName().getString().length(); ++i) {
            String string = "abcdefghijklmnopqrstuvwxyz0123456789";
            char c = string.charAt(RandomUtil.INSTANCE.getRandom().nextInt(string.length()));
            if (Character.isAlphabetic(c)) {
                if (RandomUtil.INSTANCE.getRandom().nextFloat() > 0.5f) {
                    c = Character.toUpperCase(c);
                }
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
