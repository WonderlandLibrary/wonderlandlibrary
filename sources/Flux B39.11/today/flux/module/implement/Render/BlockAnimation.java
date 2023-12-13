package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

public class BlockAnimation extends Module {

    public static ModeValue swordAnimation = new ModeValue("Animation", "Sword Animation", "1.7",
            "1.7", "Slide", "Sigma", "Push", "Push1", "Fixed", "Reverse", "Reverse1", "Vanilla", "Strange", "Spin", "Screw", "Poke", "Swong", "Exhi 1.7", "Exhi Swang", "Exhi Swong", "Exhi Swank", "E");

    public static FloatValue x = new FloatValue("Animation", "Blocking X", 0f, -2.5f, 2.5f, 0.1f);
    public static FloatValue y = new FloatValue("Animation", "Blocking Y", 0f, -2.5f, 2.5f, 0.1f);
    public static FloatValue z = new FloatValue("Animation", "Blocking Z", 0f, -2.5f, 2.5f, 0.1f);

    public static FloatValue xArm = new FloatValue("Animation", "Arm X", 0f, -2.5f, 2.5f, 0.1f);
    public static FloatValue yArm = new FloatValue("Animation", "Arm Y", 0f, -2.5f, 2.5f, 0.1f);
    public static FloatValue zArm = new FloatValue("Animation", "Arm Z", 0f, -2.5f, 2.5f, 0.1f);

    public static FloatValue swingSpeed = new FloatValue("Animation", "Swing Slowdown", 1, 0.5f, 3f, 0.1f);

    public static BooleanValue swingAnimation = new BooleanValue("Animation", "Swing Animation", true);

    public BlockAnimation() {
        super("Animation", Category.Render, false);
    }
}
