package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Iterator;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.ThreadContext;

@Converter(
   autoApply = false
)
public class ContextStackAttributeConverter implements AttributeConverter {
   public String convertToDatabaseColumn(ThreadContext.ContextStack var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder();

         String var4;
         for(Iterator var3 = var1.asList().iterator(); var3.hasNext(); var2.append(var4)) {
            var4 = (String)var3.next();
            if (var2.length() > 0) {
               var2.append('\n');
            }
         }

         return var2.toString();
      }
   }

   public ThreadContext.ContextStack convertToEntityAttribute(String var1) {
      throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((ThreadContext.ContextStack)var1);
   }
}
