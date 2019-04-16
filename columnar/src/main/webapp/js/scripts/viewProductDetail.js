function viewProductDetails(id) {
	localStorage.setItem("viewingProduct", id);
	console.log("hey hey hey :: ");
	window.location.href = "product_detail.jsp";
}