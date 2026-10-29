#!/bin/bash
# MediCart Database Viewer for macOS / Linux

cd "$(dirname "$0")"

# Find mysql command
MYSQL_CMD="mysql"
if ! command -v mysql &> /dev/null; then
    if [ -f "/usr/local/mysql/bin/mysql" ]; then
        MYSQL_CMD="/usr/local/mysql/bin/mysql"
    elif [ -f "/opt/homebrew/bin/mysql" ]; then
        MYSQL_CMD="/opt/homebrew/bin/mysql"
    else
        echo "ERROR: 'mysql' command not found in PATH or standard macOS locations."
        echo "Please install MySQL or add it to PATH."
        exit 1
    fi
fi

# Read credentials from db.properties if exists
DB_USER="medicart_app"
DB_PASS="MediCartApp2026!"
DB_NAME="medicart_db"

if [ -f "database/db.properties" ]; then
    PROP_USER=$(grep -E "^db.user=" database/db.properties | cut -d'=' -f2 | tr -d '\r\n ')
    PROP_PASS=$(grep -E "^db.password=" database/db.properties | cut -d'=' -f2 | tr -d '\r\n ')
    [ -n "$PROP_USER" ] && DB_USER="$PROP_USER"
    [ -n "$PROP_PASS" ] && DB_PASS="$PROP_PASS"
fi

while true; do
    clear
    echo "==============================================================="
    echo "            MEDICART DATABASE VIEWER (macOS)"
    echo "==============================================================="
    echo "Database: $DB_NAME   |   User: $DB_USER"
    echo ""
    echo "  [1] Show All Database Tables & Row Counts"
    echo "  [2] Show Registered Users (with Salted Hash Passwords)"
    echo "  [3] Show Medicine Catalog & Pricing"
    echo "  [4] Show Pharmacies & Stock Inventory"
    echo "  [5] Show Orders & Order Details"
    echo "  [6] Show Prescriptions"
    echo "  [7] Open Interactive MySQL Terminal"
    echo "  [8] Exit"
    echo ""
    echo "==============================================================="
    read -p "Enter your choice (1-8): " choice

    case $choice in
        1)
            clear
            echo "================== TABLES & ROW COUNTS =================="
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SHOW TABLES; SELECT 'users' AS 'Table Name', COUNT(*) AS 'Total Records' FROM users UNION ALL SELECT 'medicines', COUNT(*) FROM medicines UNION ALL SELECT 'pharmacies', COUNT(*) FROM pharmacies UNION ALL SELECT 'pharmacy_inventory', COUNT(*) FROM pharmacy_inventory UNION ALL SELECT 'orders', COUNT(*) FROM orders UNION ALL SELECT 'order_items', COUNT(*) FROM order_items UNION ALL SELECT 'prescriptions', COUNT(*) FROM prescriptions;"
            read -p "Press Enter to continue..."
            ;;
        2)
            clear
            echo "=================== REGISTERED USERS ==================="
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT user_id, full_name, email, role, created_at FROM users;"
            echo ""
            echo "Note: Passwords are encrypted using SHA-256 with random salt."
            read -p "Press Enter to continue..."
            ;;
        3)
            clear
            echo "=================== MEDICINE CATALOG ==================="
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT medicine_id, name, category, price, prescription_required AS 'Rx_Req' FROM medicines;"
            read -p "Press Enter to continue..."
            ;;
        4)
            clear
            echo "============= PHARMACIES & STOCK INVENTORY ============="
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT p.name AS 'Pharmacy', m.name AS 'Medicine', pi.available_quantity AS 'Stock', pi.price AS 'Unit Price' FROM pharmacy_inventory pi JOIN pharmacies p ON pi.pharmacy_id = p.pharmacy_id JOIN medicines m ON pi.medicine_id = m.medicine_id ORDER BY p.name, m.name;"
            read -p "Press Enter to continue..."
            ;;
        5)
            clear
            echo "==================== CUSTOMER ORDERS ===================="
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT order_id, customer_name, customer_phone, pharmacy_name, total_amount, order_status, created_at FROM orders ORDER BY created_at DESC;"
            echo ""
            echo "Order Items Detail:"
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT order_id, medicine_name, quantity, price_at_order FROM order_items;"
            read -p "Press Enter to continue..."
            ;;
        6)
            clear
            echo "==================== PRESCRIPTIONS ===================="
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT prescription_id, file_path, verification_status, reviewer_note, created_at FROM prescriptions;"
            read -p "Press Enter to continue..."
            ;;
        7)
            clear
            echo "Opening interactive MySQL shell (type 'exit' to return)..."
            "$MYSQL_CMD" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME"
            ;;
        8)
            exit 0
            ;;
        *)
            echo "Invalid choice. Please enter 1-8."
            sleep 1
            ;;
    esac
done
