package org.openmrs.module.hiepatientsearch.web.dwr;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class DWRModuleServiceTest extends BaseModuleContextSensitiveTest {

	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet("src/test/resources/InitialData.xml");
	}

	@Test
	@Verifies(value = "should Get All Patients from database", method = "getAllPatients(...)")
	public void getAllPatients_shouldGetAllPatientsFromDatabase() {
		// Test that database script works
		Assert.assertEquals(
				Context.getPatientService().getAllPatients().size(), 6);
	}

	@Test
	@Verifies(value = "should Get Matching Patients from database", method = "findPatients(...)")
	public void findPatients_shouldGetMatchingPatientsFromDatabase() {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 0;

		try {
			resultCount = dwr.findPatients("Jean", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");
		}

		Assert.assertEquals(resultCount, 3);

	}

	@Test
	@Verifies(value = "should Fail If SearchString is of Insufficient Length", method = "findPatients(...)")
	public void findPatients_shouldFailIfSearchStringIsOfInsufficientLength() {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 1;

		try {
			resultCount = dwr.findPatients("Je", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");
		}

		Assert.assertEquals(resultCount, 0);

	}

	@Test
	@Verifies(value = "should Get Matching Patients By FamilyName", method = "findPatients(...)")
	public void findPatients_shouldGetMatchingPatientsByFamilyName() {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 0;

		try {
			resultCount = dwr.findPatients("Doe", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");
		}

		Assert.assertEquals(resultCount, 3);

	}

	@Test
	@Verifies(value = "should Get Matching Patients By Search filters", method = "findPatients(...)")
	public void filter_shouldGetMatchingPatientsBySearchFilters()
			throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 0;

		try {
			// Test searching a single patient using gender filters
			resultCount = dwr.filter("5", null, null, "M", null, null, null,
					null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 1);

		resultCount = 1; // Reset value for second test

		try {
			resultCount = dwr.filter("5", null, null, "F", null, null, null,
					null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");
		}

		Assert.assertEquals(resultCount, 0);

	}

	@Test
	@Verifies(value = "should Get Multiple Patients By Search filters", method = "findPatients(...)")
	public void filter_shouldGetMultiplePatientsBySearchFilters()
			throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();
		// Test searching multiple patients using gender filter

		int resultCount = 0;

		try {
			resultCount = dwr.filter("5,6", null, null, "M", null, null, null,
					null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 2);

		resultCount = 1; // Reset value for second test

		try {
			resultCount = dwr.filter("5,6,7", null, null, "F", null, null,
					null, null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}

		Assert.assertEquals(resultCount, 0);

	}

	@Test
	@Verifies(value = "should Get Patients By Address filters", method = "findPatients(...)")
	public void filter_shouldGetPatientsByAddressFilters()
			throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();
		// Test that address search filter functions correctly

		int resultCount = 0;

		try {
			resultCount = dwr.filter("5,6", null, null, null, null, "province",
					null, null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		Assert.assertEquals(resultCount, 1);

		resultCount = 0; // Reset value for second test

		try {
			resultCount = dwr.filter("5,6,7", null, null, null, null, null,
					"district", null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		Assert.assertEquals(resultCount, 1);

	}

	@Test
	@Verifies(value = "should Get Patients By Multiple Search filters", method = "findPatients(...)")
	public void filter_shouldGetPatientsByMultipleSearchFilters()
			throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 0;

		try {
			resultCount = dwr.filter("5,6", "25", null, "M", null, null, null,
					null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 1);

		resultCount = 0; // Reset value for second test

		try {
			resultCount = dwr.filter("5,6", "13", "30", "M", null, null, null,
					null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 1);

	}

	@Test
	@Verifies(value = "should Work With Incomplete Data", method = "findPatients(...)")
	public void filter_shouldWorkWithIncompleteData() throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();
		// Test for target result with incomplete Person Address

		int resultCount = 0;

		try {
			resultCount = dwr.filter("6", "25", null, "M", null, null, null,
					null, null, null).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 1);

	}

	@Test
	@Verifies(value = "should Not Respond to Special Characters", method = "findPatients(...)")
	public void findPatients__shouldNotRespondToSpecialCharacters()
			throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 1;

		try {
			resultCount = dwr.findPatients("Doe*", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 0);

		resultCount = 1; // Reset value for second test

		try {
			resultCount = dwr.findPatients("*Doe", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 0);

	}

	@Test
	@Verifies(value = "should Not Idenify Segments Within String", method = "findPatients(...)")
	public void findPatients__shouldNotIdenifySegmentsWithinString()
			throws ParseException {
		DWRMyModuleService dwr = new DWRMyModuleService();

		int resultCount = 0;

		try {
			// Test that target search result exists
			resultCount = dwr.findPatients("Jean", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 3);

		resultCount = 1; // Reset value for second test

		try {
			// Test that target search result will not be identified unless
			// search criteria begins with exact character matches
			resultCount = dwr.findPatients("ean", true).size();
		} catch (Exception e) {
			Assert.fail("Search should not throw an exception");

		}
		
		Assert.assertEquals(resultCount, 0);

	}

}