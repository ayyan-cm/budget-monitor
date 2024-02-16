package com.example.budgetmoniter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transactions.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions (_id INTEGER PRIMARY KEY, description TEXT, amount REAL, type TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions;");
        onCreate(db);
    }

    public void saveTransaction(String description, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("amount", amount);
        values.put("type", type);
        db.insert("transactions", null, values);
        db.close();
    }

    public void resetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("transactions", null, null);
        db.close();
    }

    public Cursor getTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("transactions", null, null, null, null, null, null);
    }

    public double calculateCurrentBalance() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Calculate total income
        Cursor incomeCursor = db.rawQuery("SELECT SUM(amount) FROM transactions WHERE type = 'Income';", null);
        double totalIncome = 0;
        if (incomeCursor.moveToFirst()) {
            totalIncome = incomeCursor.getDouble(0);
        }
        incomeCursor.close();

        // Calculate total expense
        Cursor expenseCursor = db.rawQuery("SELECT SUM(amount) FROM transactions WHERE type = 'Expense';", null);
        double totalExpense = 0;
        if (expenseCursor.moveToFirst()) {
            totalExpense = expenseCursor.getDouble(0);
        }
        expenseCursor.close();

        // Calculate current balance
        double currentBalance = totalIncome - totalExpense;

        return currentBalance;
    }


    public double calculateTotalAmount(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM transactions WHERE type = ?;", new String[]{type});
        double totalAmount = 0;
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }
        cursor.close();
        return totalAmount;
    }
}
