package com.example.bt9.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt9.R;
import com.example.bt9.models.WaitingCustomer;
import com.example.bt9.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class WaitingCustomerAdapter extends RecyclerView.Adapter<WaitingCustomerAdapter.ViewHolder> {
    
    private List<WaitingCustomer> customers;
    private OnCustomerAcceptListener listener;

    public interface OnCustomerAcceptListener {
        void onAcceptCustomer(WaitingCustomer customer);
    }

    public WaitingCustomerAdapter(OnCustomerAcceptListener listener) {
        this.customers = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_waiting_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WaitingCustomer customer = customers.get(position);
        
        holder.customerNameText.setText(customer.getCustomerName());
        holder.customerIdText.setText("ID: " + customer.getCustomerId());
        holder.waitingTimeText.setText("Chờ từ: " + DateUtils.formatFullTimestamp(customer.getWaitingSince()));
        
        holder.acceptButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAcceptCustomer(customer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setCustomers(List<WaitingCustomer> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    public void addCustomer(WaitingCustomer customer) {
        customers.add(customer);
        notifyItemInserted(customers.size() - 1);
    }

    public void removeCustomer(String customerId) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId().equals(customerId)) {
                customers.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameText;
        TextView customerIdText;
        TextView waitingTimeText;
        Button acceptButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameText = itemView.findViewById(R.id.customerNameText);
            customerIdText = itemView.findViewById(R.id.customerIdText);
            waitingTimeText = itemView.findViewById(R.id.waitingTimeText);
            acceptButton = itemView.findViewById(R.id.acceptButton);
        }
    }
}

