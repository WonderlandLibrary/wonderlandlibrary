/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.Subscription;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.util.RealmsUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class RealmsSubscriptionInfoScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RealmsScreen lastScreen;
    private final RealmsServer serverData;
    private final RealmsScreen mainScreen;
    private final int BUTTON_BACK_ID = 0;
    private final int BUTTON_DELETE_ID = 1;
    private final int BUTTON_SUBSCRIPTION_ID = 2;
    private int daysLeft;
    private String startDate;
    private Subscription.SubscriptionType type;
    private final String PURCHASE_LINK = "https://account.mojang.com/buy/realms";

    public RealmsSubscriptionInfoScreen(RealmsScreen lastScreen, RealmsServer serverData, RealmsScreen mainScreen) {
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.mainScreen = mainScreen;
    }

    @Override
    public void init() {
        this.getSubscription(this.serverData.id);
        Keyboard.enableRepeatEvents(true);
        this.buttonsAdd(RealmsSubscriptionInfoScreen.newButton(2, this.width() / 2 - 100, RealmsConstants.row(6), RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.extend")));
        this.buttonsAdd(RealmsSubscriptionInfoScreen.newButton(0, this.width() / 2 - 100, RealmsConstants.row(12), RealmsSubscriptionInfoScreen.getLocalizedString("gui.back")));
        if (this.serverData.expired) {
            this.buttonsAdd(RealmsSubscriptionInfoScreen.newButton(1, this.width() / 2 - 100, RealmsConstants.row(10), RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.delete.button")));
        }
    }

    private void getSubscription(long worldId) {
        RealmsClient client = RealmsClient.createRealmsClient();
        try {
            Subscription subscription = client.subscriptionFor(worldId);
            this.daysLeft = subscription.daysLeft;
            this.startDate = this.localPresentation(subscription.startDate);
            this.type = subscription.type;
        }
        catch (RealmsServiceException e) {
            LOGGER.error("Couldn't get subscription");
            Realms.setScreen(new RealmsGenericErrorScreen(e, this.lastScreen));
        }
        catch (IOException ignored) {
            LOGGER.error("Couldn't parse response subscribing");
        }
    }

    @Override
    public void confirmResult(boolean result, int id) {
        if (id == 1 && result) {
            new Thread("Realms-delete-realm"){

                @Override
                public void run() {
                    try {
                        RealmsClient client = RealmsClient.createRealmsClient();
                        client.deleteWorld(((RealmsSubscriptionInfoScreen)RealmsSubscriptionInfoScreen.this).serverData.id);
                    }
                    catch (RealmsServiceException e) {
                        LOGGER.error("Couldn't delete world");
                        LOGGER.error(e);
                    }
                    catch (IOException e) {
                        LOGGER.error("Couldn't delete world");
                        e.printStackTrace();
                    }
                    Realms.setScreen(RealmsSubscriptionInfoScreen.this.mainScreen);
                }
            }.start();
        }
        Realms.setScreen(this);
    }

    private String localPresentation(long cetTime) {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.setTimeInMillis(cetTime);
        return DateFormat.getDateTimeInstance().format(cal.getTime());
    }

    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 2: {
                String extensionUrl = "https://account.mojang.com/buy/realms?sid=" + this.serverData.remoteSubscriptionId + "&pid=" + Realms.getUUID();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(extensionUrl), null);
                RealmsUtil.browseTo(extensionUrl);
                break;
            }
            case 1: {
                String line2 = RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.delete.question.line1");
                String line3 = RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.delete.question.line2");
                Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Warning, line2, line3, true, 1));
            }
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.renderBackground();
        int center = this.width() / 2 - 100;
        this.drawCenteredString(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.title"), this.width() / 2, 17, 0xFFFFFF);
        this.drawString(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.start"), center, RealmsConstants.row(0), 0xA0A0A0);
        this.drawString(this.startDate, center, RealmsConstants.row(1), 0xFFFFFF);
        if (this.type == Subscription.SubscriptionType.NORMAL) {
            this.drawString(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.timeleft"), center, RealmsConstants.row(3), 0xA0A0A0);
        } else if (this.type == Subscription.SubscriptionType.RECURRING) {
            this.drawString(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.recurring.daysleft"), center, RealmsConstants.row(3), 0xA0A0A0);
        }
        this.drawString(this.daysLeftPresentation(this.daysLeft), center, RealmsConstants.row(4), 0xFFFFFF);
        super.render(xm, ym, a);
    }

    private String daysLeftPresentation(int daysLeft) {
        if (daysLeft == -1 && this.serverData.expired) {
            return RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.expired");
        }
        if (daysLeft <= 1) {
            return RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.less_than_a_day");
        }
        int months = daysLeft / 30;
        int days = daysLeft % 30;
        StringBuilder sb = new StringBuilder();
        if (months > 0) {
            sb.append(months).append(" ");
            if (months == 1) {
                sb.append(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.month").toLowerCase(Locale.ROOT));
            } else {
                sb.append(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.months").toLowerCase(Locale.ROOT));
            }
        }
        if (days > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(days).append(" ");
            if (days == 1) {
                sb.append(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.day").toLowerCase(Locale.ROOT));
            } else {
                sb.append(RealmsSubscriptionInfoScreen.getLocalizedString("mco.configure.world.subscription.days").toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }
}

