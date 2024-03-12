// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.Module;

public final class Spammer extends Module
{
    private final StringSetting text;
    private final NumberSetting delay;
    private final MultipleBoolSetting settings;
    private final TimerUtil delayTimer;
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        String spammerText = this.text.getString();
        if (spammerText != null && this.delayTimer.hasTimeElapsed(this.settings.getSetting("Bypass").isEnabled() ? 2000L : this.delay.getValue().longValue())) {
            if (this.settings.getSetting("AntiSpam").isEnabled()) {
                spammerText = spammerText + " " + MathUtils.getRandomInRange(10, 100000);
            }
            Spammer.mc.thePlayer.sendChatMessage(spammerText);
            this.delayTimer.reset();
        }
    }
    
    public Spammer() {
        super("Spammer", "Spammer", Category.MISC, "Spams in chat");
        this.text = new StringSetting("Text");
        this.delay = new NumberSetting("Delay", 100.0, 1000.0, 100.0, 1.0);
        this.settings = new MultipleBoolSetting("Settings", new BooleanSetting[] { new BooleanSetting("AntiSpam", false), new BooleanSetting("Bypass", false) });
        this.delayTimer = new TimerUtil();
        this.addSettings(this.text, this.delay, this.settings);
    }
}
