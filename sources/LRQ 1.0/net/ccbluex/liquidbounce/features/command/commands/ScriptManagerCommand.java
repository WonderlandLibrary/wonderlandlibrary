/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 *  org.apache.commons.io.IOUtils
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.script.ScriptManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import org.apache.commons.io.IOUtils;

public final class ScriptManagerCommand
extends Command {
    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            if (StringsKt.equals((String)args[1], (String)"import", (boolean)true)) {
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
                        this.chat("Successfully imported script.");
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
                        this.chat("Successfully imported script.");
                        return;
                    }
                    this.chat("The file extension has to be .js or .zip");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while importing a script.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            } else if (StringsKt.equals((String)args[1], (String)"delete", (boolean)true)) {
                try {
                    List<Script> scripts;
                    if (args.length <= 2) {
                        this.chatSyntax("scriptmanager delete <index>");
                        return;
                    }
                    String fileName = args[2];
                    boolean zipFile = false;
                    int scriptIndex = Integer.parseInt(fileName);
                    if (scriptIndex >= (scripts = LiquidBounce.INSTANCE.getScriptManager().getScripts()).size()) {
                        this.chat("Index " + scriptIndex + " is too high.");
                        return;
                    }
                    Script script = scripts.get(scriptIndex);
                    LiquidBounce.INSTANCE.getScriptManager().deleteScript(script);
                    LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
                    this.chat("Successfully deleted script.");
                }
                catch (NumberFormatException numberFormat) {
                    this.chatSyntaxError();
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            } else if (StringsKt.equals((String)args[1], (String)"reload", (boolean)true)) {
                try {
                    LiquidBounce.INSTANCE.setCommandManager(new CommandManager());
                    LiquidBounce.INSTANCE.getCommandManager().registerCommands();
                    LiquidBounce.INSTANCE.setStarting(true);
                    LiquidBounce.INSTANCE.getScriptManager().disableScripts();
                    LiquidBounce.INSTANCE.getScriptManager().unloadScripts();
                    for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                        LiquidBounce.INSTANCE.getModuleManager().generateCommand$LiquidSense(module);
                    }
                    LiquidBounce.INSTANCE.getScriptManager().loadScripts();
                    LiquidBounce.INSTANCE.getScriptManager().enableScripts();
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
                    LiquidBounce.INSTANCE.setStarting(false);
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
                    LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                    this.chat("Successfully reloaded all scripts.");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            } else if (StringsKt.equals((String)args[1], (String)"folder", (boolean)true)) {
                try {
                    Desktop.getDesktop().open(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder());
                    this.chat("Successfully opened scripts folder.");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            }
            return;
        }
        ScriptManager scriptManager = LiquidBounce.INSTANCE.getScriptManager();
        Collection scripts = scriptManager.getScripts();
        boolean script = false;
        if (!scripts.isEmpty()) {
            this.chat("\u00a7c\u00a7lScripts");
            Iterable $this$forEachIndexed$iv = scriptManager.getScripts();
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                void script2;
                int n = index$iv++;
                boolean bl = false;
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n2 = n;
                Script scriptFile = (Script)item$iv;
                int index = n2;
                boolean bl2 = false;
                this.chat(index + ": \u00a7a\u00a7l" + script2.getScriptName() + " \u00a7a\u00a7lv" + script2.getScriptVersion() + " \u00a73by \u00a7a\u00a7l" + ArraysKt.joinToString$default((Object[])script2.getScriptAuthors(), (CharSequence)", ", null, null, (int)0, null, null, (int)62, null));
            }
        }
        this.chatSyntax("scriptmanager <import/delete/reload/folder>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public List<String> tabComplete(String[] args) {
        List list;
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf((Object[])new String[]{"delete", "import", "folder", "reload"});
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith((String)it, (String)args[0], (boolean)true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public ScriptManagerCommand() {
        super("scriptmanager", "scripts");
    }
}

