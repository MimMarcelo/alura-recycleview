package com.mimmarcelo.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mimmarcelo.ceep.M;
import com.mimmarcelo.ceep.R;
import com.mimmarcelo.ceep.dao.NotesDao;
import com.mimmarcelo.ceep.model.Note;
import com.mimmarcelo.ceep.ui.adapter.NotesAdapter;
import com.mimmarcelo.ceep.ui.listener.OnItemClickListener;

public class NotesListActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    NotesDao dao;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        dao = new NotesDao();
        dao.load(
                new Note("Teste", "Testando..."),
                new Note("Coração", "Mon Amour!")
        );

        RecyclerView recyclerView = findViewById(R.id.rcv_notes_list);
        adapter = new NotesAdapter(this, dao.all());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        TextView txt = findViewById(R.id.txt_insert_new_note);
        txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, NoteFormActivity.class), M.request.create_note);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note note;
        switch (requestCode){
            case M.request.create_note:
                if (!(resultCode == RESULT_OK)) {
                    Toast.makeText(this, "It was not possible to create the new note", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(data.hasExtra(M.extra.note_obj))) {
                    Toast.makeText(this, "It was not possible to create the new note", Toast.LENGTH_SHORT).show();
                    return;
                }
                note = (Note) data.getSerializableExtra(M.extra.note_obj);
                dao.add(note);
                adapter.add(note);
                break;
            case M.request.update_note:
                if (!(resultCode == RESULT_OK)) {
                    Toast.makeText(this, "It was not possible to create the new note", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(data.hasExtra(M.extra.note_obj))) {
                    Toast.makeText(this, "It was not possible to create the new note", Toast.LENGTH_SHORT).show();
                    return;
                }
                note = (Note) data.getSerializableExtra(M.extra.note_obj);
                int position = data.getIntExtra(M.extra.position, -1);
                dao.update(position, note);
                adapter.update(position, note);
                break;
        }

    }

    @Override
    public void onItemClick(int position) {
        Note note = adapter.getItem(position);

        Intent intent = new Intent(this, NoteFormActivity.class);
        intent.putExtra(M.extra.note_obj, note);
        intent.putExtra(M.extra.position, position);
        startActivityForResult(intent, M.request.update_note);
    }
}
