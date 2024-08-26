package com.example.ghtk_flow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghtk_flow.models.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel() : ViewModel() {
    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees

    private val _search = MutableStateFlow<String>("")
    val search: StateFlow<String> = _search

    private val _employeesSearch = MutableStateFlow<List<Employee>>(emptyList())
    val employeesSearch: StateFlow<List<Employee>> = _employeesSearch

    fun fetchEmployees() {
        viewModelScope.launch {
            val list = mutableListOf<Employee>()
            for (i in 1..15){
                list.add(Employee("Bui Duc Luong ${i}",2003 + (i%5),"Ha Noi ${(i%5)}"))
            }
            _employees.value = list
        }
    }
    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            _employees.value = _employees.value + employee
        }
    }
    fun filterEmployeesByYobAndCountry(yob: String , country : String): List<Employee> {
        if(yob.equals("") && country.equals("")){
            return _employees.value
        }
        else if(yob.equals("")){
            return _employees.value.filter { it.country.equals(country) }
        }
        else if(country.equals("")){
            return _employees.value.filter { it.yob.toString().equals(yob) }
        }
        else{
            return _employees.value.filter { it.yob.toString().equals(yob) && it.country.equals(country) }
        }
    }
    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            _employees.value = _employees.value - employee
        }
    }
    fun searchByName(name:String) : List<Employee>{
        return _employees.value.filter { it.name.contains(name) }
    }
}