package io.spring.initializr.actuate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActuatorIntegrationTests.class, AllTests.class, AllTests2.class, AllTests3.class, AllTests4.class })
public class General {

}
