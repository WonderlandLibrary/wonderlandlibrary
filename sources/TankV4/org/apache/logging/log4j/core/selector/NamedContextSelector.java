package org.apache.logging.log4j.core.selector;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public interface NamedContextSelector extends ContextSelector {
   LoggerContext locateContext(String var1, Object var2, URI var3);

   LoggerContext removeContext(String var1);
}
