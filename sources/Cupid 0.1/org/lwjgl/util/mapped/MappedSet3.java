package org.lwjgl.util.mapped;

public class MappedSet3 {
  private final MappedObject a;
  
  private final MappedObject b;
  
  private final MappedObject c;
  
  public int view;
  
  MappedSet3(MappedObject a, MappedObject b, MappedObject c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  
  void view(int view) {
    this.a.setViewAddress(this.a.getViewAddress(view));
    this.b.setViewAddress(this.b.getViewAddress(view));
    this.c.setViewAddress(this.c.getViewAddress(view));
  }
  
  public void next() {
    this.a.next();
    this.b.next();
    this.c.next();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\mapped\MappedSet3.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */