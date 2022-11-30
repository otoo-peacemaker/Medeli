package com.peacecodetech.medeli.ui.main.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peacecodetech.medeli.databinding.CategoriesCardBinding
import com.peacecodetech.medeli.databinding.SavedListBinding
import com.peacecodetech.medeli.model.Pharmacy

class HomeRecyAdapter(
    private var onViewDetail: OnViewDetail,
    private var onItemClick: ((Categories) -> Unit)? = null
) :
    RecyclerView.Adapter<HomeRecyAdapter.SavedListViewHolder>(), Filterable {
    private val data = mutableListOf<Pharmacy>()

    var categoriesList: ArrayList<Categories> = ArrayList()
    var categoriesListFiltered: ArrayList<Categories> = ArrayList()

    //bind the recycler list items
    inner class SavedListViewHolder(val binding: CategoriesCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: Categories?) {
            binding.nameId.text = data?.name
            if (data != null) {
                binding.cardView.setCardBackgroundColor(ColorStateList.valueOf(data.color))
            }

        }
    }

    //inflate the List
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListViewHolder {
        return SavedListViewHolder(
            CategoriesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    //bind the model list to the recycler list
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SavedListViewHolder, position: Int) {
       // val getItemPosition = getItem(position)
      //  holder.bind(getItemPosition)
        holder.bind(categoriesListFiltered[position])

        holder.itemView.setOnClickListener {
           // onViewDetail.onOnViewDetail(getItemPosition)
            onItemClick?.invoke(categoriesListFiltered[position])
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<Categories>) {
        categoriesList = list as ArrayList<Categories>
        categoriesListFiltered = categoriesList
        notifyDataSetChanged()
    }
    //perform update operation
    class ListComparator : DiffUtil.ItemCallback<Categories>() {
        override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem == newItem
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                categoriesListFiltered = if (charString.isEmpty()) categoriesList else{
                    val filteredList = ArrayList<Categories>()
                    categoriesList
                        .filter {
                            it.name.contains(constraint!!.trim())//or something  (it.author.contains(constraint))
                        }.forEach { value ->
                            filteredList.add(value)
                        }
                    filteredList
                }

                return FilterResults().apply {
                    values = categoriesListFiltered
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoriesListFiltered = if (results?.values ==null)
                    ArrayList()
                else {
                    results.values as ArrayList<Categories>
                }
                notifyDataSetChanged()
            }

        }
    }


    interface OnViewDetail {
        fun onOnViewDetail(pharmacy: Categories)
    }

    override fun getItemCount(): Int =categoriesListFiltered.size


}