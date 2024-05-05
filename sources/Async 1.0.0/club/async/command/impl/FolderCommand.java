package club.async.command.impl;

import club.async.Async;
import club.async.command.Command;
import club.async.module.Module;
import club.async.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Command.Info(name = "Folder", description = "Open the Async Folder", usage = ".folder", aliases = {"folder", "f"})
public class FolderCommand extends Command {

    @Override
    public void execute(String[] args) {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "Async/");
        String s = file.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);

            try {
                Runtime.getRuntime().exec(s1);
                return;
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        } else if(Util.getOSType() == Util.EnumOS.OSX) {
            try {
                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", s});
                return;
            } catch (IOException ioexception1) {
                ioexception1.printStackTrace();
            }
        }

        boolean flag = false;

        try
        {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, file.toURI());
        }
        catch (Throwable throwable)
        {
            flag = true;
        }

        if (flag)
        {
            Sys.openURL("file://" + s);
        }
    }
}
