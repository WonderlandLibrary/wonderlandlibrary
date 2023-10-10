package ravenweave.client.module.modules.client;

import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.module.setting.impl.ComboSetting;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.ColorM;
import ravenweave.client.utils.Utils;

public class ClickGuiModule extends Module {

    public DescriptionSetting description;
    private static ComboSetting<Preset> preset;
    public static TickSetting clean, reset, notifications;
    public static int guiScale;

    public ClickGuiModule() {
        super("Gui", ModuleCategory.client);
        withKeycode(54);

        this.registerSetting(description = new DescriptionSetting("Opens this GUI"));
        this.registerSetting(clean = new TickSetting("Clean up", false));
        this.registerSetting(reset = new TickSetting("Reset position", false));
        this.registerSetting(notifications = new TickSetting("Notifications", false));
        this.registerSetting(preset = new ComboSetting<>("Preset", Preset.PlusPlus));
    }

    @Override
    public void guiButtonToggled(Setting setting) {
        if (setting == reset) {
            reset.disable();
            Raven.clickGui.resetSort();
        }

    }

    @Override
    public void onEnable() {
        if (Utils.Player.isPlayerInGame() && ((mc.currentScreen != Raven.clickGui) || (mc.currentScreen != Raven.kvCompactGui))) {
            mc.displayGuiScreen(Raven.clickGui);
            Raven.clickGui.initMain();
        }

        this.disable();
    }


    private static Preset getPresetMode() {
        return (Preset) preset.getMode();
    }
    public static boolean showGradientEnabled() {
        return getPresetMode().showGradientEnabled;
    }
    public static boolean showGradientDisabled() {
        return  getPresetMode().showGradientDisabled;
    }
    public static boolean useCustomFont() {
        return  getPresetMode().useCustomFont;
    }
    public static int getEnabledTopRGB(int delay) {
        return  getPresetMode().enabledTopRGB.color(delay);
    }
    public static int getEnabledBottomRGB(int delay) {
        return  getPresetMode().enabledBottomRGB.color(delay);
    }
    public static int getEnabledTextRGB(int delay) {
        return  getPresetMode().enabledTextRGB.color(delay);
    }
    public static int getEnabledTextRGB() {
        return getEnabledTextRGB(0);
    }
    public static int getDisabledTopRGB(int delay) {
        return  getPresetMode().disabledTopRGB.color(delay);
    }
    public static int getDisabledBottomRGB(int delay) {
        return  getPresetMode().disabledBottomRGB.color(delay);
    }
    public static int getDisabledTextRGB(int delay) {
        return  getPresetMode().disabledTextRGB.color(0);
    }
    public static int getDisabledTextRGB() {
        return getDisabledTextRGB(0);
    }
    public static int getSettingBackgroundRGB(int delay) {
        return  getPresetMode().settingBackgroundRGB.color(delay);
    }
    public static int getSettingBackgroundRGB() {
        return getSettingBackgroundRGB(0);
    }
    public static int getCategoryBackgroundRGB(int delay) {
        return  getPresetMode().categoryBackgroundRGB.color(delay);
    }
    public static int getCategoryBackgroundRGB() {
        return getCategoryBackgroundRGB(0);
    }
    public static int getCategoryNameRGB(int delay) {
        return  getPresetMode().categoryNameRGB.color(delay);
    }
    public static int getCategoryNameRGB() {
        return getCategoryNameRGB(0);
    }
    public static int getBoarderColour(int delay) {
        return  getPresetMode().boarderColor.color(delay);
    }
    public static int getBoarderColour() {
        return getBoarderColour(0);
    }
    public static int getCategoryOutlineColor1(int delay) {
        return  getPresetMode().categoryOutlineColor.color(delay);
    }
    public static int getCategoryOutlineColor1() {
        return getCategoryOutlineColor1(0);
    }
    public static int getCategoryOutlineColor2(int delay) {
        return  getPresetMode().categoryOutlineColor2.color(delay);
    }
    public static int getCategoryOutlineColor2() {
        return getCategoryOutlineColor2(0);
    }
    public static boolean isSwingToggled() {
        return  getPresetMode().swing;
    }
    public static boolean isRoundedToggled() {
        return  getPresetMode().swing;
    }
    public static boolean isBoarderToggled() {
        return  getPresetMode().boarder;
    }

    public enum Preset {
        Vape(true, false, true, true, in -> 0xFFFFFFFE, in -> 0x99808080, in -> 0x99808080, in -> -12876693, in -> -12876693, in -> 0xFFFFFFFE, in -> 0xFF000000, in -> 0xFF000000, in -> 0xFFFFFFFE, in -> 0x99808080, true, true, false, in -> -12876693, in -> -12876693, in -> Utils.Client.otherAstolfoColorsDraw(in, 10)),
        PlusPlus(true, false, true, true, in -> 0xFFFFFFFE, in -> -15001318, in -> -15001318, in -> Utils.Client.rainbowDraw(2, in), in -> Utils.Client.rainbowDraw(2, in), in -> 0xFF000000, in -> 0xFF000000,in -> 0xFF000000, in -> 0xFFFFFFFE, in -> 0xFF808080, true, true, true, in -> 0xFFFFFFFE, in -> Utils.Client.astolfoColorsDraw(in, 10), in -> Utils.Client.otherAstolfoColorsDraw(in, 10));

        public boolean showGradientEnabled, showGradientDisabled, useCustomFont, categoryBackground, roundedCorners, swing, boarder;
        public ColorM categoryNameRGB, settingBackgroundRGB, categoryBackgroundRGB, enabledTopRGB, enabledBottomRGB,
        enabledTextRGB, disabledTopRGB, disabledBottomRGB, disabledTextRGB, backgroundRGB, boarderColor, categoryOutlineColor, categoryOutlineColor2;

        Preset(boolean showGradientEnabled, boolean showGradientDisabled, boolean useCustomFont, boolean categoryBackground, ColorM categoryNameRGB, ColorM settingBackgroundRGB, ColorM categoryBackgroundRGB, ColorM enabledTopRGB, ColorM enabledBottomRGB, ColorM enabledTextRGB, ColorM disabledTopRGB, ColorM disabledBottomRGB, ColorM disabledTextRGB, ColorM backgroundRGB, boolean roundedCorners, boolean swing, boolean boarder, ColorM boarderColor, ColorM categoryOutlineColor, ColorM categoryOutlineColor2) {
            this.showGradientEnabled = showGradientEnabled;
            this.showGradientDisabled = showGradientDisabled;
            this.useCustomFont = useCustomFont;
            this.categoryBackground = categoryBackground;
            this.categoryNameRGB = categoryNameRGB;
            this.settingBackgroundRGB = settingBackgroundRGB;
            this.categoryBackgroundRGB = categoryBackgroundRGB;
            this.enabledTopRGB = enabledTopRGB;
            this.enabledBottomRGB = enabledBottomRGB;
            this.enabledTextRGB = enabledTextRGB;
            this.disabledTopRGB = disabledTopRGB;
            this.disabledBottomRGB = disabledBottomRGB;
            this.disabledTextRGB = disabledTextRGB;
            this.backgroundRGB = backgroundRGB;
            this.roundedCorners = roundedCorners;
            this.swing = swing;
            this.boarder = boarder;
            this.boarderColor = boarderColor;
            this.categoryOutlineColor = categoryOutlineColor;
            this.categoryOutlineColor2 = categoryOutlineColor2;
        }

    }
}
