package com.androidstudiolearning.firstandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonAdd, buttonViewAll;
    EditText editTextName, editTextAge;
    Switch switchActiveCustomer;
    ListView listViewAllCustomers;
    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.btn_add);
        buttonViewAll = findViewById(R.id.btn_viewAll);
        editTextAge = findViewById(R.id.et_age);
        editTextName = findViewById(R.id.et_name);
        switchActiveCustomer = findViewById(R.id.sw_active);
        listViewAllCustomers = findViewById(R.id.lv_customerList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowCustomersOnListView(dataBaseHelper);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(-1, editTextName.getText().toString(), Integer.parseInt(editTextAge.getText().toString()), switchActiveCustomer.isChecked());
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Wrong input data. Try again. ", Toast.LENGTH_LONG).show();
                    customerModel = new CustomerModel(-1, "error", 0, false);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success = dataBaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this, "Success: " + success, Toast.LENGTH_LONG).show();

                editTextAge.setText("");
                editTextName.setText("");

                ShowCustomersOnListView(dataBaseHelper);
            }
        });

        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                ShowCustomersOnListView(dataBaseHelper);
            }
        });

        listViewAllCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel customerModel = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOneCustomer(customerModel);
                ShowCustomersOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted: " + customerModel.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAllCustomers());
        listViewAllCustomers.setAdapter(customerArrayAdapter);
    }
}