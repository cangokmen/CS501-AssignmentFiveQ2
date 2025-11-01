# Assignment Five – Q2: My Daily Hub

## Overview
**My Daily Hub** is a productivity app built with **Jetpack Compose Navigation**, **BottomNavigation**, and **ViewModel**.  
It provides three main screens — **Notes**, **Tasks**, and **Calendar** — allowing users to organize daily activities while maintaining screen state and proper navigation behavior.

---

## Features
- 📝 **Notes Screen** – Create and view text notes  
- ✅ **Tasks Screen** – Manage a list of checkable tasks  
- 📅 **Calendar Screen** – Static placeholder for future calendar integration  
- 🧭 **Bottom Navigation** – Uses `BottomNavigation` and `BottomNavigationItem` with dynamic highlighting via `currentBackStackEntryAsState()`  
- 🔄 **Navigation Architecture** – Controlled with:
  - `popUpTo()` for stack control  
  - `launchSingleTop` to prevent duplicates  
  - `restoreState = true` to maintain previous state  
- 💾 **State Preservation** – Each screen uses its own `ViewModel` or hoisted state to persist data during recompositions  
- 💡 **Icons & Animation** – Adds `Icons.Default.*` to nav items and applies animated transitions between screens  

---

## How Navigation Works
- A sealed `Routes` class defines routes for all three screens.  
- `NavHost` and `NavController` manage transitions while keeping a single instance per screen.  
- The app ensures only one instance of each screen exists in the backstack (`launchSingleTop = true`).  
- Pressing the **back button** behaves intuitively:
  - Navigates to the previous tab if possible  
  - Exits the app from the start destination if already on the Home tab  

---

## How to Run and Use
```bash
git clone https://github.com/cangokmen/CS501-AssignmentFiveQ2
# Open in Android Studio and run on an emulator or device
```
- Tap the Notes, Tasks, or Calendar icons in the bottom bar to switch screens.
- Notes – Add or edit simple text notes.
- Tasks – Check off or uncheck items from your daily task list.
- Calendar – View the static placeholder page.
- Switch between tabs freely; your screen state (notes and tasks) will remain intact.

## AI Assistance Documentation
I implemented everything up to composables manually. I then asked Gemini to generate the composables according to the definitions I created. It failed in adding add and remove functionality to both tasks and notes. To tackle this, I worked collaboratively with Gemini. I asked some minor stuff when I got stuck. Gemini also generated the README
