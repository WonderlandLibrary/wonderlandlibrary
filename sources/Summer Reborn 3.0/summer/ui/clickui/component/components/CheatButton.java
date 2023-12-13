package summer.ui.clickui.component.components;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.EnumChatFormatting;
import summer.Summer;
import summer.base.manager.config.Cheats;
import summer.cheat.cheats.render.ClickGUI;
import summer.ui.clickui.component.Parentable;
import summer.ui.clickui.panel.panels.CheatTypePanel;
import summer.ui.clickui.panel.panels.Section;
import summer.ui.clickuiutils.AnimationUtil;
import summer.ui.clickuiutils.ColorUtil;
import summer.ui.clickuiutils.RenderUtil;
import summer.base.utilities.ChatUtils;

/**
 * @author: AmirCC
 * 10:37 pm, 10/10/2020, Tuesday
 **/
public class CheatButton extends Parentable<Cheats, CheatTypePanel> {

    //animations
    private float textHoverAnimate = 0f;
    private float activeRectAnimate = 0f;

    //other shits
    private boolean binding;

    public CheatButton(Cheats object, CheatTypePanel parent, float staticX, float staticY, float width, float height, float offsetX, float offsetY) {
        super(object, parent, staticX, staticY, width, height, offsetX, offsetY);
    }

    @Override
    public void updatePosition(float x, float y) {
        setX(x + getOffsetX());
        setY(y + getOffsetY());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean hover = isHovered(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        textHoverAnimate = AnimationUtil.moveUD(textHoverAnimate, hover ? 4 : 2, 0.000542f);
        activeRectAnimate = AnimationUtil.moveUD(activeRectAnimate, getObject().isToggled() ? getWidth() : 0, 0.00542f);
        Color c = Color.getHSBColor(ClickGUI.guiHue.getValFloat(), 1.0F, 0.65F);
        if (ClickGUI.rainbowGui.getValBoolean()) {
            RenderUtil.INSTANCE.drawRect(getX(), getY(), getX() + activeRectAnimate, getY() + getHeight(), ColorUtil.getRainbow(17000, -15 * (int) getY()));
        } else
            RenderUtil.INSTANCE.drawRect(getX(), getY(), getX() + activeRectAnimate, getY() + getHeight(), c.getRGB());
        Summer.INSTANCE.fontManager.getFont("ROBO 17").drawStringWithShadow(isBinding() ? "Press a key..." : getObject().getName(), getX() + textHoverAnimate, getY() + 2, -1);
        if (Summer.INSTANCE.settingsManager.getSettingsByMod(getObject()) != null) {
            Summer.INSTANCE.fontManager.getFont("ROBO 17").drawStringWithShadow(isBinding() ? "" : "....", getX() - 5 + getWidth() - 9, getY() + 1, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
            if (mouseButton == 0) {
                getObject().toggle();
            }
            if (mouseButton == 1) {
                if (Summer.INSTANCE.settingsManager.getSettingsByMod(getObject()) != null) {
                    getParent().setSelectedCheat(this);
                    getParent().setSection(Section.SETTINGS);
                }
            }
            if (mouseButton == 2) {
                setBinding(!isBinding());
            }
        } else {
            //Maybe useful
            if (isBinding()) {
                setBinding(false);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (isBinding()) {
            if (keyCode == Keyboard.KEY_DELETE) {
                getObject().setKey(0);
                ChatUtils.sendMessage("Unbound " + getObject().getName() + ".");
            } else if (keyCode == Keyboard.KEY_ESCAPE) {
                setBinding(false);
            } else if (keyCode == Keyboard.KEY_W || keyCode == Keyboard.KEY_D || keyCode == Keyboard.KEY_A || keyCode == Keyboard.KEY_S) {
                setBinding(false);
            } else {
                getObject().setKey(keyCode);
                ChatUtils.sendMessage(EnumChatFormatting.GRAY + getObject().getName() + " has been bound to " + EnumChatFormatting.GRAY + Keyboard.getKeyName(getObject().getKey()) + EnumChatFormatting.GRAY + ".");
            }
            setBinding(false);
        }
        super.keyTyped(typedChar, keyCode);
    }

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }
}
