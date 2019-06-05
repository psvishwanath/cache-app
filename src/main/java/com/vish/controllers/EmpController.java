package com.vish.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.vish.beans.Emp;
import com.vish.dao.EmpDao;
//import com.vish.kafka.Consumer;
import com.vish.cache.Cache;

import org.apache.log4j.Logger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


@Controller
@Api(tags="Cache Application API List")
public class EmpController {
	@Autowired
	EmpDao dao;

	@Autowired
	Cache cache;

	final static Logger logger = Logger.getLogger(EmpController.class);


	@ApiResponses(value= {@ApiResponse(code=200, message="success")})
        @ApiOperation(value="Returns the employee form") 
        @RequestMapping(value = "/employee", method = RequestMethod.GET)
        public String empForm(Model m) {
                m.addAttribute("command", new Emp());
                return "employee";
        }

	@ApiResponses(value= {@ApiResponse(code=200, message="success")})
	@ApiOperation(value="Creates new employee")	
	@RequestMapping(value = "/employee", method = RequestMethod.POST)
	public String create(@ApiParam(value="employe object, required=true") @ModelAttribute("emp") Emp emp) {
	    logger.info("Adding emp to cache and DB :" + emp.getName());
	     try{
                cache.addEmp(emp);
            }catch(Exception e){
                logger.error("Exception while adding data to cache and DB: "+e.getMessage() );
            }
		
            return "redirect:/employees";// will redirect to viewemp request mapping
	}
	
        @ApiResponses(value= {@ApiResponse(code=200, message="success")})
        @ApiOperation(value="Returns the employee form cache based on given employee ID for edit")
	@RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
	public String edit(@ApiParam(value="employe ID, required=true") @PathVariable int id, Model m) {
		Emp emp = dao.getEmpById(id);
		m.addAttribute("command", emp);
		return "employee";
	}

        @ApiResponses(value= {@ApiResponse(code=200, message="Edit Success")})
        @ApiOperation(value="Updates the employee record")
	@RequestMapping(value = "/employee", method = RequestMethod.PUT)
	public String editsave(@ApiParam(value="employe object, required=true") @ModelAttribute("emp") Emp emp) {
		dao.update(emp);
		return "redirect:/employees";
	}
	
	@ApiResponses(value= {@ApiResponse(code=200, message="Delete Success")})
        @ApiOperation(value="Deletes employee record based on given ID")
	@RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
	public String deleteEmp( @ApiParam(value="employe ID, required=true") @PathVariable int id) {
		dao.delete(id);
		return "redirect:/employees";
	}

	@ApiResponses(value= {@ApiResponse(code=200, message="Success")})
        @ApiOperation(value="Returns the employee records")
	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public String viewEmployees(Model m) {
		List<Emp> list = null;
                try{
                	list=cache.getEmployees();
        	}catch(Exception e)
        	{
                	logger.error("Exception while getting emp records: "+e.getMessage());
        	}
		m.addAttribute("list", list);
		return "employees";
	}

	/*
	 * Method is used to get the records based on page number
	 */
	@ApiResponses(value= {@ApiResponse(code=200, message="Success")})
        @ApiOperation(value="Returns the employee records based on page number")

	@RequestMapping(value="/employees/{pageid}", method = RequestMethod.GET)    
        public String get( @ApiParam(value="page number, required=true") @PathVariable int pageid, Model m){    
        int recordsPerPage=5;
	int totalRecords=0;
	List<Emp> list = null;

	try{
        	totalRecords = cache.getTotalRecords();        
        	list=cache.getEmployees(pageid,recordsPerPage);
	}catch(Exception e)
	{
		logger.error("Exception while getting emp records: "+e.getMessage());
	}
    
        m.addAttribute("totalRecords", totalRecords);  
        m.addAttribute("msg", list);  
        return "employees";    
    }
}
