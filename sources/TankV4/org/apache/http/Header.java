package org.apache.http;

public interface Header {
   String getName();

   String getValue();

   HeaderElement[] getElements() throws ParseException;
}
