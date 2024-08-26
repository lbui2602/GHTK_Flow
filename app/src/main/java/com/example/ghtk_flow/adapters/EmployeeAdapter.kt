package com.example.ghtk_flow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ghtk_flow.databinding.LayoutEmployeeItemBinding
import com.example.ghtk_flow.models.Employee

class EmployeeAdapter(private var employees: List<Employee>,private var iClick:IClick) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {


    class EmployeeViewHolder(private val binding: LayoutEmployeeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee,iClick: IClick) {
            binding.tvName.text = employee.name
            binding.tvYob.text = employee.yob.toString()
            binding.tvCountry.text = employee.country
            binding.imgDelete.setOnClickListener {
                iClick.onClick(employee)
            }
        }
    }
    fun updateList(list : List<Employee>){
        employees = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = LayoutEmployeeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun getItemCount(): Int = employees.size

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(employees[position],iClick)
    }
}
interface IClick{
    fun onClick(employee: Employee)
}