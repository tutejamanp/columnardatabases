function updateProduct() {
	var form = $('#productForm')[0];
    var data = new FormData(form);
    var productid = localStorage.getItem("update_prod_id");
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/updateproductdetails/"+productid,
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