package cn.edu.buaa.onlinejudge;

import cn.edu.buaa.onlinejudge.utils.MD5Util;
import org.junit.jupiter.api.Test;

public class MD5Test extends OnlinejudgeApplicationTests {
    @Test
    public void testMd5(){
        String text = "123";
        String encryptedStr = MD5Util.encryptedByMD5(text);
        System.out.println("encryptedStr = " + encryptedStr);
        System.out.println("result = " + MD5Util.verifyMD5(text, encryptedStr));
    }
}
