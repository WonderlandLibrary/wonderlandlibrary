// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.components;

import com.google.common.collect.Lists;
import moonsense.ui.elements.Element;
import java.util.List;

public abstract class Component
{
    public final List<Element> elements;
    protected final int x;
    protected final int y;
    protected final int width;
    protected final int height;
    public boolean visible;
    public boolean hovered;
    
    public Component(final int x, final int y, final int width, final int height) {
        this.elements = (List<Element>)Lists.newArrayList();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
    }
    
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            this.hovered = this.hovered(mouseX, mouseY);
            this.renderBackground(partialTicks);
            this.elements.forEach(element -> element.render(mouseX, mouseY, partialTicks));
        }
    }
    
    public void renderBackground(final float partialTicks) {
    }
    
    public boolean hovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
