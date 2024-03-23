package dev.zprestige.prestige.client.protection.check.impl;

import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.impl.LoginScreen;

public class LoginCheck extends Check {

    public LoginCheck() {
        super(Category.Normal);
    }

    @Override
    public void run() {
        LoginScreen.class.getName();
        try {
            LoginScreen.isPaste(1);
        }
        catch (Exception exception) {
            ProtectionManager.exit("A");
        }
    }
}
