package com.blz.employeepayrolltest_dbtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayRollService {
	public enum IOService {
		DB_IO
	}

	private static List<EmployeePayRollData> employeePayRollList;

	public List<EmployeePayRollData> readPayRollData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayRollList = new EmployeePayRollDBService().readData();
		return employeePayRollList;
	}

}
