package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.dragging.DragHandler;
import de.tired.base.dragging.Draggable;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

@ModuleAnnotation(name = "Scoreboard", category = ModuleCategory.RENDER)
public class Scoreboard extends Module {

    public final Draggable draggable = DragHandler.setupDrag(this, "Scoreboard", 4, 100, false);


    @EventTarget
    public void onRender2D(Render2DEvent event2) {
    }


    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
