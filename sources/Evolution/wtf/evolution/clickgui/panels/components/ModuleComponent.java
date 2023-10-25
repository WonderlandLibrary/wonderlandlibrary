package wtf.evolution.clickgui.panels.components;

import wtf.evolution.clickgui.panels.Panel;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Module;
import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleComponent extends Component{

    public Module module;
    public float x, y, width, height;

    public List<Component> components = new ArrayList<>();
    public float offset;
    public Panel p;

    public boolean opened = true;

    public ModuleComponent(Module module, Panel p, float x, float y, float width, float height) {
        super(module, x, y, width, height);
        this.p = p;
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        int yOffset = 16;
        for (Setting c : module.getSettings()) {
            if (c instanceof BooleanSetting) {
                components.add(new BooleanComponent(module, (BooleanSetting) c, x, y + yOffset, width, height));
                yOffset += 16;
            }
        }
        opened = true;
    }

    @Override
    public void render(int mouseX, int mouseY) {



        if (module.state)
            Fonts.RUB14.drawCenteredBlurredString(module.name, x + 125 / 2f, y + 6, 13, new Color(ClickGui.getColor()), module.state ? ClickGui.getColor() : -1);
        else
            Fonts.RUB14.drawCenteredString(module.name, x + 125 / 2f, y + 6,-1);
        components.forEach(component -> {
            if (opened) {
                component.render(mouseX, mouseY);
                height += 16;
            }
        });
        super.render(mouseX, mouseY);
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void click(int mouseX, int mouseY, int button) {
        super.click(mouseX, mouseY, button);
        if (button == 1)
            opened = !opened;
        else
            module.toggle();
    }
}
