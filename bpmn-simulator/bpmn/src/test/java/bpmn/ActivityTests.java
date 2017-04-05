package bpmn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ActivityTestcase.class,
		SubprocessTestcase.class
})
public class ActivityTests {

}
