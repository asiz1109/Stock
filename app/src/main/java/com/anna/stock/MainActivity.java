package com.anna.stock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Для layout-main. Загружает список из Database и показывает его, поиск по списку
public class MainActivity extends AppCompatActivity {

    ListView lv_data;
    SearchView sv_search;
    private ArrayList <Data> mData;
    DataAdapter adapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    Data selectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sv_search = findViewById(R.id.sv_search);
        lv_data = findViewById(R.id.lv_data);

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mData = new ArrayList<>();
        addEventFirebaseListener();

        //Нажатие на позицию в списке
        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedData = (Data)parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, Edit.class);
                intent.putExtra(Edit.EXTRA_DATA_CODE, selectedData.getCode());
                startActivity(intent);
            }
        });

        //Поиск
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Data> newDataList = new ArrayList<>();
                for (Data d : mData){
                    if (d.getCode().toLowerCase().contains(newText.toLowerCase())){
                        newDataList.add(d);
                    } else if (d.getName().toLowerCase().contains(newText.toLowerCase())){
                        newDataList.add(d);
                    } else if (d.getPlace().toLowerCase().contains(newText.toLowerCase())){
                        newDataList.add(d);
                    }
                }
                DataAdapter adapter = new DataAdapter(newDataList);
                lv_data.setAdapter(adapter);
                return true;
            }
        });

    }

    //Загрузка списка из Database
    private void addEventFirebaseListener(){
        mDatabaseReference.child("Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mData.size() > 0) mData.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Data d = postSnapshot.getValue(Data.class);
                    mData.add(d);
                    DataLab.get(MainActivity.this).setDataList(mData);
                }
                adapter = new DataAdapter(mData);
                lv_data.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Адаптер для списка
    private class DataAdapter extends ArrayAdapter<Data>{
        public DataAdapter (ArrayList<Data> data){
            super(MainActivity.this, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_data, null);
            }
            Data d = getItem(position);
            TextView data_list_item_name = convertView.findViewById(R.id.tv_data_list_item_name);
            data_list_item_name.setText(d.getName());
            TextView data_list_item_code = convertView.findViewById(R.id.tv_data_list_item_code);
            String code = "артикул " + d.getCode();
            data_list_item_code.setText(code);
            TextView data_list_item_place = convertView.findViewById(R.id.tv_data_list_item_place);
            String place = "место " + d.getPlace();
            data_list_item_place.setText(place);
            TextView data_list_item_number = convertView.findViewById(R.id.tv_data_list_item_number);
            String number = "количество " + d.getNumber();
            data_list_item_number.setText(number);
            return convertView;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Кнопка добавления новой позиции в список
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_data:
                Intent intent = new Intent(this, NewCode.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
