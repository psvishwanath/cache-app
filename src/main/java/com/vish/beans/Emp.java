package com.vish.beans;  
  
public class Emp {  
	private int id;  
	private String name;  
	private float salary;  
	private String designation;  

	public int getId() {  
	    return id;  
	}  
	public void setId(int id) {  
	    this.id = id;  
	}  
	public String getName() {  
	    return name;  
	}  
	public void setName(String name) {  
	    this.name = name;  
	}  
	public float getSalary() {  
	    return salary;  
	}  
	public void setSalary(float salary) {  
	    this.salary = salary;  
	}  
	public String getDesignation() {  
	    return designation;  
	}  
	public void setDesignation(String designation) {  
	    this.designation = designation;  
	} 

	@Override
	public String toString() { 
	    String str = "Id: "+String.valueOf(this.id) +"   name: "+this.name+"   salary: "+String.valueOf(this.salary)+" designation: "+this.designation;
		return str; 
	} 

	@Override
	public int hashCode() 
	{
		return this.id;
	}


	@Override
	public boolean equals(Object obj) 
	{
	  if (this == obj) 
		  return true;
	  if (obj == null) 
		  return false;
	  if (this.getClass() != obj.getClass()) 
		  return false;
	  Emp that = (Emp) obj;
	  if (this.id == that.id) 
		  return true;
	  else
		  return false;

	}
  
}  
