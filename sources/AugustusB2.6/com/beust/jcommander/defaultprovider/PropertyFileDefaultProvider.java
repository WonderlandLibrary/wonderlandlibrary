// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.defaultprovider;

import java.net.URL;
import java.io.IOException;
import com.beust.jcommander.ParameterException;
import java.util.Properties;
import com.beust.jcommander.IDefaultProvider;

public class PropertyFileDefaultProvider implements IDefaultProvider
{
    public static final String DEFAULT_FILE_NAME = "jcommander.properties";
    private Properties properties;
    
    public PropertyFileDefaultProvider() {
        this.init("jcommander.properties");
    }
    
    public PropertyFileDefaultProvider(final String s) {
        this.init(s);
    }
    
    private void init(final String str) {
        try {
            this.properties = new Properties();
            final URL systemResource;
            if ((systemResource = ClassLoader.getSystemResource(str)) == null) {
                throw new ParameterException("Could not find property file: " + str + " on the class path");
            }
            this.properties.load(systemResource.openStream());
        }
        catch (IOException ex) {
            throw new ParameterException("Could not open property file: " + str);
        }
    }
    
    @Override
    public String getDefaultValueFor(String substring) {
        int n;
        for (n = 0; n < substring.length() && !Character.isLetterOrDigit(substring.charAt(n)); ++n) {}
        substring = substring.substring(n);
        return this.properties.getProperty(substring);
    }
}
