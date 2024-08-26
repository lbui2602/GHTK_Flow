package com.example.ghtk_flow.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghtk_flow.adapters.EmployeeAdapter
import com.example.ghtk_flow.adapters.IClick
import com.example.ghtk_flow.databinding.ActivityMainBinding
import com.example.ghtk_flow.models.Employee
import com.example.ghtk_flow.viewmodels.EmployeeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),IClick {
    private lateinit var binding: ActivityMainBinding
    private val employeeViewModel: EmployeeViewModel by viewModels()
    private val years = mutableListOf<String>()
    private val countries = mutableListOf<String>()
    private lateinit var adapterYear: ArrayAdapter<String>
    private lateinit var adapterCountry: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val employeeAdapter = EmployeeAdapter(mutableListOf(),this)
        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.adapter = employeeAdapter

        lifecycleScope.launch {
            employeeViewModel.employees.collect { employees ->
                Log.e("TAG", "Employee count: ${employees.size}")
                employeeAdapter.updateList(employees)
                updateYearAndCountryLists()
                initSpinners()
            }
        }
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                lifecycleScope.launch {
                    delay(1000)
                    employeeAdapter.updateList(employeeViewModel.searchByName(newText.toString()))
                }
                return false
            }
        })

        binding.btnLoc.setOnClickListener {
            val selectedYear = binding.spinnerYear.selectedItem.toString().trim()
            val selectedCountry = binding.spinnerCountry.selectedItem.toString().trim()

            Log.e("Filter", "Country: $selectedCountry, Year: $selectedYear")

            val filteredEmployees = employeeViewModel.filterEmployeesByYobAndCountry(selectedYear, selectedCountry)
            employeeAdapter.updateList(filteredEmployees)

            Log.e("Filtered Employees", filteredEmployees.toString())
        }
        binding.btnAdd.setOnClickListener {
            employeeViewModel.addEmployee(Employee("Bui Duc Luong ${employeeViewModel.employees.value.size+1}",
                2003+(employeeViewModel.employees.value.size % 3),
                "Ha Noi ${employeeViewModel.employees.value.size %4 }"))
        }
        employeeViewModel.fetchEmployees()
    }

    private fun updateYearAndCountryLists() {
        years.clear()
        years.addAll(employeeViewModel.employees.value.map { it.yob.toString() }.distinct().sorted())
        years.add(0, "")

        countries.clear()
        countries.addAll(employeeViewModel.employees.value.map { it.country }.distinct().sorted())
        countries.add(0, "")
    }

    private fun initSpinners() {
        adapterYear = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = adapterYear

        adapterCountry = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapterCountry
    }

    override fun onClick(employee: Employee) {
        employeeViewModel.deleteEmployee(employee)
    }
}
