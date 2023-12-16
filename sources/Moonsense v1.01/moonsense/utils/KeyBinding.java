// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import com.google.common.collect.Lists;
import java.util.List;

public class KeyBinding
{
    public static final List<KeyBinding> keyBindings;
    private int keyCode;
    private boolean pressed;
    private int pressTime;
    private boolean ignoreClashingKeybinds;
    
    static {
        keyBindings = Lists.newArrayList();
    }
    
    public KeyBinding(final int keyCode) {
        this.ignoreClashingKeybinds = false;
        this.keyCode = keyCode;
        KeyBinding.keyBindings.add(this);
    }
    
    public void onTick() {
        ++this.pressTime;
    }
    
    public void setKeyBindState(final int keyCode, final boolean pressed) {
        this.pressed = pressed;
    }
    
    public boolean isKeyDown() {
        return this.pressed;
    }
    
    public boolean isPressed() {
        if (this.pressTime == 0) {
            return false;
        }
        --this.pressTime;
        return true;
    }
    
    private void unpressKey() {
        this.pressTime = 0;
        this.pressed = false;
    }
    
    public int getKeyCode() {
        return this.keyCode;
    }
    
    public KeyBinding ignoreClashes(final boolean ignore) {
        this.ignoreClashingKeybinds = ignore;
        return this;
    }
    
    public boolean ignoreClashes() {
        return this.ignoreClashingKeybinds;
    }
}
