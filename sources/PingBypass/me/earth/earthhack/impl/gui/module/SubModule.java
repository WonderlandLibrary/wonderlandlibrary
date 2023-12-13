package me.earth.earthhack.impl.gui.module;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;

/**
 * SubModules belong to a parent module.
 * If the parent module is null they are
 * handled just like normal modules.
 */
public class SubModule extends Module
{
    /** The parent module.  */
    private final Module parent;

    public SubModule(String name, Category category, Module parent)
    {
        super(name, category);
        this.parent = parent;
    }

    public Module getParent()
    {
        return parent;
    }

}
