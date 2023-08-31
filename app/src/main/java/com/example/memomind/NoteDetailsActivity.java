package com.example.memomind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleText, contentText;
    ImageView saveNoteButton;

    TextView pageTitle;
    String title, content, docId;
    boolean isEditMode = false;
    ImageView deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        pageTitle = findViewById(R.id.pageTitle);

        deleteBtn = findViewById(R.id.deleteButtom);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null){
            isEditMode = true;
        }
        titleText.setText(title);
        contentText.setText(content);

        if (isEditMode){
            pageTitle.setText("Edit your note");
            deleteBtn.setVisibility(View.VISIBLE);
        }

        saveNoteButton.setOnClickListener(v -> saveNote());
        deleteBtn.setOnClickListener( v -> deleteNoteFirebase());

    }



    private void saveNote() {

        String noteTitle = titleText.getText().toString();
        String noteContent = contentText.getText().toString();
        if (noteTitle.isEmpty()){
            titleText.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteFirebase(note);
    }
    void saveNoteFirebase(Note note) {

        DocumentReference documentReference;

        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        } else {

            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    //note is added
                    Toast.makeText(NoteDetailsActivity.this, "Note added Successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NoteDetailsActivity.this, "Failed to add note, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        private void deleteNoteFirebase() {
            DocumentReference documentReference;


                documentReference = Utility.getCollectionReferenceForNotes().document(docId);

            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        //note is added
                        Toast.makeText(NoteDetailsActivity.this, "Note deleted Successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NoteDetailsActivity.this, "Failed to delete note, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


}


















