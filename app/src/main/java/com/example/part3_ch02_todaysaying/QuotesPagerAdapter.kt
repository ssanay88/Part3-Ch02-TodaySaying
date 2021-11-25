package com.example.part3_ch02_todaysaying

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(val quotes : List<Quote> , val isNameRevealed : Boolean)
    : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder  =
        QuoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false))

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {

        holder.bind(quotes[position] , isNameRevealed )

    }

    override fun getItemCount() = quotes.size

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote , isNameRevealed: Boolean) {

            quoteTextView.text = quote.quote

            // 이름을 감추고 싶을 경우 True , 이름까지 표시할 경우 False
            if (isNameRevealed) {
                nameTextView.text = quote.name
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }



        }

    }

}