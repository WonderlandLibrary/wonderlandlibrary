package kevin.plugin;

import java.util.LinkedList;
import java.util.List;

public class SensitiveUtils {
    static String PACKAGE;
    Class<?> getCallerClass() {
        return getClassDirectly(getCallerClassName(2));
    }

    List<Class<?>> scanStackTrace() {
        LinkedList<Class<?>> classes = new LinkedList<>();
        try {
            StackTraceElement[] trace = (new Throwable()).getStackTrace();
            for (int i = 1; i < trace.length; i++) {
                classes.add(getClassDirectly(trace[i].getClassName()));
            }
        } catch (Throwable ignored) {}
        return classes;
    }

    String getCallerClassName(int depth) {
        return (new Throwable()).getStackTrace()[depth].getClassName();
    }

    Class<?> getClassDirectly(String name) {
        try {
            return Class.forName(name);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    void callerSensitivity(Class<?> clz) {
        if (clz.getName().startsWith(PACKAGE)) return;
        throw new RuntimeException(new IllegalAccessException(clz.getName()));
    }

    void validate(PluginDescription desc) {
        if (desc.main.contains(" ")) {

        }
    }

    static {
        String name = SensitiveUtils.class.getName();
        PACKAGE = name.substring(0, name.lastIndexOf(".") + 1);
    }
}
