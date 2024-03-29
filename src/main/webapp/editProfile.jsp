<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="Free HTML Templates" name="keywords">
    <meta content="Free HTML Templates" name="description">

    <!-- Favicon -->
    <link href="/img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

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
                <a href="/${hasRole}/profile/show/${user.userId}" class="nav-item nav-link">Back</a>
            </div>
        </div>
    </div>
    <!-- Topbar End -->

	<main class="my-form">
		<div class="cotainer">
			<div class="row justify-content-center">
				<div class="col-md-8">
					<div class="card">
						<div class="card-header">${user.name}</div>
						<div class="card-body">
							<form name="my-form" action="/${hasRole}/profile/edit" method="post">
							<div class="form-group row">
                                <div class="col-md-6">
                                    <input required="required" value="${user.userId}" type="text" id="userId"
                                        class="form-control" name="userId" hidden>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="name" class="col-md-4 col-form-label text-md-right">Name</label>
                                <div class="col-md-6">
                                    <input required="required" value="${user.name}" type="text" id="name"
                                        class="form-control" name="name">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="email"
                                    class="col-md-4 col-form-label text-md-right">E-Mail
                                    Address</label>
                                <div class="col-md-6">
                                    <input required="required" value="${user.email}" type="email" id="email"
                                        class="form-control" name="email">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="password"
                                    class="col-md-4 col-form-label text-md-right">Password</label>
                                <div class="col-md-6">
                                    <input required="required" value = "${user.password}" type="password" id="password"
                                        class="form-control" name="password">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="address"
                                    class="col-md-4 col-form-label text-md-right">
                                    Address</label>
                                <div class="col-md-6">
                                    <input required="required" value="${user.address}" type="address" id="address"
                                        class="form-control" name="address">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="contact"
                                    class="col-md-4 col-form-label text-md-right">Phone
                                    Number</label>
                                <div class="col-md-6">
                                    <input required="required" value="${user.contact}" type="text"
                                        id="contact" class="form-control" name="contact">
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