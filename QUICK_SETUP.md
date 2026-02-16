# Quick Setup Guide for VM

This guide is for setting up the Money Transfer System in a VM environment where you're pulling code from GitHub.

## Prerequisites
- MySQL 8.x installed and running
- Java 17+ installed
- Maven installed (or use the included Maven wrapper)

## Setup Steps

### 1. Clone/Pull the Repository
```bash
git pull origin main
# or
git clone <your-repo-url>
cd Fidelity-leap-springboot
```

### 2. Setup MySQL Database

**Open MySQL Workbench or MySQL Command Line:**

```sql
CREATE DATABASE money_transfer_db;
```

### 3. Run Schema Script

**In MySQL Workbench:**
1. File → Open SQL Script
2. Select `database/schema.sql`
3. Click Execute (⚡ icon)

**OR via Command Line:**
```bash
mysql -u root -p money_transfer_db < database/schema.sql
```

### 4. Run Seed Data Script

**In MySQL Workbench:**
1. File → Open SQL Script
2. Select `database/seed-data.sql`
3. Click Execute (⚡ icon)

**OR via Command Line:**
```bash
mysql -u root -p money_transfer_db < database/seed-data.sql
```

✅ **No password hash generation needed!** The BCrypt hashes are already included in the SQL file.

### 5. Configure Application

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/money_transfer_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: YOUR_MYSQL_PASSWORD  # Change this to your MySQL password
```

### 6. Build and Run

**Using Maven:**
```bash
mvn clean install
mvn spring-boot:run
```

**Using Maven Wrapper (if Maven not installed):**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 7. Access the Application

Open your browser and go to:
```
http://localhost:8080
```

## Default Login Credentials

### Admin Account
- **Email:** admin@system.com
- **Password:** admin123

### Test User Accounts (Approved)
- **Email:** john@example.com | **Password:** user123
- **Email:** jane@example.com | **Password:** user123

### Test User (Pending Approval)
- **Email:** bob@example.com | **Password:** user123

## Testing the Application

### 1. Login as Admin
1. Go to http://localhost:8080/login.html
2. Login with admin@system.com / admin123
3. You'll see the admin dashboard

### 2. Approve Pending User
1. In admin dashboard, see "Bob Johnson" in pending approvals
2. Click "Approve"
3. Bob can now login

### 3. Login as User
1. Logout from admin
2. Login with john@example.com / user123
3. You'll see the user dashboard with balance ₹5000

### 4. Transfer Money
1. In user dashboard, enter:
   - To Account ID: 3 (Jane's account)
   - Amount: 500
2. Click "Transfer"
3. Check transaction history

### 5. Admin Deposit
1. Login as admin
2. Go to "Deposit Money" section
3. Enter Account ID: 2, Amount: 1000
4. Click "Deposit"

## Troubleshooting

### Build Fails - JAVA_HOME not set
```bash
# Check Java version
java -version

# If not installed, install Java 17
# Then set JAVA_HOME (Linux/Mac)
export JAVA_HOME=/path/to/java17
export PATH=$JAVA_HOME/bin:$PATH
```

### MySQL Connection Failed
- Check MySQL is running: `sudo systemctl status mysql`
- Verify credentials in `application.yaml`
- Ensure database `money_transfer_db` exists

### Port 8080 Already in Use
Change port in `application.yaml`:
```yaml
server:
  port: 8081  # or any other available port
```

## Security Notes

The BCrypt password hashes in the seed data are:
- **admin123** → `$2a$10$8cjz95BCg3xLL95xMeIgAOidoQd0mW9GvVPvb4b6RZ.WaIxPVq/Oi`
- **user123** → `$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG13AXN4dON1lKMy2S`

These are proper BCrypt hashes with strength 10 (standard security). They are safe to use for development and testing. For production, change the admin password after first login.
