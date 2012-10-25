package cn.kuehne.wsg50.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ArrayInputTests.class, ArrayOutputTests.class, InputFromStreamTests.class, OutputToStreamTests.class})
public class AllIOTests {

}
