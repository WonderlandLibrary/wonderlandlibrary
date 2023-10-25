package tech.drainwalk.client.theme;

import tech.drainwalk.animation.Animation;
import tech.drainwalk.gui.menu.MenuMain;

public class ClientColor {
    public static final Animation animation = new Animation();

    public static int prevPanelMain = MenuMain.selectedTheme.getValue().getPanelMain();
    public static int prevPanel = MenuMain.selectedTheme.getValue().getPanel();
    public static int prevPanelLines = MenuMain.selectedTheme.getValue().getPanelLines();
    public static int prevObject = MenuMain.selectedTheme.getValue().getObject();

    public static int prevTextMain = MenuMain.selectedTheme.getValue().getTextMain();
    public static int prevTextStay = MenuMain.selectedTheme.getValue().getTextStay();

    public static int prevMain = MenuMain.selectedTheme.getValue().getMain();
    public static int prevMainStay = MenuMain.selectedTheme.getValue().getMainStay();

    public static int prevCategory = MenuMain.selectedTheme.getValue().getCategory();

    public static int prevCheckBoxStayBG = MenuMain.selectedTheme.getValue().getCheckBoxStayBG();
    public static int prevCheckBoxStay = MenuMain.selectedTheme.getValue().getCheckBoxStay();

    public static int panelMain = MenuMain.selectedTheme.getValue().getPanelMain();
    public static int panel = MenuMain.selectedTheme.getValue().getPanel();
    public static int panelLines = MenuMain.selectedTheme.getValue().getPanelLines();

    public static int object = MenuMain.selectedTheme.getValue().getObject();

    public static int textMain = MenuMain.selectedTheme.getValue().getTextMain();
    public static int textStay = MenuMain.selectedTheme.getValue().getTextStay();

    public static int main = MenuMain.selectedTheme.getValue().getMain();
    public static int mainStay = MenuMain.selectedTheme.getValue().getMainStay();

    public static int category = MenuMain.selectedTheme.getValue().getCategory();


    public static int checkBoxStayBG = MenuMain.selectedTheme.getValue().getCheckBoxStayBG();
    public static int checkBoxStay = MenuMain.selectedTheme.getValue().getCheckBoxStay();
}
