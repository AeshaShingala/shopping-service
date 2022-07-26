$(document).ready(function() {
	
	let number = 1;
	var value=$('#id').val();
	$("#add").click(function(e) {
		number++;
		value++;
		event.preventDefault();
		$('#repeat').append('<div id="product'+number+'">'+
            '<div class="card-header text-left "> Product '+number+
                '<button id="delete" type="button" <i class="fas fa-trash text-primary float-right mr-1"></i> Remove</button>'+
            '</div>'+
            '<br>'+
            '<div class="form-group row">'+
                '<label for="name"'+number+' class="col-md-2 col-form-label text-md-right">Name</label>'+
                '<div class="col-md-6">'+
                    '<input required="required" type="text" id="name"'+ number +' class="form-control" name="name">'+
               '</div>'+
            '</div>'+
            '<div class="form-group row">'+
                '<label for="description"'+ number +' class="col-md-2 col-form-label text-md-right">Description</label>'+
                '<div class="col-md-6">'+
                    '<input required="required" type="text" id="description"'+ number +' class="form-control" name="description">'+
                '</div>'+
            '</div>'+
            '<div class="form-group row">'+
                '<label for="price"'+ number +' class="col-md-2 col-form-label text-md-right">Price</label>'+
                '<div class="col-md-6">'+
                    '<input required="required" type="number" id="price"'+ number +' class="form-control" name="price">'+
                '</div>'+
            '</div>'+
            '<div class="form-group row">'+
                '<label for="image"'+ number +' class="col-md-2 col-form-label text-md-right">Image</label>'+
                '<div class="col-md-6">'+
                    '<input required="required" type="file" id="image"'+ number +' class="form-control" name="image">'+
                '</div>'+
            '</div>'+
            '<div class="form-group row">'+
                '<label for="category"'+ number +' class="col-md-2 col-form-label text-md-right">Category</label>'+
                '<select name="categoryId"'+ number +' id="categoryId">'+
                    '<c:forEach items="${listOfCategories}" var="category">'+
                        '<option value="${category.categoryId}">${category.name}</option>'+
                    '</c:forEach>'+
                '</select>'+
            '</div>'+
            '<div class="form-group row">'+
                '<label for="size"'+ number +' class="col-md-2 col-form-label text-md-right">Size </label>'+
                '<div class="col-md-6 spanTopMargin">'+ 
                '<c:forEach items="${listOfSizes}" var="size">'+
                    '<span><input type="checkbox" id="size"'+ number +' name="size" value="${size}">  ${size}</span> &nbsp; &nbsp;'+
                '</c:forEach>'+ 
                '</div>'+
            '</div>'+
            '<div class="form-group row">'+
                '<label for="colour"'+ number +' class="col-md-2 col-form-label text-md-right">Colours </label>'+
                '<div class="col-md-6 spanTopMargin">'+
                    '<c:forEach items="${listOfColour}" var="colour">'+
                        '<p><input type="checkbox" id="colour"'+ number +' name="colour" value="${colour.name}">  ${colour.name}</p> &nbsp; &nbsp;'+
                    '</c:forEach>'+
                '</div>'+
            '</div>'          			
	);
			$(document).scrollTop($(document).height());
	}
	);
	$('body').on('click','#delete',function(e) {
		$("#product"+number).remove();
		number--;
		value--;
	});
});