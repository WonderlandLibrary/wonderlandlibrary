package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import org.apache.commons.lang3.RandomUtils;
import vip.radium.event.impl.world.TickEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Auto Clicker", category = ModuleCategory.COMBAT)
public final class AutoClicker extends Module {

    private final DoubleProperty minApsProperty = new DoubleProperty("Min APS", 9.0, 1.0,
            20.0, 0.1);
    private final DoubleProperty maxApsProperty = new DoubleProperty("Max APS", 12.0, 1.0,
            20.0, 0.1);
    private final Property<Boolean> rightClickProperty = new Property<>("Right Click", false);

    private final TimerUtil cpsTimer = new TimerUtil();

    @EventLink
    public final Listener<TickEvent> onTickEvent = event -> {
        if (rightClickProperty.getValue() && Wrapper.getGameSettings().keyBindUseItem.isKeyDown()) {
            Wrapper.getMinecraft().rightClickMouse();
        } else if (Wrapper.getGameSettings().keyBindAttack.isKeyDown() && !Wrapper.getPlayer().isUsingItem()) {
            final int cps = RandomUtils.nextInt(minApsProperty.getValue().intValue(), maxApsProperty.getValue().intValue());
            if (cpsTimer.hasElapsed(1000 / cps)) {
                Wrapper.getMinecraft().clickMouse();
                cpsTimer.reset();
            }
        }
    };
}
