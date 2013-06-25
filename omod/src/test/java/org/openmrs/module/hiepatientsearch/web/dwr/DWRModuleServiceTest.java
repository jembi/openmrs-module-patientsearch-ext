package org.openmrs.module.hiepatientsearch.web.dwr;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.hiepatientsearch.api.HIEPatientSearchService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class DWRModuleServiceTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(HIEPatientSearchService.class));
		
	}
}