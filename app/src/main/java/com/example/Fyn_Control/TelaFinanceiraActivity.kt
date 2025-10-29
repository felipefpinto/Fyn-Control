package com.example.Fyn_Control

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

// Banco de Dados SQLite
class BancoFinanceiroHelper(context: android.content.Context) :
    SQLiteOpenHelper(context, "financeApp", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS Transacoes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                descricao TEXT,
                valor REAL,
                tipo TEXT
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Transacoes")
        onCreate(db)
    }
}

class TelaFinanceiraActivity : AppCompatActivity() {

    private lateinit var bancoHelper: BancoFinanceiroHelper
    private lateinit var banco: SQLiteDatabase

    private lateinit var editDescricao: TextInputEditText
    private lateinit var editValor: TextInputEditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnInserir: Button
    private lateinit var btnDeletar: Button
    private lateinit var listView: ListView

    private lateinit var txtEntrada: TextView
    private lateinit var txtSaida: TextView
    private lateinit var txtSaldo: TextView
    private lateinit var saldoBarra: View

    private lateinit var adapter: ArrayAdapter<String>
    private val listaItens = ArrayList<String>()
    private var idSelecionado: Int? = null

    private val formatoMoeda: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_financeira)

        bancoHelper = BancoFinanceiroHelper(this)
        banco = bancoHelper.writableDatabase

        // UI
        editDescricao = findViewById(R.id.editDescricao)
        editValor = findViewById(R.id.editValor)
        spinnerTipo = findViewById(R.id.spinnerTipo)
        btnInserir = findViewById(R.id.btnInserir)
        btnDeletar = findViewById(R.id.btnDeletar)
        listView = findViewById(R.id.listView)

        txtEntrada = findViewById(R.id.txtEntrada)
        txtSaida = findViewById(R.id.txtSaida)
        txtSaldo = findViewById(R.id.txtSaldo)
        saldoBarra = findViewById(R.id.saldoBarra)

        spinnerTipo.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Entrada", "Saída")
        )

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaItens)
        listView.adapter = adapter

        btnInserir.setOnClickListener { inserir() }
        btnDeletar.setOnClickListener { deletar() }

        listView.setOnItemClickListener { _, _, position, _ ->
            val linha = listaItens[position]
            val partes = linha.split(" - ", " (", ")")
            idSelecionado = partes[0].toInt()
            editDescricao.setText(partes[1])
            editValor.setText(partes[2].replace("+", "").replace("-", "").replace("R$", "").trim())
            Toast.makeText(this, "Transação selecionada", Toast.LENGTH_SHORT).show()
        }

        listar()
    }

    private fun inserir() {
        val descricao = editDescricao.text.toString()
        val valor = editValor.text.toString().replace(",", ".").toDoubleOrNull()
        val tipo = spinnerTipo.selectedItem.toString()

        if (descricao.isNotEmpty() && valor != null) {
            val valores = ContentValues().apply {
                put("descricao", descricao)
                put("valor", valor)
                put("tipo", tipo)
            }
            banco.insert("Transacoes", null, valores)
            Toast.makeText(this, "Transação registrada!", Toast.LENGTH_SHORT).show()
            limparCampos()
            listar()
        } else {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listar() {
        listaItens.clear()
        var totalEntrada = 0.0
        var totalSaida = 0.0

        val cursor: Cursor = banco.rawQuery("SELECT * FROM Transacoes", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
                val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))

                val simbolo = if (tipo == "Entrada") "+" else "-"
                val cor = if (tipo == "Entrada") "🟢" else "🔴"
                listaItens.add("$id - $descricao ($simbolo${formatoMoeda.format(valor)}) $cor")

                if (tipo == "Entrada") totalEntrada += valor else totalSaida += valor
            } while (cursor.moveToNext())
        }
        cursor.close()

        adapter.notifyDataSetChanged()

        val saldoTotal = totalEntrada - totalSaida
        txtEntrada.text = "Entradas: ${formatoMoeda.format(totalEntrada)}"
        txtSaida.text = "Saídas: ${formatoMoeda.format(totalSaida)}"
        txtSaldo.text = "Saldo: ${formatoMoeda.format(saldoTotal)}"

        // Atualiza cor da barra
        when {
            saldoTotal > 0 -> saldoBarra.setBackgroundColor(getColor(android.R.color.holo_green_light))
            saldoTotal < 0 -> saldoBarra.setBackgroundColor(getColor(android.R.color.holo_red_light))
            else -> saldoBarra.setBackgroundColor(getColor(android.R.color.holo_orange_light))
        }
    }

    private fun deletar() {
        if (idSelecionado != null) {
            banco.delete("Transacoes", "id=?", arrayOf(idSelecionado.toString()))
            Toast.makeText(this, "Transação removida!", Toast.LENGTH_SHORT).show()
            limparCampos()
            listar()
        } else {
            Toast.makeText(this, "Selecione uma transação!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limparCampos() {
        editDescricao.text?.clear()
        editValor.text?.clear()
        idSelecionado = null
    }
}
