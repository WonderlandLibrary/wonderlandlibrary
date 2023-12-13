package com.sun.webkit.plugin;

import java.net.URL;

interface PluginHandler {
   String getName();

   String getFileName();

   String getDescription();

   String[] supportedMIMETypes();

   boolean isSupportedMIMEType(String var1);

   boolean isSupportedPlatform();

   Plugin createPlugin(URL var1, String var2, String[] var3, String[] var4);
}
