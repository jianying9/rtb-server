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
public class InsertAdServiceImplJUnitTest extends AbstractRtbTest {

    public InsertAdServiceImplJUnitTest() {
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
        parameterMap.put("adName", "test2");
        parameterMap.put("imageId", "1");
        parameterMap.put("url", "www.google.com.hk");
        String result = this.testHandler.execute(ActionNames.INSERT_AD, parameterMap);
        System.out.println(result);
    }
}