document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("loginForm");

    form.addEventListener("submit", function (event) {

        event.preventDefault();

        if (!navigator.geolocation) {
            alert("Geolocation is not supported by this browser.");
            return;
        }

        navigator.geolocation.getCurrentPosition(

            function(position){

                document.getElementById("latitude").value =
                    position.coords.latitude;

                document.getElementById("longitude").value =
                    position.coords.longitude;

                form.submit();

            },

            function(error){

                switch(error.code){

                    case error.PERMISSION_DENIED:
                        alert("Please allow location access.");
                        break;

                    case error.POSITION_UNAVAILABLE:
                        alert("Location unavailable.");
                        break;

                    case error.TIMEOUT:
                        alert("Location request timed out.");
                        break;

                    default:
                        alert("Unable to fetch location.");

                }

            },

            {
                enableHighAccuracy:true,
                timeout:10000,
                maximumAge:0
            }

        );

    });

});