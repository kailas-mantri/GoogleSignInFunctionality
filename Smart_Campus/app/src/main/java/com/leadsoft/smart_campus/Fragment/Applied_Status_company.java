package com.leadsoft.smart_campus.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadsoft.smart_campus.R;

public class Applied_Status_company extends Fragment {

    RecyclerView recyclerView;

    String  post[] = {"Digital Marketing Manager", "Product Manager", "Account Collector", "Human Resources"},
            company_name[] = {"IBM", "TCS", "Accenture", "Infosys"},
            job[] = {"14560389313", "4156238945632", "547896231552", "985632745165"};

    public Applied_Status_company() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applied_status_company,container,false);

        recyclerView = rootView.findViewById(R.id.recycler_AppliedStatus);
        AppliedCompany_Adaptor adaptor = new AppliedCompany_Adaptor(getContext(),post,company_name,job);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    private class AppliedCompany_Adaptor extends RecyclerView.Adapter<Applied_Status_company.ViewHolder> {

        private final Context context;
        private final String post[], company_name[], job[];

        public AppliedCompany_Adaptor(Context context, String[] post, String[] company_name, String[] job) {
            this.context = context;
            this.post = post;
            this.company_name = company_name;
            this.job = job;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewStatus = inflater.inflate(R.layout.custom_applied_status,parent,false);
            return new Applied_Status_company.ViewHolder(viewStatus);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textViewPost.setText(post[position]);
            holder.textViewCompany.setText(company_name[position]);
            holder.textViewJob.setText(job[position]);


        }

        @Override
        public int getItemCount() {
            return post.length;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPost, textViewCompany, textViewJob;
        ProgressBar progressHorizontalBar;

        public ViewHolder(View itemView) {
            super(itemView);
            findViewID(itemView);
            itemView.setClickable(true);
        }

        private void findViewID(View itemView) {
            textViewPost = itemView.findViewById(R.id.custom_applied_CompanyPost_Name);
            textViewCompany = itemView.findViewById(R.id.custom_applied_CompanyName_text);
            textViewJob = itemView.findViewById(R.id.custom_applied_CompanyJob_id_text);
            progressHorizontalBar = itemView.findViewById(R.id.progress_horizontal);
        }
    }
}
