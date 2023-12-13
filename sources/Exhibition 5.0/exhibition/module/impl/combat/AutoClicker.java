// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventMouse;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import exhibition.module.Module;

public class AutoClicker extends Module
{
    public static final String DELAY = "DELAY";
    public static final String RANDOM = "RANDOM";
    public static final String MIN = "MINRAND";
    public static final String MAX = "MAXRAND";
    public static final String MOUSE = "ON-MOUSE";
    public EntityLivingBase targ;
    Timer timer;
    
    public AutoClicker(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        ((HashMap<String, Setting<Integer>>)this.settings).put("DELAY", new Setting<Integer>("DELAY", 100, "Base click delay.", 25.0, 50.0, 500.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("RANDOM", new Setting<Boolean>("RANDOM", true, "Randomize click delay."));
        ((HashMap<String, Setting<Integer>>)this.settings).put("MINRAND", new Setting<Integer>("MINRAND", 50, "Minimum click randomization.", 25.0, 25.0, 200.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put("MAXRAND", new Setting<Integer>("MAXRAND", 100, "Maximum click randomization.", 25.0, 25.0, 200.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("ON-MOUSE", new Setting<Boolean>("ON-MOUSE", true, "Click when mouse is held down."));
    }
    
    public static int randomNumber(final int max, final int min) {
        final int ii = -min + (int)(Math.random() * (max - -min + 1));
        return ii;
    }
    
    @RegisterEvent(events = { EventMotion.class, EventMouse.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && AutoClicker.mc.currentScreen == null && AutoClicker.mc.thePlayer.isEntityAlive()) {
                if (((HashMap<K, Setting<Boolean>>)this.settings).get("ON-MOUSE").getValue() && !Mouse.isButtonDown(0)) {
                    return;
                }
                final int delay = ((HashMap<K, Setting<Number>>)this.settings).get("DELAY").getValue().intValue();
                final int minran = ((HashMap<K, Setting<Number>>)this.settings).get("MINRAND").getValue().intValue();
                final int maxran = ((HashMap<K, Setting<Number>>)this.settings).get("MAXRAND").getValue().intValue();
                final boolean random = ((HashMap<K, Setting<Boolean>>)this.settings).get("RANDOM").getValue();
                if (this.timer.delay(delay + (random ? randomNumber(minran, maxran) : 0))) {
                    AutoClicker.mc.playerController.onStoppedUsingItem(AutoClicker.mc.thePlayer);
                    AutoClicker.mc.thePlayer.swingItem();
                    AutoClicker.mc.clickMouse();
                    this.timer.reset();
                }
            }
        }
        if (event instanceof EventMouse) {
            final EventMouse em2 = (EventMouse)event;
            if (em2.getButtonID() == 1) {}
        }
    }
}
