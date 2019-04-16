function add_amount() {
	var filled = true;
	$('.required').each(function(){
        if( $(this).val() == "" ){
		  filled = true;
          return true;
        }  
    });
	
	if(filled == true) {
		var form = new FormData();
		var udata = JSON.parse(localStorage.getItem("userdata"));
		form.append('userid', udata.id );
		form.append('amount', $('#money').val());
		data = form;
		$.ajax({
	    	type: "POST",
			enctype: 'multipart/form-data',
	        url: "http://localhost:8055/amazon.com/webapi/BankController/addMoney",
	        data: data, 
	       	processData: false,
	        contentType: false,
	        cache: false,
	        async: true,
	        timeout: 600000,
	        
	        success:function(result){
	        	//window.alert(result);
	        	//window.location.href="http://localhost:8055/amazon.com/";
	    		alert("You've Successfully Added The Money!!!");

	        	//location.reload(true);
	        }
	    });
		
		
	} else {
          alert('Please fill all the fields');
	}
	
}