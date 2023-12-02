package tn.esprit.ecoventura.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import tn.esprit.ecoventura.MainActivity
import tn.esprit.ecoventura.R
import tn.esprit.ecoventura.model.Guide


class GuideAdapter(private val listener:OnItemClickListener



    ) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {
    var myGuides: List<Guide> = ArrayList()

    fun setGuides(guides: List<Guide>) {
        this.myGuides = guides
        notifyDataSetChanged()
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.guide_card, parent, false)
            return GuideViewHolder(view)
        }

        override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
            val guides = myGuides[position]
            holder.bind(guides)
        }

        override fun getItemCount(): Int {
            return myGuides.size
        }

        inner class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val titleTextView: TextView = itemView.findViewById(R.id.fullnameTextView)
            private val descriptionTextView: TextView = itemView.findViewById(R.id.locationTextView)
            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
            private val priceTextView:TextView = itemView.findViewById(R.id.priceTextView)

            init {
                itemView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                      listener.onItemClick(myGuides[position])
                    }
                }
            }

            fun bind(guide: Guide) {
                titleTextView.text = guide.fullname
                descriptionTextView.text = guide.location
                priceTextView.text = guide.price

                val imageUrl = guide.image

                Log.d("image", "$imageUrl")
                Glide.with(itemView.context).load(imageUrl).into(imageView)

            }
        }

        interface OnItemClickListener {
            fun onItemClick(guide: Guide)
        }


}

