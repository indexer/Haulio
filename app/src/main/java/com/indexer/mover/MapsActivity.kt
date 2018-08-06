package com.indexer.mover

import android.Manifest.permission
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.indexer.mover.R.string
import com.indexer.mover.model.Job
import com.sembozdemir.permissionskt.askPermissions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.toolbar
import kotlinx.android.synthetic.main.activity_maps.job_number
import kotlinx.android.synthetic.main.activity_maps.user_name
import kotlinx.android.synthetic.main.activity_maps.user_profile
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import com.indexer.mover.base.AppDatabase
import kotlinx.android.synthetic.main.activity_maps.address
import kotlinx.android.synthetic.main.activity_maps.job
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

  private lateinit var mMap: GoogleMap
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var mGoogleSignInClient: GoogleSignInClient
  private lateinit var jobObject: Job


  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_maps)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = getString(R.string.app_name)

    jobObject = intent.extras.get("job") as Job


    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    val mapFragment = supportFragmentManager
        .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    val account = GoogleSignIn.getLastSignedInAccount(this)
    if (!account?.isExpired!!) {
      Picasso.get()
          .load(account.photoUrl)
          .into(user_profile)
      user_name.text = account.displayName
      job_number.text = getString(string.jobnumber) + jobObject.job_id.toString()
      address.text = getString(string.address) + jobObject.address
    } else {
      finish()
    }

    val list = AppDatabase.getInstance(this)
        .jobDao()
        .getAllCompanyName()
    val languages = list.toTypedArray()
    val adapter =
      ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)
    job.setAdapter(adapter)

    job.onItemClickListener = OnItemClickListener { parent, arg1, pos, id ->
      val selected = adapter.getItem(pos)
      val jobList = AppDatabase.getInstance(this)
          .jobDao()
          .getJobByCompanyName(selected)
      updateViewUI(jobList)
    }

  }

  private fun updateViewUI(jobList: List<Job>) {
    job.clearFocus()
    job_number.text = getString(string.jobnumber) + jobList[0].job_id.toString()
    address.text = getString(string.address) + jobList[0].address
    val geoCoder = Geocoder(this)
    try {
      val addresses = geoCoder.getFromLocationName(jobList[0].address, 1)
      if (addresses != null && !addresses.isEmpty()) {
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(addresses[0].latitude, addresses[0].longitude)
            ).title("Job location")
        )
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                (LatLng(addresses[0].latitude, addresses[0].longitude)), 13f
            )
        )
      } else {

      }
    } catch (e: IOException) {
      // handle exception
      e.printStackTrace()
    }
    job.text.clear()

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.home_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId

    if (id == R.id.action_favorite) {
      mGoogleSignInClient.signOut()
          .addOnCompleteListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
          }
      return true
    }

    if (id == android.R.id.home) {
      onBackPressed()
    }

    return super.onOptionsItemSelected(item)
  }

  @SuppressLint("MissingPermission")
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    val geoCoder = Geocoder(this)
    try {
      val addresses = geoCoder.getFromLocationName(jobObject.address, 1)
      if (addresses != null && !addresses.isEmpty()) {
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(addresses[0].latitude, addresses[0].longitude)
            ).title("Job location")
        )

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                (LatLng(addresses[0].latitude, addresses[0].longitude)), 13f
            )
        )
      } else {

      }
    } catch (e: IOException) {
      // handle exception
      e.printStackTrace()
    }




    askPermissions(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION) {
      onGranted {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
              // Got last known location. In some rare situations this can be null.
              if (location != null) {
                mMap.addMarker(
                    MarkerOptions().position(LatLng(location.latitude, location.longitude)).title(
                        "Current location"
                    )
                )
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng
                        (location.latitude, location.longitude), 13f
                    )
                )
              }
            }
      }

      onDenied {

      }

    }

  }

}
