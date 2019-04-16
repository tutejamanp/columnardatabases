var review_average = 0;
var review_count = 0;
function makeStars(rating) {
		review_average += rating;
		review_count ++;
		var percent = Math.floor(rating/5*100);
		return "<ul class='rating-stars'><li style='width:"+percent+"%' class='stars-active'><i class='fa fa-star'></i><i class='fa fa-star'></i><i class='fa fa-star'></i><i class='fa fa-star'></i><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i><i class='fa fa-star'></i><i class='fa fa-star'></i><i class='fa fa-star'></i><i class='fa fa-star'></i></li></ul>";
}


function makeComment(reviewData) {
	
	var comment = "<div class='col-sm-12'><hr/><div class='review-block'><div class='row'><div class='col-sm-9'><div class='review-block-rate'>";
	comment = comment + makeStars(reviewData.rating);
	comment = comment + "</div><div class='review-block-title'>" + reviewData.headline + "</div>";
	comment = comment + "<div class='review-block-description'>"+ reviewData.description +"</div>";
	comment = comment + "</div></div><hr/><h6 id='reviewer"+reviewData.customerid+"'>By Amazon Customer</h6></div></div>";	
	return comment;
}

function displayComments() {
	myId = 0;
	if(localStorage.getItem("viewingProduct") != "") {
		var api = "http://localhost:8055/amazon.com/webapi/ProdReviewController/allreviews/"+localStorage.getItem("viewingProduct");
		var product;
		console.log("Here");
		//Ajax Call for getting individual product
		$.get(api , function(data, status){
		    product = data;
			
			if (localStorage.getItem("userData") != "") {
				var udata = JSON.parse(localStorage.getItem("userdata"));
				product.forEach(function(item){
				
					if( udata.id == item.customerid) {
						$('#myComments').prepend(makeComment(item));	
						myId = udata.id;
						$('#feedbackButton').remove();
					}
					else
						$('#myComments').append(makeComment(item));		
					
				});
			} else {
				product.forEach(function(item){
					$('#myComments').append(makeComment(item));
				});
				
			}
			
			console.log(product);
			if(review_count > 0) {
				$('#productRating').html(makeStars(review_average/review_count) + " Out of ("+product.length+")");
				if(myId > 0) {
					
					$("#reviewer"+myId).text("My Review");
				}
				
			}
		
		});
		
	} else {
		window.location.href = "404.jsp";
	}
	
}