package vestige.ui.menu.components;

import lombok.Getter;
import lombok.Setter;
import vestige.util.misc.TimerUtil;

@Getter
public class Button {

    private String name;

    private boolean hovered;

    private TimerUtil animationTimer;
    private final long animInDuration = 200;
    private final long animOutDuration = 70;

    @Setter
    private boolean animationDone;

    public Button(String name) {
        this.name = name;
        this.hovered = false;
        this.animationTimer = new TimerUtil();
    }

    public void updateState(boolean state) {
        if(hovered != state) {
            animationTimer.reset();
            hovered = state;
            animationDone = false;
        }

        if(animationTimer.getTimeElapsed() >= (hovered ? animInDuration : animOutDuration)) {
            animationDone = true;
        }
    }

    public double getMult() {
        double time = animationTimer.getTimeElapsed();

        return Math.min((hovered ? time : animOutDuration - time) / (hovered ? animInDuration : animOutDuration), 1);
    }

}