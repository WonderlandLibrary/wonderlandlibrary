package tech.atani.client.processor.storage;

import de.florianmichael.rclasses.storage.Storage;
import org.reflections.Reflections;
import tech.atani.client.listener.handling.EventHandling;
import tech.atani.client.processor.Processor;
import tech.atani.client.processor.data.ProcessorInfo;

import java.lang.reflect.InvocationTargetException;

public class ProcessorStorage extends Storage<Processor> {

    private static ProcessorStorage instance;

    @Override
    public void init() {
        EventHandling.getInstance().registerListener(this);
        final Reflections reflections = new Reflections("tech.atani");
        reflections.getTypesAnnotatedWith(ProcessorInfo.class).forEach(aClass -> {
            try {
                Processor processor = (Processor) aClass.getDeclaredConstructor().newInstance();
                this.add(processor);
                processor.launch();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public <V extends Processor> V getByClass(final Class<V> clazz) {
        final Processor processor = this.getList().stream().filter(m -> m.getClass().equals(clazz)).findFirst().orElse(null);
        if (processor == null) return null;
        return clazz.cast(processor);
    }

    public static ProcessorStorage getInstance() {
        return instance;
    }

    public static void setInstance(ProcessorStorage instance) {
        ProcessorStorage.instance = instance;
    }
}
