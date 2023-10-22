package com.sun.webkit.network.data;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public final class Handler extends URLStreamHandler {
   protected URLConnection openConnection(URL var1) throws IOException {
      return new DataURLConnection(var1);
   }
}
