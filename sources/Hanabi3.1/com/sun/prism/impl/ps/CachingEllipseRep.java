package com.sun.prism.impl.ps;

public class CachingEllipseRep extends CachingShapeRep {
   CachingShapeRepState createState() {
      return new CachingEllipseRepState();
   }
}
