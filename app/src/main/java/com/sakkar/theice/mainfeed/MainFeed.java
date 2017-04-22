package com.sakkar.theice.mainfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sakkar.theice.R;
import com.sakkar.theice.Utility.UserProfile;
import com.sakkar.theice.login.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;


@SuppressWarnings("ConstantConditions")
public class MainFeed extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        MyAdapter.Operation {

    RecyclerView resView;
    MyAdapter adapter;
    Toolbar actionBar;
    EditText editStatus;
    TextView postStatusButton;
    ArrayList<Posts> list;
    ArrayList<Long> postNos;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String userId;
    UserProfile userProfile;
    String start;
    ValueEventListener listener, listener2, listener3;
    DatabaseReference ref, newMessage;
    ImageView addImage;
    long n = 0;
    Posts post, posts;
    InputMethodManager manager;
    Calendar calendar;
    Uri uri = null;
    final static int imageRequestCode = 1101;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        if (savedInstanceState != null)
            return;

        resView = (RecyclerView) findViewById(R.id.resView);
        actionBar = (Toolbar) findViewById(R.id.appbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        showProgress(true);
        editStatus = (EditText) findViewById(R.id.editStatus);
        postStatusButton = (TextView) findViewById(R.id.postStatus);
        addImage = (ImageView) findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        setSupportActionBar(actionBar);

        sharedPreferences = getSharedPreferences("LOGGED_IN_STATE", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "13015604");
        firebaseDatabase = FirebaseDatabase.getInstance();

        list = new ArrayList<>();
        postNos = new ArrayList<>();
        adapter = new MyAdapter(list, getLayoutInflater(), getBaseContext());
        adapter.setOperation(this);
        resView.setLayoutManager(new LinearLayoutManager(this));


        start = firebaseDatabase.getReference().limitToFirst(1).getRef().getKey();
        postStatusButton.setOnClickListener(this);

        resView.setAdapter(adapter);

        ref = firebaseDatabase.getReference("groups/public");
        newMessage = firebaseDatabase.getReference("groups/zzz");
        loadProfile();
        storageReference = FirebaseStorage.getInstance().getReference("Posts");

        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        calendar = Calendar.getInstance();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, actionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void loadDataChanges() {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Posts.class);
                if (list.size() == 0 || (post.getPostNo() != list.get(0).getPostNo())) {
                    if (post != null) {
                        postNos.add(0, post.getPostNo());
                        list.add(0, post);
                    }
                } else {
                    if (post != null) {
                        list.remove(0);
                        list.add(0, post);
                    }
                }
                notifyAdapter();
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
        listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    post = ds.getValue(Posts.class);
                    if (post != null) {
                        if (!postNos.contains(post.getPostNo())) {
                            postNos.add(0, post.getPostNo());
                            list.add(0, post);
                        } else {
                            list.remove(postNos.indexOf(post.getPostNo()));
                            list.add(postNos.indexOf(post.getPostNo()), post);
                        }
                    }
                }
                showProgress(false);
                loadDataChanges();
                notifyAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        listener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Posts.class);
                if (post != null) {
                    list.remove(postNos.indexOf(post.getPostNo()));
                    list.add(postNos.indexOf(post.getPostNo()), post);
                }
                notifyAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        if (ref != null) {
            ref.orderByChild("postNo").limitToLast(6).addValueEventListener(listener2);
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
                if (editStatus.getText().toString().isEmpty() && uri == null) {
                    Toast.makeText(getApplicationContext(), "Nothisg To update", Toast.LENGTH_LONG).show();
                    break;
                }
                posts = new Posts(editStatus.getText().toString(), userProfile.getName(), 0, 0);
                posts.setDate(calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + "  " +
                        (calendar.get(Calendar.HOUR) < 10 ? "0" + calendar.get(Calendar.HOUR) : calendar.get(Calendar.HOUR)) + ":" +
                        (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)) + "" +
                        (calendar.get(Calendar.AM_PM) < 1 ? "AM" : "PM"));
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                firebaseDatabase.getReference("postNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        n = (long) dataSnapshot.getValue();
                        posts.setPostNo(n);
                        if (uri != null)
                            storageReference.child("post" + n).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    posts.setImageDownloadUri(taskSnapshot.getDownloadUrl().toString());
                                    if (list.size() != 0) {
                                        DatabaseReference dref = ref.child("post" + n);
                                        newMessage.setValue(posts);
                                        dref.setValue(list.get(0));
                                        dref.addValueEventListener(listener3);
                                        firebaseDatabase.getReference("postNo").setValue(n + 1);
                                    } else {
                                        newMessage.setValue(posts);
                                        firebaseDatabase.getReference("postNo").setValue(n + 1);
                                    }
                                    uri = null;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            });
                        else {
                            if (list.size() != 0) {
                                DatabaseReference dref = ref.child("post" + n);
                                newMessage.setValue(posts);
                                dref.setValue(list.get(0));
                                dref.addValueEventListener(listener3);
                                firebaseDatabase.getReference("postNo").setValue(n + 1);
                            } else {
                                newMessage.setValue(posts);
                                firebaseDatabase.getReference("postNo").setValue(n + 1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Post is rejectef", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
                addImage.setImageResource(R.drawable.ic_add_black_24dp);
                editStatus.setText("");
                break;
            case R.id.addImage:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, imageRequestCode);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == imageRequestCode && resultCode == RESULT_OK) {
            uri = data.getData();
            addImage.setImageURI(uri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newMessage.removeEventListener(listener);
        ref.orderByChild("postNo").limitToLast(postNos.size() - 1).removeEventListener(listener2);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void like(int pos) {
        Posts p = list.get(pos);
        String postNo = "post" + (p.getPostNo() + 1);
        if (!p.likers.contains(userProfile.getName())) {
            if (pos == 0) {
                p.updateLikes(1);
                p.addLikers(userProfile.getName());
                newMessage.setValue(p);
            } else {
                p.updateLikes(1);
                p.addLikers(userProfile.getName());
                ref.child(postNo).setValue(p);
            }
        } else {
            if (pos == 0) {
                p.updateLikes(-1);
                p.removeLikers(userProfile.getName());
                newMessage.setValue(p);
            } else {
                p.updateLikes(-1);
                p.removeLikers(userProfile.getName());
                ref.child(postNo).setValue(p);
            }
        }
    }

    @Override
    public void unlike(final int pos) {
        Posts p = list.get(pos);
        String postNo = "post" + (p.getPostNo() + 1);
        if (!p.unlikers.contains(userProfile.getName())) {
            if (pos == 0) {
                p.updateUnlikes(1);
                p.addUnlikers(userProfile.getName());
                newMessage.setValue(p);
            } else {
                p.updateUnlikes(1);
                p.addUnlikers(userProfile.getName());
                ref.child(postNo).setValue(p);
            }
        } else {
            if (pos == 0) {
                p.updateUnlikes(-1);
                p.removeUnikers(userProfile.getName());
                newMessage.setValue(p);
            } else {
                p.updateUnlikes(-1);
                p.removeUnikers(userProfile.getName());
                ref.child(postNo).setValue(p);
            }
        }
    }
}
