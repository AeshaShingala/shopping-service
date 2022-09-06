(function ($) {
    "use strict";

    $(document).ready(function () {
        $('.hey').each(function () {
            if ($(this).val()  == 'true') {
                $(this).prop("checked", true);
            }  
        });
   
    });

    $('.hey').on('click',function()
    {
        var current = $(this);
        console.log(current.val());
        var userId = parseInt(current.parent().parent().parent().find("#uid").text());
        console.log(userId);
        var data= {'userId':userId};
        $.ajax({
            url: 'http://localhost:8080/admin/status',
            type: 'POST',
            dataType: 'json',
            data: data,
            success: function (result) {
            }
        });
    });

})(jQuery);