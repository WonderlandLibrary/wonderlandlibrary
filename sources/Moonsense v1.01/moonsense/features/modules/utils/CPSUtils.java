// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.utils;

import com.google.common.collect.Lists;
import org.lwjgl.input.Mouse;
import java.util.Queue;

public class CPSUtils
{
    private final Queue<Long> clicks;
    private boolean wasDown;
    private final Type type;
    
    public CPSUtils click() {
        this.clicks.add(System.currentTimeMillis() + 1000L);
        return this;
    }
    
    public int getCPS() {
        while (!this.clicks.isEmpty() && this.clicks.peek() < System.currentTimeMillis()) {
            this.clicks.remove();
        }
        return this.clicks.size();
    }
    
    public void tick() {
        Mouse.poll();
        final boolean down = Mouse.isButtonDown((int)((this.type != Type.LEFT) ? 1 : 0));
        if (down != this.wasDown && down) {
            this.click();
        }
        this.wasDown = down;
    }
    
    public CPSUtils(final Type type) {
        this.clicks = (Queue<Long>)Lists.newLinkedList();
        this.type = type;
    }
    
    public enum Type
    {
        LEFT("LEFT", 0), 
        RIGHT("RIGHT", 1);
        
        private Type(final String name, final int ordinal) {
        }
    }
}
