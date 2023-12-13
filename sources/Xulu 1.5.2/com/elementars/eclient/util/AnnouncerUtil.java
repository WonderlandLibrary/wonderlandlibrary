// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import com.elementars.eclient.module.misc.Announcer;

public class AnnouncerUtil
{
    public static String getBlockBreak(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just broke a " + s + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05e8\u05d1\u05e9 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getMove(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just moved " + s + " feet thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d8\u05d9\u05e4 <> \u05d9\u05ea\u05d6\u05d6 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getCraft(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just crafted " + s + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05e8\u05e6\u05d9 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getPickedUp(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just picked up a " + s + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05de\u05e8\u05d4 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getSmelted(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just smelted " + s + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05db\u05ea\u05d4 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getRespawn() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just respawned thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d4\u05d9\u05d9\u05d7\u05ea\u05dc \u05d9\u05ea\u05de\u05e7 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getPlaced(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just placed a " + s + " block thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05d7\u05e0\u05d4 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getDropped(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just dropped a " + s + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05dc\u05e4\u05d4 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getChat() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just opened chat thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d8'\u05e6\u05d4 \u05ea\u05d0 \u05d9\u05ea\u05d7\u05ea\u05e4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getPickBlock() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just used pick block thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05e7\u05d5\u05dc\u05d1 \u05e7\u05d9\u05e4\u05d1 \u05d9\u05ea\u05e9\u05de\u05ea\u05e9\u05d4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getConsole() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just opened my console thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05dc\u05d5\u05e1\u05e0\u05d5\u05e7\u05ea \u05d9\u05ea\u05d7\u05ea\u05e4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getFullScreen() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just turned full screen mode thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05df\u05d9\u05e8\u05e7\u05e1 \u05dc\u05d5\u05e4 \u05d9\u05ea\u05d9\u05e9\u05e2 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getPause() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just paused my game thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05e7\u05d7\u05e9\u05de\u05dc \u05d6\u05d5\u05d0\u05e4 \u05d9\u05ea\u05d9\u05e9\u05e2 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getInventory() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just opened my inventory thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d9\u05dc\u05e9 \u05d9\u05e8\u05d5\u05d8\u05e0\u05d1\u05e0\u05d9\u05d0\u05d4 \u05ea\u05d0 \u05d9\u05ea\u05d7\u05ea\u05e4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getPlayerList() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just looked at the player list thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05dd\u05d9\u05e0\u05e7\u05d7\u05e9\u05d4 \u05ea\u05de\u05d9\u05e9\u05e8 \u05dc\u05e2 \u05d9\u05ea\u05dc\u05db\u05ea\u05e1\u05d4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getScreenShot() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just took a screen shot thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05da\u05e1\u05de \u05dd\u05d5\u05dc\u05d9\u05e6 \u05d9\u05ea\u05d9\u05e9\u05e2 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getSwappedHands() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just swapped hands thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05dd\u05d9\u05d9\u05d3\u05d9 \u05d9\u05ea\u05e4\u05dc\u05d7\u05d4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getCrouched() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just crouched thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d9\u05ea\u05e4\u05e4\u05d5\u05db\u05ea\u05d4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getPerspectives() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just changed perspectives thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d8\u05d1\u05de \u05ea\u05d3\u05d5\u05e7\u05e0 \u05d9\u05ea\u05e4\u05dc\u05d7\u05d4 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getJumped() {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just jumped thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            return "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 \u05d9\u05ea\u05e6\u05e4\u05e7 \u05e2\u05d2\u05e8\u05db";
        }
        return "null";
    }
    
    public static String getAte(final String s) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just ate a " + s + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            final String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 <> \u05d9\u05ea\u05dc\u05db\u05d0 \u05e2\u05d2\u05e8\u05db";
            return x.replaceAll("<>", s);
        }
        return "null";
    }
    
    public static String getAttacked(final String s, final String s2) {
        if (Announcer.mode.getValue().equalsIgnoreCase("English")) {
            return "I just attacked " + s + " with a " + s2 + " thanks to Xulu!";
        }
        if (Announcer.mode.getValue().equalsIgnoreCase("Hebrew")) {
            String x = "!Xulu \u05ea\u05d5\u05db\u05d6\u05d1 :: \u05dd\u05e2 <> \u05ea\u05d0 \u05d9\u05ea\u05e4\u05e7\u05ea \u05e2\u05d2\u05e8\u05db";
            x = x.replaceAll("<>", s);
            return x.replaceAll("::", s2);
        }
        return "null";
    }
}
