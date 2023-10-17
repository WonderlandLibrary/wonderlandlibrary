package de.tired.util.layer;

import de.tired.base.interfaces.IHook;

public abstract class ShaderRenderLayer implements IHook {

    public abstract void renderLayerWBlur();

    public abstract void renderNormalLayer();

}
