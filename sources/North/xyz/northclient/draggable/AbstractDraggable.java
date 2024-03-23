package xyz.northclient.draggable;

import net.minecraft.client.Minecraft;
import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.InstanceAccess;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;


public abstract class AbstractDraggable extends AbstractModule {
    public Minecraft mc = Minecraft.getMinecraft();
    public boolean AllowRender = false;
    public int X = 0;
    public int Y = 0;
    public Vector2f Size;

    public AbstractDraggable(boolean enabled) {
        super(true);
        setName(getDraggableName());
        setDescription("Draggable");
        setCategory(Category.INTERFACE);
        if(enabled) {
            toggle();
        }
        setDisplayName(getDraggableName());
        NorthSingleton.INSTANCE.getModules().getModules().add(this);
    }

    public void Init() {

    }

    /*
    * Render loop
    * Return value is using to calculate draggable size to make it movable
    * @author Tecness
    * */
    public abstract Vector2f Render();
    public abstract String getDraggableName();
}
