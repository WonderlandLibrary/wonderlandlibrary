package tech.atani.client.processor;

import tech.atani.client.listener.handling.EventHandling;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.processor.data.ProcessorInfo;

public class Processor implements Methods {

    private final String name;
    private boolean running = false;

    public Processor() {
        ProcessorInfo processorInfo = this.getClass().getAnnotation(ProcessorInfo.class);
        if(processorInfo == null)
            throw new RuntimeException();
        this.name = processorInfo.name();
    }

    public void launch() {
        EventHandling.getInstance().registerListener(this);
        running = true;
    }

    public void end() {
        EventHandling.getInstance().unregisterListener(this);
        running = true;
    }

}
