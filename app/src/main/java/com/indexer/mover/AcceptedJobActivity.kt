package com.indexer.mover

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.indexer.mover.base.AppDatabase
import com.indexer.mover.base.BaseViewHolder.OnItemClickListener
import com.indexer.mover.rest.Config
import com.indexer.mover.rest.RestClient
import com.indexer.ottohub.rest.enqueue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.drawer_layout
import kotlinx.android.synthetic.main.activity_home.job_list
import kotlinx.android.synthetic.main.activity_home.navigation
import kotlinx.android.synthetic.main.activity_home.toolbar

class AcceptedJobActivity : AppCompatActivity(), OnItemClickListener {
  override fun onItemClick(
    position: Int,
    status: Int
  ) {
    if (status != Config.accept) {
      val intent = Intent(this, MapsActivity::class.java)
      intent.putExtra("job", userJobAdapter.getItem(position))
      startActivity(intent)
    }

    if (status == Config.accept) {
      AppDatabase.getInstance(this@AcceptedJobActivity)
          .jobDao()
          .insertAcceptJob(userJobAdapter.getItem(position))
      userJobAdapter.removeItem(position)
      userJobAdapter.notifyItemRemoved(position)
      userJobAdapter.notifyDataSetChanged()
    }

  }

  private lateinit var mGoogleSignInClient: GoogleSignInClient
  lateinit var layoutManager: LinearLayoutManager
  private lateinit var userJobAdapter: UserJobAdapter
  private lateinit var appDatabase: AppDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    userJobAdapter = UserJobAdapter(this)
    layoutManager = LinearLayoutManager(this)
    job_list.layoutManager = layoutManager
    job_list.adapter = userJobAdapter
    job_list.addItemDecoration(SpacesItemDecoration(16))
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowTitleEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_drawer)

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    appDatabase = AppDatabase.getInstance(this)

    val header = navigation.getHeaderView(0)
    val view = header.findViewById(R.id.user_profile) as ImageView
    val userName = header.findViewById(R.id.user_profile_name) as TextView
    val account = GoogleSignIn.getLastSignedInAccount(this)
    if (account != null) {
      Picasso.get()
          .load(account.photoUrl)
          .into(view)
      userName.text = account.displayName
    }
    setupDrawerContent(navigation)

  }

  private fun setupDrawerContent(navigationView: NavigationView) {
    navigationView.setNavigationItemSelectedListener { menuItem ->
      selectDrawerItem(menuItem)
      true
    }
  }

  fun selectDrawerItem(menuItem: MenuItem) {

    when (menuItem.itemId) {
      R.id.acceptJob -> {

      }
      R.id.stop -> {
        mGoogleSignInClient.signOut()
        this.finish()
      }

    }

    // Highlight the selected item has been done by NavigationView
    menuItem.isChecked = true
    // Set action bar title
    title = menuItem.title
    // Close the navigation drawer
    drawer_layout.closeDrawers()
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
      val intent = Intent(applicationContext, SignInActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      startActivity(intent)
      return true
    }

    if (id == android.R.id.home) {
      drawer_layout.openDrawer(GravityCompat.START)
    }

    return super.onOptionsItemSelected(item)
  }

}
