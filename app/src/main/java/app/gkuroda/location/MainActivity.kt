package app.gkuroda.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUESTING_LOCATION_PERMISSIONS = 1
    }

    /** 位置情報の取得に必要な基本的な Permission の一覧 */
    private val BASE_LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /** バックグラウンドで位置情報仕様時に必要なパーミッション */
    val ACCESS_BACKGROUND_LOCATION = arrayOf(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    /** 基本的なものに加え、バックグラウンドも含めたすべての位置情報パーミッション */
    private val ALL_LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationPermissionStatus.isChecked = isLocationPermissionsEnabled()

        backgroundLocationPermissionStatus.isChecked = isAccessBackgroundLocationEnabled()

        // ACCESS_COARSE_LOCATION・ACCESS_FINE_LOCATIONを取得する
        locationPermissionGetButton.setOnClickListener {
            requestPermissions(
                BASE_LOCATION_PERMISSIONS,
                REQUESTING_LOCATION_PERMISSIONS
            )
        }

        // ACCESS_BACKGROUND_LOCATIONのみ取得する
        backgroundLocationPermissionGetButton.setOnClickListener {
            requestPermissions(
                ACCESS_BACKGROUND_LOCATION,
                REQUESTING_LOCATION_PERMISSIONS
            )
        }

        // ACCESS_COARSE_LOCATION・ACCESS_FINE_LOCATION・ACCESS_BACKGROUND_LOCATIONを取得する
        allLocationPermissionGetButton.setOnClickListener {
            requestPermissions(
                ALL_LOCATION_PERMISSION,
                REQUESTING_LOCATION_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        // permissions と grantResults をまとめる
        val zippedResults = permissions.zip(grantResults.toTypedArray())

        if (zippedResults.isEmpty()) {
            return
        }

        when (requestCode) {
            REQUESTING_LOCATION_PERMISSIONS -> {
                locationPermissionStatus.isChecked = isLocationPermissionsEnabled()

                backgroundLocationPermissionStatus.isChecked = isAccessBackgroundLocationEnabled()

            }
        }
    }

    /** ACCESS_FINE_LOCATION  ACCESS_COARSE_LOCATION が取得できているか確認 */
    private fun isLocationPermissionsEnabled(): Boolean {
        return BASE_LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /** ACCESS_BACKGROUND_LOCATION が取得できているか確認。API28以下では存在しないのでtrue扱いにする */
    private fun isAccessBackgroundLocationEnabled(): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) return true
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}