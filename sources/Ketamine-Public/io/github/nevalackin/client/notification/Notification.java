package io.github.nevalackin.client.notification;

import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;

import static org.lwjgl.opengl.GL11.glTranslated;

public final class Notification {

    private final CustomFontRenderer fontRenderer;

    private final String title;
    private final String body;
    private final long duration;
    private final long timeOfCreation;
    private final NotificationType type;

    private final double width, height;

    public Notification(CustomFontRenderer fontRenderer, String title, String body, long duration, NotificationType type) {
        this.fontRenderer = fontRenderer;

        this.title = title;
        this.body = body;
        this.duration = duration;
        this.timeOfCreation = System.currentTimeMillis();
        this.type = type;

        this.width = Math.max(fontRenderer.getWidth(title), fontRenderer.getWidth(body)) + 16 + 2 * 3;
        this.height = 10 * 2 + 4;
    }

    public void render() {
        final long timeExisted = System.currentTimeMillis() - this.timeOfCreation;
        final double progress = DrawUtil.bezierBlendAnimation((double) timeExisted / this.duration);

        final long fadeInOutDuration = 100L;

        final double animationTranslate =
            timeExisted < fadeInOutDuration ? // if notification is young
                DrawUtil.bezierBlendAnimation((double) timeExisted / fadeInOutDuration) * this.width : // do fade in
                timeExisted > this.duration - fadeInOutDuration ? // if notification is old
                    DrawUtil.bezierBlendAnimation(((double) this.duration - timeExisted) / fadeInOutDuration) * this.width :  // do fade out
                    this.width; // else translate by width

        glTranslated(-animationTranslate, 0, 0);

        DrawUtil.glDrawFilledQuad(0, 0, this.width, this.height, 0x80 << 24);

        final int colour = this.type.getColour();

        DrawUtil.glDrawFilledQuad(0, this.height - 2, this.width, 2, ColourUtil.darker(colour));
        DrawUtil.glDrawFilledQuad(0, this.height - 2, this.width * progress, 2, colour);

        this.type.getImage().draw(2, (this.height - 16) / 2.0, 16, 16, colour);

        final double availableHeight = this.height - 2;

        this.fontRenderer.draw(this.title, 2 + 16 + 2, availableHeight / 2.0 - this.fontRenderer.getHeight(this.title), 0xFFFFFFFF);
        this.fontRenderer.draw(this.body, 2 + 16 + 2, availableHeight / 2.0, 0xFFAAAAAA);

        glTranslated(animationTranslate, 0, 0);
    }

    public boolean isDead() {
        return System.currentTimeMillis() > this.timeOfCreation + this.duration;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
