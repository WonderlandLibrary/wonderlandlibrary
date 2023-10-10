package vestige.ui.click.dropdown.impl;

import lombok.Getter;
import lombok.Setter;
import vestige.module.Category;

import java.util.ArrayList;

@Getter
public class CategoryHolder {

    private final Category category;

    private final ArrayList<ModuleHolder> modules;

    @Setter
    private int x, y;

    @Setter
    private boolean shown, holded;

    public CategoryHolder(Category category, ArrayList<ModuleHolder> modules, int x, int y, boolean shown) {
        this.category = category;
        this.modules = modules;
        this.x = x;
        this.y = y;
        this.shown = shown;
        this.holded = false;
    }

}
