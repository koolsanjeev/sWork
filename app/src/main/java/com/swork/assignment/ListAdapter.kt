package com.swork.assignment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions

class ListAdapter(private val list: List<Order>, val map: GoogleMap) : RecyclerView.Adapter<OrderViewHolder>() {

    var mSelectedItemIndex: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OrderViewHolder(inflater, parent, this)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order: Order = list[position]
        holder.bind(order, position)
    }

    override fun getItemCount(): Int = list.size
}

class OrderViewHolder(inflater: LayoutInflater, parent: ViewGroup, listAdapter: ListAdapter)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_order, parent, false)) {
    private var mNameView: TextView? = null
    private var mAddressView: TextView? = null
    private var mButtonDeliveryReturn: Button? = null
    private var mButtonNavigationRoute: ImageView? = null
    private var mButtonPhoneDialer: ImageView? = null
    private var mListAdapter: ListAdapter? = null

    init {
        mNameView = itemView.findViewById(R.id.list_item_order_name)
        mAddressView = itemView.findViewById(R.id.list_item_order_address)
        mButtonDeliveryReturn = itemView.findViewById(R.id.list_item_button_delivery_return)
        mButtonNavigationRoute = itemView.findViewById(R.id.list_item_ic_navigation_route)
        mButtonPhoneDialer = itemView.findViewById(R.id.list_item_ic_phone_dialer)
        mListAdapter = listAdapter
    }

    fun bind(order: Order, position: Int) {
        itemView.setOnClickListener {
            if (mListAdapter?.mSelectedItemIndex != position) {
                mListAdapter?.mSelectedItemIndex = position
                mListAdapter?.notifyDataSetChanged()
            }
        }
        mNameView?.text = order.name
        mAddressView?.text = order.address
        if (mListAdapter?.mSelectedItemIndex == position) {
            mButtonDeliveryReturn?.visibility = View.VISIBLE
        } else {
            mButtonDeliveryReturn?.visibility = View.GONE
        }

        mButtonNavigationRoute?.setOnClickListener {
            if (mListAdapter?.mSelectedItemIndex != position) {
                itemView.callOnClick()
            }
            if (ContextCompat.checkSelfPermission(it.context,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mListAdapter?.map?.addMarker(MarkerOptions().position(order.location).title(order.name + '\n' + order.address))
                mListAdapter?.map?.moveCamera(CameraUpdateFactory.newLatLng(order.location))
            }
            Toast.makeText(itemView.context, order.name + '\n' + order.address, Toast.LENGTH_SHORT).show()
        }

        mButtonPhoneDialer?.setOnClickListener {
            if (mListAdapter?.mSelectedItemIndex != position) {
                itemView.callOnClick()
            }
            if (ContextCompat.checkSelfPermission(it.context,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_CALL);
                intent.data = Uri.parse("tel:" + order.phone)
                startActivity(itemView.context, intent, null)
            }
        }
    }
}
