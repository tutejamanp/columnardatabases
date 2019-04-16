//Function to update product info
function updateProduct(type_id, data, no) {
	$(type_id+" "+"#product_"+no+"_image").html("<img src='"+data.product_images[0].url+"' onclick=\"viewProductDetails("+data.id+");\"/>");
	$(type_id+" "+"#product_"+no+"_name").html(data.productname);
}

$(document).ready(function() {
	
//	$('.slick-prev').hide();
	if(localStorage.getItem("userdata")) {
		var udata = JSON.parse(localStorage.getItem("userdata"));

		var responsebirthdate = new Date(udata.dob);
		var currentdate = new Date ();
		
		if(currentdate.getMonth() == responsebirthdate.getMonth() &&  currentdate.getDate() == responsebirthdate.getDate() ){
		
		} else {
			$('#birthdayOffer').hide();
		}	
	} else {
		$('#birthdayOffer').hide();
	}
		
	//Today's Deals
	$.ajax({
	    url : "http://localhost:8055/amazon.com/webapi/ProductController/productsByOffer/discount",
	    type : "GET",
	    async: true,
	    success : function(products) {
	    	var innerText;
	    	//Create All Internal Images
	    	var count = 0;
	    	products.forEach(function(item) {
	    		console.log(item);
	    		count++;
	    		if(count < 10)
	    			updateProduct("#todaysDeals", item, count);
	    		
	    		
	    	});
	    	

	    },
	    error: function() {
	       console.log("Some Error");
	    }
	 });
	
	//Birthday Offer
	$.ajax({
	    url : "http://localhost:8055/amazon.com/webapi/ProductController/productsByOffer/birthday",
	    type : "GET",
	    async: true,
	    success : function(products) {
	    	var innerText;
	    	//Create All Internal Images
	    	var count = 0;
	    	products.forEach(function(item) {
	    		console.log(item);
	    		count++;
	    		if(count < 10)
	    			updateProduct("#birthdayOffer", item, count);
	    		
	    		
	    	});
	    	

	    },
	    error: function() {
	       console.log("Some Error");
	    }
	 });
	
	//Buy 1 Get 1
	$.ajax({
	    url : "http://localhost:8055/amazon.com/webapi/ProductController/productsByOffer/buy1get1",
	    type : "GET",
	    async: true,
	    success : function(products) {
	    	var innerText;
	    	//Create All Internal Images
	    	var count = 0;
	    	products.forEach(function(item) {
	    		console.log(item);
	    		count++;
	    		if(count < 10)
	    			updateProduct("#buy1get1", item, count);
	    		
	    		
	    	});
	    	

	    },
	    error: function() {
	       console.log("Some Error");
	    }
	 });
	
});