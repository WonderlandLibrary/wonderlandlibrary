package tech.drainwalk.gui.minecraft.altmanager;

import lombok.Getter;
import tech.drainwalk.animation.Animation;

public class Alt {
    @Getter
    private final String name;
    @Getter
    private final Animation selectAnimation = new Animation();
    @Getter
    private final Animation hoverAnimation = new Animation();
    public boolean canRemove = false;

    public Alt(String name) {
        this.name = name;
    }
}
