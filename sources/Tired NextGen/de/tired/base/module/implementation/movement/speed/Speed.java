package de.tired.base.module.implementation.movement.speed;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;
import lombok.SneakyThrows;
import org.lwjgl.input.Keyboard;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ModuleAnnotation(name = "Speed", key = Keyboard.KEY_NONE, category = ModuleCategory.MOVEMENT, clickG = "Move faster.")
public class Speed extends Module {

    private final ModeSetting modeValue;

    private final List<SpeedExtension> speedModes;

    public Speed() {
        speedModes = new ArrayList<>();
        final Reflections reflections = new Reflections("de.tired.base.module.implementation.movement.speed");

        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(SpeedModeAnnotation.class);

        ArrayList<String> modesArray = new ArrayList<>();
        for (Class<?> aClass : classes) {
            try {
                final SpeedExtension speedExtension = (SpeedExtension) aClass.newInstance();
                speedModes.add(speedExtension);
                modesArray.add(speedExtension.modeName);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String[] modes = new String[modesArray.size()];
        modesArray.toArray(modes);
        modeValue = new ModeSetting("SpeedMode", this, modes);
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        if (speedModes == null || MC.thePlayer == null) return;
        Objects.requireNonNull(getCurrentMode()).onPacket(e);
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (speedModes == null || MC.thePlayer == null) return;
        Objects.requireNonNull(getCurrentMode()).onPre(e);
    }

    @EventTarget
    public void eventMove(UpdateEvent e) {
        if (speedModes == null || MC.thePlayer == null) return;
        Objects.requireNonNull(getCurrentMode()).onUpdate(e);
    }

    @SneakyThrows
    private SpeedExtension getCurrentMode() {
        for (SpeedExtension speedExtension : speedModes) {
            if (speedExtension.modeName.equalsIgnoreCase(modeValue.getValue())) {
                return speedExtension;
            }
        }
        return null;
    }

    @Override
    public void onState() {
    }

    @Override
    public void onUndo() {
    }
}
