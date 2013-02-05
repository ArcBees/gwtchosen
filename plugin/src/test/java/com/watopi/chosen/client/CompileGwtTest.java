package com.watopi.chosen.client;

import com.google.gwt.junit.client.GWTTestCase;

public class CompileGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.watopi.chosen.Chosen";
    }

    public void testSandbox() {
        assertTrue(true);
    }
}
