package com.example.fix_it.helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fix_it.R;
import com.example.fix_it.ReportDetailActivity;
import com.example.fix_it.api_dto.ProblemReport;

import java.util.List;

public class AdminReportAdapter extends RecyclerView.Adapter<AdminReportAdapter.ReportViewHolder> {

    private List<ProblemReport> problemReportList;
    private Context context;

    public AdminReportAdapter(List<ProblemReport> problemReportList, Context context) {
        this.problemReportList = problemReportList;
        this.context = context;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView description, location, reportType;
        Button viewButton;

        public ReportViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.reportDescription);
            location = itemView.findViewById(R.id.reportLocation);
            reportType = itemView.findViewById(R.id.reportType);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        ProblemReport report = problemReportList.get(position);

        holder.description.setText(AndroidUtils.replaceUnderscoreWithSpace(report.getDescription()));
        holder.location.setText(AndroidUtils.replaceUnderscoreWithSpace("Location: " + report.getLocation().name()));
        holder.reportType.setText(AndroidUtils.replaceUnderscoreWithSpace("Report Type: " + report.getReportType().name()));

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetailActivity.class);
            intent.putExtra("problemReport", report);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return problemReportList.size();
    }

    public void updateData(List<ProblemReport> newReports) {
        problemReportList.clear();
        problemReportList.addAll(newReports);
        notifyDataSetChanged();
    }
}
