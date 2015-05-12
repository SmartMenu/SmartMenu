package com.smartmenu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ParseResultSetInterface {
	List<Object> parseResult(ResultSet rs) throws SQLException;
}
