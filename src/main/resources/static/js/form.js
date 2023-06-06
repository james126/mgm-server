const form = document.getElementById('contact-form');
form.addEventListener('submit', submitForm);

/**
 * Check form validity and POST form data to server
 * @param event form submission
 */
function submitForm(event) {
    event.preventDefault();     //no request is sent to server

    if (!form.checkValidity()) {    //checkValidity is built in browser method
        form.classList.add('was-validated');    //adds invalid feedback
    } else {
        const formData = new FormData(event.target);
        const formObj = Object.fromEntries(formData.entries());

        fetch('http://localhost:8080/form', {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify(formObj)
        }).then(function (response) {
            return response.status === 200
        }).then(function (response) {
            confirmSubmission();
        }).catch(function (error) {
            console.log('Request failed', error);
        });
    }
}

/**
 * When server response is OK, disable input fields and display confirmation message
 */
function confirmSubmission() {
    //disable all input elements
    const fields = document.getElementsByTagName('input');
    const array = Array.from(fields);
    Array.from(fields).forEach(element => element.disabled = true);

    //display confirmation message
    const paragraph = document.createElement("p");
    paragraph.classList.add('text-center', 'lead', 'submitted-text');
    paragraph.innerText = 'Thanks, we will be in touch shortly!';

    const brk = document.createElement("br");
    paragraph.appendChild(brk);

    const horizontal = document.getElementsByTagName("hr");
    document.body.insertBefore(paragraph, form);
}
