package gnu.trove.list;

public abstract class TLinkableAdapter implements TLinkable {
   private volatile TLinkable next;
   private volatile TLinkable prev;

   public TLinkable getNext() {
      return this.next;
   }

   public void setNext(TLinkable var1) {
      this.next = var1;
   }

   public TLinkable getPrevious() {
      return this.prev;
   }

   public void setPrevious(TLinkable var1) {
      this.prev = var1;
   }
}
