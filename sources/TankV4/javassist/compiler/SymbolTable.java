package javassist.compiler;

import java.util.HashMap;
import javassist.compiler.ast.Declarator;

public final class SymbolTable extends HashMap {
   private SymbolTable parent;

   public SymbolTable() {
      this((SymbolTable)null);
   }

   public SymbolTable(SymbolTable var1) {
      this.parent = var1;
   }

   public SymbolTable getParent() {
      return this.parent;
   }

   public Declarator lookup(String var1) {
      Declarator var2 = (Declarator)this.get(var1);
      return var2 == null && this.parent != null ? this.parent.lookup(var1) : var2;
   }

   public void append(String var1, Declarator var2) {
      this.put(var1, var2);
   }
}
