// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.comboBox;

import java.util.function.Consumer;
import java.util.List;
import java.util.function.Supplier;
import vip.Resolute.ui.click.skeet.component.impl.sub.text.TextComponent;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.ExpandableComponent;
import vip.Resolute.ui.click.skeet.framework.Component;

public final class ComboBoxTextComponent extends Component implements ExpandableComponent, PredicateComponent
{
    private static final int COMBO_BOX_HEIGHT = 10;
    private static final int COMBO_BOX_Y_OFFSET = 1;
    private final TextComponent textComponent;
    private final ComboBoxComponent comboBoxComponent;
    
    public ComboBoxTextComponent(final Component parent, final String name, final Supplier<List<String>> getModes, final Consumer<String> setSelected, final List<String> getMode, final Supplier<Boolean> isVisible, final float x, final float y) {
        super(parent, x, y, 40.166668f, 16.0f);
        this.comboBoxComponent = new ComboBoxComponent(this, 0.0f, 6.0f, this.getWidth(), 10.0f) {
            @Override
            public boolean isVisible() {
                return isVisible.get();
            }
            
            @Override
            public List<String> getMode() {
                return getMode;
            }
            
            @Override
            public void setSelected(final String mode) {
                setSelected.accept(mode);
            }
            
            @Override
            public List<String> getModes() {
                return getModes.get();
            }
        };
        this.textComponent = new TextComponent(this, name, 1.0f, 0.0f);
        this.addChild(this.comboBoxComponent);
        this.addChild(this.textComponent);
    }
    
    public ComboBoxTextComponent(final Component parent, final String name, final Supplier<List<String>> getModes, final Consumer<String> setSelected, final List<String> getMode, final Supplier<Boolean> isVisible) {
        this(parent, name, getModes, setSelected, getMode, isVisible, 0.0f, 0.0f);
    }
    
    @Override
    public boolean isVisible() {
        return this.comboBoxComponent.isVisible();
    }
    
    @Override
    public float getExpandedX() {
        return this.comboBoxComponent.getExpandedX();
    }
    
    @Override
    public float getExpandedY() {
        return this.getY() + this.textComponent.getHeight();
    }
    
    @Override
    public float getExpandedWidth() {
        return this.comboBoxComponent.getExpandedWidth();
    }
    
    @Override
    public float getExpandedHeight() {
        return this.comboBoxComponent.getExpandedHeight();
    }
    
    @Override
    public void setExpanded(final boolean expanded) {
        this.comboBoxComponent.setExpanded(expanded);
    }
    
    @Override
    public boolean isExpanded() {
        return this.comboBoxComponent.isExpanded();
    }
}
