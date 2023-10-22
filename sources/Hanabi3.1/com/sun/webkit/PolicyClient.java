package com.sun.webkit;

import java.net.URL;

public interface PolicyClient {
   boolean permitNavigateAction(long var1, URL var3);

   boolean permitRedirectAction(long var1, URL var3);

   boolean permitAcceptResourceAction(long var1, URL var3);

   boolean permitSubmitDataAction(long var1, URL var3, String var4);

   boolean permitResubmitDataAction(long var1, URL var3, String var4);

   boolean permitEnableScriptsAction(long var1, URL var3);

   boolean permitNewPageAction(long var1, URL var3);

   boolean permitClosePageAction(long var1);
}
