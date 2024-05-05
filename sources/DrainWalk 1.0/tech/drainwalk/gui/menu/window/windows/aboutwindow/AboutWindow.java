package tech.drainwalk.gui.menu.window.windows.aboutwindow;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.theme.Theme;
import tech.drainwalk.gui.menu.window.Window;
import tech.drainwalk.utility.render.RenderUtility;

public class AboutWindow extends Window {
    private boolean can;

    public AboutWindow() {
        super(true);
        this.windowWidth = 291 / 2f;
        this.windowHeight = 332 / 2f;
    }

    @Override
    public void renderWindow(int mouseX, int mouseY, float partialTicks) {
        if (ClientColor.prevPanel == MenuMain.selectedTheme.getValue().getPanel()) {
            can = false;
        }
        ClientColor.animation.animate(0, 1, 0.2f, EasingList.NONE, Minecraft.getMinecraft().getRenderPartialTicks());
        RenderUtility.drawRoundedRect(windowX, windowY, windowWidth, windowHeight, 11, -1, 45, ClientColor.panel, ClientColor.panelMain);
        if(DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), windowX, windowY, windowWidth, windowHeight, 11,  0.1f);
        }
        RenderUtility.drawRect(windowX + 0.5f, windowY + (45 / 2f) + 1.5f, windowWidth - 1f, 1, ClientColor.panelLines);
        //icons
        RenderUtility.drawRect(windowX + 129, windowY + 7.5f + 2f, 6, 6, ClientColor.textMain);
        //RenderUtility.drawRect(windowX + 8.5f, windowY + 8.5f, 7, 7, ClientColor.textMain);
        FontManager.ICONS_16.drawString("f",windowX + 8f, windowY + 11f, ClientColor.textMain);

        FontManager.SEMI_BOLD_15.drawString("About " + DrainWalk.CLIENT_NAME.toLowerCase(), windowX + 22.5f, windowY + 10f, ClientColor.textMain);

        //
        String version = "Version: ";
        FontManager.SEMI_BOLD_15.drawString(version, windowX + 10.5f, windowY + 37 + 2.5f, ClientColor.textMain);
        FontManager.SEMI_BOLD_15.drawString(DrainWalk.VERSION, windowX + 10.5f + FontManager.SEMI_BOLD_15.getStringWidth(version), windowY + 37 + 2.5f, ClientColor.main);
        String date = "Build Date: ";
        FontManager.SEMI_BOLD_15.drawString(date, windowX + 10.5f, windowY + 37 + 2.5f + 14, ClientColor.textMain);
        FontManager.SEMI_BOLD_15.drawString(DrainWalk.BUILD, windowX + 10.5f + FontManager.SEMI_BOLD_15.getStringWidth(date), windowY + 37 + 2.5f + 14, ClientColor.main);
        String type = "Build Type: ";
        FontManager.SEMI_BOLD_15.drawString(type, windowX + 10.5f, windowY + 37 + 2.5f + 28, ClientColor.textMain);
        FontManager.SEMI_BOLD_15.drawString(DrainWalk.RELEASE_TYPE, windowX + 10.5f + FontManager.SEMI_BOLD_15.getStringWidth(type), windowY + 37 + 2.5f + 28, ClientColor.main);
        String username = "Nickname: ";
        FontManager.SEMI_BOLD_15.drawString(username, windowX + 10.5f, windowY + 37 + 2.5f + 28 + 14, ClientColor.textMain);
        FontManager.SEMI_BOLD_15.drawString(DrainWalk.getInstance().getProfile().username(), windowX + 10.5f + FontManager.SEMI_BOLD_15.getStringWidth(username), windowY + 37 + 2.5f + 28 + 14, ClientColor.main);
        String subDur = "Subscription duration: ";
        FontManager.SEMI_BOLD_15.drawString(subDur, windowX + 10.5f, windowY + 37 + 2.5f + 28 + 28, ClientColor.textMain);
        FontManager.SEMI_BOLD_15.drawString(DrainWalk.getInstance().getProfile().subscriptionTill(), windowX + 10.5f + FontManager.SEMI_BOLD_15.getStringWidth(subDur), windowY + 37 + 2.5f + 28 + 28, ClientColor.main);
        //
        FontManager.SEMI_BOLD_15.drawString("Style", windowX + 10.5f, windowY + 141.5f + 2.5f, ClientColor.textMain);

        RenderUtility.drawRect(windowX + 7, windowY + 127,windowWidth - 14, 1, ClientColor.panelLines);
        float offset = 0;
        for (Theme theme : MenuMain.selectedTheme.getValues()) {
            RenderUtility.drawRoundedRect(windowX + 43 + offset, windowY + 141, 10, 10, 9, theme.getPanelMain());
            RenderUtility.drawRoundedOutlineRect(windowX + 43 + offset - 0.5f, windowY + 141 - 0.5f, 10 + 1f, 10 + 1f, 9, 1.5f, theme == MenuMain.selectedTheme.getValue() ? theme.getMain() : theme.getPanelLines());

            offset += 5 + 10;
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (Hovered.isHovered(mouseX, mouseY, windowX + 129, windowY + 7.5f + 2f, 6, 6) && mouseButton == 0) {
            setWindowActive(false);
        }
        float offset = 0;
        for (Theme theme : MenuMain.selectedTheme.getValues()) {
            if (Hovered.isHovered(mouseX, mouseY, windowX + 43 + offset, windowY + 141, 10, 10) && mouseButton == 0) {
                if (!can) {
                    update();
                    MenuMain.selectedTheme.setValue(theme);
                    can = true;
                }
            }
            offset += 5 + 10;
        }
    }

    public void update() {
        ClientColor.prevPanelMain = MenuMain.selectedTheme.getValue().getPanelMain();
        ClientColor.prevPanel = MenuMain.selectedTheme.getValue().getPanel();
        ClientColor.prevPanelLines = MenuMain.selectedTheme.getValue().getPanelLines();
        ClientColor.prevObject = MenuMain.selectedTheme.getValue().getObject();

        ClientColor.prevTextMain = MenuMain.selectedTheme.getValue().getTextMain();
        ClientColor.prevTextStay = MenuMain.selectedTheme.getValue().getTextStay();

        ClientColor.prevMain = MenuMain.selectedTheme.getValue().getMain();
        ClientColor.prevMainStay = MenuMain.selectedTheme.getValue().getMainStay();
        ClientColor.prevCategory = MenuMain.selectedTheme.getValue().getCategory();

        ClientColor.prevCheckBoxStayBG = MenuMain.selectedTheme.getValue().getCheckBoxStayBG();
        ClientColor.prevCheckBoxStay = MenuMain.selectedTheme.getValue().getCheckBoxStay();
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        super.updateScreen(mouseX, mouseY);

        ClientColor.animation.update(can);
        if (ClientColor.animation.getAnimationValue() >= 1) {
            if (ClientColor.prevPanel != MenuMain.selectedTheme.getValue().getPanel()) {
                update();
            }
            ClientColor.animation.setPrevValue(0);
            ClientColor.animation.setValue(0);
        }

        if (Hovered.isHovered(mouseX, mouseY, windowX + 129, windowY + 7.5f + 2f, 6, 6)) {
            setCanDragging(false);
        } else {
            setCanDragging(true);
        }
        float offset = 0;
        for (Theme theme : MenuMain.selectedTheme.getValues()) {
            if (Hovered.isHovered(mouseX, mouseY, windowX + 43 + offset, windowY + 141, 10, 10)) {
                setCanDragging(false);
            }

            offset += 5 + 10;


        }
    }
}
