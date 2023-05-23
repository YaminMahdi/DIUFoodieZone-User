package com.diu.mlab.foodie.zone.presentation.user

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.diu.mlab.foodie.zone.R
import com.diu.mlab.foodie.zone.databinding.ActivityUserMainBinding
import com.diu.mlab.foodie.zone.presentation.auth.LoginActivity
import com.diu.mlab.foodie.zone.util.changeStatusBarColor
import com.diu.mlab.foodie.zone.util.setBounceClickListener
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserMainBinding
    val viewModel by viewModels<UserMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.appBarUserMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navView.getHeaderView(0).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.appBarUserMain.btnMenu.setBounceClickListener{
            drawerLayout.open()
        }
        changeStatusBarColor(R.color.whiteX,resources.getBoolean(R.bool.isDarkDisable))
//        ActionBarDrawerToggle(this,drawerLayout,binding.appBarUserMain.toolbar,R.string.app_name, R.string.app_name).syncState()


        val meowNav = binding.appBarUserMain.contentMain.meowNav

        meowNav.add(MeowBottomNavigation.Model(1, R.drawable.ic_cart2))
        meowNav.add(MeowBottomNavigation.Model(2, R.drawable.ic_home))
        meowNav.add(MeowBottomNavigation.Model(3, R.drawable.ic_order_list))

        meowNav.show(2,false)
        meowNav.setOnClickMenuListener {
            when(it.id){
                1 ->{

                }
            }
        }

        findViewById<TextView>(R.id.edit).setBounceClickListener {

        }
        findViewById<TextView>(R.id.logout).setBounceClickListener {

        }
    }

}