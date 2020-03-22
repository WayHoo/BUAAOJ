package cn.edu.buaa.onlinejudge;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest extends OnlinejudgeApplicationTests {
    @Test
    public void getCodeFileSuffix(){
        List<String> compileCommandList = new ArrayList<>();
        compileCommandList.add("python -m py_compile {filename}.py");
        compileCommandList.add("gcc -O2 -s -Wall -o {filename}.exe {filename}.c -lm");
        compileCommandList.add("g++ -O2 -s -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm");
        compileCommandList.add("javac {filename}.java");
        for (String compileCommand : compileCommandList) {
            System.out.println("-------------Another Round---------------");
            System.out.println("compileCommand: " + compileCommand);
            Pattern pattern = Pattern.compile("\\{filename\\}\\.((?!exe| ).)+");
            Matcher matcher = pattern.matcher(compileCommand);
            if( matcher.find() ){
                String sourceFileName = matcher.group();
                System.out.println("sourceFileName before replaceing all: " + sourceFileName);
                sourceFileName = sourceFileName.replaceAll("\\{filename\\}\\.", "");
                System.out.println("sourceFileName after replaceing all: " + sourceFileName);
            } else{
                System.out.println("Not Found!");
            }
        }
    }
}
