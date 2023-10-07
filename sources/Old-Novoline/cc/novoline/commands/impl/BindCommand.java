package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleArrayMap;
import cc.novoline.modules.binds.KeybindFactory;
import cc.novoline.modules.binds.ModuleKeybind;
import cc.novoline.modules.configurations.holder.ModuleHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
//import net.skidunion.security.annotations.Protect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.*;

import static cc.novoline.utils.messages.MessageFactory.text;
import static cc.novoline.utils.messages.MessageFactory.usage;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

//@Protect
public final class BindCommand extends NovoCommand {

    private static final Map<String, Integer> KEYS = new Object2ObjectOpenHashMap<>();

    static {
        KEYS.put("MOUSE4", -4);
        KEYS.put("MOUSE3", -3);

        try {
            for (Field field : Keyboard.class.getFields()) {
                if (field.getName().startsWith("KEY_")) {
                    field.setAccessible(true);
                    KEYS.put(field.getName().substring(4).toUpperCase(Locale.ROOT), (int) field.get(null));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /* constructors */
    public BindCommand(@NotNull Novoline novoline) {
        super(novoline, "bind", "Binds key for module", Arrays.asList("b", "binding", "binds", "keybind", "setbind"));
    }

    /* methods */
    @Override
    public void process(String[] args) {
        if (args.length < 2) {
            sendHelp( // @off
                    "Binds help:", ".bind",
                    usage("(module) (key)", "sets keyBind to module")
            ); // @on

            sendEmpty();
            send(text("TIP: ", GRAY).append("Use ").append("\"none\"", YELLOW).append(" to unbind!"));
            return;
        }

        String moduleName = args[0], keyArg = args[1].toUpperCase(Locale.ROOT);
        Integer key = KEYS.getOrDefault(keyArg, null);

        if (key == null) {
            notifyError("Key was not found!");
            return;
        }

        AbstractModule module = novoline.getModuleManager().getByNameIgnoreCase(moduleName);

        if (module == null) {
            notifyError("Module " + moduleName + " not found!");
            return;
        }

        ModuleKeybind keyBind;

        if (key >= 0) {
            keyBind = KeybindFactory.keyboard(key);
        } else {
            keyBind = KeybindFactory.mouse(-key);
        }

        setKeybind(module, keyBind);
        notify("Module " + module.getName() + " was " + (key != 0 ? "bound to " + keyArg : "unbound"));
    }

    @Override
    public @Nullable List<String> completeTabOptions(@NotNull String[] args) {
        switch (args.length) { // @off
            case 1:
                ModuleArrayMap moduleManager = novoline.getModuleManager().getModuleManager();
                Collection<ModuleHolder<?>> values = moduleManager.values();

                return completeTab(values.stream().map(ModuleHolder::getModule).map(AbstractModule::getName), args[0], true);
            case 2:
                return completeTab(KEYS.keySet(), args[1], true);
            default:
                return null;
        } // @on
    }

    public void setKeybind(@NotNull AbstractModule module, ModuleKeybind keybind) {
        module.setKeyBind(keybind);
        novoline.getModuleManager().getBindManager().save();
    }
}
