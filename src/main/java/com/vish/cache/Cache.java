package com.vish.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vish.beans.Emp;
import com.vish.dao.EmpDao;

public class Cache {
	@Autowired
	EmpDao dao;
	
	Logger logger = Logger.getLogger(Cache.class);

	private List<Emp> empList = new ArrayList<Emp>();

	public Cache() {
		// TODO Auto-generated constructor stub

	}
	
	/* Initialize the cache by spring at the application startup */
	public void init() throws Exception{
		logger.info("Initializing the cache.....");
		empList = dao.getEmployees();
		logger.info("Initialized the cache....");
	}
	
    /* Method adds the employee object to DB and cache*/
	public void addEmp(Emp e) throws Exception{
		int ret = dao.save(e);
		empList.add(e);
	}
	
	
    /* Method used to synchronize the cache at periodic intervals*/
	public void synchCache() throws Exception{
		init();
	}
    /*Method used to get all th eemployee records from cache*/
	public List<Emp> getEmployees() throws Exception{
		return empList;
	}
	
    /*Method used to get the employees from cache based on page number*/
	public List<Emp> getEmployees(int pageid, int recordsPerPage) throws Exception{
		logger.info("Entering method getEmployees(pageid, recordsPerPage) with pageid: " + pageid);
		int startIndex = pageid * recordsPerPage - recordsPerPage;
		int endIndex = pageid * recordsPerPage - 1;
		if (endIndex > empList.size())
			endIndex = empList.size();
		return empList.subList(startIndex, endIndex);

	}
    
	/*Method used to get the total number of records from cache 
      which will be used to construct number of paged at UI*/
	public int getTotalRecords() throws Exception{
		return empList.size();
	}

}
