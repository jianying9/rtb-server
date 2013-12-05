package com.rtb.ad;

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
public class InquirePositionAdJUnitTest extends AbstractRtbTest {

    public InquirePositionAdJUnitTest() {
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
        parameterMap.put("positionId", "2");
        parameterMap.put("userId", "353922050040300");
        String result = this.testHandler.execute(ActionNames.INQUIRE_POSITION_AD, parameterMap);
        System.out.println(result);
    }
}