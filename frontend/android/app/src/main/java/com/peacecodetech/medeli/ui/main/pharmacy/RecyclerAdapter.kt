package com.peacecodetech.medeli.ui.main.pharmacy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peacecodetech.medeli.databinding.SavedListBinding
import com.peacecodetech.medeli.model.Categories
import com.peacecodetech.medeli.model.Pharmacy

class RecyclerAdapter(
    private var onSelectedItemListener: OnSelectedItemListener,
    private var onViewDetail: OnViewDetail,
    private var itemCheckListener: (isChecked: Boolean, data: MutableList<Pharmacy>) -> Unit,
) :
    ListAdapter<Pharmacy, RecyclerAdapter.SavedListViewHolder>(ListComparator()), Filterable {
    private val data = mutableListOf<Pharmacy>()

    var categoriesList: ArrayList<Categories> = ArrayList()
    var categoriesListFiltered: ArrayList<Categories> = ArrayList()

    //bind the recycler list items
    inner class SavedListViewHolder(val binding: SavedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: Pharmacy?) {
            binding.pharmacyName.text = data?.name
            binding.distance.text = data?.distance
            binding.description.text = data?.description
            Glide.with(binding.imageView).load(data?.logo).into(binding.imageView)
            binding.ratingBar.rating = data?.rating?.toFloat()!!
        }
    }

    //inflate the List
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListViewHolder {
        return SavedListViewHolder(
            SavedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //bind the model list to the recycler list
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SavedListViewHolder, position: Int) {
        val getItemPosition = getItem(position)
        holder.bind(getItemPosition)
        onSelectedItemListener.onSelectedItemListener(holder.binding)
        holder.itemView.setOnClickListener {
            onViewDetail.onOnViewDetail(getItemPosition)
        }

    }

    //perform update operation
    class ListComparator : DiffUtil.ItemCallback<Pharmacy>() {
        override fun areItemsTheSame(oldItem: Pharmacy, newItem: Pharmacy): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Pharmacy, newItem: Pharmacy): Boolean {
            return oldItem == newItem
        }
    }


    interface OnSelectedItemListener {
        fun onSelectedItemListener(viewListBinding: SavedListBinding)
    }

    interface OnViewDetail {
        fun onOnViewDetail(pharmacy: Pharmacy)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                categoriesListFiltered = if (charString.isEmpty()) categoriesList else {
                    val filteredList = ArrayList<Categories>()
                    categoriesList
                        .filter {
                            it.name!!.contains(constraint!!)//or something  (it.author.contains(constraint))
                        }.forEach { value ->
                            filteredList.add(value)
                        }
                    filteredList
                }

                return FilterResults().apply {
                    values = categoriesListFiltered
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoriesListFiltered = if (results?.values == null)
                    ArrayList()
                else {
                    results.values as ArrayList<Categories>
                }
                notifyDataSetChanged()
            }

        }
    }


}