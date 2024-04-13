package dev.tenacity.module.impl.misc;

import dev.tenacity.command.impl.FriendCommand;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;
import dev.tenacity.util.player.MovementUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Timer;

public class RegisterModule extends Module {
    private final TimerUtil timerUtil = new TimerUtil();
    public final ModeSetting mode = new ModeSetting("Mode", "Register", "Login");
    private boolean logged_in = false;
    private final NumberSetting delay = new NumberSetting("Delay", 5, 1, 10, 1);
    public RegisterModule() {
        super("AutoReg", "Register/Login automatically on servers", ModuleCategory.MISC);
        initializeSettings(mode, delay);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        switch (mode.getCurrentMode()) {
            case "Register": {
                if (timerUtil.hasTimeElapsed((long) delay.getCurrentValue() * 100, true) && !logged_in) {
                    mc.thePlayer.sendChatMessage("/register 1308reoj38fij3r809j 1308reoj38fij3r809j");
                    ChatUtil.success("Registered!");
                    logged_in = true;
                }
                break;
            }
            case "Login": {
                if (timerUtil.hasTimeElapsed((long) delay.getCurrentValue() * 100, true) && !logged_in) {
                    mc.thePlayer.sendChatMessage("/login 1308reoj38fij3r809j");
                    ChatUtil.success("Logged in!");
                    logged_in = true;
                }
            }
        }
    };
    @Override
    public void onEnable() {
        logged_in = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        logged_in = false;
        super.onDisable();
    }
    private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(mode.getCurrentMode());
}
