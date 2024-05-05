package tech.drainwalk.client.option.options;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.animation.Animation;

public class MultiOptionValue {
    @Getter
    private final Animation animation = new Animation();
    @Getter
    private final Animation hoveredAnimation = new Animation();
    @Getter
    private final String name;
    @Setter
    @Getter
    private boolean toggle;

    public MultiOptionValue(final String name, final boolean toggle) {
        this.name = name;
        this.toggle = toggle;
    }
}