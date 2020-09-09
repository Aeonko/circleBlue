package com.nemanjamiseljic.circleblue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nemanjamiseljic.circleblue.retrofitclasses.Result

class RecyclerViewPeople : RecyclerView.Adapter<RecyclerViewPeople.RVPViewHolder> (){

    interface OpenDetails{
        fun openDetailsForHero(result: Result)
    }
    private lateinit var openDetails: OpenDetails

    fun setInterface(openDetails: OpenDetails) {
        this.openDetails = openDetails
    }

    private lateinit var listOfPeople: ArrayList<Result>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVPViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_card_person,parent,false)
        return RVPViewHolder(view)
    }

    override fun onBindViewHolder(holder: RVPViewHolder, position: Int) {
        val result = listOfPeople[position]
        holder.cardViewItemHolder.setOnClickListener {
            openDetails.openDetailsForHero(result)
        }
        holder.name.text = result.name
        holder.height.text = result.height
        holder.mass.text = result.mass
    }

    public fun setListOfPeople(newList:ArrayList<Result>){
        listOfPeople = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        if(::listOfPeople.isInitialized){
            return listOfPeople.size
        }else{
            return 0
        }

    }

    class RVPViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardViewItemHolder:CardView = itemView.findViewById(R.id.rvcp_card_view)
        val name:TextView = itemView.findViewById(R.id.rvcp_name)
        val height:TextView = itemView.findViewById(R.id.rvcp_height)
        val mass:TextView = itemView.findViewById(R.id.rvcp_mass)
    }
}