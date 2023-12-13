// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.data;

public class Options
{
    public String name;
    public String selected;
    public String[] options;
    
    public Options(final String name, final String selected, final String[] options) {
        this.name = name;
        this.selected = selected;
        this.options = options;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getSelected() {
        return this.selected;
    }
    
    public void setSelected(final String selected) {
        this.selected = selected;
    }
    
    public String[] getOptions() {
        return this.options;
    }
    
    public void setOptions(final String[] options) {
        this.options = options;
    }
}
