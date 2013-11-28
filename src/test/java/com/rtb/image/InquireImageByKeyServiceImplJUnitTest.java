package com.rtb.image;

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
public class InquireImageByKeyServiceImplJUnitTest extends AbstractRtbTest {

    public InquireImageByKeyServiceImplJUnitTest() {
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
        parameterMap.put("imageId", "130e820c-adea-46ce-b105-1596293be669");
        String result = this.testHandler.execute(ActionNames.INQUIRE_IMAGE_BY_KEY, parameterMap);
        System.out.println(result);
    }
}