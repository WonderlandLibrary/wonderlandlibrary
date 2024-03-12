// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.forms.impl;

import dev.tenacity.Tenacity;
import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import dev.tenacity.ui.sidegui.forms.Form;

public class SaveForm extends Form
{
    private final ActionButton save;
    private final TextField nameField;
    
    public SaveForm() {
        super("Save Config");
        this.save = new ActionButton("Save");
        this.nameField = new TextField(SaveForm.tenacityFont20);
        this.setWidth(300.0f);
        this.setHeight(120.0f);
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.nameField.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        final float infoX = this.getX() + SaveForm.tenacityBoldFont40.getStringWidth(this.getTitle()) + 20.0f;
        final float infoY = this.getY() + 3.0f + SaveForm.tenacityFont16.getMiddleOfBox((float)SaveForm.tenacityBoldFont40.getHeight()) + 2.0f;
        SaveForm.tenacityFont16.drawString("Enter a name for the local config", infoX, infoY, ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        final float insideWidth = this.getWidth() - this.getSpacing() * 2.0f;
        final float insideX = this.getX() + this.getSpacing();
        final float insideY = this.getY() + 30.0f;
        RoundedUtil.drawRound(insideX, insideY, this.getWidth() - this.getSpacing() * 2.0f, this.getHeight() - (30.0f + this.getSpacing()), 5.0f, ColorUtil.tripleColor(29, this.getAlpha()));
        final Color noColor = ColorUtil.applyOpacity(Color.WHITE, 0.0f);
        final Color darkColor = ColorUtil.tripleColor(17, this.getAlpha());
        this.nameField.setBackgroundText("Type here...");
        this.nameField.setXPosition(insideX + this.getSpacing());
        this.nameField.setYPosition(insideY + 25.0f);
        this.nameField.setWidth(insideWidth - this.getSpacing() * 2.0f);
        this.nameField.setHeight(20.0f);
        this.nameField.setFont(SaveForm.tenacityFont18);
        this.nameField.setOutline(noColor);
        this.nameField.setFill(darkColor);
        this.nameField.setTextAlpha(this.getAlpha());
        final int maxStringLength = (SaveForm.tenacityBoldFont26.getStringWidth(this.nameField.getText()) >= 143.0f) ? this.nameField.getText().length() : 30;
        this.nameField.setMaxStringLength(maxStringLength);
        this.nameField.drawTextBox();
        SaveForm.tenacityFont24.drawString("Config name", this.nameField.getXPosition(), this.nameField.getYPosition() - (SaveForm.tenacityFont24.getHeight() + 5), this.getTextColor());
        this.save.setWidth(70.0f);
        this.save.setHeight(15.0f);
        this.save.setX(this.getX() + this.getWidth() / 2.0f - this.save.getWidth() / 2.0f);
        this.save.setY(this.getY() + this.getHeight() - (this.save.getHeight() + this.getSpacing() * 2.0f));
        this.save.setAlpha(this.getAlpha());
        this.save.setBypass(true);
        this.save.setBold(true);
        this.save.setClickAction(() -> {
            this.getUploadAction().accept(this.nameField.getText(), null);
            Tenacity.INSTANCE.getSideGui().displayForm(null);
            return;
        });
        this.save.drawScreen(mouseX, mouseY);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.nameField.mouseClicked(mouseX, mouseY, button);
        this.save.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void clear() {
        this.nameField.setText("");
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
