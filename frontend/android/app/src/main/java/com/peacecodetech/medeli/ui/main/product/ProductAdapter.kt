package com.peacecodetech.medeli.ui.main.product


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peacecodetech.medeli.databinding.ProductListBinding
import com.peacecodetech.medeli.data.responses.Categories
import com.peacecodetech.medeli.data.responses.Products


class ProductAdapter(
    private var onSelectedItemListener: OnSelectedItemListener,
    private var onViewDetail: OnViewDetail,
    private var itemCheckListener: (isChecked: Boolean, data: MutableList<Categories>) -> Unit,
) :
    ListAdapter<Products, ProductAdapter.SavedListViewHolder>(ListComparator()), Filterable {
    private val data = mutableListOf<Products>()


    var categoriesList: ArrayList<Products> = ArrayList()
    var categoriesListFiltered: ArrayList<Products> = ArrayList()

    //bind the recycler list items
    inner class SavedListViewHolder(val binding: ProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: Products?) {
            if (data != null) {
                binding.productName.text = data.name
                binding.price.text = "${data.price}"
                binding.stockValue.text = "${data.quantity} in stock"
                Glide.with(binding.imgProduct).load(data.img_url).into(binding.imgProduct)
            }
        }
    }

    //inflate the List
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListViewHolder {
        return SavedListViewHolder(
            ProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //bind the model list to the recycler list
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SavedListViewHolder, position: Int) {
        val getItemPosition = getItem(position)
        categoriesList = arrayListOf(getItemPosition)
        categoriesListFiltered = categoriesList
        holder.bind(getItemPosition)
        onSelectedItemListener.onSelectedItemListener(holder.binding)
        holder.itemView.setOnClickListener {
            onViewDetail.onOnViewDetail(getItemPosition)
        }

        with(holder.binding) {
            var incrementAmt = 0
            val amt = Integer.parseInt(price.text.toString())
            var incrementNum = Integer.parseInt(qnty.text.toString())
            add.setOnClickListener {
                incrementNum += 1
                incrementAmt = amt * incrementNum
                qnty.text = incrementNum.toString()
                price.text = incrementAmt.toString()
                getItemPosition.price = incrementAmt.toDouble().toInt()
                getItemPosition.quantity = incrementNum

            }
            minus.setOnClickListener {
                if (incrementAmt > 0) {
                    incrementNum -= 1
                    incrementAmt = amt * incrementNum
                    qnty.text = incrementNum.toString()
                    price.text = incrementAmt.toString()
                }
            }
        }
    }

    //perform update operation
    class ListComparator : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    interface OnSelectedItemListener {
        fun onSelectedItemListener(viewListBinding: ProductListBinding)
    }

    interface OnViewDetail {
        fun onOnViewDetail(pharmacy: Products)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                categoriesListFiltered = if (charString.isEmpty()) categoriesList else {
                    val filteredList = ArrayList<Products>()
                    categoriesList
                        .filter {
                            it.name.contains(constraint!!) or (constraint.let { it1 ->
                                it.name.contains(
                                    it1
                                )
                            })//or something  (it.author.contains(constraint))
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
                categoriesListFiltered = if (results?.values == null)
                    ArrayList()
                else {
                    results.values as ArrayList<Products>
                }
                notifyDataSetChanged()
            }
        }
    }


}