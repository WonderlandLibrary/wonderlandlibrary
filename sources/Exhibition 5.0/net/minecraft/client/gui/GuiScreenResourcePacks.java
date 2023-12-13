// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.Collections;
import org.lwjgl.Sys;
import java.net.URI;
import net.minecraft.util.Util;
import net.minecraft.client.resources.ResourcePackListEntry;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class GuiScreenResourcePacks extends GuiScreen
{
    private static final Logger logger;
    private GuiScreen field_146965_f;
    private List field_146966_g;
    private List field_146969_h;
    private GuiResourcePackAvailable field_146970_i;
    private GuiResourcePackSelected field_146967_r;
    private boolean field_175289_s;
    private static final String __OBFID = "CL_00000820";
    
    public GuiScreenResourcePacks(final GuiScreen p_i45050_1_) {
        this.field_175289_s = false;
        this.field_146965_f = p_i45050_1_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
        this.field_146966_g = Lists.newArrayList();
        this.field_146969_h = Lists.newArrayList();
        final ResourcePackRepository var1 = this.mc.getResourcePackRepository();
        var1.updateRepositoryEntriesAll();
        final ArrayList var2 = Lists.newArrayList((Iterable)var1.getRepositoryEntriesAll());
        var2.removeAll(var1.getRepositoryEntries());
        for (final ResourcePackRepository.Entry var4 : var2) {
            this.field_146966_g.add(new ResourcePackListEntryFound(this, var4));
        }
        for (final ResourcePackRepository.Entry var4 : Lists.reverse(var1.getRepositoryEntries())) {
            this.field_146969_h.add(new ResourcePackListEntryFound(this, var4));
        }
        this.field_146969_h.add(new ResourcePackListEntryDefault(this));
        (this.field_146970_i = new GuiResourcePackAvailable(this.mc, 200, this.height, this.field_146966_g)).setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.field_146970_i.registerScrollButtons(7, 8);
        (this.field_146967_r = new GuiResourcePackSelected(this.mc, 200, this.height, this.field_146969_h)).setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.field_146967_r.registerScrollButtons(7, 8);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146967_r.handleMouseScrolling();
        this.field_146970_i.handleMouseScrolling();
    }
    
    public boolean hasResourcePackEntry(final ResourcePackListEntry p_146961_1_) {
        return this.field_146969_h.contains(p_146961_1_);
    }
    
    public List func_146962_b(final ResourcePackListEntry p_146962_1_) {
        return this.hasResourcePackEntry(p_146962_1_) ? this.field_146969_h : this.field_146966_g;
    }
    
    public List func_146964_g() {
        return this.field_146966_g;
    }
    
    public List func_146963_h() {
        return this.field_146969_h;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                final File var2 = this.mc.getResourcePackRepository().getDirResourcepacks();
                final String var3 = var2.getAbsolutePath();
                Label_0135: {
                    if (Util.getOSType() == Util.EnumOS.OSX) {
                        try {
                            GuiScreenResourcePacks.logger.info(var3);
                            Runtime.getRuntime().exec(new String[] { "/usr/bin/open", var3 });
                            return;
                        }
                        catch (IOException var4) {
                            GuiScreenResourcePacks.logger.error("Couldn't open file", (Throwable)var4);
                            break Label_0135;
                        }
                    }
                    if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                        final String var5 = String.format("cmd.exe /C start \"Open file\" \"%s\"", var3);
                        try {
                            Runtime.getRuntime().exec(var5);
                            return;
                        }
                        catch (IOException var6) {
                            GuiScreenResourcePacks.logger.error("Couldn't open file", (Throwable)var6);
                        }
                    }
                }
                boolean var7 = false;
                try {
                    final Class var8 = Class.forName("java.awt.Desktop");
                    final Object var9 = var8.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
                    var8.getMethod("browse", URI.class).invoke(var9, var2.toURI());
                }
                catch (Throwable var10) {
                    GuiScreenResourcePacks.logger.error("Couldn't open link", var10);
                    var7 = true;
                }
                if (var7) {
                    GuiScreenResourcePacks.logger.info("Opening via system class!");
                    Sys.openURL("file://" + var3);
                }
            }
            else if (button.id == 1) {
                if (this.field_175289_s) {
                    final ArrayList var11 = Lists.newArrayList();
                    for (final ResourcePackListEntry var13 : this.field_146969_h) {
                        if (var13 instanceof ResourcePackListEntryFound) {
                            var11.add(((ResourcePackListEntryFound)var13).func_148318_i());
                        }
                    }
                    Collections.reverse(var11);
                    this.mc.getResourcePackRepository().func_148527_a(var11);
                    this.mc.gameSettings.resourcePacks.clear();
                    for (final ResourcePackRepository.Entry var14 : var11) {
                        this.mc.gameSettings.resourcePacks.add(var14.getResourcePackName());
                    }
                    this.mc.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }
                this.mc.displayGuiScreen(this.field_146965_f);
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_146970_i.func_148179_a(mouseX, mouseY, mouseButton);
        this.field_146967_r.func_148179_a(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        this.field_146970_i.drawScreen(mouseX, mouseY, partialTicks);
        this.field_146967_r.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void func_175288_g() {
        this.field_175289_s = true;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
