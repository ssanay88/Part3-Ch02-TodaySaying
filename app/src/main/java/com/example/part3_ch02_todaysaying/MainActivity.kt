package com.example.part3_ch02_todaysaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.part3_ch02_todaysaying.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue


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
        // setPageTransformer : 페이지를 스크롤 시 원하는 이펙트를 커스텀한다
        // page : 변형을 적용할 페이지 타입 View
        // position : 단순히 페이지 1,2,3..이 아닌 스크롤을 넘기는 만큼 변화하는 포지션을 나타낸다.
        // 현재 보는 페이지를 0으로 두고 왼쪽으로 스크롤시 -0.5 , -1 ... 오른쪽은 이와 반대로 작용
        mainBinding.viewPager.setPageTransformer { page, position ->
            // 여기서는 투명도(alpha)만 컨트롤해준다.
            when {
                position.absoluteValue >= 1F -> {
                    page.alpha = 0F
                }

                position == 0F -> {
                    page.alpha = 1F
                }

                else -> {
                    // alpha 값은 0~1사이 값이므로 가운데(0) 위치 시 alpha값을 1로 만들어줘야한다.
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }

        }
    }

    // remoteConfig 원격 구성 로딩 전략 부분 -> 사용자가 사용하고 있을 때 UI를 변경시키는 행동을 피하자!
    // 앱을 처음 시작할 때 데이터를 로딩
    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        // 서버에서 막지 않는 이상 앱에 들어올때마다 패치 진행 -> 개발할때만 바로 확인하기 위해 패치 텀을 작게 설정
        // 실제 앱 배포시에는 12시간 패치 텀이 기본값
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )

        // 패치가 비동기적으로 진행되기 때문에 리스너 설정
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // 패치 작업이 완료될 경우
            mainBinding.progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)
                Log.d("로그","$quotes")

            }
        }

    }

    // Json 파일을 Parsing하는 메소드
    // 인자로 Firebase에 설정한 json String 파일을 넣어준다.
    private fun parseQuotesJson(json : String) : List<Quote> {

        // json String 값을 가지고 새로운 json Array를 만들어 주는 메소드 - JSONArray
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()    // JSONObject로 이루어진 리스트 선언

        // json array에서 json Object형태로 뽑아서 jsonList에 하나씩 추가
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }

        // json(String) 배열 -> json(Object)로 이루어진 리스트 -> Quote 데이터 리스트로 반환
    }

    private fun displayQuotesPager(quotes:List<Quote> , isNameRevealed:Boolean) {

        val adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )


        mainBinding.viewPager.adapter = adapter
        // 아이템의 절반 위치로 이동시켜버린다. 두번째 인자는 이동 시 부드럽게 넘어가는 동작에 관한
        // Boolean 값으로 여기서는 사용하지 않는다.
        mainBinding.viewPager.setCurrentItem(adapter.itemCount/2, false)

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

2. ViewPager2 : 뷰 페이지에 대한 여러가지 효과들을 작동시킨다.
 */



