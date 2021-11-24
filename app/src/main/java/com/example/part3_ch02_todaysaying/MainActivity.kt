package com.example.part3_ch02_todaysaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.part3_ch02_todaysaying.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initViews()
        initData()

    }

    private fun initViews() {

        mainBinding.viewPager.adapter = QuotesPagerAdapter(emptyList())

    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        // 서버에서 막지 않는 이상 앱에 들어올때마다 패치 진행
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {

            }
        }

    }



}



/*
<작동해야하는 기능들>
코드 수정 없이 명언을 추가할 수 있다.
코드 수정 없이 이름을 숨길 수 있다.
무한 스와이프할 수 있다.

Firebase Remote Config
ViewPager2

1. Firebase - Remote Config  !!정리따로 필요!!
앱 업데이트를 게시하지 않아도 하루 활성 사용자 수 제한 없이 무료로 앱의 동작과 모양을 변경할 수 있도록 해준다.


 */



