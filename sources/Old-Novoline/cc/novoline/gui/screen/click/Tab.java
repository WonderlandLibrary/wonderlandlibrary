package cc.novoline.gui.screen.click;

import cc.novoline.Novoline;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.configurations.holder.ModuleHolder;
import cc.novoline.modules.visual.ClickGUI;
import cc.novoline.utils.RenderUtils;
import static cc.novoline.utils.fonts.impl.Fonts.ICONFONT.ICONFONT_24.ICONFONT_24;
import static cc.novoline.utils.fonts.impl.Fonts.ICONFONT.ICONFONT_35.ICONFONT_35;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_16.SFTHIN_16;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_20.SFTHIN_20;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class Tab {

    private final Novoline novoline;

    private final EnumModuleType category;
    private final List<Module> moduleList = new CopyOnWriteArrayList<>();
    private final int y;

    private boolean selected;
    private int block = 5;

    public Tab(@NotNull Novoline novoline, @NotNull EnumModuleType category, int y) {
        this.novoline = novoline;
        this.category = category;
        this.y = y;

        int modY = 30;

        for (ModuleHolder<?> holder : this.novoline.getModuleManager().getModuleManager().values()) {
            AbstractModule module = holder.getModule();

            if (module.getType().equals(this.category)) {
                moduleList.add(new Module(module, modY));
                modY += 18;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();

        if (selected && areButtonsHovered(mouseX, mouseY)) {
            Scroll scroll = DiscordGUI.scroll();

            if (scroll != null) {
                switch (scroll) {
                    case DOWN:
                        if (!moduleList.isEmpty() && moduleList.get(moduleList.size() - 1).getY() > discordGUI.getHeight() - 14) {
                            for (Module module : moduleList) {
                                module.setY(module.getY() - 7);
                            }
                        }

                        break;

                    case UP:
                        if (moduleList.get(0).getY() < 30) {
                            for (Module module : moduleList) {
                                module.setY(module.getY() + 7);
                            }
                        }

                        break;
                }
            }
        }

        int xCoordinate = discordGUI.getXCoordinate();
        int yCoordinate = discordGUI.getYCoordinate();

        int guiColor = novoline.getModuleManager().getModule(ClickGUI.class).getGUIColor();

        if (isHovered(mouseX, mouseY)) {
            if (selected) {
                Gui.drawRect(xCoordinate, yCoordinate + y - block, xCoordinate + 2, yCoordinate + y + block, 0xFFFFFFFF);
            } else {
                Gui.drawRect(xCoordinate, yCoordinate + y - 5, xCoordinate + 2, yCoordinate + y + 5, 0xFFFFFFFF);
            }
            RenderUtils.drawRoundedRect(xCoordinate - SFTHIN_16.stringWidth(
                    category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase()) - 12, yCoordinate + y - 6, SFTHIN_16.stringWidth(
                    category.name().substring(0, 1).toUpperCase() + category
                    .name().substring(1).toLowerCase()) + 7, 11, 5, 0xFF2F2F2F);
            SFTHIN_16.drawString(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(), xCoordinate - SFTHIN_16.stringWidth(
                    category.name().substring(0, 1).toUpperCase() + category
                    .name().substring(1).toLowerCase()) - 9, yCoordinate + y - 3, 0xFFFFFFFF);
        } else {
            if (selected) {
                Gui.drawRect(xCoordinate, yCoordinate + y - block, xCoordinate + 2, yCoordinate + y + block, 0xFFFFFFFF);
            }
        }

        if (selected && block <= 10) {
            this.block++;
        } else if (!selected) {
            this.block = 5;
        }

        RenderUtils.drawFilledCircle(xCoordinate + 22, yCoordinate + y, 15, 0xFF36393F);
        ICONFONT_35.drawCenteredString(getLetterForTab(), (float) getCoords()[0], (float) getCoords()[1], isHovered(mouseX,mouseY) ? guiColor : 0xFFFFFFFF);

        if (selected) {
            ICONFONT_24.drawString(getLetterForTab(), xCoordinate + (getLetterForTab().equals("D") ? 49 : 50), yCoordinate + 7, 0xFFFFFBFF);
            SFTHIN_20.drawString(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(), xCoordinate + 63, yCoordinate + 7, 0xFFFFFBFF);

            GL11.glPushMatrix();
            GL11.glScissor(0, discordGUI.sHeight() - discordGUI.getYCoordinate() * 2 - discordGUI.getHeight() * 2, 1920, discordGUI.getHeight() * 2 - 42);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            moduleList.stream() ////
                    .sorted((o1, o2) -> Boolean.compare(o1.isSelected(), o2.isSelected())) //
                    .forEach(module -> module.drawScreen(mouseX, mouseY));

            for (Module module : moduleList) {
                module.drawScreen2(mouseX, mouseY);
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        for (Module module : moduleList) module.mouseClicked(mouseX, mouseY, mouseButton);

        switch (mouseButton) {
            case 0:
                for (Module module : moduleList) {
                    if (module.isHovered(mouseX, mouseY) && mouseY >= discordGUI.getYCoordinate() + 20 && mouseY <= discordGUI.getYCoordinate() + discordGUI.getHeight()) {
                        module.getData().toggle();
                        break;
                    }
                }

                break;

            case 1:
                for (Module module : moduleList) {
                    if (module.isHovered(mouseX, mouseY) && mouseY >= discordGUI.getYCoordinate() + 20 && mouseY <= discordGUI.getYCoordinate() + discordGUI.getHeight()) {
                        for (Module o : moduleList) {
                            if (!o.equals(module)) {
                                o.setSelected(false);
                            }
                        }
                        module.setSelected(!module.isSelected());
                        break;
                    }
                }

                break;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        moduleList.forEach(module -> module.mouseReleased(mouseX, mouseY, state));
    }

    protected void keyTyped(char typedChar, int keyCode) {
        for(Module module : moduleList) module.keyTyped(typedChar, keyCode);
    }

    private boolean areButtonsHovered(int mouseX, int mouseY) {
        DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        return mouseX >= discordGUI.getXCoordinate() + 45 && mouseX <= discordGUI.getXCoordinate() + 45 + 110 && mouseY >= discordGUI
                .getYCoordinate() + 22 && mouseY <= discordGUI.getYCoordinate() + 300;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        DiscordGUI discordGUI = Novoline.getInstance().getDiscordGUI();
        return mouseX >= discordGUI.getXCoordinate() + 8 && mouseX <= discordGUI.getXCoordinate() + 35 && mouseY >= discordGUI
                .getYCoordinate() + y - 15 && mouseY <= discordGUI.getYCoordinate() + y + 15;
    }

    private boolean anyModsExtended() {
        return moduleList.stream().anyMatch(Module::isSelected);
    }

    private @NotNull String getLetterForTab() {
        switch(category.name()) {
            case "COMBAT":
                return "D"; // @off
            case "MOVEMENT":
                return "A";
            case "PLAYER":
                return "B";
            case "VISUALS":
                return "C";
            case "MISC":
                return "F";
            case "EXPLOITS":
                return "G";
            default:
                return "";
        } // @on
    }

    private double[] getCoords() {
        switch (category.name()) { // @off
            case "COMBAT":
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 21.5D, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 4.5D};
            case "MOVEMENT":
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 22.5, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 5};
            case "PLAYER":
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 22.2D, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 6};
            case "VISUALS":
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 22D, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 5};
            case "EXPLOITS":
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 22.3D, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 5.6};
            case "MISC":
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 22.5D, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 5};
            default:
                return new double[]{Novoline.getInstance().getDiscordGUI().getXCoordinate() + 22D, Novoline.getInstance().getDiscordGUI().getYCoordinate() + y - 5};
        } // @on
    }

    //region Lombok
    public List<Module> getModuleList() {
        return moduleList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
        if(!(this.selected = b)) {
            for(Setting setting : Manager.getSettingList()) {
                switch(setting.getSettingType()) {
                    case COMBOBOX:
                    case SELECTBOX:
                        setting.setOpened(false);
                        break;

                    case TEXTBOX:
                        setting.setTextHovered(false);
                        break;
                }
            }
        }
    }
    //endregion

}
