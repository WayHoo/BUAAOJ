package cn.edu.buaa.onlinejudge;

import cn.edu.buaa.onlinejudge.utils.ZipUtil;
import org.junit.jupiter.api.Test;

public class ZipTest extends OnlinejudgeApplicationTests {
    @Test
    public void unZipTest(){
        String sourceFileName = "D:\\tmp\\test.zip";
        String targetDir = "D:\\tmp\\zipDir";
        ZipUtil.unZip(sourceFileName, targetDir);
    }
}
