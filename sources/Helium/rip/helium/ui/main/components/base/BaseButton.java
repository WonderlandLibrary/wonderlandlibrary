package rip.helium.ui.main.components.base;

import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.Component;

/**
 * @author antja03
 */
public class BaseButton extends Component {

    /*
     * The action that will be performed when the button is clicked
     */
    private final Action action;

    /*
     * The bounds where the actual button is located
     * Based off of the position X/Y and maximum width/height
     */
    private double
            boundsLeft,
            boundsTop,
            boundsRight,
            boundsBottom;

    public BaseButton(Interface theInterface, double positionX, double positionY, double maxWidth, double maxHeight, Action action) {
        super(theInterface, positionX, positionY, maxWidth, maxHeight);
        this.action = action;
        this.boundsLeft = 0;
        this.boundsTop = 0;
        this.boundsRight = maxWidth;
        this.boundsBottom = maxHeight;
    }

    public void drawComponent(double x, double y) {
        updatePosition(x, y);
    }

    @Override
    public void updatePosition(double lastDrawPosX, double lastDrawPosY) {
        super.updatePosition(lastDrawPosX, lastDrawPosY);
    }

    public boolean mouseButtonClicked(int button) {
        if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + boundsLeft,
                theInterface.getPositionX() + positionX + boundsRight,
                theInterface.getPositionY() + positionY + boundsTop,
                theInterface.getPositionY() + positionY + boundsBottom)) {
            action.perform(button);
            return true;
        }
        return false;
    }

    protected boolean isMouseOver() {
        return theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + boundsLeft,
                theInterface.getPositionX() + positionX + boundsRight,
                theInterface.getPositionY() + positionY + boundsTop,
                theInterface.getPositionY() + positionY + boundsBottom);
    }

    public double getBoundsLeft() {
        return boundsLeft;
    }

    public void setBoundsLeft(double boundsLeft) {
        this.boundsLeft = boundsLeft;
    }

    public double getBoundsTop() {
        return boundsTop;
    }

    public void setBoundsTop(double boundsTop) {
        this.boundsTop = boundsTop;
    }

    public double getBoundsRight() {
        return boundsRight;
    }

    public void setBoundsRight(double boundsRight) {
        this.boundsRight = boundsRight;
    }

    public double getBoundsBottom() {
        return boundsBottom;
    }

    public void setBoundsBottom(double boundsBottom) {
        this.boundsBottom = boundsBottom;
    }

    public Action getAction() {
        return action;
    }

    public interface Action {
        void perform(int button);
    }

}
