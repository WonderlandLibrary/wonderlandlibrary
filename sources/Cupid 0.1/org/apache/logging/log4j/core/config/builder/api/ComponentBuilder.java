package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.Builder;

public interface ComponentBuilder<T extends ComponentBuilder<T>> extends Builder<Component> {
  T addAttribute(String paramString1, String paramString2);
  
  T addAttribute(String paramString, Level paramLevel);
  
  T addAttribute(String paramString, Enum<?> paramEnum);
  
  T addAttribute(String paramString, int paramInt);
  
  T addAttribute(String paramString, boolean paramBoolean);
  
  T addAttribute(String paramString, Object paramObject);
  
  T addComponent(ComponentBuilder<?> paramComponentBuilder);
  
  String getName();
  
  ConfigurationBuilder<? extends Configuration> getBuilder();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\api\ComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */