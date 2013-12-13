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
public class ClickAdJUnitTest extends AbstractRtbTest {

    public ClickAdJUnitTest() {
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
        Map<String, String> parameterMap = new HashMap<String, String>(4, 1);
        parameterMap.put("positionId", "2");
        parameterMap.put("adId", "2");
        parameterMap.put("bid", "3");
        parameterMap.put("userId", "1");
        parameterMap.put("tagId", "103006");
        String result = this.testHandler.execute(ActionNames.CLICK_AD, parameterMap);
        System.out.println(result);
    }
}