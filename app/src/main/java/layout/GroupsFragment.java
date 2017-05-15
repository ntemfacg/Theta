package layout;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.app.Activity;
import android.widget.Toast;

import com.example.ntemfacg.theta.Discussion_list;
import com.example.ntemfacg.theta.HomeActivity;
import com.example.ntemfacg.theta.Operations;
import com.example.ntemfacg.theta.R;
import com.example.ntemfacg.theta.SaveSettings;
import com.example.ntemfacg.theta.SearchType;
import com.example.ntemfacg.theta.Topic_list;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    ArrayList<Topic_list>   topicsdata = new ArrayList<Topic_list>();
    MyCustomAdapter myadapter;
    int SelectedTopicID=0;
    int StartFrom=0;
    ImageView fr_join;
    int UserOperation=SearchType.MyFollowing;
    public static int Dynamic_id;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Groups");
        //return inflater.inflate(R.layout.fragment_groups, container, false);
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        myadapter=new MyCustomAdapter(getActivity().getApplicationContext(), topicsdata);
        ListView lstopics=(ListView) rootView.findViewById(R.id.groups);
        lstopics.setAdapter(myadapter);//intisal with data
        LoadDiscussions(0,1);

        //topicsdata.add(new Topic_list(null, "Next"));


        return rootView;
    }

    String downloadUrl=null;

    public void loadfeed(Context context){
        //HomeActivity.loadgroupfeed(context);
    }

    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<Topic_list> listnewsDataAdpater ;
        Context context;
        public MyCustomAdapter(Context context, ArrayList<Topic_list> listnewsDataAdpater) {
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

            final   Topic_list s = listnewsDataAdpater.get(position);
            LayoutInflater mInflater = getActivity().getLayoutInflater();
            View myView = mInflater.inflate(R.layout.topic_item, null);
            TextView txtTopicname = (TextView) myView.findViewById(R.id.group_title);
            txtTopicname.setText(s.topic_name);

            //final TextView textView = (TextView) myView.findViewById(R.id.text_join);
            final ImageView fr_join = (ImageView) myView.findViewById(R.id.fr_join);
            fr_join.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    SelectedTopicID=Integer.parseInt(s.topic_id);
                    //LoadDiscussions(0,SearchType.OnePerson);
                    fr_join.setBackgroundResource(R.drawable.leave_24);
                    //textView.setText("joined");
                    String url="http://192.168.0.6/tHETASERVER/add_interest.php?user_id="+SaveSettings.UserID +"&topic_id="+SelectedTopicID+"&op=1";
                    new MyAsyncTaskgetNews().execute(url);
                }
            });

            ImageView fr_group = (ImageView) myView.findViewById(R.id.fr_feed);
            fr_group.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Dynamic_id =Integer.parseInt(s.topic_id);// s.topic_id;
                    HomeActivity homeActivity = new HomeActivity();
                    homeActivity.loadgroupfeed(getActivity());
                    //Toast.makeText(getActivity().getApplicationContext(), Dynamic_id, Toast.LENGTH_LONG).show();
                    //HomeActivity fragment= new HomeActivity();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(GroupsFragment.this);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                    //getActivity().finish();
                }
            });

            ImageView fr_leave = (ImageView) myView.findViewById(R.id.fr_leave);
            fr_leave.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    SelectedTopicID = Integer.parseInt(s.topic_id);
                    String url="http://192.168.0.6/tHETASERVER/add_interest.php?user_id="+SaveSettings.UserID +"&topic_id="+SelectedTopicID+"&op=2";
                    new MyAsyncTaskgetNews().execute(url);
                }
            });
            return myView;

            /*if(s.topic_name.equals("add")) {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.activity_new_query, null);

                final EditText etPost = (EditText) myView.findViewById(R.id.inputNote);
                Button iv_post=(Button) myView.findViewById(R.id.buttonNoteDone) ;

                ImageView iv_attach=(ImageView) myView.findViewById(R.id.attach) ;
                iv_attach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //LoadImage();
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
                        *//*if (downloadUrl == null){
                            downloadUrl="empty.jpg";
                            return;
                        }*//*
                        *//*String url="http://192.168.0.6/tHETASERVER/add_discussion.php?user_id="+ SaveSettings.UserID +"&topic_id="+topic_temp+"&discussion_text="+ discussions +"&discussion_picture="+ downloadUrl;
                        etPost.setText("");
                        new HomeActivity.MyAsyncTaskgetNews().execute(url);*//*
                    }
                });
                return myView;
            }*/
            /*else if(s.topic_name.equals("loading")) {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.discussions_loading, null);
                return myView;
            }*/
            /*else if(s.topic_name.equals("nodiscussions")) {
                LayoutInflater mInflater =getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.no_new_discussion, null);
                return myView;
            }*/
            //return tView;

            /*else {
                LayoutInflater mInflater = getActivity().getLayoutInflater();
                View myView = mInflater.inflate(R.layout.topic_item, null);
                TextView txtUserName = (TextView) myView.findViewById(R.id.group_title);
                txtUserName.setText(s.topic_name);
                txtUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        *//*
                        SelectedUserID=Integer.parseInt(s.user_id);
                        LoadTweets(0,SearchType.OnePerson);
                        txtnamefollowers.setText(s.first_name);
                        String url="http://10.0.2.2/~hussienalrubaye/twitterserver/isfollowing.php?user_id="+SaveSettings.UserID +"&following_user_id="+SelectedUserID;
                        new MyAsyncTaskgetNews().execute(url);*//*
                    }
                });
                *//*TextView txt_discussion = (TextView) myView.findViewById(R.id.txt_discussion);
                txt_discussion.setText(s.discussion_text);
                TextView txt_discussion_date = (TextView) myView.findViewById(R.id.txt_discussion_date);
                txt_discussion_date.setText(s.discussion_date);
                ImageView discussion_picture=(ImageView)myView.findViewById(R.id.discussion_picture);
                Picasso.with(context).load(s.discussion_picture).into(discussion_picture);
                ImageView picture_path=(ImageView)myView.findViewById(R.id.picture_path);
                Picasso.with(context).load(s.picture_path).into(picture_path);
                TextView topicTitle = (TextView) myView.findViewById(R.id.topic_title);
                topicTitle.setText(s.topic_name);*//*
                return myView;
            }*/
        }
        //load image

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
                if (json.getString("msg")==null)
                    return;

                if (json.getString("msg").equalsIgnoreCase("discussion added")) {
                    LoadDiscussions(0,UserOperation);

                }
                else if (json.getString("msg").equalsIgnoreCase("pass")) {
                    /*if(StartFrom==0) {
                        topicsdata.clear();
                        topicsdata.add(new Topic_list(null, null));
                    }

                    else {
                        //remove we are loading now
                        topicsdata.remove(topicsdata.size()-1);
                    }*/
                    JSONArray topics=new JSONArray( json.getString("info"));

                    for (int i = 0; i <topics.length() ; i++) {
                        // try to add the resourcess
                        JSONObject js=topics.getJSONObject(i);

                        //add data and view it
                        //Toast.makeText(getActivity().getApplicationContext(), "Executed", Toast.LENGTH_LONG).show();

                        topicsdata.add(new Topic_list(js.getString("topic_id"),
                                js.getString("topic_name")));
                    }
                    myadapter.notifyDataSetChanged();

                }


                /*else if (json.getString("msg").equalsIgnoreCase("no discussions")) {
                    //remove we are loading now
                    if(StartFrom==0) {
                        topicsdata.clear();
                        topicsdata.add(new Topic_list(null, null));
                    }

                    else {
                        //remove we are loading now
                        topicsdata.remove(topicsdata.size()-1);
                    }
                    // listnewsData.remove(listnewsData.size()-1);
                    //listnewsData.clear();

                    topicsdata.add(0,new Topic_list(null, null));
                }*/

                else if (json.getString("msg").equalsIgnoreCase("updated")) {
                    //TextView textView =
                    //fr_join.setBackgroundResource(R.drawable.leave_24);
                    //buFollow.setText("Un Follow");
                    /*LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                    View tview = layoutInflater.inflate(R.layout.topic_item, null);
                    TextView textView = (TextView) tview.findViewById(R.id.text_join);
                    textView.setText("joined");*/
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("msg") + " interest groups", Toast.LENGTH_LONG).show();


                }

                else if (json.getString("msg").equalsIgnoreCase("is not subscriber")) {
                    //buFollow.setText("Follow");
                }

                else if (json.getString("msg").equalsIgnoreCase("fail")){
                    //Toast.makeText(getApplicationContext(), "Discussion Added", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("msg") + " already in group", Toast.LENGTH_LONG).show();

                }

            } catch (Exception ex) {
                Log.d("er",  ex.getMessage());
                //first time
                topicsdata.clear();
                topicsdata.add(new Topic_list(null, null));
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
        //Toast.makeText(getActivity().getApplicationContext(), "Glide", Toast.LENGTH_LONG).show();
        //display loading
        /*if(StartFrom==0) // add loading at beggining
            topicsdata.add(0,new Topic_list(null, null));
        else // add loading at end
            topicsdata.add(new Topic_list(null, null));
*/

        myadapter.notifyDataSetChanged();

        String url="http://192.168.0.6/tHETASERVER/groups.php"; //;?user_id="+ SaveSettings.UserID + "&StartFrom="+StartFrom + "&op="+ UserOperation;
        if (UserOperation==SearchType.SearchIn)
            //url="http://192.168.0.6/tHETASERVER/discussion_list.php?user_id="+ SaveSettings.UserID + "&StartFrom="+StartFrom + "&op="+ UserOperation + "&query="+ Searchquery;
        if(UserOperation==SearchType.OnePerson)
            url="http://192.168.0.6/tHETASERVER/discussion_list.php?user_id="+ SelectedTopicID + "&StartFrom="+StartFrom + "&op="+ UserOperation;

        new MyAsyncTaskgetNews().execute(url);

        /*if (UserOperation==SearchType.OnePerson)
            ChannelInfo.setVisibility(View.VISIBLE);
        else
            ChannelInfo.setVisibility(View.GONE);*/
    }


}
