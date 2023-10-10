package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class BeiderMorseEncoder implements StringEncoder {
   private PhoneticEngine engine;

   public BeiderMorseEncoder() {
      this.engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof String)) {
         throw new EncoderException("BeiderMorseEncoder encode parameter is not of type String");
      } else {
         return this.encode((String)var1);
      }
   }

   public String encode(String var1) throws EncoderException {
      return var1 == null ? null : this.engine.encode(var1);
   }

   public NameType getNameType() {
      return this.engine.getNameType();
   }

   public RuleType getRuleType() {
      return this.engine.getRuleType();
   }

   public boolean isConcat() {
      return this.engine.isConcat();
   }

   public void setConcat(boolean var1) {
      this.engine = new PhoneticEngine(this.engine.getNameType(), this.engine.getRuleType(), var1, this.engine.getMaxPhonemes());
   }

   public void setNameType(NameType var1) {
      this.engine = new PhoneticEngine(var1, this.engine.getRuleType(), this.engine.isConcat(), this.engine.getMaxPhonemes());
   }

   public void setRuleType(RuleType var1) {
      this.engine = new PhoneticEngine(this.engine.getNameType(), var1, this.engine.isConcat(), this.engine.getMaxPhonemes());
   }

   public void setMaxPhonemes(int var1) {
      this.engine = new PhoneticEngine(this.engine.getNameType(), this.engine.getRuleType(), this.engine.isConcat(), var1);
   }
}
