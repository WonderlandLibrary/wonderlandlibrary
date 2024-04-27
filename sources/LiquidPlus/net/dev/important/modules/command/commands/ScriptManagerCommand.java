/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

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
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.clickgui.ClickGui;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.command.CommandManager;
import net.dev.important.modules.module.Manager;
import net.dev.important.modules.module.Module;
import net.dev.important.script.Script;
import net.dev.important.script.ScriptManager;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.misc.MiscUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/dev/important/modules/command/commands/ScriptManagerCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class ScriptManagerCommand
extends Command {
    public ScriptManagerCommand() {
        String[] stringArray = new String[]{"scripts"};
        super("scriptmanager", stringArray);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1) {
            if (StringsKt.equals(args2[1], "import", true)) {
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
                        this.chat("Successfully imported script.");
                        return;
                    }
                    if (StringsKt.endsWith$default(fileName, ".zip", false, 2, null)) {
                        ZipFile zipFile = new ZipFile(file2);
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        ArrayList<Object> scriptFiles = new ArrayList<Object>();
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
                        this.chat("Successfully imported script.");
                        return;
                    }
                    this.chat("The file extension has to be .js or .zip");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while importing a script.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            } else if (StringsKt.equals(args2[1], "delete", true)) {
                try {
                    List<Script> scripts;
                    if (args2.length <= 2) {
                        this.chatSyntax("scriptmanager delete <index>");
                        return;
                    }
                    int scriptIndex = Integer.parseInt(args2[2]);
                    if (scriptIndex >= (scripts = Client.INSTANCE.getScriptManager().getScripts()).size()) {
                        this.chat("Index " + scriptIndex + " is too high.");
                        return;
                    }
                    Script script = scripts.get(scriptIndex);
                    Client.INSTANCE.getScriptManager().deleteScript(script);
                    Client.INSTANCE.setClickGui(new ClickGui());
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().clickGuiConfig);
                    Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().hudConfig);
                    this.chat("Successfully deleted script.");
                }
                catch (NumberFormatException numberFormat) {
                    this.chatSyntaxError();
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            } else if (StringsKt.equals(args2[1], "reload", true)) {
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
                    this.chat("Successfully reloaded all scripts.");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            } else if (StringsKt.equals(args2[1], "folder", true)) {
                try {
                    Desktop.getDesktop().open(Client.INSTANCE.getScriptManager().getScriptsFolder());
                    this.chat("Successfully opened scripts folder.");
                }
                catch (Throwable t) {
                    ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", t);
                    this.chat(t.getClass().getName() + ": " + t.getMessage());
                }
            }
            return;
        }
        ScriptManager scriptManager = Client.INSTANCE.getScriptManager();
        if (!((Collection)scriptManager.getScripts()).isEmpty()) {
            this.chat("\u00a7c\u00a7lScripts");
            Iterable $this$forEachIndexed$iv = scriptManager.getScripts();
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                void script;
                int n = index$iv;
                index$iv = n + 1;
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                Script entryFile = (Script)item$iv;
                int index = n;
                boolean bl = false;
                this.chat(index + ": \u00a7a\u00a7l" + script.getScriptName() + " \u00a7a\u00a7lv" + script.getScriptVersion() + " \u00a73by \u00a7a\u00a7l" + ArraysKt.joinToString$default(script.getScriptAuthors(), (CharSequence)", ", null, null, 0, null, null, 62, null));
            }
        }
        this.chatSyntax("scriptmanager <import/delete/reload/folder>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        List list;
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 0) {
            return CollectionsKt.emptyList();
        }
        if (args2.length == 1) {
            void $this$filterTo$iv$iv;
            String[] stringArray = new String[]{"delete", "import", "folder", "reload"};
            Iterable $this$filter$iv = CollectionsKt.listOf(stringArray);
            boolean $i$f$filter = false;
            Iterable iterable = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                String it = (String)element$iv$iv;
                boolean bl = false;
                if (!StringsKt.startsWith(it, args2[0], true)) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            list = (List)destination$iv$iv;
        } else {
            list = CollectionsKt.emptyList();
        }
        return list;
    }
}

