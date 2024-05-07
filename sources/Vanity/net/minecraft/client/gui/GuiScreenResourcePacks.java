package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.masterof13fps.Client;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.render.GLSLSandboxShader;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.*;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class GuiScreenResourcePacks extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private final GLSLSandboxShader shader;
    private final GuiScreen parentScreen;
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;

    /**
     * List component that contains the available resource packs
     */
    private GuiResourcePackAvailable availableResourcePacksList;

    /**
     * List component that contains the selected resource packs
     */
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed = false;

    public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
        parentScreen = parentScreenIn;

        try {
            shader = new GLSLSandboxShader("/assets/minecraft/client/shader/main.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load the Shader");
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 38, I18n.format("resourcePack.openFolder", new Object[0])));
        buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 38, I18n.format("gui.done", new Object[0])));

        if (!changed) {
            availableResourcePacks = Lists.newArrayList();
            selectedResourcePacks = Lists.newArrayList();
            ResourcePackRepository resourcepackrepository = mc.getResourcePackRepository();
            resourcepackrepository.updateRepositoryEntriesAll();
            List<ResourcePackRepository.Entry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
            list.removeAll(resourcepackrepository.getRepositoryEntries());

            for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
                availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
            }

            for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.getRepositoryEntries())) {
                selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
            }

            selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
        }

        availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height, availableResourcePacks);
        availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
        availableResourcePacksList.registerScrollButtons(7, 8);
        selectedResourcePacksList = new GuiResourcePackSelected(mc, 200, height, selectedResourcePacks);
        selectedResourcePacksList.setSlotXBoundsFromLeft(width / 2 + 4);
        selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        selectedResourcePacksList.handleMouseInput();
        availableResourcePacksList.handleMouseInput();
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_) {
        return selectedResourcePacks.contains(p_146961_1_);
    }

    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry p_146962_1_) {
        return hasResourcePackEntry(p_146962_1_) ? selectedResourcePacks : availableResourcePacks;
    }

    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        return availableResourcePacks;
    }

    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        return selectedResourcePacks;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 2) {
                File file1 = mc.getResourcePackRepository().getDirResourcepacks();
                String s = file1.getAbsolutePath();

                if (Util.getOSType() == Util.EnumOS.OSX) {
                    try {
                        logger.info(s);
                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                        return;
                    } catch (IOException ioexception1) {
                        logger.error((String) "Couldn\'t open file", (Throwable) ioexception1);
                    }
                } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{s});

                    try {
                        Runtime.getRuntime().exec(s1);
                        return;
                    } catch (IOException ioexception) {
                        logger.error((String) "Couldn\'t open file", (Throwable) ioexception);
                    }
                }

                boolean flag = false;

                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{file1.toURI()});
                } catch (Throwable throwable) {
                    logger.error("Couldn\'t open link", throwable);
                    flag = true;
                }

                if (flag) {
                    logger.info("Opening via system class!");
                    Sys.openURL("file://" + s);
                }
            } else if (button.id == 1) {
                if (changed) {
                    List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();

                    for (ResourcePackListEntry resourcepacklistentry : selectedResourcePacks) {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
                            list.add(((ResourcePackListEntryFound) resourcepacklistentry).func_148318_i());
                        }
                    }

                    Collections.reverse(list);
                    mc.getResourcePackRepository().setRepositories(list);
                    mc.gameSettings.resourcePacks.clear();
                    mc.gameSettings.field_183018_l.clear();

                    for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
                        mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());

                        if (resourcepackrepository$entry.func_183027_f() != 1) {
                            mc.gameSettings.field_183018_l.add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }

                    mc.gameSettings.saveOptions();
                    mc.refreshResources();
                }

                mc.displayGuiScreen(parentScreen);
            }
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution s = new ScaledResolution(mc);
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        shader.useShader(width, height, mouseX, mouseY, (System.currentTimeMillis() - Client.main().getInitTime()) / 1000F);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        GL20.glUseProgram(0);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(0, 30, s.width(), s.height() - 50);
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        RenderUtils.drawRect(0, s.height() - 50, s.width(), s.height(), new Color(0, 0, 0, 120).getRGB());

        UnicodeFontRenderer comfortaa20 = Client.main().fontMgr().font("Comfortaa", 20, Font.PLAIN);
        comfortaa20.drawCenteredString(I18n.format("resourcePack.title", new Object[0]), width / 2, 16, 16777215);
        comfortaa20.drawCenteredString(I18n.format("resourcePack.folderInfo", new Object[0]), width / 2 - 77, height - 15, 8421504);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Marks the selected resource packs list as changed to trigger a resource reload when the screen is closed
     */
    public void markChanged() {
        changed = true;
    }
}
