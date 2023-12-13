package de.tired.base.module.implementation.visual.esp;

import de.tired.base.event.events.*;
import de.tired.base.interfaces.IHook;
import de.tired.base.module.Module;
import lombok.Getter;

public abstract class EspExtension implements IHook {

    @Getter
    public final String modeName = getClass().getAnnotation(EspModeAnnotation.class).name();

    public EspExtension() {

    }

    public abstract void onRender3D(Render3DEvent event2);
    public abstract void onRender3D2(Render3DEvent2 event2);

    public abstract void onRender2D(Render2DEvent event);

    public abstract void onEnable();

    public abstract void onDisable();


}