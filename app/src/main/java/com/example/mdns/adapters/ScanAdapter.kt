package com.example.mdns.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mdns.R
import com.example.mdns.`object`.ScanObject
import java.util.ArrayList
import java.util.Comparator
import java.util.TreeSet

/**
 * Created by Iswarya on 17/07/20
 */
class ScanAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ScanAdapter.ViewHolder>() {
    private val scanObjectList = ArrayList<ScanObject>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_adpater, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val scanObject = scanObjectList[i]
        viewHolder.txtName.text = "Service Name  " + " : "+scanObject.serviceName
        viewHolder.txtType.text = "Service Type  " +" : "+ scanObject.serviceType
        viewHolder.txtIp.text = "IP Address  " + " : "+scanObject.hostAddress
        viewHolder.txtPort.text = "Port  " +" : " +scanObject.servicePort

    }

    override fun getItemCount(): Int {
        return scanObjectList.size
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var txtName: TextView
         var txtType: TextView
         var txtIp: TextView
         var txtPort: TextView

        init {

            txtPort = itemView.findViewById(R.id.txtPort)
            txtIp = itemView.findViewById(R.id.txtIp)
            txtName = itemView.findViewById(R.id.txtName)
            txtType = itemView.findViewById(R.id.txtType)
        }
    }

    fun updateList(data: ScanObject) {
        scanObjectList.add(data)
        removeDuplicate()
        notifyDataSetChanged()
    }

    private fun removeDuplicate() {
        val list = TreeSet(Comparator<ScanObject> { o1, o2 ->
            o1.hostAddress.toString().compareTo(o2.hostAddress.toString())
        })
        list.addAll(scanObjectList)
        scanObjectList.clear()
        scanObjectList.addAll(list)
    }

    fun refreshAdapter() {
        scanObjectList.clear()
        notifyDataSetChanged()
    }
}
