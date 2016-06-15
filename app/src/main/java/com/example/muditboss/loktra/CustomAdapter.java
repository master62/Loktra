package com.example.muditboss.loktra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 *
 */
public class CustomAdapter extends BaseAdapter{

        private Context mContext;
        public  String[] resultStrs;



        public CustomAdapter(Context c,String[] data) {
            mContext = c;
            resultStrs = data;

        }

        public int getCount() {
            return 30;
        }

        public Object getItem(int position) {
            if(resultStrs.length!=0)
                return resultStrs[position];
            else
                return null;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;
            int layout=R.layout.list_item_commit;


            if(convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(layout, parent, false);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else {

                holder = (ViewHolder) convertView.getTag();
            }
            String[] separate = resultStrs[position].split("-");

            String author = "Author"+": "+separate[0];
            String commitMsg = "Message"+": "+separate[1];
            String sha = "SHA"+": "+separate[2];
            String url = separate[3];

            holder.shaView.setText(sha);

            holder.authorView.setText(author);

            holder.msgView.setText(commitMsg);

            if(!url.equals(""))
            Picasso.with(mContext).load(url).resize(50,50).into(holder.imgView);

            return convertView;
        }

        static class ViewHolder{
            TextView shaView;
            TextView authorView;
            TextView msgView;
            ImageView imgView;

            public ViewHolder(View view){

                shaView = (TextView) view.findViewById(R.id.list_item_sha_textview);
                authorView = (TextView) view.findViewById(R.id.list_item_author_textview);
                msgView = (TextView) view.findViewById(R.id.list_item_message_text);
                imgView = (ImageView) view.findViewById(R.id.imageProfile);
            }
        }
    }


