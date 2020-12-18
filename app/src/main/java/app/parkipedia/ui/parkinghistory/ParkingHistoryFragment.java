package app.parkipedia.ui.parkinghistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.parkipedia.HomePage;
import app.parkipedia.R;
import app.parkipedia.SingleTask;
import app.parkipedia.adapter.ParkingHistoryAdapter;
import app.parkipedia.model.Book;
import app.parkipedia.model.Profile;

public class ParkingHistoryFragment extends Fragment {
    private Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profile = ((HomePage) getActivity()).getProfileData();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parking_history, container, false);
    }

    private List<Book> bookList;
    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.parkinghistory_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ((SingleTask) getActivity().getApplication()).getBookingDatabaseReference()
                .child(profile.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList = new ArrayList<>();
                if (snapshot.getChildrenCount() > 0) {
                    Iterator<DataSnapshot> it = snapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot ds = it.next();
                        Book book = ds.getValue(Book.class);
                        bookList.add(book);
                    }
                    recyclerView.setAdapter(new ParkingHistoryAdapter(bookList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
