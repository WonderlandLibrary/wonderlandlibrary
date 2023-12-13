package summer.ui.clickui.panel.panels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.lang3.text.WordUtils;

import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.cheats.render.ClickGUI;
import summer.cheat.guiutil.Setting;
import summer.ui.clickui.component.Parentable;
import summer.ui.clickui.component.components.CheatButton;
import summer.ui.clickui.component.components.subButtons.BooleanButton;
import summer.ui.clickui.component.components.subButtons.ModeButton;
import summer.ui.clickui.component.components.subButtons.SliderButton;
import summer.ui.clickui.panel.Panel;
import summer.ui.clickuiutils.AnimationUtil;
import summer.ui.clickuiutils.ColorUtil;
import summer.ui.clickuiutils.RenderUtil;

/**
 * @author: AmirCC
 * 08:44 pm, 10/10/2020, Tuesday
 **/
public class CheatTypePanel extends Panel {

    //arraylists
    private ArrayList<CheatButton> cheatButtons;
    private ArrayList<Parentable> cheatOptions;

    //others
    private Selection cheatType;
    private Section section;
    private CheatButton selectedCheat;

    //animation values
    private float rectAnimation = 0;
    private float backButtonHoverAnimate = 0f;
    private float optionsOffsetAnimate = 14f;

    public CheatTypePanel(Selection cheatType, float x, float y) {
        super(x, y, 120, 300);
        this.cheatType = cheatType;
        this.cheatButtons = new ArrayList<>();
        this.cheatOptions = new ArrayList<>();
        setSection(Section.CHEATS);
        float offsetY = 14;
        for (Cheats cheat : Summer.INSTANCE.cheatManager.getCheats(getCheatType())) {
            cheatButtons.add(new CheatButton(cheat, this, getX(), getY(), getWidth() - 4, 14, 2, offsetY));
            if (Summer.INSTANCE.settingsManager.getSettingsByMod(cheat) != null) {
                float optionsOffset = 14;
                for (Setting setting : Summer.INSTANCE.settingsManager.getSettingsByMod(cheat)) {
                    if (setting.isCheck()) {
                        cheatOptions.add(new BooleanButton(setting, this, getX(), getY(), getWidth() - 4, 12, 2, optionsOffset));
                    }
                    if (setting.isCombo()) {
                        cheatOptions.add(new ModeButton(setting, this, getX(), getY(), getWidth() - 4, 12, 2, optionsOffset));
                    }
                    if (setting.isSlider()) {
                        cheatOptions.add(new SliderButton(setting, this, getX(), getY(), getWidth() - 4, 12, 2, optionsOffset));
                    }
                    optionsOffset += 12;
                }
            }
            offsetY += 14;
        }
    }

    @Override
    public void updatePosition(float x, float y) {
        cheatButtons.forEach(cheatButton -> cheatButton.updatePosition(x, y));
        cheatOptions.forEach(cheatOption -> cheatOption.updatePosition(x, y));
        super.updatePosition(x, y);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        rectAnimation = AnimationUtil.moveUD(rectAnimation, getHeight(), 0.0123f);
        backButtonHoverAnimate = AnimationUtil.moveUD(backButtonHoverAnimate, (isHovered(getX() + 1, getY() + 2, getX() + 14, getY() + 12) ? 0 : 2), 0.000542f);
        float height = 0;
        if (isExpand()) {
            if (getSection() == Section.CHEATS) {
                height = Summer.INSTANCE.cheatManager.getCheats(getCheatType()).isEmpty() ? 14 : 14 + Summer.INSTANCE.cheatManager.getCheats(getCheatType()).size() * 14 + 2;
            } else {
                if (Objects.nonNull(getSelectedCheat())) {
                    height = 14 + (Summer.INSTANCE.settingsManager.getSettingsByMod(getSelectedCheat().getObject()) == null ? 0 : Summer.INSTANCE.settingsManager.getSettingsByMod(getSelectedCheat().getObject()).stream().filter(option -> option.isVisible()).toArray().length * 12 + 2);
                }
            }
        } else {
            height = 14;
        }
        setHeight(height);
        Color c = Color.getHSBColor(ClickGUI.guiHue.getValFloat(), 1.0F, 1.0F);
        if (ClickGUI.rainbowGui.getValBoolean()) {
            RenderUtil.INSTANCE.drawSmoothRect(getX() - 1, getY() - 1, getX() + getWidth() + 1, getY() + rectAnimation + 1, ColorUtil.getRainbow(17000, - 15 * (int) getY() / 2));
        } else RenderUtil.INSTANCE.drawSmoothRect(getX() - 1, getY() - 1, getX() + getWidth() + 1, getY() + rectAnimation + 1, c.getRGB());
        RenderUtil.INSTANCE.drawSmoothRect(getX(), getY(), getX() + getWidth(), getY() + rectAnimation, new Color(36, 36, 36).getRGB());
        Summer.INSTANCE.fontManager.getFont("ROBO 20").drawCenteredString(getSection() == Section.CHEATS ? WordUtils.capitalizeFully(getCheatType().name()) : getSelectedCheat().getObject().getName(), getX() + getWidth() / 2, getY() + 2, -1);
        if (Summer.INSTANCE.cheatManager.getCheats(getCheatType()) != null) {
            if (isExpand()) {

                RenderUtil.INSTANCE.drawSmoothRect(getX() + 2, getY() + 14, getX() + getWidth() - 2, getY() + rectAnimation + (isExpand() ? -2 : 0), new Color(66, 66, 66).getRGB());
                if (getSection() == Section.CHEATS) {
                    cheatButtons.forEach(cheatButton -> cheatButton.drawScreen(mouseX, mouseY, partialTicks));
                } else {
                    Summer.INSTANCE.fontManager.getFont("ROBO 18").drawStringWithShadow("<<", getX() + 2, getY() + backButtonHoverAnimate, -1);
                    if (Objects.nonNull(getSelectedCheat())) {
//                        cheatOptions.stream().filter(parentable -> Client.INSTANCE.getSettingsManager().getSettingsByMod(getSelectedCheat().getObject()).contains(parentable.getObject())).forEach(parentable -> parentable.drawScreen(mouseX, mouseY, partialTicks));
                        float optionsOffset = 14;
                        for (Parentable cheatOption : cheatOptions) {
                            if (Summer.INSTANCE.settingsManager.getSettingsByMod(getSelectedCheat().getObject()) != null)
                                if (Summer.INSTANCE.settingsManager.getSettingsByMod(getSelectedCheat().getObject()).contains(cheatOption.getObject())) {
//                                optionsOffsetAnimate = AnimationUtil.moveUD(optionsOffsetAnimate, optionsOffset, 0.000542f);
                                    if (cheatOption.isVisible()) {
                                        cheatOption.setOffsetY(optionsOffset);
                                        cheatOption.updatePosition(getX(), getY());
                                        cheatOption.drawScreen(mouseX, mouseY, partialTicks);
                                        optionsOffset += 12;
                                    }
                                }
                        }
                    }
                }
            }
        }
        if (!isExpand() && getSection() == Section.SETTINGS) {
            setSection(Section.CHEATS);
            setSelectedCheat(null);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isExpand()) {
            if (getSection() == Section.CHEATS) {
                cheatButtons.forEach(cheatButton -> cheatButton.mouseClicked(mouseX, mouseY, mouseButton));
            } else {
                if (isHovered(getX() + 1, getY() + 2, getX() + 14, getY() + 12) && mouseButton == 0) {
                    setSection(Section.CHEATS);
                    setSelectedCheat(null);
                }
                if (Objects.nonNull(getSelectedCheat()))
//                    if (Client.INSTANCE.getSettingsManager().getSettingsByMod(getSelectedCheat().getObject()) != null)
                    cheatOptions.stream().filter(parentable -> Summer.INSTANCE.settingsManager.getSettingsByMod(getSelectedCheat().getObject()).contains(parentable.getObject()) && parentable.isVisible()).forEach(parentable -> parentable.mouseClicked(mouseX, mouseY, mouseButton));
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (isExpand()) {
            if (getSection() == Section.SETTINGS) {
                if (Objects.nonNull(getSelectedCheat()))
//                    if (Client.INSTANCE.getSettingsManager().getSettingsByMod(getSelectedCheat().getObject()) != null)
                    cheatOptions.stream().filter(parentable -> Summer.INSTANCE.settingsManager.getSettingsByMod(getSelectedCheat().getObject()).contains(parentable.getObject()) && parentable.isVisible()).forEach(parentable -> parentable.mouseReleased(mouseX, mouseY, state));
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() {

        super.handleMouseInput();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (isExpand()) {
            if (getSection() == Section.CHEATS) {
                cheatButtons.forEach(cheatButton -> cheatButton.keyTyped(typedChar, keyCode));
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    public Selection getCheatType() {
        return cheatType;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public CheatButton getSelectedCheat() {
        return selectedCheat;
    }

    public void setSelectedCheat(CheatButton selectedCheat) {
        this.selectedCheat = selectedCheat;
    }
}
