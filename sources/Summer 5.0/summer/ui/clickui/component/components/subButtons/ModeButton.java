package summer.ui.clickui.component.components.subButtons;

import summer.Summer;
import summer.cheat.guiutil.Setting;
import summer.ui.clickui.component.Parentable;
import summer.ui.clickui.panel.panels.CheatTypePanel;
import summer.ui.clickuiutils.AnimationUtil;

/**
 * @author: AmirCC
 * 12:21 am, 11/10/2020, Wednesday
 **/
public class ModeButton extends Parentable<Setting, CheatTypePanel> {

    private int modeCounter;

    //animation values
    private float textHoverAnimate = 0f;
    private float movingTextAnimte = 0f;

    public ModeButton(Setting object, CheatTypePanel parent, float staticX, float staticY, float width, float height, float offsetX, float offsetY) {
        super(object, parent, staticX, staticY, width, height, offsetX, offsetY);
        this.modeCounter = 0;
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

        Summer.INSTANCE.fontManager.getFont("TAHOMA 16").drawStringWithShadow(getObject().getName(), getX() + textHoverAnimate, getY() + 1, -1);
        Summer.INSTANCE.fontManager.getFont("TAHOMA 16").drawStringWithShadow(getObject().getValString(), getX() - 51 + getWidth() - movingTextAnimte, getY() + 1, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int modesSize = getObject().getOptions().size();
        if (isHovered(getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
            if (mouseButton == 0) {
                if(modeCounter + 1 > modesSize - 1){
                    modeCounter = 0;
                } else {
                    modeCounter++;
                }
                getObject().setValString(getObject().getOptions().get(modeCounter));
            } else if (mouseButton == 1){
                if(modeCounter == 0){
                    modeCounter = modesSize - 1;
                } else {
                    modeCounter--;
                }
                getObject().setValString(getObject().getOptions().get(modeCounter));
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isVisible() {
        return getObject().isVisible();
    }

}
