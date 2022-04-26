package com.tangshi.conferencesubscribe.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.tangshi.conferencesubscribe.anno.db1Dao;
import com.tangshi.conferencesubscribe.anno.db2Dao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Configuration
public class MultiDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource db1DataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource db2DataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource")DataSource db1DataSource) throws Exception{
        MybatisSqlSessionFactoryBean fb = new MybatisSqlSessionFactoryBean();
        fb.setDataSource(db1DataSource);
        fb.setTypeAliasesPackage("com.tangshi.conferencesubscribe.domain");
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        fb.setConfiguration(configuration);
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/db1/*.xml")
        );
        return fb.getObject();
    }

    @Bean
    public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource")DataSource db2DataSource) throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(db2DataSource);
        fb.setTypeAliasesPackage("com.tangshi.conferencesubscribe.domain");
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/db2/**/*.xml")
        );
        SqlSessionFactory sqlSessionFactory = fb.getObject();
        return sqlSessionFactory;
    }

    @Bean(name="db1MapperScannerConfigurer")
    public MapperScannerConfigurer oneMapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("db1SqlSessionFactory");
        configurer.setBasePackage("com.tangshi.conferencesubscribe.mapper.db1");
        configurer.setAnnotationClass(db1Dao.class);
        return configurer;
    }

    @Bean(name="db2MapperScannerConfigurer")
    public MapperScannerConfigurer twoMapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("db2SqlSessionFactory");
        configurer.setBasePackage("com.tangshi.conferencesubscribe.mapper.db2");
        configurer.setAnnotationClass(db2Dao.class);
        return configurer;
    }

    @Bean(name="db1TransactionManager")
    @Primary
    public DataSourceTransactionManager vxTransactionManager(@Qualifier("db1DataSource")DataSource vxDataSource) throws Exception{
        return new DataSourceTransactionManager(vxDataSource);
    }

    @Bean(name="db2TransactionManager")
    public DataSourceTransactionManager crawlerTransactionManager(@Qualifier("db2DataSource")DataSource crawlerDataSource) throws Exception{
        return new DataSourceTransactionManager(crawlerDataSource);
    }
}
 
