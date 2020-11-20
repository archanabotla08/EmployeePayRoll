package com.blz.employeepayrolltest_dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.blz.employeepayrolltest_dbtest.EmployeePayRollException.ExceptionType;
import com.mysql.cj.xdevapi.PreparableStatement;


public class EmployeePayRollDBService {
	
	private static EmployeePayRollDBService employeePayRollDBService;
	private PreparedStatement employeePayRollDataStatement;
	
	private EmployeePayRollDBService() {
		
	}
	
	public static EmployeePayRollDBService getInstance() {
		if(employeePayRollDBService == null)
			employeePayRollDBService = new EmployeePayRollDBService();
		return employeePayRollDBService;
	}
	
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
 		String userName = "root";
		String password = "root";
		Connection connection;
		System.out.println("Connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection successful: " + connection);
		return connection;
	}
	public List<EmployeePayRollData> readData() {
		String sql = "SELECT* FROM employee_payroll_basic;";
		List<EmployeePayRollData> employeePayrollList = new ArrayList<EmployeePayRollData>();
		try(Connection connection = this.getConnection();){		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayRollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	public List<EmployeePayRollData> getEmployeePayRollData(String name) {
		List<EmployeePayRollData> employeePayRollList = null;
		if(this.employeePayRollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayRollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayRollDataStatement.executeQuery();
			employeePayRollList = this.getEmployeePayRollData(resultSet);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayRollList;
	}
	private List<EmployeePayRollData> getEmployeePayRollData(ResultSet resultSet) {
		List<EmployeePayRollData> employeePayrollList = new ArrayList<EmployeePayRollData>();
		try{		
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayRollData(id, name, salary,startDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECt * FROM employee_payroll_basic WHERE name = ?";
			employeePayRollDataStatement = connection.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public int updateEmployeeData(String name,double salary) throws EmployeePayRollException {
		return this.updateEmployeeDataUsingStatement(name,salary);
	}
	
	public int updateEmployeeDataUsingStatement(String name,double salary) throws EmployeePayRollException {
		String sql = String.format("UPDATE employee_payroll_basic set salary=%.2d where name=%s", name,salary);
		try(Connection connection = this.getConnection();){		
			Statement statement = connection.createStatement();
			return (statement.executeUpdate(sql));
		} catch (SQLException e) {
			throw new EmployeePayRollException("Wrong SQL query given", ExceptionType.WRONG_SQL);
		}
	}
	
	public int updateSalaryUsingSQL(String name,Double salary) throws SQLException {
		String sql="UPDATE employee_payroll_basic SET salary=? WHERE name=?";
		try(Connection connection=getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			return preparedStatement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
