package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
   autoApply = false
)
public class ContextMapJsonAttributeConverter implements AttributeConverter {
   static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   public String convertToDatabaseColumn(Map var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            return OBJECT_MAPPER.writeValueAsString(var1);
         } catch (IOException var3) {
            throw new PersistenceException("Failed to convert map to JSON string.", var3);
         }
      }
   }

   public Map convertToEntityAttribute(String var1) {
      if (Strings.isEmpty(var1)) {
         return null;
      } else {
         try {
            return (Map)OBJECT_MAPPER.readValue(var1, new TypeReference(this) {
               final ContextMapJsonAttributeConverter this$0;

               {
                  this.this$0 = var1;
               }
            });
         } catch (IOException var3) {
            throw new PersistenceException("Failed to convert JSON string to map.", var3);
         }
      }
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((Map)var1);
   }
}
