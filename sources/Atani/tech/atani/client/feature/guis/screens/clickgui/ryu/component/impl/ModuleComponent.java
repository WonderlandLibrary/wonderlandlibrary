package tech.atani.client.feature.guis.screens.clickgui.ryu.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.guis.screens.clickgui.ryu.component.Component;

public class ModuleComponent extends Component {

    private final Module module;
    private boolean expanded = false;

    public ModuleComponent(Module module, float posX, float posY, float width, float height) {
        super(posX, posY, width, height);
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer normal = FontStorage.getInstance().findFont("Roboto", 19);
        normal.drawTotalCenteredString(module.getName(), getPosX() + getBaseWidth() / 2 + getAddX(), getPosY() + getBaseHeight() / 2, !module.isEnabled() ? -1 : RYU);
                boolean reset = false;
        for(Value value : ValueStorage.getInstance().getValues(module)) {
            boolean found = false;
            for(Component component : this.subComponents) {
                if(component instanceof ValueComponent) {
                    ValueComponent valueComponent = (ValueComponent) component;
                    if(valueComponent.getValue() == value)
                        found = true;
                }
            }
            if(!found) {
                reset = true;
                break;
            }
        }
        for(Component component : this.subComponents) {
            if (component instanceof ValueComponent) {
                ValueComponent valueComponent = (ValueComponent) component;
                boolean found = false;
                for(Value value : ValueStorage.getInstance().getValues(module)) {
                    if(valueComponent.getValue() == value)
                        found = true;
                }
                if(!found) {
                    reset = true;
                    break;
                }
            }
        }
        if(reset)
            this.subComponents.clear();
        if(expanded && this.subComponents.isEmpty()) {
            for(Value value : ValueStorage.getInstance().getValues(module)) {
                float valueY = this.getPosY() + this.getBaseHeight();
                if(value instanceof CheckBoxValue) {
                    CheckBoxComponent component = new CheckBoxComponent(value, this.getPosX(), valueY, this.getBaseWidth(), this.getBaseHeight() + 2);
                    this.subComponents.add(component);
                    valueY += component.getFinalHeight();
                } else if(value instanceof StringBoxValue) {
                    StringBoxComponent component = new StringBoxComponent(value, this.getPosX(), valueY, this.getBaseWidth(), this.getBaseHeight() + 2);
                    this.subComponents.add(component);
                    valueY += component.getFinalHeight();
                } else if(value instanceof MultiStringBoxValue) {
                    MultiStringBoxComponent component = new MultiStringBoxComponent(value, this.getPosX(), valueY, this.getBaseWidth(), this.getBaseHeight() + 2);
                    this.subComponents.add(component);
                    valueY += component.getFinalHeight();
                } else if(value instanceof SliderValue) {
                    SliderComponent component = new SliderComponent(value, this.getPosX(), valueY, this.getBaseWidth(), this.getBaseHeight() + 2);
                    this.subComponents.add(component);
                    valueY += component.getFinalHeight();
                }
            }
        } else if(!this.expanded) {
            subComponents.clear();
        }
        if(expanded) {
            float valueY = this.getPosY() + this.getBaseHeight();
            for(Component component : this.subComponents) {
                if(component instanceof ValueComponent) {
                    ValueComponent valueComponent = (ValueComponent) component;
                    valueComponent.setAddX(this.getAddX());
                    valueComponent.setPosX(this.getPosX());
                    valueComponent.setPosY(valueY);
                    Value value = valueComponent.getValue();
                    valueComponent.setVisible(value.isVisible());
                    if(!valueComponent.isVisible())
                        continue;
                    valueComponent.drawScreen(mouseX, mouseY);
                    valueY += valueComponent.getFinalHeight();
                }
            }
        }
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getBaseWidth(), this.getBaseHeight())) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    break;
                case 1:
                    this.expanded = !this.expanded;
                    break;
            }
        }
        if(expanded) {
            float valueY = this.getPosY() + this.getBaseHeight();
            for(Component component : this.subComponents) {
                if(component instanceof ValueComponent) {
                    ValueComponent valueComponent = (ValueComponent) component;
                    valueComponent.setPosX(this.getPosX());
                    valueComponent.setPosY(valueY);
                    Value value = valueComponent.getValue();
                    valueComponent.setVisible(value.isVisible());
                    if(!valueComponent.isVisible())
                        continue;
                    valueComponent.mouseClick(mouseX, mouseY, mouseButton);
                    valueY += valueComponent.getFinalHeight();
                }
            }
        }
    }

    @Override
    public float getFinalHeight() {
        float totalComponentHeight = 0;
        if(this.expanded) {
            for(Component component : this.subComponents) {
                totalComponentHeight += component.isVisible() ? component.getFinalHeight() : 0;
            }
        }
        return this.getBaseHeight() + totalComponentHeight;
    }

    public Module getModule() {
        return module;
    }
}
