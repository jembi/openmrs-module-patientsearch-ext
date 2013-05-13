<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<!--<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js"/>-->
<openmrs:htmlInclude
	file="/moduleResources/hiepatientsearch/media/js/jquery-1.7.2.js" />


<%--<openmrs:htmlInclude file="/dwr/interface/DWRReportingService.js" />--%>
<openmrs:htmlInclude
	file="/moduleResources/hiepatientsearch/scripts/paginate.js" />

<openmrs:htmlInclude
	file="/moduleResources/hiepatientsearch/css/style.css" />
<openmrs:require privilege="View Patients" otherwise="/login.htm"
	redirect="/index.htm" />
<style>
#openmrsSearchTable_filter {
	display: none;
}

.top {
	display: none;
}

tr.odd {
	background-color: #E2E4FF;
}

#openmrsSearchTable tbody tr:hover {
	background-color: #F0E68C;
	cursor: pointer;
}

.ui-state-default {
	font-weight: bold;
	background-image: none;
	background-color: white;
}

.ui-button {
	color: #1aad9b;
	font-family: Verdana, 'Lucida Grande', 'Trebuchet MS', Arial, Sans-Serif;
}

.fg-button {
	border-color: #1aad9b;
	font-family: Verdana, 'Lucida Grande', 'Trebuchet MS', Arial, Sans-Serif;
}

th.ui-state-default {
	border: none;
	color: black;
}

#openmrsSearchTable_info {
	padding-top: 10px;
	font-weight: bold;
}

#openmrsSearchTable_paginate {
	padding-top: 10px;
}

#openmrsSearchTable_length {
	float: left;
	text-align: left;
}
</style>
<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<script src="<openmrs:contextPath/>/dwr/interface/DWRMyModuleService.js"></script>
<script language="javascript">
	$j(window).load(function() {
		document.getElementById('findRHEAPatients').style.display = "block";
		document.getElementById('results').style.display = "block";
		document.getElementById("patientSearch").focus();
	});
</script>


<openmrs:htmlInclude file="/dwr/interface/DWRPatientService.js" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/css/dataTables_jui.css" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />
<%--<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/hiepatientsearch/scripts/openmrsSearch.js" />--%>

<openmrs:globalProperty key="patient.listingAttributeTypes"
	var="attributesToList" />

<script type="text/javascript">
	function doNav(patientId) {
		ctx = "${pageContext.request.contextPath}";
		document.location.href = ctx + '/patientDashboard.form?patientId='
				+ patientId;
	}

	var $j = jQuery.noConflict();

	var delay = (function() {
		var timer = 0;
		return function(callback, ms) {
			clearTimeout(timer);
			timer = setTimeout(callback, ms);
		};
	})();

	$j(document)
			.ready(
					function() {
						$j("#patientSearch")
								.keyup(
										function() {
											document.getElementById('loading').style.display = 'inline';
											$j('.filter').find('input:text')
													.val('');
											$j('.filter')
													.prop('checked', false);
											delay(function() {
												keydown();
											}, 400);

										});

						$j('.adh').change(function() {
							var level = $j(this).attr('id');
							//alert("Level : "+level);
							var selectedValue = $j(this).val();
							//alert("Value : "+selectedValue);
							if (selectedValue != "00") {
								loadFile(selectedValue, level);
							}
						});

						$j('#advanced').click(function() {
							$j('#filters').toggle(400);
							return false;
						});
						$j('#reset')
								.click(
										function() {
											$j('input.filter').val("");
											$j('#sex')
													.html(
															"Gender :<input type='radio' name='gender' id='genderMale' value='M' onchange='assess(0);' class='filter'>Male <input type='radio' name='gender'id='genderFemale' value='F' onchange='assess(0);' class='filter' >Female");
											$j(
													'#ad_district,#ad_sector,#ad_cell,#ad_village')
													.html(
															"<option value='00'> --Select-- </option>");
											$j('#ad_province')
													.html(
															"<option value='00'>--Select--</option><option value='01'>Kigali Province</option><option value='02'>Southern Province/Amajyepfo</option><option value='03'>Western Province/Uburengerazuba</option><option value='04'>Northern Province/Amajyaruguru</option><option value='05'>Eastern Province/Uburasirazuba</option><option value='55'>Show All Districts</option>");
										});

					});

	function processData(allText, id, level) {
		var allTextLines = allText.split(/\r\n|\n/);
		var headers = allTextLines[0].split(',');
		//var lines = [];
		var optionsValues = '<option value="00"> --Select-- </option>';
		var voidValues = '<option value="00"> --Select-- </option>';
		//optionsValues = '<select>';
		//alert("ID :"+ id);

		for ( var i = 0; i < allTextLines.length; i++) {
			var data = allTextLines[i].split(',');
			var selectToChange = "";
			if (level == "ad_province") {
				if (data[1] == id) {
					//lines.push(data[2]);
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}
				if (id == 55) {
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}

				selectToChange = "ad_district";
				document.getElementById("ad_sector").innerHTML = voidValues;
				document.getElementById("ad_cell").innerHTML = voidValues;
				document.getElementById("ad_village").innerHTML = voidValues;

			}
			if (level == "ad_district") {
				if (data[1] == id) {
					//lines.push(data[2]);
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}
				if (id == 55) {
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}

				selectToChange = "ad_sector";
				document.getElementById("ad_cell").innerHTML = voidValues;
				document.getElementById("ad_village").innerHTML = voidValues;
			}
			if (level == "ad_sector") {
				if (data[1] == id) {
					//lines.push(data[2]);
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}
				if (id == 55) {
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}

				selectToChange = "ad_cell";
				document.getElementById("ad_village").innerHTML = voidValues;
			}
			if (level == "ad_cell") {
				if (data[1] == id) {
					//lines.push(data[2]);
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}
				if (id == 55) {
					optionsValues += '<option value="' + data[0] + '">'
							+ data[2] + '</option>';
				}
				selectToChange = "ad_village";
			}

		}

		if (level == "ad_province") {
			optionsValues += '<option value="55">Show All Sectors</option>';
		}
		if (level == "ad_district") {
			optionsValues += '<option value="55">Show All Cells</option>';
		}
		if (level == "ad_sector") {
			optionsValues += '<option value="55">Show All Villages</option>';
		}

		//alert("Niveau :"+ level);
		document.getElementById(selectToChange).innerHTML = optionsValues;
		//alert(lines);
		//optionsValues += '</select>';
		//var options = $j('#district');
		//options.replaceWith(optionsValues);

		//optionsValues = '';

	}
	function loadFile(addressValue, level) {
		var path = "${pageContext.request.contextPath}/moduleResources/hiepatientsearch/locations/";
		//var path = ctx+"moduleResources/hiepatientsearch/";
		//alert(ctx);
		//var path = "";
		if (level == "ad_province") {
			//path += "${pageContext.request.contextPath}/moduleResources/hiepatientsearch/locations/District.txt";
			path += "District.txt";
		}
		if (level == "ad_district") {
			path += "Sector.txt";
		}
		if (level == "ad_sector") {
			path += "Cell.txt";
		}
		if (level == "ad_cell") {
			path += "Village.txt";
		}

		$j.ajax({
			type : "GET",
			url : path,
			dataType : "text",
			success : function(data) {
				processData(data, addressValue, level);
			}
		});

	}
</script>
<h2>
	<spring:message code="hiepatientsearch.search.title" />
</h2>

<div>
	<b class="boxHeader"><spring:message code="Patient.find" /></b>
	<div class="box">

		<div id="findRHEAPatients" style="display: none" align="left">
			<div>
				&nbsp;&nbsp; Patient Identifier or Patient Name:<input type="text"
					id="patientSearch"><span id="loading" style="display: none">
					<input type="image"
					src="${pageContext.request.contextPath}/moduleResources/hiepatientsearch/media/images/loading.gif">
				</span><br /> <span id="advanced" style="display: none"><a href="#"
					style="color: blue;">Show/Hide Search Filters</a></span>
			</div>

			<div id="errorMsg" style="color: #FF0000"></div>
			<br />
		</div>

		<div id="filters" style="display: none;">
			<fieldset>
				<legend>
					<b>Search Result Filters</b>
				</legend>
				<span id="filter_loading"
					style="display: none; float: right; font-style: italic; font-size: x-small;">loading
					<input type="image"
					src="${pageContext.request.contextPath}/moduleResources/hiepatientsearch/media/images/filter_loading.gif">
				</span> <br /> DOB : <input type="date" name="date" id="date"
					oninput="assess(0)" class="filter"> &nbsp;&nbsp;Age between
				<input type="text" id="moreThanAge" name="moreThanAge"
					onkeyup="assess(0)" class="filter"> and <input type="text"
					id="lessThanAge" name="lessThanAge" onkeyup="assess(0)"
					class="filter"><br /> <br /> <span id="sex">Gender
					:<input type="radio" name="gender" id="genderMale" value="M"
					onchange="assess(0);" class="filter">Male <input
					type="radio" name="gender" id="genderFemale" value="F"
					onchange="assess(0);" class="filter">Female
				</span><br /> <br />
				<%--Province:
				<form:select path="stateProvince" id="province"
					items="${stateProvince}" onchange="assess(0);" />
				District:
				<form:select path="countryDistrict" id="district"
					items="${countryDistrict}" onchange="assess(0);" />
				Sector:
				<form:select path="sector" id="sectors" items="${sector}"
					onchange="assess(0);" />
				Cell:
				<form:select path="cell" id="cells" items="${cell}"
					onchange="assess(0);" />
				Village:
				<form:select path="village" id="villages" items="${village}"
					onchange="assess(0);" />--%>

				Province: <select name="prov" id="ad_province" class="adh filter"
					onchange="assess(0);">
					<option value="00">--Select--</option>
					<option value="01">Kigali Province</option>
					<option value="02">Southern Province/Amajyepfo</option>
					<option value="03">Western Province/Uburengerazuba</option>
					<option value="04">Northern Province/Amajyaruguru</option>
					<option value="05">Eastern Province/Uburasirazuba</option>
					<option value="55">Show All Districts</option>

				</select> District: <select name="dist" id="ad_district" class="adh filter"
					onchange="assess(0);">
					<option value="00">--Select--</option>
				</select> Sector: <select name="sect" id="ad_sector" class="adh filter"
					onchange="assess(0);">
					<option value="00">--Select--</option>
				</select> Cell: <select name="cell" id="ad_cell" class="adh filter"
					onchange="assess(0);">
					<option value="00">--Select--</option>
				</select> Village: <select name="vill" id="ad_village" onchange="assess(0);"
					class="filter">
					<option value="00">--Select--</option>
				</select> <br /> <input type="hidden" id="patientIds" name="patientIds">
				<input type="hidden" id="stateProvince" name="stateProvince">
				<input type="hidden" id="countryDistrict" name="countryDistrict">
				<input type="hidden" id="sector" name="sector"> <input
					type="hidden" id="cell" name="cell"> <input type="hidden"
					id="village" name="village"> <input id="reset"
					type="button" value="Reset!" style="float: right;">
			</fieldset>
			<br />

		</div>

		<div id="searchString"></div>
		<div id="results" style="display: none;"></div>
		<div id="resultsBackup" style="display: none"></div>

		<table cellpadding="2" cellspacing="0" style="width: 100%">
			<c:if test="${fn:length(patientLists) > 0}">
				<tr>
					<th>Identifer</th>
					<th>Given Name</th>
					<th>Middle Name</th>
					<th>Family Name</th>
					<th>Gender</th>
					<th>Province</th>
					<th>District</th>
					<th>Sector</th>
					<th>Cell</th>
					<th>Village</th>
				</tr>
				<c:forEach var="patient" items="${patientLists}"
					varStatus="varStatus">
					<tr
						<c:if test="${varStatus.index % 2 == 0}"> style="background-color: #E6E6FA"</c:if>>

						<td>${patient.patientId}</td>
						<td>${patient.givenName}</td>
						<td>${patient.middleName}</td>
						<td>${patient.familyName}</td>
						<td>${patient.gender}</td>
						<td>${patient.personAddress.stateProvince}</td>
						<td>${patient.personAddress.countyDistrict}</td>
						<td>${patient.personAddress.cityVillage}</td>
						<td>${patient.personAddress.address3}</td>
						<td>${patient.personAddress.address1}</td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<c:if test="${noResults == true}">
			<div style="background-color: lightpink;">Sorry, your search
				did not result in any matching results. Please try again</div>
		</c:if>
	</div>
</div>
<br />
<div></div>
<c:if test="${empty model.hideAddNewPatient}">
	<openmrs:hasPrivilege privilege="Add Patients">
		<br /> &nbsp; <spring:message code="general.or" />
		<br />
		<br />
		<c:set var="baseUrl" value="${pageContext.request.contextPath}" />
		<openmrs:portlet id="addPersonForm" url="addPersonForm"
			parameters="personType=patient|postURL=${baseUrl}/admin/person/addPerson.htm|viewType=shortEdit" />
	</openmrs:hasPrivilege>
</c:if>


<%@ include file="/WEB-INF/template/footer.jsp"%>
