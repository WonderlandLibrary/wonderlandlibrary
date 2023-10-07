// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.msgo;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.Options;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AntiAim extends Module
{
    private String AAYAW;
    private String AAPITCH;
    float[] lastAngles;
    public static float rotationPitch;
    private boolean fake;
    private boolean fake1;
    Timer fakeJitter;
    
    public AntiAim(final ModuleData data) {
        super(data);
        this.AAYAW = "AAYAW";
        this.AAPITCH = "AAPITCH";
        this.fakeJitter = new Timer();
        ((HashMap<String, Setting<Options>>)this.settings).put(this.AAYAW, new Setting<Options>(this.AAYAW, new Options("AA Yaw", "FakeJitter", new String[] { "Reverse", "Jitter", "Lisp", "SpinSlow", "SpinFast", "Sideways", "FakeJitter" }), "AA Yaw."));
        ((HashMap<String, Setting<Options>>)this.settings).put(this.AAPITCH, new Setting<Options>(this.AAPITCH, new Options("AA Pitch", "HalfDown", new String[] { "Normal", "HalfDown", "Zero", "Up", "Stutter", "Reverse", "Meme" }), "AA Pitch."));
    }
    
    @Override
    public void onDisable() {
        this.fake1 = true;
        this.lastAngles = null;
        AntiAim.rotationPitch = 0.0f;
        AntiAim.mc.thePlayer.renderYawOffset = AntiAim.mc.thePlayer.rotationYaw;
    }
    
    @Override
    public void onEnable() {
        this.fake1 = true;
        this.lastAngles = null;
        AntiAim.rotationPitch = 0.0f;
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && !Aimbot.isFiring) {
                if (this.lastAngles == null) {
                    this.lastAngles = new float[] { AntiAim.mc.thePlayer.rotationYaw, AntiAim.mc.thePlayer.rotationPitch };
                }
                this.fake = !this.fake;
                final String selected = ((HashMap<K, Setting<Options>>)this.settings).get(this.AAYAW).getValue().getSelected();
                switch (selected) {
                    case "Jitter": {
                        float yawJitter = 0.0f;
                        em.setYaw(yawJitter = this.lastAngles[0] + 180.0f);
                        this.lastAngles = new float[] { yawJitter, this.lastAngles[1] };
                        this.updateAngles(yawJitter, this.lastAngles[1]);
                        break;
                    }
                    case "Lisp": {
                        final float yaw = this.lastAngles[0] + 150000.0f;
                        this.lastAngles = new float[] { yaw, this.lastAngles[1] };
                        em.setYaw(yaw);
                        this.updateAngles(yaw, this.lastAngles[1]);
                        break;
                    }
                    case "Reverse": {
                        final float yawReverse = AntiAim.mc.thePlayer.rotationYaw + 180.0f;
                        this.lastAngles = new float[] { yawReverse, this.lastAngles[1] };
                        em.setYaw(yawReverse);
                        this.updateAngles(yawReverse, this.lastAngles[1]);
                        break;
                    }
                    case "Sideways": {
                        final float yawLeft = AntiAim.mc.thePlayer.rotationYaw - 90.0f;
                        this.lastAngles = new float[] { yawLeft, this.lastAngles[1] };
                        em.setYaw(yawLeft);
                        this.updateAngles(yawLeft, this.lastAngles[1]);
                        break;
                    }
                    case "FakeJitter": {
                        if (this.fakeJitter.delay(350.0f)) {
                            this.fake1 = !this.fake1;
                            this.fakeJitter.reset();
                        }
                        final float yawRight = AntiAim.mc.thePlayer.rotationYaw + (this.fake1 ? 90 : -90);
                        this.lastAngles = new float[] { yawRight, this.lastAngles[1] };
                        em.setYaw(yawRight);
                        this.updateAngles(yawRight, this.lastAngles[1]);
                        break;
                    }
                    case "SpinFast": {
                        final float yawSpinFast = this.lastAngles[0] + 45.0f;
                        this.lastAngles = new float[] { yawSpinFast, this.lastAngles[1] };
                        em.setYaw(yawSpinFast);
                        this.updateAngles(yawSpinFast, this.lastAngles[1]);
                        break;
                    }
                    case "SpinSlow": {
                        final float yawSpinSlow = this.lastAngles[0] + 10.0f;
                        this.lastAngles = new float[] { yawSpinSlow, this.lastAngles[1] };
                        em.setYaw(yawSpinSlow);
                        this.updateAngles(yawSpinSlow, this.lastAngles[1]);
                        break;
                    }
                }
                final String selected2 = ((HashMap<K, Setting<Options>>)this.settings).get(this.AAPITCH).getValue().getSelected();
                switch (selected2) {
                    case "HalfDown": {
                        final float pitchDown = 90.0f;
                        this.lastAngles = new float[] { this.lastAngles[0], pitchDown };
                        em.setPitch(pitchDown);
                        this.updateAngles(this.lastAngles[0], pitchDown);
                        break;
                    }
                    case "Meme": {
                        float lastMeme = this.lastAngles[1];
                        lastMeme += 10.0f;
                        if (lastMeme > 90.0f) {
                            lastMeme = -90.0f;
                        }
                        this.lastAngles = new float[] { this.lastAngles[0], lastMeme };
                        em.setPitch(lastMeme);
                        this.updateAngles(this.lastAngles[0], lastMeme);
                        break;
                    }
                    case "Normal": {
                        this.updateAngles(this.lastAngles[0], 0.0f);
                        break;
                    }
                    case "Reverse": {
                        final float reverse = AntiAim.mc.thePlayer.rotationPitch + 180.0f;
                        this.lastAngles = new float[] { this.lastAngles[0], reverse };
                        em.setPitch(reverse);
                        this.updateAngles(this.lastAngles[0], reverse);
                        break;
                    }
                    case "Stutter": {
                        float sutter;
                        if (this.fake) {
                            sutter = 90.0f;
                            em.setPitch(sutter);
                        }
                        else {
                            sutter = -45.0f;
                            em.setPitch(sutter);
                        }
                        this.lastAngles = new float[] { this.lastAngles[0], sutter };
                        this.updateAngles(this.lastAngles[0], sutter);
                        break;
                    }
                    case "Up": {
                        this.lastAngles = new float[] { this.lastAngles[0], -90.0f };
                        em.setPitch(-90.0f);
                        this.updateAngles(this.lastAngles[0], -90.0f);
                        break;
                    }
                    case "Zero": {
                        this.lastAngles = new float[] { this.lastAngles[0], -179.0f };
                        em.setPitch(-180.0f);
                        this.updateAngles(this.lastAngles[0], -179.0f);
                        break;
                    }
                }
            }
        }
    }
    
    public void updateAngles(final float yaw, final float pitch) {
        if (AntiAim.mc.gameSettings.thirdPersonView != 0) {
            AntiAim.rotationPitch = pitch;
            AntiAim.mc.thePlayer.rotationYawHead = yaw;
            AntiAim.mc.thePlayer.renderYawOffset = yaw;
        }
    }
}
