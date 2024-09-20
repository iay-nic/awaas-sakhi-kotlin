package r.rural.awaassakhi.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.text.format.Formatter
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParserFactory
import r.rural.awaassakhi.R
import r.rural.awaassakhi.network.crypto.CryptLib
import r.rural.awaassakhi.ui.auth.models.Block
import r.rural.awaassakhi.ui.auth.models.District
import r.rural.awaassakhi.ui.auth.models.State
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.regex.Pattern
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

object Utility {


    fun TextView.asString(): String {
        return text.toString()
    }

    fun EditText.asString(): String {
        return text.toString()
    }

    fun ImageView.tint(colorResId: Int) {
        // Get the color from resources
        val color = ContextCompat.getColor(context, colorResId)
        // Apply the color filter to the ImageView
        setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }


    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(activity: Activity) {
        val view = activity.currentFocus
        val methodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(methodManager != null && view != null)
        methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun Date.dateToSting(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getStateCodeFromUserName(context: Context?, userName: String): String {
        var stateCode = ""
        try {
            val stateJsonArray = JSONArray(
                loadJSONFromAsset(
                    context,
                    "States.json"
                )
            )
            for (i in 0 until stateJsonArray.length()) {
                val jsonObject: JSONObject = stateJsonArray.getJSONObject(i)
                if (TextUtils.equals(
                        jsonObject.getString("abbreviation"),
                        userName.substring(0, 2)
                    )
                ) {
                    stateCode = jsonObject.getString("id")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stateCode
    }

    fun getStateShortNameFromUserName(context: Context?, userName: String): String {
        var stateShortName = ""
        try {
            val stateJsonArray = JSONArray(
                loadJSONFromAsset(
                    context,
                    "States.json"
                )
            )
            for (i in 0 until stateJsonArray.length()) {
                val jsonObject: JSONObject = stateJsonArray.getJSONObject(i)
                if (TextUtils.equals(
                        jsonObject.getString("abbreviation"),
                        userName.substring(0, 2)
                    )
                ) {
                    stateShortName = jsonObject.getString("abbreviation")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stateShortName
    }


    fun loadJSONFromAsset(context: Context?, fileName: String?): String? {

        return try {
            val inputStream = context!!.assets.open(fileName!!)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

    }


    fun sha256(input: String): String? {
        return try {
            // Create MessageDigest instance for SHA-256
            val md = MessageDigest.getInstance("SHA-256")

            // Add input string bytes to digest
            md.update(input.toByteArray())

            // Get the hash's bytes
            val bytes = md.digest()

            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            val sb = java.lang.StringBuilder()
            for (aByte in bytes) {
                sb.append(Integer.toString((aByte.toInt() and 0xff) + 0x100, 16).substring(1))
            }

            // Return the complete hash
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    fun sha512(input: String): String {
        return try {
            // Create MessageDigest instance for SHA-512
            val md = MessageDigest.getInstance("SHA-512")

            // Add input string bytes to digest
            md.update(input.toByteArray())

            // Get the hash's bytes
            val bytes = md.digest()

            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            val sb = java.lang.StringBuilder()
            for (aByte in bytes) {
                sb.append(Integer.toString((aByte.toInt() and 0xff) + 0x100, 16).substring(1))
            }

            // Return the complete hexadecimal representation
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }


    fun getApplicationVersionName(context: Context): String {
        val packageManager = context.packageManager
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return packageInfo!!.versionName
    }

    fun getIPAddress(context: Context): String {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getSchemeYearStringList(): List<String> {
        val items: MutableList<String> = ArrayList()
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        if (month < 3) {
            for (i in 2008 until year) {
                val yr = i.toString() + "-" + (i + 1).toString()
                items.add(yr)
            }
        } else {
            for (i in 2008..year) {
                val yr = i.toString() + "-" + (i + 1).toString()
                items.add(yr)
            }
        }
        return items
    }


    fun showSnackBar(context: Context, view: View, message: String) {
        val snackBar = Snackbar.make(
            view, message,
            Snackbar.LENGTH_SHORT
        ).setAction("Action", null)
        snackBar.setActionTextColor(context.getColor(R.color.white))
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(context.getColor(R.color.red))
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(context.getColor(R.color.white))
        snackBar.show()
    }

    suspend fun awaitAll(vararg blocks: suspend () -> Unit) = coroutineScope {
        blocks.forEach {
            launch { it() }
        }
    }

    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
        /*  .setAction("UNDO") {
              val snackbar1 =
                  Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT)
              snackbar1.show()
          }*/
        snackbar.show()
    }

    fun <T> Fragment.getNavigationResult(key: String) =
        findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

    fun <T> Fragment.getNavigationResultLiveData(key: String) =
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

    fun <T> Fragment.setNavigationResult(result: T, key: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }

    fun distanceBetweenTwoLatLong(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double
    ): Float {
        val locationA = Location("Current Location")
        locationA.latitude = lat1
        locationA.longitude = lng1
        val locationB = Location("Destination Location")
        locationB.latitude = lat2
        locationB.longitude = lng2
        return locationA.distanceTo(locationB)
    }

    fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            bm.compress(Bitmap.CompressFormat.WEBP_LOSSY, 0, baos)
        } else {
            bm.compress(Bitmap.CompressFormat.WEBP, 0, baos)
        }

        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    /* fun encodeImage(bm: Bitmap): String {
         val maxSize = 125 * 1024 // 125KB in bytes
         var encodedImage = ""
         val low = 1
         var high = 100

         while (low <= high) {
             val mid = (low + high) / 2
             val baos = ByteArrayOutputStream()
             bm.compress(Bitmap.CompressFormat.JPEG, mid, baos)
             val byteArray = baos.toByteArray()

             if (byteArray.size <= maxSize) {
                 encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
                 break
             } else if (mid == low || mid == high) {
                 // Final fallback compression
                 encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
                 break
             } else {
                 high = mid - 1
             }
         }

         return encodedImage
     }*/
    /*fun encodeImage(bm: Bitmap): String {
        val maxSize = 300 * 1024 // 125KB in bytes
        var low = 1
        var high = 100
        var mid: Int
        var encodedImage = ""

        while (low <= high) {
            mid = (low + high) / 2
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, mid, baos)
            val byteArray = baos.toByteArray()

            if (byteArray.size <= maxSize) {
                // Acceptable size, continue searching for better quality
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
                low = mid + 1 // Try increasing the quality
            } else {
                // Too large, reduce quality
                high = mid - 1
            }
        }

        return encodedImage
    }*/


    fun decodeImage(encodedImage: String): Bitmap? {
        val imageAsBytes =
            Base64.decode(encodedImage.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }


    fun getPermissionList(): List<String> {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE
            )
        } else {
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }



    //********** Validation ************************
    fun isValidMail(target: String): Boolean {

        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
            .matches();
    }


    fun isValidPfmsId(pfms: String?): Boolean {
        val pfms_pattern = "^[0-9]{4}+DBTPAYREQ+([0-9]+)$"
        val pattern = Pattern.compile(pfms_pattern)
        val matcher = pattern.matcher(pfms)
        return matcher.matches()
    }

    fun isValidFtoNumber(ftoNo: String?): Boolean {
        val fto_pattern = "^[A-Za-z]{2}([0-9]+)+\\_+([0-9]+)+FTO+(\\_[0-9]+)$"
        val pattern = Pattern.compile(fto_pattern)
        val matcher = pattern.matcher(ftoNo)
        return matcher.matches()
    }

    fun isValidReg(Regno: String): Boolean {
        // String REG_PATTERN = "^[A-Za-z]{2}+(\\-[_0-9]{2})+(\\-[_0-9]{3})+(\\-[_0-9]{3})+(\\-[_0-9]+)+(\\/[a-zA-Z_0-9]+)$";
        var REG_PATTERN: String? = null
        REG_PATTERN = if (Regno.length == 9) {
            "[a-zA-Z]{2}[0-9]{7}"
        } else {
            "^[A-Za-z]{2}+(\\-[_0-9]{2})+(\\-[_0-9]{3})+(\\-[_0-9]{3})+(\\-[_0-9]+)+(\\/[a-zA-Z_0-9]+)$"
        }
        // String REG_PATTERN = "^[A-Za-z]{2}+(\\-[_0-9]+)+(\\/[a-zA-Z0-9]+)$";
        val pattern = Pattern.compile(REG_PATTERN)
        val matcher = pattern.matcher(Regno)
        return matcher.matches()
    }

    fun isValidJobCardNo(nrega: String, stateCode: String): Boolean {
        if (nrega.startsWith(stateCode)) {
            val pattern: Pattern
            var pattern1: Pattern? = null
            if (stateCode == "RJ") {
                pattern = Pattern.compile("[a-zA-Z]{2}[-][0-9]{18}[/][0-9a-zA-Z]{0,10}")
                pattern1 = Pattern.compile("[a-zA-Z]{2}-[0-9]{18}/[0-9a-zA-Z]{0,10}(-[A-Z])?")
            } else if (stateCode == "MN") {
                pattern =
                    Pattern.compile("[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{3}[-][0-9]{3}[-][0-9]{3}[/][0-9a-zA-Z]{0,10}")
                pattern1 =
                    Pattern.compile("[A-Z]{2}[-][0-9]{2}[-][0-9]{3}[-][0-9]{3}[-][0-9]{3}[/][0-9a-zA-Z]{0,10}[-][A-Z]{3}[0-9]{2}")

            } else if (stateCode == "HP" || stateCode == "BH") {
                pattern =
                    Pattern.compile("[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{3}[-][0-9]{3}[-][0-9]{8}[/][0-9a-zA-Z]{0,10}")
                pattern1 =
                    Pattern.compile("[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{3}[-][0-9]{3}[-][0-9]{8}[/][0-9a-zA-Z]{0,10}[-][A-Za-z]{0,10}")
            } else {
                pattern =
                    Pattern.compile("[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{3}[-][0-9]{3}[-][0-9]{3}[/][0-9a-zA-Z]{0,10}")
                pattern1 =
                    Pattern.compile("[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{3}[-][0-9]{3}[-][0-9]{3}[/][0-9a-zA-Z]{0,10}[-][A-Za-z]{0,10}")
            }
            val matcher = pattern.matcher(nrega)
            val matcher1 = pattern1.matcher(nrega)
            return matcher.matches() || matcher1.matches()
        } else return false
    }

    fun isValidPanCard(panCard: String?): Boolean {
        val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher = pattern.matcher(panCard)
        return if (matcher.matches()) true
        else false
    }

    fun isValidMobile(phoneNo: String): Boolean {
        var flag = false
        if (phoneNo.length != 10) {
            flag = false
        } else if (phoneNo.startsWith("00")) {
            flag = false
        } else if (phoneNo.startsWith("9")) {
            flag = true
        } else if (phoneNo.startsWith("8")) {
            flag = true
        } else if (phoneNo.startsWith("7")) {
            flag = true
        } else if (phoneNo.startsWith("6")) {
            flag = true
        }
        return flag
    }

    fun isValidAadhar(phoneNo: String): Boolean {
        return phoneNo.length == 12
    }
    //******************************************************

    fun isGPSEnabled(activity: Activity): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getRandom(): String {
        val DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val RANDOM = Random()
        val sb = StringBuilder(10)
        for (i in 0..9) {
            sb.append(DATA[RANDOM.nextInt(DATA.length)])
        }
        return sb.toString()
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun encryptString(string: String): String {
        try {
            return CryptLib().encrypt(string)
        } catch (e: NoSuchAlgorithmException) {
            return ""
        } catch (e: NoSuchPaddingException) {
            return ""
        } catch (e: InvalidAlgorithmParameterException) {
            return ""
        } catch (e: UnsupportedEncodingException) {
            return ""
        } catch (e: BadPaddingException) {
            return ""
        } catch (e: IllegalBlockSizeException) {
            return ""
        } catch (e: InvalidKeyException) {
            return ""
        }
    }

    fun isJSONValid(json: String): Boolean {
        return try {
            JSONObject(json) // Try to parse as JSONObject
            true
        } catch (ex: JSONException) {
            try {
                JSONArray(json) // Try to parse as JSONArray
                true
            } catch (ex: JSONException) {
                false
            }
        }
    }


    fun createFilePartFromBase64(base64String: String, fileName: String): MultipartBody.Part {
        // Decode the Base64 string to a byte array
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)

        // Create a temporary file to store the image
        val tempFile = File.createTempFile("temp_image", null)
        tempFile.writeBytes(byteArray)

        // Create RequestBody instance from file
        val requestFile = tempFile.asRequestBody("application/octet-stream".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("file", fileName, requestFile)
    }

    fun getStateList(context: Context?): List<State> {
        val typeToken = object : TypeToken<List<State>>() {}.type
        return Gson().fromJson<List<State>?>(loadJSONFromAsset(context, "States.json"), typeToken)
            .sortedBy {
                it.name
            }
    }

    fun getDistrictList(context: Context?, stateId: String): List<District> {
        val typeToken = object : TypeToken<List<District>>() {}.type
        val districts =
            Gson().fromJson<List<District>>(loadJSONFromAsset(context, "Districts.json"), typeToken)
                .sortedBy {
                    it.District_Name
                }
        return districts.filter {
            TextUtils.equals(it.State_Code, stateId)
        }
    }

    fun getBlockList(context: Context?, districtId: String): List<Block> {
        val typeToken = object : TypeToken<List<Block>>() {}.type
        val blocks =
            Gson().fromJson<List<Block>>(loadJSONFromAsset(context, "Blocks.json"), typeToken)
                .sortedBy {
                    it.block_name
                }
        return blocks.filter {
            TextUtils.equals(it.District_Code, districtId)
        }
    }


    fun downloadAndSaveImage(context: Context, imageUrl: String, fileName: String) {
        val encodedUrl = Uri.encode(imageUrl, "@#&=*+-_.,:!?()/~'%")
        val file = File(context.filesDir, fileName)
        Glide.with(context)
            .downloadOnly()
            .load(encodedUrl)
            .listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>,
                    isFirstResource: Boolean
                ): Boolean {
                    // Handle the error here, e.g., log the error or show a toast
                    Log.e("GlideError", "Failed to load image", e)
                    return false // Return false if you want Glide to handle the error (e.g., show a placeholder)
                }

                override fun onResourceReady(
                    resource: File,
                    model: Any,
                    target: Target<File>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Return false if you want Glide to handle the setting of the image on the ImageView
                    return false
                }

            })
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    resource.copyTo(file, overwrite = true)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle when the image is cleared
                }
            })
    }



}