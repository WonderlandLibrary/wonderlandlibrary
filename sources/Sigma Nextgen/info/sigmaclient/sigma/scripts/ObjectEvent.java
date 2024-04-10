package info.sigmaclient.sigma.scripts;

import info.sigmaclient.sigma.event.Event;

public class ObjectEvent {
    public String name;
    public ObjectEvent(String o){
        name = o;
    }
    public void run(Object event){

    }
    public static ObjectEvent newEvent(EventRunnable eventRunnable, String name){
        return new ObjectEvent(name){
            @Override
            public void run(Object event) {
                eventRunnable.run((Event) event);
            }
        };
    }
}
