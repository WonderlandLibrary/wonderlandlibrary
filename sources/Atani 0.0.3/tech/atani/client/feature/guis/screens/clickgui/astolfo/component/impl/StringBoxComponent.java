package tech.atani.client.feature.guis.screens.clickgui.astolfo.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.font.storage.FontStorage;

public class StringBoxComponent extends ValueComponent {

    private boolean expanded = false;
    private StringBoxValue stringBoxValue;

    public StringBoxComponent(Value value, float posX, float posY, float baseWidth, float baseHeight) {
        super(value, posX, posY, baseWidth, baseHeight);
        this.stringBoxValue = (StringBoxValue) value;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer normal = FontStorage.getInstance().findFont("SFUI Medium", 16);
        normal.drawString(value.getName(), getPosX() + 5 + getAddX(), getPosY() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
        normal.drawString(value.getValue().toString(), getPosX() + this.getBaseWidth() - 5 - normal.getStringWidthInt(value.getValue().toString()) + getAddX(), getPosY() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2,-1);
        if(expanded) {
            float y = this.getPosY() + this.getBaseHeight();
            for(String string : stringBoxValue.getValues()) {
                normal.drawString(" - " + string, getPosX() + 5 + getAddX(), y + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
                y += this.getBaseHeight();
            }
        }
    }

    @Override
    public float getFinalHeight() {
        return this.expanded ? this.getBaseHeight() + this.getBaseHeight() * stringBoxValue.getValues().length : this.getBaseHeight();
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX() + getAddX(), this.getPosY(), this.getBaseWidth(), this.getBaseHeight())) {
            expanded = !expanded;
        }
        if(expanded) {
            float y = this.getPosY() + this.getBaseHeight();
            for(String string : stringBoxValue.getValues()) {
                if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX() + getAddX(), y, this.getBaseWidth(), this.getBaseHeight())) {
                    value.setValue(string);
                }
                y += this.getBaseHeight();
            }
        }
    }
}
