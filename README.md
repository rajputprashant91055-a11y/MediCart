# MediCartPro - Desktop Medicine Ordering Application

A complete Java Swing desktop application connected to MySQL via JDBC, featuring Smart Pharmacy Matching, Prescription Verification, and Inventory Concurrency Controls.

---

## 🚀 Quick Start in VS Code (3 Simple Steps)

### 1. Ensure MySQL is Running
MediCart connects to MySQL at `localhost:3306` with database `medicart_db`.
To check or start your MySQL Windows service, open PowerShell and run:
```powershell
Start-Service MySQL94
```

### 2. Open the Project in VS Code
1. Open **VS Code**.
2. Go to top menu: **File** -> **Open Folder...**
3. Select this folder: `MediCart`

*(Note: The `.vscode/settings.json` file is already pre-configured to automatically include `lib/mysql-connector-j-26.7.0.jar`).*

### 3. Run the Application
1. In the file explorer on the left, click on **`Main.java`**.
2. Click the **Run** button directly above `public static void main` (or press **F5** / **Ctrl+F5**).

---

## 💻 Alternative: Run from Terminal / PowerShell

You can also run the application anytime directly from PowerShell:
```powershell
cd "c:\Users\DELL\OneDrive\Desktop\MediCart"
java -cp ".;lib\mysql-connector-j-26.7.0.jar" Main
```

---

## 🔑 Default Login Accounts

| Portal | Email | Password | Role |
| :--- | :--- | :--- | :--- |
| **Customer** | `customer@medicart.com` | `customer123` | Customer |
| **Admin** | `admin@medicart.com` | `admin123` | Admin |
| **New Account** | *Any email* | *Your password* | Customer (via "CREATE ACCOUNT") |

---

## ⚙️ Configuration

Database credentials can be adjusted in:
`database/db.properties`
```properties
db.url=jdbc:mysql://localhost:3306/medicart_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=medicart_app
db.password=MediCartApp2026!
```

---

## 📁 Project Architecture

- **`Main.java`**: Launches the main application home screen.
- **`database/`**: JDBC connection management, `db.properties`, and `schema.sql`.
- **`model/`**: Normalized data models (`User`, `Medicine`, `Pharmacy`, `Order`, `Prescription`, etc.).
- **`dao/`**: Data Access Objects with prepared statements and row-level locking.
- **`service/`**: Business logic, Smart Pharmacy Matcher (100% full match), and SHA-256 salted password security.
- **`ui/`**: Java Swing interface preserving original design, colors, and workflows.
- **`lib/`**: MySQL JDBC Connector driver.
- **`images/`**: Product catalog medicine photos.
