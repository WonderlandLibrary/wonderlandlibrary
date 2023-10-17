package de.tired.base.module.implementation.visual.keystrokes;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.dragging.DragHandler;
import de.tired.base.dragging.Draggable;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleAnnotation(name = "KeyStrokes", category = ModuleCategory.RENDER)
public class Keystrokes extends Module {

    private final Draggable draggable = DragHandler.setupDrag(this, "Keystrokes", 50, 50, false);

    private final List<KeystrokeHandler> handlers = new ArrayList<>();

    public Keystrokes() {
        final AtomicInteger ASD = new AtomicInteger(20);
        for (KeystrokeKey keystrokeKey : KeystrokeKey.values()) {
            if (keystrokeKey != KeystrokeKey.W)
                handlers.add(new KeystrokeHandler(keystrokeKey, ASD.addAndGet(10), 20, 20, 20));
            else
                handlers.add(new KeystrokeHandler(keystrokeKey, ASD.get() / 2 - 20, 10, 20, 20));
        }
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        draggable.setObjectHeight(50);
        draggable.setObjectWidth(100);
        final AtomicInteger keyWidth = new AtomicInteger(0);
        final AtomicInteger ASD = new AtomicInteger((int) draggable.getXPosition() - 22);
        for (final KeystrokeHandler keystrokeHandler : handlers) {
            if (keystrokeHandler.key == KeystrokeKey.W) {
                keystrokeHandler.renderKey((int) (ASD.get() + draggable.getObjectWidth() / 2f), (int) draggable.getYPosition() + 4);
            } else {
                keystrokeHandler.renderKey(ASD.addAndGet(25), (int) draggable.getYPosition() + 29);
                keyWidth.addAndGet(25);
            }
        }
        draggable.setObjectWidth(keyWidth.get());
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
