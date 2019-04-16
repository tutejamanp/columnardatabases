function make_review(){
	var form = $('#ratingForm')[0];
    var data = new FormData(form);
    //alert(localStorage.getItem("viewingProduct"));
    var udata = JSON.parse(localStorage.getItem("userdata"));
    console.log(udata.emailid);
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProdReviewController/savereview/"+localStorage.getItem("viewingProduct")+"/"+udata.id,
        data: data, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	alert("Successfully Left The Review");

        	window.location.href = "product_detail.jsp";
        }
    });
}