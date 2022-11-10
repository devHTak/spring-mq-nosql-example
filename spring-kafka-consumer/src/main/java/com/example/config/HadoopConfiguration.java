package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;

@Configuration
@EnableHadoop
public class HadoopConfiguration extends SpringHadoopConfigurerAdapter {

    @Override
    public void configure(HadoopConfigConfigurer configure) throws Exception {
        configure.fileSystemUri("hdfs://localhost:9000");
    }
}
