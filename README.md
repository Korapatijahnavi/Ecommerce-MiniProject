# 🛒 E-Commerce Mini Project

An Android grocery shopping application developed as part of an **Android Internship E-Commerce Mini Project**.

The application is built using **Java, XML, MVVM, Retrofit, and DummyJSON API** and includes authentication, product browsing, category filtering, product details, cart management, checkout, and a simulated payment flow.

Product data is retrieved from the free **DummyJSON REST API**.

---

## ✨ Features

### 🔐 Authentication

* Splash screen with session checking
* First-launch flow: Splash → Intro → Login → OTP
* Subsequent launches skip login when a session exists
* 10-digit mobile number validation
* Numeric keyboard for mobile number input
* 4-digit static OTP verification
* Mobile number passed between screens using `Intent` extras
* Session management using `SharedPreferences`

### 🏠 Home

* Special offers displayed using a horizontal `RecyclerView`
* Categories displayed using a 4-column grid
* Best deals displayed using a 2-column grid
* Clickable **View All** options
* Bottom navigation

### 🛍️ Products

* Grocery product listing
* Search functionality
* Category filtering
* Reusable category tabs
* Product grid using `RecyclerView`
* Product quantity controls

### 📦 Product Details

* Product images
* Price and discount information
* Product rating
* Stock availability
* Warranty information
* Shipping information
* Product description
* Add to Cart functionality

### 🛒 Cart

* Add products to cart
* Increase/decrease quantity
* Remove products
* Live subtotal and total calculation
* Cart item count/badge
* Shared cart data across screens

### 🚚 Checkout

* Delivery details form
* Name, mobile number and address validation
* City, state and pincode validation
* Address type selection
* Payment method selection
* Order confirmation flow

### 💳 Payment

* Simulated payment flow
* Payment success screen
* Cart cleared after successful payment
* Automatic navigation back to Home

---

## 🛠️ Tech Stack

| Technology               | Usage                      |
| ------------------------ | -------------------------- |
| **Java**                 | Application development    |
| **XML**                  | UI layouts                 |
| **MVVM**                 | Application architecture   |
| **ViewModel & LiveData** | UI state management        |
| **RecyclerView**         | Product and category lists |
| **Retrofit 2**           | REST API communication     |
| **Gson**                 | JSON parsing               |
| **Glide**                | Image loading              |
| **SharedPreferences**    | Session management         |
| **Material Components**  | UI components              |


---



## 🏗️ Architecture

The application follows the **MVVM (Model–View–ViewModel)** architecture.

```text
Activity / Fragment
        │
        ▼
    ViewModel
        │
        ▼
   Repository
        │
        ▼
   Retrofit API
        │
        ▼
    DummyJSON
```

### Main Components

* **Activity / Fragment** — Displays the UI and handles user interactions
* **ViewModel** — Manages screen-related data and UI state
* **Repository** — Handles data operations and API communication
* **Adapter** — Displays data in `RecyclerView`


---

## 🌐 API

Product data is retrieved from the **DummyJSON REST API**.

**Base URL**

```text
https://dummyjson.com/
```

### Endpoints

| Purpose          | Method | Endpoint                               |
| ---------------- | ------ | -------------------------------------- |
| Grocery products | GET    | `/products/category/groceries?limit=0` |
| Product details  | GET    | `/products/{id}`                       |

The application groups grocery products into categories using keywords from the product titles.

Prices received from the API are converted from USD to INR using a fixed conversion rate.

> **Note:** The currency conversion is for display purposes only and is not a live exchange rate.

---

## 🔄 App Flow

### First Launch

```text
Splash
   ↓
Intro
   ↓
Login
   ↓
OTP Verification
   ↓
Home
```

### Returning User

```text
Splash
   ↓
Home
```

### Shopping Flow

```text
Home
  ↓
Products
  ↓
Product Details
  ↓
Cart
  ↓
Delivery / Checkout
  ↓
Payment
  ↓
Payment Successful
  ↓
Home
```

---

## 🔗 Screen-to-Screen Data Passing

| From     | To              | Data Passed   |
| -------- | --------------- | ------------- |
| Login    | OTP             | Mobile number |
| OTP      | Home            | Mobile number |
| Products | Product Details | Product ID    |

The product ID is passed to the Product Details screen and used to request the selected product from the API.

---




---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Korapatijahnavi/Ecommerce-MiniProject.git
```

### 2. Open the Project

Open the cloned project in **Android Studio** and allow Gradle to sync.

### 3. Run the Application

Connect an Android device or start an emulator and click **Run ▶**.

An internet connection is required to retrieve product data from the API.

---

## 🔑 Demo Login

| Field         | Value                     |
| ------------- | ------------------------- |
| Mobile Number | Any valid 10-digit number |
| OTP           | `1234`                    |

---

## ⚠️ Limitations

* Authentication is simulated.
* OTP is static (`1234`).
* Payment processing is simulated.
* No real payment gateway is integrated.
* Cart data is stored in memory.
* Cart data may be cleared if the application process is terminated.
* Product data depends on the DummyJSON API.
* Currency conversion uses a fixed rate of **1 USD = ₹83**.

---

## 🎥 Demo

### APK

[Download APK](#)

### Screen Recording

[Watch Screen Recording](#)


---

## 🎨 UI Design

The application UI was developed based on the provided Figma design.

[https://www.figma.com/design/bdpUFeWEIsYfxxypOoFH6u/E-Commerce-Task---Mini-Project---002?node-id=0-1](#)



---

## 👩‍💻 Author

### Sai Jahnavi Korapati


* GitHub: [Korapatijahnavi](https://github.com/Korapatijahnavi)
* LinkedIn: [Sai Jahnavi Korapati](https://www.linkedin.com/in/korapati-sai-jahnavi-78b947256/)

---

