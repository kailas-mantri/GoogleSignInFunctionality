package com.leadsoft.smart_campus.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadsoft.smart_campus.Activity.CompanyDetailsActivity;
import com.leadsoft.smart_campus.R;

public class CompanyFragment extends Fragment {

    RecyclerView recyclerViewCompany;

    String post[] = {"Digital Marketing Manager", "Product Manager", "Account Collector", "Human Resources"},
            name[] = {"IBM", "TCS", "Accenture", "Infosys"},
            exp[] = {"0-2 Years", "0-2 Years", "0-3 Years", "1-2 Years"},
            location[] = {"Mumbai", "Pune", "Hyderabad", "Bengaluru"},
            skills[] = {"Planning & organising", "Problem Solving", "Commercial Awareness", "Self-Motivation"};

    public CompanyFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_company,container,false);

        recyclerViewCompany = rootView.findViewById(R.id.recyclerCompany);

        recyclerViewCompanyAdaptor adaptor = new recyclerViewCompanyAdaptor(getContext(),post,name,exp,location,skills);
        recyclerViewCompany.setAdapter(adaptor);
        recyclerViewCompany.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

    private class recyclerViewCompanyAdaptor extends RecyclerView.Adapter<MyviewHolder>{

        private Context context;
        private final String post[], name[], exp[], location[], skills[];

        public recyclerViewCompanyAdaptor(Context context, String[] post, String[] name, String[] exp, String[] location, String[] skills) {
            this.context = context;
            this.post = post;
            this.name = name;
            this.exp = exp;
            this.location = location;
            this.skills = skills;

        }

        @NonNull
        @Override
        public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewGroupCompany = inflater.inflate(R.layout.custom_list_company,parent,false);
            return new MyviewHolder(viewGroupCompany);
        }

        @Override
        public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
            holder.textViewPost.setText(post[position]);
            holder.textViewName.setText(name[position]);
            holder.textViewExp.setText(exp[position]);
            holder.textViewLocation.setText(location[position]);
            holder.textViewSkills.setText(skills[position]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CompanyDetailsActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return post.length;
        }
    }

    private class MyviewHolder extends RecyclerView.ViewHolder {

        TextView textViewPost, textViewName, textViewExp, textViewLocation, textViewSkills;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            findIdHolder(itemView);
            itemView.setClickable(true);
        }

        private void findIdHolder(View itemView) {
            textViewPost = itemView.findViewById(R.id.recyclerCompanyPostName);
            textViewName = itemView.findViewById(R.id.recyclerCompanyName_name);
            textViewExp = itemView.findViewById(R.id.recyclerCompanyExpDetails);
            textViewLocation = itemView.findViewById(R.id.recyclerCompanyLocationName);
            textViewSkills = itemView.findViewById(R.id.recyclerCompanySkillName);
        }
    }
}
