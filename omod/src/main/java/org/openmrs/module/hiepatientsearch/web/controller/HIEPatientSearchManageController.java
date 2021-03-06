/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.hiepatientsearch.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.api.context.Context;
//import org.openmrs.module.addresshierarchyrwanda.AddressHierarchy;
//import org.openmrs.module.addresshierarchyrwanda.AddressHierarchyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class HIEPatientSearchManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/findPatient.htm", method = RequestMethod.GET)
	public void manage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("The HIE patient search action has been triggered...");
		
		response.sendRedirect(request.getContextPath() + "/module/hiepatientsearch/manage.form");
	}
	
	@RequestMapping(value = "/module/hiepatientsearch/manage", method = RequestMethod.GET)
	public String manager(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		model.addAttribute("user", Context.getAuthenticatedUser());
		
		return "/module/hiepatientsearch/manage";
	}
	
	@RequestMapping(value = "/module/hiepatientsearch/manage", method = RequestMethod.POST)
	public String advancedSearch(@RequestParam(value = "patientIds") String patientIds,
	        @RequestParam("stateProvince") String stateProvince, @RequestParam("countryDistrict") String countryDistrict,
	        @RequestParam("sector") String sector, @RequestParam("cell") String cell,
	        @RequestParam("village") String village, @RequestParam("moreThanAge") String moreThanAge,
	        @RequestParam("lessThanAge") String lessThanAge, @RequestParam("date") Date dateString,
	        @RequestParam(value = "gender") String gender, ModelMap model) {
		
		String[] patientIdList = patientIds.split(",");
		
		List<Patient> patients = new ArrayList<Patient>();
		List<Patient> patientLists = new ArrayList<Patient>();
		List<Patient> s = new ArrayList<Patient>();
		
		for (int i = 0; i < patientIdList.length; i++) {
			patients = Context.getPatientService().getPatients(null, patientIdList[i], null, false);
			patientLists.add(patients.get(0));
		}
		
		if (gender != null) {
			patientLists = genderFilter(patientLists, gender);
		}
		
		if (moreThanAge != "" || lessThanAge != "") {
			patientLists = ageFilter(patientLists, moreThanAge, lessThanAge);
		}
		
		if (dateString != null) {
			Date date = null;
		}
		
		if (!stateProvince.trim().isEmpty()) {
			patientLists = stateProvinceFilter(patientLists, stateProvince);
			
		}
		
		if (!countryDistrict.trim().isEmpty()) {
			patientLists = countryDistrictFilter(patientLists, countryDistrict);
		}
		
		if (!sector.trim().isEmpty()) {
			patientLists = sectorFilter(patientLists, sector);
			
		}
		
		if (!cell.trim().isEmpty()) {
			patientLists = cellFilter(patientLists, cell);
		}
		
		if (!village.trim().isEmpty()) {
			patientLists = villageFilter(patientLists, village);
			
		}
		
		model.addAttribute("patientLists", patientLists);
		return "/module/hiepatientsearch/manage";
	}
	
	private List<Patient> villageFilter(List<Patient> patientLists, String village) {
		Iterator<Patient> iterator = patientLists.iterator();
		while (iterator.hasNext()) {
			Patient patient = iterator.next();
			PersonAddress pa = patient.getPersonAddress();
			if (pa.getAddress1() != null && pa.getAddress1() != "") {
				if (!pa.getAddress1().equals(village)) {
					iterator.remove();
				}
			}
		}
		return patientLists;
	}
	
	private List<Patient> cellFilter(List<Patient> patientLists, String cell) {
		Iterator<Patient> iterator = patientLists.iterator();
		while (iterator.hasNext()) {
			Patient patient = iterator.next();
			PersonAddress pa = patient.getPersonAddress();
			if (pa.getAddress3() != null && pa.getAddress3() != "") {
				if (!pa.getAddress3().equals(cell)) {
					iterator.remove();
				}
			}
		}
		return patientLists;
	}
	
	private List<Patient> sectorFilter(List<Patient> patientLists, String sector) {
		Iterator<Patient> iterator = patientLists.iterator();
		while (iterator.hasNext()) {
			Patient patient = iterator.next();
			PersonAddress pa = patient.getPersonAddress();
			if (pa.getCityVillage() != null && pa.getCityVillage() != "") {
				if (!pa.getCityVillage().equals(sector)) {
					iterator.remove();
				}
			}
		}
		return patientLists;
	}
	
	private List<Patient> countryDistrictFilter(List<Patient> patientLists, String countryDistrict) {
		Iterator<Patient> iterator = patientLists.iterator();
		while (iterator.hasNext()) {
			Patient patient = iterator.next();
			PersonAddress pa = patient.getPersonAddress();
			if (pa.getCountyDistrict() != null && pa.getCountyDistrict() != "") {
				if (!pa.getCountyDistrict().equals(countryDistrict)) {
					iterator.remove();
				}
			}
		}
		return patientLists;
	}
	
	private List<Patient> stateProvinceFilter(List<Patient> patientLists, String stateProvince) {
		Iterator<Patient> iterator = patientLists.iterator();
		while (iterator.hasNext()) {
			Patient patient = iterator.next();
			PersonAddress pa = patient.getPersonAddress();
			
			if (pa.getStateProvince() != null && pa.getStateProvince() != "") {
				if (!pa.getStateProvince().equals(stateProvince)) {
					iterator.remove();
				}
			}
		}
		return patientLists;
	}
	
	private List<Patient> ageFilter(List<Patient> patientLists, String moreThanAge, String lessThanAge) {
		if (moreThanAge != null && lessThanAge == null) {
			Iterator<Patient> iterator = patientLists.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getAge() > Integer.parseInt(moreThanAge)) {
					
				} else {
					iterator.remove();
				}
			}
			
		} else if (moreThanAge == null && lessThanAge != null) {
			Iterator<Patient> iterator = patientLists.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getAge() < Integer.parseInt(lessThanAge)) {
					
				} else {
					iterator.remove();
				}
			}
			
		} else if (moreThanAge != null && lessThanAge != null) {
			Iterator<Patient> iterator = patientLists.iterator();
			while (iterator.hasNext()) {
				int age = iterator.next().getAge();
				if (age > Integer.parseInt(moreThanAge) && age < Integer.parseInt(lessThanAge)) {
					
				} else {
					iterator.remove();
				}
			}
			
		}
		return patientLists;
	}
	
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String redirect() {
		return "manage";
	}
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String finalPage() {
		return "manage";
	}
	
	private List<Patient> genderFilter(List<Patient> patientLists, String gender) {
		Iterator<Patient> iterator = patientLists.iterator();
		while (iterator.hasNext()) {
			if (!iterator.next().getGender().equals(gender)) {
				iterator.remove();
			}
		}
		return patientLists;
	}
	
}
