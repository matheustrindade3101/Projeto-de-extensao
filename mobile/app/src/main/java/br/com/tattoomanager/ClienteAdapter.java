package br.com.tattoomanager;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.tattoomanager.data.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    public interface OnVisibleListChangedListener {
        void onVisibleListChanged(int visibleCount);
    }

    private final List<Cliente> allClients = new ArrayList<>();
    private final List<Cliente> visibleClients = new ArrayList<>();
    private final OnVisibleListChangedListener listChangedListener;

    private String searchQuery = "";
    private Boolean activeFilter = null;

    public ClienteAdapter(OnVisibleListChangedListener listChangedListener) {
        this.listChangedListener = listChangedListener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ClienteViewHolder holder,
            int position
    ) {
        holder.bind(visibleClients.get(position));
    }

    @Override
    public int getItemCount() {
        return visibleClients.size();
    }

    public void submitList(List<Cliente> clients) {
        allClients.clear();
        allClients.addAll(clients);
        applyFilters();
    }

    public void setSearchQuery(String query) {
        searchQuery = query == null
                ? ""
                : query.trim().toLowerCase(Locale.ROOT);
        applyFilters();
    }

    public void setActiveFilter(Boolean active) {
        activeFilter = active;
        applyFilters();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void applyFilters() {
        visibleClients.clear();

        for (Cliente client : allClients) {
            boolean matchesStatus = activeFilter == null
                    || client.isAtivo() == activeFilter;

            String searchableContent = (
                    client.getNome() + " "
                            + client.getTelefone() + " "
                            + client.getEmail()
            ).toLowerCase(Locale.ROOT);

            boolean matchesSearch = searchQuery.isEmpty()
                    || searchableContent.contains(searchQuery);

            if (matchesStatus && matchesSearch) {
                visibleClients.add(client);
            }
        }

        notifyDataSetChanged();
        listChangedListener.onVisibleListChanged(visibleClients.size());
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView phone;
        private final TextView email;
        private final TextView status;

        ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textClientName);
            phone = itemView.findViewById(R.id.textClientPhone);
            email = itemView.findViewById(R.id.textClientEmail);
            status = itemView.findViewById(R.id.textClientStatus);
        }

        void bind(Cliente client) {
            name.setText(client.getNome());
            phone.setText(client.getTelefone());
            email.setText(client.getEmail());

            if (client.isAtivo()) {
                status.setText(R.string.client_status_active);
                status.setTextColor(ContextCompat.getColor(
                        itemView.getContext(),
                        R.color.success
                ));
            } else {
                status.setText(R.string.client_status_inactive);
                status.setTextColor(ContextCompat.getColor(
                        itemView.getContext(),
                        R.color.error
                ));
            }
        }
    }
}
