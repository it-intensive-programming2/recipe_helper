package com.example.recipe_helper.Commnuity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devs.readmoreoption.ReadMoreOption;
import com.example.recipe_helper.Commnuity.Adapter.PostAdapter;
import com.example.recipe_helper.Commnuity.DataFrame.Comments;
import com.example.recipe_helper.Commnuity.DataFrame.Post;
import com.example.recipe_helper.Home.Dataframe.HomeRecipeFrame;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.Mypage.MyPage;
import com.example.recipe_helper.R;


import java.util.ArrayList;

public class Community_main extends Fragment implements PostAdapter.OnListItemSelectedInterface {

    private ArrayList<Post> post_list = new ArrayList<>();
    private PostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_commnunity, container, false);
        view.setClickable(true);

        String BaseUrl = "https://www.10000recipe.com/recipe/";
        HomeRecipeFrame frame1 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2020/08/28/c37e0db0e0386d453eda2be45e26b9c01.jpg", "닭봉간장조림~ 십년째 만들어 먹는 양념 공유해요!", BaseUrl + "6940325");
        HomeRecipeFrame frame2 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2020/10/29/89b11b0ce7f7e177f3f449546dc4edc11.jpg", "여러가지 매력을 가진 치즈감자호떡", BaseUrl + "6945267");

        post_list.add(new Post(frame1.recipe_name, frame1.img_url, "Johnson", "It's good time to go for dinner Hey let's go out now nonono I have to do more develope this app", null, null));
        post_list.add(new Post(frame2.recipe_name, frame2.img_url, "Kyung-Sik Han", "으워워우어ㅜ어웅ㅁ니니ㅣ앙으워워우어ㅜ어웅ㅁ니니ㅣ앙으워워우어ㅜ어웅ㅁ니니ㅣ앙으워워우어ㅜ어웅ㅁ니니ㅣ앙", null, null));

        adapter = new PostAdapter(getContext(), post_list, this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.community_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        return view;

    }


    @SuppressLint("ResourceType")
    @Override
    public void onItemSelected(View v, String id, String img, String content) {
        ((MainActivity) getActivity()).replaceFragmentFull(Community_comment.newInstance(id, img, content));
    }
}
