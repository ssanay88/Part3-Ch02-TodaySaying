package com.example.part3_ch02_todaysaying

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(val quotes : List<Quote> , val isNameRevealed : Boolean)
    : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    // 뷰 홀더 객체를 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder  =
        QuoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false))

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {

        // 무한한 슬라이딩을 위해 계속 반복시켜준다. 포지션이 증가해도 나머지 연산을 통해 반복하도록 설정
        val actualPosition = position % quotes.size
        holder.bind(quotes[actualPosition] , isNameRevealed )    // 뷰 홀더에 데이터 연결

    }

    // 무한 슬라이드를 위해
    override fun getItemCount() = Int.MAX_VALUE     // 최대한 큰값을 준다.

    // 뷰홀더 이너 클래스
    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameRevealed: Boolean) {

            quoteTextView.text = "\"${quote.quote}\""


            // 이름을 감추고 싶을 경우 True , 이름까지 표시할 경우 False
            if (isNameRevealed) {
                nameTextView.text =  "- ${quote.name}"
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }



        }

    }

}