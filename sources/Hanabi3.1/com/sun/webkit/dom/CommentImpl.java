package com.sun.webkit.dom;

import org.w3c.dom.Comment;

public class CommentImpl extends CharacterDataImpl implements Comment {
   CommentImpl(long var1) {
      super(var1);
   }

   static Comment getImpl(long var0) {
      return (Comment)create(var0);
   }
}
