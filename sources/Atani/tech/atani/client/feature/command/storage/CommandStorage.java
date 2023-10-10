package tech.atani.client.feature.command.storage;

import de.florianmichael.rclasses.storage.Storage;
import org.reflections.Reflections;
import tech.atani.client.listener.handling.EventHandling;
import tech.atani.client.feature.command.Command;
import tech.atani.client.feature.command.data.CommandInfo;

import java.lang.reflect.InvocationTargetException;

public class CommandStorage extends Storage<Command> {

    private static CommandStorage instance;

    @Override
    public void init() {
        EventHandling.getInstance().registerListener(this);
        final Reflections reflections = new Reflections("tech.atani");
        reflections.getTypesAnnotatedWith(CommandInfo.class).forEach(aClass -> {
            try {
                this.add((Command) aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    public static CommandStorage getInstance() {
        return instance;
    }

    public static void setInstance(CommandStorage instance) {
        CommandStorage.instance = instance;
    }
}
