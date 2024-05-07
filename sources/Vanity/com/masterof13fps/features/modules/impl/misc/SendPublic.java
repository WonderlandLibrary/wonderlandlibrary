package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventChat;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleInfo(name = "SendPublic", category = Category.MISC, description = "A module to send some stuff public")
public class SendPublic extends Module {

    public Setting modToggle = new Setting("Mod Toggle", this, false);
    public Setting atAll = new Setting("At All", this, true);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventChat) {
            if (atAll.isToggled() && !(((EventChat) event).getMessage().startsWith(Client.main().getClientPrefix()))) {
                event.setCancelled(true);
                sendPacket(new C01PacketChatMessage("@a " + ((EventChat) event).getMessage()));
            }
        }
    }
}
