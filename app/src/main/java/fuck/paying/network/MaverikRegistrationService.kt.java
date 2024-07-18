package fuck.paying.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MaverikRegistrationService {

    @Headers(
        "authority: gateway.maverik.com",
        "accept: application/json, text/plain, */*",
        "accept-language: en-US,en;q=0.9",
        "sec-ch-ua: \"Not-A.Brand\";v=\"99\", \"Chromium\";v=\"124\"",
        "sec-ch-ua-mobile: ?0",
        "sec-ch-ua-platform: \"Linux\"",
        "sec-fetch-dest: empty",
        "sec-fetch-mode: cors",
        "sec-fetch-site: same-site"
    )
    @POST("mav-register/register")
    fun register(
        @Body registrationData: RegistrationData
    ): Call<RegistrationResponse>
}