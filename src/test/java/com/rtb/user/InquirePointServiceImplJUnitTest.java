package com.rtb.user;

import com.rtb.AbstractRtbTest;
import com.rtb.config.ActionNames;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aladdin
 */
public class InquirePointServiceImplJUnitTest extends AbstractRtbTest {

    public InquirePointServiceImplJUnitTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    //

    @Test
    public void test() {
        Map<String, String> parameterMap = new HashMap<String, String>(2, 1);
        String result = this.testHandler.execute(ActionNames.INQUIRE_POINT, parameterMap);
        System.out.println(result);
    }
}