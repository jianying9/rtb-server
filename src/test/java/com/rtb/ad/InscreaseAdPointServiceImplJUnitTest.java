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
public class InscreaseAdPointServiceImplJUnitTest extends AbstractRtbTest {

    public InscreaseAdPointServiceImplJUnitTest() {
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
        parameterMap.put("adPoint", "100");
        parameterMap.put("adId", "3");
        String result = this.testHandler.execute(ActionNames.INCREASE_AD_POINT, parameterMap);
        System.out.println(result);
    }
}