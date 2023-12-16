// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.event.impl.SCUpdateEvent;
import moonsense.event.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import moonsense.event.impl.SCKeyEvent;
import moonsense.utils.KeyBinding;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class TimerModule extends SCDefaultRenderModule
{
    private final Setting startStopKey;
    private final Setting resetOnStart;
    public boolean isTimerRunning;
    public long IIlIllIlIIllIIlIlIllllllI;
    public long lIIIlllIIIIllllIlIIIlIIll;
    
    public TimerModule() {
        super("Stopwatch", "Displays a stopwatch on the HUD.");
        this.isTimerRunning = false;
        this.IIlIllIlIIllIIlIlIllllllI = -1L;
        this.lIIIlllIIIIllllIlIIIlIIll = -1L;
        new Setting(this, "Stopwatch Options");
        this.startStopKey = new Setting(this, "Start/Stop Key").setDefault(new KeyBinding(22));
        this.resetOnStart = new Setting(this, "Reset on Start").setDefault(true);
    }
    
    @SubscribeEvent
    public void onKeyPress(final SCKeyEvent event) {
        if (event.getKey() == this.startStopKey.getValue().get(0).getKeyCode() && Keyboard.isKeyDown(this.startStopKey.getValue().get(0).getKeyCode())) {
            if (this.isTimerRunning) {
                this.isTimerRunning = false;
                this.lIIIlllIIIIllllIlIIIlIIll = System.currentTimeMillis();
            }
            else {
                this.isTimerRunning = true;
                this.lIIIlllIIIIllllIlIIIlIIll = -1L;
                if (this.resetOnStart.getBoolean() || this.IIlIllIlIIllIIlIlIllllllI == -1L) {
                    this.IIlIllIlIIllIIlIlIllllllI = System.currentTimeMillis();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final SCUpdateEvent event) {
    }
    
    @Override
    public Object getValue() {
        final long var1 = System.currentTimeMillis();
        final long var2 = ((this.lIIIlllIIIIllllIlIIIlIIll == -1L) ? var1 : this.lIIIlllIIIIllllIlIIIlIIll) - this.IIlIllIlIIllIIlIlIllllllI;
        String var3 = String.valueOf(String.format("%.2f", var2 / 1000.0f)) + "s";
        if (this.IIlIllIlIIllIIlIlIllllllI == -1L && this.lIIIlllIIIIllllIlIIIlIIll == -1L && !this.isTimerRunning) {
            var3 = "0.00s";
        }
        return var3;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_LEFT;
    }
}
