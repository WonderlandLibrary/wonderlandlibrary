package com.mojang.brigadier;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandDispatcher {
   public static final String ARGUMENT_SEPARATOR = " ";
   public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
   private static final String USAGE_OPTIONAL_OPEN = "[";
   private static final String USAGE_OPTIONAL_CLOSE = "]";
   private static final String USAGE_REQUIRED_OPEN = "(";
   private static final String USAGE_REQUIRED_CLOSE = ")";
   private static final String USAGE_OR = "|";
   private final RootCommandNode root;
   private final Predicate hasCommand;
   private ResultConsumer consumer;

   public CommandDispatcher(RootCommandNode root) {
      this.hasCommand = new Predicate() {
         public boolean test(CommandNode input) {
            return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand));
         }
      };
      this.consumer = (c, s, r) -> {
      };
      this.root = root;
   }

   public CommandDispatcher() {
      this(new RootCommandNode());
   }

   public LiteralCommandNode register(LiteralArgumentBuilder command) {
      LiteralCommandNode build = command.build();
      this.root.addChild(build);
      return build;
   }

   public void setConsumer(ResultConsumer consumer) {
      this.consumer = consumer;
   }

   public int execute(String input, Object source) throws CommandSyntaxException {
      return this.execute(new StringReader(input), source);
   }

   public int execute(StringReader input, Object source) throws CommandSyntaxException {
      ParseResults parse = this.parse(input, source);
      return this.execute(parse);
   }

   public int execute(ParseResults parse) throws CommandSyntaxException {
      if (parse.getReader().canRead()) {
         if (parse.getExceptions().size() == 1) {
            throw (CommandSyntaxException)parse.getExceptions().values().iterator().next();
         } else if (parse.getContext().getRange().isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
         } else {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
         }
      } else {
         int result = 0;
         int successfulForks = 0;
         boolean forked = false;
         boolean foundCommand = false;
         String command = parse.getReader().getString();
         CommandContext original = parse.getContext().build(command);
         List contexts = Collections.singletonList(original);

         label98:
         for(ArrayList next = null; contexts != null; next = null) {
            int size = ((List)contexts).size();
            Iterator var11 = ((List)contexts).iterator();

            while(true) {
               while(true) {
                  CommandContext context;
                  CommandContext child;
                  label79:
                  do {
                     while(var11.hasNext()) {
                        context = (CommandContext)var11.next();
                        child = context.getChild();
                        if (child != null) {
                           forked |= context.isForked();
                           continue label79;
                        }

                        if (context.getCommand() != null) {
                           foundCommand = true;

                           try {
                              int value = context.getCommand().run(context);
                              result += value;
                              this.consumer.onCommandComplete(context, true, value);
                              ++successfulForks;
                           } catch (CommandSyntaxException var18) {
                              this.consumer.onCommandComplete(context, false, 0);
                              if (!forked) {
                                 throw var18;
                              }
                           }
                        }
                     }

                     contexts = next;
                     continue label98;
                  } while(!child.hasNodes());

                  foundCommand = true;
                  RedirectModifier modifier = context.getRedirectModifier();
                  if (modifier == null) {
                     if (next == null) {
                        next = new ArrayList(1);
                     }

                     next.add(child.copyFor(context.getSource()));
                  } else {
                     try {
                        Collection results = modifier.apply(context);
                        if (!results.isEmpty()) {
                           if (next == null) {
                              next = new ArrayList(results.size());
                           }

                           Iterator var16 = results.iterator();

                           while(var16.hasNext()) {
                              Object source = var16.next();
                              next.add(child.copyFor(source));
                           }
                        }
                     } catch (CommandSyntaxException var19) {
                        this.consumer.onCommandComplete(context, false, 0);
                        if (!forked) {
                           throw var19;
                        }
                     }
                  }
               }
            }
         }

         if (!foundCommand) {
            this.consumer.onCommandComplete(original, false, 0);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
         } else {
            return forked ? successfulForks : result;
         }
      }
   }

   public ParseResults parse(String command, Object source) {
      return this.parse(new StringReader(command), source);
   }

   public ParseResults parse(StringReader command, Object source) {
      CommandContextBuilder context = new CommandContextBuilder(this, source, this.root, command.getCursor());
      return this.parseNodes(this.root, command, context);
   }

   private ParseResults parseNodes(CommandNode node, StringReader originalReader, CommandContextBuilder contextSoFar) {
      Object source = contextSoFar.getSource();
      Map errors = null;
      List potentials = null;
      int cursor = originalReader.getCursor();
      Iterator var8 = node.getRelevantNodes(originalReader).iterator();

      while(true) {
         CommandNode child;
         CommandContextBuilder context;
         StringReader reader;
         while(true) {
            do {
               if (!var8.hasNext()) {
                  if (potentials != null) {
                     if (potentials.size() > 1) {
                        potentials.sort((a, b) -> {
                           if (!a.getReader().canRead() && b.getReader().canRead()) {
                              return -1;
                           } else if (a.getReader().canRead() && !b.getReader().canRead()) {
                              return 1;
                           } else if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                              return -1;
                           } else {
                              return !a.getExceptions().isEmpty() && b.getExceptions().isEmpty() ? 1 : 0;
                           }
                        });
                     }

                     return (ParseResults)potentials.get(0);
                  }

                  return new ParseResults(contextSoFar, originalReader, (Map)(errors == null ? Collections.emptyMap() : errors));
               }

               child = (CommandNode)var8.next();
            } while(!child.canUse(source));

            context = contextSoFar.copy();
            reader = new StringReader(originalReader);

            try {
               try {
                  child.parse(reader, context);
               } catch (RuntimeException var14) {
                  throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, var14.getMessage());
               }

               if (reader.canRead() && reader.peek() != ' ') {
                  throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
               }
               break;
            } catch (CommandSyntaxException var15) {
               if (errors == null) {
                  errors = new LinkedHashMap();
               }

               errors.put(child, var15);
               reader.setCursor(cursor);
            }
         }

         context.withCommand(child.getCommand());
         if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
            reader.skip();
            if (child.getRedirect() != null) {
               CommandContextBuilder childContext = new CommandContextBuilder(this, source, child.getRedirect(), reader.getCursor());
               ParseResults parse = this.parseNodes(child.getRedirect(), reader, childContext);
               context.withChild(parse.getContext());
               return new ParseResults(context, parse.getReader(), parse.getExceptions());
            }

            ParseResults parse = this.parseNodes(child, reader, context);
            if (potentials == null) {
               potentials = new ArrayList(1);
            }

            potentials.add(parse);
         } else {
            if (potentials == null) {
               potentials = new ArrayList(1);
            }

            potentials.add(new ParseResults(context, reader, Collections.emptyMap()));
         }
      }
   }

   public String[] getAllUsage(CommandNode node, Object source, boolean restricted) {
      ArrayList result = new ArrayList();
      this.getAllUsage(node, source, result, "", restricted);
      return (String[])result.toArray(new String[0]);
   }

   private void getAllUsage(CommandNode node, Object source, ArrayList result, String prefix, boolean restricted) {
      if (!restricted || node.canUse(source)) {
         if (node.getCommand() != null) {
            result.add(prefix);
         }

         if (node.getRedirect() != null) {
            String redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
            result.add(prefix.isEmpty() ? node.getUsageText() + " " + redirect : prefix + " " + redirect);
         } else if (!node.getChildren().isEmpty()) {
            Iterator var8 = node.getChildren().iterator();

            while(var8.hasNext()) {
               CommandNode child = (CommandNode)var8.next();
               this.getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : prefix + " " + child.getUsageText(), restricted);
            }
         }

      }
   }

   public Map getSmartUsage(CommandNode node, Object source) {
      Map result = new LinkedHashMap();
      boolean optional = node.getCommand() != null;
      Iterator var5 = node.getChildren().iterator();

      while(var5.hasNext()) {
         CommandNode child = (CommandNode)var5.next();
         String usage = this.getSmartUsage(child, source, optional, false);
         if (usage != null) {
            result.put(child, usage);
         }
      }

      return result;
   }

   private String getSmartUsage(CommandNode node, Object source, boolean optional, boolean deep) {
      if (!node.canUse(source)) {
         return null;
      } else {
         String self = optional ? "[" + node.getUsageText() + "]" : node.getUsageText();
         boolean childOptional = node.getCommand() != null;
         String open = childOptional ? "[" : "(";
         String close = childOptional ? "]" : ")";
         if (!deep) {
            if (node.getRedirect() != null) {
               String redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
               return self + " " + redirect;
            }

            Collection children = (Collection)node.getChildren().stream().filter((c) -> {
               return c.canUse(source);
            }).collect(Collectors.toList());
            if (children.size() == 1) {
               String usage = this.getSmartUsage((CommandNode)children.iterator().next(), source, childOptional, childOptional);
               if (usage != null) {
                  return self + " " + usage;
               }
            } else if (children.size() > 1) {
               Set childUsage = new LinkedHashSet();
               Iterator var11 = children.iterator();

               while(var11.hasNext()) {
                  CommandNode child = (CommandNode)var11.next();
                  String usage = this.getSmartUsage(child, source, childOptional, true);
                  if (usage != null) {
                     childUsage.add(usage);
                  }
               }

               if (childUsage.size() == 1) {
                  String usage = (String)childUsage.iterator().next();
                  return self + " " + (childOptional ? "[" + usage + "]" : usage);
               }

               if (childUsage.size() > 1) {
                  StringBuilder builder = new StringBuilder(open);
                  int count = 0;

                  for(Iterator var20 = children.iterator(); var20.hasNext(); ++count) {
                     CommandNode child = (CommandNode)var20.next();
                     if (count > 0) {
                        builder.append("|");
                     }

                     builder.append(child.getUsageText());
                  }

                  if (count > 0) {
                     builder.append(close);
                     return self + " " + builder;
                  }
               }
            }
         }

         return self;
      }
   }

   public CompletableFuture getCompletionSuggestions(ParseResults parse) {
      return this.getCompletionSuggestions(parse, parse.getReader().getTotalLength());
   }

   public CompletableFuture getCompletionSuggestions(ParseResults parse, int cursor) {
      CommandContextBuilder context = parse.getContext();
      SuggestionContext nodeBeforeCursor = context.findSuggestionContext(cursor);
      CommandNode parent = nodeBeforeCursor.parent;
      int start = Math.min(nodeBeforeCursor.startPos, cursor);
      String fullInput = parse.getReader().getString();
      String truncatedInput = fullInput.substring(0, cursor);
      CompletableFuture[] futures = new CompletableFuture[parent.getChildren().size()];
      int i = 0;

      CompletableFuture future;
      for(Iterator var11 = parent.getChildren().iterator(); var11.hasNext(); futures[i++] = future) {
         CommandNode node = (CommandNode)var11.next();
         future = Suggestions.empty();

         try {
            future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, start));
         } catch (CommandSyntaxException var15) {
         }
      }

      CompletableFuture result = new CompletableFuture();
      CompletableFuture.allOf(futures).thenRun(() -> {
         List suggestions = new ArrayList();
         CompletableFuture[] var4 = futures;
         int var5 = futures.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            CompletableFuture future = var4[var6];
            suggestions.add(future.join());
         }

         result.complete(Suggestions.merge(fullInput, suggestions));
      });
      return result;
   }

   public RootCommandNode getRoot() {
      return this.root;
   }

   public Collection getPath(CommandNode target) {
      List nodes = new ArrayList();
      this.addPaths(this.root, nodes, new ArrayList());
      Iterator var3 = nodes.iterator();

      List list;
      do {
         if (!var3.hasNext()) {
            return Collections.emptyList();
         }

         list = (List)var3.next();
      } while(list.get(list.size() - 1) != target);

      List result = new ArrayList(list.size());
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         CommandNode node = (CommandNode)var6.next();
         if (node != this.root) {
            result.add(node.getName());
         }
      }

      return result;
   }

   public CommandNode findNode(Collection path) {
      CommandNode node = this.root;
      Iterator var3 = path.iterator();

      do {
         if (!var3.hasNext()) {
            return (CommandNode)node;
         }

         String name = (String)var3.next();
         node = ((CommandNode)node).getChild(name);
      } while(node != null);

      return null;
   }

   public void findAmbiguities(AmbiguityConsumer consumer) {
      this.root.findAmbiguities(consumer);
   }

   private void addPaths(CommandNode node, List result, List parents) {
      List current = new ArrayList(parents);
      current.add(node);
      result.add(current);
      Iterator var5 = node.getChildren().iterator();

      while(var5.hasNext()) {
         CommandNode child = (CommandNode)var5.next();
         this.addPaths(child, result, current);
      }

   }
}
