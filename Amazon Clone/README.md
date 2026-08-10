# Enterprise-Grade E-Commerce Frontend & Authentication Engine

A full-scale, pixel-accurate frontend replication of Amazon's e-commerce platform coupled with a custom, secure client-side form validation and state-handling authentication module. Built using vanilla HTML5, modern CSS3 (Flexbox/Grid architectures), and robust JavaScript validation algorithms.

---

## Key Features

### 1. E-Commerce Dashboard & Layout (`amazon.html` / `amazon.css`)
* **Responsive Navigation Bar:** Replicates Amazon’s multi-tier top navigation, featuring country delivery indicators, searchable dropdown category selectors, language selectors, account lists, and cart summaries.
* **Dynamic Grid & Flexbox Layouts:** Implements structured product display grids (`.shopping`, `.box`) showcasing home decor, major appliances, audio accessories, and electronics with precise spacing and asset sizing.
* **Interactive UI Elements:** Features multi-row promotional blocks, image-based product tiles, custom badge positioning, and a complete footer architecture mimicking production e-commerce standards.

### 2. Secure User Authentication & Registration (`registration.html` / `validation_v2.js`)
* **Custom Validation Engine:** Built entirely in vanilla JavaScript without external libraries to enforce strict client-side data integrity:
  * **Name Validation:** Restricts input strictly to alphabetical characters and spaces, preventing numerical or special character injections.
  * **Phone Number Verification:** Enforces precise 10-digit numerical length checks.
  * **Password Complexity Enforcement:** Mandates a minimum of 8 characters containing at least one uppercase letter and one numeric digit.
  * **Advanced Email Parsing:** Custom algorithmic checks verifying `@` positioning, dot placement rules, trailing domain text, and sequence integrity.
* **Dynamic Error Handling & Redirection:** Real-time DOM error rendering (`.error-message`) mapped directly to corresponding fields, with automatic conditional redirection upon successful validation.

---

## Project Structure

```text
├── amazon.html             # Main e-commerce landing page & dashboard
├── amazon.css              # Comprehensive styling for the main dashboard & navbar
├── registration.html       # Amazon-style account creation & sign-up portal
├── registration_v2.css     # Clean, isolated stylesheet for the registration form
├── validation_v2.js        # Core JavaScript validation and conditional routing logic
└── assets/                 # Directory containing required images, flags, and UI badges
```

---

## Technologies Used

* **Markup:** HTML5 (Semantic elements, form structures, DOM hierarchies)
* **Styling:** CSS3 (Flexbox, custom pseudo-classes, multi-viewport media queries, modular class encapsulation)
* **Logic:** Vanilla JavaScript (Algorithmic string parsing, event handling, conditional routing, DOM manipulation)
* **Icons:** FontAwesome 6 CDN

---

## Getting Started

1. Clone or download the repository to your local machine.
2. Ensure all asset files (`.jpg`, `.png`) are placed in the root directory matching the stylesheet path references.
3. Open `registration.html` in any modern web browser to test the interactive authentication flow and validation rules.
4. Upon successful validation, the application automatically routes to `amazon.html` to load the primary e-commerce dashboard.

---

## License

This project is developed for educational and portfolio demonstration purposes.
