//package profit.login.database;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//public class TransactionManagerConfig {
//
//    @Autowired
//    @Qualifier("masterDataSource")
//    private DataSource masterDataSource;
//
//    @Bean(name = "transactionManager")
//    @Primary
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(masterDataSource);
//    }
//
//    // 추가적인 트랜잭션 매니저 설정이 필요할 경우 추가
//}
