var $f = jQuery.noConflict();

/*function testing(){

    var testVal = 'johnny';
    window.alert(testVal);
    DWRMyModuleService.loadFile(testVal,obx);

    function obx(locs){
        var listVals = 'Start <br />';
          for(var i=0; i<locs.length ; i++) {
             listVals += locs[i] +'<br />';
          }
                 listVals += 'End'
           document.getElementById('baba').innerHTML = listVals;

         }
}*/

function assess(id) {
      //alert("change");
	 document.getElementById('filter_loading').style.display =   'inline';
	var patientSearchId = document.getElementById('patientSearch').value;

	if (patientSearchId == '') {
		document.getElementById('errorMsg').innerHTML = "Please enter a Patient name or Identifier first ";
		document.getElementById('filters').style.display =   'none';
        document.getElementById('advanced').style.display = 'none';
        document.getElementById('results').style.display = 'none';
        document.getElementById('searchString').style.display = 'none';
        document.getElementById('filter_loading').style.display =   'none';
		return;
	} else {
		//document.getElementById('filters').style.display = 'block';
        document.getElementById('advanced').style.display = 'inline';
        document.getElementById('results').style.display = 'block';
        document.getElementById('searchString').style.display = 'block';
	}

	var table = document.getElementById('backupTable');
	var gender;
	var moreThanAge = document.getElementById('moreThanAge').value;
	var lessThanAge = document.getElementById('lessThanAge').value;

	var moreThanAgeString = moreThanAge;
	moreThanAgeString = moreThanAgeString.replace(/[^\d]/g, '');

	document.getElementById('moreThanAge').value = moreThanAgeString;

	var lessThanAgeString = lessThanAge;
	lessThanAgeString = lessThanAgeString.replace(/[^\d]/g, '');

	document.getElementById('lessThanAge').value = lessThanAgeString;

	var dob = document.getElementById('date').value;

	var province = "";
    if($f("#ad_province").val() != "00" && $f("#ad_province").val() != "55" ){ province = $f("#ad_province option:selected").text(); }

	var district = "";
    if($f("#ad_district").val() != "00" && $f("#ad_district").val() != "55"){ district = $f("#ad_district option:selected").text(); }

	var sectors = "";
    if($f("#ad_sector").val() != "00" && $f("#ad_sector").val() != "55"){ sectors = $f("#ad_sector option:selected").text(); }

	var cells = "";
    if($f("#ad_cell").val() != "00" && $f("#ad_cell").val() != "55"){ cells = $f("#ad_cell option:selected").text(); }

	var villages = "";
    if($f("#ad_village").val() != "00"){ villages = $f("#ad_village option:selected").text(); }

	var inputs = document.getElementsByName("gender");
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked) {
			gender = inputs[i].value;
		}
	}

	var idString;
	for ( var i = 1, row; row = table.rows[i]; i++) {
		if (idString == undefined) {
			idString = row.cells[0].innerHTML + ",";
		} else {
			idString = idString + "," + row.cells[0].innerHTML;
		}
	}



	function obj(patientList) {
		
		var theader = '<table id="openmrsSearchTable" cellpadding="2" cellspacing="0" style="width: 100%">';
		var tbkupheader = '<table id="backupTable"  style="width: 100%">';
		var tbody = '';
		tbody += '<thead><tr><th>Identifier</th><th>Given</th><th>Middle</th><th>Family Name</th><th>Age</th><th>BirthDate</th><th>Gender</th><th>Province</th><th>District</th><th>Sector</th><th>Cell</th><th>Village</th></tr></thead><tbody>';

		for ( var i = 0; i < patientList.length; i++) {
			if (i % 2 == 0) {
				tbody += '<tr id="' + i + '"onclick="doNav('
						+ patientList[i].patientId + ');">';
			} else {
				tbody += '<tr id="' + i + '"onclick="doNav('
						+ patientList[i].patientId + ');">';
			}
			tbody += '<td>';
			tbody += patientList[i].patientId;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].givenName;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].middleName;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].familyName;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].age;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].pbd;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].gender;
			tbody += '</td>'

			if (patientList[i].stateProvince == undefined) {
				patientList[i].stateProvince = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].stateProvince;
			tbody += '</td>'

			if (patientList[i].countryDistrict == undefined) {
				patientList[i].countryDistrict = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].countryDistrict;
			tbody += '</td>'

			if (patientList[i].sector == undefined) {
				patientList[i].sector = ' ';
			}
			tbody += '<td>';
			tbody += patientList[i].sector;
			tbody += '</td>'

			if (patientList[i].cell == undefined) {
				patientList[i].cell = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].cell;
			tbody += '</td>'

			if (patientList[i].village == undefined
					|| patientList[i].village == null) {
				patientList[i].village = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].village;
			tbody += '</td>'

			tbody += '</tr>\n';
		}
		var tfooter = '</tbody></table>';
		document.getElementById('results').innerHTML = theader + tbody
				+ tfooter;
        /*document.getElementById('resultsBackup').innerHTML = tbkupheader
            + tbody + tfooter;*/
		
		$f('#openmrsSearchTable')
				.dataTable(
						{
							"sPaginationType" : "full_numbers",
							"bJQueryUI" : true,
							bSort : false,
							bFilter : false,
							sDom : '<f<t><"clear"><"clear"><"clear">ip<"clear"><"clear"><"clear"><"clear">l>'

						});
		document.getElementById('filter_loading').style.display =   'none';
		if (patientSearchId != '') {
			var data_Length = $f("select[name=openmrsSearchTable_length]")
					.val();
			var pg = patientList.length / data_Length;

			var pageCheck = (pg % 1) * 10;

			pg = Math.round(pg);
			// noPages = noPages + 1;
			// alert(noPages);

			if (pageCheck < 5) {
				pg = pg + 1;
			}
			document.getElementById('searchString').innerHTML = "Viewing results for '<strong>"
					+ document.getElementById("patientSearch").value
					+ "</strong>' - "+pg+" pages";

		}
		
		

	}
	
    DWRMyModuleService.filter(idString, moreThanAgeString, lessThanAgeString,
        gender, dob, province, district, sectors, cells, villages, obj);

}

function keydown() {

	 //var objects;

	document.getElementById('errorMsg').innerHTML = "";
	var patientSearchId = document.getElementById('patientSearch').value;
	
	if (patientSearchId == '') {
		document.getElementById('errorMsg').innerHTML = "Please enter a Patient name or Identifier first ";
		document.getElementById('filters').style.display = 'none';
        document.getElementById('advanced').style.display = 'none';
        document.getElementById('results').style.display = 'none';
        document.getElementById('searchString').style.display = 'none';
        document.getElementById('loading').style.display =   'none';
		return;
	} else {
		//document.getElementById('filters').style.display = 'block';
        document.getElementById('advanced').style.display = 'inline';
        document.getElementById('results').style.display = 'block';
        document.getElementById('searchString').style.display = 'block';
	}

    if(patientSearchId.length >= 3){


	function objects(patientList) {
		var theader = '<table id="openmrsSearchTable" cellpadding="2" cellspacing="0" style="width: 100%">';
		var tbkupheader = '<table id="backupTable"  style="width: 100%">';
		var tbody = '';
		tbody += '<thead><tr><th>Identifier</th><th>Given</th><th>Middle</th><th>Family Name</th><th>Age</th><th>BirthDate</th><th>Gender</th><th>Province</th><th>District</th><th>Sector</th><th>Cell</th><th>Village</th></tr></thead><tbody>';

		for ( var i = 0; i < patientList.length; i++) {
			if (i % 2 == 0) {
				tbody += '<tr id="' + i + '"onclick="doNav('
						+ patientList[i].patientId + ');">';
			} else {
				tbody += '<tr id="' + i + '"onclick="doNav('
						+ patientList[i].patientId + ');">';
			}
			tbody += '<td>';
			tbody += patientList[i].patientId;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].givenName;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].middleName;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].familyName;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].age;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].pbd;
			tbody += '</td>'

			tbody += '<td>';
			tbody += patientList[i].gender;
			tbody += '</td>'

			if (patientList[i].stateProvince == undefined) {
				patientList[i].stateProvince = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].stateProvince;
			tbody += '</td>'

			if (patientList[i].countryDistrict == undefined) {
				patientList[i].countryDistrict = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].countryDistrict;
			tbody += '</td>'

			if (patientList[i].sector == undefined) {
				patientList[i].sector = ' ';
			}
			tbody += '<td>';
			tbody += patientList[i].sector;
			tbody += '</td>'

			if (patientList[i].cell == undefined) {
				patientList[i].cell = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].cell;
			tbody += '</td>'

			if (patientList[i].village == undefined
					|| patientList[i].village == null) {
				patientList[i].village = ' ';
			}

			tbody += '<td>';
			tbody += patientList[i].village;
			tbody += '</td>'

			tbody += '</tr>\n';
		}
		var tfooter = '</tbody></table>';
		document.getElementById('results').innerHTML = theader + tbody
				+ tfooter;
		document.getElementById('resultsBackup').innerHTML = tbkupheader
				+ tbody + tfooter;
		document.getElementById('loading').style.display =   'none';
		$f('#openmrsSearchTable')
				.dataTable(
						{
							"sPaginationType" : "full_numbers",
							"bJQueryUI" : true,
							bSort : false,
							bFilter : false,
							sDom : '<f<t><"clear"><"clear"><"clear">ip<"clear"><"clear"><"clear"><"clear">l>'

						});
		document.getElementById('filters').style.display = 'block';

		if (patientSearchId != '') {
			var data_Length = $f("select[name=openmrsSearchTable_length]")
					.val();
			var pg = patientList.length / data_Length;

			var pageCheck = (pg % 1) * 10;

			pg = Math.round(pg);
			// noPages = noPages + 1;
			// alert(noPages);

			if (pageCheck < 5) {
				pg = pg + 1;
			}
			document.getElementById('searchString').innerHTML = "Viewing results for '<strong>"
					+ document.getElementById("patientSearch").value
					+ "</strong>' - "+pg+" pages";

		}
	}
        DWRMyModuleService.findPatients(document.getElementById("patientSearch").value, false, objects);
}

}
function activate() {
	// link.className = "active";
	window.alert("function called");
}