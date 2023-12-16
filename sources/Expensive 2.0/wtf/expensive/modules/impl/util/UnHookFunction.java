package wtf.expensive.modules.impl.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.optifine.shaders.Shaders;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.events.Event;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BindSetting;
import wtf.expensive.ui.clickgui.Window;
import wtf.expensive.ui.unHookUI;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.misc.TimerUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dedinside
 * @since 02.07.2023
 */
@FunctionAnnotation(name = "UnHookFunction", type = Type.Util)
public class UnHookFunction extends Function {
    public static final List<Function> functionsToBack = new CopyOnWriteArrayList<>();
    public BindSetting unHookKey = new BindSetting(" нопка возрата", GLFW.GLFW_KEY_HOME);
    public TimerUtil timerUtil = new TimerUtil();

    public UnHookFunction() {
        addSettings(unHookKey);
    }

    @Override
    protected void onEnable() {
        timerUtil.reset();
        Minecraft.getInstance().displayGuiScreen(new unHookUI(new StringTextComponent("UNHOOk")));
        super.onEnable();
    }

    public void onUnhook() {
        ClientUtil.stopRPC();
        functionsToBack.clear();
        for (int i = 0; i < Managment.FUNCTION_MANAGER.getFunctions().size(); i++) {
            Function function = Managment.FUNCTION_MANAGER.getFunctions().get(i);
            if (function.state && function != this) {
                functionsToBack.add(function);
                function.setState(false);
            }
        }
        File folder = new File("C:\\Expensive");

        if (folder.exists()) {
            try {
                Path folderPathObj = folder.toPath();
                DosFileAttributeView attributes = Files.getFileAttributeView(folderPathObj, DosFileAttributeView.class);
                attributes.setHidden(true);
            } catch (IOException e) {
                System.out.println("ќшибка при скрытии папки: " + e.getMessage());
            }
        }
        mc.fileResourcepacks = new File(System.getenv("appdata") + "\\.minecraft" + "\\resourcepacks");
        Shaders.shaderPacksDir = new File(System.getenv("appdata") + "\\.minecraft" + "\\shaderpacks");
        toggle();
    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }
}
