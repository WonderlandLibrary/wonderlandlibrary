package wtf.expensive.util.font;

import lombok.SneakyThrows;
import wtf.expensive.util.font.common.Lang;
import wtf.expensive.util.font.styled.StyledFont;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Fonts {
    public static final String FONT_DIR = "/assets/minecraft/expensive/font/";

    public static volatile StyledFont[] minecraft = new StyledFont[24];
    public static volatile StyledFont[] verdana = new StyledFont[24];
    public static volatile StyledFont[] gilroyBold = new StyledFont[24];
    public static volatile StyledFont[] msBold = new StyledFont[24];
    public static volatile StyledFont[] msMedium = new StyledFont[24];
    public static volatile StyledFont[] msLight = new StyledFont[24];
    public static volatile StyledFont[] msRegular = new StyledFont[24];
    public static volatile StyledFont[] msSemiBold = new StyledFont[24];

    public static volatile StyledFont[] gilroy = new StyledFont[24];
    public static volatile StyledFont[] sora = new StyledFont[24];
    public static volatile StyledFont[] woveline = new StyledFont[24];
    public static volatile StyledFont[] icons = new StyledFont[24];
    public static volatile StyledFont[] configIcon = new StyledFont[24];

    public static volatile StyledFont[] icons1 = new StyledFont[131];

    @SneakyThrows
    public static void init() {
        long time = System.currentTimeMillis();

        minecraft[8] = new StyledFont("mc.ttf", 8, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        icons[16] = new StyledFont("penus.ttf", 16, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        icons[12] = new StyledFont("penus.ttf", 12, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        woveline[19] = new StyledFont("woveline.otf", 19, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        icons1[130] = new StyledFont("icons.ttf", 130, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        for (int i = 8; i < 24;i++) {
            icons1[i] = new StyledFont("icons.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 16;i++) {
            verdana[i] = new StyledFont("verdana.ttf", i, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        }
        for (int i = 10; i < 23;i++) {
            sora[i] = new StyledFont("sora.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 23;i++) {
            configIcon[i] = new StyledFont("Glyphter.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 23;i++) {
            gilroyBold[i] = new StyledFont("gilroy-bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 24;i++) {
            gilroy[i] = new StyledFont("gilroy.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }

        for (int i = 8; i < 24;i++) {
            msBold[i] = new StyledFont("Montserrat-Bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msLight[i] = new StyledFont("Montserrat-Light.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msMedium[i] = new StyledFont("Montserrat-Medium.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msRegular[i] = new StyledFont("Montserrat-Regular.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msSemiBold[i] = new StyledFont("Montserrat-SemiBold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        System.out.println("Ўрифты загрузились за: " + (System.currentTimeMillis() - time) + " миллисекунды!");

        //fontThread.shutdown();
    }
}