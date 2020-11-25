package com.example.recipe_helper.Commnuity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipe_helper.Commnuity.Adapter.PostAdapter;
import com.example.recipe_helper.Commnuity.DataFrame.Post;
import com.example.recipe_helper.Commnuity.DataFrame.PostResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import retrofit2.Call;

public class Community_main extends Fragment implements PostAdapter.OnListItemSelectedInterface {

    private static final String TAG = "Community_main";
    private ArrayList<Post> post_list = new ArrayList<>();
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_commnunity, container, false);
        view.setClickable(true);

        adapter = new PostAdapter(getContext(), post_list, this, this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.community_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentAddBackStack(new Create_post());
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.turquoise);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPost();
            }
        });


        return view;

    }

    public void loadPost() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<PostResponse> call = service.loadPost();

        call.enqueue(new retrofit2.Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, retrofit2.Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse result = response.body();
                    if (result.checkError(getContext()) != 0) {
                        Log.d(TAG, "ERROR");
                    }
                    //Todo
                    ArrayList<Post> results = result.body;
                    post_list.clear();
                    post_list.addAll(results);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast toast = Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 30, 15);
                toast.show();
            }
        });
    }

    @Override
    public void onItemSelected(View v, String id, String img, String content, int postID) {
        ((MainActivity) getActivity()).replaceFragmentFull(Community_comment.newInstance(id, img, content, postID));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPost();
    }
}
