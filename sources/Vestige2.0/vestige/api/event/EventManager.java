package vestige.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import vestige.Vestige;

public class EventManager {

    private final ArrayList<Object> listeningObjects = new ArrayList<>();
    private final CopyOnWriteArrayList<ListeningMethod> listeningMethods = new CopyOnWriteArrayList<>();

    public void register(Object o) {
        if(!listeningObjects.contains(o)) {
            listeningObjects.add(o);
        }

        updateListeningMethods();
    }

    public void unregister(Object o) {
        if(listeningObjects.contains(o)) {
            listeningObjects.remove(o);
        }

        updateListeningMethods();
    }

    private void updateListeningMethods() {
        listeningMethods.clear();
        listeningObjects.forEach(o -> Arrays.stream(o.getClass().getMethods()).filter(m -> m.isAnnotationPresent(Listener.class) && m.getParameters().length == 1).forEach(m -> listeningMethods.add(new ListeningMethod(m, o))));
        listeningMethods.sort(Comparator.comparingInt(m -> m.getMethod().getAnnotation(Listener.class).value()));
    }

    public void handle(Event e) {
    	if (Vestige.getInstance().isDestructed()) return;
    	
        listeningMethods.forEach(m -> {
        	Listener annotation = m.getMethod().getAnnotation(Listener.class);
            for (Parameter p : m.getMethod().getParameters()) {
                if (e.getClass().equals(p.getType())) {
                    try {
                        m.getMethod().invoke(m.getInstance(), e);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

}


