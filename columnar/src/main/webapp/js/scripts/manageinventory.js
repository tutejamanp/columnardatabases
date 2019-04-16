

$(document).ready(function() {

   var udata = JSON.parse(localStorage.getItem("userdata"));

	//Get items of the Seller
	var api = "http://localhost:8055/amazon.com/webapi/ProductController/productsBysellerId/"+udata.id;
		
		
	$.get(api , function(data, status){
	//    data = JSON.parse(data);
		data.forEach(function(product){
		console.log("product is : ");
		console.log(product);
		
		var Template = "";
		
	    productInfo = data[0];
	   // console.log(productInfo.product_images[0]);
	    Template = Template + "<tr><td>";
	    Template = Template +"<figcaption class='media-body'><h6 class='title text-truncate'>"+product.productname+" </h6>";
	    Template = Template + "</figcaption></figure></td><td><div class='price-wrap'><var class='price'>INR "+product.discountedprice+"</var>"; 
	    Template = Template + "<small class='text-muted'>"+product.description+"</small>";
	    Template = Template + "</div></td><td><div class='price-wrap'><var class='price'>"+product.quantityleft+"</var></div>";
	    Template = Template + "</td><td class='text-right'><form method='post'><div class='btn-group btn btn-success' role='group' onclick=\"updateProduct('"+product.id+"');\">Update </div>";
	    Template = Template + "</td><td class='text-right'><form method='post'><div class='btn-group btn btn-success' role='group' onclick=\"deleteProduct('"+product.id+"');\">Delete </div>";
	    Template = Template + "</form></td></tr>";
	    
	    $('#shipments').append(Template);
		
		
		
		});
	   	   // $('#product_hidden').html("<input type='hidden' name='product_id' value='"+localStorage.getItem("viewingProduct")+"' id='product_id' />");
	});
	
});


function updateProduct(productid) {
	
	localStorage.setItem("update_prod_id", productid);
	window.location = "updateproduct.jsp"
//	console.log("update product successful");
//	var api = "http://localhost:8055/amazon.com/webapi/ProductController/productsById/"+productid;
//	
//	
//	$.get(api , function(product, status){
//			console.log("product is : ");
//			console.log(product);		
//	});
		
}

function deleteProduct(productid) {
    $.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/deleteproduct/"+productid, 
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


