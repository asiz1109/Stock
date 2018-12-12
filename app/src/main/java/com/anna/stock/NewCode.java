package com.anna.stock;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.UUID;

//Для layout-newcode. Добавление нового объекта в список
public class NewCode extends AppCompatActivity implements View.OnClickListener {

    TextView tv_code_new, tv_name_new, tv_place_new, tv_number_new;
    EditText et_code, et_name, et_place, et_number;
    Button b_add_new, b_find;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    //Для загрузки наименования с сайта
    public Elements content;
    String s, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_code_new = findViewById(R.id.tv_code_new);
        tv_name_new = findViewById(R.id.tv_name_new);
        tv_place_new = findViewById(R.id.tv_place_new);
        tv_number_new = findViewById(R.id.tv_number_new);
        et_code = findViewById(R.id.et_code);
        et_name = findViewById(R.id.et_name);
        et_place = findViewById(R.id.et_place);
        et_number = findViewById(R.id.et_number);
        b_add_new = findViewById(R.id.b_add_new);
        b_add_new.setOnClickListener(this);
        b_find = findViewById(R.id.b_find);
        b_find.setOnClickListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_add_new: //Добавление нового объекта в список
                Data d = new Data();
                d.setName(et_name.getText().toString());
                d.setCode(et_code.getText().toString());
                d.setPlace(et_place.getText().toString());
                d.setNumber(et_number.getText().toString());
                d.setUid(UUID.randomUUID().toString());
                mDatabaseReference.child("Data").child(d.getUid()).setValue(d);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.b_find: //Найти наименование на сайте
                code = et_code.getText().toString();
                new NewThread().execute();
                et_name.setText(s);
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

    //Загрузка наименования с сайта
    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document doc;
            try {
                doc = Jsoup.connect("https://leroymerlin.ru/catalogue/search/?token=16d6ac04bb3b9ef325ce28541c8d2495&q=" + code).get();
                content = doc.select(".product__hl");
                s = content.text().toString();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
