package io.deephaven.integrations.python;

import io.deephaven.base.log.LogOutput;
import io.deephaven.db.tables.libs.QueryLibrary;
import io.deephaven.db.tables.select.Param;
import io.deephaven.db.tables.select.QueryScope;
import io.deephaven.compilertools.CompilerTools;
import org.jetbrains.annotations.NotNull;
import org.jpy.PyLib;
import org.jpy.PyObject;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Use by calling jpy.get_type('io.deephaven.integrations.python.JavaSessionFromPython').init("path/to/java/output/directory").
 */
public class JavaSessionFromPython {
    public static void init(String directory) {
        // set up PyObject import in default library
        QueryLibrary.importClass(PyObject.class);

        // create a session scope that talks to py's globals() every time it wants to look up something
        QueryScope.setScope(new PythonGlobalsScope());

        // create a directory to write
        File classCacheDirectory = new File(directory);
        CompilerTools.Context syntheticComplerContext = new CompilerTools.Context(classCacheDirectory, JavaSessionFromPython.class.getClassLoader()) {
            {
                addClassSource(getFakeClassDestination());
            }

            @Override public File getFakeClassDestination() {
                return classCacheDirectory;
            }

            @Override public String getClassPath() {
                return classCacheDirectory.getAbsolutePath() + File.pathSeparatorChar + super.getClassPath();
            }
        };
        CompilerTools.setContext(syntheticComplerContext);
    }

    static class PythonGlobalsScope extends QueryScope {
        // as with PythonDeephavenSession.newQueryScope, we don't implement our own locks,
        // but depend on the py GIL

        @Override
        public Set<String> getParamNames() {
            return PyLib.getCurrentGlobals().asDict().keySet().stream().map(PyObject::toString).collect(Collectors.toSet());
        }

        @Override
        public boolean hasParamName(String name) {
            return getParamNames().contains(name);
        }

        @Override
        protected <T> Param<T> createParam(String name) throws MissingVariableException {
            return new Param<>(name, readParamValue(name));
        }

        @Override
        public <T> T readParamValue(String name) throws MissingVariableException {
            return (T) PyLib.getCurrentGlobals().asDict().get(name);
        }

        @Override
        public <T> T readParamValue(String name, T defaultValue) {
            throw new UnsupportedOperationException("readParamValue with default");
        }

        @Override
        public <T> void putParam(String name, T value) {
            throw new UnsupportedOperationException("putParam");
        }

        @Override
        public void putObjectFields(Object object) {
            throw new UnsupportedOperationException("putObjectFields");
        }

        @Override
        public void setQueryName(String queryName) {
            throw new UnsupportedOperationException("setQueryName");
        }

        @Override
        public LogOutput append(@NotNull LogOutput logOutput) {
            return logOutput.append(toString());
        }
    }
}
