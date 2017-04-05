package bpmn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ActivityTests.class,
		EventEndTerminationTestcase.class,
		GatewayTests.class
})
public class AllTests {

}
