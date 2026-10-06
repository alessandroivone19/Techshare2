# TechShare

**TechShare is a web application designed to connect people who own technological equipment with people who need it temporarily.**

The project was developed during a **4-month training program at Generation Italy**, as a collaborative project with other students.

The idea behind TechShare is based on the concept of the **sharing economy**, applied to the technology sector: instead of purchasing equipment that may only be needed occasionally, users can find and contact people nearby who are willing to make their equipment available.

## 💡 The Idea

Many technological tools can be expensive to purchase and are often used only occasionally.

For example, someone may need a **3D printer** for a single project but may not want to buy one. At the same time, another person may own a 3D printer that they rarely use.

TechShare connects these two users.

A user can publish an equipment listing, while another user can search for available equipment nearby and contact the owner directly.

## ⚙️ How It Works

TechShare supports two main types of users:

- **Owner** – publishes and manages their available equipment.
- **Requester** – searches for equipment based on their needs.

The same user can perform both roles.

### Equipment Listings

An owner can publish an equipment listing containing information such as:

- Title
- Category
- Description
- Photos
- Possible uses
- Location
- Geographical coordinates
- Contact phone number
- Availability

Users can then search for equipment and find the resources available in their area.

### 📍 Geolocation

One of the main features of TechShare is **location-based search**.

The application uses geographical coordinates to determine the distance between users and available equipment. Users can therefore search for equipment within a specific area and view results ordered according to their distance.

The project integrates external geolocation/map services to support this functionality.

### 📞 Contact Between Users

Once a user finds the desired equipment, they can contact the owner directly using the phone number provided on the platform.

The actual communication and agreement take place outside the application, for example through a phone call or WhatsApp.

The rental price and usage conditions are agreed privately between the users.

## 🏗️ Architecture

TechShare follows a **client-server architecture** divided into three main layers:
```
┌─────────────────────────┐
│        Angular          │
│        Frontend         │
└────────────┬────────────┘
             │
          REST API
             │
┌────────────▼────────────┐
│      Spring Boot        │
│         Backend         │
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│         MySQL           │
│        Database         │
└─────────────────────────┘
```

The frontend is responsible for the user interface, authentication, equipment search, equipment visualization and maps.

The backend manages the application logic, users, authentication, equipment and geolocation-based search.

The database stores users, equipment, geographical coordinates and contact information.

## 🛠️ Technologies

### Backend

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- MySQL

### Frontend

- Angular
- TypeScript
- HTML
- CSS

### External Services

- Browser Geolocation API
- OpenStreetMap / Google Maps API

## 🔐 Authentication

The application includes user registration and authentication.

Authentication is handled on the backend using **Spring Security and JWT (JSON Web Tokens)**.

Authenticated users can manage their profile and interact with the equipment listings.

## 🗄️ Data Model

The main entities of the application are:

### User

Contains information such as:

- ID
- First name
- Last name
- Email
- Password
- Phone number
- City
- Registration date

### Equipment

Contains information such as:

- ID
- Title
- Description
- Category
- Photo
- Possible uses
- Availability
- Latitude
- Longitude
- Owner

The relationship between the entities is:
```
USER (1) ──────────── (N) EQUIPMENT
```

A user can therefore own multiple pieces of equipment.

## 🚀 Main Features

- User registration and authentication
- JWT-based authentication
- User profile management
- Equipment listing creation
- Equipment listing editing
- Equipment listing deletion
- Equipment availability management
- Geolocation-based search
- Distance-based equipment ordering
- Map integration
- Direct contact between users

## 👥 Project

TechShare was developed as a **team project during the Generation Italy training program**.

The project involved the analysis of requirements, database design, backend development, REST API implementation, frontend development, authentication, geolocation and testing.

## 🔮 Future Improvements

Possible future developments include:

- User reviews and ratings
- Equipment reservation system
- Real-time notifications
- User identity verification
- Mobile application
- Direct WhatsApp integration

## 🎯 Goal

The main goal of TechShare is to make technological equipment **more accessible, affordable and sustainable** by connecting people who own equipment with people who need it temporarily.

> **Share technology. Reduce waste. Connect people.**
