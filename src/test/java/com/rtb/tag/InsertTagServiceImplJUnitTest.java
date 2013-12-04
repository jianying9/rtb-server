package com.rtb.tag;

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
public class InsertTagServiceImplJUnitTest extends AbstractRtbTest {

    public InsertTagServiceImplJUnitTest() {
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
        parameterMap.put("tagId", "103006");
        parameterMap.put("tagName", "塔防");
        String result = this.testHandler.execute(ActionNames.INSERT_TAG, parameterMap);
        System.out.println(result);
        //
        parameterMap.put("tagId", "103009");
        parameterMap.put("tagName", "养成");
        result = this.testHandler.execute(ActionNames.INSERT_TAG, parameterMap);
        System.out.println(result);
        //
        parameterMap.put("tagId", "122001");
        parameterMap.put("tagName", "重力");
        result = this.testHandler.execute(ActionNames.INSERT_TAG, parameterMap);
        System.out.println(result);
        //
        parameterMap.put("tagId", "125001");
        parameterMap.put("tagName", "僵尸");
        result = this.testHandler.execute(ActionNames.INSERT_TAG, parameterMap);
        System.out.println(result);
        //
        parameterMap.put("tagId", "151014");
        parameterMap.put("tagName", "跑酷");
        result = this.testHandler.execute(ActionNames.INSERT_TAG, parameterMap);
        System.out.println(result);
    }
}