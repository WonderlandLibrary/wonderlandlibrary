package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;

@Converter(
   autoApply = false
)
public class ContextStackJsonAttributeConverter implements AttributeConverter {
   public String convertToDatabaseColumn(ThreadContext.ContextStack var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            return ContextMapJsonAttributeConverter.OBJECT_MAPPER.writeValueAsString(var1.asList());
         } catch (IOException var3) {
            throw new PersistenceException("Failed to convert stack list to JSON string.", var3);
         }
      }
   }

   public ThreadContext.ContextStack convertToEntityAttribute(String var1) {
      if (Strings.isEmpty(var1)) {
         return null;
      } else {
         List var2;
         try {
            var2 = (List)ContextMapJsonAttributeConverter.OBJECT_MAPPER.readValue(var1, new TypeReference(this) {
               final ContextStackJsonAttributeConverter this$0;

               {
                  this.this$0 = var1;
               }
            });
         } catch (IOException var4) {
            throw new PersistenceException("Failed to convert JSON string to list for stack.", var4);
         }

         DefaultThreadContextStack var3 = new DefaultThreadContextStack(true);
         var3.addAll(var2);
         return var3;
      }
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((ThreadContext.ContextStack)var1);
   }
}
