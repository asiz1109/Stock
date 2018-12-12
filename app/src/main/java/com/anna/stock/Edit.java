package com.anna.stock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Для layout-edit. Показывает информацию об объекте, выбранном в списке, направляет в другие layout для изменения данных
public class Edit extends AppCompatActivity implements View.OnClickListener {

    TextView tv_code,tv_name, tv_place,tv_number;
    Button b_add, b_remove, b_edit_data, b_delete_data;

    public static final String EXTRA_DATA_CODE = "com.anna.stock.code"; //Для передачи extra в Intent

    public Data mData;
    AlertDialog.Builder ad;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        // Кнопка назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        b_add = findViewById(R.id.b_add);
        b_add.setOnClickListener(this);
        b_remove = findViewById(R.id.b_remove);
        b_remove.setOnClickListener(this);
        tv_code = findViewById(R.id.tv_code);
        tv_name = findViewById(R.id.tv_name);
        tv_place = findViewById(R.id.tv_place);
        tv_number = findViewById(R.id.tv_number);
        b_edit_data = findViewById(R.id.b_edit_data);
        b_edit_data.setOnClickListener(this);
        b_delete_data = findViewById(R.id.b_delete_data);
        b_delete_data.setOnClickListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        // Получить объект Data, переданный в Intent
        String data_code = getIntent().getStringExtra(EXTRA_DATA_CODE);
        mData = DataLab.get(this).getData(data_code);

        // Заполнить поля в activity
        String code = "АРТИКУЛ   " + mData.getCode();
        tv_code.setText(code);
        String name = "НАИМЕНОВАНИЕ   " + mData.getName();
        tv_name.setText(name);
        String place = "МЕСТО   " + mData.getPlace();
        tv_place.setText(place);
        String number = "КОЛИЧЕСТВО   " + mData.getNumber();
        tv_number.setText(number);

        // Удаление объекта из списка с подтверждением
        ad = new AlertDialog.Builder(this);
        ad.setTitle("Удалить элемент списка");
        ad.setMessage("Вы уверены?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mDatabaseReference.child("Data").child(mData.getUid()).removeValue();
                Toast.makeText(Edit.this, "Удалено", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Edit.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_add: //Добавить количество
                Intent intent = new Intent(this, Ok.class);
                intent.putExtra(Edit.EXTRA_DATA_CODE, mData.getCode());
                intent.putExtra("action", Ok.ADD);
                startActivity(intent);
                break;
            case R.id.b_remove: //Убрать количество
                Intent intent1 = new Intent(this, Ok.class);
                intent1.putExtra(Edit.EXTRA_DATA_CODE, mData.getCode());
                intent1.putExtra("action", Ok.REMOVE);
                startActivity(intent1);
                break;
            case R.id.b_edit_data: //Редактировать данные
                Intent intent3 = new Intent(this, EditData.class);
                intent3.putExtra(Edit.EXTRA_DATA_CODE, mData.getCode());
                startActivity(intent3);
                break;
            case R.id.b_delete_data: //Удалить объект
                ad.show();
                break;
        }
    }

    // Кнопка назад
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
