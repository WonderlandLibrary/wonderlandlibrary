package com.ohare.client.utils.autocheat;

import com.ohare.client.Client;
import com.ohare.client.config.Config;
import com.ohare.client.event.events.game.ServerJoinEvent;

import dorkbox.messageBus.annotations.Subscribe;

/**
 * @author Xen for OhareWare
 * @since 7/29/2019
 **/
public class AutoCheatListener {

    public AutoCheatListener() {
        Client.INSTANCE.getBus().subscribe(this);
    }

    @Subscribe
    public void onJoin(ServerJoinEvent event) {
        if (!Client.INSTANCE.isAutoCheat()) return;
        for (Config config : Client.INSTANCE.getConfigManager().getConfigs()) {
            if (event.getIp().toLowerCase().contains(config.getName().toLowerCase())) {
                Client.INSTANCE.getConfigManager().loadConfig(config.getName());
                Client.INSTANCE.getNotificationManager().addNotification("Switched config to " + config.getName() + ".", 2000);
                break;
            }
        }
    }
}
