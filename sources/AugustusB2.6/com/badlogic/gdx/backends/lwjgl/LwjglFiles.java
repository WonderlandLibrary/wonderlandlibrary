// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.backends.lwjgl;

import java.io.File;
import com.badlogic.gdx.Files;

public final class LwjglFiles implements Files
{
    public static final String externalPath;
    public static final String localPath;
    
    @Override
    public final String getExternalStoragePath() {
        return LwjglFiles.externalPath;
    }
    
    static {
        externalPath = System.getProperty("user.home") + File.separator;
        localPath = new File("").getAbsolutePath() + File.separator;
    }
}
