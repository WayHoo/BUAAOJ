package cn.edu.buaa.onlinejudge.converter;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanAndIntConverter extends BaseTypeHandler<Boolean> {
    /**
     * set: java(boolean) --> DB(int)
     * @param preparedStatement PreparedStatement对象
     * @param i PreparedStatement对象操作参数的下标
     * @param aBoolean java值
     * @param jdbcType JDBC操作的数据库类型
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean aBoolean, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,aBoolean ? 1 : 0);
    }



    /**
     * get: DB(int) --> java(boolean)
     * @param resultSet JDBC查询结果集
     * @param columnLabel 待提取结果的数据表列名
     * @throws SQLException
     */
    @Override
    public Boolean getNullableResult(ResultSet resultSet, String columnLabel) throws SQLException {
        int result = resultSet.getInt(columnLabel);
        return result == 1;
    }

    //get: DB(int) --> java(boolean)
    @Override
    public Boolean getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int result = resultSet.getInt(i);
        return result == 1;
    }

    //get: DB(int) --> java(boolean)
    @Override
    public Boolean getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int result = callableStatement.getInt(i);
        return result == 1;
    }
}
