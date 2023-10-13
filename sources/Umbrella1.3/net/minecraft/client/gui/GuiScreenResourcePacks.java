/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.Sys
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

public class GuiScreenResourcePacks
extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private GuiScreen field_146965_f;
    private List field_146966_g;
    private List field_146969_h;
    private GuiResourcePackAvailable field_146970_i;
    private GuiResourcePackSelected field_146967_r;
    private boolean field_175289_s = false;
    private static final String __OBFID = "CL_00000820";

    public GuiScreenResourcePacks(GuiScreen p_i45050_1_) {
        this.field_146965_f = p_i45050_1_;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
        this.field_146966_g = Lists.newArrayList();
        this.field_146969_h = Lists.newArrayList();
        ResourcePackRepository var1 = this.mc.getResourcePackRepository();
        var1.updateRepositoryEntriesAll();
        ArrayList var2 = Lists.newArrayList((Iterable)var1.getRepositoryEntriesAll());
        var2.removeAll(var1.getRepositoryEntries());
        for (ResourcePackRepository.Entry var4 : var2) {
            this.field_146966_g.add(new ResourcePackListEntryFound(this, var4));
        }
        for (ResourcePackRepository.Entry var4 : Lists.reverse((List)var1.getRepositoryEntries())) {
            this.field_146969_h.add(new ResourcePackListEntryFound(this, var4));
        }
        this.field_146969_h.add(new ResourcePackListEntryDefault(this));
        this.field_146970_i = new GuiResourcePackAvailable(this.mc, 200, this.height, this.field_146966_g);
        this.field_146970_i.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.field_146970_i.registerScrollButtons(7, 8);
        this.field_146967_r = new GuiResourcePackSelected(this.mc, 200, this.height, this.field_146969_h);
        this.field_146967_r.setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.field_146967_r.registerScrollButtons(7, 8);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146967_r.func_178039_p();
        this.field_146970_i.func_178039_p();
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_) {
        return this.field_146969_h.contains(p_146961_1_);
    }

    public List func_146962_b(ResourcePackListEntry p_146962_1_) {
        return this.hasResourcePackEntry(p_146962_1_) ? this.field_146969_h : this.field_146966_g;
    }

    public List func_146964_g() {
        return this.field_146966_g;
    }

    public List func_146963_h() {
        return this.field_146969_h;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                File var2 = this.mc.getResourcePackRepository().getDirResourcepacks();
                String var3 = var2.getAbsolutePath();
                if (Util.getOSType() == Util.EnumOS.OSX) {
                    try {
                        logger.info(var3);
                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", var3});
                        return;
                    }
                    catch (IOException var9) {
                        logger.error("Couldn't open file", (Throwable)var9);
                    }
                } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                    String var4 = String.format("cmd.exe /C start \"Open file\" \"%s\"", var3);
                    try {
                        Runtime.getRuntime().exec(var4);
                        return;
                    }
                    catch (IOException var8) {
                        logger.error("Couldn't open file", (Throwable)var8);
                    }
                }
                boolean var12 = false;
                try {
                    Class<?> var5 = Class.forName("java.awt.Desktop");
                    Object var6 = var5.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    var5.getMethod("browse", URI.class).invoke(var6, var2.toURI());
                }
                catch (Throwable var7) {
                    logger.error("Couldn't open link", var7);
                    var12 = true;
                }
                if (var12) {
                    logger.info("Opening via system class!");
                    Sys.openURL((String)("file://" + var3));
                }
            } else if (button.id == 1) {
                if (this.field_175289_s) {
                    ArrayList var10 = Lists.newArrayList();
                    for (ResourcePackListEntry var13 : this.field_146969_h) {
                        if (!(var13 instanceof ResourcePackListEntryFound)) continue;
                        var10.add(((ResourcePackListEntryFound)var13).func_148318_i());
                    }
                    Collections.reverse(var10);
                    this.mc.getResourcePackRepository().func_148527_a(var10);
                    this.mc.gameSettings.resourcePacks.clear();
                    for (ResourcePackRepository.Entry var14 : var10) {
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
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_146970_i.func_148179_a(mouseX, mouseY, mouseButton);
        this.field_146967_r.func_148179_a(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.field_146970_i.drawScreen(mouseX, mouseY, partialTicks);
        this.field_146967_r.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), this.width / 2, 16.0f, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), this.width / 2 - 77, this.height - 26, 0x808080);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void func_175288_g() {
        this.field_175289_s = true;
    }
}

