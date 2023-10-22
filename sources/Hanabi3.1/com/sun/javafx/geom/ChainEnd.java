package com.sun.javafx.geom;

final class ChainEnd {
   CurveLink head;
   CurveLink tail;
   ChainEnd partner;
   int etag;

   public ChainEnd(CurveLink var1, ChainEnd var2) {
      this.head = var1;
      this.tail = var1;
      this.partner = var2;
      this.etag = var1.getEdgeTag();
   }

   public CurveLink getChain() {
      return this.head;
   }

   public void setOtherEnd(ChainEnd var1) {
      this.partner = var1;
   }

   public ChainEnd getPartner() {
      return this.partner;
   }

   public CurveLink linkTo(ChainEnd var1) {
      if (this.etag != 0 && var1.etag != 0) {
         if (this.etag == var1.etag) {
            throw new InternalError("Linking chains of the same type!");
         } else {
            ChainEnd var2;
            ChainEnd var3;
            if (this.etag == 1) {
               var2 = this;
               var3 = var1;
            } else {
               var2 = var1;
               var3 = this;
            }

            this.etag = 0;
            var1.etag = 0;
            var2.tail.setNext(var3.head);
            var2.tail = var3.tail;
            if (this.partner == var1) {
               return var2.head;
            } else {
               ChainEnd var4 = var3.partner;
               ChainEnd var5 = var2.partner;
               var4.partner = var5;
               var5.partner = var4;
               if (var2.head.getYTop() < var4.head.getYTop()) {
                  var2.tail.setNext(var4.head);
                  var4.head = var2.head;
               } else {
                  var5.tail.setNext(var2.head);
                  var5.tail = var2.tail;
               }

               return null;
            }
         }
      } else {
         throw new InternalError("ChainEnd linked more than once!");
      }
   }

   public void addLink(CurveLink var1) {
      if (this.etag == 1) {
         this.tail.setNext(var1);
         this.tail = var1;
      } else {
         var1.setNext(this.head);
         this.head = var1;
      }

   }

   public double getX() {
      return this.etag == 1 ? this.tail.getXBot() : this.head.getXBot();
   }
}
