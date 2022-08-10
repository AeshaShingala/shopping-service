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
        $('#subtotal').val('$'+calculateSubtotal());
        $('#total').val('$'+ (calculateSubtotal() + 40));
        $(window).resize(toggleNavbarMethod);      
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
        var oldValue = button.parent().parent().find('#qty').val();
        var price = button.parent().parent().parent().parent().find('#price').text();
        var productId = button.parent().parent().find('#productId').val();
        if (button.hasClass('btn-plus')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
       
        var data= {"cartProductId":parseInt( productId),"quantity": newVal};
        $.ajax({
            url: 'http://localhost:8080/buyer/product/quantity',
            type: 'POST',
            data: data,
            success: function (result) {
                button.parent().parent().find('#qty').val(newVal);
                button.parent().parent().parent().parent().find('#sumtotal').attr("value",(newVal * price).toFixed(2));
                console.log(calculateSubtotal());
                $('#subtotal').val('$'+ calculateSubtotal());
                $('#total').val('$'+ (calculateSubtotal() + 40));
            }
        });

    });

    function calculateSubtotal(){
        var total = 0;
       $(".sumtotal").each(function totals() {
        total = parseInt(total) + parseInt($(this).val());
       })
       return total;
    }

})(jQuery);

