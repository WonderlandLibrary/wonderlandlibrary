package org.apache.logging.log4j.status;

import org.apache.logging.log4j.Level;

public interface StatusListener {
   void log(StatusData var1);

   Level getStatusLevel();
}
