@echo off
title MediCart Database Viewer
color 0B
cd /d "%~dp0"

set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 9.4\bin\mysql.exe"

if not exist "%MYSQL_PATH%" (
    echo [ERROR] MySQL not found at %MYSQL_PATH%
    pause
    exit /b
)

:MENU
cls
echo ===============================================================
echo                MEDICART DATABASE VIEWER (MySQL 9.4)
echo ===============================================================
echo Database: medicart_db   ^|   User: medicart_app
echo.
echo   [1] Show All Database Tables & Row Counts
echo   [2] Show Registered Users (with Salted Hash Passwords)
echo   [3] Show Medicine Catalog & Pricing
echo   [4] Show Pharmacies & Stock Inventory
echo   [5] Show Orders & Order Details
echo   [6] Show Prescriptions
echo   [7] Open Interactive MySQL Terminal (mysql> prompt)
echo   [8] Exit
echo.
echo ===============================================================
set /p choice="Enter your choice (1-8): "

if "%choice%"=="1" goto TABLES
if "%choice%"=="2" goto USERS
if "%choice%"=="3" goto MEDICINES
if "%choice%"=="4" goto INVENTORY
if "%choice%"=="5" goto ORDERS
if "%choice%"=="6" goto PRESCRIPTIONS
if "%choice%"=="7" goto SHELL
if "%choice%"=="8" exit /b
goto MENU

:TABLES
cls
echo ===============================================================
echo                    TABLE SUMMARY & ROW COUNTS
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SHOW TABLES; SELECT 'users' AS 'Table Name', COUNT(*) AS 'Total Records' FROM users UNION ALL SELECT 'medicines', COUNT(*) FROM medicines UNION ALL SELECT 'pharmacies', COUNT(*) FROM pharmacies UNION ALL SELECT 'pharmacy_inventory', COUNT(*) FROM pharmacy_inventory UNION ALL SELECT 'orders', COUNT(*) FROM orders UNION ALL SELECT 'order_items', COUNT(*) FROM order_items UNION ALL SELECT 'prescriptions', COUNT(*) FROM prescriptions;"
echo.
pause
goto MENU

:USERS
cls
echo ===============================================================
echo                       REGISTERED USERS
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SELECT user_id, full_name, email, role, created_at FROM users;"
echo.
echo Note: Passwords are securely hashed using SHA-256 with random salt.
echo.
pause
goto MENU

:MEDICINES
cls
echo ===============================================================
echo                     MEDICINE CATALOG
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SELECT medicine_id, name, category, price, prescription_required AS 'Rx_Req' FROM medicines;"
echo.
pause
goto MENU

:INVENTORY
cls
echo ===============================================================
echo                PHARMACY INVENTORY & STOCK
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SELECT p.name AS 'Pharmacy', m.name AS 'Medicine', pi.available_quantity AS 'Stock', pi.price AS 'Unit Price' FROM pharmacy_inventory pi JOIN pharmacies p ON pi.pharmacy_id = p.pharmacy_id JOIN medicines m ON pi.medicine_id = m.medicine_id ORDER BY p.name, m.name;"
echo.
pause
goto MENU

:ORDERS
cls
echo ===============================================================
echo                     CUSTOMER ORDERS
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SELECT order_id, customer_name, customer_phone, pharmacy_name, total_amount, order_status, created_at FROM orders ORDER BY created_at DESC;"
echo.
echo Order Items Detail:
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SELECT order_id, medicine_name, quantity, price_at_order FROM order_items;"
echo.
pause
goto MENU

:PRESCRIPTIONS
cls
echo ===============================================================
echo                      PRESCRIPTIONS
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! -t medicart_db -e "SELECT prescription_id, customer_name, file_name, verification_status, reviewer_note, created_at FROM prescriptions;"
echo.
pause
goto MENU

:SHELL
cls
echo ===============================================================
echo           OPENING INTERACTIVE MYSQL TERMINAL
echo           Type: SHOW TABLES; or SELECT * FROM users;
echo           Type 'exit' to return to menu.
echo ===============================================================
echo.
"%MYSQL_PATH%" -u medicart_app -pMediCartApp2026! medicart_db
goto MENU
