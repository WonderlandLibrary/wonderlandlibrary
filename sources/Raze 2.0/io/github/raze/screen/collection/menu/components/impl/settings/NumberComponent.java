package io.github.raze.screen.collection.menu.components.impl.settings;

import io.github.raze.Raze;
import io.github.raze.screen.collection.menu.components.api.BaseComponent;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.math.MathUtil;
import io.github.raze.utilities.collection.visual.MouseUtil;
import io.github.raze.utilities.collection.visual.RenderUtil;

public class NumberComponent extends BaseComponent {

    public NumberSetting setting;

    public NumberComponent(NumberSetting setting, double x, double y, double width, double height) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        double minimum = setting.min().doubleValue();
        double maximum = setting.max().doubleValue();

        double value = setting.get().doubleValue();

        double difference = (maximum - minimum);
        double percentage = (value   - minimum) / difference;

        if (isDragging()) {
            double update = minimum + (MathUtil.clamp((mouseX - getX()) / getWidth(), 0, 1)) * difference;
            setting.change(Math.round(update * 100D) / 100D);
        }

        double renderWidth = getWidth() * percentage;

        RenderUtil.rectangle(
                getX(),
                getY(),
                renderWidth,
                getHeight(),
                Raze.INSTANCE.managerRegistry.themeRegistry.getCurrentTheme().getDarkBackground()
        );

        CFontUtil.Jello_Light_16.getRenderer().drawString(
                setting.getName() + ": " + setting.get(),
                getX() + getHeight() / 2 - CFontUtil.Jello_Light_16.getRenderer().getHeight() / 2.0D,
                getY() + getHeight() / 2 - CFontUtil.Jello_Light_16.getRenderer().getHeight() / 2.0D,
                Raze.INSTANCE.managerRegistry.themeRegistry.getCurrentTheme().getForeground()
        );

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {

        if (MouseUtil.isInside(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            setDragging(true);
        }

        super.click(mouseX, mouseY, mouseButton);
    }

    @Override
    public void release(int mouseX, int mouseY, int button) {

        setDragging(false);

        super.release(mouseX, mouseY, button);
    }
}
