package dev.zprestige.prestige.client.protection.check.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;

public class SessionCheck2 extends Check {

    public SessionCheck2() {
        super(Category.Session);
    }

    @Override
    public void run() {
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit("G");
        }
    }
}
