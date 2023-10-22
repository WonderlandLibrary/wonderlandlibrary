package com.sun.webkit;

public interface LoadListenerClient {
   int PAGE_STARTED = 0;
   int PAGE_FINISHED = 1;
   int PAGE_REDIRECTED = 2;
   int PAGE_REPLACED = 3;
   int LOAD_FAILED = 5;
   int LOAD_STOPPED = 6;
   int CONTENT_RECEIVED = 10;
   int TITLE_RECEIVED = 11;
   int ICON_RECEIVED = 12;
   int CONTENTTYPE_RECEIVED = 13;
   int DOCUMENT_AVAILABLE = 14;
   int RESOURCE_STARTED = 20;
   int RESOURCE_REDIRECTED = 21;
   int RESOURCE_FINISHED = 22;
   int RESOURCE_FAILED = 23;
   int PROGRESS_CHANGED = 30;
   int UNKNOWN_HOST = 1;
   int MALFORMED_URL = 2;
   int SSL_HANDSHAKE = 3;
   int CONNECTION_REFUSED = 4;
   int CONNECTION_RESET = 5;
   int NO_ROUTE_TO_HOST = 6;
   int CONNECTION_TIMED_OUT = 7;
   int PERMISSION_DENIED = 8;
   int INVALID_RESPONSE = 9;
   int TOO_MANY_REDIRECTS = 10;
   int FILE_NOT_FOUND = 11;
   int UNKNOWN_ERROR = 99;

   void dispatchLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8);

   void dispatchResourceLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8);
}
