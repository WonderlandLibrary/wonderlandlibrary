// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.infopanel;

import dev.tenacity.utils.render.ColorUtil;
import java.util.Collection;
import java.util.Arrays;
import dev.tenacity.ui.sidegui.panels.Panel;

public class InfoPanel extends Panel
{
    private final InfoRect configInfo;
    private final InfoRect scriptInfo;
    
    public InfoPanel() {
        this.configInfo = new InfoRect();
        this.configInfo.faqButtons.addAll(Arrays.asList(new InfoButton("What client is this?", "This client was coded by cedo and others,edited by Razzle. And this client will be opensource when it is fully fixed."), new InfoButton("How do I download an online config?", "To download an online config, click the save icon in the bottom right corner of the config next to the load button."), new InfoButton("How do I delete an online config?", "To delete an online config you must first own the config you are trying to delete. There should be an edit icon in the bottom right corner of your config next to the download icon. Click the edit icon and you will be prompted with avalible editing options for the config such as deletion"), new InfoButton("How do I update an online config?", "To update an online config you must first own the config you are trying to update. There should be an edit icon in the bottom right corner of your config next to the download icon. Click the edit icon and you will be prompted with avalible editing options for the config such as updating"), new InfoButton("How do I get my online config pinned?", "To get your config pinned you must make a good enough config for a Developer to notice it and pin it."), new InfoButton("How do I save my config locally?", "To save your config locally, click the \"Save config\" button. You will then be prompted with a text field to name your local config."), new InfoButton("How does relevance ranking in cloud configs work?", "For configs, the relevance ranking is a weighted score system. We take into account the current server you are on, the ratio of upvotes to total votes, if the config was made on the current version, and how recently the config was updated."), new InfoButton("How can I copy my config's share code?", "When hovering over the \"Hover for more information\" text, click while holding the SHIFT key and the share code will be copied to your clipboard.")));
        this.scriptInfo = new InfoRect();
        this.scriptInfo.faqButtons.addAll(Arrays.asList(new InfoButton("How do I upload a script to the cloud?", "You can upload your script to the cloud by going to the Script panel and switching to the \"Local\" tab. Your local scripts will have an icon that looks like a cloud uploading. Click this icon to upload that specific script."), new InfoButton("How do I download an online script?", "To download an online script, click the file icon in the bottom right corner of the script."), new InfoButton("How do I delete an online script?", "To delete an online script you must first own the script you are trying to delete. There should be an edit icon in the bottom right corner of your script next to the download/save icon. Click the edit icon and you will be prompted with avalible editing options for the config such as deletion."), new InfoButton("How do I update an online script?", "To delete an online script you must first own the script you are trying to update. There should be an edit icon in the bottom right corner of your script next to the download/save icon. Click the edit icon and you will be prompted with avalible editing options for the config such as updating"), new InfoButton("How do I code a script?", "Scripts are coded in JavaScript using the cedoscript engine. You can find more information about all the scripting functionalities by clicking the \"Open documentation\" button."), new InfoButton("What folder should my local scripts be in?", "If you click the \"Open folder\" button the correct file location will open up in your file explorer."), new InfoButton("How do I refresh my local scripts?", "To refresh all script and config data, click the refresh icon in the top hotbar."), new InfoButton("How does relevance ranking in cloud scripts work?", "For scripts, the relevance ranking is still a weighted score system. We take into account the ratio of upvotes to total votes and how recently the script was updated."), new InfoButton("How can I copy my scripts's share code?", "When hovering over the \"Hover for more information\" text, click while holding the SHIFT key and the share code will be copied to your clipboard.")));
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        InfoPanel.tenacityBoldFont40.drawString("Info", this.getX() + 8.0f, this.getY() + 8.0f, this.getTextColor());
        InfoPanel.tenacityFont18.drawString("edited by Razzle", this.getX() + 8.0f, this.getY() + 30.0f, ColorUtil.applyOpacity(this.getTextColor(), 0.3f));
        final float spacing = 8.0f;
        final float infoWidth = (this.getWidth() - spacing * 3.0f) / 2.0f;
        this.configInfo.x = this.getX() + spacing;
        this.configInfo.y = this.getY() + 40.0f + spacing;
        this.configInfo.width = infoWidth;
        this.configInfo.height = this.getHeight() - (40.0f + spacing * 2.0f);
        this.configInfo.alpha = this.getAlpha();
        this.configInfo.drawScreen(mouseX, mouseY);
        this.scriptInfo.x = this.configInfo.x + this.configInfo.width + spacing;
        this.scriptInfo.y = this.getY() + 40.0f + spacing;
        this.scriptInfo.width = infoWidth;
        this.scriptInfo.height = this.getHeight() - (40.0f + spacing * 2.0f);
        this.scriptInfo.alpha = this.getAlpha();
        this.scriptInfo.drawScreen(mouseX, mouseY);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.configInfo.mouseClicked(mouseX, mouseY, button);
        this.scriptInfo.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
