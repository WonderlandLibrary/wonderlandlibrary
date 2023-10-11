package io.github.raze.screen.collection.menu.components.impl;

import io.github.raze.Raze;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.screen.collection.menu.components.api.BaseComponent;
import io.github.raze.screen.collection.menu.components.impl.settings.ArrayComponent;
import io.github.raze.screen.collection.menu.components.impl.settings.BooleanComponent;
import io.github.raze.screen.collection.menu.components.impl.settings.NumberComponent;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.settings.system.BaseSetting;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.visual.MouseUtil;
import io.github.raze.utilities.collection.visual.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingComponent extends BaseComponent {

    public AbstractModule module;
    public List<BaseComponent> settings;

    private boolean first = true;

    public SettingComponent(AbstractModule module, double x, double y, double width, double height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.settings = new ArrayList<>();

        update();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isOpen()) {
            return;
        }

        switch (MouseUtil.getDirection()) {
            case UP: {
                setY(getY() + 5);
                settings.forEach(setting -> setting.setY(setting.getY() + 5));
                break;
            }
            case DOWN: {
                setY(getY() - 5);
                settings.forEach(setting -> setting.setY(setting.getY() - 5));
                break;
            }
            default: {
                break;
            }
        }

        RenderUtil.rectangle(getX(), getY(), getWidth(), getHeight(), Raze.INSTANCE.managerRegistry.themeRegistry.getCurrentTheme().getBackground());
        RenderUtil.rectangle(getX(), getY(), getWidth(), 25, Raze.INSTANCE.managerRegistry.themeRegistry.getCurrentTheme().getDarkBackground());

        CFontUtil.Jello_Light_24.getRenderer().drawString(
                module.getName(),
                getX() + 12.5 - CFontUtil.Jello_Medium_24.getRenderer().getHeight() / 2.0D,
                getY() + 12.5 - CFontUtil.Jello_Medium_24.getRenderer().getHeight() / 2.0D,
                Raze.INSTANCE.managerRegistry.themeRegistry.getCurrentTheme().getDarkForeground()
        );

        settings.forEach(setting -> setting.render(mouseX, mouseY, partialTicks));

        super.render(mouseX, mouseY, partialTicks);
    }



    @Override
    public void click(int mouseX, int mouseY, int button) {
        if (isOpen()) {
            return;
        }

        update();

        settings.forEach(setting -> setting.click(mouseX, mouseY, button));

        super.click(mouseX, mouseY, button);
    }


    @Override
    public void release(int mouseX, int mouseY, int button) {
        if (isOpen()) {
            return;
        }

        update();

        settings.forEach(setting -> setting.release(mouseX, mouseY, button));

        super.release(mouseX, mouseY, button);
    }


    @Override
    public void type(char typed, int keyCode) {
        if (isOpen()) {
            return;
        }

        settings.forEach(setting -> setting.type(typed, keyCode));

        super.type(typed, keyCode);
    }

    private void update() {
        settings.clear();

        List<BaseSetting> list = Raze.INSTANCE.managerRegistry.settingRegistry.getSettingsByModule(module)

                .stream()

                .filter(setting -> !setting.isHidden())

                .collect(Collectors.toList());

        ScaledResolution scale = new ScaledResolution(mc);

        setHeight(list.size() * 20 + 25);

        if (first) {
            setY(scale.getScaledHeight() / 2.0 - getHeight() / 2.0);
            first = false;
        }

        for (BaseSetting setting : list) {
            int index = list.indexOf(setting);

            if (setting instanceof ArraySetting) {
                ArraySetting arraySetting = (ArraySetting) setting;

                settings.add(new ArrayComponent(arraySetting, getX(), getY() + index * 20 + 25, getWidth(), 20));
            }

            if (setting instanceof BooleanSetting) {
                BooleanSetting booleanSetting = (BooleanSetting) setting;

                settings.add(new BooleanComponent(booleanSetting, getX(), getY() + index * 20 + 25, getWidth(), 20));
            }

            if (setting instanceof NumberSetting) {
                NumberSetting numberSetting = (NumberSetting) setting;

                settings.add(new NumberComponent(numberSetting, getX(), getY() + index * 20 + 25, getWidth(), 20));
            }
        }
    }
}
