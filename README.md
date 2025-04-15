# 🔐 SecretNote – Encrypted Message & File Sharing Backend (Spring Boot)

This is the backend for **SecretNote** – a minimal, anonymous and secure note/file sharing service. Users can create
encrypted messages or files that self-destruct after a single view or after a defined time.

Frontend: [Angular 19 + Tailwind CSS](https://github.com/Alex777x/sharethesecret)  
Backend: Java 21, Spring Boot 3.x

---

## 🧰 Technologies Used

- Java 21
- Spring Boot 3.x
- REST API (Spring Web)
- Redis (or in-memory fallback)
- UUID for note identification
- Multipart file handling
- (Optional) Bouncy Castle or JCA for server-side encryption

---

## 📦 Features

- Anonymous note and file storage (max 2 MB)
- No authentication, no user data
- Self-destruct on view or time expiration (TTL)
- RESTful API for client-side encrypted content
- Optional password-protected decryption (client-side)

---

## 🚀 Running the Backend

### Requirements

- Java 21+
- Maven 3.x
- Redis (optional, but recommended for production)

### Setup

```bash
git clone https://github.com/yourusername/secretnote-backend.git
```

```bash
cd secretnote-backend
```

Run with Maven

```bash
./mvnw spring-boot:run
```

The API will be available at:
http://localhost:8080/api/notes

⸻

🧪 Testing

You can use Postman or curl to test endpoints:

curl -X POST http://localhost:8080/api/notes \
-F content="hello world" \
-F algorithm="AES-256" \
-F ttl="1h"

⸻

📄 License

This project is open-source and licensed under the MIT License.

⸻

🧠 Notes     
• All encryption should be performed on the client side (frontend).     
• The server never sees the secret key or password.     
• Self-destruction is handled either immediately upon access or after a TTL via Redis expiration or a scheduler.        
