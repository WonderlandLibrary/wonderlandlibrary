package com.masterof13fps.features.ui.tabgui;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.utils.render.Colors;
import com.masterof13fps.utils.render.Rainbow;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;

public class Tab {
    private final TabGUI gui;
    public ArrayList<Module> hacks;
    public String tabName;
    public int selectedItem = 0;
    public int menuHeight = 0;
    public int menuWidth = 0;
    private int colour;

    public Tab(TabGUI GUI, String TabName) {
        tabName = TabName;
        gui = GUI;
        hacks = new ArrayList<Module>();
    }

    public void countMenuSize() {
        int maxWidth = 0;
        for (int i = 0; i < hacks.size(); i++) {
            if (Minecraft.mc().fontRendererObj.getStringWidth(hacks.get(i).name() + 4) > maxWidth) {
                maxWidth = (int) (Minecraft.mc().fontRendererObj.getStringWidth(hacks.get(i).name()) + 7.5F);
            }
        }
        menuWidth = maxWidth;
        menuHeight = (hacks.size() * gui.tabHeight - 1);
    }

    public void drawTabMenu(int x, int y) {
        countMenuSize();
        x += 2;
        y += 2;
        // Background
        String mode = Client.main().setMgr().settingByName("Design", Client.main().modMgr().getByName("HUD")).getCurrentMode();
        switch (mode) {
            case "Ambien Old":
            case "Vortex": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, new Color(0, 0, 0).getRGB());
                break;
            }
            case "Ambien Newest": {
                RenderUtils.drawRect(x + 2, y + 2, x + menuWidth + 3, y + menuHeight + 2, Colors.main().getAmbienNewestGreyMainColor());
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getAmbienNewestGreySecondColor());
                break;
            }
            case "Suicide": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getSuicideBlueGreyColor());
                break;
            }
            case "Apinity": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getApinityGreyColor());
                break;
            }
            case "Huzuni": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getHuzuniGreyColor());
                break;
            }
            case "Saint": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getSaintDarkBlueColor());
                break;
            }
            case "Icarus Old": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getIcarusOldGreyColor());
                break;
            }
            case "Icarus New": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getIcarusNewGreyColor());
                break;
            }
            case "Ambien New": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getAmbienNewDarkGreyColor());
                break;
            }
            case "Hero": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, Colors.main().getHeroGreyColor());
                break;
            }
            case "Koks": {
                RenderUtils.drawRect(x - 1, y - 1, x + menuWidth, y + menuHeight - 1, new Color(0, 0, 0, 220).getRGB());
                break;
            }
        }

        for (int i = 0; i < hacks.size(); i++) {

            Module currentHack = hacks.get(i);
            // Selected Tab Background & String

            switch (mode) {
                case "Ambien Old": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getAmbienOldBlueColor() : 0);
                    Client.main().fontMgr().font("Raleway", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? new Color(255, 255, 255).getRGB() : new Color(181, 181, 181).getRGB());
                    break;
                }
                case "Ambien Newest": {
                    RenderUtils.drawVerticalGradient(x - 1, y + gui.tabHeight * i - 1 , menuWidth, gui.tabHeight,
                            i == TabGUI.selectedItem ? Colors.main().ambienBlueBottom.getRGB() : 0, i == TabGUI.selectedItem ? Colors.main().ambienBlueTop.getRGB() : 0);
                    Client.main().fontMgr().font("BebasNeue", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? new Color(255, 255, 255).getRGB() : Colors.main().getAmbienNewestLightGreyColor());
                    break;
                }
                case "Vortex": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getVortexRedColor() : 0);
                    Client.main().fontMgr().font("Raleway", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? new Color(255, 255, 255).getRGB() : new Color(181, 181, 181).getRGB());
                    break;
                }
                case "Suicide": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Rainbow.rainbow(1, 1f).getRGB() : 0);
                    Client.main().fontMgr().font("Raleway", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? new Color(255, 255, 255).getRGB() : new Color(181, 181, 181).getRGB());
                    break;
                }
                case "Apinity": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getApinityBlueColor() : 0);
                    Client.main().fontMgr().font("Raleway", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? new Color(255, 255, 255).getRGB() : new Color(181, 181, 181).getRGB());
                    break;
                }
                case "Huzuni": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getHuzuniBlueColor() : 0);
                    Client.main().fontMgr().font("Arial", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? new Color(255, 255, 255).getRGB() : new Color(181, 181, 181).getRGB());
                    break;
                }
                case "Saint": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getSaintDarkTealColor() : 0);
                    Client.main().fontMgr().font("Arial", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? Colors.main().getSaintOrangeColor() : new Color(255, 255, 255).getRGB());
                    break;
                }
                case "Icarus Old": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getIcarusOldOrangeColor() : 0);
                    Client.main().fontMgr().font("Arial", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? Colors.main().getSaintOrangeColor() : new Color(255, 255, 255).getRGB());
                    break;
                }
                case "Icarus New": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getIcarusNewBlueColor() : 0);
                    Client.main().fontMgr().font("BigNoodleTitling", 20, Font.BOLD).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? Colors.main().getSaintOrangeColor() : new Color(255, 255, 255).getRGB());
                    break;
                }
                case "Ambien New": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getAmbienNewBlueColor() : 0);
                    Client.main().fontMgr().font("BigNoodleTitling", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? Colors.main().getSaintOrangeColor() : new Color(255, 255, 255).getRGB());
                    break;
                }
                case "Hero": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getHeroGreenColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? Colors.main().getHeroGreenColor() : new Color(255, 255, 255).getRGB());
                    break;
                }
                case "Koks": {
                    RenderUtils.drawRect(x - 1, y + gui.tabHeight * i - 1, x + menuWidth, y + gui.tabHeight * i + 11, i == TabGUI.selectedItem ? Colors.main().getKoksGreenColor() : 0);
                    Client.main().fontMgr().font("Raleway Light", 20, Font.PLAIN).drawStringWithShadow(currentHack.name(), x + 1, y + gui.tabHeight * i + 1, currentHack.state() ? Colors.main().getKoksGreenColor() : new Color(255, 255, 255).getRGB());
                    break;
                }
            }
        }
    }
}
