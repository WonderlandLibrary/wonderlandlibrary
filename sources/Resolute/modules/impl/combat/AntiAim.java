// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class AntiAim extends Module
{
    public ModeSetting yaw;
    public ModeSetting pitch;
    float[] lastAngles;
    public static float rotationPitch;
    private boolean fake;
    private boolean fake1;
    TimerUtil fakeJitter;
    
    public AntiAim() {
        super("AntiAim", 0, "Randomizes your aim", Category.COMBAT);
        this.yaw = new ModeSetting("Yaw", "FakeJitter", new String[] { "FakeJitter", "Reverse", "Jitter", "Lisp", "SpinSlow", "SpinFast", "Sideways" });
        this.pitch = new ModeSetting("Pitch", "Halfdown", new String[] { "Halfdown", "Normal", "Zero", "Up", "Stutter", "Down", "Meme" });
        this.fakeJitter = new TimerUtil();
        this.addSettings(this.yaw, this.pitch);
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
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            final EventMotion em = (EventMotion)e;
            if (e.isPre()) {
                if (this.lastAngles == null) {
                    this.lastAngles = new float[] { AntiAim.mc.thePlayer.rotationYaw, AntiAim.mc.thePlayer.rotationPitch };
                }
                this.fake = !this.fake;
                final String mode = this.yaw.getMode();
                switch (mode) {
                    case "Jitter": {
                        float yawJitter = 0.0f;
                        yawJitter = this.lastAngles[0] + 180.0f;
                        em.setYaw(yawJitter);
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
                        if (this.fakeJitter.hasElapsed(350L)) {
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
                final String mode2 = this.pitch.getMode();
                switch (mode2) {
                    case "Halfdown": {
                        final float pitchDown = 90.0f;
                        this.lastAngles = new float[] { this.lastAngles[0], 90.0f };
                        em.setPitch(90.0f);
                        this.updateAngles(this.lastAngles[0], 90.0f);
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
                    case "Down": {
                        this.lastAngles = new float[] { this.lastAngles[0], 90.0f };
                        em.setPitch(90.0f);
                        this.updateAngles(this.lastAngles[0], 90.0f);
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
