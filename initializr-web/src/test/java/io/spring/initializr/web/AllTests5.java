package io.spring.initializr.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.web.support.DefaultDependencyMetadataProviderTests;
import io.spring.initializr.web.support.DefaultInitializrMetadataProviderTests;
import io.spring.initializr.web.support.SpringBootMetadataReaderTests;

@RunWith(Suite.class)
@SuiteClasses({ DefaultDependencyMetadataProviderTests.class, DefaultInitializrMetadataProviderTests.class,
		SpringBootMetadataReaderTests.class })
public class AllTests5 {

}
