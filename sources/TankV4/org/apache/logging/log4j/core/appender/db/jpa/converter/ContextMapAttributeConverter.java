package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(
   autoApply = false
)
public class ContextMapAttributeConverter implements AttributeConverter {
   public String convertToDatabaseColumn(Map var1) {
      return var1 == null ? null : var1.toString();
   }

   public Map convertToEntityAttribute(String var1) {
      throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((Map)var1);
   }
}
