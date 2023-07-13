package io.deephaven.engine.util;

import io.deephaven.jpy.PythonTest;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class SanityTest extends PythonTest {
    @Test
    public void testNoOp() throws IOException, InterruptedException, TimeoutException {
        PythonEvaluatorJpy evaluatorJpy = PythonEvaluatorJpy.withGlobalCopy();
        evaluatorJpy.evalScript("a = 1234");
        Number a = (Number) evaluatorJpy.getScope().getValue("a").get();
        assertEquals(1234, a.intValue());
    }
}
