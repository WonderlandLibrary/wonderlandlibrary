package ru.alone.ui.element.imp.panel;


import net.minecraft.client.gui.GuiTextField;
import ru.alone.ui.element.Element;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;

import java.awt.*;
import java.lang.reflect.Field;

public class ElementSearch extends Element  {

    public ElementHeader header;
    public GuiTextField field;

    public ElementSearch(ElementHeader header) {
        this.header = header;
        this.setWidth(180);
        this.setHeight(15);
        this.field = new GuiTextField(0, mc.fontRenderer, (int)this.x, (int)this.y, (int)getWidth(), (int)this.height);
        this.field.setEnableBackgroundDrawing(true);
        this.field.setMaxStringLength(32);
    }

    public void updateScreen(){
        field.updateCursorCounter();
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        field.x = (int) this.x;
        field.y = (int) this.y;
        RenderUtils.drawRoundedRect(field.x, field.y, field.x + field.width, field.y + field.height,1.5f,new Color(0x1C1C1C).getRGB());
        int fieldColor = new Color(40, 40, 40, 255).getRGB();
        if(field.getText().length() > 0) {
            Fonts.Monstserrat.drawString(field.getText(), field.x + 5, field.y + 5, fieldColor);
        }else Fonts.Monstserrat.drawString("Search for a module...", field.x + 5, field.y + 5, fieldColor);
        boolean flag1 = field.isFocused() && count() / 6 % 2 == 0;

        if(flag1) {
            Fonts.Monstserrat.drawString("_", field.x + 5 + Fonts.Monstserrat.getStringWidth(field.getText()) + 1, field.y + 5, fieldColor);
        }

    }

    public int count(){
        try {
            Field f = GuiTextField.class.getDeclaredFields()[8];
            f.setAccessible(true);
            return f.getInt(field);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        try {
            this.field.mouseClicked(x, y, 1);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void keypressed(char c, int key) {
        field.textboxKeyTyped(c, key);
        if(field.isFocused()){
            ElementFlow.searchModules.clear();
        }
        super.keypressed(c, key);
    }
}
