package cn.edu.buaa.onlinejudge;

import cn.edu.buaa.onlinejudge.utils.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTest extends OnlinejudgeApplicationTests {
    @Test
    public void testFile(){
        String uploadPath = FileUtil.getUploadPath();
        System.out.println(uploadPath);
    }
}
