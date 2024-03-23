package xyz.northclient.draggable;

import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.draggable.impl.*;

import java.util.LinkedList;

public class DraggableManager {

    //made by Tecnessino
    public LinkedList<AbstractDraggable> DraggableList = new LinkedList<>();

    public static Scoreboard scoreboard;

    public DraggableManager() {
        Add(new Watermark());
        Add(new TargetInfo());
        Add(new Arraylist());
        Add(new Keybinds());
        Add(scoreboard = new Scoreboard());

        for(AbstractDraggable draggable : DraggableList) {
            draggable.Init();
        }
    }


    public void Add(AbstractDraggable Draggable) {
        DraggableList.add(Draggable);
    }
    public <T extends AbstractDraggable> T Get(String name) {
        return (T) DraggableList.stream().filter(manager -> manager.getClass().getSimpleName().equalsIgnoreCase(name)).findFirst().get();
    }
    public void Render() {
        for(AbstractDraggable draggable : DraggableList) {
            draggable.AllowRender = draggable.isEnabled();
            if(draggable.AllowRender) {
                Vector2f size = draggable.Render();
                draggable.Size = size;
            } else {
                draggable.Size = new Vector2f(0,0);
            }
        }
    }


}
