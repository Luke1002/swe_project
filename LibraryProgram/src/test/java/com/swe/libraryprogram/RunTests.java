package com.swe.libraryprogram;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;



@Suite
@SelectPackages({"dao", "controller", "view"})
public class RunTests {
}

