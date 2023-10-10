package org.apache.http;

public interface StatusLine {
   ProtocolVersion getProtocolVersion();

   int getStatusCode();

   String getReasonPhrase();
}
