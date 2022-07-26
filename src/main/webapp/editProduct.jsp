<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Free HTML Templates" name="keywords">
    <meta content="Free HTML Templates" name="description">

    <!-- Favicon -->
    <link href="img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

    <!-- Customized Bootstrap Stylesheet -->
    <link href="/css/style.css" rel="stylesheet">
</head>

<body>
    <!-- Topbar Start -->
    <div class="container-fluid">
        <div class="row align-items-center py-3 px-xl-5">
            <div class="col-lg-3 d-none d-lg-block">
                <a href="" class="text-decoration-none">
                    <h1 class="m-0 display-5 font-weight-semi-bold"><span class="text-primary font-weight-bold border px-3 mr-1">E</span>Shopper</h1>
                </a>
            </div>
            <div class="col-lg-9  text-right">
                <a href="/seller/${user.userId}" class="nav-item nav-link">Home</a>
            </div>
        </div>
    </div>
    <!-- Topbar End -->

	<main class="my-form">
		<div class="cotainer">
			<div class="row justify-content-center">
				<div class="col-md-8">
					<div class="card">
						<div class="card-header">${user.name}'s Product</div>
						<div class="card-body">
							<form name="my-form" action="" method="post">
                           
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <input required="required" value="${product.productId}" type="text" id="productId"
                                            class="form-control" name="productId" hidden>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="name" class="col-md-4 col-form-label text-md-right">Name</label>
                                    <div class="col-md-6">
                                        <input required="required" value="${product.name}" type="text" id="name"
                                            class="form-control" name="name">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="description"
                                        class="col-md-4 col-form-label text-md-right">Description</label>
                                    <div class="col-md-6">
                                        <input required="required" value="${product.description}" type="text" id="description"
                                            class="form-control" name="description">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="price"
                                        class="col-md-4 col-form-label text-md-right">Price</label>
                                    <div class="col-md-6">
                                        <input required="required" value = "${product.price}" type="number" id="price"
                                            class="form-control" name="price">
                                    </div>
                                </div>                          
                                <div class="form-group row">
                                    <label for="colour" class="col-md-4 col-form-label text-md-right">Colour</label>
                                    <div class="col-md-6 spanTopMargin">
                                        <c:forEach items="${product.colours}" var="colours">
                                            <p><input checked type="checkbox" id="colours" name="colours" value="${colours.name}">  ${colours.name}</p> &nbsp; &nbsp;
                                        </c:forEach> 
                                        <c:forEach items="${listOfRemainingColour}" var="colour">
                                            <p><input type="checkbox" id="colour" name="colour" value="${colour.name}">  ${colour.name}</p> &nbsp; &nbsp;
                                        </c:forEach> 
                                    </div>
                                </div>
                                


                                <div class="form-group row">
                                    <label for="size" class="col-md-4 col-form-label text-md-right">Size</label>
                                    <div class="col-md-6 spanTopMargin">
                                        <c:forEach items="${product.sizes}" var="size">
                                            <span><input checked type="checkbox" id="size" name="size" value="${size}" > ${size}</span> &nbsp; &nbsp;
                                        </c:forEach>
                                        <c:forEach items="${listOfSizes}" var="size" >
                                            <span><input type="checkbox" id="size" name="size" value="${size}" > ${size}</span> &nbsp; &nbsp;      
                                        </c:forEach>
                                    </div> 
                                </div>

                                <div class="form-group row">
                                    <label for="image"
                                        class="col-md-4 col-form-label text-md-right">Image</label>
                                    <div class="col-md-3">
                                        <img src="/productImages/${product.image}" alt="" width="250" height="275">
                                        <input required="required" type="file" id="image" value="${product.image}" class="form-control" name="image">
                                    </div>
                                </div>
                                <div class="col-md-6 offset-md-4">
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                </div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>

	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

</body>