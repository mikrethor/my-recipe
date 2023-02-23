package com.lafabriquedigitowl;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class NoGuavaDirectExecutorTest implements RewriteTest {

    // Note, you can define defaults for the RecipeSpec and these defaults will be
    // used for all tests.
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new NoGuavaDirectExecutor()).parser(JavaParser.fromJavaVersion().classpath("guava","junit"));
    }

    // A Java source file that already has a lowercase package name should be left
    // unchanged.
    @Test
    void packageIsAlreadyLowercase() {
        rewriteRun(
                java(
                        """
                                    import java.util.concurrent.Executor;
                                    import com.google.common.util.concurrent.MoreExecutors;

                                    class Test {
                                        Executor executor = MoreExecutors.directExecutor();
                                    }""", """
                                   import java.util.concurrent.Executor;
                                   
                                   class Test {
                                       Executor executor = Runnable::run;
                                   }"""
                )
        );
    }
}
