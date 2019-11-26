//<![CDATA[

    // Public constants
    var monthlist = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

    window.onload = function() {
        $("[data-toggle='tooltip']").tooltip();

        var greeting =  getTodaysDate() + ' - ' + getGreeting() + "!";
        $("#dateHeading").text(greeting);

        $("#gigStartDate").datepicker({
            dateFormat: "dd-mm-yy",
            minDate: new Date(),
            showButtonPanel: true
        });

        $("#gigEndDate").datepicker({
            dateFormat: "dd-mm-yy",
            minDate: new Date(),
            showButtonPanel: true
        });

        if ($("#noRecordsFound").val()) {
            showAlert(event, "modal-header-warning", "No gigs found for the specified information");
        }

        // Start the web tikcer
        $("#webTicker").webTicker();
    }


    function getTodaysDate() {
      var today = new Date();
      var day = today.getDay();
      var daylist = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
      var minutes = today.getMinutes();
      minutes = (minutes < 10) ? '0' + minutes : minutes;
      return daylist[day] + ' ' + today.getDate() + ' ' + monthlist[today.getMonth()]
        + ' ' + today.getFullYear() + ' - ' + today.getHours() + ':' + minutes;
    }

    function getGreeting() {
        var data = [
                [0,   4, "Good Night"],
                [5,  11, "Good Morning"],
                [12, 16, "Good Afternoon"],
                [17, 20, "Good Evening"],
                [21, 23, "Good Night"]
            ],
        hr = new Date().getHours();

        for(var i = 0; i < data.length; i++){
            if(hr >= data[i][0] && hr <= data[i][1]){
                return (data[i][2]);
            }
        }
    }

    function validateForm(event) {
        var gigLocation  = $("#gigLocation").val();
        if (gigLocation == '') {
            showAlert(event, "modal-header-error", "Location cannot be empty")
            return false;
        }

        var sDate        = $("#gigStartDate").val();
        var eDate        = $("#gigEndDate").val();
        var revStartDate = reverseDate(sDate);
        var revEndDate   = reverseDate(eDate);

        if (sDate == '' || eDate == '') {
            showAlert(event, "modal-header-error", "Date cannot be empty")
            return false;
        } else {
            var startDate = Date.parse(revStartDate);
            var endDate   = Date.parse(revEndDate);

            if (startDate > endDate) {
                showAlert(event, "modal-header-warning", "Start date cannot be greater than end date")
                return false;
            }
        }
        resetCurrentPage();
    }

    function reverseDate(dt) {
        var dd = dt.substring(0,2);
        var mm = dt.substring(3,5);
        var yy = dt.substring(6);
        return yy + "-" + mm + "-" + dd;
    }

    function submitForm(href) {
        $("#currentPage").val(href.innerText);
        $("#cryptoForm").submit();
    }

    function showAlert(event, headerClassToAdd, mainMsg) {
        var hdrMsg = (headerClassToAdd.indexOf("error") >= 0) ? "Error" : "Warning"
        $("#modal-hdr").removeClass("alert-primary");
        $("#modal-hdr").removeClass("modal-header-error");
        $("#modal-hdr").removeClass("modal-header-warning");
        $("#modal-hdr").addClass("alert-primary");
        $("#modal-hdr").addClass(headerClassToAdd);
        $("#modal-hdr-msg").html(hdrMsg);
        $("#errorMsg").html(mainMsg);
        $('#myModal').modal("show");
        event.preventDefault();
        return false;
    }

    // If we change location, set metro area id to 0 to force
    // it to be looked up again for the new location
    // also set current page to one so the search starts correctly
    function resetMetroAreaId() {
        $("#metroAreaId").val(0);
        $("#currentPage").val(1);
    }

    // If we change the results per page reset current
    // page number to 1 prior to starting a new search
    function resetCurrentPage() {
        $("#currentPage").val(1);
        $("#cryptoForm").submit();
    }

    function getPreviousPage() {
        var currentPage = parseInt($("#currentPage").val());
        if (currentPage > 1) {
            currentPage = currentPage - 1;
        }
        $("#currentPage").val(currentPage);
        $("#cryptoForm").submit();
    }

    function getNextPage() {
        var currentPage = parseInt($("#currentPage").val()) + 1;
        var pageArray   = JSON.parse($("#maxPageNumber").val());
        if (currentPage > pageArray.length ) {
            currentPage = pageArray.length;
        }
        $("#currentPage").val(currentPage);
        $("#cryptoForm").submit();
    }

//]]>
