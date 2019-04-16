



$(document).ready(function() {

   var udata = JSON.parse(localStorage.getItem("userdata"));

	//Get items of the Customer
	var api = "http://localhost:8055/amazon.com/webapi/UserController/getUserWishlist/"+udata.id;
		
		
	$.get(api , function(data, status){
	//    data = JSON.parse(data);
		console.log(data);
		console.log("sorted till this nnj point manp");

		data.forEach(function(product){
			productInfo = product;
			console.log(productInfo);
			//qty = cartitem.quantity;
			
			
			var udata = JSON.parse(localStorage.getItem("userdata"));
		    var responsebirthdate = new Date(udata.dob);
			var currentdate = new Date();
			var discountedprice;
			if(productInfo.offerType == "discount" && Date.now() < productInfo.offerEndDate) {
				discountedprice = productInfo.discountedprice * (100 - productInfo.offerdiscpercent)/100;
			}
			else if(productInfo.offerType == "birthday" && currentdate.getMonth() == responsebirthdate.getMonth() &&  currentdate.getDate() == responsebirthdate.getDate() ){
				discountedprice = productInfo.discountedprice * (100 - productInfo.offerdiscpercent)/100;
				
			}else{
				//alert("Comes here ");
				discountedprice = productInfo.discountedprice;
			}
			
			
			var Template = "";    
					    Template = Template + "<tr><td onclick=\"viewProductDetails('"+productInfo.id+"');\" ><figure class='media'>	<div class='img-wrap'><img src='"+productInfo.product_images[0].url+"' class='img-thumbnail img-sm'></div>";
					    Template = Template +"<figcaption class='media-body'><h6 class='title text-truncate'>"+productInfo.productname+" </h6>";
					    Template = Template + "</figcaption></figure></td><td><div class='price-wrap'><var class='price'>INR "+discountedprice+"</var>"; 
					    Template = Template + "<small class='text-muted'>(INR"+discountedprice+" each)</small>";
					    //Template = Template + "</div></td><td><div class='price-wrap'><var class='price'>"+qty+"</var></div>";
					    Template = Template + "</td><td class='text-right'><form method='post'><div class='btn-group btn btn-success' role='group' onclick=\"removeitemfromwishlist('"+productInfo.id+"');\" >Remove </div>";
					    Template = Template + "</form></td></tr>";
					    
					    $('#wish').append(Template);

		});
	});
	

});
function removeitemfromwishlist(productid) {
	var udata = JSON.parse(localStorage.getItem("userdata"));
	console.log("manb the value of productid is :"+productid);
	uid = udata.id;
	
	var form = new FormData();

	var data = form;

	
	$.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/removeproductfromwishlist/"+productid+"/"+uid,
        data: form, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	window.location.href = "wishlist.jsp";
        },
        error: function(data){
            alert("fail");
        }
    });
	
}