package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.BeanAdapter;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Expression {
   private static final String NULL_KEYWORD = "null";
   private static final String TRUE_KEYWORD = "true";
   private static final String FALSE_KEYWORD = "false";

   public abstract Object evaluate(Object var1);

   public abstract void update(Object var1, Object var2);

   public abstract boolean isDefined(Object var1);

   public abstract boolean isLValue();

   public List getArguments() {
      ArrayList var1 = new ArrayList();
      this.getArguments(var1);
      return var1;
   }

   protected abstract void getArguments(List var1);

   public static Object get(Object var0, KeyPath var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return get(var0, var1.iterator());
      }
   }

   private static Object get(Object var0, Iterator var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         Object var2;
         if (var1.hasNext()) {
            var2 = get(get(var0, (String)var1.next()), var1);
         } else {
            var2 = var0;
         }

         return var2;
      }
   }

   public static Object get(Object var0, String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         Object var2;
         if (var0 instanceof List) {
            List var3 = (List)var0;
            var2 = var3.get(Integer.parseInt(var1));
         } else if (var0 != null) {
            Object var4;
            if (var0 instanceof Map) {
               var4 = (Map)var0;
            } else {
               var4 = new BeanAdapter(var0);
            }

            var2 = ((Map)var4).get(var1);
         } else {
            var2 = null;
         }

         return var2;
      }
   }

   public static void set(Object var0, KeyPath var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         set(var0, var1.iterator(), var2);
      }
   }

   private static void set(Object var0, Iterator var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (!var1.hasNext()) {
         throw new IllegalArgumentException();
      } else {
         String var3 = (String)var1.next();
         if (var1.hasNext()) {
            set(get(var0, var3), var1, var2);
         } else {
            set(var0, var3, var2);
         }

      }
   }

   public static void set(Object var0, String var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (var0 instanceof List) {
            List var3 = (List)var0;
            var3.set(Integer.parseInt(var1), var2);
         } else {
            if (var0 == null) {
               throw new IllegalArgumentException();
            }

            Object var4;
            if (var0 instanceof Map) {
               var4 = (Map)var0;
            } else {
               var4 = new BeanAdapter(var0);
            }

            ((Map)var4).put(var1, var2);
         }

      }
   }

   public static boolean isDefined(Object var0, KeyPath var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return isDefined(var0, var1.iterator());
      }
   }

   private static boolean isDefined(Object var0, Iterator var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (!var1.hasNext()) {
         throw new IllegalArgumentException();
      } else {
         String var2 = (String)var1.next();
         boolean var3;
         if (var1.hasNext()) {
            var3 = isDefined(get(var0, var2), var1);
         } else {
            var3 = isDefined(var0, var2);
         }

         return var3;
      }
   }

   public static boolean isDefined(Object var0, String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         boolean var2;
         if (var0 instanceof List) {
            List var3 = (List)var0;
            var2 = Integer.parseInt(var1) < var3.size();
         } else if (var0 != null) {
            Object var4;
            if (var0 instanceof Map) {
               var4 = (Map)var0;
            } else {
               var4 = new BeanAdapter(var0);
            }

            var2 = ((Map)var4).containsKey(var1);
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public static BinaryExpression add(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         Object var2;
         if (!(var0x instanceof String) && !(var1x instanceof String)) {
            Number var3 = (Number)var0x;
            Number var4 = (Number)var1x;
            if (!(var3 instanceof Double) && !(var4 instanceof Double)) {
               if (!(var3 instanceof Float) && !(var4 instanceof Float)) {
                  if (!(var3 instanceof Long) && !(var4 instanceof Long)) {
                     if (!(var3 instanceof Integer) && !(var4 instanceof Integer)) {
                        if (!(var3 instanceof Short) && !(var4 instanceof Short)) {
                           if (!(var3 instanceof Byte) && !(var4 instanceof Byte)) {
                              throw new UnsupportedOperationException();
                           }

                           var2 = var3.byteValue() + var4.byteValue();
                        } else {
                           var2 = var3.shortValue() + var4.shortValue();
                        }
                     } else {
                        var2 = var3.intValue() + var4.intValue();
                     }
                  } else {
                     var2 = var3.longValue() + var4.longValue();
                  }
               } else {
                  var2 = var3.floatValue() + var4.floatValue();
               }
            } else {
               var2 = var3.doubleValue() + var4.doubleValue();
            }
         } else {
            var2 = var0x.toString().concat(var1x.toString());
         }

         return var2;
      });
   }

   public static BinaryExpression add(Expression var0, Object var1) {
      return add((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression add(Object var0, Expression var1) {
      return add((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression add(Object var0, Object var1) {
      return add((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression subtract(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         Object var2;
         if (!(var0x instanceof Double) && !(var1x instanceof Double)) {
            if (!(var0x instanceof Float) && !(var1x instanceof Float)) {
               if (!(var0x instanceof Long) && !(var1x instanceof Long)) {
                  if (!(var0x instanceof Integer) && !(var1x instanceof Integer)) {
                     if (!(var0x instanceof Short) && !(var1x instanceof Short)) {
                        if (!(var0x instanceof Byte) && !(var1x instanceof Byte)) {
                           throw new UnsupportedOperationException();
                        }

                        var2 = var0x.byteValue() - var1x.byteValue();
                     } else {
                        var2 = var0x.shortValue() - var1x.shortValue();
                     }
                  } else {
                     var2 = var0x.intValue() - var1x.intValue();
                  }
               } else {
                  var2 = var0x.longValue() - var1x.longValue();
               }
            } else {
               var2 = var0x.floatValue() - var1x.floatValue();
            }
         } else {
            var2 = var0x.doubleValue() - var1x.doubleValue();
         }

         return (Number)var2;
      });
   }

   public static BinaryExpression subtract(Expression var0, Number var1) {
      return subtract((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression subtract(Number var0, Expression var1) {
      return subtract((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression subtract(Number var0, Number var1) {
      return subtract((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression multiply(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         Object var2;
         if (!(var0x instanceof Double) && !(var1x instanceof Double)) {
            if (!(var0x instanceof Float) && !(var1x instanceof Float)) {
               if (!(var0x instanceof Long) && !(var1x instanceof Long)) {
                  if (!(var0x instanceof Integer) && !(var1x instanceof Integer)) {
                     if (!(var0x instanceof Short) && !(var1x instanceof Short)) {
                        if (!(var0x instanceof Byte) && !(var1x instanceof Byte)) {
                           throw new UnsupportedOperationException();
                        }

                        var2 = var0x.byteValue() * var1x.byteValue();
                     } else {
                        var2 = var0x.shortValue() * var1x.shortValue();
                     }
                  } else {
                     var2 = var0x.intValue() * var1x.intValue();
                  }
               } else {
                  var2 = var0x.longValue() * var1x.longValue();
               }
            } else {
               var2 = var0x.floatValue() * var1x.floatValue();
            }
         } else {
            var2 = var0x.doubleValue() * var1x.doubleValue();
         }

         return (Number)var2;
      });
   }

   public static BinaryExpression multiply(Expression var0, Number var1) {
      return multiply((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression multiply(Number var0, Expression var1) {
      return multiply((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression multiply(Number var0, Number var1) {
      return multiply((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression divide(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         Object var2;
         if (!(var0x instanceof Double) && !(var1x instanceof Double)) {
            if (!(var0x instanceof Float) && !(var1x instanceof Float)) {
               if (!(var0x instanceof Long) && !(var1x instanceof Long)) {
                  if (!(var0x instanceof Integer) && !(var1x instanceof Integer)) {
                     if (!(var0x instanceof Short) && !(var1x instanceof Short)) {
                        if (!(var0x instanceof Byte) && !(var1x instanceof Byte)) {
                           throw new UnsupportedOperationException();
                        }

                        var2 = var0x.byteValue() / var1x.byteValue();
                     } else {
                        var2 = var0x.shortValue() / var1x.shortValue();
                     }
                  } else {
                     var2 = var0x.intValue() / var1x.intValue();
                  }
               } else {
                  var2 = var0x.longValue() / var1x.longValue();
               }
            } else {
               var2 = var0x.floatValue() / var1x.floatValue();
            }
         } else {
            var2 = var0x.doubleValue() / var1x.doubleValue();
         }

         return (Number)var2;
      });
   }

   public static BinaryExpression divide(Expression var0, Number var1) {
      return divide((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression divide(Number var0, Expression var1) {
      return divide((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression divide(Number var0, Number var1) {
      return divide((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression modulo(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         Object var2;
         if (!(var0x instanceof Double) && !(var1x instanceof Double)) {
            if (!(var0x instanceof Float) && !(var1x instanceof Float)) {
               if (!(var0x instanceof Long) && !(var1x instanceof Long)) {
                  if (!(var0x instanceof Integer) && !(var1x instanceof Integer)) {
                     if (!(var0x instanceof Short) && !(var1x instanceof Short)) {
                        if (!(var0x instanceof Byte) && !(var1x instanceof Byte)) {
                           throw new UnsupportedOperationException();
                        }

                        var2 = var0x.byteValue() % var1x.byteValue();
                     } else {
                        var2 = var0x.shortValue() % var1x.shortValue();
                     }
                  } else {
                     var2 = var0x.intValue() % var1x.intValue();
                  }
               } else {
                  var2 = var0x.longValue() % var1x.longValue();
               }
            } else {
               var2 = var0x.floatValue() % var1x.floatValue();
            }
         } else {
            var2 = var0x.doubleValue() % var1x.doubleValue();
         }

         return (Number)var2;
      });
   }

   public static BinaryExpression modulo(Expression var0, Number var1) {
      return modulo((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression modulo(Number var0, Expression var1) {
      return modulo((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression modulo(Number var0, Number var1) {
      return modulo((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression equalTo(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x.compareTo(var1x) == 0;
      });
   }

   public static BinaryExpression equalTo(Expression var0, Object var1) {
      return equalTo((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression equalTo(Object var0, Expression var1) {
      return equalTo((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression equalTo(Object var0, Object var1) {
      return equalTo((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression notEqualTo(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x.compareTo(var1x) != 0;
      });
   }

   public static BinaryExpression notEqualTo(Expression var0, Object var1) {
      return notEqualTo((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression notEqualTo(Object var0, Expression var1) {
      return notEqualTo((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression notEqualTo(Object var0, Object var1) {
      return notEqualTo((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression greaterThan(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x.compareTo(var1x) > 0;
      });
   }

   public static BinaryExpression greaterThan(Expression var0, Object var1) {
      return greaterThan((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression greaterThan(Object var0, Expression var1) {
      return greaterThan((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression greaterThan(Object var0, Object var1) {
      return greaterThan((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression greaterThanOrEqualTo(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x.compareTo(var1x) >= 0;
      });
   }

   public static BinaryExpression greaterThanOrEqualTo(Expression var0, Object var1) {
      return greaterThanOrEqualTo((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression greaterThanOrEqualTo(Object var0, Expression var1) {
      return greaterThanOrEqualTo((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression greaterThanOrEqualTo(Object var0, Object var1) {
      return greaterThanOrEqualTo((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression lessThan(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x.compareTo(var1x) < 0;
      });
   }

   public static BinaryExpression lessThan(Expression var0, Object var1) {
      return lessThan((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression lessThan(Object var0, Expression var1) {
      return lessThan((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression lessThan(Object var0, Object var1) {
      return lessThan((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression lessThanOrEqualTo(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x.compareTo(var1x) <= 0;
      });
   }

   public static BinaryExpression lessThanOrEqualTo(Expression var0, Object var1) {
      return lessThanOrEqualTo((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression lessThanOrEqualTo(Object var0, Expression var1) {
      return lessThanOrEqualTo((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression lessThanOrEqualTo(Object var0, Object var1) {
      return lessThanOrEqualTo((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression and(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x && var1x;
      });
   }

   public static BinaryExpression and(Expression var0, Boolean var1) {
      return and((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression and(Boolean var0, Expression var1) {
      return and((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression and(Boolean var0, Boolean var1) {
      return and((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression or(Expression var0, Expression var1) {
      return new BinaryExpression(var0, var1, (var0x, var1x) -> {
         return var0x || var1x;
      });
   }

   public static BinaryExpression or(Expression var0, Boolean var1) {
      return or((Expression)var0, (Expression)(new LiteralExpression(var1)));
   }

   public static BinaryExpression or(Boolean var0, Expression var1) {
      return or((Expression)(new LiteralExpression(var0)), (Expression)var1);
   }

   public static BinaryExpression or(Boolean var0, Boolean var1) {
      return or((Expression)(new LiteralExpression(var0)), (Expression)(new LiteralExpression(var1)));
   }

   public static UnaryExpression negate(Expression var0) {
      return new UnaryExpression(var0, (var0x) -> {
         Class var1 = var0x.getClass();
         if (var1 == Byte.class) {
            return -var0x.byteValue();
         } else if (var1 == Short.class) {
            return -var0x.shortValue();
         } else if (var1 == Integer.class) {
            return -var0x.intValue();
         } else if (var1 == Long.class) {
            return -var0x.longValue();
         } else if (var1 == Float.class) {
            return -var0x.floatValue();
         } else if (var1 == Double.class) {
            return -var0x.doubleValue();
         } else {
            throw new UnsupportedOperationException();
         }
      });
   }

   public static UnaryExpression negate(Number var0) {
      return negate((Expression)(new LiteralExpression(var0)));
   }

   public static UnaryExpression not(Expression var0) {
      return new UnaryExpression(var0, (var0x) -> {
         return !var0x;
      });
   }

   public static UnaryExpression not(Boolean var0) {
      return not((Expression)(new LiteralExpression(var0)));
   }

   public static Expression valueOf(String var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         Parser var1 = new Parser();

         try {
            Expression var2 = var1.parse(new StringReader(var0));
            return var2;
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   private static class Parser {
      private int c;
      private char[] pushbackBuffer;
      private static final int PUSHBACK_BUFFER_SIZE = 6;

      private Parser() {
         this.c = -1;
         this.pushbackBuffer = new char[6];
      }

      public Expression parse(Reader var1) throws IOException {
         LinkedList var2 = this.tokenize(new PushbackReader(var1, 6));
         LinkedList var3 = new LinkedList();

         Object var6;
         for(Iterator var4 = var2.iterator(); var4.hasNext(); var3.push(var6)) {
            Token var5 = (Token)var4.next();
            Operator var7;
            Expression var8;
            switch (var5.type) {
               case LITERAL:
                  var6 = new LiteralExpression(var5.value);
                  break;
               case VARIABLE:
                  var6 = new VariableExpression((KeyPath)var5.value);
                  break;
               case FUNCTION:
                  var6 = null;
                  break;
               case UNARY_OPERATOR:
                  var7 = (Operator)var5.value;
                  var8 = (Expression)var3.pop();
                  switch (var7) {
                     case NEGATE:
                        var6 = Expression.negate(var8);
                        continue;
                     case NOT:
                        var6 = Expression.not(var8);
                        continue;
                     default:
                        throw new UnsupportedOperationException();
                  }
               case BINARY_OPERATOR:
                  var7 = (Operator)var5.value;
                  var8 = (Expression)var3.pop();
                  Expression var9 = (Expression)var3.pop();
                  switch (var7) {
                     case ADD:
                        var6 = Expression.add(var9, var8);
                        continue;
                     case SUBTRACT:
                        var6 = Expression.subtract(var9, var8);
                        continue;
                     case MULTIPLY:
                        var6 = Expression.multiply(var9, var8);
                        continue;
                     case DIVIDE:
                        var6 = Expression.divide(var9, var8);
                        continue;
                     case MODULO:
                        var6 = Expression.modulo(var9, var8);
                        continue;
                     case GREATER_THAN:
                        var6 = Expression.greaterThan(var9, var8);
                        continue;
                     case GREATER_THAN_OR_EQUAL_TO:
                        var6 = Expression.greaterThanOrEqualTo(var9, var8);
                        continue;
                     case LESS_THAN:
                        var6 = Expression.lessThan(var9, var8);
                        continue;
                     case LESS_THAN_OR_EQUAL_TO:
                        var6 = Expression.lessThanOrEqualTo(var9, var8);
                        continue;
                     case EQUAL_TO:
                        var6 = Expression.equalTo(var9, var8);
                        continue;
                     case NOT_EQUAL_TO:
                        var6 = Expression.notEqualTo(var9, var8);
                        continue;
                     case AND:
                        var6 = Expression.and(var9, var8);
                        continue;
                     case OR:
                        var6 = Expression.or(var9, var8);
                        continue;
                     default:
                        throw new UnsupportedOperationException();
                  }
               default:
                  throw new UnsupportedOperationException();
            }
         }

         if (var3.size() != 1) {
            throw new IllegalArgumentException("Invalid expression.");
         } else {
            return (Expression)var3.peek();
         }
      }

      private LinkedList tokenize(PushbackReader var1) throws IOException {
         LinkedList var2 = new LinkedList();
         LinkedList var3 = new LinkedList();
         this.c = var1.read();
         boolean var4 = true;

         while(true) {
            do {
               if (this.c == -1) {
                  while(!var3.isEmpty()) {
                     var2.add(var3.pop());
                  }

                  return var2;
               }

               while(this.c != -1 && Character.isWhitespace(this.c)) {
                  this.c = var1.read();
               }
            } while(this.c == -1);

            Token var5;
            if (this.c == 110) {
               if (this.readKeyword(var1, "null")) {
                  var5 = new Token(Expression.Parser.TokenType.LITERAL, (Object)null);
               } else {
                  var5 = new Token(Expression.Parser.TokenType.VARIABLE, KeyPath.parse(var1));
                  this.c = var1.read();
               }
            } else {
               StringBuilder var6;
               if (this.c != 34 && this.c != 39) {
                  if (!Character.isDigit(this.c)) {
                     if (this.c == 116) {
                        if (this.readKeyword(var1, "true")) {
                           var5 = new Token(Expression.Parser.TokenType.LITERAL, true);
                        } else {
                           var5 = new Token(Expression.Parser.TokenType.VARIABLE, KeyPath.parse(var1));
                           this.c = var1.read();
                        }
                     } else if (this.c == 102) {
                        if (this.readKeyword(var1, "false")) {
                           var5 = new Token(Expression.Parser.TokenType.LITERAL, false);
                        } else {
                           var5 = new Token(Expression.Parser.TokenType.VARIABLE, KeyPath.parse(var1));
                           this.c = var1.read();
                        }
                     } else if (Character.isJavaIdentifierStart(this.c)) {
                        var1.unread(this.c);
                        var5 = new Token(Expression.Parser.TokenType.VARIABLE, KeyPath.parse(var1));
                        this.c = var1.read();
                     } else {
                        boolean var10 = true;
                        if (var4) {
                           switch (this.c) {
                              case 33:
                                 var5 = new Token(Expression.Parser.TokenType.UNARY_OPERATOR, Operator.NOT);
                                 break;
                              case 40:
                                 var5 = new Token(Expression.Parser.TokenType.BEGIN_GROUP, (Object)null);
                                 break;
                              case 45:
                                 var5 = new Token(Expression.Parser.TokenType.UNARY_OPERATOR, Operator.NEGATE);
                                 break;
                              default:
                                 throw new IllegalArgumentException("Unexpected character in expression.");
                           }
                        } else {
                           switch (this.c) {
                              case 33:
                                 this.c = var1.read();
                                 if (this.c != 61) {
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                                 }

                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.NOT_EQUAL_TO);
                                 break;
                              case 37:
                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.MODULO);
                                 break;
                              case 38:
                                 this.c = var1.read();
                                 if (this.c != 38) {
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                                 }

                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.AND);
                                 break;
                              case 41:
                                 var5 = new Token(Expression.Parser.TokenType.END_GROUP, (Object)null);
                                 break;
                              case 42:
                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.MULTIPLY);
                                 break;
                              case 43:
                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.ADD);
                                 break;
                              case 45:
                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.SUBTRACT);
                                 break;
                              case 47:
                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.DIVIDE);
                                 break;
                              case 60:
                                 this.c = var1.read();
                                 if (this.c == 61) {
                                    var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.LESS_THAN_OR_EQUAL_TO);
                                 } else {
                                    var10 = false;
                                    var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.LESS_THAN);
                                 }
                                 break;
                              case 61:
                                 this.c = var1.read();
                                 if (this.c != 61) {
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                                 }

                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.EQUAL_TO);
                                 break;
                              case 62:
                                 this.c = var1.read();
                                 if (this.c == 61) {
                                    var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.GREATER_THAN_OR_EQUAL_TO);
                                 } else {
                                    var10 = false;
                                    var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.GREATER_THAN);
                                 }
                                 break;
                              case 124:
                                 this.c = var1.read();
                                 if (this.c != 124) {
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                                 }

                                 var5 = new Token(Expression.Parser.TokenType.BINARY_OPERATOR, Operator.OR);
                                 break;
                              default:
                                 throw new IllegalArgumentException("Unexpected character in expression.");
                           }
                        }

                        if (var10) {
                           this.c = var1.read();
                        }
                     }
                  } else {
                     var6 = new StringBuilder();

                     boolean var11;
                     for(var11 = true; this.c != -1 && (Character.isDigit(this.c) || this.c == 46 || this.c == 101 || this.c == 69); this.c = var1.read()) {
                        var6.append((char)this.c);
                        var11 &= this.c != 46;
                     }

                     Object var13;
                     if (var11) {
                        var13 = Long.parseLong(var6.toString());
                     } else {
                        var13 = Double.parseDouble(var6.toString());
                     }

                     var5 = new Token(Expression.Parser.TokenType.LITERAL, var13);
                  }
               } else {
                  var6 = new StringBuilder();
                  int var7 = this.c;

                  for(this.c = var1.read(); this.c != -1 && this.c != var7; this.c = var1.read()) {
                     if (!Character.isISOControl(this.c)) {
                        if (this.c == 92) {
                           this.c = var1.read();
                           if (this.c == 98) {
                              this.c = 8;
                           } else if (this.c == 102) {
                              this.c = 12;
                           } else if (this.c == 110) {
                              this.c = 10;
                           } else if (this.c == 114) {
                              this.c = 13;
                           } else if (this.c == 116) {
                              this.c = 9;
                           } else if (this.c != 117) {
                              if (this.c != 92 && this.c != 47 && this.c != 34 && this.c != 39 && this.c != var7) {
                                 throw new IllegalArgumentException("Unsupported escape sequence.");
                              }
                           } else {
                              StringBuilder var8 = new StringBuilder();

                              while(var8.length() < 4) {
                                 this.c = var1.read();
                                 var8.append((char)this.c);
                              }

                              String var9 = var8.toString();
                              this.c = (char)Integer.parseInt(var9, 16);
                           }
                        }

                        var6.append((char)this.c);
                     }
                  }

                  if (this.c != var7) {
                     throw new IllegalArgumentException("Unterminated string.");
                  }

                  this.c = var1.read();
                  var5 = new Token(Expression.Parser.TokenType.LITERAL, var6.toString());
               }
            }

            switch (var5.type) {
               case LITERAL:
               case VARIABLE:
                  var2.add(var5);
                  break;
               case FUNCTION:
               default:
                  throw new UnsupportedOperationException();
               case UNARY_OPERATOR:
               case BINARY_OPERATOR:
                  int var14 = ((Operator)var5.value).getPriority();

                  while(!var3.isEmpty() && ((Token)var3.peek()).type != Expression.Parser.TokenType.BEGIN_GROUP && ((Operator)((Token)var3.peek()).value).getPriority() >= var14 && ((Operator)((Token)var3.peek()).value).getPriority() != 6) {
                     var2.add(var3.pop());
                  }

                  var3.push(var5);
                  break;
               case BEGIN_GROUP:
                  var3.push(var5);
                  break;
               case END_GROUP:
                  for(Token var12 = (Token)var3.pop(); var12.type != Expression.Parser.TokenType.BEGIN_GROUP; var12 = (Token)var3.pop()) {
                     var2.add(var12);
                  }
            }

            var4 = var5.type != Expression.Parser.TokenType.LITERAL && var5.type != Expression.Parser.TokenType.VARIABLE && var5.type != Expression.Parser.TokenType.END_GROUP;
         }
      }

      private boolean readKeyword(PushbackReader var1, String var2) throws IOException {
         int var3 = var2.length();

         int var4;
         for(var4 = 0; this.c != -1 && var4 < var3; ++var4) {
            this.pushbackBuffer[var4] = (char)this.c;
            if (var2.charAt(var4) != this.c) {
               break;
            }

            this.c = var1.read();
         }

         boolean var5;
         if (var4 < var3) {
            var1.unread(this.pushbackBuffer, 0, var4 + 1);
            var5 = false;
         } else {
            var5 = true;
         }

         return var5;
      }

      // $FF: synthetic method
      Parser(Object var1) {
         this();
      }

      public static enum TokenType {
         LITERAL,
         VARIABLE,
         FUNCTION,
         UNARY_OPERATOR,
         BINARY_OPERATOR,
         BEGIN_GROUP,
         END_GROUP;
      }

      public static class Token {
         public final TokenType type;
         public final Object value;

         public Token(TokenType var1, Object var2) {
            this.type = var1;
            this.value = var2;
         }

         public String toString() {
            return this.value.toString();
         }
      }
   }
}
