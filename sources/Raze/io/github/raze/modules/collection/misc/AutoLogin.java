package io.github.raze.modules.collection.misc;

import io.github.nevalackin.radbus.Listen;
import io.github.raze.Raze;
import io.github.raze.events.collection.network.EventPacketReceive;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.utilities.collection.visual.ChatUtil;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoLogin extends AbstractModule {

    private final BooleanSetting autoLogin, autoRegister, notify;

    public AutoLogin() {
        super("AutoLogin", "Automatically logins for you.", ModuleCategory.MISC);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                autoLogin = new BooleanSetting(this, "Auto Login", true),
                autoRegister = new BooleanSetting(this, "Auto Register", true),
                notify = new BooleanSetting(this, "Notify", true)

        );
    }

    @Listen
    public void onPacketRecieve(EventPacketReceive event) {
        if(event.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02 = (S02PacketChat) event.getPacket();

            String text = s02.getChatComponent().getUnformattedText();

            if(autoLogin.get()) {
                if(text.contains("/login") || text.contains("/login password") || text.contains("/login <password>")) {
                    mc.thePlayer.sendChatMessage("/login ligma123");
                    if(notify.get())
                        ChatUtil.addChatMessage("Logged in with the password: ligma123", true);
                }
            }

            if(autoRegister.get()) {
                if(text.contains("/register") || text.contains("/register password password") || text.contains("/register <password> <password>") || text.contains("/register <password> <repeat_password>")) {
                    mc.thePlayer.sendChatMessage("/register ligma123 ligma123");
                    if(notify.get())
                        ChatUtil.addChatMessage("Registered with the password: ligma123", true);
                }
            }
        }
    }

}
