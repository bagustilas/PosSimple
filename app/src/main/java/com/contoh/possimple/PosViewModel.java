package com.contoh.possimple;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class PosViewModel extends ViewModel {

    // key = produk, value = jumlah yang dipesan
    private final MutableLiveData<Map<Product, Integer>> orders =
            new MutableLiveData<>(new LinkedHashMap<>());

    // Activity hanya boleh membaca, tidak boleh mengubah langsung
    public LiveData<Map<Product, Integer>> getOrders() {
        return orders;
    }

    public void addItem(Product product) {
        Map<Product, Integer> copy = new LinkedHashMap<>(orders.getValue());
        int qty = copy.getOrDefault(product, 0);
        copy.put(product, qty + 1);
        orders.setValue(copy); // harus objek baru agar observer terpanggil
    }

    public void clear() {
        orders.setValue(new LinkedHashMap<>());
    }
}