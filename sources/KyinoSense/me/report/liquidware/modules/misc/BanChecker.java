/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.report.liquidware.modules.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="BanChecker", spacedName="Ban Checker", description="Checks for ban on Hypixel.net every minute and alert you if there is any.", category=ModuleCategory.WORLD)
public class BanChecker
extends Module {
    private static String API_PUNISHMENT = BanChecker.aB("68747470733a2f2f6170692e706c616e636b652e696f2f6879706978656c2f76312f70756e6973686d656e745374617473");
    public final BoolValue alertValue = new BoolValue("Alert", true);
    public final BoolValue serverCheckValue = new BoolValue("ServerCheck", true);
    public static int WATCHDOG_BAN_LAST_MIN = 0;
    public static int LAST_TOTAL_STAFF = -1;
    public static int STAFF_BAN_LAST_MIN = 0;
    private String checkTag = "Idle...";

    public BanChecker() {
        new Thread("Hypixel-BanChecker"){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run() {
                MSTimer checkTimer = new MSTimer();
                while (true) {
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
                            if (LiquidBounce.moduleManager.getModule(BanChecker.class).getState() && ((Boolean)BanChecker.this.alertValue.get()).booleanValue() && mc.field_71439_g != null && (!((Boolean)BanChecker.this.serverCheckValue.get()).booleanValue() || BanChecker.this.isOnHypixel()) && STAFF_BAN_LAST_MIN <= 0) {
                                // empty if block
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (!LiquidBounce.moduleManager.getModule(BanChecker.class).getState() || !((Boolean)BanChecker.this.alertValue.get()).booleanValue() || mc.field_71439_g == null || !((Boolean)BanChecker.this.serverCheckValue.get()).booleanValue() || BanChecker.this.isOnHypixel()) {
                            // empty if block
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

