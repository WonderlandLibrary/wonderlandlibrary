package vestige.module.impl.visual;

import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.EventListenType;
import vestige.module.Module;

public class Freelook extends Module {

    private boolean wasFreelooking;

    public Freelook() {
        super("Freelook", Category.VISUAL);

        this.listenType = EventListenType.MANUAL;
        this.startListening();
    }

    @Listener
    public void onTick(TickEvent event) {
        if(mc.thePlayer.ticksExisted < 10) {
            stop();
        }

        if(Keyboard.isKeyDown(getKey())) {
            wasFreelooking = true;

            Vestige.instance.getCameraHandler().setFreelooking(true);

            mc.gameSettings.thirdPersonView = 1;
        } else {
            if(wasFreelooking) {
                stop();
            }
        }
    }

    private void stop() {
        this.setEnabled(false);

        Vestige.instance.getCameraHandler().setFreelooking(false);
        wasFreelooking = false;

        mc.gameSettings.thirdPersonView = 0;
    }

}
