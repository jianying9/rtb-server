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
public class RegisterServiceImplJUnitTest extends AbstractRtbTest {

    public RegisterServiceImplJUnitTest() {
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
        parameterMap.put("userEmail", "aladdin@rtb.com");
        parameterMap.put("nickName", "aladdin");
        parameterMap.put("password", "670b14728ad9902aecba32e22fa4f6bd");
        String result = this.testHandler.execute(ActionNames.REGISTER, parameterMap);
        System.out.println(result);
    }
}