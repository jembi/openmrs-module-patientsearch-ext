package org.openmrs.module.hiepatientsearch.web.dwr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class DWRMyModuleService {
 
	private static final Log log = LogFactory.getLog(DWRMyModuleService.class);

	
	private static Integer maximumResults;
 
 public Collection<PatientListItem> filter(String ids, String moreThanAge, String lessThanAge, String gender, String date, String province, String district, String sectors, String cells, String villages) throws ParseException {
	System.out.println("---------------------> date : " + province);
	// ids = ids.replaceFirst("|", "");
	 ids = ids.trim();
	 String[] idList = ids.split(",");
	 List<Patient> patients = new ArrayList<Patient>();
	 
	 for(String s : idList){
		 System.out.println(s);
		 if(!s.isEmpty()){
		 Patient patient = Context.getPatientService().getPatient(Integer.parseInt(s));
		 patients.add(patient);
		 }
	 }
	 System.out.println(patients.size());
	 
	 if(moreThanAge != null && !moreThanAge.trim().isEmpty()){
		 Iterator<Patient> i = patients.iterator();
		 while(i.hasNext()){
			 Patient p = i.next();
			 if(p.getAge() < Integer.parseInt(moreThanAge)){
				 i.remove();
			 }
		 }
	 }
	 
	 if(lessThanAge != null && !lessThanAge.trim().isEmpty()){
		 Iterator<Patient> i = patients.iterator();
		 while(i.hasNext()){
			 Patient p = i.next();
			 if(p.getAge() > Integer.parseInt(lessThanAge)){
				 i.remove();
			 }
		 }
	 }
	 
	 if(gender != null && !gender.trim().isEmpty()){
		 Iterator<Patient> i = patients.iterator();
		 while(i.hasNext()){
			 Patient p = i.next();
			 
			 if(p.getGender() == null || p.getGender().isEmpty()){
				 i.remove();
			 }
			 if(!p.getGender().equals(gender)){
				 i.remove();
			 }
		 }
	 }
	 
	 if(date != null && !date.trim().isEmpty()){
		 
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date dateObj = myFormat.parse(date);
		 
		 Iterator<Patient> i = patients.iterator();
		 while(i.hasNext()){
			 Patient p = i.next();
			 
			 if(p.getBirthdate() == null){
				 i.remove();
			 }
			 if(p.getBirthdate().compareTo(dateObj)!=0){
				 i.remove();
			 }
		 }
	 } 
	 
	 System.out.println("After gender" + patients.size());
	 
	 if(province != null && !province.trim().isEmpty()){
		 if(!province.equals("select province")){
			 Iterator<Patient> i = patients.iterator();
			 while(i.hasNext()){
				 Patient p = i.next();
				 

				 System.out.println("%" + p.getPersonAddress().getStateProvince());
				
				 if(p.getPersonAddress() != null && p.getPersonAddress().getStateProvince() != null && p.getPersonAddress().getStateProvince().trim().isEmpty() == false){
				 System.out.println(" in        !");
				 if(!p.getPersonAddress().getStateProvince().equalsIgnoreCase(province)){
					 System.out.println("remove !");
					 i.remove();
				 }
				 
				 }else{
					 i.remove();
					 System.out.println("not is !");
				 }
				 
			 }
		 }
	 }
	 System.out.println("After genderW" + patients.size()); 
	 
	 if(district != null && !district.trim().isEmpty()){
		 if(!district.equals("select district")){
			 Iterator<Patient> i = patients.iterator();
			 while(i.hasNext()){
				 Patient p = i.next();
				 
				 if(p.getPersonAddress() != null && p.getPersonAddress().getCountyDistrict() != null && p.getPersonAddress().getCountyDistrict().trim().isEmpty() == false){

				 if(!p.getPersonAddress().getCountyDistrict().equalsIgnoreCase(district)){
					 i.remove();
				 }
				 }else{
					 i.remove();
				 }
			 }
		 }
	 }
	 
	 System.out.println("After genderU" + patients.size()); 
	 
	 if(sectors != null && !sectors.trim().isEmpty()){
		 if(!sectors.equals("select sector")){
			 Iterator<Patient> i = patients.iterator();
			 while(i.hasNext()){
				 Patient p = i.next();
				 
				 //if(p.getPersonAddress() != null && p.getPersonAddress().getAddress3() != null && p.getPersonAddress().getAddress3().trim().isEmpty() == false){
				 if(p.getPersonAddress() != null && p.getPersonAddress().getCityVillage() != null && p.getPersonAddress().getCityVillage().trim().isEmpty() == false){
				 if(!p.getPersonAddress().getCityVillage().equalsIgnoreCase(sectors)){
					 System.out.println("removed ! # + " + sectors);
					 System.out.println(p.getPersonAddress().getCityVillage());
					 i.remove();
				 }
				 }else{
					 i.remove();
				 }
			 }
		 }
	 }
	 
	 System.out.println("After genderX" + patients.size()); 
	 
	 if(cells != null && !cells.trim().isEmpty()){
		 if(!cells.equals("select cell")){
			 Iterator<Patient> i = patients.iterator();
			 while(i.hasNext()){
				 Patient p = i.next();
				 
				 //if(p.getPersonAddress() != null && p.getPersonAddress().getAddress1() != null && p.getPersonAddress().getAddress1().trim().isEmpty() == false){
				 if(p.getPersonAddress() != null && p.getPersonAddress().getNeighborhoodCell() != null && p.getPersonAddress().getNeighborhoodCell().trim().isEmpty() == false){
				 if(!p.getPersonAddress().getNeighborhoodCell().equalsIgnoreCase(cells)){
					 i.remove();
				 }
				 }else{
					 i.remove();
				 }
			 }
		 }
	 }
	 System.out.println("After genderY" + patients.size()); 
	 
	 
	 if(villages != null && !villages.trim().isEmpty()){
		 if(!villages.equals("select village")){
			 Iterator<Patient> i = patients.iterator();
			 while(i.hasNext()){
				 Patient p = i.next();
				 
				 //if(p.getPersonAddress() != null && p.getPersonAddress().getCityVillage() != null && p.getPersonAddress().getCityVillage().trim().isEmpty() == false){
				 if(p.getPersonAddress() != null && p.getPersonAddress().getAddress1() != null && p.getPersonAddress().getAddress1().trim().isEmpty() == false){
				 if(!p.getPersonAddress().getAddress1().equalsIgnoreCase(villages)){
					 i.remove();
				 }
				 }else{
					 i.remove();
				 }
			 }
		 }
	 }
	 
	 System.out.println("After genderZ" + patients.size()); 
	 
	 List<PatientListItem> patientList = new Vector<PatientListItem>();
	 
	 for (Patient p : patients)
			patientList.add(new PatientListItem(p));
	return patientList;
}
 
	/**
	 * Search on the <code>searchValue</code>. If a number is in the search string, do an identifier
	 * search. Else, do a name search
	 * 
	 * @param searchValue string to be looked for
	 * @param includeVoided true/false whether or not to included voided patients
	 * @return Collection<Object> of PatientListItem or String
	 * @should return only patient list items with nonnumeric search
	 * @should return string warning if invalid patient identifier
	 * @should not return string warning if searching with valid identifier
	 * @should include string in results if doing extra decapitated search
	 * @should not return duplicate patient list items if doing decapitated search
	 * @should not do decapitated search if numbers are in the search string
	 * @should get results for patients that have edited themselves
	 * @should logged in user should load their own patient object
	 */
	public Collection<PatientListItem> findPatients(String searchValue, boolean includeVoided) {
		System.out.println("EEEEEEEEEEEEE");
		return findBatchOfPatients(searchValue, includeVoided, null, null);
	}
	
	/**
	 * Search on the <code>searchValue</code>. If a number is in the search string, do an identifier
	 * search. Else, do a name search
	 * 
	 *
	 * @param searchValue string to be looked for
	 * @param includeVoided true/false whether or not to included voided patients
	 * @param start The starting index for the results to return
	 * @param length The number of results of return
	 * @return Collection<Object> of PatientListItem or String
	 * @since 1.8
	 */
	private Collection<PatientListItem> findBatchOfPatients(String searchValue, boolean includeVoided, Integer start, Integer length) {
		System.out.println("hit hit hit ");
		if (maximumResults == null)
			maximumResults = getMaximumSearchResults();
		if (length != null && length > maximumResults)
			length = maximumResults;
		
		// the list to return
		List<PatientListItem> patientList = new Vector<PatientListItem>();
		
		PatientService ps = Context.getPatientService();
		Collection<Patient> patients = null;
		
		
		try {
			patients = ps.getPatients(searchValue, start, length);
		}
		catch (APIAuthenticationException e) {
			//patientList.add(Context.getMessageSourceService().getMessage("Patient.search.error") + " - " + e.getMessage());
			//return patientList;
		}

		for (Patient p : patients)
			patientList.add(new PatientListItem(p));

		return patientList;
	}
	
 

    /**
	 * Fetch the max results value from the global properties table
	 * 
	 * @return Integer value for the person search max results global property
	 */
	private static Integer getMaximumSearchResults() {
		try {
			return Integer.valueOf(Context.getAdministrationService().getGlobalProperty(
			    OpenmrsConstants.GLOBAL_PROPERTY_PERSON_SEARCH_MAX_RESULTS,
			    String.valueOf(OpenmrsConstants.GLOBAL_PROPERTY_PERSON_SEARCH_MAX_RESULTS_DEFAULT_VALUE)));
		}
		catch (Exception e) {
			log.warn("Unable to convert the global property " + OpenmrsConstants.GLOBAL_PROPERTY_PERSON_SEARCH_MAX_RESULTS
			        + "to a valid integer. Returning the default "
			        + OpenmrsConstants.GLOBAL_PROPERTY_PERSON_SEARCH_MAX_RESULTS_DEFAULT_VALUE);
		}
		
		return OpenmrsConstants.GLOBAL_PROPERTY_PERSON_SEARCH_MAX_RESULTS_DEFAULT_VALUE;
	}
	
 public Collection<String> loadFile(String idType){

     /* String holder = idType; */
     String line ;
     ArrayList<String> locs = new ArrayList<String>();

     for (Patient patient : Context.getPatientService().getAllPatients()) {
         
     }


     try{

     InputStream is =  new FileInputStream("/WEB-INF/view/module/hiepatientsearch/resources/locations/Test.txt");
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr);



         while ((line = br.readLine()) != null) {
             String[] tokens = line.split(",");

             locs.add(tokens[2].trim());

         }
         line = idType;
         locs.add(line);
            return locs;
     }

     catch(FileNotFoundException io){
            return null;
     }
     catch(IOException ex){
         return null;
     }

 }
}
