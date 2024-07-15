import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.momtaz.amshopping.data.Product
import com.momtaz.amshopping.databinding.ProductRvItemBinding

class SearchProductAdapter : RecyclerView.Adapter<SearchProductAdapter.SearchProductsViewHolder>() {

    inner class SearchProductsViewHolder(private val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                val priceAfterOffer = product.offerPercentage?.let {
                    product.price - (product.price * it / 100)
                } ?: product.price

                tvNewPrice.text = "EGP ${String.format("%.2f", priceAfterOffer)}"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvNewPrice.visibility = if (product.offerPercentage == null) View.INVISIBLE else View.VISIBLE

                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                tvPrice.text = "EGP ${product.price}"
                tvName.text = product.name

                itemView.setOnClickListener {
                    onClick?.invoke(product)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductsViewHolder {
        val binding = ProductRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }

    var onClick: ((Product) -> Unit)? = null
}
