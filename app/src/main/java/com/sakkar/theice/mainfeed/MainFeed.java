package com.sakkar.theice.mainfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkar.theice.R;
import com.sakkar.theice.Utility.UserProfile;
import com.sakkar.theice.login.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;


@SuppressWarnings("ConstantConditions")
public class MainFeed extends AppCompatActivity implements View.OnClickListener {

    RecyclerView resView;
    MyAdapter adapter;
    Toolbar actionBar;
    EditText editStatus;
    TextView postStatusButton;
    ArrayList<Posts> list;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String userId;
    UserProfile userProfile;
    String start;
    ValueEventListener listener;
    DatabaseReference ref,newMessage;
    long n=0;
    Posts post,posts;
    InputMethodManager manager;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        /*if(savedInstanceState!=null)
            return;*/

        resView = (RecyclerView) findViewById(R.id.resView);
        actionBar = (Toolbar) findViewById(R.id.appbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        showProgress(true);
        editStatus = (EditText) findViewById(R.id.editStatus);
        postStatusButton = (TextView) findViewById(R.id.postStatus);
        setSupportActionBar(actionBar);

        sharedPreferences = getSharedPreferences("LOGGED_IN_STATE", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "13015604");
        firebaseDatabase = FirebaseDatabase.getInstance();

        list = new ArrayList<>();
        adapter = new MyAdapter(list, getLayoutInflater());
        resView.setLayoutManager(new LinearLayoutManager(this));


        start = firebaseDatabase.getReference().limitToFirst(1).getRef().getKey();
        postStatusButton.setOnClickListener(this);

        resView.setAdapter(adapter);

        ref = firebaseDatabase.getReference("groups/public");
        newMessage=firebaseDatabase.getReference("groups/zzz");
        loadProfile();


        manager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        calendar=Calendar.getInstance();
    }

    private void loadDataChanges() {
        listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post=dataSnapshot.getValue(Posts.class);
                if(post!=null) {
                    list.add(0, post);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        newMessage.addValueEventListener(listener);
    }

    private void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    private void loadProfile() {
        DatabaseReference reference = firebaseDatabase.getReference("userprofile").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                loadData(start);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadData(String starter) {
        if (ref != null) {
            ref.orderByChild("postNo").limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        post=ds.getValue(Posts.class);
                        if(post!=null) {
                            list.add(0,post);
                            notifyAdapter();
                        }
                        //Log.e("sakkar","adder");
                    }
                    showProgress(false);
                    loadDataChanges();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showProgress(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
        resView.setVisibility(state ? View.GONE : View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postStatus:
                posts = new Posts(editStatus.getText().toString(), userProfile.getName(), 0, 0);
                posts.setDate(calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR)+"  "+
                        (calendar.get(Calendar.HOUR)<10?"0"+calendar.get(Calendar.HOUR):calendar.get(Calendar.HOUR))+":"+
                        (calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE))+""+
                        (calendar.get(Calendar.AM_PM)<1?"AM":"PM"));
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                firebaseDatabase.getReference("postNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        n=(long) dataSnapshot.getValue();
                        posts.setPostNo(n);
                        if(list.size()!=0) {
                            DatabaseReference dref = ref.child("post" + n);
                            newMessage.setValue(posts);
                            dref.setValue(list.get(0));
                            firebaseDatabase.getReference("postNo").setValue(n+1);
                        }else {
                            newMessage.setValue(posts);
                            firebaseDatabase.getReference("postNo").setValue(n+1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Post is rejectef",Toast.LENGTH_LONG).show();
                        return;
                    }
                });
                editStatus.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.over_flow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newMessage.removeEventListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
