package com.blz.employeepayrolltest_dbtest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.employeepayrolltest_dbtest.EmployeePayRollService.IOService;

public class EmployeePayRollTest {

	private static EmployeePayRollService employeePayRollService;
	
	@BeforeClass
	public static void createEmployeePayRollServiceObject() {
		employeePayRollService = new EmployeePayRollService();
	}
	
	@Test
	public void givenEmployeePayRoll_WhenRetrived_ShouldMatchEmployeeCount() {
		List<EmployeePayRollData> employeePayRollData = employeePayRollService.readPayRollData(IOService.DB_IO);
		assertEquals(6, employeePayRollData.size());
	}	

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWIthDB() throws EmployeePayRollException{
		List<EmployeePayRollData> employeePayRollData = employeePayRollService.readPayRollData(IOService.DB_IO);
		employeePayRollService.updateEmployeeSalary("SHRUTI",4000000.00);
		boolean result = employeePayRollService.checkEmployeePayRollInSyncWithDB("SHRUTI");
		assertTrue(result);
	}
	@Test
	public void givenDateRange_WhenRetrived_ShouldMatchEmployeeCount() {
		employeePayRollService.readPayRollData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2020, 01, 8);
		LocalDate endDate = LocalDate.of(2020, 12, 30);
		List<EmployeePayRollData> employeePayRollData = 
				employeePayRollService.readPayRollDataForDateRange(IOService.DB_IO,startDate,endDate);
		assertEquals(6,employeePayRollData.size());
	}
	
	@Test
	public void givenPayRollData_WhenAverageSalaryRetriveByGender_ShouldRetriveProperValue() throws SQLException {
		employeePayRollService.readPayRollData(IOService.DB_IO);
		Map<String,Double> averageSalaryByGender = employeePayRollService.readAverageSalaryByGender(IOService.DB_IO);
		assertTrue(averageSalaryByGender.get("F").equals(50000.0) &&
				averageSalaryByGender.get("M").equals(126500.0) );
	}
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws SQLException {
		employeePayRollService.readPayRollData(IOService.DB_IO);
		employeePayRollService.addEmployeePayRollData("Sri","M",1000000.0,LocalDate.now());
		boolean result = employeePayRollService.checkEmployeePayRollInSyncWithDB("Sri");
		assertTrue(result);
	}
}

