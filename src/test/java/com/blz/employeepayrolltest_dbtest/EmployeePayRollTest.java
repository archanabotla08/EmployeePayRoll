package com.blz.employeepayrolltest_dbtest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
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
		assertEquals(3, employeePayRollData.size());
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
		assertEquals(5,employeePayRollData.size());
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
//	@Test
//	public void givenNewEmployee_WhenAdded_ShouldSyncWithDBUC11() throws SQLException {
//		employeePayRollService.readPayRollData(IOService.DB_IO);
//		employeePayRollService.addEmployeePayRollDataUC11("Sri","M",1000000.0,LocalDate.now(),"jio",1,"testing");
//		boolean result = employeePayRollService.checkEmployeePayRollInSyncWithDB("Sri");
//		assertTrue(result);
//	}
	
	@Test
	public void givenEmployees_WhenAddedToDb_ShouldMatchEmployeeEntries_MultipleThreadingUC1_UC2_UC3() {
		EmployeePayRollData[] arrayOfEmps = {
			new EmployeePayRollData(0, "Shr","F" ,100000.0,LocalDate.now()),
			new EmployeePayRollData(0, "Vaibhav", "M",100000.0,LocalDate.now()),
			new EmployeePayRollData(0, "Krishna", "M",100000.0,LocalDate.now()),
			new EmployeePayRollData(0, "Shivani", "F",100000.0,LocalDate.now()),
			new EmployeePayRollData(0, "Anmol","M" ,100000.0,LocalDate.now()),
			new EmployeePayRollData(0, "Swati", "F",100000.0,LocalDate.now()),
		};
		
		employeePayRollService.readPayRollData(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayRollService.addEmployeeToPayRollWIthThreads(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayRollService.addEmployeeToPayRollWIthThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration With Thread : " + Duration.between(threadStart, threadEnd));
		employeePayRollService.printData(IOService.DB_IO);
		assertEquals(13, employeePayRollService.countEnteries(IOService.DB_IO));
	}
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_MultipleThreading() throws EmployeePayRollException{
		List<EmployeePayRollData> employeePayRollData = employeePayRollService.readPayRollData(IOService.DB_IO);
		employeePayRollService.readPayRollData(IOService.DB_IO);
		Instant threadStart = Instant.now();
		employeePayRollService.updateEmployeeSalaryWithThreads("Krishna",4000000.00);
		Instant threadEnd = Instant.now();
		System.out.println("Duration While Updated Query With Thread: " + Duration.between(threadStart, threadEnd));
		boolean result = employeePayRollService.checkEmployeePayRollInSyncWithDB("Krishna");
		assertTrue(result);
	}
	
}

