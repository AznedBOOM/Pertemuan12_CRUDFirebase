package com.example.pertemuan9_restapi.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan9_restapi.R
import com.example.pertemuan9_restapi.databinding.UserListBinding
import com.example.pertemuan9_restapi.model.request.DataMahasiswa
import com.example.pertemuan9_restapi.model.response.ResponseDeleteData
import com.example.pertemuan9_restapi.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAdapter(private var dataMahasiswa : List<DataMahasiswa>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(val binding : UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        var api = APIClient.instance
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UserListBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataMahasiswa.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtNama.text = dataMahasiswa[position].nama
        holder.binding.txtNIM.text = dataMahasiswa[position].nIM
        holder.binding.txtTelp.text = dataMahasiswa[position].telepon
        holder.binding.cardView.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("nim", dataMahasiswa[position].nIM)
            bundle.putString("nama", dataMahasiswa[position].nama)
            bundle.putString("telepon", dataMahasiswa[position].telepon)
            Navigation.findNavController(it).navigate(R.id
                .action_homeFragment_to_detailFragment,
                bundle)
        }

        holder.binding.btnEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nim", dataMahasiswa[position].nIM)
            bundle.putString("nama", dataMahasiswa[position].nama)
            bundle.putString("telepon", dataMahasiswa[position].telepon)
            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment_to_updateFragment, bundle)
        }

        holder.binding.btnDelete.setOnClickListener {
            holder.api.deleteDetailMahasiswa(dataMahasiswa[position].nIM)
                .enqueue(object : Callback<ResponseDeleteData>{
                    override fun onResponse(
                        call: Call<ResponseDeleteData>,
                        response: Response<ResponseDeleteData>
                    ) {
                        if (response.isSuccessful){
                            dataMahasiswa = dataMahasiswa.toMutableList().apply {
                                removeAt(position)
                            }
                            notifyDataSetChanged()
                            Toast.makeText(holder.itemView.context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseDeleteData>, t: Throwable) {
                        Toast.makeText(holder.itemView.context, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

}