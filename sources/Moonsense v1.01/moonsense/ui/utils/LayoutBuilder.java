// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils;

public class LayoutBuilder
{
    private final int start;
    private final int width;
    private final int gap;
    private final int size;
    private boolean reverse;
    
    public LayoutBuilder(final int start, final int gap, final int size) {
        this(start, -1, gap, size);
    }
    
    public int getCoordinateForIndex(final int index) {
        if (this.width != -1) {
            if (this.reverse) {
                return this.start - (this.width + this.gap * (this.size - 1)) / this.size * index + index * this.gap;
            }
            return this.start + (this.width - this.gap * (this.size - 1)) / this.size * index + index * this.gap;
        }
        else {
            if (this.reverse) {
                return this.start - index * (this.size + this.gap);
            }
            return this.start + index * (this.size + this.gap);
        }
    }
    
    public LayoutBuilder reverse() {
        this.reverse = !this.reverse;
        return this;
    }
    
    public int getSplittedWidth() {
        return (this.width - (this.size - 1) * this.gap) / this.size;
    }
    
    public LayoutBuilder(final int start, final int width, final int gap, final int size) {
        this.reverse = false;
        this.start = start;
        this.width = width;
        this.gap = gap;
        this.size = size;
    }
}
