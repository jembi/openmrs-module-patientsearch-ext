package org.openmrs.module.hiepatientsearch.web.dwr;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class DWRModuleServiceTest extends BaseModuleContextSensitiveTest  {
	
	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet("src/test/resources/InitialData.xml");
	}
	
	@Test
	@Verifies(value = "should Get All Patients from database (", method = "getAllPatients(...)")
	public void getAllPatients_shouldGetAllPatientsFromDatabase(){
		Assert.assertEquals(Context.getPatientService().getAllPatients().size(),6);
	}
	
	@Test
	@Verifies(value = "should Get Matching Patients from database (", method = "findPatients(...)")
	public void findPatients_shouldGetMatchingPatientsFromDatabase(){
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.findPatients("Jean", true).size(), 3);
	}
	
	@Test
	@Verifies(value = "should Fail If SearchString Is of Insufficient Length (", method = "findPatients(...)")
	public void findPatients_shouldFailIfSearchStringIsofInsufficientLength(){
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.findPatients("Je", true).size(), 0);
	}
	
	@Test
	@Verifies(value = "should Get Matching Patients By FamilyName (", method = "findPatients(...)")
	public void findPatients_shouldGetMatchingPatientsByFamilyName(){
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.findPatients("Doe", true).size(), 3);
	}
	
	@Test
	@Verifies(value = "should Get Matching Patients By Search filters (", method = "findPatients(...)")
	public void filter_shouldGetMatchingPatientsBySearchFilters() throws ParseException{
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.filter("5", null,null, "M", null, null, null, null, null, null).size(), 1);
		Assert.assertEquals(dwr.filter("5", null,null, "F", null, null, null, null, null, null).size(), 0);

	}
	
	@Test
	@Verifies(value = "should Get Multiple Patients By Search filters (", method = "findPatients(...)")
	public void filter_shouldGetMultiplePatientsBySearchFilters() throws ParseException{
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.filter("5,6", null,null, "M", null, null, null, null, null, null).size(), 2);
		Assert.assertEquals(dwr.filter("5,6,7", null,null, "F", null, null, null, null, null, null).size(), 0);

	}
	
	@Test
	@Verifies(value = "should Get Patients By Multiple Search filters (", method = "findPatients(...)")
	public void filter_shouldGetPatientsByMultipleSearchFilters() throws ParseException{
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.filter("5,6", "25", null, "M", null, null, null, null, null, null).size(), 1);
		Assert.assertEquals(dwr.filter("5,6", "13", "30", "M", null, null, null, null, null, null).size(), 1);

	}
	
	@Test
	@Verifies(value = "should Work With Incomplete Data (", method = "findPatients(...)")
	public void filter_shouldWorkWithIncompleteData() throws ParseException{
		DWRMyModuleService dwr = new DWRMyModuleService();
		Assert.assertEquals(dwr.filter("6", "25", null, "M", null, null, null, null, null, null).size(), 1);

	}
}