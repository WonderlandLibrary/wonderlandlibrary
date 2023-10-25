package tech.drainwalk.client.option.options;

import lombok.Getter;
import tech.drainwalk.animation.Animation;

public class SelectOptionValue  {
    @Getter
    private final Animation animation = new Animation();
    @Getter
    private final Animation hoveredAnimation = new Animation();

    @Getter
    private final String name;

    public SelectOptionValue(final String name) {
        this.name = name;
    }
}