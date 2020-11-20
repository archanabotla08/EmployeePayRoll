package com.blz.employeepayrolltest_dbtest;

import static org.junit.Assert.*;

import java.util.List;

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
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWIthDB(){
		List<EmployeePayRollData> employeePayRollData = employeePayRollService.readPayRollData(IOService.DB_IO);
		employeePayRollService.updateEmployeeSalary("SHRUTI",4000000.00);
		boolean result = employeePayRollService.checkEmployeePayRollInSyncWithDB("SHRUTI");
		assertTrue(result);
	}
	
}
