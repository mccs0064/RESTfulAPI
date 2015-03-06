package com.rest.dao;

import java.sql.Connection;

import javax.naming.*;
import javax.sql.*;

public class Sql {

	private static DataSource Sqldb = null;
	private static Context context = null;

	public static DataSource SqlConn() throws Exception {

		if (Sqldb != null) {
			return Sqldb;
		}

		try {
			if (context == null) {
				context = new InitialContext();
			}

			Sqldb = (DataSource) context.lookup("sqldb");
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return Sqldb;

	}

	protected static Connection sqlConnection() {// protected allows dao to call
													
		Connection conn = null;
		try {
			conn = Sql.SqlConn().getConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

}
