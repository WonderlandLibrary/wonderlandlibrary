// 
// Decompiled by Procyon v0.5.36
// 

package de.wazed.wrapper.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebUtil
{
    private static WebUtil instance;
    
    public WebUtil() {
        WebUtil.instance = this;
    }
    
    public List<String> performRequest(final String url) throws IOException {
        final List<String> tempList = new ArrayList<String>();
        final URL requestUrl = new URL(url);
        final BufferedReader br = new BufferedReader(new InputStreamReader(requestUrl.openStream()));
        String line;
        while ((line = br.readLine()) != null) {
            tempList.add(line);
        }
        return tempList;
    }
    
    public static WebUtil getInstance() {
        return WebUtil.instance;
    }
}
