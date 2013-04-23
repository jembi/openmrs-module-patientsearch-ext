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
package org.openmrs.module.hiepatientsearch.web.dwr;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.web.dwr.PersonListItem;


public class PatientListItem extends PersonListItem {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private Integer patientId;
	
	private String identifier = "";
	
	private Boolean identifierCheckDigit = false;
	
	private String identifierTypeName = "";
	
	private String otherIdentifiers = "";
	
	private String birthdateString;
	
	private Date birthdate;
	
	public Date getBirthdate() {
		birthdate= super.getBirthdate();
		System.out.println("---------> " + birthdate);
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	/*@Override
	public String getAddress1() {
		System.out.println("L" + getAddress1());
		if(getAddress1()== null || getAddress1().isEmpty()){
			setAddress1(" ");
		}
		return getAddress1();
	}
	
	
	@Override
	public String getAddress2() {
		if(getAddress2()== null || getAddress2().isEmpty()){
			setAddress2(" ");
		}
		return getAddress2();
	}
	*/
	
	/**
	 * Returns a formatted birthdate value
	 * 
	 * @since 1.8
	 */
	public String getBirthdateString() {
		birthdateString = super.getBirthdateString();
		System.out.println("------||-> " + birthdateString);
		return birthdateString;
	}
	
	/**
	 * @since 1.8
	 */
	public void setBirthdateString(String birthdateString) {
		this.birthdateString = birthdateString;
	}
	
	public String getCountryDistrict() {
		System.out.println("EEEEEEEEEEEEEE888888888888888888888888888" + countryDistrict);
		if(countryDistrict == null || countryDistrict.isEmpty()){
			countryDistrict = " ";
		}
		return countryDistrict;
	}

	public void setCountryDistrict(String countryDistrict) {
		this.countryDistrict = countryDistrict;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	private String stateProvince = "";
	
	private String countryDistrict = "";
	
	private String cell = "";
	
	private String sector = "";
	
	private String village = "";
	
	public PatientListItem() {
	}
	
/*	public PatientListItem(Patient patient) {
		this(patient, null);
	}*/
	
	public PatientListItem(Patient patient) {
		super(patient);
		
		if (patient != null) {
			
			stateProvince = patient.getPersonAddress().getStateProvince();
			countryDistrict = patient.getPersonAddress().getCountyDistrict();
			//cell = patient.getPersonAddress().getAddress3();
			cell = patient.getPersonAddress().getNeighborhoodCell();
			sector = patient.getPersonAddress().getCityVillage();
			village = patient.getPersonAddress().getAddress1();
			patientId = patient.getPatientId();
			
			// get patient's identifiers
			boolean first = true;
			for (PatientIdentifier pi : patient.getIdentifiers()) {
				if (first) {
					identifier = pi.getIdentifier();
					identifierCheckDigit = pi.getIdentifierType().hasCheckDigit();
					first = false;
				} else {
					if (!"".equals(otherIdentifiers))
						otherIdentifiers += ",";
					otherIdentifiers += " " + pi.getIdentifier();
				}
			}
			
		}
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof PatientListItem) {
			PatientListItem pi = (PatientListItem) obj;
			if (pi.getPatientId() == null || patientId == null)
				return false;
			return pi.getPatientId().equals(patientId);
		}
		return false;
	}
	
	public int hashCode() {
		if (patientId == null)
			return super.hashCode();
		return patientId.hashCode();
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getOtherIdentifiers() {
		return otherIdentifiers;
	}
	
	public void setOtherIdentifiers(String otherIdentifiers) {
		this.otherIdentifiers = otherIdentifiers;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	/**
	 * @return Returns the identifierIdentifierCheckdigit.
	 */
	public Boolean getIdentifierCheckDigit() {
		return identifierCheckDigit;
	}
	
	/**
	 * @param identifierIdentifierCheckdigit The identifierIdentifierCheckdigit to set.
	 */
	public void setIdentifierCheckDigit(Boolean identifierCheckDigit) {
		this.identifierCheckDigit = identifierCheckDigit;
	}
	
	/**
	 * @param identifierTypeName the identifierTypeName to set
	 */
	public void setIdentifierTypeName(String identifierTypeName) {
		this.identifierTypeName = identifierTypeName;
	}
	
	/**
	 * @return the identifierTypeName
	 */
	public String getIdentifierTypeName() {
		return identifierTypeName;
	}
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}
}
