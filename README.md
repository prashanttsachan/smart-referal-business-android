# smart-referal-business-android
  # API EndPoints:
                      
    POST https://srbn.herokuapp.com/auth/register
        body: {
            "email": "psachan190@gmail.com", 
            "mobile": "9996933880", 
            "firstname": "Prashant", 
            "lastname": "Sachan", 
            "password": "1234", 
            "sponsor": "609e452e21cfbf1df059d48f"
        }
    POST https://srbn.herokuapp.com/auth/login
        body: {
            "email": "psachan190@gmail.com",
            "password": "1234",
        }
    GET https://srbn.herokuapp.com/auth/checkauth
        header: {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MDllNDUyZTIxY2ZiZjFkZjA1OWQ0OGYiLCJpYXQiOjE2MjA5OTI3NjUzNzEsImV4cCI6MTYyMDk5MzM3MDE3MX0.PUuTOAau71oYqupQLR0pOhMvyLsu-LY5i_o8Za9HyBI"
        }
    POST https://srbn.herokuapp.com/user/bank-details
        header: {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MDllNDUyZTIxY2ZiZjFkZjA1OWQ0OGYiLCJpYXQiOjE2MjA5OTI3NjUzNzEsImV4cCI6MTYyMDk5MzM3MDE3MX0.PUuTOAau71oYqupQLR0pOhMvyLsu-LY5i_o8Za9HyBI"
        }
        body: {
            "account": "3686000101215904", 
            "ifsc": "PUNB0368600", 
            "name": "Prashant Sachan"
        }
    
    POST https://srbn.herokuapp.com/user/aadhar-details
        header: {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MDllNDUyZTIxY2ZiZjFkZjA1OWQ0OGYiLCJpYXQiOjE2MjA5OTI3NjUzNzEsImV4cCI6MTYyMDk5MzM3MDE3MX0.PUuTOAau71oYqupQLR0pOhMvyLsu-LY5i_o8Za9HyBI"
        }
        body: multipart/formdata
        {
            'aadharFront' //This is an image,
            'aadharBack' //This is an image
            "aadhar": "980230048359", // Form-field
        }

    POST https://srbn.herokuapp.com/user/pan-details
        header: {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MDllNDUyZTIxY2ZiZjFkZjA1OWQ0OGYiLCJpYXQiOjE2MjA5OTI3NjUzNzEsImV4cCI6MTYyMDk5MzM3MDE3MX0.PUuTOAau71oYqupQLR0pOhMvyLsu-LY5i_o8Za9HyBI"
        }
        body: multipart/formdata
        {
            'pan' //This is an image,
            "panNumber": "980230048359", // Form-field
        }

    POST https://srbn.herokuapp.com/user/change-password
        header: {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MDllNDUyZTIxY2ZiZjFkZjA1OWQ0OGYiLCJpYXQiOjE2MjA5OTI3NjUzNzEsImV4cCI6MTYyMDk5MzM3MDE3MX0.PUuTOAau71oYqupQLR0pOhMvyLsu-LY5i_o8Za9HyBI"
        }
        body: {
            "password": "1234",
            "newPassword": "4567"
        }
