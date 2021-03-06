package com.example.muditboss.loktra;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter for custom listview
 */

public class CustomAdapter extends BaseAdapter implements Filterable{

        private Context mContext;
        public  String[] resultStrs;
        private ItemFilter filter = new ItemFilter();
        protected List<String> originalData;
        protected List<String> filteredData;
        public String wildcardForSplit = "///";


        public CustomAdapter(Context c,String[] data) {
            mContext = c;
            resultStrs = data;
        }

        public int getCount() {
            if(filteredData.size()!=0)
                return filteredData.size();

            return 0;
        }

        public Object getItem(int position) {
            if(filteredData.size()!=0)
                return filteredData.get(position);
            else
                return null;
        }

        public long getItemId(int position) { return position;
        }


        public Filter getFilter(){
         return filter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;
            int layout=R.layout.list_item_commit;
            int empty_layout = R.layout.no_result_filter_layout;
            String author="No result found";
            String sha="";
            String commitMsg="";
            String url="";

            if(convertView==null&&!filteredData.isEmpty()) {
                convertView = LayoutInflater.from(mContext).inflate(layout, parent, false);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else if(filteredData.isEmpty()){
                convertView = LayoutInflater.from(mContext).inflate(empty_layout, parent, false);

                holder = new ViewHolder(convertView);
                //convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            String[] separate=null;

            if(!filteredData.isEmpty()) {

                if (position < filteredData.size()) {

                    separate = filteredData.get(position).split(wildcardForSplit);
                    author =  separate[0];
                    commitMsg =  separate[1];
                    url = separate[2];

                  //  holder.shaView.setText(sha);

                    holder.authorView.setText(author);

                    holder.msgView.setText(commitMsg);

                       if(!url.isEmpty())
                        Picasso.with(mContext)
                                .load(url)
                                .error(R.drawable.download)
                                .resize(120,120)
                                .centerCrop()
                                .into(holder.imgView);

                           Log.d("Empty_URL", url);

                }
            }

            return convertView;
        }

       private static class ViewHolder{

           // private TextView shaView;
            private TextView authorView;
            private TextView msgView;
            private ImageView imgView;

            public ViewHolder(View view){

              //  shaView = (TextView) view.findViewById(R.id.list_item_sha_textview);
                authorView = (TextView) view.findViewById(R.id.list_item_author_textview);
                msgView = (TextView) view.findViewById(R.id.list_item_message_text);
                imgView = (ImageView) view.findViewById(R.id.imageProfile);
            }
        }

    private class ItemFilter extends Filter{

        protected FilterResults performFiltering(CharSequence constraint){
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final ArrayList<String> nlist = new ArrayList<String>();

            String filterableString ;

            for (int i = 0; i < originalData.size(); i++) {
                filterableString = originalData.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

//            results.values = originalData;
//            results.count = originalData.size();

            if(!nlist.isEmpty()) {
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;

        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count!=0) {
                filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }else
                filteredData.clear();


        }

    }
    }


