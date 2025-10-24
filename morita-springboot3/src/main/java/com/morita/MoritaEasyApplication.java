package com.morita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MoritaEasyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoritaEasyApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  酒店管理系统启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
