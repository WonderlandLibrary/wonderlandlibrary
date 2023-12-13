/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.viamcp.viamcp.exemple;

import me.wintware.client.viamcp.viafabric.ViaFabric;

public class MainExemple {
    public static MainExemple instance = new MainExemple();

    public void startClient() {
        try {
            new ViaFabric().onInitialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopClient() {
    }
}

