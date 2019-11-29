//<![CDATA[

    var monthlist = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

    window.onload = function() {
        $('select').selectpicker();

        $("[data-toggle='tooltip']").tooltip();

        var greeting =  getTodaysDate() + ' - ' + getGreeting() + "!";
        $("#dateHeading").text(greeting);

        var tickerDirection = $("input[name='radiovalue']:checked").val();
        $("#webTicker").webTicker({
            direction: tickerDirection
        });
    }

    // If the 'Right' radio button is ticked, scroll the ticker from right to left
    // otherwise 'Left' is ticked so scroll the ticker from left to right
    function checkRadioButton() {
        var tickerDirection = $("input[name='radiovalue']:checked").val();
        if (tickerDirection == 'left') {
            $("#leftButton").val(false);
            $("#rightButton").val(true);
        } else {
            $("#leftButton").val(true);
            $("#rightButton").val(false);
        }
        submitForm();
    }

    function submitForm() {
        $("#cryptoDetailsForm").submit();
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

    function reverseDate(dt) {
        var dd = dt.substring(0,2);
        var mm = dt.substring(3,5);
        var yy = dt.substring(6);
        return yy + "-" + mm + "-" + dd;
    }

//]]>
