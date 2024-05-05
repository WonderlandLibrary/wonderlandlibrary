/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.ArraysKt
 *  kotlin.text.StringsKt
 *  org.apache.commons.io.IOUtils
 */
package net.ccbluex.liquidbounce.ui.client;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import kotlin.collections.ArraysKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import org.apache.commons.io.IOUtils;

public final class GuiScripts
extends WrappedGuiScreen {
    private GuiList list;
    private final IGuiScreen prevGui;

    @Override
    public void initGui() {
        this.list = new GuiList(this.getRepresentedScreen());
        this.list.getRepresented().registerScrollButtons(7, 8);
        this.list.elementClicked(-1, false, 0, 0);
        int j = 22;
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() - 80, this.getRepresentedScreen().getHeight() - 65, 70, 20, "Back"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() - 80, j + 24, 70, 20, "Import"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() - 80, j + 48, 70, 20, "Delete"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(3, this.getRepresentedScreen().getWidth() - 80, j + 72, 70, 20, "Reload"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(4, this.getRepresentedScreen().getWidth() - 80, j + 96, 70, 20, "Folder"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(5, this.getRepresentedScreen().getWidth() - 80, j + 120, 70, 20, "Docs"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(6, this.getRepresentedScreen().getWidth() - 80, j + 144, 70, 20, "Find Scripts"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        this.list.getRepresented().drawScreen(mouseX, mouseY, partialTicks);
        Fonts.font40.drawCenteredString("\u00a79\u00a7lScripts", (float)this.getRepresentedScreen().getWidth() / 2.0f, 28.0f, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        switch (button.getId()) {
            case 0: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                try {
                    File file = MiscUtils.openFileChooser();
                    if (file == null) {
                        return;
                    }
                    File file2 = file;
                    String fileName = file2.getName();
                    if (StringsKt.endsWith$default((String)fileName, (String)".js", (boolean)false, (int)2, null)) {
                        LiquidBounce.INSTANCE.getScriptManager().importScript(file2);
                        LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                        return;
                    }
                    if (StringsKt.endsWith$default((String)fileName, (String)".zip", (boolean)false, (int)2, null)) {
                        ZipFile zipFile = new ZipFile(file2);
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        ArrayList<File> scriptFiles = new ArrayList<File>();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            File entryFile = new File(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder(), entryName);
                            if (entry.isDirectory()) {
                                entryFile.mkdir();
                                continue;
                            }
                            InputStream fileStream = zipFile.getInputStream(entry);
                            FileOutputStream fileOutputStream = new FileOutputStream(entryFile);
                            IOUtils.copy((InputStream)fileStream, (OutputStream)fileOutputStream);
                            fileOutputStream.close();
                            fileStream.close();
                            if (entryName.equals("/")) continue;
                            scriptFiles.add(entryFile);
                        }
                        Iterable $this$forEach$iv = scriptFiles;
                        boolean $i$f$forEach = false;
                        for (Object element$iv : $this$forEach$iv) {
                            File scriptFile = (File)element$iv;
                            boolean bl = false;
                            LiquidBounce.INSTANCE.getScriptManager().loadScript(scriptFile);
                        }
                        LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
                        return;
                    }
                    MiscUtils.showErrorPopup("Wrong file extension.", "The file extension has to be .js or .zip");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while importing a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 2: {
                try {
                    if (this.list.getSelectedSlot$LiquidSense() == -1) break;
                    Script script = LiquidBounce.INSTANCE.getScriptManager().getScripts().get(this.list.getSelectedSlot$LiquidSense());
                    LiquidBounce.INSTANCE.getScriptManager().deleteScript(script);
                    LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 3: {
                try {
                    LiquidBounce.INSTANCE.getScriptManager().reloadScripts();
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 4: {
                try {
                    Desktop.getDesktop().open(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder());
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 5: {
                try {
                    Desktop.getDesktop().browse(new URL("https://liquidbounce.net/docs/ScriptAPI/Getting%20Started").toURI());
                }
                catch (Exception exception) {}
                break;
            }
            case 6: {
                try {
                    Desktop.getDesktop().browse(new URL("https://forum.ccbluex.net/viewforum.php?id=16").toURI());
                    break;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        this.list.getRepresented().handleMouseInput();
    }

    public GuiScripts(IGuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    private final class GuiList
    extends WrappedGuiSlot {
        private int selectedSlot;

        @Override
        public boolean isSelected(int id) {
            return this.selectedSlot == id;
        }

        public final int getSelectedSlot$LiquidSense() {
            return this.selectedSlot > LiquidBounce.INSTANCE.getScriptManager().getScripts().size() ? -1 : this.selectedSlot;
        }

        @Override
        public int getSize() {
            return LiquidBounce.INSTANCE.getScriptManager().getScripts().size();
        }

        @Override
        public void elementClicked(int id, boolean doubleClick, int var3, int var4) {
            this.selectedSlot = id;
        }

        @Override
        public void drawSlot(int id, int x, int y, int var4, int var5, int var6) {
            Script script = LiquidBounce.INSTANCE.getScriptManager().getScripts().get(id);
            Fonts.font40.drawCenteredString("\u00a79" + script.getScriptName() + " \u00a77v" + script.getScriptVersion(), (float)GuiScripts.this.getRepresentedScreen().getWidth() / 2.0f, (float)y + 2.0f, Color.LIGHT_GRAY.getRGB());
            Fonts.font40.drawCenteredString("by \u00a7c" + ArraysKt.joinToString$default((Object[])script.getScriptAuthors(), (CharSequence)", ", null, null, (int)0, null, null, (int)62, null), (float)GuiScripts.this.getRepresentedScreen().getWidth() / 2.0f, (float)y + 15.0f, Color.LIGHT_GRAY.getRGB());
        }

        @Override
        public void drawBackground() {
        }

        public GuiList(IGuiScreen gui) {
            super(MinecraftInstance.mc, gui.getWidth(), gui.getHeight(), 40, gui.getHeight() - 40, 30);
        }
    }
}

