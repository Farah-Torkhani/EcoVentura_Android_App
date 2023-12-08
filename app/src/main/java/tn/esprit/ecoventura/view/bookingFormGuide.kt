package tn.esprit.ecoventura.view

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tn.esprit.ecoventura.R
import tn.esprit.ecoventura.apiService.GuideApi
import tn.esprit.ecoventura.model.Guide
import tn.esprit.ecoventura.model.ReservationRequest
import java.util.*
import java.text.SimpleDateFormat;
import java.util.Date;
import kotlin.collections.ArrayList
import kotlin.collections.mutableListOf


class BookingFormActivity : AppCompatActivity() {

    private lateinit var hoursEditText: EditText
    private lateinit var totalPriceTextView: TextView
    private lateinit var forMeSwitch: Switch
    private lateinit var guide: Guide
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var calendar: DatePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookingformguide)
        hoursEditText = findViewById(R.id.hoursEditText)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        val bookNowButton: Button = findViewById(R.id.bookButton)

        forMeSwitch = findViewById(R.id.forMeSwitch)

        // Assume you passed the Guide object to this activity
        guide = intent.getParcelableExtra("GUIDE_OBJECT")!!

        val guideId = guide._id
        Log.d("guideInForm", "$guide")
        // Add TextWatcher to calculate total price dynamically
        bookNowButton.setOnClickListener {
            Log.d("button", "clicked")
            handleBooking()
        }
        hoursEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Calculate total price when the text changes
                calculateAndDisplayTotalPrice(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this implementation
            }
        })

        // Fetch guide availability and set available dates in the date picker
        fetchGuideAvailability(guideId)
    }
    private fun calculateAndDisplayTotalPrice(hoursText: String) {
        if (hoursText.isNotBlank()) {
            val numberOfHours = hoursText.toInt()
            val pricePerHour = guide.price // Assuming pricePerHour is a property in the Guide model
            val totalPrice = numberOfHours * pricePerHour
            totalPriceTextView.text = "Total Price: $totalPrice" // Update the UI with the total price
        } else {
            // Handle the case where the hoursEditText is empty
            totalPriceTextView.text = ""
        }
    }
    private class DateValidator(private val availableDates: List<Long>) :
        CalendarConstraints.DateValidator, Parcelable {

        override fun isValid(date: Long): Boolean {
            availableDates.forEach{
                if (it + 86400000 >= date && it <= date ) {
                    return true
                }
            }
            return false
        }

        // Implement Parcelable methods
        constructor(parcel: Parcel) : this(parcel.readArrayList(Long::class.java.classLoader) as List<Long>)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeList(availableDates)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<DateValidator> {
            override fun createFromParcel(parcel: Parcel): DateValidator {
                return DateValidator(parcel)
            }

            override fun newArray(size: Int): Array<DateValidator?> {
                return arrayOfNulls(size)
            }
        }
    }
    private fun updateUiDatePicker(selectedCalendar: Calendar) {
        val calendarView = findViewById<DatePicker>(R.id.datePicker) // Assuming your XML DatePicker ID is datePicker
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val day = selectedCalendar.get(Calendar.DAY_OF_MONTH)
        calendarView.updateDate(year, month, day)
    }
    private fun fetchGuideAvailability(guideId: String) {
        val guideApi = GuideApi.create()
        val calendar = findViewById<DatePicker>(R.id.datePicker) // Assuming your XML DatePicker ID is datePicker

        Log.d("HELLO WORLD","HELLO");

        // Use a coroutine to make the API call
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = guideApi.getGuideAvailability(guideId)
                Log.d("API_RESPONSE", response.toString())

                if (response.isSuccessful) {
                    val availability = response.body()
                    Log.d("AVAILABILITY_RESPONSE", availability.toString())
                    val availableDates : ArrayList<Long> =  ArrayList()
                    // Filter available dates and set them as constraints
                    availability?.let {
                        it.map { dateString ->
                            val availableDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            Log.d("AVAILABILITY", dateString)
                            availableDates.add(availableDate.time);
                        }
                        val today = Calendar.getInstance()

                        Log.d("Today Time",today.timeInMillis.toString())
                        // Filter out the unavailable dates
                        val availableDateList = mutableListOf<Long>()
                        availableDates.forEach{ availableDate ->
                            Log.d("AV Time",availableDate.toString())
                            if (availableDate >= today.timeInMillis) {
                                Log.d("AV DATE",availableDate.toString())
                                availableDateList.add(availableDate)
                            }
                        }
                        Calendar.getInstance()

                        // Sort the available dates
                        availableDateList.sort()
                        Log.d("AV DATES",availableDateList.toString())
                        val constraintsBuilder = CalendarConstraints.Builder()
                            .setStart(today.timeInMillis)
                        // Create a CalendarConstraints.DateValidator with the available dates
                        val dateValidator = DateValidator(availableDateList)

                        // Set the available dates as the constraints
                        constraintsBuilder.setValidator(dateValidator)
                        datePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Date")
                            .setCalendarConstraints(constraintsBuilder.build())
                            .build()

                        datePicker.addOnPositiveButtonClickListener(
                            MaterialPickerOnPositiveButtonClickListener<Long> { selection ->
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = selection
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH) + 1
                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                updateUiDatePicker(calendar)

                                val msg = "You Selected: $day/$month/$year"
                                Toast.makeText(
                                    this@BookingFormActivity,
                                    msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        // Show the date picker
                        datePicker.show(supportFragmentManager, datePicker.toString())
                    }
                } else {
                    // Handle error
                    Toast.makeText(
                        this@BookingFormActivity,
                        "Failed to fetch guide availability",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Handle exception
                Toast.makeText(
                    this@BookingFormActivity,
                    "Exception occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("ERROR",e.message.toString())
            }

        }
    }

    private fun handleBooking() {
        Log.d("button", "clicked")
        //val selectedDate: Long = datePicker.selection ?: return
        val numberOfHours: Int = hoursEditText.text.toString().toIntOrNull() ?: return

        // Assuming you have the user ID and location, you need to replace these with actual values
        val userId = "655aa08d78adce5f7b9a9159"
        val location = "location"

        val reservationRequest = ReservationRequest(userId, numberOfHours, location)

        // Assuming you have a valid guide ID from the previous activity
        val guideId = guide._id

        // Make the API call to add a reservation
        val guideApi = GuideApi.create()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = guideApi.addGuideReservation(guideId, reservationRequest)
                if (response.isSuccessful) {
                    // Reservation added successfully
                    val reservationResponse = response.body()
                    Log.d("button", "works")
                    Toast.makeText(
                        this@BookingFormActivity,
                        "Reservation added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Handle any further actions upon successful reservation
                } else {
                    // Handle API error
                    Log.d("button", "doesn't work")
                    Toast.makeText(
                        this@BookingFormActivity,
                        "Failed to add reservation. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Handle exception
                Toast.makeText(
                    this@BookingFormActivity,
                    "An error occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("button", "Problem")
                Log.e("BookingFormActivity", "Error: ${e.message}", e)
            }
        }}
}