package org.apache.logging.log4j.message;

public interface MessageFactory2 extends MessageFactory {
  Message newMessage(CharSequence paramCharSequence);
  
  Message newMessage(String paramString, Object paramObject);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  Message newMessage(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\MessageFactory2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */