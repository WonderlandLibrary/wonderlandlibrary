/*    */ package org.yaml.snakeyaml.env;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.yaml.snakeyaml.constructor.AbstractConstruct;
/*    */ import org.yaml.snakeyaml.constructor.Constructor;
/*    */ import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;
/*    */ import org.yaml.snakeyaml.nodes.Node;
/*    */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*    */ import org.yaml.snakeyaml.nodes.Tag;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnvScalarConstructor
/*    */   extends Constructor
/*    */ {
/* 34 */   public static final Tag ENV_TAG = new Tag("!ENV");
/* 35 */   public static final Pattern ENV_FORMAT = Pattern.compile("^\\$\\{\\s*((?<name>\\w+)((?<separator>:?(-|\\?))(?<value>\\w+)?)?)\\s*\\}$");
/*    */   
/*    */   public EnvScalarConstructor() {
/* 38 */     this.yamlConstructors.put(ENV_TAG, new ConstructEnv());
/*    */   }
/*    */   
/*    */   private class ConstructEnv extends AbstractConstruct {
/*    */     public Object construct(Node node) {
/* 43 */       String val = EnvScalarConstructor.this.constructScalar((ScalarNode)node);
/* 44 */       Matcher matcher = EnvScalarConstructor.ENV_FORMAT.matcher(val);
/* 45 */       matcher.matches();
/* 46 */       String name = matcher.group("name");
/* 47 */       String value = matcher.group("value");
/* 48 */       String separator = matcher.group("separator");
/* 49 */       return EnvScalarConstructor.this.apply(name, separator, (value != null) ? value : "", EnvScalarConstructor.this.getEnv(name));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private ConstructEnv() {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String apply(String name, String separator, String value, String environment) {
/* 63 */     if (environment != null && !environment.isEmpty()) return environment;
/*    */     
/* 65 */     if (separator != null) {
/*    */       
/* 67 */       if (separator.equals("?") && 
/* 68 */         environment == null) {
/* 69 */         throw new MissingEnvironmentVariableException("Missing mandatory variable " + name + ": " + value);
/*    */       }
/* 71 */       if (separator.equals(":?")) {
/* 72 */         if (environment == null)
/* 73 */           throw new MissingEnvironmentVariableException("Missing mandatory variable " + name + ": " + value); 
/* 74 */         if (environment.isEmpty())
/* 75 */           throw new MissingEnvironmentVariableException("Empty mandatory variable " + name + ": " + value); 
/*    */       } 
/* 77 */       if (separator.startsWith(":")) {
/* 78 */         if (environment == null || environment.isEmpty()) {
/* 79 */           return value;
/*    */         }
/* 81 */       } else if (environment == null) {
/* 82 */         return value;
/*    */       } 
/*    */     } 
/* 85 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEnv(String key) {
/* 95 */     return System.getenv(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\env\EnvScalarConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */