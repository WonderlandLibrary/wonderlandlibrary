package graphite.impl.notifications.dev;

import graphite.api.util.render.animate.Opacity;
import graphite.api.util.render.animate.Translate;

public class DevNotification implements IDevNotification {
    private final String text;
    private final long start;

    public float targetOpacity;
    public Translate translate;
    public Opacity opacity;

    public DevNotification(String text) {
        this.text = text;
        this.start = System.currentTimeMillis();
        this.translate = new Translate(60.0F, 0.0F);
        this.opacity = new Opacity(100);
        this.targetOpacity = 255;
    }

    public long checkTime() {
        return System.currentTimeMillis() - this.getDisplayTime();
    }

    @Override
    public String getMessage() {
        return this.text;
    }

    @Override
    public long getInitializeTime() {
        return this.start;
    }

    @Override
    public long getDisplayTime() {
        return 1000;
    }
}