package me.aquavit.liquidsense.ui.client.clickgui.neverlose;

import me.aquavit.liquidsense.module.Module;

public class Impl {
    public static String selectedCategory = "Blatant";
    public static Type theType = Type.CLIENT;
    public static String Search = "";
    public static boolean openAbout = false;

    public static boolean openModel = false;

    // middle key ManagerModule
    public static boolean openmidmanger = false;
    public static Module midmangermodule = null;
    public static String midmangerSetnameString = "";
    public static int midmangerPositionX = 0;
    public static int midmangerPositionY = 0;

    public static float coordinateX = 100f;
    public static float coordinateY = 100f;
    public static float lwheel = 0f;
    public static float rwheel = 0f;
    public static String configname = "Global";

    //ClickGui-Color
    public static String hue = "blue";

    public enum Type{
        CLIENT,
        Manager
    }
}
