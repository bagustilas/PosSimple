package com.contoh.possimple;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.contoh.possimple.databinding.ActivityMainBinding; // sesuaikan package

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PosViewModel ViewModel;

    // Daftar produk (harga bebas kamu ganti)
    private final Product kopi   = new Product("Kopi Hitam", 5000);
    private final Product dawet  = new Product("Dawet", 7000);
    private final Product jenang = new Product("Jenang Alot", 8000);
    private final Product mangut = new Product("Mangut Lele", 20000);

    // key = produk, value = jumlah yang dipesan
    private final Map<Product, Integer> orders = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnKopi.setOnClickListener(v -> addItem(kopi));
        binding.btnDawet.setOnClickListener(v -> addItem(dawet));
        binding.btnJenang.setOnClickListener(v -> addItem(jenang));
        binding.btnMangut.setOnClickListener(v -> addItem(mangut));

        binding.btnCancel.setOnClickListener(v -> resetOrder());
        binding.btnCheckout.setOnClickListener(v -> checkout());

        render();
    }

    private void addItem(Product product) {
        int qty = orders.getOrDefault(product, 0);
        orders.put(product, qty + 1);
        render();
    }

    private int total() {
        int sum = 0;
        for (Map.Entry<Product, Integer> e : orders.entrySet()) {
            sum += e.getKey().getPrice() * e.getValue();
        }
        return sum;
    }

    private String formatRupiah(int value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        return "Rp " + nf.format(value);
    }

    private String buildOrderSummary() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Product, Integer> e : orders.entrySet()) {
            Product p = e.getKey();
            int qty = e.getValue();
            if (sb.length() > 0) sb.append("\n");
            sb.append(qty).append("x ").append(p.getName())
                    .append(" = ").append(formatRupiah(p.getPrice() * qty));
        }
        return sb.toString();
    }

    // Satu method untuk memperbarui seluruh tampilan
    private void render() {
        binding.tvTotal.setText(formatRupiah(total()));

        boolean hasOrder = !orders.isEmpty();
        binding.tvOrder.setText(hasOrder
                ? buildOrderSummary()
                : getString(R.string.empty_order));

        binding.btnCheckout.setEnabled(hasOrder);
        binding.btnCancel.setEnabled(hasOrder);
    }

    private void resetOrder() {
        orders.clear();
        render();
    }

    private void checkout() {
        String message = buildOrderSummary() + "\n\nTotal: " + formatRupiah(total());

        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Pembayaran")
                .setMessage(message)
                .setPositiveButton("Bayar", (dialog, which) -> {
                    Toast.makeText(this, "Transaksi berhasil", Toast.LENGTH_SHORT).show();
                    resetOrder();
                })
                .setNegativeButton("Kembali", null)
                .show();
    }
}