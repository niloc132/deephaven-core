/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.plot;

import com.google.auto.service.AutoService;
import io.deephaven.engine.util.GroovyDeephavenSession;
import io.deephaven.engine.util.ScriptSession.InitScript;
import javax.inject.Inject;

@AutoService(InitScript.class)
public class PlottingScript implements InitScript {
    @Override
    public String scriptPath() {
        return "groovy/3-plotting.groovy";
    }

    @Override
    public String scriptLanguage() {
        return GroovyDeephavenSession.SCRIPT_TYPE;
    }

    @Override
    public int priority() {
        return 3;
    }
}
