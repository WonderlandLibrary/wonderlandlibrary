/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsResourcePackScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RealmsScreen lastScreen;
    private final RealmsServerAddress serverAddress;
    private final ReentrantLock connectLock;

    public RealmsResourcePackScreen(RealmsScreen lastScreen, RealmsServerAddress serverAddress, ReentrantLock connectLock) {
        this.lastScreen = lastScreen;
        this.serverAddress = serverAddress;
        this.connectLock = connectLock;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void confirmResult(boolean result, int id) {
        try {
            if (!result) {
                Realms.setScreen(this.lastScreen);
            } else {
                try {
                    final RealmsServerAddress finalAddress = this.serverAddress;
                    Futures.addCallback(Realms.downloadResourcePack(this.serverAddress.resourcePackUrl, this.serverAddress.resourcePackHash), new FutureCallback<Object>(){

                        @Override
                        public void onSuccess(@Nullable Object result) {
                            RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(RealmsResourcePackScreen.this.lastScreen, new RealmsTasks.RealmsConnectTask(RealmsResourcePackScreen.this.lastScreen, finalAddress));
                            longRunningMcoTaskScreen.start();
                            Realms.setScreen(longRunningMcoTaskScreen);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Realms.clearResourcePack();
                            LOGGER.error(t);
                            Realms.setScreen(new RealmsGenericErrorScreen("Failed to download resource pack!", RealmsResourcePackScreen.this.lastScreen));
                        }
                    });
                }
                catch (Exception e) {
                    Realms.clearResourcePack();
                    LOGGER.error(e);
                    Realms.setScreen(new RealmsGenericErrorScreen("Failed to download resource pack!", this.lastScreen));
                }
            }
        }
        finally {
            if (this.connectLock != null && this.connectLock.isHeldByCurrentThread()) {
                this.connectLock.unlock();
            }
        }
    }
}

