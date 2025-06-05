# Expense Tracker App

*Company*: CODTECH IT SOLUTION

*Name*: Vaghela Jeet Vijaybhai

*Intern Id*: CT04DM877

*Domain*: Android Development

*Duration*: 4 Weeks 

*Mentor*: Neela Santhosh Kumar

# Description

# Add Transactions
Adding income or expense entries is easily done by typing the amount, choosing the type of transaction (Income or Expense), adding a category (e.g., Food, Salary, Travel), and selecting the currency. This offers a clean and structured method of recording each financial transaction.

# Real-Time Total Balance
The app dynamically shows the total balance from all transactions. Income adds to the balance, and expenses subtract from it. This real-time update enables users to immediately view the financial effect of their actions.

# Currency Switching
Users can toggle between various currencies (INR, USD, EUR) to see the total balance and transaction amounts converted accordingly. This becomes helpful for users who handle multiple currencies or travel abroad.

# History of Transactions
Every entry is viewed in a ListView, listing the type, category, and amount. Transactions are long-pressable to be deleted one by one. Thus, easy management and correction of entries.

# Clear All History
Users can clear all transactions by a simple button click. A confirmation dialog prevents accidental clearing.

# Light/Dark Theme Switch
The application has a Toggle Theme option to enable Light Mode or Dark Mode for improved readability and comfort, particularly at night.

# Technical Information
Programming Language: Java

IDE: Android Studio

Database: SQLite (through DBHelper)

UI Controls: EditText, Spinner, Button, TextView, ListView

Design Pattern: MVC-like design for ease of use

Data Persistence: Transaction records are kept locally by SQLite

# Project Structure
MainActivity.java: Houses the main logic such as UI handling, database operations, and list updates.

DBHelper.java: Handles SQLite database creation, upgrade, and CRUD operations.

activity_main.xml: Specifies the layout, such as input fields, spinners, buttons, and list.

# Use Case
This application is for:

Students handling their pocket money

Freelancers monitoring income and expenses

Anyone seeking a simple personal finance tracker


# OutPut

![Image](https://github.com/user-attachments/assets/86550b86-76a9-44d7-9db2-2286b570d4b6)
