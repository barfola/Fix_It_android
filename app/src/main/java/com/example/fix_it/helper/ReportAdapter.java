package com.example.fix_it.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fix_it.R;
import com.example.fix_it.api_dto.ProblemReport;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private  List<ProblemReport> ProblemReportList;

    public ReportAdapter(List<ProblemReport> ProblemReportList) {
        this.ProblemReportList = ProblemReportList;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView description, location, reportType;

        public ReportViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.reportDescription);
            location = itemView.findViewById(R.id.reportLocation);
            reportType = itemView.findViewById(R.id.reportType);
        }
    }



    public void updateData(List<ProblemReport> newReports) {
        ProblemReportList.clear();
        ProblemReportList.addAll(newReports);
        notifyDataSetChanged();
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.problem_report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        ProblemReport report = ProblemReportList.get(position);

        holder.description.setText(report.getDescription());
        holder.location.setText("Location: " + report.getLocation().name());
        holder.reportType.setText("Report Type: " + report.getReportType().name());
    }

    @Override
    public int getItemCount() {
        return ProblemReportList.size();
    }
}
