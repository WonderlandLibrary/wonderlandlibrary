/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.eclipse.EcjAugments
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.jdt.core.IAnnotatable
 *  org.eclipse.jdt.core.IAnnotation
 *  org.eclipse.jdt.core.IField
 *  org.eclipse.jdt.core.IJavaElement
 *  org.eclipse.jdt.core.IMethod
 *  org.eclipse.jdt.core.IType
 *  org.eclipse.jdt.core.JavaModelException
 *  org.eclipse.jdt.core.dom.ASTNode
 *  org.eclipse.jdt.core.dom.ASTVisitor
 *  org.eclipse.jdt.core.dom.AbstractTypeDeclaration
 *  org.eclipse.jdt.core.dom.Annotation
 *  org.eclipse.jdt.core.dom.CompilationUnit
 *  org.eclipse.jdt.core.dom.MethodDeclaration
 *  org.eclipse.jdt.core.dom.Name
 *  org.eclipse.jdt.core.dom.NormalAnnotation
 *  org.eclipse.jdt.core.dom.QualifiedName
 *  org.eclipse.jdt.core.dom.SimpleName
 *  org.eclipse.jdt.core.dom.SingleMemberAnnotation
 *  org.eclipse.jdt.core.dom.Type
 *  org.eclipse.jdt.core.dom.rewrite.ListRewrite
 *  org.eclipse.jdt.core.search.SearchMatch
 *  org.eclipse.jdt.internal.compiler.ast.ASTNode
 *  org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.Annotation
 *  org.eclipse.jdt.internal.compiler.ast.Expression
 *  org.eclipse.jdt.internal.compiler.ast.FieldDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.LocalDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.TypeDeclaration
 *  org.eclipse.jdt.internal.compiler.lookup.BlockScope
 *  org.eclipse.jdt.internal.compiler.lookup.TypeBinding
 *  org.eclipse.jdt.internal.core.SourceField
 *  org.eclipse.jdt.internal.core.dom.rewrite.NodeRewriteEvent
 *  org.eclipse.jdt.internal.core.dom.rewrite.RewriteEvent
 *  org.eclipse.jdt.internal.core.dom.rewrite.TokenScanner
 *  org.eclipse.jdt.internal.corext.refactoring.SearchResultGroup
 *  org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil
 */
package lombok.launch;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lombok.eclipse.EcjAugments;
import lombok.launch.Main;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.dom.rewrite.NodeRewriteEvent;
import org.eclipse.jdt.internal.core.dom.rewrite.RewriteEvent;
import org.eclipse.jdt.internal.core.dom.rewrite.TokenScanner;
import org.eclipse.jdt.internal.corext.refactoring.SearchResultGroup;
import org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;

final class PatchFixesHider {
    PatchFixesHider() {
    }

    public static final class Delegate {
        private static final Method HANDLE_DELEGATE_FOR_TYPE;
        private static final Method ADD_GENERATED_DELEGATE_METHODS;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchDelegatePortal");
            HANDLE_DELEGATE_FOR_TYPE = Util.findMethod(shadowed, "handleDelegateForType", Object.class);
            ADD_GENERATED_DELEGATE_METHODS = Util.findMethod(shadowed, "addGeneratedDelegateMethods", Object.class, Object.class);
        }

        public static boolean handleDelegateForType(Object classScope) {
            return (Boolean)Util.invokeMethod(HANDLE_DELEGATE_FOR_TYPE, classScope);
        }

        public static Object[] addGeneratedDelegateMethods(Object returnValue, Object javaElement) {
            return (Object[])Util.invokeMethod(ADD_GENERATED_DELEGATE_METHODS, returnValue, javaElement);
        }
    }

    public static final class ExtensionMethod {
        private static final String MESSAGE_SEND_SIG = "org.eclipse.jdt.internal.compiler.ast.MessageSend";
        private static final String TYPE_BINDING_SIG = "org.eclipse.jdt.internal.compiler.lookup.TypeBinding";
        private static final String SCOPE_SIG = "org.eclipse.jdt.internal.compiler.lookup.Scope";
        private static final String BLOCK_SCOPE_SIG = "org.eclipse.jdt.internal.compiler.lookup.BlockScope";
        private static final String TYPE_BINDINGS_SIG = "[Lorg.eclipse.jdt.internal.compiler.lookup.TypeBinding;";
        private static final String PROBLEM_REPORTER_SIG = "org.eclipse.jdt.internal.compiler.problem.ProblemReporter";
        private static final String METHOD_BINDING_SIG = "org.eclipse.jdt.internal.compiler.lookup.MethodBinding";
        private static final String AST_NODE_SIG = "org.eclipse.jdt.internal.compiler.ast.ASTNode";
        private static final Method RESOLVE_TYPE;
        private static final Method ERROR_NO_METHOD_FOR;
        private static final Method INVALID_METHOD;
        private static final Method INVALID_METHOD2;
        private static final Method NON_STATIC_ACCESS_TO_STATIC_METHOD;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchExtensionMethod");
            RESOLVE_TYPE = Util.findMethod(shadowed, "resolveType", TYPE_BINDING_SIG, MESSAGE_SEND_SIG, BLOCK_SCOPE_SIG);
            ERROR_NO_METHOD_FOR = Util.findMethod(shadowed, "errorNoMethodFor", PROBLEM_REPORTER_SIG, MESSAGE_SEND_SIG, TYPE_BINDING_SIG, TYPE_BINDINGS_SIG);
            INVALID_METHOD = Util.findMethod(shadowed, "invalidMethod", PROBLEM_REPORTER_SIG, MESSAGE_SEND_SIG, METHOD_BINDING_SIG);
            INVALID_METHOD2 = Util.findMethod(shadowed, "invalidMethod", PROBLEM_REPORTER_SIG, MESSAGE_SEND_SIG, METHOD_BINDING_SIG, SCOPE_SIG);
            NON_STATIC_ACCESS_TO_STATIC_METHOD = Util.findMethod(shadowed, "nonStaticAccessToStaticMethod", PROBLEM_REPORTER_SIG, AST_NODE_SIG, METHOD_BINDING_SIG, MESSAGE_SEND_SIG);
        }

        public static Object resolveType(Object resolvedType, Object methodCall, Object scope) {
            return Util.invokeMethod(RESOLVE_TYPE, resolvedType, methodCall, scope);
        }

        public static void errorNoMethodFor(Object problemReporter, Object messageSend, Object recType, Object params) {
            Util.invokeMethod(ERROR_NO_METHOD_FOR, problemReporter, messageSend, recType, params);
        }

        public static void invalidMethod(Object problemReporter, Object messageSend, Object method) {
            Util.invokeMethod(INVALID_METHOD, problemReporter, messageSend, method);
        }

        public static void invalidMethod(Object problemReporter, Object messageSend, Object method, Object scope) {
            Util.invokeMethod(INVALID_METHOD2, problemReporter, messageSend, method, scope);
        }

        public static void nonStaticAccessToStaticMethod(Object problemReporter, Object location, Object method, Object messageSend) {
            Util.invokeMethod(NON_STATIC_ACCESS_TO_STATIC_METHOD, problemReporter, location, method, messageSend);
        }
    }

    public static final class Javadoc {
        private static final Method GET_HTML;
        private static final Method PRINT_METHOD;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchJavadoc");
            GET_HTML = Util.findMethod(shadowed, "getHTMLContentFromSource", String.class, Object.class);
            PRINT_METHOD = Util.findMethod(shadowed, "printMethod", AbstractMethodDeclaration.class, Integer.class, StringBuffer.class, TypeDeclaration.class);
        }

        public static String getHTMLContentFromSource(String original, IJavaElement member) {
            return (String)Util.invokeMethod(GET_HTML, original, member);
        }

        public static StringBuffer printMethod(AbstractMethodDeclaration methodDeclaration, int tab, StringBuffer output, TypeDeclaration type) {
            return (StringBuffer)Util.invokeMethod(PRINT_METHOD, methodDeclaration, tab, output, type);
        }
    }

    public static final class LombokDeps {
        public static final Method ADD_LOMBOK_NOTES;
        public static final Method POST_COMPILER_BYTES_STRING;
        public static final Method POST_COMPILER_OUTPUTSTREAM;
        public static final Method POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchFixesShadowLoaded");
            ADD_LOMBOK_NOTES = Util.findMethod(shadowed, "addLombokNotesToEclipseAboutDialog", String.class, String.class);
            POST_COMPILER_BYTES_STRING = Util.findMethod(shadowed, "runPostCompiler", byte[].class, String.class);
            POST_COMPILER_OUTPUTSTREAM = Util.findMethod(shadowed, "runPostCompiler", OutputStream.class);
            POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING = Util.findMethod(shadowed, "runPostCompiler", BufferedOutputStream.class, String.class, String.class);
        }

        public static String addLombokNotesToEclipseAboutDialog(String origReturnValue, String key) {
            try {
                return (String)Util.invokeMethod(ADD_LOMBOK_NOTES, origReturnValue, key);
            }
            catch (Throwable throwable) {
                return origReturnValue;
            }
        }

        public static byte[] runPostCompiler(byte[] bytes, String fileName) {
            return (byte[])Util.invokeMethod(POST_COMPILER_BYTES_STRING, bytes, fileName);
        }

        public static OutputStream runPostCompiler(OutputStream out) throws IOException {
            return (OutputStream)Util.invokeMethod(POST_COMPILER_OUTPUTSTREAM, out);
        }

        public static BufferedOutputStream runPostCompiler(BufferedOutputStream out, String path, String name) throws IOException {
            return (BufferedOutputStream)Util.invokeMethod(POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING, out, path, name);
        }
    }

    public static final class PatchFixes {
        public static final int ALREADY_PROCESSED_FLAG = 0x800000;

        public static boolean isGenerated(ASTNode node) {
            boolean result = false;
            try {
                result = (Boolean)node.getClass().getField("$isGenerated").get(node);
                if (!result && node.getParent() != null && node.getParent() instanceof QualifiedName) {
                    result = PatchFixes.isGenerated(node.getParent());
                }
            }
            catch (Exception exception) {}
            return result;
        }

        public static boolean isGenerated(org.eclipse.jdt.internal.compiler.ast.ASTNode node) {
            boolean result = false;
            try {
                result = node.getClass().getField("$generatedBy").get(node) != null;
            }
            catch (Exception exception) {}
            return result;
        }

        public static boolean isBlockedVisitorAndGenerated(ASTNode node, ASTVisitor visitor) {
            if (visitor == null) {
                return false;
            }
            String className = visitor.getClass().getName();
            if (!(className.startsWith("org.eclipse.jdt.internal.corext.fix") || className.startsWith("org.eclipse.jdt.internal.ui.fix") || className.startsWith("org.eclipse.jdt.ls.core.internal.semantictokens.SemanticTokensVisitor"))) {
                return false;
            }
            if (className.equals("org.eclipse.jdt.internal.corext.fix.VariableDeclarationFixCore$WrittenNamesFinder")) {
                return false;
            }
            return PatchFixes.isGenerated(node);
        }

        public static boolean isListRewriteOnGeneratedNode(ListRewrite rewrite) {
            return PatchFixes.isGenerated(rewrite.getParent());
        }

        public static boolean returnFalse(Object object) {
            return false;
        }

        public static boolean returnTrue(Object object) {
            return true;
        }

        public static List removeGeneratedNodes(List list) {
            try {
                ArrayList realNodes = new ArrayList(list.size());
                for (Object node : list) {
                    if (PatchFixes.isGenerated((ASTNode)node)) continue;
                    realNodes.add(node);
                }
                return realNodes;
            }
            catch (Exception exception) {
                return list;
            }
        }

        public static String getRealMethodDeclarationSource(String original, Object processor, MethodDeclaration declaration) throws Exception {
            if (!PatchFixes.isGenerated((ASTNode)declaration)) {
                return original;
            }
            ArrayList<org.eclipse.jdt.core.dom.Annotation> annotations = new ArrayList<org.eclipse.jdt.core.dom.Annotation>();
            for (Object modifier : declaration.modifiers()) {
                org.eclipse.jdt.core.dom.Annotation annotation;
                String qualifiedAnnotationName;
                if (!(modifier instanceof org.eclipse.jdt.core.dom.Annotation) || "java.lang.Override".equals(qualifiedAnnotationName = (annotation = (org.eclipse.jdt.core.dom.Annotation)modifier).resolveTypeBinding().getQualifiedName()) || "java.lang.SuppressWarnings".equals(qualifiedAnnotationName)) continue;
                annotations.add(annotation);
            }
            StringBuilder signature = new StringBuilder();
            PatchFixes.addAnnotations(annotations, signature);
            if (((Boolean)processor.getClass().getDeclaredField("fPublic").get(processor)).booleanValue()) {
                signature.append("public ");
            }
            if (((Boolean)processor.getClass().getDeclaredField("fAbstract").get(processor)).booleanValue()) {
                signature.append("abstract ");
            }
            signature.append(declaration.getReturnType2().toString()).append(" ").append(declaration.getName().getFullyQualifiedName()).append("(");
            boolean first = true;
            for (Object parameter : declaration.parameters()) {
                if (!first) {
                    signature.append(", ");
                }
                first = false;
                signature.append(parameter);
            }
            signature.append(");");
            return signature.toString();
        }

        public static void addAnnotations(List<org.eclipse.jdt.core.dom.Annotation> annotations, StringBuilder signature) {
            for (org.eclipse.jdt.core.dom.Annotation annotation : annotations) {
                ArrayList<String> values = new ArrayList<String>();
                if (annotation.isSingleMemberAnnotation()) {
                    SingleMemberAnnotation smAnn = (SingleMemberAnnotation)annotation;
                    values.add(smAnn.getValue().toString());
                } else if (annotation.isNormalAnnotation()) {
                    NormalAnnotation normalAnn = (NormalAnnotation)annotation;
                    for (Object value : normalAnn.values()) {
                        values.add(value.toString());
                    }
                }
                signature.append("@").append(annotation.resolveTypeBinding().getQualifiedName());
                if (!values.isEmpty()) {
                    signature.append("(");
                    boolean first = true;
                    for (String string : values) {
                        if (!first) {
                            signature.append(", ");
                        }
                        first = false;
                        signature.append('\"').append(string).append('\"');
                    }
                    signature.append(")");
                }
                signature.append(" ");
            }
        }

        public static MethodDeclaration getRealMethodDeclarationNode(IMethod sourceMethod, CompilationUnit cuUnit) throws JavaModelException {
            MethodDeclaration methodDeclarationNode = ASTNodeSearchUtil.getMethodDeclarationNode((IMethod)sourceMethod, (CompilationUnit)cuUnit);
            if (PatchFixes.isGenerated((ASTNode)methodDeclarationNode)) {
                IType declaringType = sourceMethod.getDeclaringType();
                Stack<IType> typeStack = new Stack<IType>();
                while (declaringType != null) {
                    typeStack.push(declaringType);
                    declaringType = declaringType.getDeclaringType();
                }
                IType rootType = (IType)typeStack.pop();
                AbstractTypeDeclaration typeDeclaration = PatchFixes.findTypeDeclaration(rootType, cuUnit.types());
                while (!typeStack.isEmpty() && typeDeclaration != null) {
                    typeDeclaration = PatchFixes.findTypeDeclaration((IType)typeStack.pop(), typeDeclaration.bodyDeclarations());
                }
                if (typeStack.isEmpty() && typeDeclaration != null) {
                    String methodName = sourceMethod.getElementName();
                    for (Object declaration : typeDeclaration.bodyDeclarations()) {
                        MethodDeclaration methodDeclaration;
                        if (!(declaration instanceof MethodDeclaration) || !(methodDeclaration = (MethodDeclaration)declaration).getName().toString().equals(methodName)) continue;
                        return methodDeclaration;
                    }
                }
            }
            return methodDeclarationNode;
        }

        public static AbstractTypeDeclaration findTypeDeclaration(IType searchType, List<?> nodes) {
            for (Object object : nodes) {
                AbstractTypeDeclaration typeDeclaration;
                if (!(object instanceof AbstractTypeDeclaration) || !(typeDeclaration = (AbstractTypeDeclaration)object).getName().toString().equals(searchType.getElementName())) continue;
                return typeDeclaration;
            }
            return null;
        }

        public static int getSourceEndFixed(int sourceEnd, org.eclipse.jdt.internal.compiler.ast.ASTNode node) throws Exception {
            org.eclipse.jdt.internal.compiler.ast.ASTNode object;
            if (sourceEnd == -1 && (object = (org.eclipse.jdt.internal.compiler.ast.ASTNode)node.getClass().getField("$generatedBy").get(node)) != null) {
                return object.sourceEnd;
            }
            return sourceEnd;
        }

        public static int fixRetrieveStartingCatchPosition(int original, int start) {
            return original == -1 ? start : original;
        }

        public static int fixRetrieveIdentifierEndPosition(int original, int start, int end) {
            if (original == -1) {
                return end;
            }
            if (original < start) {
                return end;
            }
            return original;
        }

        public static int fixRetrieveEllipsisStartPosition(int original, int end) {
            return original == -1 ? end : original;
        }

        public static int fixRetrieveStartBlockPosition(int original, int start) {
            return original == -1 ? start : original;
        }

        public static int fixRetrieveRightBraceOrSemiColonPosition(int original, int end) {
            return original == -1 ? end : original;
        }

        public static int fixRetrieveRightBraceOrSemiColonPosition(int retVal, AbstractMethodDeclaration amd) {
            boolean isGenerated;
            if (retVal != -1 || amd == null) {
                return retVal;
            }
            boolean bl = isGenerated = EcjAugments.ASTNode_generatedBy.get((Object)amd) != null;
            if (isGenerated) {
                return amd.declarationSourceEnd;
            }
            return -1;
        }

        public static int fixRetrieveRightBraceOrSemiColonPosition(int retVal, FieldDeclaration fd) {
            boolean isGenerated;
            if (retVal != -1 || fd == null) {
                return retVal;
            }
            boolean bl = isGenerated = EcjAugments.ASTNode_generatedBy.get((Object)fd) != null;
            if (isGenerated) {
                return fd.declarationSourceEnd;
            }
            return -1;
        }

        public static int fixRetrieveProperRightBracketPosition(int retVal, Type type) {
            if (retVal != -1 || type == null) {
                return retVal;
            }
            if (PatchFixes.isGenerated((ASTNode)type)) {
                return type.getStartPosition() + type.getLength() - 1;
            }
            return -1;
        }

        public static boolean checkBit24(Object node) throws Exception {
            int bits = (Integer)node.getClass().getField("bits").get(node);
            return (bits & 0x800000) != 0;
        }

        public static boolean skipRewritingGeneratedNodes(ASTNode node) throws Exception {
            return (Boolean)node.getClass().getField("$isGenerated").get(node);
        }

        public static void setIsGeneratedFlag(ASTNode domNode, org.eclipse.jdt.internal.compiler.ast.ASTNode internalNode) throws Exception {
            boolean isGenerated;
            if (internalNode == null || domNode == null) {
                return;
            }
            boolean bl = isGenerated = EcjAugments.ASTNode_generatedBy.get((Object)internalNode) != null;
            if (isGenerated) {
                domNode.getClass().getField("$isGenerated").set(domNode, true);
            }
        }

        public static void setIsGeneratedFlagForName(Name name, Object internalNode) throws Exception {
            if (internalNode instanceof org.eclipse.jdt.internal.compiler.ast.ASTNode) {
                boolean isGenerated;
                boolean bl = isGenerated = EcjAugments.ASTNode_generatedBy.get((Object)((org.eclipse.jdt.internal.compiler.ast.ASTNode)internalNode)) != null;
                if (isGenerated) {
                    name.getClass().getField("$isGenerated").set(name, true);
                }
            }
        }

        public static RewriteEvent[] listRewriteHandleGeneratedMethods(RewriteEvent parent) {
            RewriteEvent[] children = parent.getChildren();
            ArrayList<Object> newChildren = new ArrayList<Object>();
            ArrayList<NodeRewriteEvent> modifiedChildren = new ArrayList<NodeRewriteEvent>();
            int i2 = 0;
            while (i2 < children.length) {
                RewriteEvent child = children[i2];
                boolean isGenerated = PatchFixes.isGenerated((ASTNode)child.getOriginalValue());
                if (isGenerated) {
                    boolean isReplacedOrRemoved = child.getChangeKind() == 4 || child.getChangeKind() == 2;
                    boolean convertingFromMethod = child.getOriginalValue() instanceof MethodDeclaration;
                    if (isReplacedOrRemoved && convertingFromMethod && child.getNewValue() != null) {
                        modifiedChildren.add(new NodeRewriteEvent(null, child.getNewValue()));
                    }
                } else {
                    newChildren.add(child);
                }
                ++i2;
            }
            newChildren.addAll(modifiedChildren);
            return newChildren.toArray(new RewriteEvent[0]);
        }

        public static int getTokenEndOffsetFixed(TokenScanner scanner, int token, int startOffset, Object domNode) throws CoreException {
            boolean isGenerated = false;
            try {
                isGenerated = (Boolean)domNode.getClass().getField("$isGenerated").get(domNode);
            }
            catch (Exception exception) {}
            if (isGenerated) {
                return -1;
            }
            return scanner.getTokenEndOffset(token, startOffset);
        }

        public static IMethod[] removeGeneratedMethods(IMethod[] methods) throws Exception {
            ArrayList<IMethod> result = new ArrayList<IMethod>();
            IMethod[] iMethodArray = methods;
            int n2 = methods.length;
            int n3 = 0;
            while (n3 < n2) {
                IMethod m2 = iMethodArray[n3];
                if (m2.getNameRange().getLength() > 0 && !m2.getNameRange().equals(m2.getSourceRange())) {
                    result.add(m2);
                }
                ++n3;
            }
            return result.size() == methods.length ? methods : result.toArray(new IMethod[0]);
        }

        public static SearchMatch[] removeGenerated(SearchMatch[] returnValue) {
            ArrayList<SearchMatch> result = new ArrayList<SearchMatch>();
            int j2 = 0;
            while (j2 < returnValue.length) {
                IField field;
                IAnnotation annotation;
                SearchMatch searchResult = returnValue[j2];
                if (!(searchResult.getElement() instanceof IField) || (annotation = (field = (IField)searchResult.getElement()).getAnnotation("Generated")) == null) {
                    result.add(searchResult);
                }
                ++j2;
            }
            return result.toArray(new SearchMatch[0]);
        }

        public static SearchResultGroup[] createFakeSearchResult(SearchResultGroup[] returnValue, Object processor) throws Exception {
            Field declaredField;
            if ((returnValue == null || returnValue.length == 0) && (declaredField = processor.getClass().getDeclaredField("fField")) != null) {
                declaredField.setAccessible(true);
                SourceField fField = (SourceField)declaredField.get(processor);
                IAnnotation dataAnnotation = fField.getDeclaringType().getAnnotation("Data");
                if (dataAnnotation != null) {
                    return new SearchResultGroup[]{new SearchResultGroup(null, new SearchMatch[1])};
                }
            }
            return returnValue;
        }

        public static SimpleName[] removeGeneratedSimpleNames(SimpleName[] in) throws Exception {
            Field f2 = SimpleName.class.getField("$isGenerated");
            int count = 0;
            int i2 = 0;
            while (i2 < in.length) {
                if (in[i2] == null || !((Boolean)f2.get(in[i2])).booleanValue()) {
                    ++count;
                }
                ++i2;
            }
            if (count == in.length) {
                return in;
            }
            SimpleName[] newSimpleNames = new SimpleName[count];
            count = 0;
            int i3 = 0;
            while (i3 < in.length) {
                if (in[i3] == null || !((Boolean)f2.get(in[i3])).booleanValue()) {
                    newSimpleNames[count++] = in[i3];
                }
                ++i3;
            }
            return newSimpleNames;
        }

        public static Annotation[] convertAnnotations(Annotation[] out, IAnnotatable annotatable) {
            IAnnotation[] in;
            try {
                in = annotatable.getAnnotations();
            }
            catch (Exception exception) {
                return out;
            }
            if (out == null) {
                return null;
            }
            int toWrite = 0;
            int idx = 0;
            while (idx < out.length) {
                String oName = new String(out[idx].type.getLastToken());
                boolean found = false;
                IAnnotation[] iAnnotationArray = in;
                int n2 = in.length;
                int n3 = 0;
                while (n3 < n2) {
                    IAnnotation i2 = iAnnotationArray[n3];
                    String name = i2.getElementName();
                    int li = name.lastIndexOf(46);
                    if (li > -1) {
                        name = name.substring(li + 1);
                    }
                    if (name.equals(oName)) {
                        found = true;
                        break;
                    }
                    ++n3;
                }
                if (!found) {
                    out[idx] = null;
                } else {
                    ++toWrite;
                }
                ++idx;
            }
            Annotation[] replace = out;
            if (toWrite < out.length) {
                replace = new Annotation[toWrite];
                int idx2 = 0;
                int i3 = 0;
                while (i3 < out.length) {
                    if (out[i3] != null) {
                        replace[idx2++] = out[i3];
                    }
                    ++i3;
                }
            }
            return replace;
        }
    }

    public static final class Transform {
        private static Method TRANSFORM;
        private static Method TRANSFORM_SWAPPED;

        private static synchronized void init(ClassLoader prepend) {
            if (TRANSFORM != null) {
                return;
            }
            Main.prependClassLoader(prepend);
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.TransformEclipseAST");
            TRANSFORM = Util.findMethodAnyArgs(shadowed, "transform");
            TRANSFORM_SWAPPED = Util.findMethodAnyArgs(shadowed, "transform_swapped");
        }

        public static void transform(Object parser, Object ast) throws IOException {
            Transform.init(parser.getClass().getClassLoader());
            Util.invokeMethod(TRANSFORM, parser, ast);
        }

        public static void transform_swapped(Object ast, Object parser) throws IOException {
            Transform.init(parser.getClass().getClassLoader());
            Util.invokeMethod(TRANSFORM_SWAPPED, ast, parser);
        }
    }

    public static final class Util {
        private static ClassLoader shadowLoader;

        public static ClassLoader getShadowLoader() {
            if (shadowLoader == null) {
                try {
                    Class.forName("lombok.core.LombokNode");
                    shadowLoader = Util.class.getClassLoader();
                }
                catch (ClassNotFoundException classNotFoundException) {
                    shadowLoader = Main.getShadowClassLoader();
                }
            }
            return shadowLoader;
        }

        public static Class<?> shadowLoadClass(String name) {
            try {
                return Class.forName(name, true, Util.getShadowLoader());
            }
            catch (ClassNotFoundException e2) {
                throw Util.sneakyThrow(e2);
            }
        }

        public static Method findMethod(Class<?> type, String name, Class<?> ... parameterTypes) {
            try {
                return type.getDeclaredMethod(name, parameterTypes);
            }
            catch (NoSuchMethodException e2) {
                throw Util.sneakyThrow(e2);
            }
        }

        public static Method findMethod(Class<?> type, String name, String ... parameterTypes) {
            Method[] methodArray = type.getDeclaredMethods();
            int n2 = methodArray.length;
            int n3 = 0;
            while (n3 < n2) {
                Method m2 = methodArray[n3];
                if (name.equals(m2.getName()) && Util.sameTypes(m2.getParameterTypes(), parameterTypes)) {
                    return m2;
                }
                ++n3;
            }
            throw Util.sneakyThrow(new NoSuchMethodException(String.valueOf(type.getName()) + "::" + name));
        }

        public static Method findMethodAnyArgs(Class<?> type, String name) {
            Method[] methodArray = type.getDeclaredMethods();
            int n2 = methodArray.length;
            int n3 = 0;
            while (n3 < n2) {
                Method m2 = methodArray[n3];
                if (name.equals(m2.getName())) {
                    return m2;
                }
                ++n3;
            }
            throw Util.sneakyThrow(new NoSuchMethodException(String.valueOf(type.getName()) + "::" + name));
        }

        public static Object invokeMethod(Method method, Object ... args) {
            try {
                return method.invoke(null, args);
            }
            catch (IllegalAccessException e2) {
                throw Util.sneakyThrow(e2);
            }
            catch (InvocationTargetException e3) {
                throw Util.sneakyThrow(e3.getCause());
            }
        }

        private static RuntimeException sneakyThrow(Throwable t2) {
            if (t2 == null) {
                throw new NullPointerException("t");
            }
            Util.sneakyThrow0(t2);
            return null;
        }

        private static <T extends Throwable> void sneakyThrow0(Throwable t2) throws T {
            throw t2;
        }

        private static boolean sameTypes(Class<?>[] types, String[] typeNames) {
            if (types.length != typeNames.length) {
                return false;
            }
            int i2 = 0;
            while (i2 < types.length) {
                if (!types[i2].getName().equals(typeNames[i2])) {
                    return false;
                }
                ++i2;
            }
            return true;
        }
    }

    public static final class Val {
        private static final String BLOCK_SCOPE_SIG = "org.eclipse.jdt.internal.compiler.lookup.BlockScope";
        private static final String LOCAL_DECLARATION_SIG = "org.eclipse.jdt.internal.compiler.ast.LocalDeclaration";
        private static final String FOREACH_STATEMENT_SIG = "org.eclipse.jdt.internal.compiler.ast.ForeachStatement";
        private static final Method HANDLE_VAL_FOR_LOCAL_DECLARATION;
        private static final Method HANDLE_VAL_FOR_FOR_EACH;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchVal");
            HANDLE_VAL_FOR_LOCAL_DECLARATION = Util.findMethod(shadowed, "handleValForLocalDeclaration", LOCAL_DECLARATION_SIG, BLOCK_SCOPE_SIG);
            HANDLE_VAL_FOR_FOR_EACH = Util.findMethod(shadowed, "handleValForForEach", FOREACH_STATEMENT_SIG, BLOCK_SCOPE_SIG);
        }

        public static boolean handleValForLocalDeclaration(Object local, Object scope) {
            return (Boolean)Util.invokeMethod(HANDLE_VAL_FOR_LOCAL_DECLARATION, local, scope);
        }

        public static boolean handleValForForEach(Object forEach, Object scope) {
            return (Boolean)Util.invokeMethod(HANDLE_VAL_FOR_FOR_EACH, forEach, scope);
        }

        public static TypeBinding skipResolveInitializerIfAlreadyCalled(Expression expr, BlockScope scope) {
            if (expr.resolvedType != null) {
                return expr.resolvedType;
            }
            try {
                return expr.resolveType(scope);
            }
            catch (NullPointerException nullPointerException) {
                return null;
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return null;
            }
        }

        public static TypeBinding skipResolveInitializerIfAlreadyCalled2(Expression expr, BlockScope scope, LocalDeclaration decl) {
            if (decl != null && LocalDeclaration.class.equals(decl.getClass()) && expr.resolvedType != null) {
                return expr.resolvedType;
            }
            try {
                return expr.resolveType(scope);
            }
            catch (NullPointerException nullPointerException) {
                return null;
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return null;
            }
        }
    }

    public static final class ValPortal {
        private static final Method COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE;
        private static final Method COPY_INITIALIZATION_OF_LOCAL_DECLARATION;
        private static final Method ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT;
        private static final Method ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchValEclipsePortal");
            COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE = Util.findMethod(shadowed, "copyInitializationOfForEachIterable", Object.class);
            COPY_INITIALIZATION_OF_LOCAL_DECLARATION = Util.findMethod(shadowed, "copyInitializationOfLocalDeclaration", Object.class);
            ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT = Util.findMethod(shadowed, "addFinalAndValAnnotationToVariableDeclarationStatement", Object.class, Object.class, Object.class);
            ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION = Util.findMethod(shadowed, "addFinalAndValAnnotationToSingleVariableDeclaration", Object.class, Object.class, Object.class);
        }

        public static void copyInitializationOfForEachIterable(Object parser) {
            Util.invokeMethod(COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE, parser);
        }

        public static void copyInitializationOfLocalDeclaration(Object parser) {
            Util.invokeMethod(COPY_INITIALIZATION_OF_LOCAL_DECLARATION, parser);
        }

        public static void addFinalAndValAnnotationToVariableDeclarationStatement(Object converter, Object out, Object in) {
            Util.invokeMethod(ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT, converter, out, in);
        }

        public static void addFinalAndValAnnotationToSingleVariableDeclaration(Object converter, Object out, Object in) {
            Util.invokeMethod(ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION, converter, out, in);
        }
    }
}

