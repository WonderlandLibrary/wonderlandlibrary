/*
HCU on top
*/
package dev.zprestige.prestige.client.protection.check.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.auth.CheckAuth;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;

public class SessionCheck extends Check {

    public SessionCheck() {
        super(Category.Session);
    }

    @Override
    public void run() {
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit("H");
        }
        CheckAuth.run(Prestige.Companion.getSession().getEmail(), Prestige.Companion.getSession().getID());
    }
}
