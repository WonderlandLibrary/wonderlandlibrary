package joptsimple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import joptsimple.internal.AbbreviationMap;
import joptsimple.util.KeyValuePair;

public class OptionParser implements OptionDeclarer {
   private final AbbreviationMap recognizedOptions;
   private final Map requiredIf;
   private final Map requiredUnless;
   private OptionParserState state;
   private boolean posixlyCorrect;
   private boolean allowsUnrecognizedOptions;
   private HelpFormatter helpFormatter;

   public OptionParser() {
      this.helpFormatter = new BuiltinHelpFormatter();
      this.recognizedOptions = new AbbreviationMap();
      this.requiredIf = new HashMap();
      this.requiredUnless = new HashMap();
      this.state = OptionParserState.moreOptions(false);
      this.recognize(new NonOptionArgumentSpec());
   }

   public OptionParser(String var1) {
      this();
      (new OptionSpecTokenizer(var1)).configure(this);
   }

   public OptionSpecBuilder accepts(String var1) {
      return this.acceptsAll(Collections.singletonList(var1));
   }

   public OptionSpecBuilder accepts(String var1, String var2) {
      return this.acceptsAll(Collections.singletonList(var1), var2);
   }

   public OptionSpecBuilder acceptsAll(Collection var1) {
      return this.acceptsAll(var1, "");
   }

   public OptionSpecBuilder acceptsAll(Collection var1, String var2) {
      if (var1.isEmpty()) {
         throw new IllegalArgumentException("need at least one option");
      } else {
         ParserRules.ensureLegalOptions(var1);
         return new OptionSpecBuilder(this, var1, var2);
      }
   }

   public NonOptionArgumentSpec nonOptions() {
      NonOptionArgumentSpec var1 = new NonOptionArgumentSpec();
      this.recognize(var1);
      return var1;
   }

   public NonOptionArgumentSpec nonOptions(String var1) {
      NonOptionArgumentSpec var2 = new NonOptionArgumentSpec(var1);
      this.recognize(var2);
      return var2;
   }

   public void posixlyCorrect(boolean var1) {
      this.posixlyCorrect = var1;
      this.state = OptionParserState.moreOptions(var1);
   }

   boolean posixlyCorrect() {
      return this.posixlyCorrect;
   }

   public void allowsUnrecognizedOptions() {
      this.allowsUnrecognizedOptions = true;
   }

   boolean doesAllowsUnrecognizedOptions() {
      return this.allowsUnrecognizedOptions;
   }

   public void recognizeAlternativeLongOptions(boolean var1) {
      if (var1) {
         this.recognize(new AlternativeLongOptionSpec());
      } else {
         this.recognizedOptions.remove(String.valueOf("W"));
      }

   }

   void recognize(AbstractOptionSpec var1) {
      this.recognizedOptions.putAll(var1.options(), var1);
   }

   public void printHelpOn(OutputStream var1) throws IOException {
      this.printHelpOn((Writer)(new OutputStreamWriter(var1)));
   }

   public void printHelpOn(Writer var1) throws IOException {
      var1.write(this.helpFormatter.format(this.recognizedOptions.toJavaUtilMap()));
      var1.flush();
   }

   public void formatHelpWith(HelpFormatter var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.helpFormatter = var1;
      }
   }

   public Map recognizedOptions() {
      return new HashMap(this.recognizedOptions.toJavaUtilMap());
   }

   public OptionSet parse(String... var1) {
      ArgumentList var2 = new ArgumentList(var1);
      OptionSet var3 = new OptionSet(this.recognizedOptions.toJavaUtilMap());
      var3.add((AbstractOptionSpec)this.recognizedOptions.get("[arguments]"));

      while(var2.hasMore()) {
         this.state.handleArgument(this, var2, var3);
      }

      this.reset();
      this.ensureRequiredOptions(var3);
      return var3;
   }

   private void ensureRequiredOptions(OptionSet var1) {
      Collection var2 = this.missingRequiredOptions(var1);
      boolean var3 = this.isHelpOptionPresent(var1);
      if (!var2.isEmpty() && !var3) {
         throw new MissingRequiredOptionException(var2);
      }
   }

   private Collection missingRequiredOptions(OptionSet var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = this.recognizedOptions.toJavaUtilMap().values().iterator();

      while(var3.hasNext()) {
         AbstractOptionSpec var4 = (AbstractOptionSpec)var3.next();
         if (var4.isRequired() && !var1.has((OptionSpec)var4)) {
            var2.addAll(var4.options());
         }
      }

      var3 = this.requiredIf.entrySet().iterator();

      AbstractOptionSpec var5;
      Entry var6;
      while(var3.hasNext()) {
         var6 = (Entry)var3.next();
         var5 = this.specFor((String)((Collection)var6.getKey()).iterator().next());
         if ((Collection)var6.getValue() != false && !var1.has((OptionSpec)var5)) {
            var2.addAll(var5.options());
         }
      }

      var3 = this.requiredUnless.entrySet().iterator();

      while(var3.hasNext()) {
         var6 = (Entry)var3.next();
         var5 = this.specFor((String)((Collection)var6.getKey()).iterator().next());
         if ((Collection)var6.getValue() != false && !var1.has((OptionSpec)var5)) {
            var2.addAll(var5.options());
         }
      }

      return var2;
   }

   private boolean isHelpOptionPresent(OptionSet var1) {
      boolean var2 = false;
      Iterator var3 = this.recognizedOptions.toJavaUtilMap().values().iterator();

      while(var3.hasNext()) {
         AbstractOptionSpec var4 = (AbstractOptionSpec)var3.next();
         if (var4.isForHelp() && var1.has((OptionSpec)var4)) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   void handleLongOptionToken(String var1, ArgumentList var2, OptionSet var3) {
      KeyValuePair var4 = parseLongOptionWithArgument(var1);
      if (!this.isRecognized(var4.key)) {
         throw OptionException.unrecognizedOption(var4.key);
      } else {
         AbstractOptionSpec var5 = this.specFor(var4.key);
         var5.handleOption(this, var2, var3, var4.value);
      }
   }

   void handleShortOptionToken(String var1, ArgumentList var2, OptionSet var3) {
      KeyValuePair var4 = parseShortOptionWithArgument(var1);
      if (this.isRecognized(var4.key)) {
         this.specFor(var4.key).handleOption(this, var2, var3, var4.value);
      } else {
         this.handleShortOptionCluster(var1, var2, var3);
      }

   }

   private void handleShortOptionCluster(String var1, ArgumentList var2, OptionSet var3) {
      char[] var4 = extractShortOptionsFrom(var1);
      this.validateOptionCharacters(var4);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         AbstractOptionSpec var6 = this.specFor(var4[var5]);
         if (var6.acceptsArguments() && var4.length > var5 + 1) {
            String var7 = String.valueOf(var4, var5 + 1, var4.length - 1 - var5);
            var6.handleOption(this, var2, var3, var7);
            break;
         }

         var6.handleOption(this, var2, var3, (String)null);
      }

   }

   void handleNonOptionArgument(String var1, ArgumentList var2, OptionSet var3) {
      this.specFor("[arguments]").handleOption(this, var2, var3, var1);
   }

   void noMoreOptions() {
      this.state = OptionParserState.noMoreOptions();
   }

   boolean looksLikeAnOption(String var1) {
      return ParserRules.isShortOptionToken(var1) || ParserRules.isLongOptionToken(var1);
   }

   boolean isRecognized(String var1) {
      return this.recognizedOptions.contains(var1);
   }

   void requiredIf(Collection var1, String var2) {
      this.requiredIf(var1, (OptionSpec)this.specFor(var2));
   }

   void requiredIf(Collection var1, OptionSpec var2) {
      this.putRequiredOption(var1, var2, this.requiredIf);
   }

   void requiredUnless(Collection var1, String var2) {
      this.requiredUnless(var1, (OptionSpec)this.specFor(var2));
   }

   void requiredUnless(Collection var1, OptionSpec var2) {
      this.putRequiredOption(var1, var2, this.requiredUnless);
   }

   private void putRequiredOption(Collection var1, OptionSpec var2, Map var3) {
      Iterator var4 = var1.iterator();

      AbstractOptionSpec var6;
      do {
         if (!var4.hasNext()) {
            Object var7 = (Set)var3.get(var1);
            if (var7 == null) {
               var7 = new HashSet();
               var3.put(var1, var7);
            }

            ((Set)var7).add(var2);
            return;
         }

         String var5 = (String)var4.next();
         var6 = this.specFor(var5);
      } while(var6 != null);

      throw new UnconfiguredOptionException(var1);
   }

   private AbstractOptionSpec specFor(char var1) {
      return this.specFor(String.valueOf(var1));
   }

   private AbstractOptionSpec specFor(String var1) {
      return (AbstractOptionSpec)this.recognizedOptions.get(var1);
   }

   private void reset() {
      this.state = OptionParserState.moreOptions(this.posixlyCorrect);
   }

   private static char[] extractShortOptionsFrom(String var0) {
      char[] var1 = new char[var0.length() - 1];
      var0.getChars(1, var0.length(), var1, 0);
      return var1;
   }

   private void validateOptionCharacters(char[] var1) {
      char[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         String var6 = String.valueOf(var5);
         if (!this.isRecognized(var6)) {
            throw OptionException.unrecognizedOption(var6);
         }

         if (this.specFor(var6).acceptsArguments()) {
            return;
         }
      }

   }

   private static KeyValuePair parseLongOptionWithArgument(String var0) {
      return KeyValuePair.valueOf(var0.substring(2));
   }

   private static KeyValuePair parseShortOptionWithArgument(String var0) {
      return KeyValuePair.valueOf(var0.substring(1));
   }
}
