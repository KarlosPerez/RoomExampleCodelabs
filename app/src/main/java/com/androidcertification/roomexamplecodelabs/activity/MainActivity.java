package com.androidcertification.roomexamplecodelabs.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.androidcertification.roomexamplecodelabs.R;
import com.androidcertification.roomexamplecodelabs.adapter.WordListAdapter;
import com.androidcertification.roomexamplecodelabs.database.model.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private WordViewModel mWordViewModel;
    private RecyclerView recyclerView;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setupWordList();
    }

    private void setupWordList() {
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Update the cached copy of the words in the adapter.
        // Refactor to lambda method reference
        mWordViewModel.getAllWords().observe(this, adapter::setWords);
    }

    private void initComponents() {
        recyclerView = findViewById(R.id.recyclerview);
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> addNewWord());
    }

    private void addNewWord() {
        Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
