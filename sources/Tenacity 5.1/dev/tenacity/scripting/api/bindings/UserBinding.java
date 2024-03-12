package dev.tenacity.scripting.api.bindings;

import dev.tenacity.Tenacity;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude(Strategy.NAME_REMAPPING)
public class UserBinding {

    public String uid() {
        return String.valueOf(Tenacity.INSTANCE.getIntentAccount().client_uid);
    }

    public String username() {
        return String.valueOf(Tenacity.INSTANCE.getIntentAccount().username);
    }

    public String discordTag() {
        return String.valueOf(Tenacity.INSTANCE.getIntentAccount().discord_tag);
    }

}
