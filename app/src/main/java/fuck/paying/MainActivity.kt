package fuck.paying


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import fuck.paying.databinding.ActivityMainBinding
import fuck.paying.model.RegistrationData
import fuck.paying.network.MaverikRegistrationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.view.View // Importing View to resolve progressBar issues
import fuck.paying.R // Importing R to resolve color issues

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGenerate.setOnClickListener {
            generateData()
        }
    }

    private fun generateData() {
        showProgressBar()

        val email = generateEmail()
        val phone = generatePhone()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.maverik.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MaverikRegistrationService::class.java)

        val registrationData = RegistrationData(
            firstName = "Austin",
            lastName = "Clayton",
            email = email,
            password = "M1$$10nMaver",
            phone = phone,
            birthDate = "1999-06-27",
            optIn = true,
            perks = listOf(1),
            textCampaignOptIn = 1,
            stateProvince = "UT"
        )

        service.register("PAYX", "web", registrationData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    updateUI(email, phone)
                } else {
                    showErrorSnackbar("Registration failed. Please try again.")
                    hideProgressBar()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showErrorSnackbar("Network error. Please check your internet connection.")
                hideProgressBar()
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
        binding.buttonGenerate.isEnabled = false
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
        binding.buttonGenerate.isEnabled = true
    }

    private fun updateUI(email: String, phone: String) {
        binding.textViewEmail.text = getString(R.string.generated_email, email)
        binding.textViewPhone.text = getString(R.string.generated_phone, phone)
        generateQRCode("Email: $email, Phone: $phone")
        hideProgressBar()
    }

    private fun generateEmail(): String {
        val randomPart = (Math.random() * 1000000).toInt()
        return "mumbles$randomPart@gmail.com"
    }

    private fun generatePhone(): String {
        val baseNumber = "435299"
        val randomPart = (1000..9999).random()
        return "$baseNumber$randomPart"
    }

    private fun generateUserAgent(): String {
        val chromeVersion = (60..90).random()
        return "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeVersion.0.0.0 Safari/537.36"
    }

    private fun generateQRCode(text: String) {
        // Implement QR code generation using ZXing library or similar
        // Example: binding.imageViewQR.setImageBitmap(QRCode.from(text).bitmap())
    }

    private fun showErrorSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }
}