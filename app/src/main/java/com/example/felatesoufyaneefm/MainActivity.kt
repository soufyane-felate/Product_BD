package com.example.felatesoufyaneefm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rc: RecyclerView
    private lateinit var db: DataBase
    private lateinit var pcList: MutableList<Product>
    private lateinit var adapter: ProductAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rc = findViewById(R.id.recycler)
        db = DataBase(applicationContext)
        pcList = mutableListOf()
        adapter = ProductAdapter(pcList) { selectedPc -> showDetails(selectedPc) }

        rc.adapter = adapter
        rc.layoutManager = GridLayoutManager(this, 1)

        pcList.addAll(db.getAllPc())
        adapter.notifyDataSetChanged()

        val addButton: Button = findViewById(R.id.add)
        addButton.setOnClickListener {
            val idEditText: EditText = findViewById(R.id.id)
            val nameEditText: EditText = findViewById(R.id.name)
            val priceEditText: EditText = findViewById(R.id.price)
            val imageEditText: EditText = findViewById(R.id.image)

            val id = idEditText.text.toString().toIntOrNull() ?: 0
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val image = imageEditText.text.toString()

            if (name.isNotEmpty() && price != 0.0 && image.isNotEmpty()) {
                val existingProduct = pcList.find { it.id == id }

                if (existingProduct == null) {
                    val newPc = Product(id, name, price, image)
                    pcList.add(newPc)
                    adapter.notifyItemInserted(pcList.size - 1)

                    val insertedId = db.addData(id, name, price, image)
                    if (insertedId != -1L) {
                        Toast.makeText(applicationContext, "Item added to database", Toast.LENGTH_SHORT).apply {
                            view?.setBackgroundResource(R.drawable.su_toast)
                            setGravity(Gravity.BOTTOM, 20, 20)
                        }
                            .show()
                    } else {
                        Toast.makeText(applicationContext, "Failed to add item to database", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Item with the given ID already exists", Toast.LENGTH_SHORT).show()
                }

                idEditText.text.clear()
                nameEditText.text.clear()
                priceEditText.text.clear()
                imageEditText.text.clear()
            } else {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .apply {
                        view?.setBackgroundResource(R.drawable.taost_style)
                        setGravity(Gravity.BOTTOM, 20, 20)
                    }
                    .show()
            }
        }


        val searchButton: Button = findViewById(R.id.search)
        searchButton.setOnClickListener {
            val idEditText: EditText = findViewById(R.id.id)
            val id = idEditText.text.toString().toIntOrNull() ?: 0

            searchById(id)
        }
    }

    private fun showDetails(pc: Product) {
        val idEditText: EditText = findViewById(R.id.id)
        val nameEditText: EditText = findViewById(R.id.name)
        val priceEditText: EditText = findViewById(R.id.price)
        val imageEditText: EditText = findViewById(R.id.image)

        idEditText.setText(pc.id.toString())
        nameEditText.setText(pc.name)
        priceEditText.setText(pc.price.toString())
        imageEditText.setText(pc.image)
    }


    private fun searchById(id: Int) {
        val product = pcList.find { it.id == id }

        if (product != null) {
            val idEditText: EditText = findViewById(R.id.id)
            val nameEditText: EditText = findViewById(R.id.name)
            val priceEditText: EditText = findViewById(R.id.price)
            val imageEditText: EditText = findViewById(R.id.image)

            idEditText.setText(product.id.toString())
            nameEditText.setText(product.name)
            priceEditText.setText(product.price.toString())
            imageEditText.setText(product.image)

            adapter.submitList(listOf(product))
        } else {
            Toast.makeText(applicationContext, "   Product not found   ", Toast.LENGTH_LONG)
                .apply {
                    view?.setBackgroundResource(R.drawable.taost_style)
                    setGravity(Gravity.BOTTOM, 20, 20)
                }
                .show()

            clearUIElements()
        }
    }

    private fun clearUIElements() {
        val idEditText: EditText = findViewById(R.id.id)
        val nameEditText: EditText = findViewById(R.id.name)
        val priceEditText: EditText = findViewById(R.id.price)
        val imageEditText: EditText = findViewById(R.id.image)

        idEditText.text.clear()
        nameEditText.text.clear()
        priceEditText.text.clear()
        imageEditText.text.clear()

        adapter.submitList(pcList)
    }

}
