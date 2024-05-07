package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "ServerFucker", category = Category.MISC, description = "Fucks many servers with commands")
public class ServerFucker extends Module {

    Setting reportMode = new Setting("Report Mode", this, "Gomme", new String[]{"Gomme", "Normal"});
    Setting delay = new Setting("Delay", this, 2500, 0, 5000, true);
    Setting addFriends = new Setting("Add Friends", this, true);
    Setting inviteToParty = new Setting("Invite to Party", this, true);
    Setting reportPlayer = new Setting("Report Player", this, true);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        timeHelper.reset();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            for (Entity e : getWorld().getLoadedEntityList()) {
                if (e instanceof EntityPlayer && e != getPlayer()) {
                    String playerName = e.getName();

                    if (timeHelper.hasReached((long) delay.getCurrentValue())) {
                        if (addFriends.isToggled()) {
                            sendChatMessage("/friend add " + playerName);
                        }
                        if (inviteToParty.isToggled()) {
                            sendChatMessage("/party invite " + playerName);
                        }
                        if (reportPlayer.isToggled()) {
                            sendChatMessage("/report " + playerName + "Hacking");

                            if (reportMode.getCurrentMode().equalsIgnoreCase("Gomme")) {
                                sendChatMessage("/report confirm");
                            }
                        }
                        timeHelper.reset();
                    }
                }
            }
        }
    }
}
