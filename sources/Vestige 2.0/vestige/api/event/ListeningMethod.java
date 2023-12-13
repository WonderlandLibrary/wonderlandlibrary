package vestige.api.event;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class ListeningMethod {

    private Method method;
    private Object instance;

    public ListeningMethod(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }


}
