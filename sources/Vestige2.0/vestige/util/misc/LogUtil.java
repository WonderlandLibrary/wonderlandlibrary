package vestige.util.misc;

import lombok.experimental.UtilityClass;
import vestige.Vestige;

@UtilityClass
public class LogUtil {

    public void logToConsole(String message) {
        System.out.println("[" + Vestige.getInstance().getName() + "] " + message);
    }

}
