//package profit.login.database;
//
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class DataSourceAspect {
//
//    @Before("@annotation(readOnly)")
//    public void setReadOnlyDataSource(ReadOnly readOnly) {
//        DataSourceContextHolder.setDataSourceType(DataSourceType.READ_ONLY);
//    }
//
//    @After("@annotation(readOnly)")
//    public void clearDataSource(ReadOnly readOnly) {
//        DataSourceContextHolder.clearDataSourceType();
//    }
//}
