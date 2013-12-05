package com.rtb.timer;

import com.rtb.utils.KinitUtil;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Stateless;

/**
 *
 * @author aladdin
 */
@Stateless
@Startup
public class RtbTimerSessionBean implements RtbTimerSessionBeanLocal {

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*", persistent = false)
    @Override
    public void kinitTimer() {
        KinitUtil.kinit();
    }
}
