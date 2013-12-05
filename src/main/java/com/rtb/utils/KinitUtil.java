package com.rtb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author aladdin
 */
public class KinitUtil {
    
    public static void kinit() {
        String kinitPath = KinitUtil.class.getClassLoader().getResource("kinit.sh").getPath();
        try {
            Process ps = Runtime.getRuntime().exec("chmod 777 " + kinitPath);
            ps.waitFor();
            ps = Runtime.getRuntime().exec(kinitPath);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
