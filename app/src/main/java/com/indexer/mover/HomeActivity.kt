package com.indexer.mover

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.indexer.mover.base.AppDatabase
import com.indexer.mover.base.BaseViewHolder.OnItemClickListener
import com.indexer.mover.rest.Config
import com.indexer.mover.rest.RestClient
import com.indexer.ottohub.rest.enqueue
import kotlinx.android.synthetic.main.activity_home.job_list
import kotlinx.android.synthetic.main.activity_home.toolbar

class HomeActivity : AppCompatActivity(), OnItemClickListener {
  override fun onItemClick(position: Int) {
    if (position != Config.accept) {
      val intent = Intent(this, MapsActivity::class.java)
      intent.putExtra("job", userJobAdapter.getItem(position))
      startActivity(intent)
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


    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    appDatabase = AppDatabase.getInstance(this)
    fetchFromApi()

  }

  fun fetchFromApi() {
    val jobList = RestClient.getService()
        .getJobList()
    jobList.enqueue(success = {
      if (it.isSuccessful) {
        if (it.body()?.isNotEmpty()!!) {
         appDatabase.jobDao().insertAllJob(it.body()!!)
          userJobAdapter.items = it.body()
        }
      }
    }, failure = {

    })
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
      this.finish()
      return true
    }

    return super.onOptionsItemSelected(item)
  }

}
