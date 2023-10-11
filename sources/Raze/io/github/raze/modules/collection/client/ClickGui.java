package io.github.raze.modules.collection.client;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.utilities.collection.visual.ChatUtil;
import org.lwjgl.input.Keyboard;

public class ClickGui extends AbstractModule {

    private final ArraySetting theme;
    private boolean wasMessageSaid;

    public ClickGui() {
        super("ClickGui", "Displays a sexy little gui.", ModuleCategory.CLIENT, Keyboard.KEY_RSHIFT);
        Raze.INSTANCE.managerRegistry.settingRegistry.add(
                theme = new ArraySetting(this, "Theme", "Jello", "Jello", "Sunrise", "Serenity", "Emerald Noir", "Warped Night", "Crimson Night")
        );
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Raze.INSTANCE.screenRegistry.uiModuleManager);
        if(!wasMessageSaid) {
            ChatUtil.addChatMessage("When changing themes, reopen the Module Manager!", true);
            wasMessageSaid = true;
        }
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        Raze.INSTANCE.managerRegistry.themeRegistry.currentTheme = Raze.INSTANCE.managerRegistry.themeRegistry.getTheme(theme.get());
    }
}
