package cn.edu.buaa.onlinejudge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication  // SpringBoot的主配置类
@MapperScan("cn.edu.buaa.onlinejudge.mapper")
@EnableCaching
@EnableSwagger2 // 启用Swagger
@ComponentScan(basePackages = {"cn.edu.buaa.onlinejudge.mapper", "cn.edu.buaa.onlinejudge.service", "cn.edu.buaa.onlinejudge.controller"})

public class OnlinejudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinejudgeApplication.class, args);
    }

}
