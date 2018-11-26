package io.spring.initializr.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.util.AgentTests;
import io.spring.initializr.util.VersionParserTests;
import io.spring.initializr.util.VersionPropertyTests;
import io.spring.initializr.util.VersionRangeTests;
import io.spring.initializr.util.VersionTests;

@RunWith(Suite.class)
@SuiteClasses({ AgentTests.class, VersionParserTests.class, VersionPropertyTests.class, VersionRangeTests.class,
		VersionTests.class })
public class AllTests5 {

}
