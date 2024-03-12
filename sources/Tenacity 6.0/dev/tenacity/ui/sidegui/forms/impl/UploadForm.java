// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.forms.impl;

import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.Tenacity;
import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import dev.tenacity.ui.sidegui.forms.Form;

public class UploadForm extends Form
{
    private final ActionButton uploadButton;
    private final String type;
    private final TextField nameField;
    private final TextField descriptionField;
    private final TextField serverField;
    private boolean error;
    
    public UploadForm(final String type) {
        super("Upload " + type);
        this.uploadButton = new ActionButton("Upload");
        this.nameField = new TextField(UploadForm.tenacityFont18);
        this.descriptionField = new TextField(UploadForm.tenacityFont18);
        this.serverField = new TextField(UploadForm.tenacityFont18);
        this.error = false;
        this.setWidth(375.0f);
        this.setHeight(175.0f);
        this.type = type;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.nameField.keyTyped(typedChar, keyCode);
        this.descriptionField.keyTyped(typedChar, keyCode);
        if (this.type.equals("Config")) {
            this.serverField.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        final float infoX = this.getX() + UploadForm.tenacityBoldFont40.getStringWidth(this.getTitle()) + 20.0f;
        float infoY = this.getY() + 7.5f;
        UploadForm.tenacityFont16.drawString("To upload your " + this.type.toLowerCase() + " you must provide some information", infoX, infoY, ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        infoY += UploadForm.tenacityFont16.getHeight() + 3;
        UploadForm.tenacityFont16.drawString("All fields are §lrequired", infoX, infoY, ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        final float insideWidth = this.getWidth() - this.getSpacing() * 2.0f;
        RoundedUtil.drawRound(this.getX() + this.getSpacing(), this.getY() + 40.0f, this.getWidth() - this.getSpacing() * 2.0f, this.getHeight() - (40.0f + this.getSpacing()), 5.0f, ColorUtil.tripleColor(29, this.getAlpha()));
        final float insideX = this.getX() + this.getSpacing();
        final float insideY = this.getY() + 40.0f;
        final Color noColor = ColorUtil.applyOpacity(Color.WHITE, 0.0f);
        final Color darkColor = ColorUtil.tripleColor(17, this.getAlpha());
        this.nameField.setBackgroundText("Type here...");
        this.nameField.setXPosition(insideX + this.getSpacing());
        this.nameField.setYPosition(insideY + 25.0f);
        this.nameField.setWidth(150.0f);
        this.nameField.setHeight(20.0f);
        this.nameField.setOutline(noColor);
        this.nameField.setFill(darkColor);
        this.nameField.setTextAlpha(this.getAlpha());
        final int maxStringLength = (UploadForm.tenacityBoldFont26.getStringWidth(this.nameField.getText()) >= 143.0f) ? this.nameField.getText().length() : 30;
        this.nameField.setMaxStringLength(maxStringLength);
        this.nameField.drawTextBox();
        UploadForm.tenacityFont24.drawString(this.type + " name", this.nameField.getXPosition(), this.nameField.getYPosition() - (UploadForm.tenacityFont24.getHeight() + 5), this.getTextColor());
        if (this.type.equals("Config")) {
            this.serverField.setBackgroundText("Type here...");
            this.serverField.setYPosition(this.nameField.getYPosition());
            this.serverField.setWidth(150.0f);
            this.serverField.setXPosition(insideX + insideWidth - (this.serverField.getWidth() + this.getSpacing() * 2.0f));
            this.serverField.setHeight(20.0f);
            this.serverField.setOutline(noColor);
            this.serverField.setFill(darkColor);
            this.serverField.setTextAlpha(this.getAlpha());
            this.serverField.drawTextBox();
            UploadForm.tenacityFont24.drawString("Server IP", this.serverField.getXPosition(), this.serverField.getYPosition() - (UploadForm.tenacityFont24.getHeight() + 5), this.getTextColor());
            UploadForm.tenacityFont14.drawCenteredString("Input the IP of the server the config was made for", this.serverField.getXPosition() + this.serverField.getWidth() / 2.0f + 2.0f, this.serverField.getYPosition() + this.serverField.getHeight() + 5.0f, this.getTextColor());
        }
        this.descriptionField.setBackgroundText("Type here...");
        this.descriptionField.setXPosition(insideX + this.getSpacing());
        this.descriptionField.setYPosition(this.nameField.getYPosition() + this.nameField.getHeight() + 25.0f);
        this.descriptionField.setWidth(insideWidth - this.getSpacing() * 2.0f);
        this.descriptionField.setHeight(20.0f);
        this.descriptionField.setOutline(noColor);
        this.descriptionField.setFill(darkColor);
        this.descriptionField.setTextAlpha(this.getAlpha());
        this.descriptionField.setMaxStringLength(210);
        this.descriptionField.drawTextBox();
        UploadForm.tenacityFont24.drawString("Description", this.descriptionField.getXPosition(), this.descriptionField.getYPosition() - (UploadForm.tenacityFont24.getHeight() + 5), this.getTextColor());
        this.uploadButton.setWidth(70.0f);
        this.uploadButton.setHeight(15.0f);
        this.uploadButton.setX(this.getX() + this.getWidth() / 2.0f - this.uploadButton.getWidth() / 2.0f);
        this.uploadButton.setY(this.getY() + this.getHeight() - (this.uploadButton.getHeight() + this.getSpacing() * 2.0f));
        this.uploadButton.setAlpha(this.getAlpha());
        this.uploadButton.setBypass(true);
        this.uploadButton.setBold(true);
        this.uploadButton.setClickAction(() -> {
            if (this.type.equals("Config")) {
                this.getTriUploadAction().accept(this.nameField.getText(), this.descriptionField.getText(), this.serverField.getText());
            }
            else {
                this.getUploadAction().accept(this.nameField.getText(), this.descriptionField.getText());
            }
            Tenacity.INSTANCE.getSideGui().displayForm(null);
            return;
        });
        this.uploadButton.drawScreen(mouseX, mouseY);
        if (this.error) {
            UploadForm.tenacityFont16.drawCenteredStringWithShadow("Error please fill out the required fields", this.uploadButton.getX() + this.uploadButton.getWidth() / 2.0f, this.uploadButton.getY() - (UploadForm.tenacityFont16.getHeight() + 5), Tenacity.INSTANCE.getSideGui().getRedBadColor().getRGB());
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.nameField.mouseClicked(mouseX, mouseY, button);
        this.descriptionField.mouseClicked(mouseX, mouseY, button);
        if (this.type.equals("Config")) {
            this.serverField.mouseClicked(mouseX, mouseY, button);
        }
        if (HoveringUtil.isHovering(this.uploadButton.getX(), this.uploadButton.getY(), this.uploadButton.getWidth(), this.uploadButton.getHeight(), mouseX, mouseY)) {
            final String descriptionText = this.descriptionField.getText();
            final String[] descArray = descriptionText.split(" ");
            final boolean descriptionFilter = descriptionText.length() > 35 && descArray.length <= 1;
            if ((this.type.equals("Config") && this.serverField.getText().isEmpty()) || this.nameField.getText().isEmpty() || this.descriptionField.getText().isEmpty() || descriptionFilter) {
                this.error = true;
            }
            else {
                this.error = false;
                this.uploadButton.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
    
    @Override
    public void clear() {
        this.error = false;
        this.nameField.setText("");
        this.descriptionField.setText("");
        this.serverField.setText("");
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
