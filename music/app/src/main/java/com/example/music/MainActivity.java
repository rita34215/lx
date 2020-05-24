package com.example.music;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue;
    private ContactAdapter contactAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("流行音乐排行榜");


        queue = Volley.newRequestQueue(this);

        swipeRefreshLayout = findViewById(R.id.sw);
        RecyclerView recyclerView = findViewById(R.id.listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);设置为横向水平滚动，默认是垂直
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        contactAdapter = new ContactAdapter(new ArrayList<MusicItem>());
        recyclerView.setAdapter(contactAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                reqListMoblie();
            }
        });
        reqListMoblie();
        //swipeRefreshLayout.setRefreshing(true);
    }

    private void reqListMoblie(){
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://www.cjlly.com:3041/record",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "list_mobileOK"+response);

                        Gson gson = new Gson();
                        Type type = new TypeToken<List<MusicItem>>() {}.getType();
                        List<MusicItem> list = gson.fromJson(response, type);

                        if(list != null){
                            contactAdapter.getmContactList().clear();
                            contactAdapter.getmContactList().addAll(list);
                            contactAdapter.notifyDataSetChanged();
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("TAG", "list_mobileERR:"+error.getMessage(), error);
            }


        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }


    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
        private List<MusicItem> mContactList;

        class ViewHolder extends RecyclerView.ViewHolder {
            View contactView;//存储解析到的view
            TextView list_name,list_id;
            TextView shanchu;
            ImageView list_iv;

            public ViewHolder(View view) {
                super(view);
                contactView = view;
                list_id = view.findViewById(R.id.list_id);
                list_iv = view.findViewById(R.id.list_iv);
                list_name = view.findViewById(R.id.list_name);
                shanchu = view.findViewById(R.id.shanchu);
            }
        }

        public List<MusicItem> getmContactList() {
            return mContactList;
        }

        public void setmContactList(List<MusicItem> mContactList) {
            this.mContactList = mContactList;
        }

        public ContactAdapter(List<MusicItem> contactList){
            mContactList = contactList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_moblie,parent,false);//解析layout
            final ViewHolder viewHolder = new ViewHolder(view);//新建一个viewHolder绑定解析到的view
            //监听每一项里的控件的点击事件，如点击了ImageView
            viewHolder.shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = viewHolder.getAdapterPosition();
                    MusicItem contact = mContactList.get(position);

                    List<MusicItem> myContacts = contactAdapter.getmContactList();
                    myContacts.remove(position);
                    contactAdapter.notifyDataSetChanged();
                    //Toast.makeText(view.getContext(),contact.getId(),Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.contactView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    MusicItem musicItem = contactAdapter.getmContactList().get(position);
                    Intent intent = new Intent(MainActivity.this, MusicDeat.class);
                    intent.putExtra("xiangqing",musicItem);
                    startActivity(intent);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final MusicItem contact = mContactList.get(position);
            holder.list_id.setText(""+contact.getId());
            Glide.with(MainActivity.this).load(contact.getImg()).into(holder.list_iv);
            holder.list_name.setText(contact.getName());


        }

        @Override
        public int getItemCount() {
            return mContactList.size();
        }

    }


}
