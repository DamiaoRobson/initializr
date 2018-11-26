package io.spring.initializr.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllTests2.class, AllTests3.class, InitializrServiceHttpsTests.class,
		InitializrServiceSmokeTests.class })
public class AllTests {

}
