package com.anna.stock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Для layout-edit_data. Редактирование данных объекта из списка
public class EditData extends AppCompatActivity implements View.OnClickListener {

    TextView tv_code_edit_data, tv_name_edit_data, tv_place_edit_data;
    EditText et_code_edit_data, et_name_edit_data, et_place_edit_data;
    Button b_save;

    private Data mData;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data);

        // Кнопка назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_code_edit_data = findViewById(R.id.tv_code_edit_data);
        tv_name_edit_data = findViewById(R.id.tv_name_edit_data);
        tv_place_edit_data = findViewById(R.id.tv_place_edit_data);
        et_code_edit_data = findViewById(R.id.et_code_edit_data);
        et_name_edit_data = findViewById(R.id.et_name_edit_data);
        et_place_edit_data = findViewById(R.id.et_place_edit_data);
        b_save = findViewById(R.id.b_save);
        b_save.setOnClickListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        // Получить объект Data, переданный в Intent
        String data_code = getIntent().getStringExtra(Edit.EXTRA_DATA_CODE);
        mData = DataLab.get(this).getData(data_code);

        // Заполнить поля в activity
        et_code_edit_data.setText(mData.getCode());
        et_name_edit_data.setText(mData.getName());
        et_place_edit_data.setText(mData.getPlace());
    }

    //Сохранить изменения объекта
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_save:
                Data mData1 = new Data();
                mData1.setUid(mData.getUid());
                mData1.setName(et_name_edit_data.getText().toString());
                mData1.setCode(et_code_edit_data.getText().toString());
                mData1.setPlace(et_place_edit_data.getText().toString());
                mDatabaseReference.child("Data").child(mData1.getUid()).child("name").setValue(mData1.getName());
                mDatabaseReference.child("Data").child(mData1.getUid()).child("place").setValue(mData1.getPlace());
                mDatabaseReference.child("Data").child(mData1.getUid()).child("code").setValue(mData1.getCode());
                Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    // Кнопка назад
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
