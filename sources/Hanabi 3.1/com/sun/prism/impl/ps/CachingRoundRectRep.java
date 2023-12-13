package com.sun.prism.impl.ps;

public class CachingRoundRectRep extends CachingShapeRep {
   CachingShapeRepState createState() {
      return new CachingRoundRectRepState();
   }
}
