

$(document).ready(function() {

   var udata = JSON.parse(localStorage.getItem("userdata"));

	//Get items of the Customer
	var api = "http://localhost:8055/amazon.com/webapi/UserController/getUserCart/"+udata.id;
		
		
	$.get(api , function(data, status){
	//    data = JSON.parse(data);
		console.log(data);
		console.log("sorted till this nnj point manp");

		var i = 0;
		var totalval = 0;
		
		data.forEach(function(cartitem){
			i++;
			console.log(cartitem.product);
			productInfo = cartitem.product;
			console.log(productInfo);
			qty = cartitem.quantity;
			freeqty = cartitem.freequantity;
			
			console.log("free quamtity is :"+freeqty);
			
			
			var offerType = productInfo.offerType;
			
			var udata = JSON.parse(localStorage.getItem("userdata"));
		    var responsebirthdate = new Date(udata.dob);
			var currentdate = new Date();
			var discountedprice;
			if(productInfo.offerType == "discount" && Date.now() < productInfo.offerEndDate && Date.now() >= productInfo.offerStartDate) {
				discountedprice = productInfo.discountedprice * (100 - productInfo.offerdiscpercent)/100;
			}
			else if(productInfo.offerType == "birthday" && currentdate.getMonth() == responsebirthdate.getMonth() &&  currentdate.getDate() == responsebirthdate.getDate() ){
				discountedprice = productInfo.discountedprice * (100 - productInfo.offerdiscpercent)/100;
				
			}
			else if(productInfo.offerType == "buy1get1"  && Date.now() < productInfo.offerEndDate && Date.now() >= productInfo.offerStartDate){
				discountedprice = productInfo.discountedprice;
				
			}
			else if(productInfo.offerType == "buy3get1" && Date.now() < productInfo.offerEndDate && Date.now() >= productInfo.offerStartDate){
				discountedprice = productInfo.discountedprice;
				
			}
			else{
				alert("Comes here ");
				discountedprice = productInfo.discountedprice;
			}
			
			
			
			var Template = "";
			            
					    Template = Template + "<tr><td><div class='price-wrap'><var class='price'>"+i+"</var></div></td>" ;
					    Template = Template + "<td><div class='price-wrap'><var class='price'>"+productInfo.productid+"</var></div>";
					    Template = Template + "</td><td><div class='price-wrap'><var class='price'>INR "+discountedprice * qty+"</var>"; 
					    Template = Template + "<small class='text-muted'>(INR"+discountedprice+" each)</small>";
					    Template = Template + "</div></td><td><div class='price-wrap'><var class='price'>"+qty+"</var></div>";
					    Template = Template + "</td></tr>";
					    
					    $('#cart').append(Template);
					    
					    totalval = totalval + discountedprice * qty;

		});
		
		$('#total_value').html(totalval);
		
		console.log("setting vat to : + "+(totalval*5)/100);
		
		$('#vat').html((totalval*5)/100);
		
		
	});
	

});
function removeitemfromcart(productid) {
	var udata = JSON.parse(localStorage.getItem("userdata"));
	console.log("manb the value of productid is :"+productid);
	uid = udata.id;
	
	var form = new FormData();

	var data = form;

	
	$.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/ProductController/removeproductfromcart/"+productid+"/"+uid,
        data: form, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	window.location.href = "customer_cart.jsp";
        },
        error: function(data){
            alert("fail");
        }
    });
	
}