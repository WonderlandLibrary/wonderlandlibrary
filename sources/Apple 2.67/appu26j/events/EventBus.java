package appu26j.events;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.google.common.eventbus.Subscribe;

public class EventBus
{
    private final ArrayList<Object> registeredObjects = new ArrayList<>();
    
    public EventBus(String name)
    {
        ;
    }
    
    public void register(Object object)
    {
        this.registeredObjects.add(object);
    }
    
    public void unregister(Object object)
    {
        this.registeredObjects.remove(object);
    }
    
    public void post(Object object)
    {
        for (int i = 0; i < this.registeredObjects.size(); i++)
        {
            Object clazz = this.registeredObjects.get(i);
            Class originalClazz = clazz.getClass();
            
            for (Method method : originalClazz.getMethods())
            {
                if (method.isAnnotationPresent(Subscribe.class))
                {
                    Class<?>[] args = method.getParameterTypes();
                    
                    for (Class<?> argument : args)
                    {
                        if (argument.getName().equals(object.getClass().getName()))
                        {
                            try
                            {
                                method.invoke(clazz, object);
                            }
                            
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
