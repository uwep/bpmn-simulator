package bpmn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		GatewayExclusiveTestcase.class,
		GatewayInclusiveTestcase.class,
		GatewayParallelTestcase.class
})
public class GatewayTests {

}
