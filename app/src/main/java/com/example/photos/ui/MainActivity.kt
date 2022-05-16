package com.example.photos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.photos.R
import com.example.photos.databinding.ActivityMainBinding
import com.example.photos.util.applyRequestResult
import com.example.photos.viewmodels.PhotoViewModel
import com.example.photos.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding?.apply {
            navController = findNavController(R.id.my_nav_host_fragment)
            appBarConfiguration = AppBarConfiguration(navController!!.graph)

            setupActionBarWithNavController(navController!!, appBarConfiguration!!)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return if(appBarConfiguration != null) navController?.navigateUp(appBarConfiguration!!) == true || super.onSupportNavigateUp()
        else super.onSupportNavigateUp()
    }
}