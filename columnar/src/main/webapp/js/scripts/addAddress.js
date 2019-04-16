

$(document).ready(function() {

var udata = JSON.parse(localStorage.getItem("userdata"));

	var api = "http://localhost:8055/amazon.com/webapi/AddressController/customeraddress/"+udata.id;
	
	
	$.get(api , function(data, status){
		console.log(data);
		data.forEach(function(item) {
			var temp = "<tr><td>"+item.addressline1+", "+ item.addressline2 +", "+item.street+", "+ item.city +", "+item.pincode+"</td>";
			$('#myAddress').append(temp);
		});
	});


$('#addStudentImageSelect').change(function (e) {
	    
	    var oFReader = new FileReader();
	    oFReader.readAsDataURL(document.getElementById("addStudentImageSelect").files[0]);
	
	    oFReader.onload = function (oFREvent) {
	        document.getElementById("addStudentImage").src = oFREvent.target.result;
	    };
	});

});


var mealsByCategory = {
	    "Skin Care": ["Face Cream", "Face wash"],
	    "Hair and Others": ["Hair oil", "Shampoo", "Conditioner", "Hair cream"]
	}

	    function changecat(value) {
	        if (value.length == 0) document.getElementById("subcategory").innerHTML = "<option></option>";
	        else {
	            var catOptions = "";
	            for (categoryId in mealsByCategory[value]) {
	                catOptions += "<option>" + mealsByCategory[value][categoryId] + "</option>";
	            }
	            //document.getElementById("subcategory").innerHTML = catOptions;
	        }
	    }



function addAddress() {
	var form = $('#addressForm')[0];
    var data = new FormData(form);
    
    var udata = JSON.parse(localStorage.getItem("userdata"));
    console.log(udata.emailid);
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/AddressController/addAddress/"+udata.id,
        data: data, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	location.reload(true);
        }
    });
}