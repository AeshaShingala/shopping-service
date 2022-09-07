<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>EShopper - Bootstrap Shop Template</title>
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
			<div class="col-lg-9 col-6 text-right">
				<a href="#" class=" btn border" data-toggle="dropdown">Roles<i class="fa fa-angle-down float-right mt-1"></i>
                    <div class=" dropdown-menu position-absolute bg-secondary border-0 rounded-0 w-30 m-0 ">
                        <a href="/seller/home" class="dropdown-item">Seller</a>
                        <a href="/buyer/home" class="dropdown-item">Buyer</a>
                    </div>
                </a>
				<a href="/admin/profile/show/${admin.userId}" class="btn border">
                    <i class="fas fa-user text-primary"></i>
                    <span class="badge">${admin.name}</span>
                </a>
				<a href="/logout" class="btn border">
                    <i class="fas fa-sign-out-alt text-primary"></i>
                    <span class="badge">Logout</span>
                </a>
            </div>
        </div>
    </div>
    <!-- Topbar End -->

	<div class="row px-xl-5">
		<div class="col-lg-12 table-responsive ">
			<table class="table table-bordered text-center mb-0 table-striped">
				<thead class="bg-secondary text-dark">
					<tr>
						<th><a href="/admin/home/${currentPage}/userId">Id</a></th>
						<th><a href="/admin/home/${currentPage}/name">Name</a></th>
						<th><a href="/admin/home/${currentPage}/email">Email</a></th>
						<th><a href="/admin/home/${currentPage}/contact">Contact</a></th>
						<th><a href="#">Enabled</a></th>
					</tr>
				</thead>
				<tbody class="align-middle">
					<c:forEach items="${listOfSellers}" var="user">
						<tr>
							<td><label id="uid"  class="uid">${user.userId}</label></td>
							<td>${user.name}</td>
							<td>${user.email}</td>
							<td>${user.contact}</td>
							<td>
								<label class="switch">
									<input type="checkbox" class="hey" id="togle" value="${user.enable}">
									<span class="slider round"></span>
								</label>
							</td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	&nbsp
	&nbsp

	<div aria-label="Page navigation">
		<ul class="pagination justify-content-center">
			<c:if test="${currentPage != 1}"> 
				<li class="page-item"><a class="page-link" href="/admin/home/${currentPage-1}/${sort}">Previous</a></li>
			</c:if>
		  <c:forEach begin="1" end="${totalPages}" var="p">
			<c:if test="${currentPage != p}"> 
			   <li class="page-item"><a class="page-link" href="/admin/home/${p}/${sort}">${p}</a></li>  
			</c:if>
			<c:if test="${currentPage == p}"> 
			   <li class="page-item"><a class="page-link" style="color: #6F6F6F;" href="/admin/home/${p}/${sort}">${p}</a></li>  
			</c:if>
		  </c:forEach>   
		  <c:if test="${currentPage != totalPages}">  
			  <li class="page-item"><a class="page-link" href="/admin/home/${currentPage+1}/${sort}">Next</a></li>
		  </c:if>
		</ul>
	</div> 


	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>


	<!-- Template Javascript -->
    <script src="/js/status.js"></script>
</body>