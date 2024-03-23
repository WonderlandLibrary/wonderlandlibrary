package dev.zprestige.prestige.client.handler.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.handler.Handler;
import dev.zprestige.prestige.client.managers.ProtectionManager;

public class ProtectionHandler implements Handler {

    @Override
    public void register() {
        if (!Prestige.Companion.getProtectionManager().something()) {
            try {
                ProtectionManager.exit("L");
            }
            catch (Exception exception) {
                ProtectionManager.exit("L");
            }
        }
    }
}
