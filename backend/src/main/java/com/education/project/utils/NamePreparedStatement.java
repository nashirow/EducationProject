/*
 * Copyright 2020 Hicham AZIMANI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.education.project.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NamePreparedStatement {

    private PreparedStatement preparedStatement;

    private List<String> fields;

    public NamePreparedStatement(Connection connection, String requestSql) throws SQLException {
        fields = new ArrayList<>();
        int pos;
        while ((pos = requestSql.indexOf(":")) != -1) {
            int end = requestSql.substring(pos).indexOf(" ");
            if (end == -1) end = requestSql.length();
            else end += pos;
            fields.add(requestSql.substring(pos + 1, end));
            requestSql = requestSql.substring(0, pos) + "?" + requestSql.substring(end);
        }
        preparedStatement = connection.prepareStatement(requestSql);
    }//NamePreparedStatement

    private int getIndex(String name) {
        return fields.indexOf(name) + 1;
    }//getIndex()

    public void setString(String name, String value) throws SQLException {
        preparedStatement.setString(getIndex(name), value);
    }//setString()

    public void setTime(String name, java.sql.Time value) throws SQLException {
        preparedStatement.setTime(getIndex(name), value);
    }//setTime()

    public void setInt(String name, Integer value) throws SQLException {
        preparedStatement.setInt(getIndex(name), value);
    }//setString()

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }//getPreparedStatement()

    public ResultSet executeQuery() throws SQLException {
        return preparedStatement.executeQuery();
    }//executeQuery()

    public void close() throws SQLException {
        preparedStatement.close();
    }//close()
}//NamePreparedStatement
