/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSlot
 *  org.apache.commons.io.IOUtils
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.gui.client;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.clickgui.ClickGui;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.command.CommandManager;
import net.dev.important.modules.module.Manager;
import net.dev.important.modules.module.Module;
import net.dev.important.script.Script;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.misc.MiscUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\f\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0014J \u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0007H\u0016J\u0018\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fH\u0014R\u0012\u0010\u0004\u001a\u00060\u0005R\u00020\u0000X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/dev/important/gui/client/GuiScripts;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "(Lnet/minecraft/client/gui/GuiScreen;)V", "list", "Lnet/dev/important/gui/client/GuiScripts$GuiList;", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "handleMouseInput", "initGui", "keyTyped", "typedChar", "", "keyCode", "GuiList", "LiquidBounce"})
public final class GuiScripts
extends GuiScreen {
    @NotNull
    private final GuiScreen prevGui;
    private GuiList list;

    public GuiScripts(@NotNull GuiScreen prevGui) {
        Intrinsics.checkNotNullParameter(prevGui, "prevGui");
        this.prevGui = prevGui;
    }

    public void func_73866_w_() {
        this.list = new GuiList(this);
        GuiList guiList = this.list;
        if (guiList == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
            guiList = null;
        }
        guiList.func_148134_d(7, 8);
        GuiList guiList2 = this.list;
        if (guiList2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
            guiList2 = null;
        }
        guiList2.func_148144_a(-1, false, 0, 0);
        int j = 22;
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l - 80, this.field_146295_m - 65, 70, 20, "Back"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l - 80, j + 24, 70, 20, "Import"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 80, j + 48, 70, 20, "Delete"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l - 80, j + 72, 70, 20, "Reload"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l - 80, j + 96, 70, 20, "Folder"));
        this.field_146292_n.add(new GuiButton(5, this.field_146294_l - 80, j + 120, 70, 20, "Docs"));
        this.field_146292_n.add(new GuiButton(6, this.field_146294_l - 80, j + 144, 70, 20, "Find Scripts"));
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        GuiList guiList = this.list;
        if (guiList == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
            guiList = null;
        }
        guiList.func_148128_a(mouseX, mouseY, partialTicks);
        this.func_73732_a(Fonts.font40, "\u00a79\u00a7lScripts", this.field_146294_l / 2, 28, 0xFFFFFF);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkNotNullParameter(button, "button");
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
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
                    Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                    if (StringsKt.endsWith$default(fileName, ".js", false, 2, null)) {
                        Client.INSTANCE.getScriptManager().importScript(file2);
                        Client.INSTANCE.setClickGui(new ClickGui());
                        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().clickGuiConfig);
                        return;
                    }
                    if (StringsKt.endsWith$default(fileName, ".zip", false, 2, null)) {
                        ZipFile zipFile = new ZipFile(file2);
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        ArrayList<File> scriptFiles = new ArrayList<File>();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            File entryFile = new File(Client.INSTANCE.getScriptManager().getScriptsFolder(), entryName);
                            if (entry.isDirectory()) {
                                entryFile.mkdir();
                                continue;
                            }
                            InputStream fileStream = zipFile.getInputStream(entry);
                            FileOutputStream fileOutputStream = new FileOutputStream(entryFile);
                            IOUtils.copy((InputStream)fileStream, (OutputStream)fileOutputStream);
                            fileOutputStream.close();
                            fileStream.close();
                            Intrinsics.checkNotNullExpressionValue(entryName, "entryName");
                            if (StringsKt.contains$default((CharSequence)entryName, "/", false, 2, null)) continue;
                            scriptFiles.add(entryFile);
                        }
                        Iterable $this$forEach$iv = scriptFiles;
                        boolean $i$f$forEach = false;
                        for (Object element$iv : $this$forEach$iv) {
                            File scriptFile = (File)element$iv;
                            boolean bl = false;
                            Client.INSTANCE.getScriptManager().loadScript(scriptFile);
                        }
                        Client.INSTANCE.setClickGui(new ClickGui());
                        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().clickGuiConfig);
                        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().hudConfig);
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
                    GuiList guiList = this.list;
                    if (guiList == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("list");
                        guiList = null;
                    }
                    if (guiList.getSelectedSlot$LiquidBounce() == -1) break;
                    List<Script> list = Client.INSTANCE.getScriptManager().getScripts();
                    GuiList guiList2 = this.list;
                    if (guiList2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("list");
                        guiList2 = null;
                    }
                    Script script = list.get(guiList2.getSelectedSlot$LiquidBounce());
                    Client.INSTANCE.getScriptManager().deleteScript(script);
                    Client.INSTANCE.setClickGui(new ClickGui());
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().clickGuiConfig);
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().hudConfig);
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 3: {
                try {
                    Client.INSTANCE.setCommandManager(new CommandManager());
                    Client.INSTANCE.getCommandManager().registerCommands();
                    Client.INSTANCE.setStarting(true);
                    Client.INSTANCE.getScriptManager().disableScripts();
                    Client.INSTANCE.getScriptManager().unloadScripts();
                    for (Module module2 : Client.INSTANCE.getModuleManager().getModules()) {
                        Manager manager = Client.INSTANCE.getModuleManager();
                        Intrinsics.checkNotNullExpressionValue(module2, "module");
                        manager.generateCommand$LiquidBounce(module2);
                    }
                    Client.INSTANCE.getScriptManager().loadScripts();
                    Client.INSTANCE.getScriptManager().enableScripts();
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().modulesConfig);
                    Client.INSTANCE.setStarting(false);
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().valuesConfig);
                    Client.INSTANCE.setClickGui(new ClickGui());
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().clickGuiConfig);
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t);
                    MiscUtils.showErrorPopup(t.getClass().getName(), t.getMessage());
                }
                break;
            }
            case 4: {
                try {
                    Desktop.getDesktop().open(Client.INSTANCE.getScriptManager().getScriptsFolder());
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

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    public void func_146274_d() {
        super.func_146274_d();
        GuiList guiList = this.list;
        if (guiList == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
            guiList = null;
        }
        guiList.func_178039_p();
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0014J8\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0014J(\u0010\u0010\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0006H\u0016J\r\u0010\u0014\u001a\u00020\u0006H\u0000\u00a2\u0006\u0002\b\u0015J\b\u0010\u0016\u001a\u00020\u0006H\u0014J\u0010\u0010\u0017\u001a\u00020\u00122\u0006\u0010\n\u001a\u00020\u0006H\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lnet/dev/important/gui/client/GuiScripts$GuiList;", "Lnet/minecraft/client/gui/GuiSlot;", "gui", "Lnet/minecraft/client/gui/GuiScreen;", "(Lnet/dev/important/gui/client/GuiScripts;Lnet/minecraft/client/gui/GuiScreen;)V", "selectedSlot", "", "drawBackground", "", "drawSlot", "id", "x", "y", "var4", "var5", "var6", "elementClicked", "doubleClick", "", "var3", "getSelectedSlot", "getSelectedSlot$LiquidBounce", "getSize", "isSelected", "LiquidBounce"})
    private final class GuiList
    extends GuiSlot {
        private int selectedSlot;

        public GuiList(GuiScreen gui) {
            Intrinsics.checkNotNullParameter((Object)GuiScripts.this, "this$0");
            Intrinsics.checkNotNullParameter(gui, "gui");
            super(GuiScripts.this.field_146297_k, gui.field_146294_l, gui.field_146295_m, 40, gui.field_146295_m - 40, 30);
        }

        protected boolean func_148131_a(int id) {
            return this.selectedSlot == id;
        }

        public final int getSelectedSlot$LiquidBounce() {
            return this.selectedSlot > Client.INSTANCE.getScriptManager().getScripts().size() ? -1 : this.selectedSlot;
        }

        protected int func_148127_b() {
            return Client.INSTANCE.getScriptManager().getScripts().size();
        }

        public void func_148144_a(int id, boolean doubleClick, int var3, int var4) {
            this.selectedSlot = id;
        }

        protected void func_180791_a(int id, int x, int y, int var4, int var5, int var6) {
            Script script = Client.INSTANCE.getScriptManager().getScripts().get(id);
            GuiScripts.this.func_73732_a(Fonts.fontSFUI40, "\u00a79" + script.getScriptName() + " \u00a77v" + script.getScriptVersion(), this.field_148155_a / 2, y + 3, Color.LIGHT_GRAY.getRGB());
            GuiScripts.this.func_73732_a(Fonts.fontSFUI40, Intrinsics.stringPlus("by \u00a7c", ArraysKt.joinToString$default(script.getScriptAuthors(), (CharSequence)", ", null, null, 0, null, null, 62, null)), this.field_148155_a / 2, y + 16, Color.LIGHT_GRAY.getRGB());
        }

        protected void func_148123_a() {
        }
    }
}

