package com.cognixia.jump.project.one;

public class CertifiedEmployee extends Employee {
	
	public static enum OvertimeOptions{
		FULL, OPTIONAL, NONE
	}
	
	private OvertimeOptions option;
	
	public CertifiedEmployee() {
		super();
	}
	public CertifiedEmployee(int iD, String name, String occupation, Department department, OvertimeOptions option) {
		super(iD,name,occupation,department);
		this.setOption(option);
	}
	public CertifiedEmployee(Employee e, OvertimeOptions option) {
		super(e.getiD(),e.getName(),e.getOccupation(),e.getDepartment());
		this.setOption(option);
	}
	public OvertimeOptions getOption() {
		return option;
	}
	public void setOption(OvertimeOptions option) {
		this.option = option;
	}
}
