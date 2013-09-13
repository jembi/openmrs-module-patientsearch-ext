function doNav(patientId) {
	ctx = "${pageContext.request.contextPath}";
	document.location.href = ctx + '/patientDashboard.form?patientId='
			+ patientId;
}

function highlight(obj) {
	obj.style.backgroundColor = "#FFF68F";
}

function reColour(obj) {
	obj.style.backgroundColor = "#E6E6FA";
}

function reColourWhite(obj) {
	obj.style.backgroundColor = "white";
}

function assess(id) {

	var paginationId = document.getElementById("paginationId").value;
	var patientSearchId = document.getElementById('patientSearch').value;

	if (patientSearchId == '') {
		document.getElementById('errorMsg').innerHTML = "Please enter a Patient name or Identifier first ";
		document.getElementById('searchString').innerHTML = "";
		document.getElementById('pagination').style.display = 'none';
		document.getElementById('pages').style.display = 'none';
		document.getElementById('filters').style.display = 'none';
		return;
	} else {
		document.getElementById('searchString').innerHTML = "Viewing results for '<strong>"
				+ document.getElementById("patientSearch").value + "</strong>'";
		document.getElementById('pagination').style.display = 'block';
		document.getElementById('pages').style.display = 'block';
		document.getElementById('filters').style.display = 'block';
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
	var province = document.getElementById('province').value;
	var district = document.getElementById('district').value;
	var sectors = document.getElementById('sectors').value;
	var cells = document.getElementById('cells').value;
	var villages = document.getElementById('villages').value;

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

	if (idString != null && idString != "") {
		DWRMyModuleService.filter(idString, moreThanAgeString,
				lessThanAgeString, gender, dob, province, district, sectors,
				cells, villages, obj);

		function obj(patientList) {
			var theader = '<table id="resultsTable" bordercolor="white" frame="hsides" rules="rows" style="width: 100%">\n';
			var tbody = '';
			var paginatedBody = '';
			tbody += '<tr><th>Identifier</th><th>Given</th><th>Middle</th><th>Family Name</th><th>Age</th><th>BirthDate</th><th>Gender</th><th>Province</th><th>District</th><th>Sector</th><th>Cell</th><th>Village</th>';
			paginatedBody = tbody;

			var noPages;
			var pageString;
			if (patientList.length != 0 && patientList.length != undefined) {
				if (patientList.length > paginationId) {
					noPages = patientList.length / paginationId;
				}
				var pageCheck = (noPages % 1) * 10;
				noPages = Math.round(noPages);
				if (pageCheck < 5) {
					noPages = noPages + 1;
				}

				if (noPages >= 1) {

					for ( var i = noPages; i >= 1; i--) {
						if (pageString == undefined) {
							pageString = "";
						}

						var link = '<li><a href="Javascript:assess(' + i
								+ ');" onclick="activate(this)">' + i
								+ '</a></li>';
						var x = pageString + " | " + link;

						pageString = pageString + link;

					}
				}

				var count;
				if (paginationId >= patientList.length) {
					count = patientList.length;

				} else if (paginationId < patientList.length) {
					count = paginationId;
				}

				if (id <= 0) {
					id = 1;
				}
				var no = (id - 1) * paginationId;
				var newNo = +no + +count;

				no = no + 1;

				if (newNo > patientList.length) {
					newNo = patientList.length;
				}

				if (pageString != undefined) {

					document.getElementById('pages').innerHTML = "<ul id ='pagination-digg'>"
							+ pageString + "</ul>";
					document.getElementById('pageInfo').innerHTML = "Showing "
							+ no + " to " + newNo + " of " + patientList.length
							+ " entries";

				} else {
					document.getElementById('pageInfo').innerHTML = "Showing "
							+ 1 + " to " + patientList.length + " of "
							+ patientList.length + " entries";
				}
			}

			if (patientList.length == 0) {

				tbody += '<tr id="' + i + '"style="background-color: #E6E6FA">';
				tbody += '<td colspan="12" class="centered-cell">No matching records found</td>';
				tbody += '</tr>\n';
			}

			else if (patientList.length != 0
					&& (paginationId != '' || paginationId != undefined)) {
				var counter;
				if (paginationId >= patientList.length) {
					counter = patientList.length;

				} else if (paginationId < patientList.length) {
					counter = paginationId;
				}

				if (id == 0) {

					for ( var i = 0; i < counter; i++) {

						if (i % 2 == 0) {
							paginatedBody += '<tr class = "point" id="'
									+ i
									+ '"style="background-color: #E6E6FA" onclick="doNav('
									+ patientList[i].patientId
									+ ');" onMouseover="highlight(this);" onMouseout="reColour(this);"  >';
						} else {
							paginatedBody += '<tr class = "point" id="'
									+ i
									+ '" onclick="doNav('
									+ patientList[i].patientId
									+ ');"  onMouseover="highlight(this);" onMouseout="reColourWhite(this);" >';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].patientId;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].givenName;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].middleName;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].familyName;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].age;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].birthdateString;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].gender;
						paginatedBody += '</td>'

						if (patientList[i].stateProvince == undefined) {
							patientList[i].stateProvince = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].stateProvince;
						paginatedBody += '</td>'

						if (patientList[i].countryDistrict == undefined) {
							patientList[i].countryDistrict = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].countryDistrict;
						paginatedBody += '</td>'

						if (patientList[i].sector == undefined) {
							patientList[i].sector = ' ';
						}
						paginatedBody += '<td>';
						paginatedBody += patientList[i].sector;
						paginatedBody += '</td>'

						if (patientList[i].cell == undefined) {
							patientList[i].cell = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].cell;
						paginatedBody += '</td>'

						if (patientList[i].village == undefined
								|| patientList[i].village == null) {
							patientList[i].village = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].village;
						paginatedBody += '</td>'

						paginatedBody += '</tr>\n';

					}
				} else if (id >= 1) {

					var newCount = (id - 1) * paginationId;
					var newCheck = +newCount + +counter;

					if (newCheck > patientList.length) {
						newCheck = patientList.length;
					}

					for ( var i = newCount; i < newCheck; i++) {

						if (i % 2 == 0) {
							paginatedBody += '<tr class = "point" id="'
									+ i
									+ '"style="background-color: #E6E6FA" onclick="doNav('
									+ patientList[i].patientId
									+ ');" onMouseover="highlight(this);" onMouseout="reColour(this);"  >';
						} else {
							paginatedBody += '<tr class = "point" id="'
									+ i
									+ '" onclick="doNav('
									+ patientList[i].patientId
									+ ');"  onMouseover="highlight(this);" onMouseout="reColourWhite(this);" >';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].patientId;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].givenName;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].middleName;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].familyName;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].age;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].birthdateString;
						paginatedBody += '</td>'

						paginatedBody += '<td>';
						paginatedBody += patientList[i].gender;
						paginatedBody += '</td>'

						if (patientList[i].stateProvince == undefined) {
							patientList[i].stateProvince = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].stateProvince;
						paginatedBody += '</td>'

						if (patientList[i].countryDistrict == undefined) {
							patientList[i].countryDistrict = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].countryDistrict;
						paginatedBody += '</td>'

						if (patientList[i].sector == undefined) {
							patientList[i].sector = ' ';
						}
						paginatedBody += '<td>';
						paginatedBody += patientList[i].sector;
						paginatedBody += '</td>'

						if (patientList[i].cell == undefined) {
							patientList[i].cell = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].cell;
						paginatedBody += '</td>'

						if (patientList[i].village == undefined
								|| patientList[i].village == null) {
							patientList[i].village = ' ';
						}

						paginatedBody += '<td>';
						paginatedBody += patientList[i].village;
						paginatedBody += '</td>'

						paginatedBody += '</tr>\n';

					}
				}
			}

			var tfooter = '</table> ';

			if (patientList.length == 0)
				document.getElementById('results').innerHTML = theader + tbody
						+ tfooter;
			else
				document.getElementById('results').innerHTML = theader
						+ paginatedBody + tfooter;
		}
	}
}

function keydown() {

	var paginationId = document.getElementById("paginationId").value;

	document.getElementById('errorMsg').innerHTML = "";
	var patientSearchId = document.getElementById('patientSearch').value;

	if (patientSearchId == '') {
		document.getElementById('errorMsg').innerHTML = "Please enter a Patient name or Identifier first ";
		document.getElementById('searchString').innerHTML = "";
		document.getElementById('pagination').style.display = 'none';
		document.getElementById('pages').style.display = 'none';
		document.getElementById('filters').style.display = 'none';
		return;
	} else {
		document.getElementById('searchString').innerHTML = "Viewing results for '<strong>"
				+ document.getElementById("patientSearch").value + "</strong>'";
		document.getElementById('pagination').style.display = 'block';
		document.getElementById('pages').style.display = 'block';
		document.getElementById('filters').style.display = 'block';
	}
	DWRMyModuleService.findPatients(
			document.getElementById("patientSearch").value, false, objects);

	function objects(patientList) {

		var theader = '<table id="resultsTable" bordercolor="white" frame="hsides" rules="rows" style="width: 100%">\n';
		var tbkupheader = '<table id="backupTable"  style="width: 100%">\n';
		var tbody = '';
		var paginatedBody = '';

		tbody += '<tr><th>Identifier</th><th>Given</th><th>Middle</th><th>Family Name</th><th>Age</th><th>BirthDate</th><th>Gender</th><th>Province</th><th>District</th><th>Sector</th><th>Cell</th><th>Village</th>';
		paginatedBody = tbody;

		var noPages;
		var pageString;
		if (patientList.length != 0 && patientList.length != undefined) {
			if (patientList.length > paginationId) {
				noPages = patientList.length / paginationId;
			}
			var pageCheck = (noPages % 1) * 10;

			noPages = Math.round(noPages);

			if (pageCheck < 5) {
				noPages = noPages + 1;
			}

			if (noPages >= 1) {

				for ( var i = noPages; i >= 1; i--) {
					if (pageString == undefined) {
						pageString = "";
					}

					var link = '<li><a href="Javascript:assess(' + i
							+ '); " onclick="activate(this)" >' + i
							+ '</a></li>';
					var x = pageString + " | " + link;

					pageString = pageString + link;

				}
			}

			var count;
			if (paginationId >= patientList.length) {
				count = patientList.length;

			} else if (paginationId < patientList.length) {
				count = paginationId;
			}

			var no = 1;
			count = count - 1;
			var newNo = +no + +count;

			if (newNo > patientList.length) {
				newNo = patientList.length;
			}

			if (pageString != undefined) {

				document.getElementById('pages').innerHTML = "<ul id ='pagination-digg'>"
						+ pageString + "</ul>";
				document.getElementById('pageInfo').innerHTML = "Showing " + no
						+ " to " + newNo + " of " + patientList.length
						+ " entries";

			} else {

				var no = 1;
				document.getElementById('pageInfo').innerHTML = "Showing " + no
						+ " to " + patientList.length + " of "
						+ patientList.length + " entries";
			}
		}

		if (patientList.length == 0) {
			tbody += '<tr id="' + i + '"style="background-color: #E6E6FA">';
			tbody += '<td colspan="12" class="centered-cell">No matching records found</td>';
			tbody += '</tr>\n';
		}

		else {

			for ( var i = 0; i < patientList.length; i++) {
				if (i % 2 == 0) {
					tbody += '<tr class = "point" id="'
							+ i
							+ '"style="background-color: #E6E6FA" onclick="doNav('
							+ patientList[i].patientId
							+ ');" onMouseover="highlight(this);" onMouseout="reColour(this);"  >';
				} else {
					tbody += '<tr class = "point" id="'
							+ i
							+ '" onclick="doNav('
							+ patientList[i].patientId
							+ ');"  onMouseover="highlight(this);" onMouseout="reColourWhite(this);" >';
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
				tbody += patientList[i].birthdateString;
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
		}

		if (patientList.length != 0
				&& (paginationId != '' || paginationId == undefined)) {
			var counter;
			if (paginationId >= patientList.length) {
				counter = patientList.length;
			} else if (paginationId < patientList.length) {
				counter = paginationId;
			}

			for ( var i = 0; i < counter; i++) {
				if (i % 2 == 0) {
					paginatedBody += '<tr class = "point" id="'
							+ i
							+ '"style="background-color: #E6E6FA" onclick="doNav('
							+ patientList[i].patientId
							+ ');" onMouseover="highlight(this);" onMouseout="reColour(this);"  >';
				} else {
					paginatedBody += '<tr class = "point" id="'
							+ i
							+ '" onclick="doNav('
							+ patientList[i].patientId
							+ ');"  onMouseover="highlight(this);" onMouseout="reColourWhite(this);" >';
				}

				paginatedBody += '<td>';
				paginatedBody += patientList[i].patientId;
				paginatedBody += '</td>'

				paginatedBody += '<td>';
				paginatedBody += patientList[i].givenName;
				paginatedBody += '</td>'

				paginatedBody += '<td>';
				paginatedBody += patientList[i].middleName;
				paginatedBody += '</td>'

				paginatedBody += '<td>';
				paginatedBody += patientList[i].familyName;
				paginatedBody += '</td>'

				paginatedBody += '<td>';
				paginatedBody += patientList[i].age;
				paginatedBody += '</td>'

				paginatedBody += '<td>';
				paginatedBody += patientList[i].birthdateString;
				paginatedBody += '</td>'

				paginatedBody += '<td>';
				paginatedBody += patientList[i].gender;
				paginatedBody += '</td>'

				if (patientList[i].stateProvince == undefined) {
					patientList[i].stateProvince = ' ';
				}

				paginatedBody += '<td>';
				paginatedBody += patientList[i].stateProvince;
				paginatedBody += '</td>'

				if (patientList[i].countryDistrict == undefined) {
					patientList[i].countryDistrict = ' ';
				}

				paginatedBody += '<td>';
				paginatedBody += patientList[i].countryDistrict;
				paginatedBody += '</td>'

				if (patientList[i].sector == undefined) {
					patientList[i].sector = ' ';
				}
				paginatedBody += '<td>';
				paginatedBody += patientList[i].sector;
				paginatedBody += '</td>'

				if (patientList[i].cell == undefined) {
					patientList[i].cell = ' ';
				}

				paginatedBody += '<td>';
				paginatedBody += patientList[i].cell;
				paginatedBody += '</td>'

				if (patientList[i].village == undefined
						|| patientList[i].village == null) {
					patientList[i].village = ' ';
				}

				paginatedBody += '<td>';
				paginatedBody += patientList[i].village;
				paginatedBody += '</td>'

				paginatedBody += '</tr>\n';
			}
		}

		var tfooter = '</table>';

		if (patientList.length == 0)
			document.getElementById('results').innerHTML = theader + tbody
					+ tfooter;
		else
			document.getElementById('results').innerHTML = theader
					+ paginatedBody + tfooter;

		document.getElementById('resultsBackup').innerHTML = tbkupheader
				+ tbody + tfooter;

	}
}

function activate(link) {
	link.className = "active";
}