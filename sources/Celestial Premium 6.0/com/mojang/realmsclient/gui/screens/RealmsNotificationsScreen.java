/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsNotificationsScreen
extends RealmsScreen {
    private static final String INVITE_ICON_LOCATION = "realms:textures/gui/realms/invite_icon.png";
    private static final String TRIAL_ICON_LOCATION = "realms:textures/gui/realms/trial_icon.png";
    private static final String NEWS_ICON_LOCATION = "realms:textures/gui/realms/news_notification_mainscreen.png";
    private static final RealmsDataFetcher realmsDataFetcher = new RealmsDataFetcher();
    private volatile int numberOfPendingInvites;
    private static boolean checkedMcoAvailability;
    private static boolean trialAvailable;
    private static boolean validClient;
    private static boolean hasUnreadNews;
    private static final List<RealmsDataFetcher.Task> tasks;

    public RealmsNotificationsScreen(RealmsScreen lastScreen) {
    }

    @Override
    public void init() {
        this.checkIfMcoEnabled();
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
    }

    @Override
    public void tick() {
        if (!(Realms.getRealmsNotificationsEnabled() && Realms.inTitleScreen() && validClient || realmsDataFetcher.isStopped())) {
            realmsDataFetcher.stop();
            return;
        }
        if (!validClient || !Realms.getRealmsNotificationsEnabled()) {
            return;
        }
        realmsDataFetcher.initWithSpecificTaskList(tasks);
        if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
            this.numberOfPendingInvites = realmsDataFetcher.getPendingInvitesCount();
        }
        if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE)) {
            trialAvailable = realmsDataFetcher.isTrialAvailable();
        }
        if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.UNREAD_NEWS)) {
            hasUnreadNews = realmsDataFetcher.hasUnreadNews();
        }
        realmsDataFetcher.markClean();
    }

    private void checkIfMcoEnabled() {
        if (!checkedMcoAvailability) {
            checkedMcoAvailability = true;
            new Thread("Realms Notification Availability checker #1"){

                @Override
                public void run() {
                    RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        RealmsClient.CompatibleVersionResponse versionResponse = client.clientCompatible();
                        if (!versionResponse.equals((Object)RealmsClient.CompatibleVersionResponse.COMPATIBLE)) {
                            return;
                        }
                    }
                    catch (RealmsServiceException e) {
                        if (e.httpResultCode != 401) {
                            checkedMcoAvailability = false;
                        }
                        return;
                    }
                    catch (IOException ignored) {
                        checkedMcoAvailability = false;
                        return;
                    }
                    validClient = true;
                }
            }.start();
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        if (validClient) {
            this.drawIcons(xm, ym);
        }
        super.render(xm, ym, a);
    }

    @Override
    public void mouseClicked(int xm, int ym, int button) {
    }

    private void drawIcons(int xm, int ym) {
        int pendingInvitesCount = this.numberOfPendingInvites;
        int spacing = 24;
        int topPos = this.height() / 4 + 48;
        int baseX = this.width() / 2 + 80;
        int baseY = topPos + 48 + 2;
        int iconOffset = 0;
        if (hasUnreadNews) {
            RealmsScreen.bind(NEWS_ICON_LOCATION);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glScalef(0.4f, 0.4f, 0.4f);
            RealmsScreen.blit((int)((double)(baseX + 2 - iconOffset) * 2.5), (int)((double)baseY * 2.5), 0.0f, 0.0f, 40, 40, 40.0f, 40.0f);
            GL11.glPopMatrix();
            iconOffset += 14;
        }
        if (pendingInvitesCount != 0) {
            RealmsScreen.bind(INVITE_ICON_LOCATION);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(baseX - iconOffset, baseY - 6, 0.0f, 0.0f, 15, 25, 31.0f, 25.0f);
            GL11.glPopMatrix();
            iconOffset += 16;
        }
        if (trialAvailable) {
            RealmsScreen.bind(TRIAL_ICON_LOCATION);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            int ySprite = 0;
            if ((System.currentTimeMillis() / 800L & 1L) == 1L) {
                ySprite = 8;
            }
            RealmsScreen.blit(baseX + 4 - iconOffset, baseY + 4, 0.0f, ySprite, 8, 8, 8.0f, 16.0f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void removed() {
        realmsDataFetcher.stop();
    }

    static {
        tasks = Arrays.asList(new RealmsDataFetcher.Task[]{RealmsDataFetcher.Task.PENDING_INVITE, RealmsDataFetcher.Task.TRIAL_AVAILABLE, RealmsDataFetcher.Task.UNREAD_NEWS});
    }
}

