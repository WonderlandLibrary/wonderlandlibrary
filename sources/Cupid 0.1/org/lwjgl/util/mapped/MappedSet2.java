package org.lwjgl.util.mapped;

public class MappedSet2 {
  private final MappedObject a;
  
  private final MappedObject b;
  
  public int view;
  
  MappedSet2(MappedObject a, MappedObject b) {
    this.a = a;
    this.b = b;
  }
  
  void view(int view) {
    this.a.setViewAddress(this.a.getViewAddress(view));
    this.b.setViewAddress(this.b.getViewAddress(view));
  }
  
  public void next() {
    this.a.next();
    this.b.next();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\mapped\MappedSet2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */