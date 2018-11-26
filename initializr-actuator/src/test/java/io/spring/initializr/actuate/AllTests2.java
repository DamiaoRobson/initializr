package io.spring.initializr.actuate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.actuate.info.BomRangesInfoContributorTests;
import io.spring.initializr.actuate.info.DependencyRangesInfoContributorTests;

@RunWith(Suite.class)
@SuiteClasses({ BomRangesInfoContributorTests.class, DependencyRangesInfoContributorTests.class })
public class AllTests2 {

}
