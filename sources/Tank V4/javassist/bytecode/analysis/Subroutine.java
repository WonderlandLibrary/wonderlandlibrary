package javassist.bytecode.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subroutine {
   private List callers = new ArrayList();
   private Set access = new HashSet();
   private int start;

   public Subroutine(int var1, int var2) {
      this.start = var1;
      this.callers.add(new Integer(var2));
   }

   public void addCaller(int var1) {
      this.callers.add(new Integer(var1));
   }

   public int start() {
      return this.start;
   }

   public void access(int var1) {
      this.access.add(new Integer(var1));
   }

   public boolean isAccessed(int var1) {
      return this.access.contains(new Integer(var1));
   }

   public Collection accessed() {
      return this.access;
   }

   public Collection callers() {
      return this.callers;
   }

   public String toString() {
      return "start = " + this.start + " callers = " + this.callers.toString();
   }
}
