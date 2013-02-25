<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Patients" otherwise="/login.htm"
	redirect="/index.htm" />
<style>
#openmrsSearchTable_wrapper {
	/* Removes the empty space between the widget and the Create New Patient section if the table is short */
	/* Over ride the value set by datatables */
	min-height: 0px;
	height: auto !important;
}
</style>

<script language="javascript">
function advancedSearch()
{
	var table = document.getElementById("openmrsSearchTable");
	var val ;
	var idString = '';
		
	for (var i = 1, row; row = table.rows[i]; i++) {
		   //iterate through rows
		   //rows would be accessed using the "row" variable assigned in the for loop
			  val = row.cells[0].innerHTML;
		   	  idString = idString + val +"," ;
		     //columns would be accessed using the "col" variable assigned in the for loop 
		}
	
	//most horrible ! fix later !
	
	document.getElementById('patientIds').value = idString ; 
	
	if(document.getElementById('province').value != "select province"){
	document.getElementById('stateProvince').value = document.getElementById('province').value;
	}else {
		document.getElementById('stateProvince').value = " ";
	}
	
	if(document.getElementById('district').value != "select district"){
	document.getElementById('countryDistrict').value = document.getElementById('district').value;
	} else {
		document.getElementById('countryDistrict').value = " ";
	}
	
	if(document.getElementById('sectors').value != "select sector"){
	document.getElementById('sector').value = document.getElementById('sectors').value;
	}else{
		document.getElementById('sector').value = " ";
	}
	
	if(document.getElementById('cells').value != "select cell"){
	document.getElementById('cell').value = document.getElementById('cells').value;
	}else {
		
	}
	
	if(document.getElementById('villages').value != "select village"){
	document.getElementById('village').value = document.getElementById('villages').value;
	}else {
		document.getElementById('village').value = " ";
	}
	
}
</script>

<openmrs:htmlInclude file="/dwr/interface/DWRPatientService.js" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/css/dataTables_jui.css" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/js/openmrsSearch.js" />

<openmrs:globalProperty key="patient.listingAttributeTypes"
	var="attributesToList" />

<script type="text/javascript">
				var lastSearch;
				$j(document).ready(function() {
					new OpenmrsSearch("findPatients", false, doPatientSearch, doSelectionHandler,
						[	{fieldName:"identifier", header:omsgs.identifier},
							{fieldName:"givenName", header:omsgs.givenName},
							{fieldName:"middleName", header:omsgs.middleName},
							{fieldName:"familyName", header:omsgs.familyName},
							{fieldName:"age", header:omsgs.age},
							{fieldName:"gender", header:omsgs.gender},
							{fieldName:"birthdateString", header:omsgs.birthdate},
							{fieldName:"deathDateString", header:omsgs.deathdate}
						],
						{
                            searchLabel: '<spring:message code="Patient.searchBox" javaScriptEscape="true"/>',
                            searchPlaceholder:'<spring:message code="Patient.searchBox.placeholder" javaScriptEscape="true"/>',
                            attributes: [
                                     	<c:forEach var="attribute" items="${fn:split(attributesToList, ',')}" varStatus="varStatus">
                                           <c:if test="${fn:trim(attribute) != ''}">
                                           <c:set var="attributeName" value="${fn:trim(attribute)}" />
                                			     <c:choose>
                                					<c:when test="${varStatus.index == 0}">
                                						{name:"${attributeName}", header:"<spring:message code="PersonAttributeType.${fn:replace(attributeName, ' ', '')}" text="${attributeName}"/>"}
                                					</c:when>
                                					<c:otherwise>
                                						,{name:"${attributeName}", header:"<spring:message code="PersonAttributeType.${fn:replace(attributeName, ' ', '')}" text="${attributeName}"/>"}
                                					</c:otherwise>
                                				 </c:choose>
                                           </c:if>
                                   		</c:forEach>
                                     ]
                            <c:if test="${not empty param.phrase}">
                                , searchPhrase: '<spring:message text="${ param.phrase }" javaScriptEscape="true"/>'
                            </c:if>                      
                        });

					//set the focus to the first input box on the page(in this case the text box for the search widget)
					var inputs = document.getElementsByTagName("input");
				    if(inputs[0])
				    	inputs[0].focus();


				});

				function doSelectionHandler(index, data) {
					document.location = "${model.postURL}?patientId=" + data.patientId + "&phrase=" + lastSearch;
				}

				//searchHandler for the Search widget
				function doPatientSearch(text, resultHandler, getMatchCount, opts) {
					lastSearch = text;
					DWRPatientService.findCountAndPatients(text, opts.start, opts.length, getMatchCount, resultHandler);
				}

			</script>

<div>
	<b class="boxHeader"><spring:message code="Patient.find" /></b>
	<div class="box">
		<div class="searchWidgetContainer" id="findPatients"></div>
	</div>
</div>
<br />
<div>
	<form name="input" method="POST">
		<fieldset>
			<legend>
				<b>Optional Search parameters</b>
			</legend>
			<br /> Age between <input type="text" name="moreThanAge"> and
			<input type="text" name="lessThanAge"> <br /> DOB :
			<openmrs_tag:dateField formFieldName="date" startValue="" />
			<br /> Gender :<input type="radio" name="gender" id="gender"
				checked="checked" value="M">Male <input type="radio"
				name="gender" id="gender" value="F">Female<br /> Province:

			<%-- 		<form:select path="province">
					  <form:option value="NONE" label="--- Select ---" />
					  <form:options items="${stateProvince}" />
				       </form:select> --%>


			Province:
			<form:select path="stateProvince" id="province"
				items="${stateProvince}" />
			<br /> District:
			<form:select path="countryDistrict" id="district"
				items="${countryDistrict}" />
			<br /> Sector:
			<form:select path="sector" id="sectors" items="${sector}" />
			<br /> Cell:
			<form:select path="cell" id="cells" items="${cell}" />
			<br /> Village:
			<form:select path="village" id="villages" items="${village}" />
			<br /> <input type="hidden" id="patientIds" name="patientIds">
			<input type="hidden" id="stateProvince" name="stateProvince">
			<input type="hidden" id="countryDistrict" name="countryDistrict">
			<input type="hidden" id="sector" name="sector"> 
			<input type="hidden" id="cell" name="cell">
			<input type="hidden" id="village" name="village"> 
			<tab><tab><tab><input type="submit" value="Submit" label="Search" onclick="advancedSearch()">
		</fieldset>
	</form>

<%-- <c:if test="${not empty patientLists}">
	<c:forEach var="patient" items='$(patientLists}'>
		${patient}
	</c:forEach>
	</c:if> --%>
	
	<c:if test="${not empty patientLists}">
	<table border="1">
	<tr><td>Identifer</td><td>Given Name</td></tr>
		<c:forEach var="patient" items="${patientLists}">
		<tr><td>
			${patient.patientId}
			</td>
			<td>
			${patient.givenName}
			</td>
			</tr>
	</c:forEach>
	</table>
	</c:if>

</div>

<c:if test="${empty model.hideAddNewPatient}">
	<openmrs:hasPrivilege privilege="Add Patients">
		<br /> &nbsp; <spring:message code="general.or" />
		<br />
		<br />
		<openmrs:portlet id="addPersonForm" url="addPersonForm"
			parameters="personType=patient|postURL=admin/person/addPerson.htm|viewType=${model.viewType}" />
	</openmrs:hasPrivilege>
</c:if>


<%@ include file="/WEB-INF/template/footer.jsp"%>