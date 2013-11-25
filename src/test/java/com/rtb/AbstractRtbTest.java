package com.rtb;

import com.rtb.session.SessionImpl;
import com.wolf.framework.config.FrameworkConfig;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.session.Session;
import com.wolf.framework.test.TestHandler;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;

/**
 *
 * @author aladdin
 */
public abstract class AbstractRtbTest {

    protected final TestHandler testHandler;

    public AbstractRtbTest() {
        Map<String, String> parameterMap = new HashMap<String, String>(8, 1);
        parameterMap.put(FrameworkConfig.ANNOTATION_SCAN_PACKAGES, "com.rtb");
        parameterMap.put(FrameworkConfig.TASK_CORE_POOL_SIZE, "1");
        parameterMap.put(FrameworkConfig.TASK_MAX_POOL_SIZE, "2");
        //
        parameterMap.put(FrameworkConfig.REDIS_SERVER_HOST, "192.168.59.49");
        parameterMap.put(FrameworkConfig.REDIS_SERVER_PORT, "6379");
        parameterMap.put(FrameworkConfig.REDIS_MAX_POOL_SIZE, "1");
        parameterMap.put(FrameworkConfig.REDIS_MIN_POOL_SIZE, "2");
        this.testHandler = new TestHandler(parameterMap);
        Session session = new SessionImpl("c9028bdc-2a8c-4100-9156-9a0f267c64cb");
        this.testHandler.setSession(session);
    }

    @AfterClass
    public static void tearDownClass() {
        ApplicationContext.CONTEXT.contextDestroyed();
    }
}
