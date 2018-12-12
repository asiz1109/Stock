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

//Для layout-ok. Изменение количества товара
public class Ok extends AppCompatActivity implements View.OnClickListener {

    TextView tv_edit_number;
    EditText et_edit_number;
    Button b_ok;

    private Data mData;

    //Передаётся в extra
    public static final String ADD = "1";
    public static final String REMOVE = "2";
    String action;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        b_ok = findViewById(R.id.b_ok);
        b_ok.setOnClickListener(this);
        tv_edit_number = findViewById(R.id.tv_edit_number);
        et_edit_number = findViewById(R.id.et_edit_number);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        // Получить объект Data, переданный в Intent, и как изменить его количество
        String data_code = getIntent().getStringExtra(Edit.EXTRA_DATA_CODE);
        mData = DataLab.get(this).getData(data_code);
        action = getIntent().getStringExtra("action");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_ok: //Изменить количество
                switch (action) {
                    case ADD: //Добавить количество
                        String number = "" + (Integer.parseInt(mData.getNumber()) + Integer.parseInt(et_edit_number.getText().toString()));
                        mDatabaseReference.child("Data").child(mData.getUid()).child("number").setValue(number);
                        break;
                    case REMOVE: //Убрать количество
                        int number1 = Integer.parseInt(mData.getNumber()) - Integer.parseInt(et_edit_number.getText().toString());
                        if (number1 < 0) number1 = 0;
                        String s_number1 = "" + number1;
                        mDatabaseReference.child("Data").child(mData.getUid()).child("number").setValue(s_number1);
                        break;
                }
                Toast.makeText(this, "Количество изменено", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    //Кнопка назад
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}

