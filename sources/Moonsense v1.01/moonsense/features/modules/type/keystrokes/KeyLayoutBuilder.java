// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.keystrokes;

import java.util.Arrays;
import moonsense.features.modules.type.keystrokes.keys.Key;
import com.google.common.collect.Lists;
import java.util.List;

public class KeyLayoutBuilder
{
    private float width;
    private float height;
    private int gapSize;
    public final List<Rows> rows;
    
    public KeyLayoutBuilder() {
        this.rows = (List<Rows>)Lists.newArrayList();
    }
    
    public KeyLayoutBuilder addRow(final Key... keys) {
        final double keyWidth = (this.width - this.gapSize * (keys.length - 1)) / keys.length;
        final double height = Arrays.stream(keys).mapToDouble(Key::getHeight).max().orElse(0.0);
        this.rows.add(new Rows(keys, keyWidth, height));
        return this;
    }
    
    public KeyLayoutBuilder build() {
        this.height = (float)(int)(this.rows.stream().mapToDouble(Rows::getHeight).sum() + this.gapSize * (this.rows.size() - 1));
        return this;
    }
    
    public KeyLayoutBuilder setWidth(final float width) {
        this.width = width;
        return this;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public KeyLayoutBuilder setGapSize(final int gapSize) {
        this.gapSize = gapSize;
        return this;
    }
    
    public int getGapSize() {
        return this.gapSize;
    }
}
