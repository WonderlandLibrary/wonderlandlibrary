package tech.atani.client.utility.render.shader.container;


import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.render.shader.annotation.Info;
import tech.atani.client.utility.render.shader.data.ShaderRenderType;
import tech.atani.client.utility.render.shader.render.Type;

import java.util.List;

public abstract class ShaderContainer extends ShaderReload implements Methods {

    public String vert, frag;

    public ShaderContainer() {
        final Info info = getClass().getAnnotation(Info.class);
        vert = info.vert();
        frag = info.frag();
    }


    protected abstract void reload();

    public abstract void doRender(final ShaderRenderType type, Type renderType, List<Runnable> runnables);

}
