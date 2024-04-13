package com.canon.majik.impl.hook;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.LoopEvent;
import org.lwjgl.opengl.Display;

public class TitleHook implements Hooker{

    private final String[] text = {"S","Sk","Sku","Skul","Skull","SkullH","SkullHa","SkullH4","SkullHac","SkullHack","SkullHac","SkullH4","SkullHa","SkullH","Skull","Skul","Sku","Sk","S"};
    private long time = 0L;
    private int index = 0;

    @Override
    public void init() {
        Initializer.eventBus.registerListener(this);
    }

    @EventListener
    public void onLoop(LoopEvent event){
        if (System.currentTimeMillis() - time > 300) {
            Display.setTitle(text[index]);
            if (index >= text.length - 1) {
                index = 0;
            } else {
                index++;
            }
            time = System.currentTimeMillis();
        }
    }

    @Override
    public void unInit() {

    }
}
