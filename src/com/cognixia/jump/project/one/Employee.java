package com.cognixia.jump.project.one;

public class Employee {
	
	enum Department{
		MEDICAL, EXAMINER, TECHNICAL
	}
	
	private int iD;
	private String name;
	private String occupation;
	private Department department;
	private final int dataTypes=4;
	
	public Employee() {
		
	}
	public Employee(int iD, String name, String occupation, Department department) {
		this.setiD(iD);
		this.setName(name);
		this.setOccupation(occupation);
		this.setDepartment(department);
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getiD() {
		return iD;
	}
	public void setiD(int iD) {
		this.iD = iD;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public void formatIn() {
		this.name=this.name.replaceAll("_", " ");
	}
	public void formatOut() {
		this.name=this.name.replaceAll(" ", "_");
	}
	public int getDataTypes() {
		return dataTypes;
	}
	
}
