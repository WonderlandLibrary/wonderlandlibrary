// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.forms.impl;

import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.intent.cloud.CloudUtils;
import dev.tenacity.Tenacity;
import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.ui.sidegui.utils.DropdownObject;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import dev.tenacity.intent.cloud.data.CloudData;
import dev.tenacity.ui.sidegui.forms.Form;

public class EditForm extends Form
{
    private boolean script;
    private CloudData data;
    private final ActionButton updateButton;
    private final ActionButton deleteButton;
    private final DropdownObject scriptFiles;
    private final TextField descriptionField;
    private final String type;
    private boolean error;
    
    public EditForm(final String type) {
        super("Edit " + type);
        this.updateButton = new ActionButton("Update");
        this.deleteButton = new ActionButton("Delete");
        this.scriptFiles = new DropdownObject("File");
        this.descriptionField = new TextField(EditForm.tenacityFont18);
        this.error = false;
        this.type = type;
        this.setWidth(404.0f);
        this.setHeight(175.0f);
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.descriptionField.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        final float infoX = this.getX() + EditForm.tenacityBoldFont40.getStringWidth(this.getTitle()) + 15.0f;
        final float infoY = this.getY() + 3.0f + EditForm.tenacityFont16.getMiddleOfBox((float)EditForm.tenacityBoldFont40.getHeight()) + 2.0f;
        EditForm.tenacityFont16.drawString("Choose to either update or delete your " + this.type.toLowerCase(), infoX, infoY, ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        EditForm.tenacityBoldFont16.drawString(this.data.getName(), this.getX() + 5.0f, this.getY() + 5.5f + EditForm.tenacityBoldFont40.getHeight(), this.getAccentColor());
        final float totalSpacing = this.getSpacing() * 3.0f;
        final float updateWidth = (this.getWidth() - totalSpacing) * 0.7f;
        final float deleteWidth = (this.getWidth() - totalSpacing) * 0.3f;
        final float updateX = this.getX() + this.getSpacing();
        final float updateY = this.getY() + 40.0f;
        final float updateHeight = this.getHeight() - (40.0f + this.getSpacing());
        RoundedUtil.drawRound(this.getX() + this.getSpacing(), this.getY() + 40.0f, updateWidth, updateHeight, 5.0f, ColorUtil.tripleColor(29, this.getAlpha()));
        EditForm.tenacityBoldFont26.drawString("Update", updateX + 5.0f, updateY + 3.0f, this.getTextColor());
        final Color noColor = ColorUtil.applyOpacity(Color.WHITE, 0.0f);
        this.descriptionField.setWidth(updateWidth - 10.0f);
        this.descriptionField.setXPosition(updateX + 5.0f);
        this.descriptionField.setYPosition(updateY + (this.script ? 65 : 45));
        this.descriptionField.setTextAlpha(this.getAlpha());
        this.descriptionField.setMaxStringLength(210);
        this.descriptionField.setHeight(this.script ? 18.0f : 20.0f);
        this.descriptionField.setFont(EditForm.tenacityFont16);
        this.descriptionField.setFill(ColorUtil.tripleColor(17, this.getAlpha()));
        this.descriptionField.setOutline(noColor);
        this.descriptionField.drawTextBox();
        EditForm.tenacityFont18.drawString("Description", this.descriptionField.getXPosition(), this.descriptionField.getYPosition() - (EditForm.tenacityFont18.getHeight() + 5), this.getTextColor());
        this.updateButton.setBypass(true);
        this.updateButton.setWidth(70.0f);
        this.updateButton.setHeight(15.0f);
        this.updateButton.setBold(true);
        this.updateButton.setX(updateX + updateWidth / 2.0f - this.updateButton.getWidth() / 2.0f);
        this.updateButton.setY(updateY + updateHeight - (this.updateButton.getHeight() + this.getSpacing()));
        this.updateButton.setAlpha(this.getAlpha());
        this.updateButton.setClickAction(() -> {
            this.getUploadAction().accept(this.scriptFiles.getSelection(), this.descriptionField.getText());
            Tenacity.INSTANCE.getSideGui().displayForm(null);
            return;
        });
        this.updateButton.drawScreen(mouseX, mouseY);
        if (this.error) {
            EditForm.tenacityFont16.drawCenteredStringWithShadow("Error please fill out the required fields", this.updateButton.getX() + this.updateButton.getWidth() / 2.0f, this.updateButton.getY() - (EditForm.tenacityFont16.getHeight() + 5), Tenacity.INSTANCE.getSideGui().getRedBadColor().getRGB());
        }
        if (this.script) {
            EditForm.tenacityFont16.drawString("Update the script with one of your local scripts", updateX + 10.0f + EditForm.tenacityBoldFont26.getStringWidth("Update"), updateY + 4.0f + EditForm.tenacityFont16.getMiddleOfBox((float)EditForm.tenacityBoldFont26.getHeight()), ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
            this.scriptFiles.setWidth(160.0f);
            this.scriptFiles.setHeight(18.0f);
            this.scriptFiles.setY(updateY + 25.0f);
            this.scriptFiles.setX(updateX + updateWidth / 2.0f - this.scriptFiles.getWidth() / 2.0f);
            this.scriptFiles.setAlpha(this.getAlpha());
            this.scriptFiles.setBypass(true);
            this.scriptFiles.setAccentColor(this.getAccentColor());
            this.scriptFiles.drawScreen(mouseX, mouseY);
        }
        else {
            EditForm.tenacityFont16.drawString("Update the config with your current settings", updateX + 10.0f + EditForm.tenacityBoldFont26.getStringWidth("Update"), updateY + 4.0f + EditForm.tenacityFont16.getMiddleOfBox((float)EditForm.tenacityBoldFont26.getHeight()), ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        }
        final float deleteX = this.getX() + this.getSpacing() * 2.0f + updateWidth;
        final float deleteY = this.getY() + 40.0f;
        final float deleteHeight = this.getHeight() - (40.0f + this.getSpacing());
        RoundedUtil.drawRound(deleteX, deleteY, deleteWidth, deleteHeight, 5.0f, new Color(251, 14, 14, (int)(255.0f * (0.22f * this.getAlpha()))));
        EditForm.tenacityBoldFont26.drawString("Delete", deleteX + 5.0f, deleteY + 3.0f, this.getTextColor());
        EditForm.iconFont26.drawString("q", deleteX + 10.0f + EditForm.tenacityBoldFont26.getStringWidth("Delete"), deleteY + 4.5f, this.getTextColor());
        EditForm.tenacityFont16.drawString("Delete the " + this.type.toLowerCase() + " from the", deleteX + 5.0f, deleteY + 22.0f, ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        EditForm.tenacityFont16.drawString("cloud", deleteX + 5.0f, deleteY + 22.0f + EditForm.tenacityFont16.getHeight() + 3.0f, ColorUtil.applyOpacity(this.getTextColor(), 0.5f));
        this.deleteButton.setWidth(70.0f);
        this.deleteButton.setX(deleteX + deleteWidth / 2.0f - this.deleteButton.getWidth() / 2.0f);
        this.deleteButton.setHeight(15.0f);
        this.deleteButton.setY(deleteY + deleteHeight - (this.deleteButton.getHeight() + this.getSpacing()));
        this.deleteButton.setAlpha(this.getAlpha());
        this.deleteButton.setBypass(true);
        this.deleteButton.setBold(true);
        this.deleteButton.setColor(Tenacity.INSTANCE.getSideGui().getRedBadColor());
        this.deleteButton.setClickAction(() -> Multithreading.runAsync(() -> {
            if (CloudUtils.deleteData(this.data.getShareCode())) {
                NotificationManager.post(NotificationType.SUCCESS, "Success", "Deleted " + this.type + " from the cloud");
            }
            else {
                NotificationManager.post(NotificationType.DISABLE, "Error", "Failed to delete " + this.type + " from the cloud");
            }
            Tenacity.INSTANCE.getSideGui().displayForm(null);
            Tenacity.INSTANCE.getCloudDataManager().refreshData();
        }));
        this.deleteButton.drawScreen(mouseX, mouseY);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.scriptFiles.mouseClicked(mouseX, mouseY, button);
        this.deleteButton.mouseClicked(mouseX, mouseY, button);
        if (this.scriptFiles.isClosed()) {
            super.mouseClicked(mouseX, mouseY, button);
            this.descriptionField.mouseClicked(mouseX, mouseY, button);
            if (HoveringUtil.isHovering(this.updateButton.getX(), this.updateButton.getY(), this.updateButton.getWidth(), this.updateButton.getHeight(), mouseX, mouseY)) {
                final String descriptionText = this.descriptionField.getText();
                final String[] descArray = descriptionText.split(" ");
                final boolean descriptionFilter = descriptionText.length() > 35 && descArray.length <= 1;
                if (this.descriptionField.getText().isEmpty() || descriptionFilter) {
                    this.error = true;
                }
                else {
                    this.error = false;
                    this.updateButton.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    @Override
    public void clear() {
    }
    
    public void setup(final CloudData data, final boolean script) {
        this.script = script;
        this.data = data;
        this.descriptionField.setMaxStringLength(210);
        this.descriptionField.setText(data.getDescription());
        if (script) {
            this.scriptFiles.setupOptions(Tenacity.INSTANCE.getScriptManager().getScriptFileNameList());
        }
    }
}
