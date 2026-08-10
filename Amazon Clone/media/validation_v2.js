function validate() {
    // Get all input values using the form name 'myform'
    let name = document.forms["myform"]["name"].value;      // name
    let password = document.forms["myform"]["password"].value;  // password
    let email = document.forms["myform"]["email"].value;    // email
    let phone = document.forms["myform"]["phone"].value;    // phone

    let text1 = ""; // Name errors (p1)
    let text2 = ""; // Email errors (p2)
    let text3 = ""; // Password errors (p3)
    let text4 = ""; // Phone errors (p4)

    // // Clear previous errors on the page
    // document.getElementById('p1').innerHTML = "";
    // document.getElementById('p2').innerHTML = "";
    // document.getElementById('p3').innerHTML = "";
    // document.getElementById('p4').innerHTML = "";

    // 1. NAME VALIDATION
    if (name == '') {
        text1 += "Name field empty!\n";
    } else {
        for (let i = 0; i < name.length; i++) {
            let ch = name[i].toLowerCase();
            //Only letters allowed
            if (!((ch >= 'a' && ch <= 'z') || name[i] === ' ')) {
                text1 += "Name cannot contain Numbers or Special Characters\n";
                break;
            }
        }
    }

    // 2. PASSWORD VALIDATION
    let u_count = 0;
    let n_count = 0;

    if (password == '') {
        text3 += "Password cannot be empty\n";
    } else {
        if (password.length < 8) {
            text3 += "Password should be atleast 8 characters\n";
        }
        for (let i = 0; i < password.length; i++) {
            if (password[i] >= 'A' && password[i] <= 'Z')
                u_count++;
            //Checking if character is between '0' and '9'
            if (password[i] >= '0' && password[i] <= '9')
                n_count++;
        }
        if (u_count == 0)
            text3 += "Password should contain atleast 1 Upper-Case letter\n";
        if (n_count == 0)
            text3 += "Password should contain atleast 1 Number\n";
    }

    // 3. PHONE NUMBER VALIDATION
    if (phone == '') {
        text4 += "Phone Number cannot be empty\n";
    } else {
        if (phone.length != 10) {
            text4 += "Phone Number should contain exactly 10 digits\n";
        }
        for (let i = 0; i < phone.length; i++) {
            //Check if character is NOT a digit
            if (!(phone[i] >= '0' && phone[i] <= '9')) {
                text4 += "Phone Number should contain only digits\n";
                break;
            }
        }
    }

    // 4. EMAIL VALIDATION (Using your original complex logic)
    if (email == '') {
        text2 += "Email cannot be empty\n";
    } else {
        if (!email.includes('@'))
            text2 += "Email should include '@'\n";
        if (!email.includes('.'))
            text2 += "Email should include '.'\n";

        if (email.includes('@') && email.includes('.')) {
            //Dot position relative to @ (Must be at least two chars after @, e.g., a@b.c)
            if (email.lastIndexOf('.') < email.indexOf('@') + 2)
                text2 += "Invaid format for email! (.) must follow @ by at least two characters.\n";
            
            if (email.lastIndexOf('@') > email.indexOf('.')) {
                text2 += "invalid email format: last @ should come before (.)\n ";
            }

            //There should be text after (.)
            if (email.lastIndexOf('.') == email.length - 1) {
                text2 += "there should be text after (.)\n";
            }
        }
    }

    // FINAL RESULT & REDIRECTION

    // Combine all error messages
    const allErrors = text1 + text2 + text3 + text4;

    if (allErrors === '') {
        // SUCCESS: Redirect to main page
        alert('Form Validated Successfully! Redirecting to the Amazon clone page.');
        window.location.href = 'amazon.html'; 
        
    } else {
        // FAILURE: Display individual error messages next to their fields
        document.getElementById('p1').innerHTML = text1;
        document.getElementById('p2').innerHTML = text2;
        document.getElementById('p3').innerHTML = text3;
        document.getElementById('p4').innerHTML = text4;   
    }
}