package com.blz.employeepayrolltest_dbtest;

import java.time.LocalDate;

public class EmployeePayRollData {
	public int id;
	public String name;
	public double salary;
	public LocalDate startDate;

	public EmployeePayRollData(int id, String name, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	public EmployeePayRollData(int id, String name, double salary, LocalDate startDate) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.startDate = startDate;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmployeePayRollData that = (EmployeePayRollData) obj;
		return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name);
	}
}

