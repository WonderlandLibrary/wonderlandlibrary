package info.sigmaclient.sigma.event;

import info.sigmaclient.sigma.modules.Module;

import java.lang.reflect.Method;

public class Temp {
    public Event event;
    public Module module;
    public Method method;
    public Temp(Event e, Module m, Method me){
        this.event = e;
        this.module = m;
        this.method = me;
    }
}
