(function ($) {
    "use strict";
    
    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 992) {
                $('.navbar .dropdown').on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                }).on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);


        $('#placeOrder').on('click', function () {
            let form = document.getElementById("my-form");
            let formData = new FormData(form);
            let userId = $('#userId').val();
            $.ajax({
                url: 'http://localhost:8080/buyer/order/' + userId ,
                type: 'POST',
                data: formData,
                processData:false,contentType:false,
                success: function (result) {
                    
                    if (result != "") {
                        swal.fire({
                            title: "Order Placed Successfully",
                            showDenyButton: true,
                            confirmButtonText: `Download Invoice`,
                            denyButtonText: `Continue Shopping`,
                            }).then((swalResult) => {
                            if (swalResult.isConfirmed) {
                                $.ajax({
                                    url:result,
                                    type:'GET',
                                    processData:false,contentType:false,
                                    success:function(){
                                        window.location.href = result
                                    }
                                }).then( () => {
                                swal.fire({
                                        title: "Invoice Downloaded Successfully",
                                        text: "Happy Shopping",
                                        icon: "success",
                                }).then(function () {
                                    window.location.href = "http://localhost:8080/buyer/" + userId;
                                });
                            })
                                
                            } else if (swalResult.isDenied) {
                                window.location.href = "http://localhost:8080/buyer/" + userId ;
                            } 
                        })
                    }
                }
            });
        });
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Vendor carousel
    $('.vendor-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0:{
                items:2
            },
            576:{
                items:3
            },
            768:{
                items:4
            },
            992:{
                items:5
            },
            1200:{
                items:6
            }
        }
    });


    // Related carousel
    $('.related-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:2
            },
            768:{
                items:3
            },
            992:{
                items:4
            }
        }
    });


    // Product Quantity
    $('.quantity button').on('click', function () {
        var button = $(this);
        var oldValue = button.parent().parent().find('input').val();
        if (button.hasClass('btn-plus')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        button.parent().parent().find('input').val(newVal);
    });


    $('')
    
})(jQuery);

