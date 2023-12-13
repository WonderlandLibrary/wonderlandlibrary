package org.apache.logging.log4j.message;

public interface MultiformatMessage extends Message {
   String getFormattedMessage(String[] var1);

   String[] getFormats();
}
