package koks.api.manager.mainmenu;

import koks.mainmenu.Simple;
import koks.mainmenu.Windows;

import java.util.ArrayList;

public class MainMenuManager {
    public static final ArrayList<MainMenu> MAIN_MENUS = new ArrayList<>();

    public static MainMenu currentMainMenu = null;

    public static void init() {
        MAIN_MENUS.add(new Simple());
        MAIN_MENUS.add(new Windows());

        currentMainMenu = MAIN_MENUS.get(0);
    }

    public static void switchTo(MainMenu mainMenu) {
        currentMainMenu = mainMenu;
        mainMenu.init();
    }
}
