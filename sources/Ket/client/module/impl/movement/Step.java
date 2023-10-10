package client.module.impl.movement;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.StepEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.ModeValue;
import client.value.impl.NumberValue;
import client.value.impl.SubMode;

@ModuleInfo(name = "Step", description = "", category = Category.MOVEMENT)
public class Step extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Vanilla"))
            .add(new SubMode("Jump"))
            .setDefault("Vanilla");
    private final NumberValue height = new NumberValue("Height", this, 0.6f, 0, 10, 0.1f);
    @EventLink
    public final Listener<StepEvent> onStep = event -> {
        switch (mode.getValue().getName()) {
            case "Vanilla": {
                event.setHeight(height.getValue().floatValue());
                break;
            }
            case "Jump": {
                if (mc.thePlayer.jumpTicks == 0) {
                    mc.thePlayer.jump();
                    mc.thePlayer.jumpTicks = 10;
                }
                break;
            }
            default: {
                break;
            }
        }
    };
}
