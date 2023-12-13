// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.core;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.util.GLSLShaders;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class TitleScreenShader extends Module
{
    public final Value<String> mode;
    public final Value<GLSLShaders> shader;
    public static Value<Integer> fps;
    
    public TitleScreenShader() {
        super("TitleScreenShader", "Displays cool graphics for the main menu background", 0, Category.CORE, false);
        this.mode = this.register(new Value<String>("Mode", this, "Random", new String[] { "Random", "Select" }));
        this.shader = this.register(new Value<GLSLShaders>("Shader", this, GLSLShaders.ICYFIRE, GLSLShaders.values()));
        TitleScreenShader.fps = this.register(new Value<Integer>("FPS", this, 60, 5, 60));
    }
}
