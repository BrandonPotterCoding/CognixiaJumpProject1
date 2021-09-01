package com.cognixia.jump.project.one;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cognixia.jump.project.one.CertifiedEmployee.OvertimeOptions;
import com.cognixia.jump.project.one.Employee.Department;

public class EmployeeManagementSystem {

	private static final int options = 7;

	public static void main(String[] args) throws IOException {

		File file = new File("resources/EmployeeInfo.txt");
		String temp;
		String[] tempStrArray;
		if (!file.exists())
			file.createNewFile();

		FileReader fReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fReader);
		Scanner scan = new Scanner(System.in);

		ArrayList<String> fileLines = new ArrayList<String>();
		while ((temp = reader.readLine()) != null) {
			fileLines.add(temp);
		}
		reader.close();

		// System.out.println(fileLines);

		// the idea is to separate employee info
		// into name id and position
		/*
		 * String[][] fStream = fileLines.stream() .map(x->String::split(" "))
		 * .collect(Collectors.toList());
		 */
		ArrayList<Employee> employeeSet = new ArrayList<Employee>();
		for (int i = 0; i < fileLines.size(); i++) {
			tempStrArray = fileLines.get(i).split(" ");
			if (tempStrArray.length > 4)
				employeeSet.add(new CertifiedEmployee(Integer.parseInt(tempStrArray[0]), tempStrArray[1],
						tempStrArray[2], Department.valueOf(tempStrArray[3].toUpperCase()),
						OvertimeOptions.valueOf(tempStrArray[4].toUpperCase())));
			else
				employeeSet.add(new Employee(Integer.parseInt(tempStrArray[0]), tempStrArray[1], tempStrArray[2],
						Department.valueOf(tempStrArray[3].toUpperCase())));
		}
		for (Employee e : employeeSet) {
			e.formatIn();
		}
		boolean cond = true;
		System.out.println("How would you like to interact with the Management System?");
		System.out.println("==========================================================");

		while (cond) {
			listOptions();
			int response = getValidIntResponse(options, scan);

			switch (response) {
			case 1:
				listEmployeeInfo(employeeSet, scan);
				break;
			case 2:
				listAllEmployeeNames(employeeSet, scan);
				break;
			case 3:
				addEmplyoee(employeeSet, scan);
				break;
			case 4:
				removeEmployee(employeeSet, scan);
				break;
			case 5:
				updateEmployee(employeeSet, scan);
				break;
			case 6:
				listEmployeeByDepartment(employeeSet, scan);
				break;
			case 7:
				System.out.println("Thank you for using this Management System!");
				endOfProgram(employeeSet, file);
				cond = false;
				break;
			default:
				System.out.println("This should not be reached!");
				break;
			}
		}

		scan.close();

	}

	private static void endOfProgram(ArrayList<Employee> employeeSet, File file) throws IOException {
		FileWriter fWriter = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fWriter);
		for (Employee e : employeeSet) {
			e.formatOut();
			if (e instanceof CertifiedEmployee)
				writer.write(e.getiD() + " " + e.getName() + " " + e.getOccupation() + " " + e.getDepartment() + " "
						+ ((CertifiedEmployee) e).getOption()+"\n");
			else
				writer.write(e.getiD() + " " + e.getName() + " " + e.getOccupation() + " " + e.getDepartment()+"\n");
		}
		writer.close();

	}

	private static void listEmployeeByDepartment(ArrayList<Employee> employeeSet, Scanner scan) {
		System.out.println("Which Department are you looking to see Employees of?");
		Department dep = getDepartmentValidated(scan);
		Stream<Employee> empStream = employeeSet.stream();
		ArrayList<Employee> filteredList = (ArrayList<Employee>) empStream.filter(e -> e.getDepartment().equals(dep))
				.collect(Collectors.toList());
		for (Employee e : filteredList) {
			System.out.println("Employee: " + e.getiD() + "\nName: " + e.getName() + "\nOccupation: "
					+ e.getOccupation() + "\nDepartment: " + e.getDepartment());
			if (e instanceof CertifiedEmployee)
				System.out.println("Overtime Options: " + ((CertifiedEmployee) e).getOption());
			System.out.println();
		}
	}

	private static void updateEmployee(ArrayList<Employee> employeeSet, Scanner scan) {
		System.out.println("What is the ID of the Employee " + "that you would like to update?");
		int address = searchEmployeeID(employeeSet, scan);
		int dataTypes = employeeSet.get(address).getDataTypes();
		Employee currentEmployee = employeeSet.get(address);
		boolean isCert = false;
		if (currentEmployee instanceof CertifiedEmployee)
			isCert = true;

		System.out.println("What do you need to update for " + employeeSet.get(address).getName() + "?");

		System.out.println("1: Change Name.");
		System.out.println("2: Change Occupation.");
		System.out.println("3: Change Department.");
		if (isCert) {
			System.out.println("4: Change Overtime Options.");
		}
		else
			System.out.println("4: Offer Overtime Options");
		int answer = getValidIntResponse(dataTypes, scan);

		switch (answer) {
		case 1:
			System.out.println("What would you like to change " + currentEmployee.getName()
					+ "'s Name to? \n Enter in \"FirstName LastName\" format with space between.");
			currentEmployee.setName(getStringValidated("Name", scan));
			System.out.println("Name successfully changed to " + currentEmployee.getName() + ".");
			break;
		case 2:
			System.out.println("What would you like to change " + currentEmployee.getName() + "'s Occupation to?"
					+ "(Current Occupation is: " + currentEmployee.getOccupation() + ")");
			currentEmployee.setOccupation(getStringValidated("Occupation", scan));
			System.out.println("Occupation successfully changed to " + currentEmployee.getOccupation() + ".");
			break;
		case 3:
			System.out.println("What would you like to change " + currentEmployee.getName() + "'s Department to? "
					+ "(Current Department is :" + currentEmployee.getDepartment() + ")");
			currentEmployee.setDepartment(getDepartmentValidated(scan));
			break;
		case 4:
			if (isCert) {
				System.out.println("What would you like to change " + currentEmployee.getName()
						+ "'s Overtime Option to?" + "(Current Overtime Option is: "
						+ ((CertifiedEmployee) currentEmployee).getOption() + ")");
				((CertifiedEmployee) currentEmployee).setOption(getOvertimeValidated(scan));
				System.out.println("Overtime Option successfully changed to "
						+ ((CertifiedEmployee) currentEmployee).getOption() + ".");
			} else {
				System.out.println("Would you like to offer "+currentEmployee.getName()+" Overtime Options?(Y/N)");
				char acceptance = getYN(scan);
				if(acceptance=='Y') {
					System.out.println("Would you like to change "+currentEmployee.getName()+" to: Full, Optional or None?");
					OvertimeOptions option = getOvertimeValidated(scan);
					CertifiedEmployee replaceEmployee= new CertifiedEmployee(currentEmployee,option);
					employeeSet.remove(address);
					employeeSet.add(address,replaceEmployee);
				}
				else
					System.out.println(currentEmployee.getName()+" will remain without Overtime Options then!");
			}
			break;
		default:
			System.out.println("This should not be reached.");
		}

	}

	private static OvertimeOptions getOvertimeValidated(Scanner scan) {
		String holder = null;
		char acceptance;
		boolean cond = false;
		boolean cond2;
		while (!cond) {
			cond2 = false;
			while (!cond2) {
				holder = scan.next().toUpperCase();
				for (OvertimeOptions e : OvertimeOptions.values()) {
					if (e.name().equals(holder)) {
						cond2 = true;
					}
				}
				if (!cond2) {
					System.out.println("Invalid Overtime Option! Please enter: Full, Optional or None");
				}
			}
			System.out.println("You entered: " + holder + " for the new Overtime Option.\n Is this Acceptable?(Y/N)");
			acceptance = getYN(scan);
			if (acceptance == 'N')
				System.out.println("What would you like to change Overtime Options to?");
			else
				cond = true;
		}

		return OvertimeOptions.valueOf(holder);
	}

	private static String getStringValidated(String string, Scanner scan) {
		String holder = null;
		char acceptance;
		boolean cond = false;
		while (!cond) {
			scan.nextLine();
			holder = scan.nextLine();
			System.out.println(
					"You entered: " + holder + " for the new " + string + ". " + "\n Is this Acceptable?(Y/N)");
			acceptance = getYN(scan);
			if (acceptance == 'N') {
				System.out.println("What would you like to change " + string + " to?");
			} else
				cond = true;
		}
		return holder;
	}

	private static char getYN(Scanner scan) {
		boolean cond = false;
		char acceptance = 'l';
		while (!cond) {
			try {
				acceptance = scan.next().toUpperCase().charAt(0);
				if (acceptance != 'Y' && acceptance != 'N')
					throw new YesNoException();
				cond = true;

			} catch (YesNoException e) {
				System.out.println(e.getMessage());
			}
		}
		return acceptance;
	}

	private static int searchEmployeeID(ArrayList<Employee> employeeSet, Scanner scan) {
		int address = -1;
		boolean cond = false;
		while (!cond) {
			try {
				int empID = getValidInt(scan);

				for (int i = 0; i < employeeSet.size(); i++) {
					if (empID == employeeSet.get(i).getiD()) {
						address = i;
						i = employeeSet.size() + 1;
						cond = true;
					}
				}
				if (address == -1)
					throw new NoEmployeeException();

			} catch (NoEmployeeException e) {
				System.out.println(e.getMessage());
				System.out.println("Please enter in a valid ID!");
			}
		}
		return address;
	}

	private static void removeEmployee(ArrayList<Employee> employeeSet, Scanner scan) {

		System.out.println("What is the ID of the Employee that you would like to remove?");

		int address = searchEmployeeID(employeeSet, scan);
		Employee holder = employeeSet.remove(address);
		System.out.println("Employee: " + holder.getiD() + ", " + holder.getName() + " has been removed!");

	}

	private static void addEmplyoee(ArrayList<Employee> employeeSet, Scanner scan) {
		OvertimeOptions overtimeOption = null;
		System.out.println("What is the ID of the Employee that you will be adding?");
		Integer id = getUniqueID(employeeSet, scan);
		System.out.println("What is the Name of the Employee that you will be adding?");
		String name = getStringValidated("Name", scan);
		System.out.println("What is the Occupation of the Employee that you will be adding?");
		String occupation = getStringValidated("Occupation", scan);
		System.out.println("What Department is Employee that you will be adding in?");
		Department department = getDepartmentValidated(scan);
		System.out.println("Is this Employee Certified?");
		char acceptance = getYN(scan);
		if (acceptance == 'Y') {
			System.out.println("What is the Overtime Options of the Employee that you will be adding?");
			overtimeOption = getOvertimeValidated(scan);
		}
		if (acceptance == 'Y') {
			CertifiedEmployee addEmployee = new CertifiedEmployee(id, name, occupation, department, overtimeOption);
			employeeSet.add(addEmployee);
		} else {
			Employee addEmployee = new Employee(id, name, occupation, department);
			employeeSet.add(addEmployee);
		}
	}

	private static Department getDepartmentValidated(Scanner scan) {
		String holder = null;
		char acceptance;
		boolean cond = false;
		boolean cond2;
		while (!cond) {
			cond2 = false;
			while (!cond2) {
				holder = scan.next().toUpperCase();
				for (Department d : Department.values()) {
					if (d.name().equals(holder)) {
						cond2 = true;
					}
				}
				if (!cond2) {
					System.out.println("Invalid Department! Please enter: Medical, Examiner or Technical");
				}
			}
			System.out.println("You entered: " + holder + " for the Department.\n Is this Acceptable?(Y/N)");
			acceptance = getYN(scan);
			if (acceptance == 'N')
				System.out.println("What would you like to change Department to?");
			else
				cond = true;
		}

		return Department.valueOf(holder);
	}

	private static int getUniqueID(ArrayList<Employee> employeeSet, Scanner scan) {
		int id = 0;
		boolean cond = false;
		boolean flag = false;
		while (!cond) {
			try {
				System.out.println("Enter ID: ");
				id = getValidInt(scan);
				for (Employee e : employeeSet) {
					if (id == e.getiD())
						flag = true;
				}
				if (flag)
					throw new IDExistsException(id);
				if (id < 1)
					throw new NonNegativeException();

				cond = true;
			} catch (IDExistsException e) {
				System.out.println(e.getMessage());
				flag = false;
			} catch (InputMismatchException e) {
				System.out.println("Please Enter a number");
			} catch (NonNegativeException e) {
				System.out.println(e.getMessage());
			}
		}
		return id;
	}

	private static void listAllEmployeeNames(ArrayList<Employee> employeeSet, Scanner scan) {
		System.out.println("The Employee Names are as Follows:");
		for (Employee e : employeeSet)
			System.out.println(e.getName());
	}

	private static void listEmployeeInfo(ArrayList<Employee> employeeSet, Scanner scan) {
		System.out.println("Which Employee are you looking to see information on?(Employee ID)");
		int address = searchEmployeeID(employeeSet, scan);
		Employee employee = employeeSet.get(address);
		System.out.println("Employee: " + employee.getiD() + "\nName: " + employee.getName() + "\nOccupation: "
				+ employee.getOccupation() + "\nDepartment: " + employee.getDepartment());
		if (employee instanceof CertifiedEmployee)
			System.out.println("Overtime Options: " + ((CertifiedEmployee) employee).getOption());
	}

	private static int getValidInt(Scanner scan) {
		boolean cond = true;
		int holder = 0;
		while (cond) {
			try {
				holder = scan.nextInt();
				cond = false;
			} catch (InputMismatchException e) {
				System.out.println("Please enter a number!");
				scan.next();
			}
		}
		return holder;
	}

	private static int getValidIntResponse(int range, Scanner scan) {
		boolean cond = true;
		int optionHolder = -1;
		System.out.println("Enter the option you would like to use by number.");
		while (cond) {
			try {
				optionHolder = scan.nextInt();
				if (optionHolder < 1 || optionHolder > range)
					System.out.println("You must select a number between 1 and " + range + "!");
				else
					cond = false;
			} catch (InputMismatchException e) {
				System.out.println("Please enter a number!");
				scan.next();
			}
		}
		return optionHolder;
	}

	private static void listOptions() {

		System.out.println("========================OPTIONS===========================");
		System.out.println("==========================================================");
		System.out.println("Your options are as follows:");
		System.out.println("1: View Employee information by reference.");
		System.out.println("2: View full list of Employee Names.");
		System.out.println("3: Add an Employee to the list.");
		System.out.println("4: Remove an Employee from the list by reference.");
		System.out.println("5: Update an Employee's information from the list by reference.");
		System.out.println("6: List Employee by Department.");
		System.out.println("7: Exit Management Software.");
		System.out.println("==========================================================");
	}
}
