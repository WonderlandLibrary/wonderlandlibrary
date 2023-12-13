package today.flux.module.implement.Player;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.FloatValue;

public class Wings extends Module {

    public static FloatValue red = new FloatValue("Wing", "Red", 255, 0, 255, 1);
    public static FloatValue green = new FloatValue("Wing", "Green", 255, 0, 255, 1);
    public static FloatValue blue = new FloatValue("Wing", "Blue", 255, 0, 255, 1);

    //IM TOO LAZY
    //TODO public static BooleanValue rainbow = new BooleanValue("Wing", "Rainbow", false);

    public Wings() {
        super("Wing", Category.Player, false);
    }
}
