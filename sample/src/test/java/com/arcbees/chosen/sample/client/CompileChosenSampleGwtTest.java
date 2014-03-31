package com.arcbees.chosen.sample.client;

import com.google.gwt.junit.client.GWTTestCase;

public class CompileChosenSampleGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.arcbees.chosen.sample.client.ChosenSample";
    }

    public void testSandbox() {
        assertTrue(true);
    }
}
