# 🔎 SIEM (Security Information and Event Management)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=firebase)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white)
![Apache Ant](https://img.shields.io/badge/Apache%20Ant-A81C7D?style=for-the-badge&logo=apache&logoColor=white)
![License: MIT](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)

## 📌 Overview
This project is a **Security Information and Event Management (SIEM) system** built in **Java**.  
It collects system and application logs, parses them, and provides enhanced log viewing for security analysis.

The project simulates log generation, aggregation, and parsing, enabling analysts to monitor and detect suspicious activities.

---

## ⚙️ Features
- 🖥 **System Log Collection** (Windows & custom logs)  
- 📑 **Application Log Generation**  
- 🔗 **Log Aggregation** (push & pop models)  
- 🔍 **Log Parsing and Viewing**  
  - Enhanced SIEM Log Viewer  
  - Filter and search support  
- 🛠 **Simulated Attack Events** for testing detection  
- ☁️ **Google Firebase Integration** (for remote logging & monitoring)  

---

## 🏗 Project Structure
```
SIEM/
├── src/                  # Source code
│   ├── Agents/           # Log Readers & Agents
│   ├── Aggregator/       # Aggregation classes
│   ├── Parser/           # Log Parsers & Viewers
│   └── siem/Util/        # Utilities (Log Generators, Simulation)
├── build/                # Compiled classes
├── lib/                  # Required JAR dependencies
├── nbproject/            # NetBeans project configs
├── build.xml             # Ant build file
└── manifest.mf           # Manifest
```

---

## 🚀 Getting Started

### 🔧 Prerequisites
- Java JDK 8+  
- Apache Ant (for build.xml)  
- NetBeans IDE (recommended)  

### 📥 Clone the Repository
```bash
git clone https://github.com/your-username/SIEM.git
cd SIEM
```

### ▶️ Running the Project
Using **NetBeans**:  
1. Open the project in NetBeans.  
2. Build and run.  

Using **Ant**:  
```bash
ant run
```

---

## 🔑 Configuration
- The project requires a **Firebase Admin SDK key** (`.json`).  
  ⚠️ For security reasons, **do not commit your key** to GitHub.  
- Store it safely and load it using an environment variable or configuration file outside version control.  

---

## 🧪 Example Use Cases
- Simulate system & application logs.  
- Detect anomalies or unusual patterns.  
- Integrate with cloud logging (Firebase).  

---

## 📂 Dependencies
The `lib/` folder contains required JARs, including:  
- Firebase Admin SDK  
- Google Cloud libraries  
- Apache Commons & Logging  
- Netty, gRPC, Guava, Gson  

---

## 🤝 Contributing
Pull requests are welcome!  
For major changes, open an issue first to discuss what you’d like to change.  

---

## 📜 License
This project is licensed under the MIT License.  
