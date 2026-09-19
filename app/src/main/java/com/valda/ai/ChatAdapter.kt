package com.valda.ai

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.valda.ai.databinding.ItemMessageBinding

class ChatAdapter(
    private var data: List<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.VH>() {

    inner class VH(val b: ItemMessageBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        val b = ItemMessageBinding.inflate(
            LayoutInflater.from(p.context), p, false)
        return VH(b)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val m = data[pos]
        h.b.tvMessage.text = m.text

        val lp = h.b.tvMessage.layoutParams as LinearLayout.LayoutParams
        if (m.isUser) {
            lp.gravity = Gravity.END
            h.b.tvMessage.setBackgroundColor(
                h.itemView.context.getColor(R.color.bubble_user))
            h.b.tvMessage.setTextColor(
                h.itemView.context.getColor(R.color.black))
        } else {
            lp.gravity = Gravity.START
            h.b.tvMessage.setBackgroundColor(
                h.itemView.context.getColor(R.color.bubble_ai))
            h.b.tvMessage.setTextColor(
                h.itemView.context.getColor(R.color.text_primary))
        }
        h.b.tvMessage.layoutParams = lp
    }

    override fun getItemCount() = data.size

    fun submit(list: List<ChatMessage>) {
        data = list
        notifyDataSetChanged()
    }
}
