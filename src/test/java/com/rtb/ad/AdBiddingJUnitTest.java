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
public class AdBiddingJUnitTest extends AbstractRtbTest {

    public AdBiddingJUnitTest() {
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
        parameterMap.put("positionId", "1");
        parameterMap.put("adId", "72275453-10bb-41a9-bd86-29f4a1568495");
        parameterMap.put("bid", "1");
        String result = this.testHandler.execute(ActionNames.AD_BIDDING, parameterMap);
        System.out.println(result);
    }
}