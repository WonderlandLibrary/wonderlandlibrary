package com.masterof13fps.features.ui.tabgui;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.utils.render.Colors;
import com.masterof13fps.utils.render.Rainbow;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;

public class TabGUI {
    public static int selectedTab = 0;
    public static int selectedItem = 0;
    public static boolean mainMenu = true;
    public static ArrayList<Tab> tabsList;
    private final Minecraft mc;
    public int guiWidth = 88;
    public int guiHeight = 0;
    public int tabHeight = 12;

    public TabGUI(Minecraft minecraft) {
        mc = minecraft;
        tabsList = new ArrayList<>();
        for (Category category : Category.values()) {
            String capitalizedName = category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase();
            final Tab tab = new Tab(this, capitalizedName);

            for (Module module : Client.main().modMgr().getModules()) {
                if (module.category() == category) {
                    tab.hacks.add(module);
                }
            }

            if (!tab.tabName.equalsIgnoreCase("Gui")) {
                tabsList.add(tab);
            }
        }

        guiHeight = (tabHeight + tabsList.size() * tabHeight);
    }

    public static void parseKeyUp() {
        if (mainMenu) {
            selectedTab -= 1;
            if (selectedTab < 0) {
                selectedTab = tabsList.size() - 1;
            }
        } else {
            selectedItem -= 1;
            if (selectedItem < 0) {
                selectedItem = (tabsList.get(selectedTab)).hacks.size() - 1;
            }
        }
    }

    public static void parseKeyDown() {
        if (mainMenu) {
            selectedTab += 1;
            if (selectedTab > tabsList.size() - 1) {
                selectedTab = 0;
            }
        } else {
            selectedItem += 1;
            if (selectedItem > (tabsList.get(selectedTab)).hacks.size() - 1) {
                selectedItem = 0;
            }
        }
    }

    public static void parseKeyLeft() {
        if (!mainMenu) {
            mainMenu = true;
        }
    }

    public static void parseKeyRight() {
        if (mainMenu) {
            mainMenu = false;
            selectedItem = 0;
        }
    }

    public static void parseKeyToggle() {
        if (!mainMenu) {
            int sel = selectedItem;
            (tabsList.get(selectedTab)).hacks.get(sel).toggle();
        }
    }

    public void drawGui(int posX, int posY, int width) {
        int x = posX;
        int y = posY;
        guiWidth = width;
        // Background
        String mode = Client.main().setMgr().settingByName("Design", Client.main().modMgr().getByName("HUD")).getCurrentMode();
        switch (mode) {
            case "Ambien Old":
            case "Vortex": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, new Color(0, 0, 0).getRGB());
                break;
            }
            case "Ambien Newest": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getAmbienNewestGreyMainColor());
                break;
            }
            case "Suicide": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getSuicideBlueGreyColor());
                break;
            }
            case "Apinity": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getApinityGreyColor());
                break;
            }
            case "Huzuni": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getHuzuniGreyColor());
                break;
            }
            case "Saint": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getSaintDarkBlueColor());
                break;
            }
            case "Icarus Old": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getIcarusOldGreyColor());
                break;
            }
            case "Icarus New": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getIcarusNewGreyColor());
                break;
            }
            case "Ambien New": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getAmbienNewDarkGreyColor());
                break;
            }
            case "Hero": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, Colors.main().getHeroGreyColor());
                break;
            }
            case "Koks": {
                RenderUtils.drawRect(posX - 1, posY - 1, posX + guiWidth, posY + guiHeight - 13, new Color(0, 0, 0, 220).getRGB());
                break;
            }
        }

        int yOff = posY;
        for (int i = 0; i < tabsList.size(); i++) {

            switch (mode) {
                case "Ambien Old": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getAmbienOldBlueColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow((tabsList.get(i)).tabName, x + 1, yOff, -1);
                    break;
                }
                case "Ambien Newest": {
                    RenderUtils.drawVerticalGradient(x - 1, yOff - 1, guiWidth + 1, tabHeight, i == selectedTab ? Colors.main().ambienBlueBottom.getRGB() : 0,
                            i == selectedTab ? Colors.main().ambienBlueTop.getRGB() : 0);
                    Client.main().fontMgr().font("BebasNeue", 20, Font.PLAIN).drawCenteredString((tabsList.get(i)).tabName.toUpperCase(), x + 40, yOff + 2, -1);
                    break;
                }
                case "Vortex": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getVortexRedColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow((tabsList.get(i)).tabName, x + 1, yOff, -1);
                    break;
                }
                case "Suicide": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Rainbow.rainbow(1, 1f).getRGB() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 1, yOff, -1);
                    break;
                }
                case "Apinity": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getApinityBlueColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 1, yOff, -1);
                    break;
                }
                case "Huzuni": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getHuzuniBlueColor() : 0);
                    Client.main().fontMgr().font("Arial", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 1, yOff, -1);
                    break;
                }
                case "Saint": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getSaintDarkTealColor() : 0);
                    Client.main().fontMgr().font("Arial", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 1, yOff, -1);
                    break;
                }
                case "Icarus Old": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getIcarusOldOrangeColor() : 0);
                    Client.main().fontMgr().font("Arial", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 1, yOff, -1);
                    break;
                }
                case "Icarus New": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getIcarusNewBlueColor() : 0);
                    Client.main().fontMgr().font("BigNoodleTitling", 20, Font.BOLD).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 1, yOff + 1, -1);
                    break;
                }
                case "Ambien New": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getAmbienNewBlueColor() : 0);
                    Client.main().fontMgr().font("BigNoodleTitling", 20, Font.PLAIN).drawCenteredString(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x + 30, yOff + 1, -1);
                    break;
                }
                case "Hero": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getHeroGreenColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x, yOff + 1, -1);
                    break;
                }
                case "Koks": {
                    RenderUtils.drawRect(x - 1, yOff - 1, x + guiWidth, y + tabHeight * i + 11, i == selectedTab ? Colors.main().getKoksGreenColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow(StringUtils.capitalize((tabsList.get(i)).tabName.toLowerCase()), x, yOff + 1, -1);
                    break;
                }
            }


            if ((i == selectedTab) && (!mainMenu)) {
                (tabsList.get(i)).drawTabMenu(x + guiWidth + 2, yOff - 2);
            }
            yOff += tabHeight;
        }
    }
}
