package ru.timerchat.bnet_test_tkh1511.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.timerchat.bnet_test_tkh1511.Utils;
import ru.timerchat.bnet_test_tkh1511.activity.NoteListActivity;
import ru.timerchat.bnet_test_tkh1511.model.ModelNote;
import ru.timerchat.bnet_test_tkh1511.R;

/**
 * Created by Timur on 22.03.16.
 */
public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    List<ModelNote> notes;

    NoteListActivity noteListActivity;

    public NoteListAdapter(NoteListActivity noteListActivity) {
        this.noteListActivity = noteListActivity;
        notes = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_note, viewGroup, false);
        TextView da = (TextView) v.findViewById(R.id.tvNoteDateCreation_model);
        TextView dm = (TextView) v.findViewById(R.id.tvNoteDateModify_model);
        TextView body = (TextView) v.findViewById(R.id.tvNoteBody200simbols);


        return new NoteViewHolder(v, da, dm, body);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ModelNote note = notes.get(position);


        viewHolder.itemView.setEnabled(true);
        final ModelNote modelNote = (ModelNote) note;
        final NoteViewHolder noteViewHolder = (NoteViewHolder) viewHolder;

        final View itemView = noteViewHolder.itemView;
        final Resources resources = itemView.getResources();

        itemView.setVisibility(View.VISIBLE);

        itemView.setBackgroundColor(resources.getColor(R.color.gray_50));

        noteViewHolder.da.setText(Utils.getFullDate(modelNote.getDa()));

        if(modelNote.getDa()!=modelNote.getDm()) {
            noteViewHolder.dm.setText(Utils.getFullDate(modelNote.getDm()));
        }
        String fullText = modelNote.getBody();//выгружаем боди
        String text = fullText.substring(0, Math.min(fullText.length(), 200));//обрезаем боди до 200 символов
        noteViewHolder.body.setText(text);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getNoteListActivity().showNoteDialog(modelNote);
            }
        });


    }

    public void addNote(ModelNote modelNote) {
        notes.add(getItemCount(), modelNote);
        notifyItemInserted(getItemCount());
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }


    protected class NoteViewHolder extends RecyclerView.ViewHolder {
        protected TextView da;
        protected TextView dm;
        protected TextView body;

        public NoteViewHolder(View itemView, TextView da, TextView dm, TextView body) {
            super(itemView);
            this.da = da;
            this.dm = dm;
            this.body = body;
        }
    }


    public NoteListActivity getNoteListActivity() {
        return noteListActivity;
    }

}
