//package profit.login.database;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean(name = "masterDataSource")
//    @Primary
//    @ConfigurationProperties(prefix = "datasource.master")
//    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "slave1DataSource")
//    @ConfigurationProperties(prefix = "datasource.slave1")
//    public DataSource slave1DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "slave2DataSource")
//    @ConfigurationProperties(prefix = "datasource.slave2")
//    public DataSource slave2DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//}
