package com.swork.assignment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

data class Order(val name: String, val location: LatLng, val phone: Int, val address: String)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val PERMISSION_REQUEST_CODE = 101

    private val mOrders = listOf(
            Order("shawn madnes", LatLng(19.1231, 12.12312), 860012312, "hobokon, Nywork"),
            Order("chrester hola", LatLng(20.1231, 21.12312), 960012312, "new jersy, Nywork"),
            Order("sharon1", LatLng(21.1231, 22.12312), 900012312, "abc, Nywork"),
            Order("sharon2", LatLng(21.1231, 22.12312), 900012312, "abc, Nywork")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        when {
            ContextCompat.checkSelfPermission(this@MapsActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this@MapsActivity,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
            -> {
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE),
                        PERMISSION_REQUEST_CODE
                )
            }
            else -> {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE),
                        PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val recyclerView = findViewById<RecyclerView>(R.id.listOfClients) as RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MapsActivity)
            adapter = ListAdapter(list = mOrders, map = mMap)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(getString(R.string.app_name), "One or more permissions have been denied by user")
                    Toast.makeText(this@MapsActivity, "Grant all permissions to run the app properly", Toast.LENGTH_LONG).show()
                } else {
                    Log.i(getString(R.string.app_name), "All permissions has been granted by user")
                    val mapFragment = supportFragmentManager
                            .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }
        }
    }
}