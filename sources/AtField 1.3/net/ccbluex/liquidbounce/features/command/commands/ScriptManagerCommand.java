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
import java.util.Iterator;
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
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            if (StringsKt.equals((String)stringArray[1], (String)"import", (boolean)true)) {
                try {
                    File file = MiscUtils.openFileChooser();
                    if (file == null) {
                        return;
                    }
                    File file2 = file;
                    String string = file2.getName();
                    if (StringsKt.endsWith$default((String)string, (String)".js", (boolean)false, (int)2, null)) {
                        LiquidBounce.INSTANCE.getScriptManager().importScript(file2);
                        LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                        this.chat("Successfully imported script.");
                        return;
                    }
                    if (StringsKt.endsWith$default((String)string, (String)".zip", (boolean)false, (int)2, null)) {
                        Object object;
                        Object object2;
                        Object object3;
                        Object object4;
                        ZipFile zipFile = new ZipFile(file2);
                        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
                        ArrayList<Object> arrayList = new ArrayList<Object>();
                        while (enumeration.hasMoreElements()) {
                            object4 = enumeration.nextElement();
                            String string2 = ((ZipEntry)object4).getName();
                            object3 = new File(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder(), string2);
                            if (((ZipEntry)object4).isDirectory()) {
                                ((File)object3).mkdir();
                                continue;
                            }
                            object2 = zipFile.getInputStream((ZipEntry)object4);
                            object = new FileOutputStream((File)object3);
                            IOUtils.copy(object2, (OutputStream)((OutputStream)object));
                            ((FileOutputStream)object).close();
                            ((InputStream)object2).close();
                            if (string2.equals("/")) continue;
                            arrayList.add(object3);
                        }
                        object4 = arrayList;
                        boolean bl = false;
                        object3 = object4.iterator();
                        while (object3.hasNext()) {
                            object2 = object3.next();
                            object = (File)object2;
                            boolean bl2 = false;
                            LiquidBounce.INSTANCE.getScriptManager().loadScript((File)object);
                        }
                        LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
                        this.chat("Successfully imported script.");
                        return;
                    }
                    this.chat("The file extension has to be .js or .zip");
                }
                catch (Throwable throwable) {
                    ClientUtils.getLogger().error("Something went wrong while importing a script.", throwable);
                    this.chat(throwable.getClass().getName() + ": " + throwable.getMessage());
                }
            } else if (StringsKt.equals((String)stringArray[1], (String)"delete", (boolean)true)) {
                try {
                    if (stringArray.length <= 2) {
                        this.chatSyntax("scriptmanager delete <index>");
                        return;
                    }
                    Object object = stringArray[2];
                    boolean bl = false;
                    int n = Integer.parseInt((String)object);
                    if (n >= (object = LiquidBounce.INSTANCE.getScriptManager().getScripts()).size()) {
                        this.chat("Index " + n + " is too high.");
                        return;
                    }
                    Script script = (Script)object.get(n);
                    LiquidBounce.INSTANCE.getScriptManager().deleteScript(script);
                    LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
                    this.chat("Successfully deleted script.");
                }
                catch (NumberFormatException numberFormatException) {
                    this.chatSyntaxError();
                }
                catch (Throwable throwable) {
                    ClientUtils.getLogger().error("Something went wrong while deleting a script.", throwable);
                    this.chat(throwable.getClass().getName() + ": " + throwable.getMessage());
                }
            } else if (StringsKt.equals((String)stringArray[1], (String)"reload", (boolean)true)) {
                try {
                    LiquidBounce.INSTANCE.setCommandManager(new CommandManager());
                    LiquidBounce.INSTANCE.getCommandManager().registerCommands();
                    LiquidBounce.INSTANCE.setStarting(true);
                    LiquidBounce.INSTANCE.getScriptManager().disableScripts();
                    LiquidBounce.INSTANCE.getScriptManager().unloadScripts();
                    for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                        LiquidBounce.INSTANCE.getModuleManager().generateCommand$AtField(module);
                    }
                    LiquidBounce.INSTANCE.getScriptManager().loadScripts();
                    LiquidBounce.INSTANCE.getScriptManager().enableScripts();
                    LiquidBounce.INSTANCE.setStarting(false);
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
                    LiquidBounce.INSTANCE.setClickGui(new ClickGui());
                    LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
                    this.chat("Successfully reloaded all scripts.");
                }
                catch (Throwable throwable) {
                    ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", throwable);
                    this.chat(throwable.getClass().getName() + ": " + throwable.getMessage());
                }
            } else if (StringsKt.equals((String)stringArray[1], (String)"folder", (boolean)true)) {
                try {
                    Desktop.getDesktop().open(LiquidBounce.INSTANCE.getScriptManager().getScriptsFolder());
                    this.chat("Successfully opened scripts folder.");
                }
                catch (Throwable throwable) {
                    ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", throwable);
                    this.chat(throwable.getClass().getName() + ": " + throwable.getMessage());
                }
            }
            return;
        }
        ScriptManager scriptManager = LiquidBounce.INSTANCE.getScriptManager();
        Iterable iterable = scriptManager.getScripts();
        boolean bl = false;
        if (!iterable.isEmpty()) {
            this.chat("\u00a7c\u00a7lScripts");
            iterable = scriptManager.getScripts();
            bl = false;
            int n = 0;
            for (Object e : iterable) {
                int n2 = n++;
                boolean bl3 = false;
                if (n2 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n3 = n2;
                Script script = (Script)e;
                int n4 = n3;
                boolean bl4 = false;
                this.chat(n4 + ": \u00a7a\u00a7l" + script.getScriptName() + " \u00a7a\u00a7lv" + script.getScriptVersion() + " \u00a73by \u00a7a\u00a7l" + ArraysKt.joinToString$default((Object[])script.getScriptAuthors(), (CharSequence)", ", null, null, (int)0, null, null, (int)62, null));
            }
        }
        this.chatSyntax("scriptmanager <import/delete/reload/folder>");
    }

    public ScriptManagerCommand() {
        super("scriptmanager", "scripts");
    }

    @Override
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (stringArray.length) {
            case 1: {
                object = CollectionsKt.listOf((Object[])new String[]{"delete", "import", "folder", "reload"});
                bl = false;
                Object object2 = object;
                Collection collection = new ArrayList();
                boolean bl2 = false;
                Iterator iterator2 = object2.iterator();
                while (iterator2.hasNext()) {
                    Object t = iterator2.next();
                    String string = (String)t;
                    boolean bl3 = false;
                    if (!StringsKt.startsWith((String)string, (String)stringArray[0], (boolean)true)) continue;
                    collection.add(t);
                }
                list = (List)collection;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }
}

