function displayCart(){
	
	if(localStorage.getItem("directBuy") != null) {
		var api = "http://localhost:8055/amazon.com/webapi/ProductController/productsById/"+localStorage.getItem("cartProduct");
		var product;

		//Ajax Call for getting individual product
		$.get(api , function(data, status){
		    product = data[0];
		    console.log(product);
		    
		    var udata = JSON.parse(localStorage.getItem("userdata"));

		    var responsebirthdate = new Date(udata.dob);
			var currentdate = new Date();
			var discountedprice;
			var freequantity = 0;
			//localStorage.setItem('offerType', product.offerType);
			if(product.offerType == "discount" && Date.now() < product.offerEndDate && Date.now() >= product.offerStartDate ) {
				discountedprice = product.discountedprice * (100 - product.offerdiscpercent)/100;
			}
			else if(product.offerType == "buy1get1" && Date.now() < product.offerEndDate && Date.now() >= product.offerStartDate) {
				freequantity = localStorage.getItem("cartProductQty");
				console.log("manp free quantity is : "+freequantity);
				discountedprice = product.discountedprice;
			}
			else if(product.offerType == "buy3get1" && Date.now() < product.offerEndDate && Date.now() >= product.offerStartDate) {
				freequantity = localStorage.getItem("cartProductQty");
				freequantity = Math.floor(freequantity/3);
				discountedprice = product.discountedprice;
			}
			else if(product.offerType == "birthday" && currentdate.getMonth() == responsebirthdate.getMonth() &&  currentdate.getDate() == responsebirthdate.getDate() ){
				discountedprice = product.discountedprice * (100 - product.offerdiscpercent)/100;
				
			}else{
				discountedprice = product.discountedprice;
			}
			
			alert("hey hi manp inside display cart function, freequantity is : "+freequantity);
			
			localStorage.setItem("freequantity", freequantity);
		    $('#cartItems').append(generateItem(product.product_images[0].url, product.productname, product.id, localStorage.getItem("cartProductQty"), discountedprice));
		    var discount = (product.price - discountedprice)*parseInt(localStorage.getItem("cartProductQty"));
		    
		    $('#discount').html(discount);
		    $('#price').html(discountedprice*localStorage.getItem("cartProductQty"));
		    localStorage.setItem("cartProductValue", discountedprice*localStorage.getItem("cartProductQty"));
		    
		});
		
	} else {
		window.location.href = "404.jsp";
	}
}


function generateItem(productImageUrl, productName, productId, productQuantity, productPrice) {
	var myItem = "<tr><td><figure class='media'><div class='img-wrap'>";
	myItem = myItem + "<img src='"+ productImageUrl+ "' class='img-thumbnail img-sm'>";
	myItem = myItem + "</div><figcaption class='media-body'>";
	myItem = myItem + "<h6 class='title text-truncate'>"+productName+"</h6>";
	myItem = myItem + "</figcaption></figure></td><td>";
	if(Number(localStorage.getItem('freequantity')) > 0)
		myItem = myItem + "<strong>"+productQuantity+"+" + localStorage.getItem('freequantity')+"(Free)</strong>";
	else
		myItem = myItem + "<strong>"+productQuantity+"</strong>";
	myItem = myItem + "</td><td><div class='price-wrap'><var class='price'>INR "+productPrice*productQuantity+"</var>"; 
	myItem = myItem + "</div></td><td class='text-right'><a href='#' onclick='removeProduct();' class='btn btn-outline-danger'> × Remove</a></td></tr>";
	return myItem;
}

function removeProduct() {
	console.log("remove method has been called manp");
	localStorage.removeItem("cartProductQty");
	localStorage.removeItem("cartProduct");
	localStorage.removeItem("directBuy");
	window.location.href="home.jsp";

}

function additemintocart (product_id , product_quantity , customerid) {
	console.log("method call successfull");
	
	var api = "http://localhost:8055/amazon.com/webapi/ProductController/addproductincart/"+product_id+"/"+product_quantity+"/0/"+customerid;
	
	$.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: api,
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	alert(" Item sucessfully added into the cart");
        	location.reload(true);
        },
        error: function(data){
            alert("fail");
        }
    });
	
	
	
}

