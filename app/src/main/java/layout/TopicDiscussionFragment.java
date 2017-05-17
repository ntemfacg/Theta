package layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntemfacg.theta.Discussion_list;
import com.example.ntemfacg.theta.HomeActivity;
import com.example.ntemfacg.theta.Operations;
import com.example.ntemfacg.theta.R;
import com.example.ntemfacg.theta.SaveSettings;
import com.example.ntemfacg.theta.SearchType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

//import static layout.GroupsFragment.Dynamic_id;
public class TopicDiscussionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    int StartFrom=0;
    int UserOperation=SearchType.MyFollowing; // 0 my followers post 2- specifc user post 3- search post



    private String mParam2;
    ArrayList<Discussion_list> listnewsData = new ArrayList<Discussion_list>();
    MyCustomAdapter myadapter;
    public String val;
    private String plusTitle;

   // private OnFragmentInteractionListener mListener;

    public TopicDiscussionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String id = getArguments().getString("interest_id");
        val = id;
        String title = getArguments().getString("group");
        plusTitle = title;
        getActivity().setTitle(plusTitle);
        View view = inflater.inflate(R.layout.fragment_topic_discussion, container, false);
        myadapter = new MyCustomAdapter(getActivity().getApplicationContext(), listnewsData);
        ListView listView = (ListView) view.findViewById(R.id.interest_discussions);
        listView.setAdapter(myadapter);
        LoadDiscussions(0, SearchType.OneTopic);
        return view;
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_topic_discussion, container, false);
    }

    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<Discussion_list> listnewsDataAdpater ;
        Context context;
        public MyCustomAdapter(Context context, ArrayList<Discussion_list> listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
            this.context=context;
        }

        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            final   Discussion_list s = listnewsDataAdpater.get(position);

            if(s.discussion_date.equals("add")) {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.activity_new_query, null);

                final EditText etPost = (EditText) myView.findViewById(R.id.inputNote);
                Button iv_post=(Button) myView.findViewById(R.id.buttonNoteDone) ;

                ImageView iv_attach=(ImageView) myView.findViewById(R.id.attach) ;
                iv_attach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoadImage();
                    }
                });
                iv_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String discussions=null;
                        try {
                            //for space with name
                            discussions = java.net.URLEncoder.encode(etPost.getText().toString() , "UTF-8");

                            if (downloadUrl == null){
                                downloadUrl="empty.jpg";
                                downloadUrl= java.net.URLEncoder.encode(downloadUrl , "UTF-8");
                            }

                            else{
                                downloadUrl= java.net.URLEncoder.encode(downloadUrl , "UTF-8");
                            }

                        } catch (UnsupportedEncodingException e) {
                            discussions=".";
                        }
                        /*if (downloadUrl == null){
                            downloadUrl="empty.jpg";
                            return;
                        }*/
                        String url="http://192.168.0.6/tHETASERVER/add_discussion.php?user_id="+ SaveSettings.UserID +"&topic_id="+val+"&discussion_text="+ discussions +"&discussion_picture="+ downloadUrl;
                        etPost.setText("");
                        new MyAsyncTaskgetNews().execute(url);
                    }
                });
                return myView;
            }
            else if(s.discussion_date.equals("loading")) {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.discussions_loading, null);
                return myView;
            }
            else if(s.discussion_date.equals("nodiscussions")) {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.no_new_discussion, null);
                return myView;
            }

            else {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.discussion_item, null);
                TextView txtUserName = (TextView) myView.findViewById(R.id.txtUserName);
                txtUserName.setText(s.first_name);
                txtUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        SelectedUserID=Integer.parseInt(s.user_id);
                        LoadTweets(0,SearchType.OnePerson);
                        txtnamefollowers.setText(s.first_name);
                        String url="http://10.0.2.2/~hussienalrubaye/twitterserver/isfollowing.php?user_id="+SaveSettings.UserID +"&following_user_id="+SelectedUserID;
                        new MyAsyncTaskgetNews().execute(url);*/
                    }
                });
                TextView txt_discussion = (TextView) myView.findViewById(R.id.txt_discussion);
                txt_discussion.setText(s.discussion_text);
                TextView txt_discussion_date = (TextView) myView.findViewById(R.id.txt_discussion_date);
                txt_discussion_date.setText(s.discussion_date);
                ImageView discussion_picture=(ImageView)myView.findViewById(R.id.discussion_picture);
                Picasso.with(context).load(s.discussion_picture).into(discussion_picture);
                ImageView picture_path=(ImageView)myView.findViewById(R.id.picture_path);
                Picasso.with(context).load(s.picture_path).into(picture_path);
                TextView topicTitle = (TextView) myView.findViewById(R.id.topic_title);
                topicTitle.setText(s.topic_name);
                return myView;
            }
        }
        //load image

    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //save image
    int RESULT_LOAD_IMAGE=233;
    void LoadImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor =getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // postImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            uploadimage( BitmapFactory.decodeFile(picturePath));
        }
    }
    String downloadUrl=null;
    // ImageView postImage = new ImageView(this);

    public void uploadimage(Bitmap bitmap ) {
        showProgressDialog();
        FirebaseStorage storage= FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://theta-app-2368b.appspot.com");
        DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
        Date dateobj = new Date();
        // System.out.println(df.format(dateobj));

// Create a reference to "mountains.jpg"
        String mydownloadUrl=SaveSettings.UserID+ "_"+ df.format(dateobj) +".jpg";
        StorageReference mountainsRef = storageRef.child("images/"+ mydownloadUrl);
        // postImage.setDrawingCacheEnabled(true);

        // postImage.buildDrawingCache();

        // Bitmap bitmap = imageView.getDrawingCache();

        // BitmapDrawable drawable=(BitmapDrawable)postImage.getDrawable();

        //  Bitmap bitmap =drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                downloadUrl = taskSnapshot.getDownloadUrl().toString();
                hideProgressDialog();
            }
        });
    }

    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }

        @Override
        protected String  doInBackground(String... params) {

            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    Operations operations=new Operations(getActivity().getApplicationContext());
                    NewsData = operations.ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);

                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            try {
                JSONObject json= new JSONObject(progress[0]);
                //display response data
                //Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_LONG).show();
                if (json.getString("msg")==null)
                    return;

                if (json.getString("msg").equalsIgnoreCase("discussion added")) {
                    LoadDiscussions(0,SearchType.OneTopic);

                }
                else if (json.getString("msg").equalsIgnoreCase("discussions")) {
                    if(StartFrom==0) {
                        listnewsData.clear();
                        listnewsData.add(new Discussion_list(null, null, null, "add", null, null, null, null, null, null));
                        //Toast.makeText(getApplicationContext(), Dynamic_id, Toast.LENGTH_LONG).show();
                        /*if (Dynamic_id != 0){
                            listnewsData.add(new Discussion_list(null, null, null, "add", null, null, null, null, null, null));
                            //Toast.makeText(getActivity(), Dynamic_id, Toast.LENGTH_LONG).show();

                        }*/
                        //listnewsData.add(new Discussion_list(null, null, null, "add", null, null, null, null, null, null));
                    }

                    else {
                        //remove we are loading now
                        listnewsData.remove(listnewsData.size()-1);
                    }
                    JSONArray discssions=new JSONArray( json.getString("info"));
                    //Toast.makeText(getApplicationContext(), Dynamic_id, Toast.LENGTH_LONG).show();

                    for (int i = 0; i <discssions.length() ; i++) {
                        // try to add the resourcess
                        JSONObject js=discssions.getJSONObject(i);

                        //add data and view it
                        listnewsData.add(new Discussion_list(js.getString("discussion_id"),
                                js.getString("discussion_text"),js.getString("discussion_picture") ,
                                js.getString("discussion_date") ,js.getString("user_id") ,js.getString("first_name"),
                                js.getString("picture_path"),js.getString("second_name"),
                                js.getString("topic_id"), js.getString("topic_name") ));
                    }
                    myadapter.notifyDataSetChanged();

                }


                else if (json.getString("msg").equalsIgnoreCase("no discussions")) {
                    //remove we are loading now
                    if(StartFrom==0) {
                        listnewsData.clear();
                        listnewsData.add(new Discussion_list(null, null, null,
                                "add", null, null, null, null, null, null));
                    }

                    else {
                        //remove we are loading now
                        listnewsData.remove(listnewsData.size()-1);
                    }
                    // listnewsData.remove(listnewsData.size()-1);
                    //listnewsData.clear();

                    listnewsData.add(0,new Discussion_list(null, null, null,
                            "nodiscussions", null, null, null, null, null, null));
                }

                else if (json.getString("msg").equalsIgnoreCase("is subscriber")) {
                    //buFollow.setText("Un Follow");
                }

                else if (json.getString("msg").equalsIgnoreCase("is not subscriber")) {
                   // buFollow.setText("Follow");
                }

                else{
                    //Toast.makeText(getApplicationContext(), "Discussion Added", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("msg"), Toast.LENGTH_LONG).show();

                }

            } catch (Exception ex) {
                Log.d("er",  ex.getMessage());
                //first time
                listnewsData.clear();
                /*listnewsData.add(new Discussion_list(null, null, null,
                        "add", null, null, null, null, null, null));*/
            }

            myadapter.notifyDataSetChanged();
            //downloadUrl=null;
        }

        protected void onPostExecute(String  result2){
        }
    }
    void LoadDiscussions(int StartFrom,int UserOperation){
        this.StartFrom=StartFrom;
        this.UserOperation=UserOperation;
        //display loading
        if(StartFrom==0) // add loading at beggining
            listnewsData.add(0,new Discussion_list(null, null, null,
                    "loading", null, null, null, null, null, null));
        else // add loading at end
            listnewsData.add(new Discussion_list(null, null, null,
                    "loading", null, null, null, null, null, null));


        //myadapter.notifyDataSetChanged();
        String url;
        /*if (UserOperation == SearchType.MyFollowing){
            url="http://192.168.0.6/tHETASERVER/discussion_list.php?user_id="+ SaveSettings.UserID + "&StartFrom="+StartFrom + "&op="+ UserOperation;
            new MyAsyncTaskgetNews().execute(url);
        }*/

        //String url="http://192.168.0.6/tHETASERVER/discussion_list.php?user_id="+ SaveSettings.UserID + "&StartFrom="+StartFrom + "&op="+ UserOperation;
       /* if (UserOperation==SearchType.SearchIn){
            url="http://192.168.0.6/tHETASERVER/discussion_list.php?user_id="+ SaveSettings.UserID + "&StartFrom="+StartFrom + "&op="+ UserOperation + "&query="+ Searchquery;
            new MyAsyncTaskgetNews().execute(url);

        }*/
        /*if(UserOperation==SearchType.OnePerson){
            url="http://192.168.0.6/tHETASERVER/discussion_list.php?user_id="+ SelectedUserID + "&StartFrom="+StartFrom + "&op="+ UserOperation;
            new MyAsyncTaskgetNews().execute(url);

        }*/
        if(UserOperation == SearchType.OneTopic){
            url = "http://192.168.0.6/tHETASERVER/topic_discussions.php?topic_id="+val+"&StartFrom=0&op=1";//+UserOperation;
            //"http://192.168.0.6/tHETASERVER/topic_discussions.php?topic_id=2&StartFrom=0&op=1"
            //Toast.makeText(getActivity().getApplicationContext(), val, Toast.LENGTH_LONG).show();
            /*listnewsData.clear();
            listnewsData.add(new Discussion_list(null, null, null,"add", null, null, null, null, null, null));*/
            new MyAsyncTaskgetNews().execute(url);
        }


        /*if (UserOperation==SearchType.OnePerson)
            ChannelInfo.setVisibility(View.VISIBLE);
        else
            ChannelInfo.setVisibility(View.GONE);*/
    }


}
