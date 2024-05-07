package com.masterof13fps.utils.render;

import java.awt.*;

public class Colors {

    private final static Colors instance = new Colors();

    public static Colors main() {
        return instance;
    }

    public Color notificationInfo = new Color(0, 233, 255, 255);
    public Color notificationWarning = new Color(255, 216, 0, 255);
    public Color notificationError = new Color(255, 0, 0, 255);
    public Color ambienBlueTop = new Color(0x22b4e6);
    public Color ambienBlueBottom = new Color(0x0065b4);
    public Color red = new Color(255, 0, 0);
    public Color orange = new Color(255, 140, 0);
    public Color green = new Color(78, 255, 0);
    public Color grey = new Color(81, 81, 81);
    public Color aqua = new Color(0, 249, 255);

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public int ambienOldBlueColor = new Color(32, 188, 240).getRGB();
    public int ambienNewBlueColor = new Color(0x0090ff).getRGB();
    public int ambienNewDarkGreyColor = new Color(0x20252b).getRGB();
    public int greyColor = new Color(168, 167, 169).getRGB();
    public int vortexRedColor = new Color(0xE37974).getRGB();
    public int suicideBlueGreyColor = new Color(0x1c293a).getRGB();
    public int suicideBlueColor = new Color(0x0993b0).getRGB();
    public int suicideDarkBlueGreyColor = new Color(0x1a1f24).getRGB();
    public int apinityBlueColor = new Color(0x4c80ee).getRGB();
    public int apinityGreyColor = new Color(0x312e30).getRGB();
    public int huzuniBlueColor = new Color(0x00a5ff).getRGB();
    public int huzuniGreyColor = new Color(0x191923).getRGB();
    public int nodusPurpleColor = new Color(0x5a0454).getRGB();
    public int nodusTealColor = new Color(0x94d6ce).getRGB();
    public int saintDarkBlueColor = new Color(0x11274c).getRGB();
    public int saintDarkTealColor = new Color(0x13a4a1).getRGB();
    public int saintOrangeColor = new Color(0xE0B71F).getRGB();
    public int icarusOldOrangeColor = new Color(0xdb9615).getRGB();
    public int icarusOldGreyColor = new Color(0x4a586a).getRGB();
    public int icarusNewBlueColor = new Color(0x92c2cc).getRGB();
    public int icarusNewGreyColor = new Color(0, 0, 0, 100).getRGB();
    public int heroGreenColor = new Color(0x1ab753).getRGB();
    public int heroGreyColor = new Color(0, 0, 0, 70).getRGB();
    public int vantaGreyColor = new Color(0x2e3b4a).getRGB();
    public int vantaBlueColor = new Color(0x0198d6).getRGB();
    public int ambienNewestBlueColor = new Color(0x00a5d1).getRGB();
    public int ambienNewestGreyMainColor = new Color(0x121110).getRGB();
    public int ambienNewestGreySecondColor = new Color(0x212022).getRGB();
    public int ambienNewestLightGreyColor = new Color(0xaaa6a8).getRGB();
    public int koksGreenColor = new Color(0x40e912).getRGB();

    public int getNodusPurpleColor() {
        return nodusPurpleColor;
    }

    public int getNodusTealColor() {
        return nodusTealColor;
    }

    public int getSaintDarkBlueColor() {
        return saintDarkBlueColor;
    }

    public int getSaintDarkTealColor() {
        return saintDarkTealColor;
    }

    public int getSaintOrangeColor() {
        return saintOrangeColor;
    }

    public int getIcarusNewBlueColor() {
        return icarusNewBlueColor;
    }

    public int getIcarusNewGreyColor() {
        return icarusNewGreyColor;
    }

    public int getIcarusOldGreyColor() {
        return icarusOldGreyColor;
    }

    public int getIcarusOldOrangeColor() {
        return icarusOldOrangeColor;
    }

    public int getAmbienNewBlueColor() {
        return ambienNewBlueColor;
    }

    public int getAmbienNewDarkGreyColor() {
        return ambienNewDarkGreyColor;
    }

    public int getHeroGreenColor() {
        return heroGreenColor;
    }

    public int getHeroGreyColor() {
        return heroGreyColor;
    }

    public int getVantaGreyColor() {
        return vantaGreyColor;
    }

    public int getVantaBlueColor() {
        return vantaBlueColor;
    }

    public int getAmbienOldBlueColor() {
        return ambienOldBlueColor;
    }

    public int getGrey() {
        return greyColor;
    }

    public int getVortexRedColor() {
        return vortexRedColor;
    }

    public int getSuicideBlueGreyColor() {
        return suicideBlueGreyColor;
    }

    public int getSuicideBlueColor() {
        return suicideBlueColor;
    }

    public int getSuicideDarkBlueGreyColor() {
        return suicideDarkBlueGreyColor;
    }

    public int getGreyColor() {
        return greyColor;
    }

    public int getApinityGreyColor() {
        return apinityGreyColor;
    }

    public int getApinityBlueColor() {
        return apinityBlueColor;
    }

    public int getHuzuniBlueColor() {
        return huzuniBlueColor;
    }

    public int getHuzuniGreyColor() {
        return huzuniGreyColor;
    }

    public int getAmbienNewestBlueColor() {
        return ambienNewestBlueColor;
    }

    public int getAmbienNewestGreyMainColor() {
        return ambienNewestGreyMainColor;
    }

    public int getAmbienNewestGreySecondColor() {
        return ambienNewestGreySecondColor;
    }

    public int getAmbienNewestLightGreyColor() {
        return ambienNewestLightGreyColor;
    }

    public int getKoksGreenColor() {
        return koksGreenColor;
    }

    public static Colors getInstance() {
        return instance;
    }

    public Color getNotificationInfo() {
        return notificationInfo;
    }

    public Color getNotificationWarning() {
        return notificationWarning;
    }

    public Color getNotificationError() {
        return notificationError;
    }

    public Color getAmbienBlueTop() {
        return ambienBlueTop;
    }

    public Color getAmbienBlueBottom() {
        return ambienBlueBottom;
    }

    public Color getRed() {
        return red;
    }

    public Color getOrange() {
        return orange;
    }

    public Color getGreen() {
        return green;
    }

    public Color getAqua() {
        return aqua;
    }

    public static String getAnsiReset() {
        return ANSI_RESET;
    }

    public static String getAnsiBlack() {
        return ANSI_BLACK;
    }

    public static String getAnsiRed() {
        return ANSI_RED;
    }

    public static String getAnsiGreen() {
        return ANSI_GREEN;
    }

    public static String getAnsiYellow() {
        return ANSI_YELLOW;
    }

    public static String getAnsiBlue() {
        return ANSI_BLUE;
    }

    public static String getAnsiPurple() {
        return ANSI_PURPLE;
    }

    public static String getAnsiCyan() {
        return ANSI_CYAN;
    }

    public static String getAnsiWhite() {
        return ANSI_WHITE;
    }
}
