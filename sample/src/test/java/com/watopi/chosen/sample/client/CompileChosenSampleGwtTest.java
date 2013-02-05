package com.watopi.chosen.sample.client;

import com.google.gwt.junit.client.GWTTestCase;

public class CompileChosenSampleGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.watopi.chosen.sample.ChosenSample";
    }

    public void testSandbox() {
        assertTrue(true);
    }
}
