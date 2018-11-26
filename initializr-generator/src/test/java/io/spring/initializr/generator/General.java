package io.spring.initializr.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllTests2.class, AllTests3.class, AllTests4.class, AllTests5.class, CommandLineHelpGeneratorTests.class,
		CustomProjectGeneratorTests.class, ProjectGeneratorBuildTests.class, ProjectGeneratorLanguageTests.class,
		ProjectGeneratorTests.class, ProjectRequestResolverTests.class, ProjectRequestTests.class })
public class General {

}
