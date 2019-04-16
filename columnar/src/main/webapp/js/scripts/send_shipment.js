

$(document).ready(function() {

   var udata = JSON.parse(localStorage.getItem("userdata"));

	//Get items of the Seller
	var api = "http://localhost:8055/amazon.com/webapi/OrderController/getOrdersOfSeller/"+udata.id;
		
		
	$.get(api , function(data, status){
	//    data = JSON.parse(data);
		
		data.forEach(function(order){
			order.ordereditemlist.forEach(function(product){
				//Get Product information
				console.log(product);
				if(product.status == "PENDING") {
					var api = "http://localhost:8055/amazon.com/webapi/ProductController/productsById/"+product.productid;
					

					//Ajax Call for getting individual product
					$.get(api , function(data, status){
						var Template = "";
						
					    productInfo = data[0];
					    console.log(productInfo.product_images[0]);
					    Template = Template + "<tr><td><figure class='media'>	<div class='img-wrap'><img src='"+productInfo.product_images[0].url+"' class='img-thumbnail img-sm'></div>";
					    Template = Template +"<figcaption class='media-body'><h6 class='title text-truncate'>"+productInfo.productname+" </h6>";
					    Template = Template + "</figcaption></figure></td><td><div class='price-wrap'><var class='price'>INR "+product.unitdiscountedamount * product.quantity+"</var>"; 
					    Template = Template + "<small class='text-muted'>(INR"+product.unitdiscountedamount+" each)</small>";
					    
					    if(Number(product.freequantity) > 0)
					    	Template = Template + "</div></td><td><div class='price-wrap'><var class='price'>"+product.quantity+"+" + product.freequantity+"(Free)</var></div>";
						else
							Template = Template + "</div></td><td><div class='price-wrap'><var class='price'>"+product.quantity+"</var></div>";
					    
					    //Template = Template + "</div></td><td><div class='price-wrap'><var class='price'>"+product.quantity+"</var></div>";
					    Template = Template + "</td><td class='text-right'><form method='post'><div class='btn-group btn btn-success' role='group' onclick=\"sendShipment('"+order.orderid+"');\">Send Shipment </div>";
					    Template = Template + "</form></td></tr>";
					    
					    $('#shipments').append(Template);
					});

				}
			});
		});
	   	   // $('#product_hidden').html("<input type='hidden' name='product_id' value='"+localStorage.getItem("viewingProduct")+"' id='product_id' />");
	});
	
});


function sendShipment(orderid) {
	var udata = JSON.parse(localStorage.getItem("userdata"));

	
	var form = new FormData();
	form.append('sellerid', udata.id );
	form.append('orderid', orderid);
	var data = form;
	//Ajax Call for getting individual product
	
	$.ajax({
    	type: "POST",
		enctype: 'multipart/form-data',
        url: "http://localhost:8055/amazon.com/webapi/OrderController/shipItem",
        data: form, 
       	processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 600000,
        
        success:function(result){
        	//window.alert(result);
        	//window.location.href="http://localhost:8055/amazon.com/";
        	alert("You've successfully shipped the item!!!");
        	location.reload(true);
        },
        error: function(data){
            alert("fail");
        }
    });
	
}