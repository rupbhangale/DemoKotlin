package com.ezest.dackotlin.login

import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import com.ezest.dackotlin.R
import java.io.IOException

import kotlinx.android.synthetic.main.activity_login.mSurfaceView

class LoginActivity : AppCompatActivity(), SurfaceHolder.Callback, LoginFragment.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener {
    override fun onHelp() {
        replaceFragment(HelpFragment.newInstance(), R.id.frag_container_login, "HelpFrag")
    }

    override fun onLogin() {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mp?.stop()
        mp?.release()
        mp = null
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        val video = Uri.parse("android.resource://" + packageName + "/"
                + R.raw.dac_splash)

        try {
            if (mp == null)
                mp = MediaPlayer()
            mp?.setDataSource(this, video)
            mp?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels
        //Get the width of the screen
        //int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        //Get the SurfaceView layout parameters
        val lp = mSurfaceView.getLayoutParams()


        //Set the width of the SurfaceView to the width of the screen
        lp.width = screenWidth
        //Commit the layout parameters
        mSurfaceView.setLayoutParams(lp)

        //Start video
        mp?.setDisplay(holder)
        mp?.setLooping(false)
        mp?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, fragName: String){
        supportFragmentManager.inTransaction { add(frameId, fragment, fragName) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, fragName: String) {
        supportFragmentManager.inTransaction{replace(frameId, fragment, fragName).addToBackStack(null)}
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    private var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mp = MediaPlayer()
        mSurfaceView.holder.addCallback(this)

        /*Add Login Fragment*/
        addFragment(LoginFragment.newInstance(), R.id.frag_container_login, "LoginFrag")
    }
}
