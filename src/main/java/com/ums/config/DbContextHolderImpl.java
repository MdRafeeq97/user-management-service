package com.ums.config;

public class DbContextHolderImpl {
    private static ThreadLocal<DataSourceType> dataSourceTypeThreadLocal = new ThreadLocal<>();

    public static void setDbType(DataSourceType dataSourceType) {
        dataSourceTypeThreadLocal.set(dataSourceType);
    }

    public static DataSourceType getDbType() {
        return dataSourceTypeThreadLocal.get();
    }

    public static void clearDbType() {
        dataSourceTypeThreadLocal.remove();
    }
}
