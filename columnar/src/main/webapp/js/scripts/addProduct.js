

$(document).ready(function() {
$('#addStudentImageSelect').change(function (e) {
	    
	    var oFReader = new FileReader();
	    oFReader.readAsDataURL(document.getElementById("addStudentImageSelect").files[0]);
	
	    oFReader.onload = function (oFREvent) {
	        document.getElementById("addStudentImage").src = oFREvent.target.result;
	    };
	});


	//check if seller details exists
	var udata = JSON.parse(localStorage.getItem("userdata"));
	var api;
	api = "http://localhost:8055/amazon.com/webapi/UserController/checkSellerDetails/" + udata.id;
	
	$.get(api , function(data, status){
		if(data == '0')
			window.location.href="addseller.jsp";
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



function addProduct() {
	var form = $('#productForm')[0];
    var data = new FormData(form);
    
    var udata = JSON.parse(localStorage.getItem("userdata"));
    
   
    var customlabels = [];
    var lnames = []
    $("table tr").each(function(i){
      if(i==0) return;
      var lname = $.trim($(this).find("td").eq(0).html());
      var lvalue = $.trim($(this).find("td").eq(1).html());
      customlabels.push(lname + "--" +lvalue);
    });
    
    console.log(customlabels.join(','));
    //alert(customlabels);
    
    data.append('customlabels',	customlabels);
 
    console.log(data);
    
    console.log(udata.emailid);
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/saveproduct/"+udata.id,
        data: data, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	alert(result);
        	if(result == "success")
        		location.reload(true);
        
        		
        }
    });
}


function updateProduct() {
	var form = $('#productForm')[0];
    var data = new FormData(form);
    
    var udata = JSON.parse(localStorage.getItem("userdata"));
    
   
    var customlabels = [];
    var lnames = []
    $("table tr").each(function(i){
      if(i==0) return;
      var lname = $.trim($(this).find("td").eq(0).html());
      var lvalue = $.trim($(this).find("td").eq(1).html());
      customlabels.push(lname + "--" +lvalue);
    });
    
    console.log(customlabels.join(','));
    //alert(customlabels);
    
    data.append('customlabels',	customlabels);
 
    console.log(data);
    var productid = $('#prodid').val();
    console.log(udata.emailid);
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/updateexistingproduct/"+udata.id+"/"+productid,
        data: data, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	alert(result);
        	if(result == "success")
        		location.reload(true);
        
        		
        }
    });
}


function deleteProduct() {
	var form = $('#productForm')[0];
    var data = new FormData(form);
    
    var udata = JSON.parse(localStorage.getItem("userdata"));

    console.log(data);
    var productid = $('#prodid').val();
    console.log(udata.emailid);
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/deleteproduct/"+productid,
        data: data, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	alert(result);
        	if(result == "success")
        		location.reload(true);
        
        		
        }
    });
}




function loadproduct(productid) {
	var api;
	api = "http://localhost:8055/amazon.com/webapi/ProductController/productsById/"+productid;


	$.get(api , function(data, status){
		
		$('#productid').val(data[0].productid);
		$('#productname').val(data[0].productname);
		$('#price').val(data[0].price);
		$('#discountedprice').val(data[0].discountedprice);
		$('#description').val(data[0].description);
		$('#prodid').val(data[0].id);
		
		var date = new Date(data[0].manufactured_date);
		function getFormattedDate(date) {

			  const year  = date.getFullYear(),
			        month = ('0' + (date.getMonth() + 1)).slice(-2),
			        day   = ('0' + date.getDate()).slice(-2);

			  return [year, month, day].join('-');
			}
		$('#manDate').val(getFormattedDate(date));
		//$('#quantityleft').val(data[0].quantityleft);
		$('#quantity').val(data[0].quantityleft);
		$('#manname').val(data[0].manufacturer);
		$('#addStudentImage').attr("src",data[0].product_images[0].url);
	
		
		console.log(data);
			});
}

