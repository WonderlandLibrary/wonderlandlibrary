package dev.zprestige.prestige.client.protection.check.impl;

import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionCheck extends Check {

    public ConnectionCheck() {
        super(Category.Normal);
    }

    @Override
    public void run() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows") && !(InetAddress.getByName("prestigeclient.vip")).isReachable(1000)) {
                ProtectionManager.exit("D");
            }
        }  catch (Exception exception) {
            // empty catch block
        }
    }

}
