// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils.shader;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.io.FileOutputStream;
import java.util.Iterator;
import net.augustus.Augustus;
import java.io.File;
import java.util.ArrayList;

public class BackgroundShaderUtil
{
    private final ArrayList<ShaderUtil> shaders;
    private final ArrayList<String> shaderNames;
    private ShaderUtil currentShader;
    
    public BackgroundShaderUtil() {
        this.shaders = new ArrayList<ShaderUtil>();
        this.shaderNames = new ArrayList<String>();
        this.saveResource("shaders/abraxas.frag", "augustus", false);
        this.saveResource("shaders/galaxy.frag", "augustus", false);
        this.saveResource("shaders/galaxy2.frag", "augustus", false);
        this.saveResource("shaders/purplewater.frag", "augustus", false);
        this.saveResource("shaders/liquidbounce.frag", "augustus", false);
        this.saveResource("shaders/redwaterlike.frag", "augustus", false);
        this.saveResource("shaders/sexy.frag", "augustus", false);
        this.saveResource("shaders/metal.frag", "augustus", false);
        this.saveResource("shaders/snowstar.frag", "augustus", false);
        this.saveResource("shaders/spider.frag", "augustus", false);
        this.saveResource("shaders/trinity.frag", "augustus", false);
        this.saveResource("shaders/bluefog.frag", "augustus", false);
        this.saveResource("shaders/cloudtunnel.frag", "augustus", false);
        this.saveResource("ip_changer_fritzbox.vbs", "augustus", false);
        final File folder = new File("augustus/shaders");
        final File[] listFiles;
        final File[] listOfFiles = listFiles = folder.listFiles();
        for (final File file : listFiles) {
            if (file.getName().contains(".")) {
                final String[] s = file.getName().split("\\.");
                if (s.length > 1 && s[1].equals("frag")) {
                    final ShaderUtil shaderUtil = new ShaderUtil();
                    final char[] arr = s[0].toCharArray();
                    arr[0] = Character.toUpperCase(arr[0]);
                    shaderUtil.createBackgroundShader(file.getAbsolutePath(), new String(arr));
                    this.shaders.add(shaderUtil);
                    this.shaderNames.add(shaderUtil.getName());
                }
            }
        }
        final String shaderName = Augustus.getInstance().getConverter().readBackground();
        for (final ShaderUtil shader : this.shaders) {
            if (shader.getName().equalsIgnoreCase(shaderName)) {
                this.currentShader = shader;
                return;
            }
        }
        this.currentShader = this.shaders.get(0);
    }
    
    public void setCurrentShader(final String name) {
        for (final ShaderUtil shaderUtil : this.shaders) {
            if (shaderUtil.getName().equalsIgnoreCase(name)) {
                this.currentShader = shaderUtil;
                Augustus.getInstance().getConverter().saveBackground(shaderUtil);
            }
        }
    }
    
    private void saveResource(String resourcePath, final String dataFolder, final boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        resourcePath = resourcePath.replace('\\', '/');
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in client");
        }
        final File outFile = new File(dataFolder, resourcePath);
        final int lastIndex = resourcePath.lastIndexOf(47);
        final File outDir = new File(dataFolder, resourcePath.substring(0, (lastIndex >= 0) ? lastIndex : 0));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        try {
            if (!outFile.exists() || replace) {
                final OutputStream out = new FileOutputStream(outFile);
                final byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        }
        catch (IOException var10) {
            System.err.println(Level.SEVERE + "Could not save " + outFile.getName() + " to " + outFile);
        }
    }
    
    public ShaderUtil getCurrentShader() {
        return this.currentShader;
    }
    
    public ArrayList<ShaderUtil> getShaders() {
        return this.shaders;
    }
    
    public ArrayList<String> getShaderNames() {
        return this.shaderNames;
    }
}
