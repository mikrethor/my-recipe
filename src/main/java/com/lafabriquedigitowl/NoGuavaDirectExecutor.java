package com.lafabriquedigitowl;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;

public class NoGuavaDirectExecutor extends Recipe {
    private static final MethodMatcher DIRECT_EXECUTOR = new MethodMatcher("com.google.common.util.concurrent.MoreExecutors directExecutor()");

    @Override
    public String getDisplayName() {
        return "NoGuavaDirectExecutor";
    }

    @Override
    public String getDescription() {
        return "Don't use `MoreExecutors::directExecutor()`.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaVisitor<ExecutionContext>() {
            private final JavaTemplate template = JavaTemplate.builder(this::getCursor, "Runnable::run")
                    .imports("java.lang.Runnable")
                    .build();

            @Override
            public J visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {
                if (DIRECT_EXECUTOR.matches(method)) {
                    maybeRemoveImport("com.google.common.util.concurrent.MoreExecutors");
                    return template.withTemplate(method, method.getCoordinates().replace(), new Object[]{});
                }
                return super.visitMethodInvocation(method, executionContext);
            }
        };
    }
}
