package com.blz.employeepayrolltest_dbtest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayRollService {
	public enum IOService {
		DB_IO,FILE_IO
	}

	private static List<EmployeePayRollData> employeePayRollList;
	private static EmployeePayRollDBService employeePayRollDBService;
	
	public EmployeePayRollService() {
		employeePayRollDBService = EmployeePayRollDBService.getInstance();
	}

	public List<EmployeePayRollData> readPayRollData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayRollList = employeePayRollDBService.readData();
		return employeePayRollList;
	}
	
	public boolean checkEmployeePayRollInSyncWithDB(String name) {
		List<EmployeePayRollData> employeePayRollDataList = employeePayRollDBService.getEmployeePayRollData(name);
		return employeePayRollDataList.get(0).equals(getEmployeePayRollData(name));
		
	}
	public void updateEmployeeSalary(String name,double salary) throws EmployeePayRollException {
		int result = employeePayRollDBService.updateEmployeeData(name,salary);
		if(result == 0) return;
		EmployeePayRollData employeePayRollData = this.getEmployeePayRollData(name);
		if(employeePayRollData != null) employeePayRollData.salary = salary;
		
	}
	
	public EmployeePayRollData getEmployeePayRollData(String name) {
		return this.employeePayRollList.stream()
					.filter(employeePayRollDataItem -> employeePayRollDataItem.name.equals(name))
					.findFirst()
					.orElse(null);
	}

	public List<EmployeePayRollData> readPayRollDataForDateRange(IOService ioService, LocalDate startDate,
			LocalDate endDate) {
		if(ioService.equals(IOService.DB_IO)) {
			return employeePayRollDBService.getEmployeePayRollForDateRange(startDate,endDate);
		}
		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService ioService) throws SQLException {
		 if(ioService.equals(IOService.DB_IO))
			 return employeePayRollDBService.getAverageSalaryByGender();
		return null;
	}

	public void addEmployeePayRollData(String name, String gender, double salary, LocalDate date) throws SQLException {
		employeePayRollList.add(employeePayRollDBService.addEmployeeToPayRoll(name,gender,salary,date));
		
		
	}

	public void addEmployeePayRollDataUC11(String name, String gender, double salary, LocalDate date, String companyName,
			int companyId, String department) {
		
		employeePayRollList.add(employeePayRollDBService.addEmployeeToPayRollUC11(name,gender,salary,date,companyName,companyId,department));
	}

	public void addEmployeePayRollData(List<EmployeePayRollData> employeePayRollList) {
		employeePayRollList.forEach(employeePayRollData -> {
			try {
				this.addEmployeePayRollData(employeePayRollData.name,
						employeePayRollData.gender,
						employeePayRollData.salary, 
						employeePayRollData.startDate
						);
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		});
		
	}

	public long countEnteries(IOService ioService) {
		if(ioService.equals(IOService.FILE_IO))
			return new EmployeePayRollService().countEnteries(ioService);
		return employeePayRollList.size();
	}

	

}
