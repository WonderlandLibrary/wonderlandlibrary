package cc.novoline.gui.screen.click;

import cc.novoline.Novoline;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.binds.KeyboardKeybind;
import cc.novoline.modules.visual.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static cc.novoline.gui.screen.setting.Manager.getSettingList;
import static cc.novoline.gui.screen.setting.Manager.getSettingsByMod;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_26.SFBOLD_26;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_12.SFTHIN_12;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_20.SFTHIN_20;
import static net.minecraft.client.gui.Gui.drawRect;
import static net.minecraft.util.MathHelper.clamp_float;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Module {

    /* fields */
    @NonNull
    private final AbstractModule data;

    private boolean selected;
    private boolean listening;
    private int y;

    /* constructors */
    public Module(@NonNull AbstractModule module, int y) {
        this.data = module;
        this.y = y;
    }

    /* methods */
    public void drawScreen(int mouseX, int mouseY) {
        final DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        ClickGUI clickGUI = Novoline.getInstance().getModuleManager().getModule(ClickGUI.class);
        final boolean isMaterial = clickGUI.design.equalsIgnoreCase("Material");

        final int xCoordinate = discordGUI.getXCoordinate();
        final int yCoordinate = discordGUI.getYCoordinate();

        if (isHovered(mouseX, mouseY) || this.selected) {
            drawRect(xCoordinate + 45, //
                    yCoordinate + this.y - 4, //
                    xCoordinate + 45 + 110, //
                    yCoordinate + this.y + SFTHIN_20.getHeight() + 4 > yCoordinate + discordGUI.getHeight() ? clamp_float(yCoordinate + this.y + SFTHIN_20.getHeight() + 4, yCoordinate + this.y - 4, yCoordinate + discordGUI.getHeight()) : yCoordinate + this.y + SFTHIN_20.getHeight() + 4, //
                    isMaterial ? new Color(32, 34, 37).getRGB() : 0xFF36393E);
        }

        SFBOLD_26.drawString("#", xCoordinate + 50, yCoordinate + this.y - 2, 0xFF605D60);

        if (this.data.isEnabled()) {
            SFTHIN_20.drawString(this.data.getName(), xCoordinate + 63, yCoordinate + this.y, 0xFFE3DFE3);
        } else {
            SFTHIN_20.drawString(this.data.getName(), xCoordinate + 63, yCoordinate + this.y, 0xFF868386);
        }

        try {

            final Tab currentTab = discordGUI.getSelectedTab();

            if (isHovered(mouseX, mouseY) && (currentTab == null || currentTab.getModuleList().stream().noneMatch(Module::isListening)) && isNotBlank(getData().getDescription())) {
                String description = getData().getDescription();
                final String novoline;

                if (isMaterial) {
                    // noinspection SpellCheckingInspection
                } else {
                }

                SFTHIN_12.drawString(description, xCoordinate + 147 + discordGUI.getWidth() - SFTHIN_12.stringWidth(description), yCoordinate - 6, 0xFFFFFF, true);
            }
        } catch (NullPointerException ignored) {
        }
    }

    void drawScreen2(int mouseX, int mouseY) {
        final DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();

        final List<Setting> settingsByMod = getSettingsByMod(this.data);

        if (this.selected) {
            final Scroll scroll = DiscordGUI.scroll();

            if (scroll != null && settingsByMod != null && !settingsByMod.isEmpty()) {
                switch (scroll) {
                    case DOWN:
                        if (settingsByMod.get(settingsByMod.size() - 1).getY() > discordGUI.getYCoordinate() + discordGUI.getHeight() - 14) {
                            for (Setting setting : settingsByMod) {
                                setting.setOffset(setting.getOffset() - 5);
                            }
                        }

                        break;

                    case UP:
                        if (settingsByMod.get(0).getY() < discordGUI.getYCoordinate() + 30) {
                            for (Setting setting : settingsByMod) {
                                setting.setOffset(setting.getOffset() + 5);
                            }
                        }

                        break;
                }
            }
        }

        if (this.selected) {
            SFTHIN_20.drawString(this.data.getName() + " Settings", discordGUI.getXCoordinate() + 165, discordGUI.getYCoordinate() + 6, 0xFFFFFFFF);

            if (settingsByMod == null || settingsByMod.isEmpty()) {
                final String text = "NO SETTINGS ;(";
                final FontRenderer fontRenderer = Minecraft.getInstance().fontRendererObj;

                fontRenderer.drawStringWithShadow(text, //
                        discordGUI.getXCoordinate() + 150 + (discordGUI.getWidth() - fontRenderer.getStringWidth(text)) / 2F, //
                        discordGUI.getYCoordinate() + (discordGUI.getHeight() - fontRenderer.getHeight()) / 2F, //
                        0xFF6A7179);
            }
        }

        GL11.glPushMatrix();
        //Gui.drawRect(0,discordGUI.sHeight() / 2 - discordGUI.getYCoordinate() - discordGUI.getHeight(),1920,discordGUI.sHeight() / 2,0xffffffff);
        GL11.glScissor(0, discordGUI.sHeight() - discordGUI.getYCoordinate() * 2 - discordGUI.getHeight() * 2, 1920, discordGUI.getHeight() * 2 - 42);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        getSettingList().stream().filter(setting -> setting.getSupplier() != null ? setting.getSupplier().get() : true) //
                .filter(setting -> setting.getParentModule().equals(this.data) && this.selected) //
                .sorted((o1, o2) -> Boolean.compare(o1.isOpened(), o2.isOpened())) //
                .forEach(setting -> {
                    setting.update();
                    setting.drawScreen(mouseX, mouseY);
                });
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }


    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        if (mouseY >= discordGUI.getYCoordinate() + 20 && mouseY <= discordGUI.getYCoordinate() + discordGUI.getHeight() && isHovered(mouseX, mouseY)) {
            if (mouseButton == 2) {
                final Tab currentTab = Novoline.getInstance().getDiscordGUI().getSelectedTab();

                if (currentTab == null) {
                    return;
                }

                for (Module module : currentTab.getModuleList()) {
                    if (module.listening) {
                        module.listening = false;
                    }
                }

                this.listening = true;
            }
        }

        if (this.selected) {
            for (Setting setting : getSettingList()) {
                if (mouseY >= discordGUI.getYCoordinate() + 20 && mouseY <= discordGUI.getYCoordinate() + discordGUI.getHeight() && this.data.equals(setting.getParentModule()) && setting.mouseClicked(mouseX, mouseY, mouseButton)) {
                    return;
                }
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        final DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        if (this.listening) {
            if (keyCode != Keyboard.KEY_ESCAPE) {
                this.data.setKeyBind(KeyboardKeybind.of(keyCode));
                this.data.getNovoline().getModuleManager().getBindManager().save();
            }

            this.listening = false;
        }

        if (this.selected) {
            for (Setting setting : getSettingList()) {
                if (this.data.equals(setting.getParentModule())) {
                    setting.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.selected) {
            getSettingList().stream().filter(setting -> setting.getParentModule().equals(this.data) && setting.isInsideMenu()).forEach(setting -> setting.mouseReleased(mouseX, mouseY, state));
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        final DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        final int xCoordinate = discordGUI.getXCoordinate(), // @off
                yCoordinate = discordGUI.getYCoordinate(); // @on

        return mouseX >= xCoordinate + 45 && mouseX <= xCoordinate + 45 + 110 && mouseY >= yCoordinate + this.y - 4 && mouseY <= yCoordinate + this.y + SFTHIN_20.getHeight() + 4;
    }

    public boolean isInsideMenu() {
        final DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        final int yCoordinate = discordGUI.getYCoordinate();

        return yCoordinate + this.y <= yCoordinate + discordGUI.getHeight() - 9 && yCoordinate + this.y >= yCoordinate + 23;
    }

    //region Lombok
    @NonNull
    public AbstractModule getData() {
        return this.data;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isListening() {
        return this.listening;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    //endregion

}
