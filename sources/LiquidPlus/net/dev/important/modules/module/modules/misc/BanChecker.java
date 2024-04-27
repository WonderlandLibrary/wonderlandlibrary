/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package net.dev.important.modules.module.modules.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.misc.HttpUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;

@Info(name="BanChecker", spacedName="Ban Checker", description="Checks for ban on Hypixel every minute and alert you if there is any.", category=Category.MISC, cnName="\u5c01\u7981\u68c0\u6d4b")
public class BanChecker
extends Module {
    private static String API_PUNISHMENT = BanChecker.aB("68747470733a2f2f6170692e706c616e636b652e696f2f6879706978656c2f76312f70756e6973686d656e745374617473");
    public final BoolValue alertValue = new BoolValue("Alert", true);
    public final BoolValue serverCheckValue = new BoolValue("ServerCheck", true);
    public final IntegerValue alertTimeValue = new IntegerValue("Alert-Time", 10, 1, 50, " seconds");
    public static int WATCHDOG_BAN_LAST_MIN = 0;
    public static int LAST_TOTAL_STAFF = -1;
    public static int STAFF_BAN_LAST_MIN = 0;
    private String checkTag = "Idle...";

    public BanChecker() {
        new Thread("Hypixel-BanChecker"){

            @Override
            public void run() {
                MSTimer checkTimer = new MSTimer();
                while (true) {
                    block11: {
                        if (!checkTimer.hasTimePassed(60000L)) {
                            continue;
                        }
                        try {
                            String apiContent = HttpUtils.get(API_PUNISHMENT);
                            JsonObject jsonObject = new JsonParser().parse(apiContent).getAsJsonObject();
                            if (jsonObject.get("success").getAsBoolean() && jsonObject.has("record")) {
                                JsonObject objectAPI = jsonObject.get("record").getAsJsonObject();
                                WATCHDOG_BAN_LAST_MIN = objectAPI.get("watchdog_lastMinute").getAsInt();
                                int staffBanTotal = objectAPI.get("staff_total").getAsInt();
                                if (staffBanTotal < LAST_TOTAL_STAFF) {
                                    staffBanTotal = LAST_TOTAL_STAFF;
                                }
                                if (LAST_TOTAL_STAFF == -1) {
                                    LAST_TOTAL_STAFF = staffBanTotal;
                                } else {
                                    STAFF_BAN_LAST_MIN = staffBanTotal - LAST_TOTAL_STAFF;
                                    LAST_TOTAL_STAFF = staffBanTotal;
                                }
                                BanChecker.this.checkTag = STAFF_BAN_LAST_MIN + "";
                                if (Client.moduleManager.getModule(BanChecker.class).getState() && ((Boolean)BanChecker.this.alertValue.get()).booleanValue() && mc.field_71439_g != null && (!((Boolean)BanChecker.this.serverCheckValue.get()).booleanValue() || BanChecker.this.isOnHypixel())) {
                                    if (STAFF_BAN_LAST_MIN > 0) {
                                        Client.hud.addNotification(new Notification("Staffs banned " + STAFF_BAN_LAST_MIN + " players in the last minute!", STAFF_BAN_LAST_MIN > 3 ? Notification.Type.ERROR : Notification.Type.WARNING, (long)((Integer)BanChecker.this.alertTimeValue.get()).intValue() * 1000L));
                                    } else {
                                        Client.hud.addNotification(new Notification("Staffs didn't ban any player in the last minute.", Notification.Type.SUCCESS, (long)((Integer)BanChecker.this.alertTimeValue.get()).intValue() * 1000L));
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            if (!Client.moduleManager.getModule(BanChecker.class).getState() || !((Boolean)BanChecker.this.alertValue.get()).booleanValue() || mc.field_71439_g == null || ((Boolean)BanChecker.this.serverCheckValue.get()).booleanValue() && !BanChecker.this.isOnHypixel()) break block11;
                            Client.hud.addNotification(new Notification("An error has occurred.", Notification.Type.ERROR, 1000L));
                        }
                    }
                    checkTimer.reset();
                }
            }
        }.start();
    }

    public boolean isOnHypixel() {
        return !mc.func_71387_A() && BanChecker.mc.func_147104_D().field_78845_b.contains("hypixel.net");
    }

    public static String aB(String str) {
        String result = new String();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i += 2) {
            String st = "" + charArray[i] + "" + charArray[i + 1];
            char ch = (char)Integer.parseInt(st, 16);
            result = result + ch;
        }
        return result;
    }

    @Override
    public String getTag() {
        return this.checkTag;
    }
}

